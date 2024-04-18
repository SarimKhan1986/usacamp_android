package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.GridView;

import com.usacamp.R;
import com.usacamp.utils.LessonPtGridAdapter;

public class MyCampActivity extends BaseActivity {

    GridView mgridLessonPt;
    LessonPtGridAdapter mlesssonPtGridAdapter;

    int mnCurrLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camp);

        mnCurrLevel = getIntent().getIntExtra("Level_Id", 0);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int) (height * .8));

//        String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
//                "&type=1" + "&level_id=" + String.valueOf(mnCurrLevel);
//
//        MyApplication.mNetProc.getQuizTestResult(this, "getQuizTestResult", strRequestParameter);

        mgridLessonPt = (GridView)findViewById(R.id.grid_lesson_pt);
        mlesssonPtGridAdapter = new LessonPtGridAdapter(this, R.layout.layout_lessonpt_item);
    }

    public void refreshLessonPt(){

        mlesssonPtGridAdapter.setLessonPt(MyApplication.mNetProc.mLoginUserInf.mlistScores);
        mlesssonPtGridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
