package com.n23.foa.travelassistant.dto;

public record TripOverview(
        String destination,
        String duration,
        String totalBudget,
        int groupSize,
        String style
) {
}
