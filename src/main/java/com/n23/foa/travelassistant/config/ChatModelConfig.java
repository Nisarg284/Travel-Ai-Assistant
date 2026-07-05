package com.n23.foa.travelassistant.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatModelConfig {

    @Value("${groq.api-key}")
    private String apiKey;

    @Value("${groq.model-name}")
    private String modelName;

    @Value("${groq.base-url}")
    private String baseUrl;

    @Value("${groq.api-key-2}")
    private String apiKey2;

    @Value("${groq.api-key-3}")
    private String apiKey3;

    @Bean
    @org.springframework.context.annotation.Primary
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .baseUrl(baseUrl)
//                .maxRetries(5)
                .build();
    }

    @Bean("chatModel2")
    public ChatModel chatModel2() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey2)
                .modelName(modelName)
                .baseUrl(baseUrl)
//                .maxRetries(5)
                .build();
    }

    @Bean("chatModel3")
    public ChatModel chatModel3() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey3)
                .modelName(modelName)
                .baseUrl(baseUrl)
//                .maxRetries(5)
                .build();
    }
}
