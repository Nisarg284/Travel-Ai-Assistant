package com.n23.foa.travelassistant.ingestion;

import com.n23.foa.travelassistant.ingestion.dto.TravelSection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownSectionExtractorTest {

    private MarkdownSectionExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new MarkdownSectionExtractor();
    }

    @Test
    void extract_parsesDestinationAndSections() {
        String markdown = """
                # Goa
                
                ## Overview
                Goa is a popular beach destination on the west coast of India.
                
                ## Top Attractions
                * Baga Beach
                * Fort Aguada
                * Dudhsagar Falls
                
                ## Estimated Budget
                * Budget: ₹1,500/day
                """;

        List<TravelSection> sections = extractor.extract(markdown);
        assertNotNull(sections);
        assertEquals(3, sections.size());

        TravelSection s1 = sections.get(0);
        assertEquals("Goa", s1.destination());
        assertEquals("Overview", s1.section());
        assertTrue(s1.content().contains("beach destination"));

        TravelSection s2 = sections.get(1);
        assertEquals("Goa", s2.destination());
        assertEquals("Top Attractions", s2.section());
        assertTrue(s2.content().contains("Baga Beach"));

        TravelSection s3 = sections.get(2);
        assertEquals("Goa", s3.destination());
        assertEquals("Estimated Budget", s3.section());
    }

    @Test
    void extract_handlesEmptyInput() {
        List<TravelSection> sections = extractor.extract("");
        assertNotNull(sections);
        assertTrue(sections.isEmpty());
    }
}
