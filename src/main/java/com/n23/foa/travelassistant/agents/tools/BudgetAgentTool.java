package com.n23.foa.travelassistant.agents.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Budget Agent Tool — estimates costs for a trip based on destination,
 * duration, and travel style. Zero LLM calls — fully Java-based.
 */
@Slf4j
@Component
public class BudgetAgentTool {

    @Tool("Estimate the budget for a trip to a destination. " +
            "Returns estimated daily costs for accommodation, food, transport, and activities.")
    public String estimateBudget(
            @P("The travel destination, e.g. Goa, Jaipur, Kerala, Manali") String destination,
            @P("Number of days for the trip") String daysStr,
            @P("Travel style: backpacker, comfort, or luxury") String travelStyle
    ) {
        int days;
        try {
            days = Integer.parseInt(daysStr.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            days = 3;
        }

        log.info("💰 BudgetAgentTool invoked — destination: {}, days: {}, style: {}", destination, days, travelStyle);

        String style = travelStyle != null ? travelStyle.toLowerCase().trim() : "comfort";

        double dailyAccommodation = switch (style) {
            case "backpacker" -> 800;
            case "luxury" -> 8000;
            default -> 2500; // comfort
        };

        double dailyFood = switch (style) {
            case "backpacker" -> 500;
            case "luxury" -> 3000;
            default -> 1200;
        };

        double dailyTransport = switch (style) {
            case "backpacker" -> 300;
            case "luxury" -> 2000;
            default -> 800;
        };

        double dailyActivities = switch (style) {
            case "backpacker" -> 200;
            case "luxury" -> 3000;
            default -> 1000;
        };

        double dailyTotal = dailyAccommodation + dailyFood + dailyTransport + dailyActivities;
        double tripTotal = dailyTotal * days;

        String result = """
                === Budget Estimate: %s (%d days, %s style) ===

                Daily Breakdown:
                | Category       | ₹/day  |
                |----------------|--------|
                | Accommodation  | ₹%.0f  |
                | Food           | ₹%.0f  |
                | Transport      | ₹%.0f  |
                | Activities     | ₹%.0f  |
                | **Daily Total**| **₹%.0f** |

                **Trip Total: ₹%.0f** (for %d days)
                """.formatted(destination, days, style,
                dailyAccommodation, dailyFood, dailyTransport, dailyActivities,
                dailyTotal, tripTotal, days);

        log.info("💰 BudgetAgentTool completed — estimated ₹{} for {} days in {}", tripTotal, days, destination);
        return result;
    }
}
