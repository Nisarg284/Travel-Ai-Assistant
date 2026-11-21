package com.n23.foa.travelassistant.config.extractorConfig;

import com.n23.foa.travelassistant.extractors.TripConstraintsExtractor;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TripConstraintsExtractorConfig {

    @Bean
    public TripConstraintsExtractor tripConstraintsExtractor(
            ChatModel chatModel
    ){
        return AiServices.builder(TripConstraintsExtractor.class)
                .chatModel(chatModel)
                .build();
    }
}
