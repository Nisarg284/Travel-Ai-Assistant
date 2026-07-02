package com.n23.foa.travelassistant.classifiers;

import com.n23.foa.travelassistant.enums.TravelRequestType;

public interface TravelRequestClassifier {

    TravelRequestType classify(
            String question
    );
}
