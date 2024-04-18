package com.usacamp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.model.MessageItem;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
    //list view item
    ArrayList<MessageItem> marrMessage;
    Context maincon;
    LayoutInflater mInflater;
    int mLayout;

    public MessageAdapter(Context context, int nlayout, ArrayList<MessageItem> arrSrc){
        maincon = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        marrMessage = arrSrc;
        mLayout = nlayout;
    }
    @Override
    public int getCount() {
        return marrMessage.size();
    }

    public void removeAdapter() {
        marrMessage.clear();
    }

    public void setAdapter(ArrayList<MessageItem> arr)
    {
        marrMessage=arr;
    }

    @Override
    public String getItem(int i) {
        return marrMessage.get(i).mstrTitle;
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
        TextView txt = (TextView)view.findViewById(R.id.text_message_title);
        txt.setText(marrMessage.get(pos).mstrTitle);
        txt = (TextView)view.findViewById(R.id.text_message_content);
        txt.setText(marrMessage.get(pos).mstrContent);
        txt = (TextView)view.findViewById(R.id.text_message_date);
        txt.setText(marrMessage.get(pos).mstrDate);

        if (marrMessage.get(pos).mIsRead == 1){
            view.findViewById(R.id.messageacticon).setBackgroundResource(R.drawable.messageinactive);
        }else
            view.findViewById(R.id.messageacticon).setBackgroundResource(R.drawable.messageactive);

        return view;
    }
}
