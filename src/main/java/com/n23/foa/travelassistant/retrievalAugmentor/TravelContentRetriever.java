package com.n23.foa.travelassistant.retrievalAugmentor;

import com.n23.foa.travelassistant.enums.TravelIntent;
import com.n23.foa.travelassistant.filters.TravelFilterBuilder;
import com.n23.foa.travelassistant.retriever.DestinationExtractor;
import com.n23.foa.travelassistant.retriever.TravelIntentClassifier;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class TravelContentRetriever implements ContentRetriever {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final TravelIntentClassifier intentClassifier;
    private final DestinationExtractor destinationExtractor;
    private final TravelFilterBuilder filterBuilder;

    public TravelContentRetriever(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore, TravelIntentClassifier intentClassifier, DestinationExtractor destinationExtractor, TravelFilterBuilder filterBuilder) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.intentClassifier = intentClassifier;
        this.destinationExtractor = destinationExtractor;
        this.filterBuilder = filterBuilder;
    }

    @Override
    public List<Content> retrieve(Query query) {

        String question = query.text();

        TravelIntent intent =
                intentClassifier.classify(question);

        Optional<String> destination =
                destinationExtractor.extract(question);

        Filter filter = filterBuilder.build(
                intent,
                destination
        );

        Embedding queryEmbedding =
                embeddingModel
                        .embed(question)
                        .content();

        EmbeddingSearchRequest searchRequest =
                EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .filter(filter)
                .maxResults(5)
                .build();

        EmbeddingSearchResult<TextSegment> result =
                embeddingStore.search(searchRequest);

        List<Content> contentList = result.matches()
                .stream()
                .map(
                        match -> Content.from(match.embedded())
                )
                .toList();

        contentList.forEach( c->{

            System.out.println(c);
            System.out.println();

                }
        );


        return contentList;
    }
}
