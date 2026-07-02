package com.n23.foa.travelassistant.agents;

import com.n23.foa.travelassistant.dto.ValidationResult;

public interface TripFeasibilityExpert {

    ValidationResult validate(
            String prompt
    );
}
