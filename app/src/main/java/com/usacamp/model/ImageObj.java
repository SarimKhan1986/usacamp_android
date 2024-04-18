package com.usacamp.model;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

public class ImageObj {
    public ImageObj(int Id, Drawable dr, String imagelink, String link, int order, String backcolor , String fontcolor, int type){
        mIdx = Id;
        imageDrw = dr;
        mstrImageLink = imagelink;
        mstrLink = link;
        mOrderNum = order;
        mstrTitleBackground= backcolor;
        mstrFontColor = fontcolor;
        muserType = type;
    }
    public int mIdx;
    public Drawable imageDrw;
    public String mstrImageLink;
    public String mstrLink;
    public int mOrderNum;
    public String mstrTitleBackground;
    public String mstrFontColor;
    public int muserType;

}