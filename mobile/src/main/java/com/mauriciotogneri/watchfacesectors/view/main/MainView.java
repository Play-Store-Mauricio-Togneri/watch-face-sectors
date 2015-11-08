package com.mauriciotogneri.watchfacesectors.view.main;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

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
                ColorDrawable viewColor = (ColorDrawable) view.getBackground();

                observer.onChooseColor(viewColor.getColor(), new ColorPickerCallback()
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
                ColorDrawable viewColor = (ColorDrawable) view.getBackground();

                observer.onChooseColor(viewColor.getColor(), new ColorPickerCallback()
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
                ColorDrawable viewColor = (ColorDrawable) view.getBackground();

                observer.onChooseColor(viewColor.getColor(), new ColorPickerCallback()
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
        profile.middleSector = ui.middleSector.isChecked();
        profile.innerSector = ui.innerSector.isChecked();

        return profile;
    }

    private static class UiContainer
    {
        final Switch outerSector;
        final View outerSectorColor;

        final Switch middleSector;
        final View middleSectorColor;

        final Switch innerSector;
        final View innerSectorColor;

        final View buttonUpdate;

        private UiContainer(View view)
        {
            this.outerSector = (Switch) view.findViewById(R.id.outerSector);
            this.outerSectorColor = view.findViewById(R.id.outerSectorColor);

            this.middleSector = (Switch) view.findViewById(R.id.middleSector);
            this.middleSectorColor = view.findViewById(R.id.middleSectorColor);

            this.innerSector = (Switch) view.findViewById(R.id.innerSector);
            this.innerSectorColor = view.findViewById(R.id.innerSectorColor);

            this.buttonUpdate = view.findViewById(R.id.buttonUpdate);
        }
    }
}