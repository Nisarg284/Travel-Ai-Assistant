package com.n23.foa.travelassistant.agents;

import com.n23.foa.travelassistant.dto.TripConstraints;
import com.n23.foa.travelassistant.dto.ValidationResult;
import dev.langchain4j.rag.content.Content;

import java.util.List;

public interface ItineraryPlanner {
    String plan(
            String sessionId,
            TripConstraints constraints,
            List<Content> contents,
            ValidationResult validation
    );
}
