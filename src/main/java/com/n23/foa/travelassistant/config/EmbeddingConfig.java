package com.n23.foa.travelassistant.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.OnnxEmbeddingModel;
import dev.langchain4j.model.embedding.onnx.PoolingMode;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Configuration
public class EmbeddingConfig {

    @Value("${qdrant.host-name:localhost}")
    private String qdrantHost;

    @Value("${qdrant.port:6334}")
    private int port;

    @Value("${qdrant.collection-name:${QDRANT_COLLECTION:travel-ai-ingestor}}")
    private String collectionName;

    @Value("${qdrant.api-key:demo-key}")
    private String qdrantApiKey;

    @Bean
    public EmbeddingModel embeddingModel() {
        // Stream-extract model files to temp directory in 8KB chunks to prevent JVM heap spikes
        try {
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "onnx-allminilm");
            Files.createDirectories(tempDir);
            Path localModel = tempDir.resolve("all-minilm-l6-v2.onnx");
            Path localTokenizer = tempDir.resolve("all-minilm-l6-v2-tokenizer.json");

            if (!Files.exists(localModel) || Files.size(localModel) == 0) {
                try (InputStream in = getClass().getResourceAsStream("/all-minilm-l6-v2.onnx")) {
                    if (in != null) {
                        Files.copy(in, localModel, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }

            if (!Files.exists(localTokenizer) || Files.size(localTokenizer) == 0) {
                try (InputStream in = getClass().getResourceAsStream("/all-minilm-l6-v2-tokenizer.json")) {
                    if (in != null) {
                        Files.copy(in, localTokenizer, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }

            if (Files.exists(localModel) && Files.exists(localTokenizer)) {
                log.info("Loading ONNX embedding model from cached disk path: {}", localModel);
                return new OnnxEmbeddingModel(
                        localModel.toAbsolutePath().toString(),
                        localTokenizer.toAbsolutePath().toString(),
                        PoolingMode.MEAN
                );
            }
        } catch (Exception e) {
            log.warn("Could not load ONNX model from disk, falling back to default: {}", e.getMessage());
        }

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
