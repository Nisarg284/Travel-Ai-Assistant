package com.n23.foa.travelassistant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Fetches destination images from the Unsplash API.
 */
@Slf4j
@Service
public class UnsplashImageService {

    private final WebClient webClient;
    private final String apiKey;

    public UnsplashImageService(
            @Value("${unsplash.base-url:https://api.unsplash.com}") String baseUrl,
            @Value("${unsplash.api-key:}") String apiKey
    ) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @SuppressWarnings("unchecked")
    public List<String> searchImages(String query, int count) {
        if (apiKey == null || apiKey.isBlank()) {
            log.debug("Unsplash API key not configured — skipping image search");
            return List.of();
        }
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/photos")
                            .queryParam("query", query + " travel India")
                            .queryParam("per_page", count)
                            .queryParam("orientation", "landscape")
                            .build())
                    .header("Authorization", "Client-ID " + apiKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                return results.stream()
                        .map(r -> {
                            Map<String, Object> urls = (Map<String, Object>) r.get("urls");
                            return (String) urls.get("regular");
                        })
                        .toList();
            }
        } catch (Exception e) {
            log.warn("Failed to fetch images from Unsplash for query '{}': {}", query, e.getMessage());
        }
        return List.of();
    }
}
