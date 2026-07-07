package com.n23.foa.travelassistant.dto;


public record DayItinerary(
        int dayNumber,
        String theme,
        String morningActivity,
        String afternoonActivity,
        String eveningActivity,
        String meals,
        String dailySpend
) {
}
