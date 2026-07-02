package com.n23.foa.travelassistant.filters;

import com.n23.foa.travelassistant.enums.TravelIntent;
import dev.langchain4j.store.embedding.filter.Filter;

import java.util.Optional;

public interface TravelFilterBuilder {

    Filter build(
            TravelIntent intent,
            Optional<String> destination
    );
}
