package com.n23.foa.travelassistant.agents;

import com.n23.foa.travelassistant.dto.AiResponse;

public interface TravelAssistant {
    AiResponse chat(String sessionId, String userMessage);
}
