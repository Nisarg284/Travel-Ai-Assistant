package com.n23.foa.travelassistant.agents.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Itinerary Agent Tool — provides curated daily activity suggestions
 * for popular Indian destinations. Zero LLM calls.
 */
@Slf4j
@Component
public class ItineraryAgentTool {

    private static final Map<String, String> ITINERARY_HINTS = Map.of(
            "goa", """
                    Day activities for Goa:
                    - Morning: Beach visits (Baga, Calangute, Anjuna), water sports
                    - Afternoon: Old Goa churches, spice plantations, Chapora Fort
                    - Evening: Beach shacks, Tito's Lane nightlife, sunset cruises
                    - Food: Fish thali, prawn curry, bebinca, feni
                    """,
            "jaipur", """
                    Day activities for Jaipur:
                    - Morning: Amber Fort, Jaigarh Fort, Nahargarh Fort
                    - Afternoon: City Palace, Hawa Mahal, Jantar Mantar
                    - Evening: Johari Bazaar shopping, Chokhi Dhani, rooftop dining
                    - Food: Dal baati churma, laal maas, ghewar, kachori
                    """,
            "kerala", """
                    Day activities for Kerala:
                    - Morning: Houseboat in Alleppey, tea gardens in Munnar
                    - Afternoon: Periyar Wildlife Sanctuary, Fort Kochi, Chinese fishing nets
                    - Evening: Kathakali performances, Ayurvedic spa treatments
                    - Food: Appam with stew, fish molee, Kerala sadya, payasam
                    """,
            "manali", """
                    Day activities for Manali:
                    - Morning: Solang Valley adventures, Rohtang Pass (seasonal)
                    - Afternoon: Hadimba Temple, Old Manali cafes, Jogini Falls
                    - Evening: Mall Road walk, bonfire at camps
                    - Food: Siddu, trout fish, thukpa, momos
                    """
    );

    @Tool("Get curated daily activity suggestions for a travel destination. " +
            "Returns recommended activities, attractions, and food for each part of the day.")
    public String suggestActivities(
            @P("The travel destination, e.g. Goa, Jaipur, Kerala, Manali") String destination
    ) {
        log.info("📋 ItineraryAgentTool invoked — destination: {}", destination);

        String key = destination.toLowerCase().trim();
        String hints = ITINERARY_HINTS.getOrDefault(key,
                "No curated suggestions available for " + destination + ". Use general travel knowledge.");

        log.info("📋 ItineraryAgentTool completed for {}", destination);
        return hints;
    }
}
