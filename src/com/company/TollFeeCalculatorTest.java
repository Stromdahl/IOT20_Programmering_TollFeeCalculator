package com.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TollFeeCalculatorTest {
    TollFeeCalculator tollFeeCalculator = new TollFeeCalculator("testData/Lab4_f.txt");

    private final PrintStream standardOut = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setErr(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("Testing TollFeeCalculator")
    void testTollFeeCalculator(){
        //Test if IOException is caught if the given file doesn't exist
        String inputFile = "testData/notLab4.txt";
        new TollFeeCalculator(inputFile);
        assertEquals("Could not read file " + inputFile, outputStreamCaptor.toString().trim());

    }

    @Test
    @DisplayName("Testing parseStringsToDates")
    void testParseStringToDate(){
        //Test a string with a valid date
        String dateString = "2020-06-30 02:00";
        LocalDateTime date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        assertEquals(date, tollFeeCalculator.parseStringToDate(dateString));

        //Test a string with an invalid date
        assertNull(tollFeeCalculator.parseStringToDate("2020-06-32 02:00"));
    }

    @Test
    @DisplayName("Testing getTotalFeeCost")
    void testGetTotalFeeCost(){

        this.testParseStringToDate();

        //Test amount less than 60
        LocalDateTime[] datesTest1 = {
                tollFeeCalculator.parseStringToDate("2020-06-01 07:00"), //18
                tollFeeCalculator.parseStringToDate("2020-06-01 08:30"), //8
                tollFeeCalculator.parseStringToDate("2020-06-01 14:00"), //8
                tollFeeCalculator.parseStringToDate("2020-06-01 17:00")  //13
        };
        assertEquals(47, tollFeeCalculator.getTotalFeeCost(datesTest1));

        //test more amount than 60
        LocalDateTime[] datesTest2 = {
                tollFeeCalculator.parseStringToDate("2020-06-01 07:00"), //18
                tollFeeCalculator.parseStringToDate("2020-06-01 08:30"), //8
                tollFeeCalculator.parseStringToDate("2020-06-01 14:00"), //8
                tollFeeCalculator.parseStringToDate("2020-06-01 16:00"), //13
                tollFeeCalculator.parseStringToDate("2020-06-01 17:30"), //18
        };
        assertEquals(60, tollFeeCalculator.getTotalFeeCost(datesTest2));

        //test less than 60 minuted between dates
        LocalDateTime[] datesTest3 = {
                tollFeeCalculator.parseStringToDate("2020-06-01 06:20"), //8
                tollFeeCalculator.parseStringToDate("2020-06-01 06:40"), //13
                tollFeeCalculator.parseStringToDate("2020-06-01 07:10"), //18
                tollFeeCalculator.parseStringToDate("2020-06-01 17:00"), //13
                tollFeeCalculator.parseStringToDate("2020-06-01 18:00"), //8
        };
        assertEquals(31, tollFeeCalculator.getTotalFeeCost(datesTest3));

    }

    @Test
    @DisplayName("Testing getTollFeePerPassing")
    void testGetTollFeePerPassing(){

        this.testParseStringToDate();

        assertEquals(8, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 06:00")));
        assertEquals(13, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 06:30")));
        assertEquals(18, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 07:00")));
        assertEquals(13, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 08:00")));
        assertEquals(8, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 08:30")));
        assertEquals(8, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 14:00")));
        assertEquals(13, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 15:00")));
        assertEquals(18, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 15:30")));
        assertEquals(13, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 17:00")));
        assertEquals(8, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 18:00")));
        assertEquals(0, tollFeeCalculator.getTollFeePerPassing(tollFeeCalculator.parseStringToDate("2020-06-01 23:00")));
    }

    @Test
    @DisplayName("Testing isTollFreeDate")
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