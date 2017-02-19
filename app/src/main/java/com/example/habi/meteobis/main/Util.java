package com.example.habi.meteobis.main;

import android.util.Log;

import com.example.habi.meteobis.model.FullParams;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Gabriel Fortin
 */
public class Util {
    private static final String TAG = Util.class.getSimpleName();

    /**
     * Rounds the hour value of <code>date</code> down to a multiple of
     * <code>roundThresholdHours</code>
     *
     * @param date the date object to be rounded
     * @param roundThresholdHours the hour value will be rounded to be a multiple of this number
     * @return a new date object with rounded hours
     */
    public static DateTime round(DateTime date, int roundThresholdHours) {
        int year = date.year().get();
        int month = date.monthOfYear().get();
        int day = date.dayOfMonth().get();
        int hour = (date.hourOfDay().get() / roundThresholdHours) * roundThresholdHours;

        DateTime result = new DateTime(year, month, day, hour, 0, date.getZone());
        Log.v(TAG, "rounding:  " + date + "  -->  " + result);
        return result;
    }

    public static String formatTime(DateTime dateTime) {
        // y: year,  M: month,  d: day,  k: hour 1-24,  H: hour 0-23
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMddHH");
        String result = formatter.print(dateTime);
        Log.v(TAG, "formatted time: " + result);
        return result;
    }

    public static DateTime calculateShiftedDate(FullParams params, int pos) {
        int interval = params.model.interval;
        DateTime date = params.date;
        Period timeAdjustment = Period.hours(-pos * interval);
        return date.minus(timeAdjustment);
    }
}
