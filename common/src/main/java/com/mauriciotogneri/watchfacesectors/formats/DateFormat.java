package com.mauriciotogneri.watchfacesectors.formats;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateFormat
{
    public final String format;
    public final String name;

    public static final String TYPE_1 = "dd-MM-yyyy";
    public static final String TYPE_2 = "dd/MM/yyyy";

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

        DateFormat[] result = new DateFormat[2];
        result[0] = new DateFormat(TYPE_1, formatDate(TYPE_1, calendar));
        result[1] = new DateFormat(TYPE_2, formatDate(TYPE_2, calendar));

        return result;
    }
}