package com.n23.foa.travelassistant.dto;

public record ValidationResult(
        boolean feasible,
        String reason,
        String suggestion
) {
}
