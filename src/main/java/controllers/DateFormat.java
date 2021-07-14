package controllers;

import java.text.ParseException;
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


    public static Date stringToDate(String birthday) {
        Date birthDate = null;
        if (birthday!=null && !birthday.equals("")) {
            try {
                birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return birthDate;
    }
}
