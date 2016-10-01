package com.example.habi.meteobis;

import android.util.Log;

import org.joda.time.DateTime;

import java.util.Locale;

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
        Log.d(TAG, "rounding:  " + date + "  -->  " + result);
        return result;
    }

    public static String formatTime(DateTime dateTime) {
        int year = dateTime.year().get();
        int month = dateTime.monthOfYear().get();
        int day = dateTime.dayOfMonth().get();
        int hour = dateTime.hourOfDay().get();
        String result = String.format(Locale.UK, "%d%02d%02d%02d", year, month, day, hour);
        Log.v(TAG, "formatted time: " + result);
        return result;
    }
}
