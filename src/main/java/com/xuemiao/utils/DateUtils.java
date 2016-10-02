package com.xuemiao.utils;

import com.xuemiao.exception.DateFormatErrorException;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static DateTime parseDateString(String dateString) {

        return DateTime.parse(dateString);
    }

    private static int getDiffDays(DateTime startDate, DateTime currentDate){
        return (int)((currentDate.getMillis()-startDate.getMillis())/86400000);
    }

    public static int getCurrentWeek(DateTime startDate, DateTime currentDate){
        return 1 + getDiffDays(startDate, currentDate)/7;
    }

    public static int getCurrentWeekDay(DateTime startDate, DateTime currentDate){
        return getDiffDays(startDate, currentDate)%7;
    }

    public static List<String> DateList2DateStringList(List<DateTime> dateList){
        List<String> dateStringList = new ArrayList<>();
        for(DateTime date : dateList){
            dateStringList.add(sdf.format(date));
        }
        return dateStringList;
    }
}
