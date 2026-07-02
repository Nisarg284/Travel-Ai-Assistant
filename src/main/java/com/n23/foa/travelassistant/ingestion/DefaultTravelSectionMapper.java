package com.n23.foa.travelassistant.ingestion;

import com.n23.foa.travelassistant.ingestion.dto.TravelSection;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class DefaultTravelSectionMapper implements TravelSectionMapper{
    @Override
    public TextSegment map(TravelSection section) {


        Map<String,String> metaMap = Map.of(
                "destination", section.destination(),
                "section", section.section()
        );
        Metadata metadata = Metadata.from(metaMap);
        TextSegment segment =  TextSegment.from(section.content(),metadata);

        return segment;
    }
}
