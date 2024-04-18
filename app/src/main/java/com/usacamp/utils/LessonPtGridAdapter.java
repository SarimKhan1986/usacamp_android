package com.usacamp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.model.ScoreItem;

public class LessonPtGridAdapter extends BaseAdapter {

    Activity mContext;
    LayoutInflater mInflater;
    int[] marrLessonPts = new int[Constants.LESSON_COUNT];
    int mLayout;

    public LessonPtGridAdapter(Activity context, int nlayout){
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = nlayout;

        for (int i = 0; i < Constants.LESSON_COUNT; i++)
            marrLessonPts[i] = -1;
    }

    public void setLessonPt(ArrayList<ScoreItem> arrLessonPts){
        for (int i = 0; i < arrLessonPts.size(); i++)
            marrLessonPts[arrLessonPts.get(i).mnLessonId] = arrLessonPts.get(i).mnScore;
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
        if (view == null)
            view = mInflater.inflate(mLayout, viewGroup, false);

        TextView txt = (TextView)view.findViewById(R.id.text_lesson_id);
        txt.setText(String.valueOf(i + 1));
        txt = (TextView)view.findViewById(R.id.text_lesson_pt);
        if (marrLessonPts[i] == -1){
            txt.setText("");
        }else
            txt.setText(marrLessonPts[i] + " pt");

        return view;
    }
}
