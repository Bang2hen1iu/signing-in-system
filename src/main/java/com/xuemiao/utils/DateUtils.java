package com.xuemiao.utils;

import org.joda.time.DateTime;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
public class DateUtils {
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

    private static SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");

    public static DateTime parseDateString(String dateString) {
        return DateTime.parse(dateString);
    }

    public static Timestamp adjustYearMonthDay(Timestamp t1) {
        DateTime dateTime = new DateTime(t1.getTime());
        DateTime now = DateTime.now();
        DateTime realTime = new DateTime(now.getYear(),now.getMonthOfYear(),now.getDayOfMonth(),dateTime.getHourOfDay(),
                dateTime.getMinuteOfHour(),dateTime.getSecondOfMinute());
        return new Timestamp(realTime.getMillis());
    }

    public static String timestamp2String(Timestamp timestamp, int formatCode) {
        switch (formatCode) {
            case 1:
                return sdf1.format(timestamp);
            case 2:
                return sdf2.format(timestamp);
            case 3:
                return sdf3.format(timestamp);
            default:
                return null;
        }
    }

    public static String sqlDate2String(Date date) {
        return sdf1.format(date);
    }

    private static int getDiffDays(DateTime startDate, DateTime currentDate) {
        return (int) ((currentDate.getMillis() - startDate.getMillis()) / 86400000);
    }

    public static int getCurrentWeek(DateTime startDate, DateTime currentDate) {
        return 1 + getDiffDays(startDate, currentDate) / 7;
    }

    public static int getCurrentWeekDay(DateTime startDate, DateTime currentDate) {
        return getDiffDays(startDate, currentDate) % 7;
    }

    public static List<String> DateList2DateStringList(List<Date> dateList) {
        List<String> dateStringList = new ArrayList<>();
        for (Date date : dateList) {
            dateStringList.add(sdf1.format(date));
        }
        return dateStringList;
    }

    public static int getTimeGapInSecond(DateTime t1, DateTime t2) {
        Long timeGapInMills = t2.getMillis() - t1.getMillis();
        return timeGapInMills.intValue() / 1000;
    }
}
