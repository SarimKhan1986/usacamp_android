package com.usacamp.model;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class ShareTypeItem {

    public ShareTypeItem(int param1, Drawable pararm2, boolean param3, String param4, String param5, int param6, Drawable param7)
    {
        idx = param1;
        mImg = pararm2;
        isCardShowing = param3;
        cardtvtxt1 = param4;
        cardtvtxt2 = param5;
        cardcolor = param6;
        mBottomImage = param7;
    }
    public int idx;
    public Drawable mImg;
    public boolean isCardShowing;
    public String cardtvtxt1, cardtvtxt2;
    public int cardcolor;
    public Drawable mBottomImage;
}
