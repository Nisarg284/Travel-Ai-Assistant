package com.n23.foa.travelassistant.dto;

import java.util.List;

public record TravelPlanResponse(
        TripOverview overview,
        List<TransportOption> howToReach,
        List<HotelOption> hotels,
        java.util.Map<String, String> budgetBreakdown,
        List<DayItinerary> itinerary,
        List<String> localInsights,
        List<String> destinationImages
) {
}
