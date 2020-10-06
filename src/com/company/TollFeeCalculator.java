package com.company;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;


public class TollFeeCalculator {

    public TollFeeCalculator(String inputFile) {
        try {
            String[] dateStrings = getDateStringsFromFile(inputFile);
            LocalDateTime[] dates = getDates(dateStrings);
            System.out.println("The total fee for the inputfile is " + getTotalFeeCost(dates));
        } catch (IOException e) {
            System.err.println("Could not find file: " + inputFile);
        }
    }

    public String[] getDateStringsFromFile(String inputFile) throws IOException{
        Scanner sc = new Scanner(new File(inputFile));
        return sc.nextLine().split(", ");
    }

    public LocalDateTime[] getDates(String[] dateStrings){
        LocalDateTime[] dates = new LocalDateTime[dateStrings.length];
        for(int i = 0; i < dates.length; i++){
            try {
                dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch ( DateTimeException e){
                System.err.printf("Could not parse \"%s\", it´s not a valid date\n", dateStrings[i]);
            }
        }
        return dates;
    }

    public int getTotalFeeCost(LocalDateTime[] dates) {
        ArrayList<LocalDateTime> hourIntervalFees = new ArrayList<>();
        hourIntervalFees.add(dates[0]);
        int totalFee = 0;
        for(int i = 1; i < dates.length; i++){
            LocalDateTime date = dates[i];
            if(date == null) continue;
            long diffInMinutes = hourIntervalFees.get(0).until(date, ChronoUnit.MINUTES);
            if(diffInMinutes > 60){
                int fee = 0;
                for (LocalDateTime hourIntervalFee : hourIntervalFees) {
                    fee = Math.max(fee, getTollFeePerPassing(hourIntervalFee));
                }
                totalFee += fee;
                hourIntervalFees.clear();
            }
            hourIntervalFees.add(date);
        }
        int fee = 0;
        for (LocalDateTime hourIntervalFee : hourIntervalFees) {
            fee = Math.max(fee, getTollFeePerPassing(hourIntervalFee));

        }
        return Math.min(totalFee + fee, 60);
    }

    int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        if (hour == 6 && minute <= 29) return 8;
        else if (hour == 6) return 13;
        else if (hour == 7) return 18;
        else if (hour == 8 && minute <= 29) return 13;
        else if (hour >= 8 && hour <= 14) return 8;
        else if (hour == 15 && minute <= 29) return 13;
        else if (hour == 15 || hour == 16) return 18;
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