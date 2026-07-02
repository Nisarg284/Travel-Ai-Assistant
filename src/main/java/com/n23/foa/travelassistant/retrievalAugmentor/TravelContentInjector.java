package com.n23.foa.travelassistant.retrievalAugmentor;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.injector.ContentInjector;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TravelContentInjector
        implements ContentInjector {

    @Override
    public ChatMessage inject(List<Content> contents, ChatMessage chatMessage) {

//        System.out.println("<-----------------------------inside content injector----------------------------->");


        StringBuilder prompt = new StringBuilder();

        String question = "";

        if (chatMessage instanceof UserMessage userMessage){
            question = userMessage.singleText();
        }


        prompt.append("""
                You are an expert travel assistant.

                Use only the information provided below.
                
                If the provided information is insufficient,
                say that the information is not available.
                Do not make up facts.
                
                Answer in a friendly and concise manner.
                Use bullet points when appropriate.

                === Retrieved Information ===

                """);

        for(Content content: contents){

            TextSegment segment = content.textSegment();
//            System.out.println("<------------------segment text:" + segment.text()+"-------------------->");
            prompt.append(
                    """
                            Destination: %s
                            Section: %s
                            
                            content: %s
                            """.formatted(
                                    segment.metadata()
                                            .getString("destination"),

                            segment.metadata()
                                    .getString("section"),
                            segment.text()
                    )
            );
        }

        prompt.append("""
                
                === User Question ===
                
                %s
                """.formatted(question));

        System.out.println(
                "========== FINAL PROMPT =========="
        );

        System.out.println(prompt);

        System.out.println(
                "=================================="
        );

        return UserMessage.from(prompt.toString());
    }
}
