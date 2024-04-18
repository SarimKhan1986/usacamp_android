package com.usacamp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class SeekBarImage extends ImageView {
    //list view item
    private final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private int width = 0;

    public SeekBarImage(Context context) {
        super(context);
    }

    public SeekBarImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void attachToSeekBar(SeekBar seekBar) {
        float contentWidth = this.getMeasuredWidth();
        int realWidth = width - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
        int maxLimit = (int) (width - seekBar.getPaddingRight());
        int minLimit = (int) (seekBar.getPaddingLeft() - contentWidth / 2);
        float percent = (float) (1.0 * seekBar.getProgress() / seekBar.getMax());

        int left = minLimit + (int) (realWidth * percent);
        left = left <= minLimit ? minLimit : left >= maxLimit ? maxLimit : left;
        lp.setMargins(left, 0, 0, 0);
        setLayoutParams(lp);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (width == 0)
            width = MeasureSpec.getSize(widthMeasureSpec);
    }

}

