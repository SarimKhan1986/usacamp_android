package com.usacamp.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.usacamp.R;
import com.usacamp.activities.MyApplication;

import java.util.ArrayList;

public class LearningRecordAdapter extends BaseAdapter {
    //list view item
    ArrayList<LearningRecordItem> marrLearningRecord;
    Context maincon;
    LayoutInflater mInflater;
    int mLayout;

    public LearningRecordAdapter(Context context, int nlayout, ArrayList<LearningRecordItem> arrSrc){
        maincon = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        marrLearningRecord = arrSrc;
        mLayout = nlayout;
    }
    @Override
    public int getCount() {
        return marrLearningRecord.size();
    }

    @Override
    public String getItem(int i) {
        return marrLearningRecord.get(i).mstrComment;
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
        String strScoreDate = marrLearningRecord.get(pos).mstrSroceDate;
        if (strScoreDate.equals("null")) {
            TextView txt = (TextView) view.findViewById(R.id.txt_learningrecord_lesson);
            txt.setText(marrLearningRecord.get(pos).mstrComment);
            //txt = (TextView)view.findViewById(R.id.txt_learningrecord_part);
            //txt.setText("Part : " + marrLearningRecord.get(pos).mstrPart_id);
            txt = (TextView) view.findViewById(R.id.txt_learningrecord_status);
            txt.setText("学习中");
            txt.setTextColor(Color.rgb(15, 167, 58));
            txt = (TextView) view.findViewById(R.id.txt_learningrecord_comment);
            txt.setText("...");
            txt = (TextView) view.findViewById(R.id.txt_learningrecord_date);
            txt.setText("继续学习");
            txt.setTextSize(16);
            txt.setTextColor(Color.rgb(22, 155, 213));
        } else {
            TextView txt = (TextView) view.findViewById(R.id.txt_learningrecord_lesson);
            txt.setText(marrLearningRecord.get(pos).mstrComment);
            //txt = (TextView)view.findViewById(R.id.txt_learningrecord_part);
            //txt.setText("Part : " + marrLearningRecord.get(pos).mstrPart_id);
            txt = (TextView) view.findViewById(R.id.txt_learningrecord_status);
            txt.setText("已完成");
            txt.setTextColor(Color.rgb(22, 155, 213));
            txt = (TextView) view.findViewById(R.id.txt_learningrecord_comment);
            if(!marrLearningRecord.get(pos).mstrComment.contains("LESSON")) {
                //((TextView)view.findViewById(R.id.txt_learningrecord_part)).setText("");
                txt.setText("test : " + marrLearningRecord.get(pos).mstrScore);
            } else {
                txt.setText("quiz : " + marrLearningRecord.get(pos).mstrScore);
            }
            txt = (TextView) view.findViewById(R.id.txt_learningrecord_date);
            txt.setText(strScoreDate);
            txt.setTextSize(14);
            txt.setTextColor(Color.BLACK);
        }
        switch (marrLearningRecord.get(pos).kind) {
            case 0:
                ((Group) view.findViewById(R.id.group)).setVisibility(View.VISIBLE);
                ((Button) view.findViewById(R.id.button26)).setVisibility(View.GONE);
                break;
            case 1://음성평가
                ((Group) view.findViewById(R.id.group)).setVisibility(View.GONE);
                ((Button) view.findViewById(R.id.button26)).setVisibility(View.VISIBLE);
                ((Button) view.findViewById(R.id.button26)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strrequestparameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id + "&learn_id=" + marrLearningRecord.get(pos).id;
                        MyApplication.mNetProc.getOralLearnInfo("getOralLearnInfo", strrequestparameter);
                    }
                });
                break;

        }

        return view;
    }
}
