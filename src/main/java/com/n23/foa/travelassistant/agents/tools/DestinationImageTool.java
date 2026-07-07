package com.n23.foa.travelassistant.agents.tools;

import com.n23.foa.travelassistant.service.UnsplashImageService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Destination Image Tool — fetches relevant travel images from Unsplash.
 * Zero LLM calls — uses the Unsplash REST API.
 */
@Slf4j
@Component
public class DestinationImageTool {

    private final UnsplashImageService unsplashImageService;

    public DestinationImageTool(UnsplashImageService unsplashImageService) {
        this.unsplashImageService = unsplashImageService;
    }

    @Tool("Fetch destination travel images. Returns URLs of high-quality landscape photos for a travel destination.")
    public String fetchImages(
            @P("The travel destination name, e.g. Goa, Jaipur, Kerala, Manali") String destination
    ) {
        log.info("📸 DestinationImageTool invoked — destination: {}", destination);

        List<String> imageUrls = unsplashImageService.searchImages(destination, 3);

        if (imageUrls.isEmpty()) {
            return "No images found for " + destination + ".";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Destination Images: ").append(destination).append(" ===\n\n");
        for (int i = 0; i < imageUrls.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, imageUrls.get(i)));
        }

        log.info("📸 DestinationImageTool completed — {} images for {}", imageUrls.size(), destination);
        return sb.toString();
    }
}
