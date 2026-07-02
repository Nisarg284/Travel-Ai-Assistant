package com.n23.foa.travelassistant.agents;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface TravelKnowledgeAgent {

    String chat(@MemoryId String sessionId,@UserMessage String question);

}
