package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.usacamp.R;
import com.usacamp.constants.Constants;

public class FInishQuiz extends BaseActivity {

    private int CurretLvl = 0;
    private int CurretLesson = 0;
    boolean isQuizShare = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_finish_lesson);
//        TextView pointTxt = findViewById(R.id.text_score);
//        pointTxt.setText("+" + String.valueOf(MyApplication.mNetProc.mLoginUserInf.mSharePointValue));
        Button shareBtn = findViewById(R.id.btn_just_record);
        isQuizShare = getIntent().getBooleanExtra("Finish_Lesson", false);
        if (isQuizShare){
            CurretLvl = getIntent().getIntExtra("Level_Id", 0);
            CurretLesson = getIntent().getIntExtra("Lesson_Id", 0);
            String strTitle = String.format("Camp %d-Lesson %d", CurretLvl, CurretLesson);
            //((TextView)findViewById(R.id.text_finished_lesson)).setText(strTitle);
        }
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent shareIntent = new Intent(FInishQuiz.this, ShareActivity.class);
//                shareIntent.putExtra("Finish_Lesson", true);
//                shareIntent.putExtra("Level_Id", CurretLvl);
//                shareIntent.putExtra("Lesson_Id", CurretLesson + 1);
//                shareIntent.putExtra(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Share_Entry_Quiz);
                String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                        "&kind=0";

                MyApplication.mNetProc.getShareInfo("getShareInfo", strRequestParameter);
            }

        });

        ImageView dismissBtn = findViewById(R.id.img_closeQuiz);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width * .5), (int) (height * .6));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
