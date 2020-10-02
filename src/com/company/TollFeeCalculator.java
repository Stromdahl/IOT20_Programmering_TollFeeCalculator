package com.company;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


public class TollFeeCalculator {

    public TollFeeCalculator(String inputFile) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(inputFile));
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = parseStringsToDates(dateStrings);
            System.out.println("The total fee for the inputfile is " + getTotalFeeCost(dates));
        } catch(IOException e) {
            System.err.println("Could not read file " + inputFile);
        } finally {
            if(sc != null) sc.close(); // Todo: 8 file did not close when the reading is done
        }
    }

    public LocalDateTime[] parseStringsToDates(String[] dateStrings){
        LocalDateTime[] dates = new LocalDateTime[dateStrings.length];// todo: 1. new LocalDateTime[dateStrings.length-1];
        for(int i = 0; i < dates.length; i++) {
            try { // todo: 6. if inputed date is not valid then exeption is thrown
                dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (DateTimeException e) {
                System.err.printf("Could not parse %s, It´s not a valid date\n", dateStrings[i]);
            }
        }
        return dates;
    }

    private int getTotalFeeCost(LocalDateTime[] dates) {
        int totalFee = 0;
        LocalDateTime intervalStart = dates[0];
        int maxFeeIn60MinWindow = 0;
        for(LocalDateTime date: dates) {
            if(date == null) continue;
            long diffInMinutes = intervalStart.until(date, ChronoUnit.MINUTES);
            if(diffInMinutes > 60) {
                int fee = getTollFeePerPassing(date) + maxFeeIn60MinWindow;
                totalFee += fee;
                System.out.println(date.toString() + " - " + fee + " - " + totalFee);
                maxFeeIn60MinWindow = 0;
                intervalStart = date;
            } else {
                maxFeeIn60MinWindow = Math.max(getTollFeePerPassing(date), maxFeeIn60MinWindow); // todo: 2. max fee adds fee even in 60 min window
            }
        }
        return Math.min(totalFee + maxFeeIn60MinWindow, 60); //Todo 3. Math.max(totalFee, 60);
    }

    int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        // todo 7. Unneccerery code - removed
        if (hour == 6 && minute <= 29) return 8;
        else if (hour == 6) return 13;
        else if (hour == 7) return 18;
        else if (hour == 8 && minute <= 29) return 13;
        else if (hour >= 8 && hour <= 14) return 8; // todo: 4. else if (hour >= 8 && hour <= 14 && minute >= 30 && minute <= 59) return 8;
        else if (hour == 15 && minute <= 29) return 13;
        else if (hour == 15 || hour == 16) return 18; // todo: 5. else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) return 18;
        else if (hour == 17) return 13;
        else if (hour == 18 && minute <= 29) return 8;
        else return 0;
    }

    boolean isTollFreeDate(LocalDateTime date) {
        return date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7 || date.getMonth().getValue() == 7;
    }

    public static void main(String[] args) {
        new TollFeeCalculator("testData/Lab4_f.txt");
    }
}