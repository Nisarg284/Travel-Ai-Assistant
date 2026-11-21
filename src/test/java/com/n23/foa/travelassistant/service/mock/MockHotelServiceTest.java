package com.n23.foa.travelassistant.service.mock;

import com.n23.foa.travelassistant.dto.HotelOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockHotelServiceTest {

    private MockHotelService hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new MockHotelService();
    }

    @Test
    void searchHotels_returnsHotelsWithinBudget() {
        List<HotelOption> hotels = hotelService.searchHotels("goa", 3000, "comfort");
        assertNotNull(hotels);
        assertFalse(hotels.isEmpty());
        for (HotelOption h : hotels) {
            assertTrue(h.pricePerNight() <= 3000);
        }
    }

    @Test
    void searchHotels_backpackerSortsByPriceAscending() {
        List<HotelOption> hotels = hotelService.searchHotels("jaipur", 10000, "backpacker");
        assertNotNull(hotels);
        assertTrue(hotels.size() >= 2);
        for (int i = 0; i < hotels.size() - 1; i++) {
            assertTrue(hotels.get(i).pricePerNight() <= hotels.get(i + 1).pricePerNight());
        }
    }

    @Test
    void searchHotels_luxurySortsByPriceDescending() {
        List<HotelOption> hotels = hotelService.searchHotels("goa", 20000, "luxury");
        assertNotNull(hotels);
        assertTrue(hotels.size() >= 2);
        for (int i = 0; i < hotels.size() - 1; i++) {
            assertTrue(hotels.get(i).pricePerNight() >= hotels.get(i + 1).pricePerNight());
        }
    }

    @Test
    void searchHotels_handlesNullOrEmptySafely() {
        List<HotelOption> nullDest = hotelService.searchHotels(null, 5000, "comfort");
        assertNotNull(nullDest);
        assertTrue(nullDest.isEmpty());

        List<HotelOption> emptyDest = hotelService.searchHotels("  ", 5000, "comfort");
        assertNotNull(emptyDest);
        assertTrue(emptyDest.isEmpty());
    }

    @Test
    void searchHotels_returnsEmptyForZeroBudget() {
        List<HotelOption> hotels = hotelService.searchHotels("goa", 100, "backpacker");
        assertNotNull(hotels);
        assertTrue(hotels.isEmpty());
    }
}
