package com.mauriciotogneri.watchfacesectors.formats;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateFormat
{
    public final String format;
    public final String name;

    public static final String TYPE_1 = "d MMM";
    public static final String TYPE_2 = "d MMMM";
    public static final String TYPE_3 = "EEE d MMM";
    public static final String TYPE_4 = "EEE d MMMM";
    public static final String TYPE_5 = "EEEE d MMM";
    public static final String TYPE_6 = "EEEE d MMMM";

    public DateFormat(String format, String name)
    {
        this.format = format;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public static String formatDate(String format, Calendar calendar)
    {
        return new SimpleDateFormat(format, Locale.getDefault()).format(calendar.getTime());
    }

    public static DateFormat[] getTypes()
    {
        Calendar calendar = Calendar.getInstance();

        DateFormat[] result = new DateFormat[6];
        result[0] = new DateFormat(TYPE_1, formatDate(TYPE_1, calendar));
        result[1] = new DateFormat(TYPE_2, formatDate(TYPE_2, calendar));
        result[2] = new DateFormat(TYPE_3, formatDate(TYPE_3, calendar));
        result[3] = new DateFormat(TYPE_4, formatDate(TYPE_4, calendar));
        result[4] = new DateFormat(TYPE_5, formatDate(TYPE_5, calendar));
        result[5] = new DateFormat(TYPE_6, formatDate(TYPE_6, calendar));

        return result;
    }
}