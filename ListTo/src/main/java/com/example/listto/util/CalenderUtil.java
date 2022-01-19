package com.example.listto.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalenderUtil {

    public static String getCalenderDateSqlFormat(Calendar calendar){
        String date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }
    public static Calendar getStringToDateSql(String date){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
        try {
            calendar.setTime(simpleDateFormat.parse(date));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Calendar getStringToDate(String date){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        try {
            calendar.setTime(simpleDateFormat.parse(date));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getMonthFromCalender(Calendar date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        return simpleDateFormat.format(date.getTime());
    }
    public static String calculateSuffix(Calendar date) {
        int day = date.get(Calendar.DAY_OF_MONTH) % 10;
        if (day == 1) return "st";
        else if (day == 2) return "nd";
        else if (day == 3) return "rd";
        else return "th";
    }
    public static String getDateFormat(Calendar cal){
        SimpleDateFormat formatDate = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        return formatDate.format(cal.getTime());
    }
    public static String getDateTitle(int month,int year){
        String date=String.valueOf("1/"+(month+1)+"/"+year);
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        Calendar calendar= Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getDateFormat(calendar);
    }
}
