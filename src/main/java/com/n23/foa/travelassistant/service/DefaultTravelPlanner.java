package com.n23.foa.travelassistant.service;


import com.n23.foa.travelassistant.agents.ItineraryPlanner;
import com.n23.foa.travelassistant.agents.TravelPlannerAgent;
import com.n23.foa.travelassistant.dto.TripConstraints;
import com.n23.foa.travelassistant.dto.ValidationResult;
import com.n23.foa.travelassistant.extractors.TripConstraintsExtractor;
import com.n23.foa.travelassistant.memory.InMemoryTravelSessionMemory;
import com.n23.foa.travelassistant.retriever.meltisection_retriever.MultiSectionRetriever;
import dev.langchain4j.rag.content.Content;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DefaultTravelPlanner implements TravelPlannerAgent {


    private final TripConstraintsExtractor tripConstraintsExtractor;
    private final MultiSectionRetriever multiSectionRetriever;
    private final ItineraryPlanner itineraryPlanner;
    private final TripFeasibilityValidator tripFeasibilityValidator;
    private final InMemoryTravelSessionMemory memory;

    public DefaultTravelPlanner(TripConstraintsExtractor tripConstraintsExtractor, MultiSectionRetriever multiSectionRetriever, ItineraryPlanner itineraryPlanner, TripFeasibilityValidator tripFeasibilityValidator, InMemoryTravelSessionMemory memory) {
        this.tripConstraintsExtractor = tripConstraintsExtractor;
        this.multiSectionRetriever = multiSectionRetriever;
        this.itineraryPlanner = itineraryPlanner;
        this.tripFeasibilityValidator = tripFeasibilityValidator;
        this.memory = memory;
    }


    @Override
    public String plan(String question) {
        TripConstraints constraints = tripConstraintsExtractor.extract(question);

        List<Content> contents = multiSectionRetriever.retrieve(constraints);

        ValidationResult validator =
                tripFeasibilityValidator.validate(constraints, contents);


        if (!validator.feasible()){
            return """
                    Trip not feasible.
                    
                    Reason:
                    %s
                    
                    Suggestion:
                    %s
                    """.formatted(
                    validator.reason(),
                    validator.suggestion()
            );

        }
        System.out.println(constraints);

        contents.forEach(
                content -> System.out.println(
                        content.textSegment().metadata()
                )
        );

        System.out.println(validator);





        String plan = itineraryPlanner.plan(constraints, contents,validator);

//        TravelSession session = new TravelSession(
//                constraints.destination(),
//                constraints.days(),
//                constraints.budget(),
//                plan
//        );


        System.out.println(plan);
        return plan;
    }
}
