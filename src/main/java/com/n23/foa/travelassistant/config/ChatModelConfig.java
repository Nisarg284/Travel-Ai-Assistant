package com.n23.foa.travelassistant.config;

import dev.langchain4j.model.chat.ChatModel;
//import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@Configuration
public class ChatModelConfig {


    @Value("${SARVAM_API_KEY}")
    private String sarvamApiKey;

    @Value("${AI_MODEL_NAME}")
    private String modelName;

    @Value("${AI_BASE_URL}")
    private String baseUrl;

    @Bean
    public ChatModel chatModel() {

        return OpenAiChatModel.builder()
                .apiKey(sarvamApiKey)
                .modelName(modelName)
                .baseUrl(baseUrl)
                .build();
    }
}