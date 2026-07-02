package com.n23.foa.travelassistant.service;

import com.n23.foa.travelassistant.agents.ItineraryPlanner;
import com.n23.foa.travelassistant.dto.TripConstraints;
import com.n23.foa.travelassistant.dto.ValidationResult;
import com.n23.foa.travelassistant.memory.InMemoryChatMemoryStore;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.Content;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DefaultItineraryPlanner
implements ItineraryPlanner {

    private final ChatModel chatModel;
    private final InMemoryChatMemoryStore memoryStore;

    public DefaultItineraryPlanner(ChatModel chatModel, InMemoryChatMemoryStore inMemoryChatMemoryStore) {
        this.chatModel = chatModel;
        this.memoryStore = inMemoryChatMemoryStore;
    }

    @Override
    public String plan(
            String sessionId,
            TripConstraints constraints,
            List<Content> contents,
            ValidationResult validation

    ) {


        String prompt = buildPrompt(
                constraints,
                contents
        );

        List<ChatMessage> history = memoryStore.getMessages(sessionId);

        List<ChatMessage> messages = new ArrayList<>(history);
        messages.add(UserMessage.from(prompt));

        AiMessage response = chatModel.chat(messages).aiMessage();

        messages.add(response);
        memoryStore.updateMessages(sessionId,messages);




        System.out.println(
//                "============= FINAL PROMPT ============="
                "=============== memoryStore =============="
        );
//
//        System.out.println(prompt);
        System.out.println(memoryStore.getMessages(sessionId));
//
        System.out.println(
                "========================================"
        );
        return response.text();
    }

    private String buildPrompt(TripConstraints constraints, List<Content> contents) {

        StringBuilder builder = new StringBuilder();

        builder.append("""
                You are an expert travel planner.

                Create a practical and realistic itinerary.

                CRITICAL BUDGET RULES:

                1. The TOTAL estimated cost MUST NOT exceed the user's budget.
                2. Calculate approximate expenses for:
                   - Accommodation
                   - Food
                   - Transportation
                   - Activities
                3. Add all expenses before generating the itinerary.
                4. If the itinerary exceeds the budget:
                   - Reduce activities
                   - Suggest cheaper accommodation
                   - Prefer free attractions
                   - Reduce restaurant spending
                5. Never generate an itinerary that violates the user's budget.
                6. Used only provided data not to use outside.
                7. if user's budget is low then cut out some places that takes extra money.
                8. Always think practically and try to best try to make user's itinerary according to it's budget.


                User Constraints:
                """
        );

        builder.append("""
                Destination: %s
                Duration: %s Days
                Budget: ₹%s

                """.formatted(
                        constraints.destination(),
                constraints.days(),
                constraints.budget()
                )
        );

        builder.append("""
                        Retrieved Information:
                        """
        );

        for (Content content : contents) {
            TextSegment segment =
                    content.textSegment();

            String section =
                    segment.metadata()
                            .getString("section");

            builder.append("""
                            =========================
                            Section: %s
                            =========================
                            %s
                            """.formatted(
                            section,
                            segment.text()
                    )
            );
        }

            builder.append("""
                Instructions:

                1. Create a day-wise itinerary.
                2. Include food recommendations.
                3. Include important attractions.
                4. Suggest accommodation according to budget.
                5. Mention approximate expenses.
                6. Try to stay within the user's budget.
                7. Include travel tips at the end.

                Generate the itinerary now.
                """);
            return builder.toString();
    }


}

