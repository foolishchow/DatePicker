package me.foolishchow.android.datepicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Description:
 * Author: foolishchow
 * Date: 19/12/2020 3:55 PM
 */
public class Utils {
    public static Calendar asCalendar(int year, int month, int dayOfMonth) {
        return asCalendar(year, month, dayOfMonth, 0, 0, 0);
    }

    public static Calendar asCalendar(
            int year, int month, int dayOfMonth,
            int hourOfDay, int minute, int second
    ) {
        Calendar result = Calendar.getInstance();
        result.set(year, month - 1, dayOfMonth, hourOfDay, minute, second);
        return result;
    }

    public static Calendar asCalendar(Date date) {
        Calendar result = Calendar.getInstance();
        result.setTime(date);
        return result;
    }

    public static Date asDate(int year, int month, int dayOfMonth) {
        return asDate(year, month, dayOfMonth, 0, 0, 0);
    }

    public static Date asDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        return new Date(asCalendar(year, month, dayOfMonth, hourOfDay, minute, second).getTimeInMillis());
    }


    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
