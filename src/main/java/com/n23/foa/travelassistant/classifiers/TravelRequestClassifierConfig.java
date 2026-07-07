package com.n23.foa.travelassistant.classifiers;


import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelRequestClassifierConfig {


    @Bean
    public TravelRequestClassifier classifier(@Qualifier("chatModel2") ChatModel chatModel){
        return AiServices.builder(TravelRequestClassifier.class)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .chatModel(chatModel)
                .build();

    }
}
