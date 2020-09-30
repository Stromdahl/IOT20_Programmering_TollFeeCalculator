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
        try {
            Scanner sc = new Scanner(new File(inputFile));
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = new LocalDateTime[dateStrings.length];// todo: 1. new LocalDateTime[dateStrings.length-1];
            for(int i = 0; i < dates.length; i++) {
                try { // todo: 6.
                    dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                } catch (DateTimeException e) {
                    System.err.printf("Something went wrong %s is not a valid date\n", dateStrings[i]);
                }
            }
            System.out.println("The total fee for the inputfile is " + getTotalFeeCost(dates));
        } catch(IOException e) {
            System.err.println("Could not read file " + inputFile);
        }
    }

    private int getTotalFeeCost(LocalDateTime[] dates) {
        int totalFee = 0;
        LocalDateTime intervalStart = dates[0];
        int maxFeeUnder60Minutes = 0;
        for(LocalDateTime date: dates) {
            if(date == null) continue;
            long diffInMinutes = intervalStart.until(date, ChronoUnit.MINUTES);
            if(diffInMinutes > 60) {
                totalFee = getTollFeePerPassing(date) + maxFeeUnder60Minutes;
                System.out.println(date.toString() + " - " + totalFee);
                maxFeeUnder60Minutes = 0;
                intervalStart = date;
            } else {
                maxFeeUnder60Minutes = Math.max(getTollFeePerPassing(date), maxFeeUnder60Minutes); // todo: 2.
            }
        }
        return Math.min(totalFee + maxFeeUnder60Minutes, 60); //Todo 3. Math.max(totalFee, 60);
    }

    private int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        // todo 7.
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

    private boolean isTollFreeDate(LocalDateTime date) {
        return date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7 || date.getMonth().getValue() == 7;
    }

    public static void main(String[] args) {
        new TollFeeCalculator("testData/Lab4_f.txt");
    }
}