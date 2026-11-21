package com.n23.foa.travelassistant.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n23.foa.travelassistant.agents.TravelAssistant;
import com.n23.foa.travelassistant.dto.AiResponse;
import com.n23.foa.travelassistant.ingestion.IngestorTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class TravelAiController {

    private final TravelAssistant assistantService;
    private final IngestorTestService ingestorService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TravelAiController(TravelAssistant assistantService, IngestorTestService ingestorService) {
        this.assistantService = assistantService;
        this.ingestorService = ingestorService;
    }

    @RequestMapping(value = "/ingest", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> ingest() {
        try {
            int count = ingestorService.ingest();
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Successfully ingested " + count + " destination sections into Qdrant vector store."
            ));
        } catch (Exception e) {
            log.error("Failed to ingest knowledge base: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "ERROR",
                    "message", "Failed to ingest knowledge base: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/ask")
    public AiResponse askDefaultSession(@RequestBody(required = false) String question) {
        return ask("default-session", question);
    }

    @PostMapping("/ask/{sessionId}")
    public AiResponse ask(@PathVariable String sessionId, @RequestBody(required = false) String question) {
        if (question == null || question.isBlank()) {
            return AiResponse.chat("Please provide a question or travel plan request.");
        }

        String cleanQuestion = question.trim();

        // If enclosed in double quotes (e.g. JSON string payload), unwrap
        if (cleanQuestion.startsWith("\"") && cleanQuestion.endsWith("\"") && cleanQuestion.length() >= 2) {
            cleanQuestion = cleanQuestion.substring(1, cleanQuestion.length() - 1).trim();
        }

        // If JSON object format like {"question": "..."} or {"message": "..."}
        if (cleanQuestion.startsWith("{") && cleanQuestion.endsWith("}")) {
            try {
                JsonNode node = objectMapper.readTree(cleanQuestion);
                if (node.hasNonNull("question")) {
                    cleanQuestion = node.get("question").asText();
                } else if (node.hasNonNull("message")) {
                    cleanQuestion = node.get("message").asText();
                } else if (node.hasNonNull("prompt")) {
                    cleanQuestion = node.get("prompt").asText();
                }
            } catch (Exception e) {
                log.debug("Could not parse request body as JSON object, treating as raw string: {}", e.getMessage());
            }
        }

        return assistantService.chat(sessionId, cleanQuestion);
    }
}
