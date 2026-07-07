package com.n23.foa.travelassistant.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@Configuration
public class ChatModelConfig {

    // @Value("${groq.api-key}")
    // private String apiKey;
    //
    // @Value("${groq.model-name}")
    // private String modelName;
    //
    // @Value("${groq.base-url}")
    // private String baseUrl;
    //
    // @Value("${groq.api-key-2}")
    // private String apiKey2;
    //
    // @Value("${groq.api-key-3}")
    // private String apiKey3;

    @Value("${openrouter.api-key}")
    private String apiKey;

    @Value("${openrouter.model-name}")
    private String modelName;

    @Value("${openrouter.base-url}")
    private String baseUrl;

    @Value("${openrouter.api-key-2}")
    private String apiKey2;

    @Value("${openrouter.api-key-3}")
    private String apiKey3;

    // @Value("${groq.api-key}")
    private String header = "Bearer ";

    @Bean
    public ChatModel chatModel1() {
        // return OpenAiChatModel.builder()
        // .apiKey(apiKey)
        // .modelName(modelName)
        // .baseUrl(baseUrl)
        // .build();

        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .maxTokens(4096)
                .build();
    }

    @Bean
    public ChatModel chatModel2() {
        // return OpenAiChatModel.builder()
        // .apiKey(apiKey2)
        // .modelName(modelName)
        // .baseUrl(baseUrl)
        //
        // .build();
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .maxTokens(4096)
                .build();
    }

    @Bean
    public ChatModel chatModel3Raw() {
        // return OpenAiChatModel.builder()
        // .apiKey(apiKey3)
        // .modelName(modelName)
        // .baseUrl(baseUrl)
        //
        // .build();
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .maxTokens(4096)
                .build();
    }

    @Bean
    @Primary
    public ChatModel chatModel(ChatModel chatModel1, ChatModel chatModel2, ChatModel chatModel3Raw) {
        return createRoundRobin(chatModel1, chatModel2, chatModel3Raw);
    }

    @Bean("chatModel3")
    public ChatModel chatModel3(ChatModel chatModel1, ChatModel chatModel2, ChatModel chatModel3Raw) {
        return createRoundRobin(chatModel1, chatModel2, chatModel3Raw);
    }

    private ChatModel createRoundRobin(ChatModel... models) {
        java.util.concurrent.atomic.AtomicInteger index = new java.util.concurrent.atomic.AtomicInteger(0);
        return (ChatModel) java.lang.reflect.Proxy.newProxyInstance(
                ChatModel.class.getClassLoader(),
                new Class<?>[] { ChatModel.class },
                (proxy, method, args) -> {
                    int i = Math.abs(index.getAndIncrement()) % models.length;
                    return method.invoke(models[i], args);
                });
    }
}
