package com.n23.foa.travelassistant.config;

import dev.langchain4j.model.chat.ChatModel;
//import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.Map;

@Configuration
public class ChatModelConfig {


    @Value("${sarvam.api-key:${SARVAM_API_KEY:demo-key}}")
    private String sarvamApiKey;

    @Value("${sarvam.model-name:${AI_MODEL_NAME:sarvam-105b-conversations}}")
    private String modelName;

    @Value("${sarvam.base-url:${AI_BASE_URL:https://api.sarvam.ai/v1}}")
    private String baseUrl;

    @Bean
    public ChatModel chatModel() {

        return OpenAiChatModel.builder()
                .apiKey(sarvamApiKey)
                .modelName(modelName)
                .baseUrl(baseUrl)
                .timeout(Duration.ofSeconds(180))
                .maxRetries(2)
                .build();
    }
}