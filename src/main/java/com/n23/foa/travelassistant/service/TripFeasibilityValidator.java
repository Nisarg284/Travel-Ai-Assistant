package com.n23.foa.travelassistant.service;

import com.n23.foa.travelassistant.dto.TripConstraints;
import com.n23.foa.travelassistant.dto.ValidationResult;
import dev.langchain4j.rag.content.Content;

import java.util.List;

public interface TripFeasibilityValidator {

    ValidationResult validate(
            TripConstraints constraints,
            List<Content> contents
    );
}
