package com.usacamp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class sekBaText extends TextView {
    //list view item
    private final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private int width = 0;

    public sekBaText(Context context) {
        super(context);
    }

    public sekBaText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void attachToSeekBar(SeekBar seekBar) {
        String content = getText().toString();
        if (TextUtils.isEmpty(content) || seekBar == null)
            return;
        float contentWidth = this.getPaint().measureText(content);
        int realWidth = width - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
        int maxLimit = (int) (width - contentWidth - seekBar.getPaddingRight());
        int minLimit = seekBar.getPaddingLeft();
        float percent = (float) (1.0 * seekBar.getProgress() / seekBar.getMax());
        int left = minLimit + (int) (realWidth * percent - contentWidth / 2.0);
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
