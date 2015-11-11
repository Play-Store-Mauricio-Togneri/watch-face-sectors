package com.mauriciotogneri.watchfacesectors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

public class Preferences
{
    private final SharedPreferences sharedPreferences;

    private static Preferences instance;

    private static final String KEY_PROFILE = "KEY_PROFILE";

    private Preferences(Context context)
    {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized Preferences getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new Preferences(context);
        }

        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    private void save(String key, byte[] value)
    {
        String stringValue = Base64.encodeToString(value, Base64.DEFAULT);

        sharedPreferences.edit().putString(key, stringValue).commit();
    }

    private byte[] get(String key)
    {
        String stringValue = sharedPreferences.getString(key, null);

        return (stringValue != null) ? Base64.decode(stringValue, Base64.DEFAULT) : null;
    }

    public synchronized void saveProfile(Profile profile)
    {
        save(KEY_PROFILE, Serializer.serialize(profile));
    }

    public synchronized Profile getProfile()
    {
        byte[] data = get(KEY_PROFILE);

        if (data != null)
        {
            Profile profile = Serializer.deserialize(data);

            return (profile != null) ? profile : new Profile();
        }
        else
        {
            return new Profile();
        }
    }
}