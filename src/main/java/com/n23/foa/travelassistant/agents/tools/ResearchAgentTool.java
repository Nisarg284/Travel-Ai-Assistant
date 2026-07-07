package com.n23.foa.travelassistant.agents.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Research Agent Tool — provides quick destination facts and key information.
 * Zero LLM calls — fully Java-based.
 */
@Slf4j
@Component
public class ResearchAgentTool {

    private static final Map<String, String> DESTINATION_FACTS = Map.of(
            "goa", """
                    Destination: Goa
                    Type: Beach & Party
                    State: Goa, India
                    Language: Konkani, English, Hindi
                    Currency: INR (₹)
                    Best Season: Nov–Feb
                    Average Daily Temp: 25–33°C
                    Key Highlights: Beaches, Portuguese architecture, seafood, nightlife, water sports
                    Nearest Airport: Dabolim (GOI) / Mopa (GOX)
                    """,
            "jaipur", """
                    Destination: Jaipur (Pink City)
                    Type: Heritage & Culture
                    State: Rajasthan, India
                    Language: Hindi, Rajasthani, English
                    Currency: INR (₹)
                    Best Season: Oct–Mar
                    Average Daily Temp: 15–30°C (winter), 30–45°C (summer)
                    Key Highlights: Forts, palaces, bazaars, royal heritage, handicrafts
                    Nearest Airport: Jaipur International (JAI)
                    """,
            "kerala", """
                    Destination: Kerala (God's Own Country)
                    Type: Nature & Wellness
                    State: Kerala, India
                    Language: Malayalam, English
                    Currency: INR (₹)
                    Best Season: Sep–Mar
                    Average Daily Temp: 23–33°C
                    Key Highlights: Backwaters, tea gardens, Ayurveda, wildlife, spice plantations
                    Nearest Airport: Cochin (COK) / Trivandrum (TRV)
                    """,
            "manali", """
                    Destination: Manali
                    Type: Mountain & Adventure
                    State: Himachal Pradesh, India
                    Language: Hindi, Pahari
                    Currency: INR (₹)
                    Best Season: Apr–Jun, Dec–Feb
                    Average Daily Temp: 10–25°C (summer), -5–10°C (winter)
                    Key Highlights: Snow, trekking, Rohtang Pass, Solang Valley, cafes, temples
                    Nearest Airport: Bhuntar (KUU, 50 km away)
                    """
    );

    @Tool("Get factual information about a travel destination including location, weather, " +
            "best season to visit, key highlights, and nearest airport.")
    public String researchDestination(
            @P("The travel destination, e.g. Goa, Jaipur, Kerala, Manali") String destination
    ) {
        log.info("🔍 ResearchAgentTool invoked — destination: {}", destination);

        String key = destination.toLowerCase().trim();
        String facts = DESTINATION_FACTS.getOrDefault(key,
                "No detailed research available for " + destination + ". Please provide basic travel information.");

        log.info("🔍 ResearchAgentTool completed for {}", destination);
        return facts;
    }
}
