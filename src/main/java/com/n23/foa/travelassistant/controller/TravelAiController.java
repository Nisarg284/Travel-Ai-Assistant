package com.n23.foa.travelassistant.controller;


import com.n23.foa.travelassistant.agents.TravelAssistant;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class TravelAiController {

    private final TravelAssistant assistantService;

    public TravelAiController(TravelAssistant assistantService) {
        this.assistantService = assistantService;
    }

    @PostMapping("/ask/{sessionId}")
    public com.n23.foa.travelassistant.dto.AiResponse ask(@PathVariable String sessionId, @RequestBody String question) {
        return assistantService.chat(sessionId, question);
    }
}
