package com.usacamp.utils;
/*
 * 
 * Copyright (C) 2018 Krishna Kumar Sharma
 * 
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class KKViewPager extends ViewPager implements ViewPager.PageTransformer {
    public static final String TAG = "KKViewPager";
    private final float MAX_SCALE = 0.0f;
    private int mPageMargin;
    private final int mPageMarginLeft;
    private final int mPageMarginRight;
    private final int mPageMarginTop;
    private final int mPageMarginBottom;
    private boolean animationEnabled=true;
    private boolean fadeEnabled=false;
    private  float fadeFactor=0.5f;


    public KKViewPager(Context context) {
        this(context, null);
    }

    public KKViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // clipping should be off on the pager for its children so that they can scale out of bounds.
        setClipChildren(false);
        setClipToPadding(false);

        // to avoid fade effect at the end of the page
        setOverScrollMode(2);
        setPageTransformer(false, this);
        setOffscreenPageLimit(2);
        mPageMargin = dp2px(context.getResources(), 10);
        mPageMarginLeft = dp2px(context.getResources(), 0);
        mPageMarginTop = dp2px(context.getResources(), 5);
        mPageMarginRight= dp2px(context.getResources(), 150);
        mPageMarginBottom = dp2px(context.getResources(), 5);
        setPadding(mPageMarginLeft, mPageMarginTop, mPageMarginRight, mPageMarginBottom);
        setPageMargin(mPageMargin);
    }

    public int dp2px(Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }
    public void setAnimationEnabled(boolean enable) {
        this.animationEnabled = enable;
    }

    public void setFadeEnabled(boolean fadeEnabled) {
        this.fadeEnabled = fadeEnabled;
    }

    public void setFadeFactor(float fadeFactor) {
        this.fadeFactor = fadeFactor;
    }

    @Override
    public void setPageMargin(int marginPixels) {
        mPageMargin = marginPixels;
        setPadding(mPageMargin, mPageMargin, mPageMargin, mPageMargin);
    }

    @Override
    public void transformPage(View page, float position) {
//        if (mPageMargin <= 0|| !animationEnabled)
//            return;
//        page.setPadding(mPageMargin / 3, mPageMargin / 3, mPageMargin / 3, mPageMargin / 3);
//
//        if (MAX_SCALE == 0.0f && position > 0.0f && position < 1.0f) {
//            MAX_SCALE = position;
//        }
//        position = position - MAX_SCALE;
//        float absolutePosition = Math.abs(position);
//        if (position <= -1.0f || position >= 1.0f) {
//            if(fadeEnabled)
//            page.setAlpha(fadeFactor);
//            // Page is not visible -- stop any running animations
//
//        } else if (position == 0.0f) {
//
//            // Page is selected -- reset any views if necessary
//            page.setScaleX((1 + MAX_SCALE));
//            page.setScaleY((1 + MAX_SCALE));
//            page.setAlpha(1);
//        } else {
//            page.setScaleX(1 + MAX_SCALE * (1 - absolutePosition));
//            page.setScaleY(1 + MAX_SCALE * (1 - absolutePosition));
//            if(fadeEnabled)
//            page.setAlpha( Math.max(fadeFactor, 1 - absolutePosition));
//        }
    }
}
