package com.n23.foa.travelassistant.retriever.meltisection_retriever;

import com.n23.foa.travelassistant.dto.TripConstraints;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.Content;

import java.util.List;

public interface MultiSectionRetriever{

    List<Content> retrieve(
            TripConstraints constraints
    );
}
