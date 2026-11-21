package com.n23.foa.travelassistant.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    @Test
    void health_returnsUp() {
        HealthController controller = new HealthController();
        ResponseEntity<String> response = controller.health();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("UP", response.getBody());
    }
}
