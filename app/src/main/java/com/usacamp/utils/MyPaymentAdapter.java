package com.usacamp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usacamp.R;

import java.util.ArrayList;

public class MyPaymentAdapter extends BaseAdapter {
    //list view item
     ArrayList<MyPaymentItem> marrMyPaymentItems;
     Context maincon;
     LayoutInflater mInflater;
     int mLayout;

    public MyPaymentAdapter(Context context, int nlayout, ArrayList<MyPaymentItem> arrSrc){
        maincon = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        marrMyPaymentItems = arrSrc;
        mLayout = nlayout;
    }
    @Override
    public int getCount() {
        return marrMyPaymentItems.size();
    }

    @Override
    public String getItem(int i) {
        return marrMyPaymentItems.get(i).mstrComment;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        if (view == null){
            view = mInflater.inflate(mLayout, viewGroup, false);
        }
        ((TextView)view.findViewById(R.id.txt_mypayment_comment)).setText(marrMyPaymentItems.get(i).mstrComment);
        ((TextView)view.findViewById(R.id.txt_mypayment_point)).setText(String.valueOf(marrMyPaymentItems.get(i).mnPoint));
        ((TextView)view.findViewById(R.id.txt_mypayment_date)).setText(marrMyPaymentItems.get(i).mstrDate);
        ((TextView)view.findViewById(R.id.txt_mypayment_price)).setText(String.valueOf(marrMyPaymentItems.get(i).mnPrice));
        return view;
    }


}
