package com.n23.foa.travelassistant.extractors;

import com.n23.foa.travelassistant.dto.TripConstraints;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TripConstraintsExtractor {

    @UserMessage("""
            Extract trip planning information.

            Return:
            - destination
            - days
            - budget

            Rules:
            - Budget should be numeric only.
            - Convert values like 35k to 35000.
            // - If information is missing, return null.

            Question:
            {{question}}
            """)
    TripConstraints extract(@V("question") String question);
}
