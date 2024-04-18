package com.usacamp.utils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.usacamp.R;
import com.usacamp.model.ParentItem;

import java.util.ArrayList;

public class ParentAdapter extends BaseAdapter {
    //list view item
    ArrayList<ParentItem> marrItemList;
    Context maincon;
    LayoutInflater mInflater;
    int mLayout;

    public ParentAdapter() {}
    public ParentAdapter(Context context, int nlayout, ArrayList<ParentItem> arrSrc){
        maincon = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        marrItemList = arrSrc;
        mLayout = nlayout;
    }
    @Override
    public int getCount() {
        return marrItemList.size();
    }

    public void removeAdapter() {
        marrItemList.clear();
    }

    public void setAdapter(ArrayList<ParentItem> arr)
    {
        marrItemList=arr;
    }

    @Override
    public String getItem(int i) {
        return marrItemList.get(i).mstrTitle;
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
        TextView txt = (TextView)view.findViewById(R.id.parentitemtitle);
        txt.setText(marrItemList.get(pos).mstrTitle);
        txt = (TextView)view.findViewById(R.id.parentitemcontent);
        txt.setText(marrItemList.get(pos).mstrContent);
        ImageView imgView = (ImageView)view.findViewById(R.id.parentitemimg);
        Uri uri = Uri.parse(marrItemList.get(i).mstrImageUrl);
        RequestCreator reqCreator = Picasso.with(view.getContext()).load(uri).resize(800,600);
        reqCreator.into(imgView);

        if (marrItemList.get(pos).mIsImportant == 1){
            view.findViewById(R.id.parentitemimpimng).setVisibility(View.VISIBLE);
        }else
            view.findViewById(R.id.parentitemimpimng).setVisibility(View.INVISIBLE);

        return view;
    }
}
