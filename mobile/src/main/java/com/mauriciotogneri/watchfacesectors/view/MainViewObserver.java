package com.mauriciotogneri.watchfacesectors.view;

import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.colorpicker.ColorPicker;

public interface MainViewObserver
{
    void onUpdate(Profile profile);

    void onChooseColor(int initialColor, ColorPicker.ColorPickerCallback callback);
}