package com.n23.foa.travelassistant.service.mock;

import com.n23.foa.travelassistant.dto.HotelOption;
import com.n23.foa.travelassistant.enums.HotelType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mock Hotel Service — provides hardcoded hotel data for Indian destinations.
 * Replace with a real API (Booking.com, MakeMyTrip, etc.) in production.
 */
@Service
public class MockHotelService {

    private static final Map<String, List<HotelOption>> HOTEL_DATABASE = new HashMap<>();

    static {
        // Goa Hotels
        HOTEL_DATABASE.put("goa", List.of(
                new HotelOption("Zostel Goa", "Anjuna", HotelType.Hostel, 800, 4.2f, "WiFi, Common Kitchen, Beach Access"),
                new HotelOption("OYO Beachside Inn", "Calangute", HotelType.Hotel, 1500, 3.8f, "AC, WiFi, Breakfast"),
                new HotelOption("Hotel Mandovi", "Panaji", HotelType.Hotel, 2500, 4.0f, "AC, WiFi, Restaurant, Pool"),
                new HotelOption("Taj Fort Aguada", "Sinquerim", HotelType.Resort, 12000, 4.7f, "Spa, Pool, Beach, Fine Dining"),
                new HotelOption("Alila Diwa Goa", "Majorda", HotelType.Resort, 9000, 4.5f, "Spa, Pool, Gym, Restaurant"),
                new HotelOption("Casa Vagator", "Vagator", HotelType.Hotel, 3500, 4.3f, "AC, WiFi, Pool, Bar")
        ));

        // Jaipur Hotels
        HOTEL_DATABASE.put("jaipur", List.of(
                new HotelOption("Moustache Hostel", "MI Road", HotelType.Hostel, 600, 4.3f, "WiFi, Rooftop, Common Area"),
                new HotelOption("Hotel Pearl Palace", "Hathroi Fort", HotelType.Hotel, 1800, 4.5f, "AC, WiFi, Rooftop Restaurant"),
                new HotelOption("Jai Mahal Palace", "Civil Lines", HotelType.Resort, 8000, 4.6f, "Heritage, Pool, Spa, Gardens"),
                new HotelOption("Rambagh Palace", "Bhawani Singh Rd", HotelType.Resort, 25000, 4.9f, "Royal Heritage, Spa, Pool, Fine Dining"),
                new HotelOption("Zone By The Park", "MI Road", HotelType.Hotel, 3000, 4.1f, "AC, WiFi, Restaurant, Bar")
        ));

        // Kerala Hotels
        HOTEL_DATABASE.put("kerala", List.of(
                new HotelOption("Zostel Alleppey", "Alleppey", HotelType.Hostel, 700, 4.1f, "WiFi, Backwater View, Common Kitchen"),
                new HotelOption("KTDC Waterscapes", "Kumarakom", HotelType.Hotel, 2200, 4.0f, "AC, WiFi, Backwater View"),
                new HotelOption("Taj Malabar", "Cochin", HotelType.Resort, 7500, 4.5f, "Harbor View, Pool, Spa, Restaurant"),
                new HotelOption("Coconut Lagoon", "Kumarakom", HotelType.Resort, 10000, 4.6f, "Houseboat, Pool, Ayurvedic Spa"),
                new HotelOption("Fragrant Nature", "Munnar", HotelType.Hotel, 3500, 4.3f, "AC, WiFi, Tea Garden View")
        ));

        // Manali Hotels
        HOTEL_DATABASE.put("manali", List.of(
                new HotelOption("Zostel Manali", "Old Manali", HotelType.Hostel, 500, 4.2f, "WiFi, Mountain View, Common Area"),
                new HotelOption("Hotel Beas", "Mall Road", HotelType.Hotel, 1500, 3.9f, "AC, WiFi, River View"),
                new HotelOption("Johnson Lodge", "Circuit House Rd", HotelType.Hotel, 3000, 4.4f, "Heated Rooms, WiFi, Restaurant"),
                new HotelOption("The Himalayan", "Log Huts Area", HotelType.Resort, 6000, 4.5f, "Spa, Pool, Mountain View, Bonfire"),
                new HotelOption("Solang Valley Resort", "Solang", HotelType.Resort, 5000, 4.3f, "Adventure Sports, Mountain View, Restaurant")
        ));
    }

    public List<HotelOption> searchHotels(String destination, int budgetPerNight, String travelStyle) {
        String key = destination.toLowerCase().trim();

        List<HotelOption> allHotels = HOTEL_DATABASE.getOrDefault(key, Collections.emptyList());

        return allHotels.stream()
                .filter(h -> h.pricePerNight() <= budgetPerNight)
                .sorted(getComparator(travelStyle))
                .collect(Collectors.toList());
    }

    private Comparator<HotelOption> getComparator(String travelStyle) {
        if (travelStyle == null) return Comparator.comparingDouble(HotelOption::pricePerNight);

        return switch (travelStyle.toLowerCase().trim()) {
            case "backpacker" -> Comparator.comparingDouble(HotelOption::pricePerNight);
            case "luxury" -> Comparator.comparingDouble(HotelOption::pricePerNight).reversed();
            default -> Comparator.comparingDouble(h -> -h.rating()); // comfort: sort by rating
        };
    }
}
