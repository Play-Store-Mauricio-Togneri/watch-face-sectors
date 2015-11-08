package com.mauriciotogneri.watchfacesectors.view.main;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

import com.mauriciotogneri.watchfacesectors.ColorDisplayer;
import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.R;
import com.mauriciotogneri.watchfacesectors.colorpicker.ColorPicker.ColorPickerCallback;

public class MainView
{
    private final UiContainer ui;

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

        this.ui.outerSectorColor.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                observer.onChooseColor(ui.outerSectorColor.getDisplayedColor(), new ColorPickerCallback()
                {
                    @Override
                    public void onColorChosen(int color)
                    {
                        view.setBackgroundColor(color);
                    }
                });
            }
        });

        this.ui.middleSectorColor.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                observer.onChooseColor(ui.middleSectorColor.getDisplayedColor(), new ColorPickerCallback()
                {
                    @Override
                    public void onColorChosen(int color)
                    {
                        view.setBackgroundColor(color);
                    }
                });
            }
        });

        this.ui.innerSectorColor.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                observer.onChooseColor(ui.innerSectorColor.getDisplayedColor(), new ColorPickerCallback()
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
        Profile profile = new Profile();

        profile.outerSector = ui.outerSector.isChecked();
        profile.outerSectorColor = ui.outerSectorColor.getDisplayedColor();

        profile.middleSector = ui.middleSector.isChecked();
        profile.middleSectorColor = ui.middleSectorColor.getDisplayedColor();

        profile.innerSector = ui.innerSector.isChecked();
        profile.innerSectorColor = ui.innerSectorColor.getDisplayedColor();

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

        final View buttonUpdate;

        private UiContainer(View view)
        {
            this.outerSector = (Switch) view.findViewById(R.id.outerSector);
            this.outerSectorColor = (ColorDisplayer) view.findViewById(R.id.outerSectorColor);

            this.middleSector = (Switch) view.findViewById(R.id.middleSector);
            this.middleSectorColor = (ColorDisplayer) view.findViewById(R.id.middleSectorColor);

            this.innerSector = (Switch) view.findViewById(R.id.innerSector);
            this.innerSectorColor = (ColorDisplayer) view.findViewById(R.id.innerSectorColor);

            this.buttonUpdate = view.findViewById(R.id.buttonUpdate);
        }
    }
}