package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.model.LessonItem;
import com.usacamp.utils.MessageAlert;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ExperienceActivity extends BaseActivity {

    int mnSelLevel = 0;
    int mnSelLesson = 0;
    ArrayList<Integer> marrSelParts = new ArrayList<Integer>();
    int[] manLessonId = new int[Constants.TRIAL_LEVEL_COUNT];

    int[] manResourceLevelText = {
            R.id.text_camp_level1,
            R.id.text_camp_level2,
            R.id.text_camp_level3,
            R.id.text_camp_level4,
            R.id.text_camp_level5,
            R.id.text_camp_level6
    };

    String getLessonName(String strLessonUrl){

        StringBuffer delimiter = new StringBuffer();
        delimiter.append('/');
        StringTokenizer st = new StringTokenizer(strLessonUrl, delimiter.toString());
        int nIdx = 0;
        String strTemp = "";
        while (st.hasMoreTokens()){
            strTemp = st.nextToken();
            if (nIdx == 4)
                break;

            nIdx++;
        }

        return strTemp.substring(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);
        String strPart = "";
        String strLessonName = "";
        String strCamp = "";

        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.size(); i++){
            LessonItem lessonItem = MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(i);
            manLessonId[i] = lessonItem.mnLessonId;
//            if (lessonItem.mnLevelId <= Constants.BASE_CAMP_LEVEL)
//                strCamp = "Base Camp";
//            else if (lessonItem.mnLevelId <= Constants.CAMP_750_LEVEL)
//                strCamp = "Camp 750";
//            else if (lessonItem.mnLevelId <= Constants.CAMP_1500_LEVEL)
//                strCamp = "Camp 1500";

//            ((TextView)findViewById(manResourceLevelText[i])).setText(strCamp);
//            strPart = lessonItem.mlistParts.get(0).mstrUrl;
//            strLessonName = "0" + getLessonName(strPart);
//            ((TextView)findViewById(manResourceLessonText[i])).setText(strLessonName);
        }
    }
    public void goLearnActivity(){
        if ( MyApplication.mNetProc.mfNetworkEnable == true ) {
            Intent learnActvity = new Intent(this, LearnActivity.class);
            learnActvity.putExtra("LearnMode", 1);
            learnActvity.putExtra("LevelId", mnSelLevel);
            learnActvity.putExtra("LessonId", mnSelLesson - 1);
            learnActvity.putIntegerArrayListExtra("AvailableParts", marrSelParts);
            startActivity(learnActvity);
        }
        else {
            MessageAlert.showMessage( this, "网错误，不能下载课件！");
        }
    }

    public void onBack(View view){
        finish();
    }
    public void onExperience1(View view){
        mnSelLevel = MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(0).mnLevelId;

        mnSelLesson = manLessonId[0];
        marrSelParts.clear();
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(0).mlistParts.size(); i++)
            marrSelParts.add(MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(0).mlistParts.get(i).mnPartId);
        goLearnActivity();
    }
    public void onExperience2(View view){
        mnSelLevel = MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(1).mnLevelId;
        mnSelLesson = manLessonId[1];
        marrSelParts.clear();
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(1).mlistParts.size(); i++)
            marrSelParts.add(MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(1).mlistParts.get(i).mnPartId);
        goLearnActivity();
    }
    public void onExperience3(View view){
        mnSelLevel = MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(2).mnLevelId;
        mnSelLesson = manLessonId[2];
        marrSelParts.clear();
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(2).mlistParts.size(); i++)
            marrSelParts.add(MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(2).mlistParts.get(i).mnPartId);
        goLearnActivity();
    }
    public void onExperience4(View view){
        mnSelLevel = MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(3).mnLevelId;
        mnSelLesson = manLessonId[3];
        marrSelParts.clear();
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(3).mlistParts.size(); i++)
            marrSelParts.add(MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(3).mlistParts.get(i).mnPartId);
        goLearnActivity();
    }
    public void onExperience5(View view){
        mnSelLevel = MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(4).mnLevelId;
        mnSelLesson = manLessonId[4];
        marrSelParts.clear();
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(4).mlistParts.size(); i++)
            marrSelParts.add(MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(4).mlistParts.get(i).mnPartId);
        goLearnActivity();
    }
    public void onExperience6(View view){
        mnSelLevel = MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(5).mnLevelId;
        mnSelLesson = manLessonId[5];
        marrSelParts.clear();
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(5).mlistParts.size(); i++)
            marrSelParts.add(MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(5).mlistParts.get(i).mnPartId);
        goLearnActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
