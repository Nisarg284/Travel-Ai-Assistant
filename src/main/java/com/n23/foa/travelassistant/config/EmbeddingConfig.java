package com.n23.foa.travelassistant.config;


import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {

    @Value("${qdrant.host-name:localhost}")
    private String qdrantHost;

    @Value("${qdrant.port:6334}")
    private int port;

    @Value("${qdrant.collection-name:travel-docs-ingestor}")
    private String collectionName;

    @Value("${qdrant.api-key:demo-key}")
    private String qdrantApiKey;

    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return QdrantEmbeddingStore.builder()
                .apiKey(qdrantApiKey)
                .port(port)
                .host(qdrantHost)
                .collectionName(collectionName)
                .useTls(true)
                .build();
    }
}
