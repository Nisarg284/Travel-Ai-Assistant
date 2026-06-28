package com.n23.foa.travelassistant.service.intineraryPlanner;

import com.n23.foa.travelassistant.dto.TripConstraints;
import dev.langchain4j.rag.content.Content;

import java.util.List;

public interface ItineraryPlanner {
    String plan(
            TripConstraints constraints,
            List<Content> contents
    );
}
