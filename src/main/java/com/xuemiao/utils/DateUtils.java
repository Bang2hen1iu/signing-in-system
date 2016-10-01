package com.xuemiao.utils;

import com.xuemiao.exception.DateFormatErrorException;

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

    public static Date parseDateString(String dateString) {

        Date date = null;
        try{
            date = sdf.parse(dateString);
        }catch (ParseException e){
            return null;
        }
        return date;
    }

    private static int getDiffDays(Date startDate, Date currentDate){
        return (int)((startDate.getTime()-currentDate.getTime())/86400000);
    }

    public static int getCurrentWeek(Date startDate, Date currentDate){
        return 1 + getDiffDays(startDate, currentDate)/7;
    }

    public static int getCurrentWeekDay(Date startDate, Date currentDate){
        return getDiffDays(startDate, currentDate)%7;
    }

    public static List<String> DateList2DateStringList(List<Date> dateList){
        List<String> dateStringList = new ArrayList<>();
        for(Date date : dateList){
            dateStringList.add(sdf.format(date));
        }
        return dateStringList;
    }
}
