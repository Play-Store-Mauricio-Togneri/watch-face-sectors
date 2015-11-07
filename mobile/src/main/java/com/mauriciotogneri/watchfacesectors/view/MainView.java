package com.mauriciotogneri.watchfacesectors.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;

import com.mauriciotogneri.watchfacesectors.Profile;
import com.mauriciotogneri.watchfacesectors.R;

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
        final Switch middleSector;
        final Switch innerSector;
        final View buttonUpdate;

        private UiContainer(View view)
        {
            this.outerSector = (Switch) view.findViewById(R.id.outerSector);
            this.middleSector = (Switch) view.findViewById(R.id.middleSector);
            this.innerSector = (Switch) view.findViewById(R.id.innerSector);
            this.buttonUpdate = view.findViewById(R.id.buttonUpdate);
        }
    }
}