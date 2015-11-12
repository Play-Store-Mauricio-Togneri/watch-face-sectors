package com.mauriciotogneri.watchfacesectors.formats;

import android.text.TextUtils;

import java.util.Calendar;

public class TimeFormat
{
    public final String format;
    public final String name;

    public static final String TYPE_24H = "24H";
    public static final String TYPE_AM_PM = "AM/PM";

    private static final String TIME_FORMAT = "%d:%02d";

    public TimeFormat(String format, String name)
    {
        this.format = format;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public static String formatTime(String format, int hours, int minutes)
    {
        if (TextUtils.equals(format, TimeFormat.TYPE_24H))
        {
            return String.format(TIME_FORMAT, hours, minutes);
        }
        else if (TextUtils.equals(format, TimeFormat.TYPE_AM_PM))
        {
            boolean isPM = hours > 12;

            int finalHours = (isPM) ? (hours - 12) : hours;
            String meridiemType = (isPM) ? "PM" : "AM";

            return String.format(TIME_FORMAT, finalHours, minutes) + " " + meridiemType;
        }
        else
        {
            return "";
        }
    }

    public static TimeFormat[] getTypes()
    {
        Calendar calendar = Calendar.getInstance();
        int calendarHours = calendar.get(Calendar.HOUR_OF_DAY);
        int calendarMinutes = calendar.get(Calendar.MINUTE);

        TimeFormat[] result = new TimeFormat[2];
        result[0] = new TimeFormat(TYPE_24H, formatTime(TYPE_24H, calendarHours, calendarMinutes));
        result[1] = new TimeFormat(TYPE_AM_PM, formatTime(TYPE_AM_PM, calendarHours, calendarMinutes));

        return result;
    }
}