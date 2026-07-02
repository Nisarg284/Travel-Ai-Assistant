package com.n23.foa.travelassistant.config;

import com.n23.foa.travelassistant.agents.TripFeasibilityExpert;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TripFeasibilityExpertConfig {

    @Bean
    public TripFeasibilityExpert tripFeasibilityExpert(
            ChatModel chatModel
    ){
        return AiServices.create(
                TripFeasibilityExpert.class,
                chatModel
        );
    }
}
