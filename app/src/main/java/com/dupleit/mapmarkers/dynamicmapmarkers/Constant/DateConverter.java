package com.dupleit.mapmarkers.dynamicmapmarkers.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by khalil on 11/7/17.
 */

public class DateConverter {

    public String convertDate(String myDate){
        String dateTime = myDate;
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = dateParser.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Then convert the Date to a String, formatted as you dd/MM/yyyy
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yy");
        System.out.println(dateFormatter.format(date));
        return dateFormatter.format(date);
    }

}
