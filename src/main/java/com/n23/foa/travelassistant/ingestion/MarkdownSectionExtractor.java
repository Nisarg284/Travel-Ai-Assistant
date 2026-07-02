package com.n23.foa.travelassistant.ingestion;

import com.n23.foa.travelassistant.ingestion.dto.TravelSection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkdownSectionExtractor implements TravelSectionExtractor {
    @Override
    public List<TravelSection> extract(String markdownContent) {
        List<TravelSection> allSections = new ArrayList<>();

        String currentDestination = null;
        String currentSection = null;
        StringBuilder currentContent = new StringBuilder();

        String[] lines = markdownContent.split("\\r?\\n");
        for(String line : lines){

            if(line.startsWith("# "))
            {
                currentDestination = line.substring(2).trim();
            }else if(line.startsWith("## "))
            {
                if(!currentContent.isEmpty()){
                    TravelSection section = new TravelSection(currentDestination, currentSection,currentContent.toString().trim());
//                    System.out.println(section);
                    allSections.add(section);
                    currentContent.delete(0,currentContent.length());
                }
                currentSection = line.substring(3).trim();
            } else if (!line.isBlank()) {

                currentContent.append(line).append("\n");
            }
        }

        if(currentSection != null && !currentContent.isEmpty()){
            allSections.add(new TravelSection(currentDestination,currentSection,currentContent.toString().trim()));
        }
        return allSections;

    }
}
