package com.usacamp.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usacamp.R;

import java.util.ArrayList;

public class MyPointAdapter extends BaseAdapter {
    //list view item
     ArrayList<MyPointItem> marrMyPont;
     Context maincon;
     LayoutInflater mInflater;
     int mLayout;
    int count = 0;
    public MyPointAdapter(Context context, int nlayout, ArrayList<MyPointItem> arrSrc){
        maincon = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        marrMyPont = arrSrc;
        mLayout = nlayout;
    }
    @Override
    public int getCount() {
        return marrMyPont.size();
    }

    @Override
    public String getItem(int i) {
        return marrMyPont.get(i).mstrComment;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        count++;
        final int pos = i;
        if (view == null){
            view = mInflater.inflate(mLayout, viewGroup, false);
        }

        TextView txt = (TextView)view.findViewById(R.id.txt_mypoint_comment);
        txt.setText(marrMyPont.get(pos).mstrComment);
        txt = (TextView)view.findViewById(R.id.txt_mypoint_point);
        int nPoint = marrMyPont.get(pos).mnPoint;
        if (nPoint >=0) {
            txt.setTextColor(maincon.getResources().getColor(R.color.green_dark));
            txt.setText("+" + nPoint);
        } else {
            txt.setTextColor(maincon.getResources().getColor(R.color.red));
            txt.setText(String.valueOf(nPoint));
        }
        txt = (TextView)view.findViewById(R.id.txt_mypayment_date);
        txt.setText(marrMyPont.get(pos).mstrDate);

        return view;
    }


}
