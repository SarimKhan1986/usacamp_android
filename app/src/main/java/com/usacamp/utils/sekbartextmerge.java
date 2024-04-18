package com.usacamp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.usacamp.R;

@SuppressLint("AppCompatCustomView")
public class sekbartextmerge extends LinearLayout  {
    public sekBaText tvThumb;
    public SeekBar seekBar;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;
    public TextView tvShowing;
    public sekbartextmerge(Context context) {
        super(context);
        init();
    }

    public sekbartextmerge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_thum_tet_sekba, this);
        setOrientation(LinearLayout.VERTICAL);
        tvThumb = (sekBaText) findViewById(R.id.tvThumb);
        tvShowing = (TextView) findViewById(R.id.tvshowing);
        seekBar = (SeekBar) findViewById(R.id.sbProgress);
        seekBar.setClickable(false);
        seekBar.setActivated(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (onSeekBarChangeListener != null)
//                    onSeekBarChangeListener.onStopTrackingTouch(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                if (onSeekBarChangeListener != null)
//                    onSeekBarChangeListener.onStartTrackingTouch(seekBar);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                tvThumb.attachToSeekBar(seekBar);
            }
        });

    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener l) {
        this.onSeekBarChangeListener = l;
    }

    public void setThumbText(int text) {
        tvThumb.setText(text + "%");
    }

    public void setProgress(int progress) {

        seekBar.setProgress(progress);
    }

    public void setTvShowing(String text)
    {
        tvShowing.setText(text);
    }

}
