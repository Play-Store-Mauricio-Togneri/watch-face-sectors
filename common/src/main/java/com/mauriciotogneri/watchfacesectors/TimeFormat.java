package com.mauriciotogneri.watchfacesectors;

import android.content.Context;

import com.mauriciotogneri.common.R;

public class TimeFormat
{
    public final String format;
    public final String name;

    public static final String TYPE_AM_PM = "AM/PM";
    public static final String TYPE_24H = "24H";

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

    public static TimeFormat[] getTypes(Context context)
    {
        TimeFormat[] result = new TimeFormat[2];
        result[0] = new TimeFormat(TYPE_AM_PM, context.getString(R.string.main_timeFormat_ampm));
        result[1] = new TimeFormat(TYPE_24H, context.getString(R.string.main_timeFormat_24h));

        return result;
    }
}