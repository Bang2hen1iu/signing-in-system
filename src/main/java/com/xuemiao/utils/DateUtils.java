package com.xuemiao.utils;

import com.xuemiao.exception.DateFormatErrorException;
import org.joda.time.DateTime;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static List<String> DateList2DateStringList(List<Date> dateList){
        List<String> dateStringList = new ArrayList<>();
        for(Date date : dateList){
            dateStringList.add(sdf.format(date));
        }
        return dateStringList;
    }

    public static int getTimeGapInSecond(DateTime t1, DateTime t2){
        Long timeGapInMills = t2.getMillis()-t1.getMillis();
        return timeGapInMills.intValue()/1000;
    }
}
