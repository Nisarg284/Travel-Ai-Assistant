package com.n23.foa.travelassistant.service;

import com.n23.foa.travelassistant.agents.TravelAssistant;
import com.n23.foa.travelassistant.agents.TravelKnowledgeAgent;
import com.n23.foa.travelassistant.agents.TravelSupervisorAgent;
import com.n23.foa.travelassistant.classifiers.TravelRequestClassifier;
import com.n23.foa.travelassistant.dto.AiResponse;
import com.n23.foa.travelassistant.dto.TravelPlanResponse;
import com.n23.foa.travelassistant.enums.TravelRequestType;

import dev.langchain4j.service.output.OutputParsingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Main entry point for the Travel Assistant.
 *
 * Routes requests based on intent classification:
 * - ITINERARY → TravelSupervisorAgent (multi-agent orchestration)
 * - QUESTION → TravelKnowledgeAgent (RAG-powered Q&A)
 */
@Slf4j
@Service
public class DefaultTravelAssistant
        implements TravelAssistant {

    private static final int MAX_RETRIES = 2;

    private final TravelRequestClassifier classifier;
    private final TravelKnowledgeAgent knowledgeAgent;
    private final TravelSupervisorAgent supervisorAgent;

    public DefaultTravelAssistant(
            TravelRequestClassifier classifier,
            TravelKnowledgeAgent knowledgeAgent,
            TravelSupervisorAgent supervisorAgent) {
        this.classifier = classifier;
        this.knowledgeAgent = knowledgeAgent;
        this.supervisorAgent = supervisorAgent;
    }

    @Override
    public AiResponse chat(String sessionId, String question) {

        TravelRequestType requestType = classifier.classify(question);

        log.info("Request classified as: {} for session: {}", requestType, sessionId);

        if (requestType == TravelRequestType.ITINERARY) {
            log.info("Routing to Supervisor Agent (multi-agent pipeline)");
            TravelPlanResponse plan = orchestrateWithRetry(sessionId, question);
            return AiResponse.itinerary(plan);
        } else {
            log.info("Routing to Knowledge Agent (RAG Q&A)");
            String chatResponse = knowledgeAgent.chat(sessionId, question);
            return AiResponse.chat(chatResponse);
        }
    }

    /**
     * Calls the supervisor agent with automatic retry on JSON parsing failures.
     * LLMs occasionally produce malformed JSON (e.g., unclosed quotes, trailing
     * commas).
     * Retrying typically succeeds since these errors are non-deterministic.
     */
    private TravelPlanResponse orchestrateWithRetry(String sessionId, String question) {
        OutputParsingException lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES + 1; attempt++) {
            try {
                TravelPlanResponse plan = supervisorAgent.orchestrate(sessionId, question);
                if (attempt > 1) {
                    log.info("✅ Supervisor Agent succeeded on retry attempt {}", attempt);
                }
                return plan;
            } catch (OutputParsingException e) {
                lastException = e;
                if (attempt <= MAX_RETRIES) {
                    log.warn("⚠️ JSON parsing failed on attempt {} — retrying ({}/{})... Error: {}",
                            attempt, attempt, MAX_RETRIES + 1,
                            e.getMessage().substring(0, Math.min(200, e.getMessage().length())));
                } else {
                    log.error("❌ JSON parsing failed after {} attempts. Giving up.", MAX_RETRIES + 1);
                }
            }
        }

        throw lastException;
    }
}
