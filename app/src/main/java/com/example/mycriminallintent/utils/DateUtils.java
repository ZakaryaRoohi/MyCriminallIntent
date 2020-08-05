package com.example.mycriminallintent.utils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class DateUtils {
    public static Date getRandomDate(int startYear, int endYear) {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(startYear, endYear);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        int hour = createRandomNumber(24);
        int minute = createRandomNumber(60);
        int second= createRandomNumber(60);

        gc.set(gc.HOUR,hour);
        gc.set(gc.MINUTE,minute);
        gc.set(gc.SECOND,second);
        return gc.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
    public static  int createRandomNumber(int input){
        Random random = new Random();
        return random.nextInt(input);
    }
}
