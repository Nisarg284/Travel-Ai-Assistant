package com.n23.foa.travelassistant.filters;

import com.n23.foa.travelassistant.enums.TravelIntent;
import dev.langchain4j.store.embedding.filter.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DefaultTravelFilterBuilderTest {

    private DefaultTravelFilterBuilder filterBuilder;

    @BeforeEach
    void setUp() {
        filterBuilder = new DefaultTravelFilterBuilder();
    }

    @Test
    void build_createsFilterWithDestinationAndIntent() {
        Filter filter = filterBuilder.build(TravelIntent.FOOD, Optional.of("Goa"));
        assertNotNull(filter);
    }

    @Test
    void build_createsFilterWithOnlyDestination() {
        Filter filter = filterBuilder.build(TravelIntent.GENERAL, Optional.of("Jaipur"));
        assertNotNull(filter);
    }

    @Test
    void build_createsFilterWithOnlyIntent() {
        Filter filter = filterBuilder.build(TravelIntent.ACCOMMODATION, Optional.empty());
        assertNotNull(filter);
    }

    @Test
    void build_returnsNullWhenNoCriteria() {
        Filter filter = filterBuilder.build(TravelIntent.GENERAL, Optional.empty());
        assertNull(filter);
    }

    @Test
    void build_handlesNullIntentSafely() {
        Filter filter = filterBuilder.build(null, Optional.of("Manali"));
        assertNotNull(filter);
    }
}
