package com.n23.foa.travelassistant.config;

import com.n23.foa.travelassistant.agents.TravelSupervisorAgent;
import com.n23.foa.travelassistant.agents.tools.*;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TravelSupervisorConfig {

    @Bean
    public TravelSupervisorAgent travelSupervisorAgent(
            @Qualifier("chatModel3") ChatModel chatModel,
            ChatMemoryProvider chatMemoryProvider,
            HotelSearchTool hotelSearchTool,
            TransportSearchTool transportSearchTool,
            BudgetAgentTool budgetAgent,
            ItineraryAgentTool itineraryAgent,
            LocalExpertAgentTool localExpertAgent,
            ResearchAgentTool researchAgent,
            DestinationImageTool destinationImageTool
    ) {
        return AiServices.builder(TravelSupervisorAgent.class)
                .chatModel(chatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .tools(hotelSearchTool, transportSearchTool, budgetAgent, itineraryAgent, localExpertAgent, researchAgent, destinationImageTool)
                .build();
    }
}
