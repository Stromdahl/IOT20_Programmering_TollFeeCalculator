package com.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TollFeeCalculatorTest {
    TollFeeCalculator tollFeeCalculator = new TollFeeCalculator("testData/Lab4_f.txt");

    @Test
    @DisplayName("Testing getTollFeePerPassing")
    void testGetTollFeePerPassing(){
        LocalDateTime date = LocalDateTime.parse("2020-06-01 06:20", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        assertEquals(8, tollFeeCalculator.getTollFeePerPassing(date));
    }

    @Test
    @DisplayName("Tesing isTollFreeDate")
    void testIsTollFreeDate(){
        // Create a date that is not weekend and not month 7
        LocalDateTime date = LocalDateTime.parse("2020-06-01 00:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        // Create a date that is on a saturday
        LocalDateTime dateWeekday6 = LocalDateTime.parse("2020-06-06 00:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        // Create a date that is on a sunday
        LocalDateTime dateWeekday7 = LocalDateTime.parse("2020-06-07 00:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        // Create a date that is in July (month 7)
        LocalDateTime dateMonth7 = LocalDateTime.parse("2020-07-07 00:05", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        assertFalse(tollFeeCalculator.isTollFreeDate(date));
        assertTrue(tollFeeCalculator.isTollFreeDate(dateWeekday6));
        assertTrue(tollFeeCalculator.isTollFreeDate(dateWeekday7));
        assertTrue(tollFeeCalculator.isTollFreeDate(dateMonth7));
    }

}