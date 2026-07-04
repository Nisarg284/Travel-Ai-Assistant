package com.n23.foa.travelassistant;

import com.n23.foa.travelassistant.ingestion.MarkdownSectionExtractor;
import com.n23.foa.travelassistant.ingestion.Test;
import com.n23.foa.travelassistant.ingestion.TravelIngestionService;
import com.n23.foa.travelassistant.ingestion.TravelSectionExtractor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class TravelAssistantApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TravelAssistantApplication.class, args);

        // TravelSectionExtractor extractor = new MarkdownSectionExtractor();
        //
        // Path mdPath =
        // Path.of("C:/Users/Nisarg/OneDrive/Desktop/java/Langchain4j/Projects/Travel
        // Assistant/knowledge-base/destinations/goa.md");
        //
        // System.out.println(Files.readString(mdPath));
        //
        //// TravelIngestionService ingestionService = new TravelIngestionService();
        //
        //
        // Test test =new Test(new TravelIngestionService());

    }

}
