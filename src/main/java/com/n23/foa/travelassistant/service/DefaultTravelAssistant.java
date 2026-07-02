package com.n23.foa.travelassistant.service;

import com.n23.foa.travelassistant.agents.TravelAssistant;
import com.n23.foa.travelassistant.agents.TravelKnowledgeAgent;
import com.n23.foa.travelassistant.agents.TravelPlannerAgent;
import com.n23.foa.travelassistant.classifiers.TravelRequestClassifier;
import com.n23.foa.travelassistant.enums.TravelRequestType;

import org.springframework.stereotype.Service;

@Service
public class DefaultTravelAssistant
implements TravelAssistant {


    private final TravelRequestClassifier classifier;
    private final TravelKnowledgeAgent knowledgeAgent;
    private final TravelPlannerAgent plannerAgent;

    public DefaultTravelAssistant(TravelRequestClassifier classifier, TravelKnowledgeAgent knowledgeAgent, TravelPlannerAgent plannerAgent) {
        this.classifier = classifier;
        this.knowledgeAgent = knowledgeAgent;
        this.plannerAgent = plannerAgent;
    }

    @Override
    public String chat(String sessionId,String question) {

        TravelRequestType requestType = classifier.classify(question);

        System.out.println("Request Type: "+requestType);

        String response;

        if (requestType == TravelRequestType.ITINERARY){
            response = plannerAgent.plan(sessionId,question);
        }else{
            response = knowledgeAgent.chat(sessionId,question);
        }

        return response;
    }
}
