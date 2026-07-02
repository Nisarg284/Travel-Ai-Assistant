package com.n23.foa.travelassistant.config;

import com.n23.foa.travelassistant.retrievalAugmentor.TravelContentInjector;
import com.n23.foa.travelassistant.retrievalAugmentor.TravelContentRetriever;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetrievalAugmentorConfig {


    @Bean
    public RetrievalAugmentor retrievalAugmentor(
            TravelContentRetriever retriever,
            TravelContentInjector injector
    ){
        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(retriever)
                .contentInjector(injector)
                .build();
    }
}
