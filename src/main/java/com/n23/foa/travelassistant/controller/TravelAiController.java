package com.n23.foa.travelassistant.controller;


import com.n23.foa.travelassistant.agents.TravelAssistant;
import com.n23.foa.travelassistant.ingestion.Test;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class TravelAiController {

    private final TravelAssistant assistantService;
    private final Test ingestorService;

    public TravelAiController(TravelAssistant assistantService, Test ingestorService) {
        this.assistantService = assistantService;
        this.ingestorService = ingestorService;
    }

    @GetMapping("/ask")
    public String ask(@RequestBody String question){
        return assistantService.chat(question);
    }

    @PostMapping("/ingest")
    public void ingest() throws IOException {
        ingestorService.test2();
    }


}
