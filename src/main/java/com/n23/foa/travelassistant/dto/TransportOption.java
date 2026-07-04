package com.n23.foa.travelassistant.dto;

import com.n23.foa.travelassistant.enums.TravelMode;

public record TransportOption(
        TravelMode mode,
        String operator,
        String departure,
        String arrival,
        double duration,
        double price

) {
}
