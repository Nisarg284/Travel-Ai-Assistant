package com.n23.foa.travelassistant.retriever.meltisection_retriever.imple;

import com.n23.foa.travelassistant.dto.TripConstraints;
import com.n23.foa.travelassistant.retriever.meltisection_retriever.MultiSectionRetriever;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.logical.And;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Slf4j
@Service
public class DefaultMultisectionRetriever
        implements MultiSectionRetriever {

    private static final List<String> SECTIONS =
            List.of(
                    "Top Attractions",
                    "Accommodation",
                    "Food To Try",
                    "How To Reach",
                    "Planning Metadata",
                    "Estimated Budget",
                    "Travel Tips"
            );
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public DefaultMultisectionRetriever(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }


    @Override
    public List<Content> retrieve(TripConstraints constraints) {

        String destinationRaw = constraints.destination();

        if (destinationRaw == null || destinationRaw.isBlank()) {
            return List.of();
        }

        // Capitalize first letter to match exact casing in MD files e.g. "kerala" -> "Kerala"
        String destination = destinationRaw.trim().substring(0, 1).toUpperCase() + 
                             destinationRaw.trim().substring(1).toLowerCase();

        List<Content> contents = new ArrayList<>();

        for (String section : SECTIONS) {
            Content content = retrieveSection(destination, section);

            if (content != null) {
                contents.add(content);
            }

            log.debug("Retrieving: {} -> {}", destination, section);
        }

        return contents;
    }

    private Content retrieveSection(String destination, String section) {
        Filter filter = new And(
                metadataKey("destination").isEqualTo(destination),
                metadataKey("section").isEqualTo(section)
        );

        String query = destination + " " + section;

        Embedding queryEmbedding = embeddingModel.embed(query).content();

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .filter(filter)
                .maxResults(3)
                .build();

        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(searchRequest);

        if (result.matches().isEmpty()) {
            return null;
        }

        TextSegment segment = result.matches().getFirst().embedded();

        return Content.from(segment);
    }
}
