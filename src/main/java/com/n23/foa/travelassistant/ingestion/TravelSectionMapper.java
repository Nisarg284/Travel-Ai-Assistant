package com.n23.foa.travelassistant.ingestion;

import com.n23.foa.travelassistant.ingestion.dto.TravelSection;
import dev.langchain4j.data.segment.TextSegment;

public interface TravelSectionMapper {

    TextSegment map(TravelSection section);
}
