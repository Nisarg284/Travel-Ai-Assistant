package com.n23.foa.travelassistant.retriever;

import org.springframework.stereotype.Service;

import java.util.Optional;

public interface DestinationExtractor {

    Optional<String> extract(String query);
}
