package com.n23.foa.travelassistant.retriever;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StaticDestinationExtractorTest {

    private StaticDestinationExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new StaticDestinationExtractor();
    }

    @Test
    void extract_detectsKnownDestinations() {
        Optional<String> goa = extractor.extract("Plan a 3-day trip to Goa under 20k");
        assertTrue(goa.isPresent());
        assertEquals("Goa", goa.get());

        Optional<String> jaipur = extractor.extract("What are the best places in jaipur?");
        assertTrue(jaipur.isPresent());
        assertEquals("Jaipur", jaipur.get());

        Optional<String> kerala = extractor.extract("I want to visit Kerala next month");
        assertTrue(kerala.isPresent());
        assertEquals("Kerala", kerala.get());

        Optional<String> manali = extractor.extract("Budget trip to MANALI");
        assertTrue(manali.isPresent());
        assertEquals("Manali", manali.get());
    }

    @Test
    void extract_returnsEmptyForUnknownDestination() {
        Optional<String> unknown = extractor.extract("Plan a trip to Paris");
        assertTrue(unknown.isEmpty());

        Optional<String> empty = extractor.extract("");
        assertTrue(empty.isEmpty());
    }
}
