package com.n23.foa.travelassistant.dto;

import java.util.List;

public record TripConstraints(
        String destination,
        Integer days,
        Integer budget,
        Integer numberOfMembers,
        String travelStyle,
        List<String> interests
) {
    /**
     * Backward-compatible constructor for existing code
     * that only provides destination, days, budget.
     */
    public TripConstraints(String destination, Integer days, Integer budget) {
        this(destination, days, budget, null, null, null);
    }
}
