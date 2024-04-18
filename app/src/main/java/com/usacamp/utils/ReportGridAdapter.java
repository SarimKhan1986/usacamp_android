package com.usacamp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

import com.usacamp.R;

public class ReportGridAdapter extends BaseAdapter {

    Activity mContext;
    LayoutInflater mInflater;
    int mLayout;
    int mnPrevSelId = 0;
    public ArrayList<Uri> mlistReport = new ArrayList<Uri>();
    int nDeletableId = -1;

    public ReportGridAdapter(Activity context, int nlayout){
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = nlayout;
    }

    public void addReport(Uri uriImagePath){
        nDeletableId = -1;
        mlistReport.add(uriImagePath);
    }
    public void deleteReport(int nId){
        if (nDeletableId == nId)
            mlistReport.remove(nId);
        nDeletableId = -1;
    }
    public void setDeletable(int nId){
        nDeletableId = nId;
    }

    @Override
    public int getCount() {
        return mlistReport.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return mlistReport.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null)
            view = mInflater.inflate(mLayout, viewGroup, false);

        if (i == mlistReport.size()){
            view.findViewById(R.id.lyt_add_report).setVisibility(View.VISIBLE);
            view.findViewById(R.id.img_report_image).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.img_delete_report).setVisibility(View.INVISIBLE);
        }else{
            view.findViewById(R.id.lyt_add_report).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.img_report_image).setVisibility(View.VISIBLE);


            //Uri uri= Uri.parse(mlistReport.get(i));
            RequestCreator reqCreator = Picasso.with(mContext).load(mlistReport.get(i));
            reqCreator.into( (ImageView)view.findViewById(R.id.img_report_image));

            if (nDeletableId == i)
                view.findViewById(R.id.img_delete_report).setVisibility(View.VISIBLE);
            else
                view.findViewById(R.id.img_delete_report).setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
