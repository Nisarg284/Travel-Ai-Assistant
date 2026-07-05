package com.n23.foa.travelassistant.agents;

public interface TravelAssistant {
    com.n23.foa.travelassistant.dto.AiResponse chat(String sessionId, String userMessage);
}
