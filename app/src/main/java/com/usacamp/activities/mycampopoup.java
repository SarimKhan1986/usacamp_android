package com.usacamp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.constants.Constants;

public class mycampopoup extends Activity{

    private final Button[] btn = new Button[5];
    private final TextView[] cmpLessonNumListTxt = new TextView[24];
    private final TextView[] cmpLessonPointListTxt = new TextView[24];
    private Button btn_unfocus;
    private LinearLayout camplayout ;
    private TextView campTestTxt , campTestPassTxt, campTestFailTxt ,campTestMsgTxt ;
    private final int[] cmpLessontNumtextId = {R.id.cmplessonNum1, R.id.cmplessonNum2, R.id.cmplessonNum3, R.id.cmplessonNum4,R.id.cmplessonNum5,R.id.cmplessonNum6,R.id.cmplessonNum7,R.id.cmplessonNum8,
            R.id.cmplessonNum9,R.id.cmplessonNum10,R.id.cmplessonNum11,R.id.cmplessonNum12,R.id.cmplessonNum13,R.id.cmplessonNum14,R.id.cmplessonNum15,R.id.cmplessonNum16,
            R.id.cmplessonNum17,R.id.cmplessonNum18,R.id.cmplessonNum19,R.id.cmplessonNum20,R.id.cmplessonNum21,R.id.cmplessonNum22,R.id.cmplessonNum23,R.id.cmplessonNum24};
    private final int[] cmpLessonPointtextId = {  R.id.cmplessonPoint1,   R.id.cmplessonPoint2,   R.id.cmplessonPoint3,   R.id.cmplessonPoint4,  R.id.cmplessonPoint5,  R.id.cmplessonPoint6,  R.id.cmplessonPoint7,  R.id.cmplessonPoint8,
              R.id.cmplessonPoint9,  R.id.cmplessonPoint10,  R.id.cmplessonPoint11,  R.id.cmplessonPoint12,  R.id.cmplessonPoint13,  R.id.cmplessonPoint14,  R.id.cmplessonPoint15,  R.id.cmplessonPoint16,
              R.id.cmplessonPoint17,  R.id.cmplessonPoint18,  R.id.cmplessonPoint19,  R.id.cmplessonPoint20,  R.id.cmplessonPoint21,  R.id.cmplessonPoint22,  R.id.cmplessonPoint23, R.id.cmplessonPoint24};

    int currnetTabIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycampopoup);
        TextView campLevelTxt  = (TextView) findViewById(R.id.campleveltxt);
        for(int i = 0; i < Constants.LESSON_COUNT; i++)
        {
            cmpLessonNumListTxt[i] = (TextView) findViewById(cmpLessontNumtextId[i]);
            cmpLessonPointListTxt[i] = (TextView) findViewById(cmpLessonPointtextId[i]);
        }
        campLevelTxt.setText("Camp " + MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int) (height * .8));
        setlessonNumandPoint();
    }

    private void setlessonNumandPoint()
    {
        for(int i = 0; i < Constants.LESSON_COUNT; i++)
            cmpLessonNumListTxt[i].setText(String.valueOf(i + 1));
        if(MyApplication.mNetProc.mLoginUserInf.mlistScores == null)
            return;

        for(int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistScores.size() ; i++)
            cmpLessonPointListTxt[MyApplication.mNetProc.mLoginUserInf.mlistScores.get(i).mnLessonId].setText(MyApplication.mNetProc.mLoginUserInf.mlistScores.get(i).mnScore + " pt");

    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void GotoLession(View v) {
        finish();

    }
}