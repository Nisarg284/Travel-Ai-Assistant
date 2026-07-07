package com.n23.foa.travelassistant.agents.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Local Expert Agent Tool — provides insider travel tips for Indian destinations.
 * Zero LLM calls — fully Java-based.
 */
@Slf4j
@Component
public class LocalExpertAgentTool {

    private static final Map<String, String> LOCAL_TIPS = Map.of(
            "goa", """
                    Local Expert Tips for Goa:
                    - Best time: November to February (peak season), June-Sept (monsoon — cheaper but rainy)
                    - Rent a scooter (₹300-500/day) — best way to explore
                    - North Goa = parties & beaches; South Goa = peaceful & scenic
                    - Carry cash — many beach shacks don't accept cards
                    - Try local feni (cashew/coconut liquor) — it's unique to Goa
                    - Avoid touts at the airport; pre-book your taxi
                    """,
            "jaipur", """
                    Local Expert Tips for Jaipur:
                    - Best time: October to March (winters are pleasant)
                    - Buy a composite ticket for all major forts (saves money)
                    - Visit Amber Fort early morning to avoid crowds
                    - Auto-rickshaw rates are negotiable — agree on price before getting in
                    - Johari Bazaar is best for jewelry; Bapu Bazaar for textiles
                    - Try lassi at Lassiwala on MI Road (the original one!)
                    """,
            "kerala", """
                    Local Expert Tips for Kerala:
                    - Best time: September to March (post-monsoon greenery)
                    - Book houseboat at least a week in advance during peak season
                    - Munnar gets cold at night — carry a light jacket
                    - Ayurvedic treatments need at least 3-day courses to be effective
                    - Use KSRTC buses — they're cheap, reliable, and cover all Kerala
                    - Try Toddy (palm wine) from a local toddy shop
                    """,
            "manali", """
                    Local Expert Tips for Manali:
                    - Best time: April-June (summer), Dec-Feb (snow), avoid monsoon
                    - Rohtang Pass requires a permit — book online in advance
                    - Old Manali has better cafes and vibe than New Manali
                    - Carry altitude sickness medicine if going above 13,000 ft
                    - Trek to Jogini Falls is easy and rewarding (1-2 hours)
                    - Local buses to Solang Valley are cheap (₹20-30)
                    """
    );

    @Tool("Get local insider tips and practical advice for a travel destination. " +
            "Returns best time to visit, money-saving tips, local food recommendations, and travel hacks.")
    public String getLocalTips(
            @P("The travel destination, e.g. Goa, Jaipur, Kerala, Manali") String destination
    ) {
        log.info("🗺️ LocalExpertAgentTool invoked — destination: {}", destination);

        String key = destination.toLowerCase().trim();
        String tips = LOCAL_TIPS.getOrDefault(key,
                "No specific local tips available for " + destination + ". General travel advice: carry cash, respect local customs, and try local food.");

        log.info("🗺️ LocalExpertAgentTool completed for {}", destination);
        return tips;
    }
}
