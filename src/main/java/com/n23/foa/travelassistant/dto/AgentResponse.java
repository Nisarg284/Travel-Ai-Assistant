package com.n23.foa.travelassistant.dto;

public record AgentResponse(
        String agentName,
        String content,
        String[]images,
        double confidenceScore
) {
}
