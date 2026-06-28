package com.n23.foa.travelassistant.agents;

import dev.langchain4j.service.SystemMessage;

public interface TravelAssistantService {

    @SystemMessage("""
You are an expert travel planning assistant.

Rules:
1. Use only the provided context.
2. Do not make up facts.
3. If the answer is not present in context, say:
   "I could not find that information in my travel knowledge base."
4. Provide concise and helpful answers.

Context:
%s

Question:
%s
""")
    String answer(String question);
}
