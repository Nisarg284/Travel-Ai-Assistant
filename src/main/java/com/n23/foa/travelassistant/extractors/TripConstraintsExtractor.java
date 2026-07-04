package com.n23.foa.travelassistant.extractors;

import com.n23.foa.travelassistant.dto.TripConstraints;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TripConstraintsExtractor {

    @UserMessage("""
            Extract trip planning information from the user's message.

            Return:
            - destination (String)
            - days (Integer)
            - budget (Integer, numeric only)
            - numberOfMembers (Integer, default 1 if not mentioned)
            - travelStyle (String: "backpacker", "comfort", or "luxury" — infer from budget if not stated)
            - interests (List<String>: e.g. ["adventure", "food", "culture", "beaches", "nature"])

            Rules:
            - Budget should be numeric only.
            - Convert values like 35k to 35000, 1.5L to 150000.
            - If information is missing, return null for that field.
            - For travelStyle, infer: <₹1000/person/day = backpacker, ₹1000-3000 = comfort, >₹3000 = luxury
            - For interests, extract any mentioned preferences or activities.

            Question:
            {{question}}
            """)
    TripConstraints extract(@V("question") String question);
}
