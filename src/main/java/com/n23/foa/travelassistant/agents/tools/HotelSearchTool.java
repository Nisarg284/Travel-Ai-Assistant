package com.n23.foa.travelassistant.agents.tools;

import com.n23.foa.travelassistant.dto.HotelOption;
import com.n23.foa.travelassistant.service.mock.MockHotelService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Hotel Search Agent Tool — searches for accommodation options
 * at a destination, filtered by budget and travel style.
 *
 * Uses MockHotelService internally (swap to real API in future).
 * Zero LLM calls — fully Java-based, rate-limit safe.
 */
@Slf4j
@Component
public class HotelSearchTool {

    private final MockHotelService hotelService;

    public HotelSearchTool(MockHotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Tool("Search for hotels and accommodation at a travel destination. " +
            "Returns available hotels with names, areas, prices per night, ratings, and amenities. " +
            "Results are filtered by budget and sorted by travel style preference.")
    public String searchHotels(
            @P("The travel destination name, e.g. Goa, Jaipur, Kerala, Manali") String destination,
            @P("Maximum budget per night in INR per room") String budgetPerNightStr,
            @P("Travel style preference: backpacker, comfort, or luxury") String travelStyle
    ) {
        int budgetPerNight;
        try {
            budgetPerNight = Integer.parseInt(budgetPerNightStr.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            budgetPerNight = 5000; // sensible default
        }

        log.info("🏨 HotelSearchTool invoked — destination: {}, budget/night: ₹{}, style: {}",
                destination, budgetPerNight, travelStyle);

        List<HotelOption> hotels = hotelService.searchHotels(destination, budgetPerNight, travelStyle);

        if (hotels.isEmpty()) {
            return "No hotels found for " + destination + " within ₹" + budgetPerNight + "/night budget. " +
                    "Try increasing the budget or check if the destination is supported.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Hotel Search Results: ").append(destination).append(" ===\n");
        sb.append("(Budget ≤ ₹").append(budgetPerNight).append("/night, Style: ").append(travelStyle).append(")\n\n");
        sb.append("| # | Hotel Name | Area | Type | Price/Night (₹) | Rating | Amenities |\n");
        sb.append("|---|---|---|---|---|---|---|\n");

        int count = 0;
        for (HotelOption hotel : hotels) {
            count++;
            sb.append(String.format("| %d | %s | %s | %s | ₹%.0f | %.1f⭐ | %s |\n",
                    count, hotel.name(), hotel.area(), hotel.type(),
                    hotel.pricePerNight(), hotel.rating(), hotel.amenities()));
            if (count >= 6) break; // Limit to top 6 to save tokens
        }

        sb.append("\nTotal results: ").append(hotels.size()).append(" hotels found.\n");

        log.info("🏨 HotelSearchTool completed — {} results for {}", hotels.size(), destination);
        return sb.toString();
    }
}
