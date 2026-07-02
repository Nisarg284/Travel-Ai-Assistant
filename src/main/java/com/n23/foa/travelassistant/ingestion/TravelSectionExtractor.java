package com.n23.foa.travelassistant.ingestion;

import com.n23.foa.travelassistant.ingestion.dto.TravelSection;

import java.util.List;

public interface TravelSectionExtractor {

    List<TravelSection>extract(String markdownContent);
}
