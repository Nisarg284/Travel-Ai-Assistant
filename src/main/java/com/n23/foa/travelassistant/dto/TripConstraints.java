package com.n23.foa.travelassistant.dto;

public record TripConstraints(
        String destination,
        Integer days,
        Integer budget
) {
}
