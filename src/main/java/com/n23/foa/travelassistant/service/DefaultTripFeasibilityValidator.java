package com.n23.foa.travelassistant.service;


import com.n23.foa.travelassistant.agents.TripFeasibilityExpert;
import com.n23.foa.travelassistant.dto.TripConstraints;
import com.n23.foa.travelassistant.dto.ValidationResult;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.Content;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTripFeasibilityValidator
implements TripFeasibilityValidator{
    private final TripFeasibilityExpert tripFeasibilityExpert;

    public DefaultTripFeasibilityValidator(TripFeasibilityExpert tripFeasibilityExpert) {
        this.tripFeasibilityExpert = tripFeasibilityExpert;
    }

    @Override
    public ValidationResult validate(TripConstraints constraints, List<Content> contents) {

        String prompt =
                buildPrompt(
                        constraints,
                        contents
                );

        return tripFeasibilityExpert.validate(prompt);
    }

    private String buildPrompt(TripConstraints constraints, List<Content> contents) {

        StringBuilder builder = new StringBuilder();

        builder.append("""
                You are an expert travel budget validator.

                IMPORTANT RULES:

                1. The estimated budget information may correspond to trips of different durations.
                2. Do NOT directly compare the user's budget with the retrieved budget ranges.
                3. Adjust the estimated cost according to the requested trip duration.
                4. A shorter trip may require significantly less budget.
                5. A longer trip may require significantly more budget.
                6. Be practical and realistic.
                7. Do not use outside knowledge base, use only given data.
                8. Don't assume any amount.

                Determine:
                - Is the trip feasible?
                - Why?
                - Suggest alternatives if not feasible
                """
        );

        builder.append("""
                Destination: %s
                Duration: %s
                Budget: %s
                """
                .formatted(
                        constraints.destination(),
                        constraints.days(),
                        constraints.budget()
                )
        );

        builder.append("""
                Retrieved Information:
                """);

        for (Content content : contents){
            TextSegment segment =
                    content.textSegment();

            builder.append("""
                    Knowledge Base:
                    ===================
                    %s
                    ===================
                    %s
                    """
                    .formatted(
                            segment.metadata()
                                    .getString("section"),
                            segment.text()
                    )
            );
        }

        return builder.toString();
    }
}
