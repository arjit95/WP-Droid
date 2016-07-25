package com.xedus.tutorials.utils;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PRAV on 22-06-2016.
 */
public class DateFormatter {
    public static String format(String date){
        if(date==null || date.length()<10)
            return date;
        date = date.substring(0,10);
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = format1.parse(date);
            DateFormat format2 = new SimpleDateFormat("dd MMMM");
            return format2.format(d1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
       return date;
    }
}
