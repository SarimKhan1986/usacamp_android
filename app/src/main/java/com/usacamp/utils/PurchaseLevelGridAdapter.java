package com.usacamp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.constants.Constants;

public class PurchaseLevelGridAdapter extends BaseAdapter {

    Activity mContext;
    LayoutInflater mInflater;
    int[] marrCampStatus = new int[Constants.LEVEL_COUNT1];
    int mLayout;

    public PurchaseLevelGridAdapter(Activity context, int nlayout){
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = nlayout;

        for (int i = 0; i < Constants.LEVEL_COUNT1; i++)
            marrCampStatus[i] = Constants.LEVEL_UNAVAILABLE;
    }

    public void setLevelStatus(int nLevelId, int nStatus){
        marrCampStatus[nLevelId] = nStatus;
        notifyDataSetChanged();
    }
    public int getLevelStatus(int nLevelId){
        return marrCampStatus[nLevelId];
    }


    @Override
    public int getCount() {
        return Constants.LEVEL_COUNT1;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = mInflater.inflate(mLayout, viewGroup, false);

        RelativeLayout lytRoot = view.findViewById(R.id.lyt_purchase_lesson);
        switch (marrCampStatus[i]){
            case Constants.LEVEL_UNAVAILABLE:
                lytRoot.setBackgroundResource(R.drawable.white_radius);
                ((ImageView)view.findViewById(R.id.image_checkable)).setImageResource(R.drawable.purchase_enable);
                ((TextView)view.findViewById(R.id.text_level_id)).setTextColor(mContext.getResources().getColor(R.color.black));
                ((TextView)view.findViewById(R.id.text_level_en)).setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            case Constants.LEVEL_EXPERIENCE:
                lytRoot.setBackgroundResource(R.drawable.white_radius);
                ((ImageView)view.findViewById(R.id.image_checkable)).setImageResource(R.drawable.purchase_enable);
                ((TextView)view.findViewById(R.id.text_level_id)).setTextColor(mContext.getResources().getColor(R.color.black));
                ((TextView)view.findViewById(R.id.text_level_en)).setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            case Constants.LEVEL_ALREADY_PURCHASE:
                lytRoot.setBackgroundResource(R.drawable.purchaseitemback_purchased);
                ((ImageView)view.findViewById(R.id.image_checkable)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.text_level_id)).setTextColor(mContext.getResources().getColor(R.color.black));
                ((TextView)view.findViewById(R.id.text_level_en)).setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            case Constants.LEVEL_SELECTED:
                lytRoot.setBackgroundResource(R.drawable.purcahseitemback_selected);
                ((ImageView)view.findViewById(R.id.image_checkable)).setImageResource(R.drawable.purchase_selected);
                ((TextView)view.findViewById(R.id.text_level_id)).setTextColor(Color.parseColor("#F84C59"));
                ((TextView)view.findViewById(R.id.text_level_en)).setTextColor(Color.parseColor("#F84C59"));
                break;
        }

        TextView txt = (TextView)view.findViewById(R.id.text_level_id);
        txt.setText("级别 " + (i + 1));
        TextView txten = (TextView)view.findViewById(R.id.text_level_en);
        txten.setText("Camp " + (i + 1));
        return view;
    }
}
