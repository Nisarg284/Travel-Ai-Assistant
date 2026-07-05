package com.n23.foa.travelassistant.config;


import com.n23.foa.travelassistant.agents.TravelKnowledgeAgent;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelKnowledgeAgentConfig {


    @Bean
    public TravelKnowledgeAgent travelKnowledgeAgent(
            @Qualifier("chatModel2") ChatModel chatModel,
            RetrievalAugmentor retrievalAugmentor,
            ChatMemoryProvider store
            )
    {
        return AiServices.builder(TravelKnowledgeAgent.class)
                .chatModel(chatModel)
                .chatMemoryProvider(store)
                .retrievalAugmentor(retrievalAugmentor)
                .build();
    }
}
