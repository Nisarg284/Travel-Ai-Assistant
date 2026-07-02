package com.n23.foa.travelassistant.ingestion;


import com.n23.foa.travelassistant.ingestion.dto.TravelSection;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

        Path mdPath = Path.of("C:/Users/Nisarg/OneDrive/Desktop/java/Langchain4j/Projects/Travel Assistant/knowledge-base/destinations/goa.md");

        ingest(mdPath);
    }

    public void ingest(Path markdownFile) throws IOException {
        String content = Files.readString(markdownFile);


        List<TravelSection> sections = sectionExtractor.extract(content);

        List<TextSegment> list = sections.stream()
                .map(sectionMapper::map)
                .peek(System.out::println)
                .toList();


        list.forEach(segment -> {
            Embedding embedding = embeddingModel.embed(segment).content();

            embeddingStore.add(embedding,segment);
        });
    }
}
