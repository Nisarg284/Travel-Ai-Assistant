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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

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

        String destination = constraints.destination();

        if (destination == null){
            return List.of();
        }

        List<Content> contents =
                new ArrayList<>();

        for (String section : SECTIONS){
            Content content = retrieveSection(
                    destination,
                    section
            );

            if (content != null){
                contents.add(content);
            }

            System.out.println(
                    "Retrieving : "
                            + destination
                            + " -> "
                            + section
            );
        }



        return contents;
    }

    private Content retrieveSection(
            String destination,
            String section
    ) {
        Filter filter = new And(
                metadataKey("destination")
                        .isEqualTo(destination),

                metadataKey("section")
                        .isEqualTo(section)
        );

        String query = destination + " " + section;

        Embedding queryEmbedding = embeddingModel
                .embed(query)
                .content();

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .filter(filter)
                .maxResults(3)
                .build();

        EmbeddingSearchResult<TextSegment> result =
                embeddingStore.search(searchRequest);

        if (result.matches().isEmpty()){
            return null;
        }

        TextSegment segment = result.matches()
                .getFirst()
                .embedded();


//        System.out.println(
//                "\n================================"
//        );
//
//        System.out.println(
//                "Destination : " + destination
//        );
//
//        System.out.println(
//                "Section     : " + section
//        );
//
//        System.out.println(
//                "Metadata    : "
//                        + segment.metadata()
//        );
//
//        System.out.println(
//                "Text        :\n"
//                        + segment.text()
//        );
//
//        System.out.println(
//                "================================"
//        );


        return Content.from(segment);
    }



}
