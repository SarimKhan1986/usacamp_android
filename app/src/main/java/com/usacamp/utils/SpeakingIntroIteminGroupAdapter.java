package com.usacamp.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.usacamp.R;
import com.usacamp.model.SpeakingRowItem;

import java.util.ArrayList;

public class SpeakingIntroIteminGroupAdapter extends BaseAdapter {
    //list view item
    ArrayList<SpeakingRowItem> mSpeakingItem;
    Context maincon;
    LayoutInflater mInflater;
    int mLayout;
    int callcount = 0;
    int mType = 0; // 0 - hearing, 1- speaking
    public SpeakingIntroIteminGroupAdapter(Context context, int nlayout, ArrayList<SpeakingRowItem> arrSrc, int type){
        maincon = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSpeakingItem = arrSrc;
        mLayout = nlayout;
        mType = type;
    }
    @Override
    public int getCount() {
        return mSpeakingItem.size();
    }

    @Override
    public String getItem(int n) {
        return "";
    }
    @Override
    public long getItemId(int n) {
        return n;
    }
    @Override
    public View getView(int n, View view, ViewGroup viewGroup) {
        callcount++;
        SpeakingRowItem rowItem = mSpeakingItem.get(n);
        if (view == null){
            view = mInflater.inflate(mLayout, viewGroup, false);
        }
        Log.d("callcount1", String.valueOf(callcount));
        TextView titletxt, commenttxt;
        titletxt = (TextView) (view.findViewById(R.id.txt_speakingintro_title));
        commenttxt = (TextView) (view.findViewById(R.id.txt_speakingintro_comment));
        titletxt.setText(rowItem.mstrTitle);
        commenttxt.setText(rowItem.mstrComment);
        switch (mType){
            case 0 ://hearing
                if(n % 3 == 0)
                    titletxt.setBackgroundColor(Color.parseColor("#1D9F38"));
                else if(n % 3 == 1)
                    titletxt.setBackgroundColor(Color.parseColor("#FFBB00"));
                else
                    titletxt.setBackgroundColor(Color.parseColor("#5DA2FF"));
                break;
            case 1://speaking
                if(n % 2 == 0)
                    titletxt.setBackgroundColor(Color.parseColor("#FFBB00"));
                else
                    titletxt.setBackgroundColor(Color.parseColor("#1D9F38"));
                break;
        }

        return view;
    }
}
