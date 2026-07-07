package com.n23.foa.travelassistant.agents.tools;

import com.n23.foa.travelassistant.dto.TransportOption;
import com.n23.foa.travelassistant.service.mock.MockTransportService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Transport Search Agent Tool — searches for travel options
 * (flights, trains, buses) to reach a destination.
 *
 * Uses MockTransportService internally (swap to real APIs in future).
 * Zero LLM calls — fully Java-based, rate-limit safe.
 */
@Slf4j
@Component
public class TransportSearchTool {

    private final MockTransportService transportService;

    public TransportSearchTool(MockTransportService transportService) {
        this.transportService = transportService;
    }

    @Tool("Search for transport options (flights, trains, buses) to reach a travel destination from a specific origin city. " +
            "Returns available options with operator names, departure/arrival times, duration, and prices. " +
            "If user hasn't mentioned origin city, use 'Delhi' as default.")
    public String searchTransport(
            @P("Origin city name, e.g. Mumbai, Delhi, Bangalore, Pune, Chandigarh") String origin,
            @P("Destination city name, e.g. Goa, Jaipur, Kerala, Manali") String destination,
            @P("Preferred travel mode: flight, train, or bus. Use 'any' if no preference.") String preferredMode
    ) {
        log.info("🚌 TransportSearchTool invoked — {} → {}, preferred: {}", origin, destination, preferredMode);

        List<TransportOption> options = transportService.searchTransport(origin, destination, preferredMode);

        if (options.isEmpty()) {
            // Try to suggest available origins
            List<String> supportedOrigins = transportService.getSupportedOrigins(destination);
            String suggestion = supportedOrigins.isEmpty()
                    ? "This destination may not be supported yet."
                    : "Available origin cities for " + destination + ": " + String.join(", ", supportedOrigins);

            return "No transport routes found from " + origin + " to " + destination + ". " + suggestion;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Transport Options: ").append(origin).append(" → ").append(destination).append(" ===\n\n");
        sb.append("| # | Mode | Operator | Departure | Arrival | Duration (hrs) | Price (₹) |\n");
        sb.append("|---|---|---|---|---|---|---|\n");

        int count = 0;
        for (TransportOption opt : options) {
            count++;
            sb.append(String.format("| %d | %s | %s | %s | %s | %.1f | ₹%.0f |\n",
                    count, opt.mode(), opt.operator(), opt.departure(),
                    opt.arrival(), opt.duration(), opt.price()));
            if (count >= 3) break; // Limit to top 3 to save tokens
        }

        sb.append("\nTotal: ").append(options.size()).append(" options found.\n");

        log.info("🚌 TransportSearchTool completed — {} results for {} → {}", options.size(), origin, destination);
        return sb.toString();
    }
}
