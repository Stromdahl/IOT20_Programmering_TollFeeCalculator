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
        try (Scanner sc = new Scanner(new File(inputFile))) {
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = new LocalDateTime[dateStrings.length];
            for(int i = 0; i < dates.length; i++){
                dates[i] = parseStringToDate(dateStrings[i]);
            }
            System.out.println("The total fee for the inputfile is " + getTotalFeeCost(dates));
        } catch (IOException e) {
            System.err.println("Could not read file " + inputFile);
        }
        //8 file did not close when the reading is done
    }

    public LocalDateTime parseStringToDate(String dateString){
        LocalDateTime date = null;
        try { //6. if inputed date is not valid then exception is thrown
            date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeException e) {
            System.err.printf("Could not parse %s, It´s not a valid date\n", dateString);
        }
        return date;
    }

    public int getTotalFeeCost(LocalDateTime[] dates) {
        int totalFee = 0;
        LocalDateTime intervalStart = dates[0];
        int tempMaxFeeIn60Minutes = getTollFeePerPassing(intervalStart);
        long diffInMinutes = 0;
        for (int i = 1; i < dates.length; i++) {
            LocalDateTime date = dates[i];
            if (date == null) continue;
            diffInMinutes = intervalStart.until(date, ChronoUnit.MINUTES);
            System.out.println(date.toString());
            if (diffInMinutes < 60) {
                tempMaxFeeIn60Minutes = Math.max(getTollFeePerPassing(date), tempMaxFeeIn60Minutes);
            } else {
                totalFee += tempMaxFeeIn60Minutes;
                intervalStart = date;
                tempMaxFeeIn60Minutes = getTollFeePerPassing(intervalStart);
            }
        }
        if(diffInMinutes > 60) totalFee += tempMaxFeeIn60Minutes;
        return Math.min(totalFee, 60); //3. Math.max(totalFee, 60);
    }

    int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        //Unneccerery code - removed
        if (hour == 6 && minute <= 29) return 8;
        else if (hour == 6) return 13;
        else if (hour == 7) return 18;
        else if (hour == 8 && minute <= 29) return 13;
        else if (hour >= 8 && hour <= 14) return 8; //4. else if (hour >= 8 && hour <= 14 && minute >= 30 && minute <= 59) return 8;
        else if (hour == 15 && minute <= 29) return 13;
        else if (hour == 15 || hour == 16) return 18; // 5. else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) return 18;
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