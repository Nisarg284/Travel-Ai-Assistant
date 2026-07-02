package com.n23.foa.travelassistant.classifiers.imple;

import com.n23.foa.travelassistant.classifiers.TravelRequestClassifier;
import com.n23.foa.travelassistant.enums.TravelRequestType;
import org.springframework.stereotype.Service;


//@Service
public class DefaultTravelRequestClassifier {
//    @Override
    public TravelRequestType classify(String question) {

        String q = question.toLowerCase();

        if (
                q.contains("plan")
                || q.contains("itinerary")
                || q.contains("trip")
                || q.contains("days")
        ){
            return TravelRequestType.ITINERARY;
        }
        return TravelRequestType.QUESTION;
    }
}
