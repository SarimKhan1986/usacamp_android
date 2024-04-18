package com.usacamp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.constants.Constants;

public class LessonsMenuGridAdapter extends BaseAdapter {

    Activity mContext;
    LayoutInflater mInflater;
    int[] marrLessonStatus = new int[Constants.LESSON_COUNT];
    int mLayout;
    int mnPrevSelId = -1;

    public LessonsMenuGridAdapter(Activity context, int nlayout){
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = nlayout;

        for (int i = 0; i < Constants.LESSON_COUNT; i++)
            marrLessonStatus[i] = Constants.LESSON_UNAVAILABLE;
    }

    public void setLessonStatus(int nLessonId, int nStatus){

        if (mnPrevSelId != -1 && mnPrevSelId != 24)
            marrLessonStatus[mnPrevSelId] = Constants.LESSON_AVAILABLE;

        if(nLessonId == 24) {
            notifyDataSetChanged();
            return;
        }
        marrLessonStatus[nLessonId] = nStatus;
        mnPrevSelId = nLessonId;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Constants.LESSON_COUNT;
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
        LinearLayout lytRoot = null;
        TextView txt = null;
        if (i == Constants.LESSON_COUNT){
            if (view == null)
                view = mInflater.inflate(mLayout, viewGroup, false);
            lytRoot = view.findViewById(R.id.lyt_lesson_root);
            ViewGroup.LayoutParams params = lytRoot.getLayoutParams();

            txt = (TextView)view.findViewById(R.id.text_lesson_id);

            txt.setText("Test");
        }else{
            if (view == null)
                view = mInflater.inflate(mLayout, viewGroup, false);
            lytRoot = view.findViewById(R.id.lyt_lesson_root);
            txt = (TextView)view.findViewById(R.id.text_lesson_id);
            txt.setText(String.valueOf(i + 1));

        }
        switch (marrLessonStatus[i]){
            case Constants.LESSON_UNAVAILABLE:
                lytRoot.setBackgroundResource(R.drawable.gray_transparent_radius);
                txt.setTextColor(mContext.getResources().getColor(R.color.green_dark));
                break;
            case Constants.LESSON_AVAILABLE:
                lytRoot.setBackgroundResource(R.drawable.green_dark_radius_outline);
                txt.setTextColor(mContext.getResources().getColor(R.color.green_dark));
                break;
            case Constants.LESSON_SELECTED:
                lytRoot.setBackgroundResource(R.drawable.green_dark_radius_outline);
                txt.setTextColor(mContext.getResources().getColor(R.color.red));

                break;
        }


        return view;
    }
}
