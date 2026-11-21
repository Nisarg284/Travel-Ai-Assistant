package com.n23.foa.travelassistant.retriever;

import com.n23.foa.travelassistant.enums.TravelIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RuleBasedTravelIntentClassifierTest {

    private RuleBasedTravelIntentClassifier classifier;

    @BeforeEach
    void setUp() {
        classifier = new RuleBasedTravelIntentClassifier();
    }

    @Test
    void classify_foodQueries() {
        assertEquals(TravelIntent.FOOD, classifier.classify("What is the best food in Goa?"));
        assertEquals(TravelIntent.FOOD, classifier.classify("Best dish to eat in Jaipur"));
        assertEquals(TravelIntent.FOOD, classifier.classify("Authentic Kerala cuisine"));
    }

    @Test
    void classify_accommodationQueries() {
        assertEquals(TravelIntent.ACCOMMODATION, classifier.classify("Best hotel to stay in Manali"));
        assertEquals(TravelIntent.ACCOMMODATION, classifier.classify("Budget accommodation in Goa"));
        assertEquals(TravelIntent.ACCOMMODATION, classifier.classify("Luxury resort near beach"));
    }

    @Test
    void classify_budgetQueries() {
        assertEquals(TravelIntent.BUDGET, classifier.classify("What is the estimated budget for 3 days?"));
        assertEquals(TravelIntent.BUDGET, classifier.classify("Trip cost breakdown for Jaipur"));
        assertEquals(TravelIntent.BUDGET, classifier.classify("Approximate expense for Goa"));
    }

    @Test
    void classify_attractionQueries() {
        assertEquals(TravelIntent.ATTRACTIONS, classifier.classify("Top attractions in Kerala"));
        assertEquals(TravelIntent.ATTRACTIONS, classifier.classify("Famous places to visit in Manali"));
    }

    @Test
    void classify_transportQueries() {
        assertEquals(TravelIntent.TRANSPORT, classifier.classify("How to reach Goa by train?"));
        assertEquals(TravelIntent.TRANSPORT, classifier.classify("Flight options from Delhi"));
    }

    @Test
    void classify_bestTimeQueries() {
        assertEquals(TravelIntent.BEST_TIME, classifier.classify("What is the best time to visit Manali?"));
        assertEquals(TravelIntent.BEST_TIME, classifier.classify("How is the weather in Goa in December?"));
    }

    @Test
    void classify_generalQueries() {
        assertEquals(TravelIntent.GENERAL, classifier.classify("Hello there"));
        assertEquals(TravelIntent.GENERAL, classifier.classify("Is Goa safe for solo female travellers?"));
    }
}
