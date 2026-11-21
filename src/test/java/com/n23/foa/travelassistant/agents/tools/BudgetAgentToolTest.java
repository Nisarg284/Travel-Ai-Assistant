package com.n23.foa.travelassistant.agents.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BudgetAgentToolTest {

    private BudgetAgentTool budgetAgentTool;

    @BeforeEach
    void setUp() {
        budgetAgentTool = new BudgetAgentTool();
    }

    @Test
    void estimateBudget_calculatesTotalsForBackpacker() {
        String result = budgetAgentTool.estimateBudget("Goa", "3", "backpacker");
        assertNotNull(result);
        assertTrue(result.contains("Goa"));
        assertTrue(result.contains("backpacker"));
        // Daily: 800 + 500 + 300 + 200 = 1800; Total for 3 days = 5400
        assertTrue(result.contains("5400"));
    }

    @Test
    void estimateBudget_calculatesTotalsForLuxury() {
        String result = budgetAgentTool.estimateBudget("Jaipur", "2", "luxury");
        assertNotNull(result);
        assertTrue(result.contains("luxury"));
        // Daily: 8000 + 3000 + 2000 + 3000 = 16000; Total for 2 days = 32000
        assertTrue(result.contains("32000"));
    }

    @Test
    void estimateBudget_handlesNullOrInvalidInputsGracefully() {
        String resultNullDays = budgetAgentTool.estimateBudget("Manali", null, null);
        assertNotNull(resultNullDays);
        assertTrue(resultNullDays.contains("Manali"));

        String resultNullDest = budgetAgentTool.estimateBudget(null, "5", "comfort");
        assertNotNull(resultNullDest);
        assertTrue(resultNullDest.contains("valid destination"));
    }
}
