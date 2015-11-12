package com.mauriciotogneri.watchfacesectors.view;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.mauriciotogneri.watchfacesectors.ClockHandType;
import com.mauriciotogneri.watchfacesectors.Preferences;
import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.R;
import com.mauriciotogneri.watchfacesectors.colorpicker.ColorPicker.ColorPickerCallback;
import com.mauriciotogneri.watchfacesectors.formats.DateFormat;
import com.mauriciotogneri.watchfacesectors.formats.TimeFormat;
import com.mauriciotogneri.watchfacesectors.widgets.ColorDisplayer;
import com.mauriciotogneri.watchfacesectors.widgets.NumberInput;

public class MainView
{
    private final UiContainer ui;
    private final Profile profile;

    public MainView(View view, final MainViewObserver observer)
    {
        final Preferences preferences = Preferences.getInstance(view.getContext());
        this.profile = preferences.getProfile();

        this.ui = new UiContainer(view);

        this.ui.buttonUpdate.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Profile profile = getProfile();

                preferences.saveProfile(profile);
                observer.onUpdate(profile);
            }
        });

        initializeColorDisplayer(ui.backgroundColor, profile.backgroundColor, observer);

        ui.outerSector.setChecked(profile.outerSector);
        initializeClockTypeSpinner(ui.outerSectorType, profile.outerSectorType, 0);
        initializeColorDisplayer(ui.outerSectorColor, profile.outerSectorColor, observer);

        ui.middleSector.setChecked(profile.middleSector);
        initializeClockTypeSpinner(ui.middleSectorType, profile.middleSectorType, 1);
        initializeColorDisplayer(ui.middleSectorColor, profile.middleSectorColor, observer);

        ui.innerSector.setChecked(profile.innerSector);
        initializeClockTypeSpinner(ui.innerSectorType, profile.innerSectorType, 2);
        initializeColorDisplayer(ui.innerSectorColor, profile.innerSectorColor, observer);

        ui.hoursMark.setChecked(profile.hoursMarkOn);
        ui.hoursMarkLength.setValue(profile.hoursMarkLength);
        ui.hoursMarkWidth.setValue(profile.hoursMarkWidth);
        initializeColorDisplayer(ui.hoursMarkColor, profile.hoursMarkColor, observer);

        ui.minutesMark.setChecked(profile.minutesMarkOn);
        ui.minutesMarkLength.setValue(profile.minutesMarkLength);
        ui.minutesMarkWidth.setValue(profile.minutesMarkWidth);
        initializeColorDisplayer(ui.minutesMarkColor, profile.minutesMarkColor, observer);

        ui.date.setChecked(profile.dateOn);
        initializeDateFormatSpinner(ui.dateFormat, profile.dateFormat);
        ui.datePosition.setValue(profile.datePosition);
        ui.dateFontSize.setValue(profile.dateFontSize);
        initializeColorDisplayer(ui.dateFontColor, profile.dateFontColor, observer);
        ui.dateBorderWidth.setValue(profile.dateBorderWidth);
        initializeColorDisplayer(ui.dateBorderColor, profile.dateBorderColor, observer);

        ui.time.setChecked(profile.timeOn);
        initializeTimeFormatSpinner(ui.timeFormat, profile.timeFormat);
        ui.timePosition.setValue(profile.timePosition);
        ui.timeFontSize.setValue(profile.timeFontSize);
        initializeColorDisplayer(ui.timeFontColor, profile.timeFontColor, observer);
        ui.timeBorderWidth.setValue(profile.timeBorderWidth);
        initializeColorDisplayer(ui.timeBorderColor, profile.timeBorderColor, observer);
    }

    private void initializeDateFormatSpinner(Spinner spinner, String initialValue)
    {
        DateFormat[] types = DateFormat.getTypes();

        ArrayAdapter<DateFormat> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        for (int i = 0; i < types.length; i++)
        {
            DateFormat type = types[i];

            if (TextUtils.equals(type.format, initialValue))
            {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void initializeTimeFormatSpinner(Spinner spinner, String initialValue)
    {
        TimeFormat[] types = TimeFormat.getTypes();

        ArrayAdapter<TimeFormat> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        for (int i = 0; i < types.length; i++)
        {
            TimeFormat type = types[i];

            if (TextUtils.equals(type.format, initialValue))
            {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void initializeClockTypeSpinner(Spinner spinner, int initialValue, final int spinnerPosition)
    {
        ClockHandType[] types = ClockHandType.getTypes(spinner.getContext());

        ArrayAdapter<ClockHandType> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        for (int i = 0; i < types.length; i++)
        {
            ClockHandType type = types[i];

            if (type.type == initialValue)
            {
                spinner.setSelection(i);
                break;
            }
        }

        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ClockHandType clockHandType = (ClockHandType) parent.getItemAtPosition(position);
                clockHandTypeChanged(spinnerPosition, clockHandType.type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    private void clockHandTypeChanged(int spinnerPosition, int selectedType)
    {
        if (spinnerPosition == 0)
        {
            correctHandClockSpinner(ui.middleSectorType, ui.innerSectorType, selectedType);
        }
        else if (spinnerPosition == 1)
        {
            correctHandClockSpinner(ui.outerSectorType, ui.innerSectorType, selectedType);
        }
        else if (spinnerPosition == 2)
        {
            correctHandClockSpinner(ui.outerSectorType, ui.middleSectorType, selectedType);
        }
    }

    private void correctHandClockSpinner(Spinner spinner1, Spinner spinner2, int selectedType)
    {
        ClockHandType clockHandType1 = getClockHandType(spinner1);
        ClockHandType clockHandType2 = getClockHandType(spinner2);
        int complementaryClockHandType = getComplementaryClockHandType(clockHandType1.type, clockHandType2.type);

        if (complementaryClockHandType != -1)
        {
            if (clockHandType1.type == selectedType)
            {
                spinner1.setSelection(complementaryClockHandType);
            }
            else if (clockHandType2.type == selectedType)
            {
                spinner2.setSelection(complementaryClockHandType);
            }
        }
    }

    private int getComplementaryClockHandType(int type1, int type2)
    {
        if (((type1 == ClockHandType.TYPE_HOURS) && (type2 == ClockHandType.TYPE_MINUTES)) || ((type2 == ClockHandType.TYPE_HOURS) && (type1 == ClockHandType.TYPE_MINUTES)))
        {
            return ClockHandType.TYPE_SECONDS;
        }
        else if (((type1 == ClockHandType.TYPE_HOURS) && (type2 == ClockHandType.TYPE_SECONDS)) || ((type2 == ClockHandType.TYPE_HOURS) && (type1 == ClockHandType.TYPE_SECONDS)))
        {
            return ClockHandType.TYPE_MINUTES;
        }
        else if (((type1 == ClockHandType.TYPE_MINUTES) && (type2 == ClockHandType.TYPE_SECONDS)) || ((type2 == ClockHandType.TYPE_MINUTES) && (type1 == ClockHandType.TYPE_SECONDS)))
        {
            return ClockHandType.TYPE_HOURS;
        }
        else
        {
            return -1;
        }
    }

    private ClockHandType getClockHandType(Spinner spinner)
    {
        return (ClockHandType) spinner.getSelectedItem();
    }

    private TimeFormat getTimeFormat(Spinner spinner)
    {
        return (TimeFormat) spinner.getSelectedItem();
    }

    private DateFormat getDateFormat(Spinner spinner)
    {
        return (DateFormat) spinner.getSelectedItem();
    }

    private void initializeColorDisplayer(final ColorDisplayer colorDisplayer, int initialColor, final MainViewObserver observer)
    {
        colorDisplayer.setBackgroundColor(initialColor);
        colorDisplayer.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                observer.onChooseColor(colorDisplayer.getDisplayedColor(), new ColorPickerCallback()
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
        profile.backgroundColor = ui.backgroundColor.getDisplayedColor();

        profile.outerSector = ui.outerSector.isChecked();
        profile.outerSectorType = getClockHandType(ui.outerSectorType).type;
        profile.outerSectorColor = ui.outerSectorColor.getDisplayedColor();

        profile.middleSector = ui.middleSector.isChecked();
        profile.middleSectorType = getClockHandType(ui.middleSectorType).type;
        profile.middleSectorColor = ui.middleSectorColor.getDisplayedColor();

        profile.innerSector = ui.innerSector.isChecked();
        profile.innerSectorType = getClockHandType(ui.innerSectorType).type;
        profile.innerSectorColor = ui.innerSectorColor.getDisplayedColor();

        profile.hoursMarkOn = ui.hoursMark.isChecked();
        profile.hoursMarkLength = ui.hoursMarkLength.getValue();
        profile.hoursMarkWidth = ui.hoursMarkWidth.getValue();
        profile.hoursMarkColor = ui.hoursMarkColor.getDisplayedColor();

        profile.minutesMarkOn = ui.minutesMark.isChecked();
        profile.minutesMarkLength = ui.minutesMarkLength.getValue();
        profile.minutesMarkWidth = ui.minutesMarkWidth.getValue();
        profile.minutesMarkColor = ui.minutesMarkColor.getDisplayedColor();

        profile.dateOn = ui.date.isChecked();
        profile.dateFormat = getDateFormat(ui.dateFormat).format;
        profile.datePosition = ui.datePosition.getValue();
        profile.dateFontSize = ui.dateFontSize.getValue();
        profile.dateFontColor = ui.dateFontColor.getDisplayedColor();
        profile.dateBorderWidth = ui.dateBorderWidth.getValue();
        profile.dateBorderColor = ui.dateBorderColor.getDisplayedColor();

        profile.timeOn = ui.time.isChecked();
        profile.timeFormat = getTimeFormat(ui.timeFormat).format;
        profile.timePosition = ui.timePosition.getValue();
        profile.timeFontSize = ui.timeFontSize.getValue();
        profile.timeFontColor = ui.timeFontColor.getDisplayedColor();
        profile.timeBorderWidth = ui.timeBorderWidth.getValue();
        profile.timeBorderColor = ui.timeBorderColor.getDisplayedColor();

        return profile;
    }

    private static class UiContainer
    {
        final ColorDisplayer backgroundColor;

        final Switch outerSector;
        final Spinner outerSectorType;
        final ColorDisplayer outerSectorColor;

        final Switch middleSector;
        final Spinner middleSectorType;
        final ColorDisplayer middleSectorColor;

        final Switch innerSector;
        final Spinner innerSectorType;
        final ColorDisplayer innerSectorColor;

        final Switch hoursMark;
        final NumberInput hoursMarkLength;
        final NumberInput hoursMarkWidth;
        final ColorDisplayer hoursMarkColor;

        final Switch minutesMark;
        final NumberInput minutesMarkLength;
        final NumberInput minutesMarkWidth;
        final ColorDisplayer minutesMarkColor;

        final Switch date;
        final Spinner dateFormat;
        final NumberInput datePosition;
        final NumberInput dateFontSize;
        final ColorDisplayer dateFontColor;
        final NumberInput dateBorderWidth;
        final ColorDisplayer dateBorderColor;

        final Switch time;
        final Spinner timeFormat;
        final NumberInput timePosition;
        final NumberInput timeFontSize;
        final ColorDisplayer timeFontColor;
        final NumberInput timeBorderWidth;
        final ColorDisplayer timeBorderColor;

        final View buttonUpdate;

        private UiContainer(View view)
        {
            this.backgroundColor = (ColorDisplayer) view.findViewById(R.id.backgroundColor);

            this.outerSector = (Switch) view.findViewById(R.id.outerSector);
            this.outerSectorType = (Spinner) view.findViewById(R.id.outerSectorType);
            this.outerSectorColor = (ColorDisplayer) view.findViewById(R.id.outerSectorColor);

            this.middleSector = (Switch) view.findViewById(R.id.middleSector);
            this.middleSectorType = (Spinner) view.findViewById(R.id.middleSectorType);
            this.middleSectorColor = (ColorDisplayer) view.findViewById(R.id.middleSectorColor);

            this.innerSector = (Switch) view.findViewById(R.id.innerSector);
            this.innerSectorType = (Spinner) view.findViewById(R.id.innerSectorType);
            this.innerSectorColor = (ColorDisplayer) view.findViewById(R.id.innerSectorColor);

            this.hoursMark = (Switch) view.findViewById(R.id.hoursMark);
            this.hoursMarkLength = (NumberInput) view.findViewById(R.id.hoursMarkLength);
            this.hoursMarkWidth = (NumberInput) view.findViewById(R.id.hoursMarkWidth);
            this.hoursMarkColor = (ColorDisplayer) view.findViewById(R.id.hoursMarkColor);

            this.minutesMark = (Switch) view.findViewById(R.id.minutesMark);
            this.minutesMarkLength = (NumberInput) view.findViewById(R.id.minutesMarkLength);
            this.minutesMarkWidth = (NumberInput) view.findViewById(R.id.minutesMarkWidth);
            this.minutesMarkColor = (ColorDisplayer) view.findViewById(R.id.minutesMarkColor);

            this.date = (Switch) view.findViewById(R.id.date);
            this.dateFormat = (Spinner) view.findViewById(R.id.dateFormat);
            this.datePosition = (NumberInput) view.findViewById(R.id.datePosition);
            this.dateFontSize = (NumberInput) view.findViewById(R.id.dateFontSize);
            this.dateFontColor = (ColorDisplayer) view.findViewById(R.id.dateFontColor);
            this.dateBorderWidth = (NumberInput) view.findViewById(R.id.dateBorderWidth);
            this.dateBorderColor = (ColorDisplayer) view.findViewById(R.id.dateBorderColor);

            this.time = (Switch) view.findViewById(R.id.time);
            this.timeFormat = (Spinner) view.findViewById(R.id.timeFormat);
            this.timePosition = (NumberInput) view.findViewById(R.id.timePosition);
            this.timeFontSize = (NumberInput) view.findViewById(R.id.timeFontSize);
            this.timeFontColor = (ColorDisplayer) view.findViewById(R.id.timeFontColor);
            this.timeBorderWidth = (NumberInput) view.findViewById(R.id.timeBorderWidth);
            this.timeBorderColor = (ColorDisplayer) view.findViewById(R.id.timeBorderColor);

            this.buttonUpdate = view.findViewById(R.id.buttonUpdate);
        }
    }
}