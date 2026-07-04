package com.n23.foa.travelassistant.service;

import com.n23.foa.travelassistant.agents.TravelAssistant;
import com.n23.foa.travelassistant.agents.TravelKnowledgeAgent;
import com.n23.foa.travelassistant.agents.TravelSupervisorAgent;
import com.n23.foa.travelassistant.classifiers.TravelRequestClassifier;
import com.n23.foa.travelassistant.enums.TravelRequestType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Main entry point for the Travel Assistant.
 *
 * Routes requests based on intent classification:
 * - ITINERARY → TravelSupervisorAgent (multi-agent orchestration)
 * - QUESTION  → TravelKnowledgeAgent (RAG-powered Q&A)
 */
@Slf4j
@Service
public class DefaultTravelAssistant
        implements TravelAssistant {

    private final TravelRequestClassifier classifier;
    private final TravelKnowledgeAgent knowledgeAgent;
    private final TravelSupervisorAgent supervisorAgent;

    public DefaultTravelAssistant(
            TravelRequestClassifier classifier,
            TravelKnowledgeAgent knowledgeAgent,
            TravelSupervisorAgent supervisorAgent
    ) {
        this.classifier = classifier;
        this.knowledgeAgent = knowledgeAgent;
        this.supervisorAgent = supervisorAgent;
    }

    @Override
    public String chat(String sessionId, String question) {

        TravelRequestType requestType = classifier.classify(question);

        log.info("Request classified as: {} for session: {}", requestType, sessionId);

        String response;

        if (requestType == TravelRequestType.ITINERARY) {
            log.info("Routing to Supervisor Agent (multi-agent pipeline)");
            response = supervisorAgent.orchestrate(sessionId, question);
        } else {
            log.info("Routing to Knowledge Agent (RAG Q&A)");
            response = knowledgeAgent.chat(sessionId, question);
        }

        return response;
    }
}
