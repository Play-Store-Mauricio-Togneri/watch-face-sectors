package com.mauriciotogneri.watchfacesectors;

import android.content.Context;

import com.mauriciotogneri.common.R;

public class ClockHandType
{
    public final int type;
    public final String name;

    public static final int TYPE_HOURS = 0;
    public static final int TYPE_MINUTES = 1;
    public static final int TYPE_SECONDS = 2;

    public ClockHandType(int type, String name)
    {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public static ClockHandType[] getTypes(Context context)
    {
        ClockHandType[] result = new ClockHandType[3];
        result[0] = new ClockHandType(TYPE_HOURS, context.getString(R.string.main_clockHandType_hours));
        result[1] = new ClockHandType(TYPE_MINUTES, context.getString(R.string.main_clockHandType_minutes));
        result[2] = new ClockHandType(TYPE_SECONDS, context.getString(R.string.main_clockHandType_seconds));

        return result;
    }
}