//
//package com.n23.foa.travelassistant.service;
//
//import com.n23.foa.travelassistant.agents.ItineraryPlanner;
//import com.n23.foa.travelassistant.dto.TripConstraints;
//import com.n23.foa.travelassistant.dto.ValidationResult;
//import dev.langchain4j.data.segment.TextSegment;
//import dev.langchain4j.model.chat.ChatModel;
//import dev.langchain4j.rag.content.Content;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
//@Service
//public class DefaultItineraryPlanner implements ItineraryPlanner {
//
//    private final ChatModel chatModel;
//
//    public DefaultItineraryPlanner(ChatModel chatModel) {
//        this.chatModel = chatModel;
//    }
//
//    @Override
//    public String plan(
//            TripConstraints constraints,
//            List<Content> contents,
//            ValidationResult validation
//    ) {
//        String systemPrompt = buildSystemPrompt();
//        String userPrompt   = buildUserPrompt(constraints, contents);
//
//        System.out.println("============= SYSTEM PROMPT =============");
//        System.out.println(systemPrompt);
//        System.out.println("============= USER PROMPT =============");
//        System.out.println(userPrompt);
//        System.out.println("========================================");
//
//        // Ideally pass systemPrompt separately via SystemMessage if your ChatModel supports it.
//        // Combining here for compatibility:
//        return chatModel.chat(systemPrompt + "\n\n" + userPrompt);
//    }
//
//    // ---------------------------------------------------------------------------
//    // SYSTEM PROMPT  — role, reasoning rules, output contract
//    // ---------------------------------------------------------------------------
//    private String buildSystemPrompt() {
//        return """
//                You are an expert travel planner specialising in India.
//                Your job is to create personalised, day-by-day itineraries that are
//                realistic, enjoyable, AND strictly within the traveller's budget.
//
//                ──────────────────────────────────────────
//                CORE RULES  (follow every time, no exceptions)
//                ──────────────────────────────────────────
//
//                BUDGET DISCIPLINE
//                • Before writing a single day, silently estimate the full trip cost:
//                    Accommodation  +  Food  +  Local transport  +  Activities  +  Misc (5 %)
//                • If the estimate exceeds the budget:
//                    – Replace paid attractions with free/low-cost alternatives.
//                    – Suggest budget accommodation (hostels, guesthouses, budget hotels).
//                    – Reduce dining from restaurants to dhabas / local eateries.
//                    – Cut or combine expensive day trips.
//                    – Keep trimming until the total fits.
//                • If the budget is very tight, be honest — mention what cannot realistically
//                  be included rather than inventing a plan that cannot be executed.
//
//                DATA FIDELITY
//                • Use ONLY the information provided in the "Knowledge Base" section below.
//                • Do NOT invent prices, timings, or attractions that are not in the data.
//                • If a detail is missing from the data, say "details not available" rather
//                  than guessing.
//
//                PRACTICALITY
//                • Group nearby attractions on the same day to minimise travel time and cost.
//                • Account for realistic travel time between locations.
//                • Avoid overbooking — a relaxed traveller enjoys the trip more.
//
//                ──────────────────────────────────────────
//                OUTPUT FORMAT  (mandatory structure)
//                ──────────────────────────────────────────
//
//                ## 🗺️ Trip Overview
//                - Destination, duration, total budget, estimated total spend
//
//                ## 💰 Budget Breakdown
//                | Category       | Estimated Cost (₹) |
//                |----------------|-------------------|
//                | Accommodation  |                   |
//                | Food           |                   |
//                | Local Travel   |                   |
//                | Activities     |                   |
//                | Miscellaneous  |                   |
//                | **Total**      |                   |
//
//                ## 📅 Day-by-Day Itinerary
//                For each day use this sub-structure:
//                ### Day N – <Theme / Highlight>
//                **Morning** – …
//                **Afternoon** – …
//                **Evening** – …
//                🍽️ **Meals** – Breakfast: … | Lunch: … | Dinner: …
//                🏨 **Stay** – <Accommodation name / type & approx. ₹ per night>
//                💸 **Day Spend** – ₹ approx.
//
//                ## 🏨 Accommodation Summary
//                Brief list of recommended stays with nightly cost.
//
//                ## 🚌 Getting Around
//                How to travel locally (auto, bus, metro, taxi) with cost hints.
//
//                ## 💡 Travel Tips
//                5–7 practical tips specific to this destination and trip.
//
//                ## ⚠️ Budget Alert (include only if relevant)
//                Any honest caveats if the budget is tight or certain items were cut.
//                """;
//    }
//
//    // ---------------------------------------------------------------------------
//    // USER PROMPT  — dynamic data: constraints + RAG knowledge base
//    // ---------------------------------------------------------------------------
//    private String buildUserPrompt(TripConstraints constraints,
//                                   List<Content> contents) {
//        StringBuilder builder = new StringBuilder();
//
//        // ── Trip constraints ──
//        builder.append("""
//                ══════════════════════════════════════
//                TRAVELLER REQUEST
//                ══════════════════════════════════════
//                Destination : %s
//                Duration    : %d day(s)
//                Total Budget: ₹%s
//
//                """.formatted(
//                constraints.destination(),
//                constraints.days(),
//                constraints.budget()
//        ));
//
//        // ── RAG knowledge base ──
//        builder.append("""
//                ══════════════════════════════════════
//                KNOWLEDGE BASE  (use this data only)
//                ══════════════════════════════════════
//                """);
//
//        if (contents == null || contents.isEmpty()) {
//            builder.append("No additional knowledge base data was retrieved.\n\n");
//        } else {
//            for (Content content : contents) {
//                TextSegment segment = content.textSegment();
//                String section = segment.metadata().getString("section");
//
//                builder.append("""
//                        ── Section: %s ──
//                        %s
//
//                        """.formatted(
//                        section != null ? section : "General",
//                        segment.text()
//                ));
//            }
//        }
//
//        // ── Final instruction ──
//        builder.append("""
//                ══════════════════════════════════════
//                TASK
//                ══════════════════════════════════════
//                Using only the knowledge base above and the traveller's constraints,
//                generate a complete itinerary following the output format defined in
//                your instructions. Double-check the budget total before responding.
//                """);
//
//        return builder.toString();
//    }
//}
