package com.n23.foa.travelassistant.dto;

/**
 * Unified API response wrapper.
 * Either contains a chat message (for Q&A) or a structured itinerary plan.
 */
public record AiResponse(
        String type,
        String message,
        TravelPlanResponse itinerary
) {

    /**
     * Factory: create a chat-type response (RAG Q&A).
     */
    public static AiResponse chat(String message) {
        return new AiResponse("CHAT", message, null);
    }

    /**
     * Factory: create an itinerary-type response (structured plan).
     */
    public static AiResponse itinerary(TravelPlanResponse plan) {
        return new AiResponse("ITINERARY", null, plan);
    }
}
