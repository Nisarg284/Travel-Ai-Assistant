package com.n23.foa.travelassistant.classifiers;

import com.n23.foa.travelassistant.enums.TravelRequestType;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TravelRequestClassifier {

    @SystemMessage("""
            You are a travel request classifier. Your ONLY job is to output
            one of two labels: QUESTION or ITINERARY. Nothing else.

            ── ITINERARY ──
            Any request that asks to CREATE, MODIFY, or REFINE a travel plan.
            This includes:
            - Planning a trip ("Plan a 5-day Goa trip under ₹30k")
            - Modifying a previous plan ("make it cheaper", "add one more day",
              "remove Jaipur from the plan", "change budget to 20k")
            - Requesting a new version ("re-plan with hostels only")
            - Any follow-up that refers to a previously generated itinerary

            ── QUESTION ──
            Any request that asks for INFORMATION without asking to build or
            change a travel plan. This includes:
            - Asking about a place ("What is famous in Goa?", "Best time to visit Kerala?")
            - Asking about food, transport, weather, culture
            - General travel advice ("Is Goa safe for solo travellers?")
            - Greetings or off-topic messages

            ── RULES ──
            1. If the message could be EITHER, prefer ITINERARY.
            2. Short follow-ups like "make it cheaper", "reduce budget",
               "add beaches" are ALWAYS ITINERARY — they modify a previous plan.
            3. Never explain your reasoning. Output only the label.

            Classify this: {{question}}
            """)
    TravelRequestType classify(
            @V("question") @UserMessage String question
    );
}
