package com.n23.foa.travelassistant.retriever;

import com.n23.foa.travelassistant.enums.TravelIntent;
import org.springframework.stereotype.Service;


@Service
public class RuleBasedTravelIntentClassifier implements TravelIntentClassifier{
    @Override
    public TravelIntent classify(String query) {
        if (query == null || query.isBlank()) {
            return TravelIntent.GENERAL;
        }

        String q = query.toLowerCase();

        if (q.contains("best time")
                || q.contains("weather")
                || q.contains("season")
                || q.contains("when to visit")
                || q.contains("which month")) {
            return TravelIntent.BEST_TIME;
        }

        if (q.contains("food")
                || q.contains("eat")
                || q.contains("dish")
                || q.contains("cuisine")) {
            return TravelIntent.FOOD;
        }

        if (q.contains("hotel")
                || q.contains("stay")
                || q.contains("accommodation")
                || q.contains("resort")) {
            return TravelIntent.ACCOMMODATION;
        }

        if (q.contains("budget")
                || q.contains("cost")
                || q.contains("expense")
                || q.contains("price")) {
            return TravelIntent.BUDGET;
        }

        if (q.contains("reach")
                || q.contains("transport")
                || q.contains("flight")
                || q.contains("train")
                || q.contains("bus")) {
            return TravelIntent.TRANSPORT;
        }

        if (q.contains("attraction")
                || q.contains("visit")
                || q.contains("places")
                || q.contains("sightseeing")) {
            return TravelIntent.ATTRACTIONS;
        }

        return TravelIntent.GENERAL;
    }
}
