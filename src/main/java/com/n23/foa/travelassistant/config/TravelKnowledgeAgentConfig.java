package com.n23.foa.travelassistant.config;


import com.n23.foa.travelassistant.agents.TravelAssistant;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelAssistantConfig {


    @Bean
    public TravelAssistant travelAssistant(
            ChatModel chatModel,
            RetrievalAugmentor retrievalAugmentor
            )
    {
        return AiServices.builder(TravelAssistant.class)
                .chatModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .retrievalAugmentor(retrievalAugmentor)
                .build();
    }
}
