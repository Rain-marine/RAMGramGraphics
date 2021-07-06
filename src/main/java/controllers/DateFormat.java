package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    public static String dayMonthYear(Date date){
        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String customFormat (Date date ,String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
