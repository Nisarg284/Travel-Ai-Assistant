package com.n23.foa.travelassistant.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryChatMemoryStore implements ChatMemoryStore {

    private final Map<Object, List<ChatMessage>> store = new ConcurrentHashMap<>();

    /**
     * Retrieves messages for a specified chat memory.
     *
     * @param memoryId The ID of the chat memory.
     * @return List of messages for the specified chat memory. Must not be null. Can
     *         be deserialized from JSON using {@link ChatMessageDeserializer}.
     */
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        return store.getOrDefault(
                memoryId,
                List.of());
    }

    /**
     * Updates messages for a specified chat memory.
     *
     * @param memoryId The ID of the chat memory.
     * @param messages List of messages for the specified chat memory, that
     *                 represent the current state of the {@link ChatMemory}.
     *                 Can be serialized to JSON using
     *                 {@link ChatMessageSerializer}.
     */
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        store.put(memoryId, new ArrayList<>(messages));
    }

    /**
     * Deletes all messages for a specified chat memory.
     *
     * @param memoryId The ID of the chat memory.
     */
    @Override
    public void deleteMessages(Object memoryId) {
        store.remove(memoryId);
    }
}
