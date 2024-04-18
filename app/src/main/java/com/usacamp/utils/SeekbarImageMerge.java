package com.usacamp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.usacamp.R;

@SuppressLint("AppCompatCustomView")
public class SeekbarImageMerge extends LinearLayout  {
    public SeekBarImage tvThumb;
    public SeekBar seekBar;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;
    public static int mProgress = 0;
    public SeekbarImageMerge(Context context) {
        super(context);
        init();
    }

    public SeekbarImageMerge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_thumb_image_sekbar, this);
        setOrientation(LinearLayout.VERTICAL);
        tvThumb = (SeekBarImage) findViewById(R.id.tvThumb);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onStopTrackingTouch(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onStartTrackingTouch(seekBar);

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (onSeekBarChangeListener != null)
                    onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                tvThumb.attachToSeekBar(seekBar);
                Log.d("purchase_point", String.valueOf(seekBar.getProgress()));
            }
        });

    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener l) {
        this.onSeekBarChangeListener = l;
    }

    public void setProgress(int progress) {
        Log.d("seeekbar1",String.valueOf(progress));
        seekBar.setProgress(progress);
    }

    public int getProgress()
    {
        Log.d("seeekbar",String.valueOf(mProgress));
        return mProgress;
    }

}
