package com.mauriciotogneri.watchfacesectors.view.main;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Switch;

import com.mauriciotogneri.watchfacesectors.ColorDisplayer;
import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.R;
import com.mauriciotogneri.watchfacesectors.colorpicker.ColorPicker.ColorPickerCallback;

public class MainView
{
    private final UiContainer ui;
    private Profile profile = new Profile();

    public MainView(View view, final MainViewObserver observer)
    {
        this.ui = new UiContainer(view);

        this.ui.buttonUpdate.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                observer.onUpdate(getProfile());
            }
        });

        ui.outerSector.setChecked(profile.outerSector);
        initializeColorDisplayer(ui.outerSectorColor, profile.outerSectorColor, observer);

        ui.middleSector.setChecked(profile.middleSector);
        initializeColorDisplayer(ui.middleSectorColor, profile.middleSectorColor, observer);

        ui.innerSector.setChecked(profile.innerSector);
        initializeColorDisplayer(ui.innerSectorColor, profile.innerSectorColor, observer);

        ui.hoursMark.setChecked(profile.hoursMarkOn);
        ui.hoursMarkLength.setText(String.valueOf(profile.hoursMarkLength));
        ui.hoursMarkWidth.setText(String.valueOf(profile.hoursMarkWidth));
        initializeColorDisplayer(ui.hoursMarkColor, profile.hoursMarkColor, observer);

        ui.minutesMark.setChecked(profile.minutesMarkOn);
        ui.minutesMarkLength.setText(String.valueOf(profile.minutesMarkLength));
        ui.minutesMarkWidth.setText(String.valueOf(profile.minutesMarkWidth));
        initializeColorDisplayer(ui.minutesMarkColor, profile.minutesMarkColor, observer);
    }

    private void initializeColorDisplayer(ColorDisplayer colorDisplayer, int initialColor, final MainViewObserver observer)
    {
        colorDisplayer.setBackgroundColor(initialColor);
        colorDisplayer.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                observer.onChooseColor(ui.hoursMarkColor.getDisplayedColor(), new ColorPickerCallback()
                {
                    @Override
                    public void onColorChosen(int color)
                    {
                        view.setBackgroundColor(color);
                    }
                });
            }
        });
    }

    private Profile getProfile()
    {
        profile.outerSector = ui.outerSector.isChecked();
        profile.outerSectorColor = ui.outerSectorColor.getDisplayedColor();

        profile.middleSector = ui.middleSector.isChecked();
        profile.middleSectorColor = ui.middleSectorColor.getDisplayedColor();

        profile.innerSector = ui.innerSector.isChecked();
        profile.innerSectorColor = ui.innerSectorColor.getDisplayedColor();

        profile.hoursMarkOn = ui.hoursMark.isChecked();
        profile.hoursMarkLength = Float.parseFloat(ui.hoursMarkLength.getText().toString());
        profile.hoursMarkWidth = Float.parseFloat(ui.hoursMarkWidth.getText().toString());
        profile.hoursMarkColor = ui.hoursMarkColor.getDisplayedColor();

        profile.minutesMarkOn = ui.minutesMark.isChecked();
        profile.minutesMarkLength = Float.parseFloat(ui.minutesMarkLength.getText().toString());
        profile.minutesMarkWidth = Float.parseFloat(ui.minutesMarkWidth.getText().toString());
        profile.minutesMarkColor = ui.minutesMarkColor.getDisplayedColor();

        return profile;
    }

    private static class UiContainer
    {
        final Switch outerSector;
        final ColorDisplayer outerSectorColor;

        final Switch middleSector;
        final ColorDisplayer middleSectorColor;

        final Switch innerSector;
        final ColorDisplayer innerSectorColor;

        final Switch hoursMark;
        final EditText hoursMarkLength;
        final EditText hoursMarkWidth;
        final ColorDisplayer hoursMarkColor;

        final Switch minutesMark;
        final EditText minutesMarkLength;
        final EditText minutesMarkWidth;
        final ColorDisplayer minutesMarkColor;

        final View buttonUpdate;

        private UiContainer(View view)
        {
            this.outerSector = (Switch) view.findViewById(R.id.outerSector);
            this.outerSectorColor = (ColorDisplayer) view.findViewById(R.id.outerSectorColor);

            this.middleSector = (Switch) view.findViewById(R.id.middleSector);
            this.middleSectorColor = (ColorDisplayer) view.findViewById(R.id.middleSectorColor);

            this.innerSector = (Switch) view.findViewById(R.id.innerSector);
            this.innerSectorColor = (ColorDisplayer) view.findViewById(R.id.innerSectorColor);

            this.hoursMark = (Switch) view.findViewById(R.id.hoursMark);
            this.hoursMarkLength = (EditText) view.findViewById(R.id.hoursMarkLength);
            this.hoursMarkWidth = (EditText) view.findViewById(R.id.hoursMarkWidth);
            this.hoursMarkColor = (ColorDisplayer) view.findViewById(R.id.hoursMarkColor);

            this.minutesMark = (Switch) view.findViewById(R.id.minutesMark);
            this.minutesMarkLength = (EditText) view.findViewById(R.id.minutesMarkLength);
            this.minutesMarkWidth = (EditText) view.findViewById(R.id.minutesMarkWidth);
            this.minutesMarkColor = (ColorDisplayer) view.findViewById(R.id.minutesMarkColor);

            this.buttonUpdate = view.findViewById(R.id.buttonUpdate);
        }
    }
}