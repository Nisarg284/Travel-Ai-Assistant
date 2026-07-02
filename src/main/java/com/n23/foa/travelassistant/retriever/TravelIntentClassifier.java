package com.n23.foa.travelassistant.retriever;

import com.n23.foa.travelassistant.enums.TravelIntent;
import org.springframework.stereotype.Service;

public interface TravelIntentClassifier {
    TravelIntent classify(String query);
}
