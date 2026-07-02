package com.n23.foa.travelassistant.config;


import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryProviderConfig {


    @Bean
    public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore store){
        return memoryId -> MessageWindowChatMemory.builder()
                .chatMemoryStore(store)
                .id(memoryId)
                .maxMessages(20)
                .build();
    }
}
