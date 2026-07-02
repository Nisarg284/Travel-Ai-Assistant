package com.n23.foa.travelassistant.memory;


import dev.langchain4j.data.message.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ConversationMemory {

    private final Map<String, List<ChatMessage>>
            conversations  = new HashMap<>();

    public void add(
            String sessionId,
            ChatMessage message
    ){
        conversations.computeIfAbsent(
                sessionId,
                k -> new CopyOnWriteArrayList<>()
        ).add(message);
    }

    public List<ChatMessage> get(
            String sessionId
    ){
        return conversations.getOrDefault(
                sessionId,
                List.of()
        );
    }

    public void clear(
            String sessionId
    ){
        conversations.remove(sessionId);
    }
}
