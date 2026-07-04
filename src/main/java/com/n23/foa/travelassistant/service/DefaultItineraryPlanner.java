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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
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

        String prompt = buildPrompt(constraints, contents);

        List<ChatMessage> history = memoryStore.getMessages(sessionId);

        List<ChatMessage> messages = new ArrayList<>(history);
        messages.add(UserMessage.from(prompt));

        AiMessage response = chatModel.chat(messages).aiMessage();

        messages.add(response);
        memoryStore.updateMessages(sessionId, messages);

        log.debug("Memory store updated for session: {}", sessionId);
        log.trace("Memory contents: {}", memoryStore.getMessages(sessionId));

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
            TextSegment segment = content.textSegment();

            String section = segment.metadata().getString("section");

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
