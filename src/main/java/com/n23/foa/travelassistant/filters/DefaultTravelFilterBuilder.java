package com.n23.foa.travelassistant.filters;


import com.n23.foa.travelassistant.enums.TravelIntent;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.logical.And;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Service
public class DefaultTravelFilterBuilder implements TravelFilterBuilder {


    @Override
    public Filter build(TravelIntent intent, Optional<String> destination) {
        List<Filter> filters = new ArrayList<>();

        destination.ifPresent(
                d -> filters.add
                        (metadataKey("destination")
                                .isEqualTo(d))
        );

        switch (intent){

            case FOOD -> filters.add(
                    metadataKey("section")
                            .isEqualTo("Food To Try")
            );


            case BUDGET ->
                    filters.add(
                            metadataKey("section")
                                    .isEqualTo("Estimated Budget")
                    );

            case ACCOMMODATION ->
                    filters.add(
                            metadataKey("section")
                                    .isEqualTo("Accommodation")
                    );

            case BEST_TIME ->
                    filters.add(
                            metadataKey("section")
                                    .isEqualTo("Best Time To Visit")
                    );

            case TRANSPORT ->
                    filters.add(
                            metadataKey("section")
                                    .isEqualTo("How To Reach")
                    );

            case ATTRACTIONS ->
                    filters.add(
                            metadataKey("section")
                                    .isEqualTo("Top Attractions")
                    );

            case GENERAL -> {

            }
        }

        if(filters.isEmpty()) {
            return null;
        }


        if(filters.size() == 1){
            return filters.getFirst();
        }

        Filter destinationFilter = filters.get(0);
        Filter sectionFilter = filters.get(1);

//        System.out.println(new And(destinationFilter,sectionFilter));

        return new And(destinationFilter,sectionFilter);
    }
}
