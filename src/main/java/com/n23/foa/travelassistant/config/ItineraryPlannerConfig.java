package com.n23.foa.travelassistant.config;


import com.n23.foa.travelassistant.agents.ItineraryPlanner;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItineraryPlannerConfig {


//    @Bean
//    public ItineraryPlanner itineraryPlanner(
//            RetrievalAugmentor retrievalAugmentor
//    ){
//        return AiServices.builder(ItineraryPlanner.class)
//                .retrievalAugmentor(retrievalAugmentor)
//                .build();
//    }
}
