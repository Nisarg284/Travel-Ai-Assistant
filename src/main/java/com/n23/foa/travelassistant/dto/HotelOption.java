package com.n23.foa.travelassistant.dto;

import com.n23.foa.travelassistant.enums.HotelType;

public record HotelOption(
        String name,
        String area,
        HotelType type,
        double pricePerNight,
        float rating,
        String amenities

) {
}
