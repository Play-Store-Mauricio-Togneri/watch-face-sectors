package com.mauriciotogneri.watchfacesectors.formats;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeFormat
{
    public final String format;
    public final String name;

    public static final String TYPE_24H = "HH:mm";
    public static final String TYPE_AM_PM = "hh:mm a";

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

    public static String formatTime(String format, Calendar calendar)
    {
        return new SimpleDateFormat(format, Locale.getDefault()).format(calendar.getTime());
    }

    public static TimeFormat[] getTypes()
    {
        Calendar calendar = Calendar.getInstance();

        TimeFormat[] result = new TimeFormat[2];
        result[0] = new TimeFormat(TYPE_24H, formatTime(TYPE_24H, calendar));
        result[1] = new TimeFormat(TYPE_AM_PM, formatTime(TYPE_AM_PM, calendar));

        return result;
    }
}