package com.n23.foa.travelassistant.ingestion;


import com.n23.foa.travelassistant.ingestion.dto.TravelSection;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class TravelIngestionService {

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;

    private final TravelSectionExtractor sectionExtractor;

    private final TravelSectionMapper sectionMapper;


    public TravelIngestionService(EmbeddingModel embeddingModel,
                                  EmbeddingStore<TextSegment> embeddingStore, TravelSectionExtractor sectionExtractor, TravelSectionMapper sectionMapper) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.sectionExtractor = sectionExtractor;
        this.sectionMapper = sectionMapper;
    }



    public void uploadFile() throws IOException {

        Path mdPath = Paths.get("knowledge-base", "destinations", "goa.md");

        ingest(mdPath);
    }

    public void ingest(Path markdownFile) throws IOException {
        String content = Files.readString(markdownFile);


        List<TravelSection> sections = sectionExtractor.extract(content);

        List<TextSegment> list = sections.stream()
                .map(sectionMapper::map)
                .peek(segment -> log.debug("Ingesting segment: {}", segment))
                .toList();


        list.forEach(segment -> {
            Embedding embedding = embeddingModel.embed(segment).content();

            embeddingStore.add(embedding,segment);
        });
    }
}
