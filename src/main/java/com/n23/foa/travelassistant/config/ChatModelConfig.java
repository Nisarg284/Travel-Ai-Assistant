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

    // @Value("${gemini.api-key}")
    // private String geminiApiKey;

    // @Value("${gemini.model-name}")
    // private String geminiModelName;

    // @Bean
    // @Primary
    // public ChatModel geminiConfigChatModel(){
    // return GoogleAiGeminiChatModel.builder()
    // .apiKey(geminiApiKey)
    // .modelName(geminiModelName)
    // .build();
    // }

    @Bean
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .baseUrl(baseUrl)
                .maxRetries(5)
                .build();
    }
}
