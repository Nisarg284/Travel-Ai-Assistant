package com.n23.foa.travelassistant.service.mock;

import com.n23.foa.travelassistant.dto.TransportOption;
import com.n23.foa.travelassistant.enums.TravelMode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mock Transport Service — provides hardcoded transport data for Indian routes.
 * Replace with a real API (IRCTC, Skyscanner, etc.) in production.
 */
@Service
public class MockTransportService {

    private record RouteKey(String origin, String destination) {
        RouteKey {
            origin = origin.toLowerCase().trim();
            destination = destination.toLowerCase().trim();
        }
    }

    private static final Map<RouteKey, List<TransportOption>> TRANSPORT_DATABASE = new HashMap<>();

    static {
        // Delhi → Goa
        TRANSPORT_DATABASE.put(new RouteKey("delhi", "goa"), List.of(
                new TransportOption(TravelMode.Flight, "IndiGo", "06:00", "08:30", 2.5, 4500),
                new TransportOption(TravelMode.Flight, "Air India", "10:15", "12:45", 2.5, 5200),
                new TransportOption(TravelMode.Train, "Rajdhani Express", "16:00", "08:00+1", 16.0, 1800),
                new TransportOption(TravelMode.Bus, "RSRTC Volvo", "18:00", "12:00+1", 18.0, 1200)
        ));

        // Mumbai → Goa
        TRANSPORT_DATABASE.put(new RouteKey("mumbai", "goa"), List.of(
                new TransportOption(TravelMode.Flight, "SpiceJet", "07:00", "08:10", 1.2, 3200),
                new TransportOption(TravelMode.Train, "Konkan Kanya", "23:00", "09:00+1", 10.0, 800),
                new TransportOption(TravelMode.Bus, "Paulo Travels", "20:00", "08:00+1", 12.0, 900)
        ));

        // Delhi → Jaipur
        TRANSPORT_DATABASE.put(new RouteKey("delhi", "jaipur"), List.of(
                new TransportOption(TravelMode.Flight, "IndiGo", "08:00", "09:00", 1.0, 3500),
                new TransportOption(TravelMode.Train, "Shatabdi Express", "06:00", "10:30", 4.5, 700),
                new TransportOption(TravelMode.Bus, "RSRTC Volvo", "06:30", "12:30", 6.0, 500)
        ));

        // Delhi → Manali
        TRANSPORT_DATABASE.put(new RouteKey("delhi", "manali"), List.of(
                new TransportOption(TravelMode.Flight, "IndiGo (to Kullu)", "07:00", "08:30", 1.5, 5000),
                new TransportOption(TravelMode.Bus, "HRTC Volvo", "17:00", "07:00+1", 14.0, 1200),
                new TransportOption(TravelMode.Bus, "RedBus Private", "19:00", "09:00+1", 14.0, 1500)
        ));

        // Delhi → Kerala (Kochi)
        TRANSPORT_DATABASE.put(new RouteKey("delhi", "kerala"), List.of(
                new TransportOption(TravelMode.Flight, "IndiGo", "06:00", "09:30", 3.5, 5500),
                new TransportOption(TravelMode.Flight, "Air India", "14:00", "17:20", 3.3, 6200),
                new TransportOption(TravelMode.Train, "Kerala Express", "11:00", "19:00+1", 32.0, 1500)
        ));

        // Bangalore → Kerala
        TRANSPORT_DATABASE.put(new RouteKey("bangalore", "kerala"), List.of(
                new TransportOption(TravelMode.Flight, "IndiGo", "09:00", "10:15", 1.25, 3000),
                new TransportOption(TravelMode.Train, "Kochuveli Express", "22:00", "08:00+1", 10.0, 600),
                new TransportOption(TravelMode.Bus, "KSRTC Volvo", "21:00", "07:00+1", 10.0, 800)
        ));

        // Mumbai → Jaipur
        TRANSPORT_DATABASE.put(new RouteKey("mumbai", "jaipur"), List.of(
                new TransportOption(TravelMode.Flight, "SpiceJet", "08:00", "10:00", 2.0, 4000),
                new TransportOption(TravelMode.Train, "Jaipur Superfast", "17:00", "05:00+1", 12.0, 1200),
                new TransportOption(TravelMode.Bus, "Private Volvo", "19:00", "09:00+1", 14.0, 1000)
        ));
    }

    public List<TransportOption> searchTransport(String origin, String destination, String preferredMode) {
        RouteKey key = new RouteKey(origin, destination);

        List<TransportOption> allOptions = TRANSPORT_DATABASE.getOrDefault(key, Collections.emptyList());

        if (preferredMode == null || preferredMode.equalsIgnoreCase("any")) {
            return allOptions;
        }

        // Filter by preferred mode
        List<TransportOption> filtered = allOptions.stream()
                .filter(opt -> opt.mode().name().equalsIgnoreCase(preferredMode.trim()))
                .collect(Collectors.toList());

        // Fall back to all options if no match for the preferred mode
        return filtered.isEmpty() ? allOptions : filtered;
    }

    public List<String> getSupportedOrigins(String destination) {
        String dest = destination.toLowerCase().trim();
        return TRANSPORT_DATABASE.keySet().stream()
                .filter(key -> key.destination().equals(dest))
                .map(RouteKey::origin)
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.toList());
    }
}
