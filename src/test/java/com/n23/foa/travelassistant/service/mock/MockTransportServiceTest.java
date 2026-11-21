package com.n23.foa.travelassistant.service.mock;

import com.n23.foa.travelassistant.dto.TransportOption;
import com.n23.foa.travelassistant.enums.TravelMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockTransportServiceTest {

    private MockTransportService transportService;

    @BeforeEach
    void setUp() {
        transportService = new MockTransportService();
    }

    @Test
    void searchTransport_returnsOptionsForValidRoute() {
        List<TransportOption> options = transportService.searchTransport("delhi", "goa", "any");
        assertNotNull(options);
        assertFalse(options.isEmpty());
    }

    @Test
    void searchTransport_filtersByPreferredMode() {
        List<TransportOption> flightOptions = transportService.searchTransport("delhi", "goa", "flight");
        assertNotNull(flightOptions);
        assertFalse(flightOptions.isEmpty());
        for (TransportOption opt : flightOptions) {
            assertEquals(TravelMode.Flight, opt.mode());
        }
    }

    @Test
    void searchTransport_handlesNullAndEmptySafely() {
        List<TransportOption> nullOrigin = transportService.searchTransport(null, "goa", "any");
        assertNotNull(nullOrigin);
        assertFalse(nullOrigin.isEmpty()); // Should fallback to delhi

        List<TransportOption> nullDest = transportService.searchTransport("delhi", null, "any");
        assertNotNull(nullDest);
        assertTrue(nullDest.isEmpty());
    }

    @Test
    void getSupportedOrigins_returnsOriginsForSupportedDestination() {
        List<String> origins = transportService.getSupportedOrigins("goa");
        assertNotNull(origins);
        assertTrue(origins.contains("Delhi") || origins.contains("Mumbai"));
    }

    @Test
    void getSupportedOrigins_handlesNullSafely() {
        List<String> origins = transportService.getSupportedOrigins(null);
        assertNotNull(origins);
        assertTrue(origins.isEmpty());
    }
}
