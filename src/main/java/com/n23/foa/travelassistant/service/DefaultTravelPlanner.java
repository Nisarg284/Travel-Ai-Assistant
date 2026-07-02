package com.n23.foa.travelassistant.service;

import com.n23.foa.travelassistant.agents.ItineraryPlanner;
import com.n23.foa.travelassistant.agents.TravelPlannerAgent;
import com.n23.foa.travelassistant.dto.TripConstraints;
import com.n23.foa.travelassistant.dto.ValidationResult;
import com.n23.foa.travelassistant.extractors.TripConstraintsExtractor;
import com.n23.foa.travelassistant.retriever.meltisection_retriever.MultiSectionRetriever;
import dev.langchain4j.rag.content.Content;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultTravelPlanner implements TravelPlannerAgent {

    private final TripConstraintsExtractor tripConstraintsExtractor;
    private final MultiSectionRetriever multiSectionRetriever;
    private final ItineraryPlanner itineraryPlanner;
    private final TripFeasibilityValidator tripFeasibilityValidator;

    /**
     * Per-session constraints cache.
     *
     * Jab user pehli baar "Plan a 5-day Goa trip under ₹30k" bolta hai,
     * constraints { Goa, 5, 30000 } yahan cache ho jaate hain.
     *
     * Jab baad mein "make it cheaper" bolta hai, extractor se null aata hai,
     * toh cached constraints se { Goa, 5, 30000 } reuse ho jaate hain.
     */
    private final Map<String, TripConstraints> constraintsCache = new ConcurrentHashMap<>();

    public DefaultTravelPlanner(
            TripConstraintsExtractor tripConstraintsExtractor,
            MultiSectionRetriever multiSectionRetriever,
            ItineraryPlanner itineraryPlanner,
            TripFeasibilityValidator tripFeasibilityValidator) {
        this.tripConstraintsExtractor = tripConstraintsExtractor;
        this.multiSectionRetriever = multiSectionRetriever;
        this.itineraryPlanner = itineraryPlanner;
        this.tripFeasibilityValidator = tripFeasibilityValidator;
    }

    @Override
    public String plan(String sessionId, String question) {

        // Step 1: Try extracting constraints from the current message
        TripConstraints extracted = tripConstraintsExtractor.extract(question);

        // Step 2: Merge with cached constraints (handles follow-ups like "make it
        // cheaper")
        TripConstraints constraints = resolveConstraints(sessionId, extracted);

        // Step 3: If still no valid constraints → ask user for details
        if (constraints == null) {
            return "Please provide trip details: destination, number of days, and budget.";
        }

        // Step 4: Cache the resolved constraints for future follow-ups
        constraintsCache.put(sessionId, constraints);

        // Step 5: Continue normal pipeline — retrieve → validate → plan
        List<Content> contents = multiSectionRetriever.retrieve(constraints);

        ValidationResult validator = tripFeasibilityValidator.validate(constraints, contents);

        if (!validator.feasible()) {
            return """
                    Trip not feasible.

                    Reason:
                    %s

                    Suggestion:
                    %s
                    """.formatted(
                    validator.reason(),
                    validator.suggestion());
        }

        System.out.println(constraints);
        System.out.println(validator);

        String plan = itineraryPlanner.plan(sessionId, constraints, contents, validator);

        System.out.println(plan);
        return plan;
    }

    /**
     * Extracted constraints ko cached constraints ke saath merge karta hai.
     *
     * Case 1: "Plan 5-day Goa trip ₹30k" → extracted = { Goa, 5, 30000 }
     * → No cache exists → return extracted as-is
     *
     * Case 2: "make it cheaper" → extracted = { null, null, null }
     * → Cache has { Goa, 5, 30000 } → return cached
     *
     * Case 3: "change to 3 days" → extracted = { null, 3, null }
     * → Cache has { Goa, 5, 30000 } → return { Goa, 3, 30000 }
     * (sirf days update hua, baaki cached se aaya)
     */
    private TripConstraints resolveConstraints(String sessionId, TripConstraints extracted) {

        TripConstraints cached = constraintsCache.get(sessionId);

        // No cache → first request for this session
        if (cached == null) {
            return isUsable(extracted) ? extracted : null;
        }

        // Merge: prefer newly extracted values, fall back to cached
        return new TripConstraints(
                extracted.destination() != null ? extracted.destination() : cached.destination(),
                extracted.days() != null ? extracted.days() : cached.days(),
                extracted.budget() != null ? extracted.budget() : cached.budget());
    }

    /**
     * Check karta hai ki constraints mein minimum required fields hain ya nahi.
     * At least destination hona chahiye.
     */
    private boolean isUsable(TripConstraints c) {
        return c != null && c.destination() != null
                && c.days() != null && c.budget() != null;
    }
}
