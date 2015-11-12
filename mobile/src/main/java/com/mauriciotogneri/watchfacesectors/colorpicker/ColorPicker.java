package com.mauriciotogneri.watchfacesectors.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.mauriciotogneri.watchfacesectors.R;
import com.mauriciotogneri.watchfacesectors.widgets.ColorDisplayer;

public class ColorPicker
{
    @SuppressLint("InflateParams")
    public void show(Context context, int initialColor, final ColorPicker.ColorPickerCallback callback)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.screen_color_picker, null, false);

        final ColorDisplayer resultColor = (ColorDisplayer) view.findViewById(R.id.resultColor);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Choose a color");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                callback.onColorChosen(resultColor.getDisplayedColor());
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });

        int red = Color.red(initialColor);
        int green = Color.green(initialColor);
        int blue = Color.blue(initialColor);

        final SeekBar seekBarRed = (SeekBar) view.findViewById(R.id.valueRed);
        final SeekBar seekBarGreen = (SeekBar) view.findViewById(R.id.valueGreen);
        final SeekBar seekBarBlue = (SeekBar) view.findViewById(R.id.valueBlue);

        seekBarRed.setProgress(red);
        seekBarRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                updateColor(resultColor, seekBarRed.getProgress(), seekBarGreen.getProgress(), seekBarBlue.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        seekBarGreen.setProgress(green);
        seekBarGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                updateColor(resultColor, seekBarRed.getProgress(), seekBarGreen.getProgress(), seekBarBlue.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        seekBarBlue.setProgress(blue);
        seekBarBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                updateColor(resultColor, seekBarRed.getProgress(), seekBarGreen.getProgress(), seekBarBlue.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        updateColor(resultColor, red, green, blue);
    }

    private void updateColor(View resultColor, int red, int green, int blue)
    {
        resultColor.setBackgroundColor(Color.argb(255, red, green, blue));
    }

    public interface ColorPickerCallback
    {
        void onColorChosen(int color);
    }
}