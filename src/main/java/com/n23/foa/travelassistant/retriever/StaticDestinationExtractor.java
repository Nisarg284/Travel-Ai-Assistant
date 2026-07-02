package com.n23.foa.travelassistant.retriever;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class StaticDestinationExtractor implements DestinationExtractor {

    private static final Map<String, String> DESTINATIONS =
            Map.of(
                    "goa", "Goa",
                    "jaipur", "Jaipur",
                    "udaipur", "Udaipur",
                    "manali", "Manali",
                    "shimla", "Shimla",
                    "kashmir","Kashmir",
                    "kerala","Kerala",
                    "rishikesh","Rishikesh",
                    "tarkarli","Tarkarli",
                    "munnar","Munnar"
//                    "khajuraho","Khajuraho"
//                    "kutch","Kutch"
            );


    @Override
    public Optional<String> extract(String query) {
        String q = query.toLowerCase();
//        return null;
        return DESTINATIONS.entrySet()
                .stream()
                .filter(entry -> q.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();


    }

}
