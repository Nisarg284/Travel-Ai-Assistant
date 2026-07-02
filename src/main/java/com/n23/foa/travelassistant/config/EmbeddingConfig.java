package com.n23.foa.travelassistant.config;


import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class EmbeddingConfig {

    @Value("${qdrant.host-name}")
    private String qdrantHost;

    @Value("${qdrant.port}")
    private int port;

    @Value("${huggingface.api-key}")
    private String huggingFaceApiKey;

    @Value("${qdrant.collection-name}")
    private String collectionName;

    @Value("${qdrant.api-key}")
    private String qdrantApiKey;

    @Value("${huggingface.embedding-model}")
    private String embeddingModel;


    @Bean
    public EmbeddingModel embeddingModel(){
        return HuggingFaceEmbeddingModel.builder()
                .accessToken(huggingFaceApiKey)
                .modelId(embeddingModel)
                .waitForModel(true)
                .timeout(Duration.ofSeconds(15))
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(){
        return QdrantEmbeddingStore.builder()
                .apiKey(qdrantApiKey)
                .port(port)
                .host(qdrantHost)
//                .collectionName(collectionName)
                .collectionName("travel-docs-ingestor")
                .useTls(true)
                .build();
    }
}
