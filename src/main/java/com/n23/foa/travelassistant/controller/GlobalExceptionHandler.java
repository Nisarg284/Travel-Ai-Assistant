package com.n23.foa.travelassistant.controller;

import com.n23.foa.travelassistant.dto.AiResponse;
import dev.langchain4j.service.output.OutputParsingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler — catches unhandled exceptions and returns
 * meaningful responses to the frontend instead of raw 500 errors.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OutputParsingException.class)
    public ResponseEntity<AiResponse> handleOutputParsingException(OutputParsingException e) {
        log.warn("LLM returned unparseable response: {}", e.getMessage());

        // Extract the raw text from the exception if the LLM returned a conversational response
        String rawText = e.getMessage();
        if (rawText != null && rawText.contains("\"")) {
            // Try to extract meaningful text from the error
            int start = rawText.indexOf("\"");
            int end = rawText.lastIndexOf("\"");
            if (start != end) {
                rawText = rawText.substring(start + 1, end);
            }
        }

        AiResponse response = AiResponse.chat(
                rawText != null ? rawText : "I couldn't generate a complete plan. Please try rephrasing your request."
        );
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AiResponse> handleGenericException(Exception e) {
        log.error("Unexpected error: ", e);

        AiResponse response = AiResponse.chat(
                "Something went wrong while processing your request. Please try again."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
