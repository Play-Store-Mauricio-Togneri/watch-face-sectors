package com.mauriciotogneri.watchfacesectors;

import android.graphics.Color;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Profile implements Serializable
{
    public boolean outerSector = true;
    public int outerSectorColor = Color.argb(255, 165, 200, 60);

    public boolean middleSector = true;
    public int middleSectorColor = Color.argb(255, 90, 160, 210);

    public boolean innerSector = true;
    public int innerSectorColor = Color.argb(255, 210, 90, 90);

    // -------------------------------------------------------------------

    public boolean hoursMarkOn = true;
    public float hoursMarkLength = 1.5f;
    public float hoursMarkWidth = 4;
    public int hoursMarkColor = Color.argb(255, 240, 240, 240);

    // -------------------------------------------------------------------

    public boolean minutesMarkOn = true;
    public float minutesMarkLength = 1;
    public float minutesMarkWidth = 2;
    public int minutesMarkColor = Color.argb(255, 220, 220, 220);

    // -------------------------------------------------------------------

    public boolean timeOn = true;
    public String timeFormat = TIME_FORMAT_24;
    public float timePosition = 2;

    public float timeForegroundSize = 40;
    public int timeForegroundColor = Color.argb(255, 220, 220, 220);

    public float timeBorderSize = 1;
    public int timeBorderColor = Color.argb(255, 150, 150, 150);

    // -------------------------------------------------------------------

    public int backgroundColor = Color.BLACK;

    private static final String TIME_FORMAT_24 = "%d:%02d";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Profile()
    {
    }
}