package com.n23.foa.travelassistant.ingestion.dto;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;

import java.util.Map;

public record EnrichedSegment(TextSegment segment, Metadata metadata) {
}
