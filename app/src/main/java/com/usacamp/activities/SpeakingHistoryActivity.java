package com.usacamp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.usacamp.R;
import com.usacamp.utils.LearningRecordAdapter;
import com.usacamp.utils.LearningRecordItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SpeakingHistoryActivity extends BaseActivity {
    private ListView mlistLearningSpeechRecord;
    private ArrayList<LearningRecordItem> marrLearningRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_history);
        String strrequestparameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id + "&type=1" ;
        MyApplication.mNetProc.getLearnList("getLearnList" , strrequestparameter );
        marrLearningRecord = new ArrayList<LearningRecordItem>();

        mlistLearningSpeechRecord = findViewById(R.id.speakinghistorylistview);
        LearningRecordAdapter adapterlearningrecord = new LearningRecordAdapter(this, R.layout.layout_learningrecord_item, marrLearningRecord);
        mlistLearningSpeechRecord.setAdapter(adapterlearningrecord);
    }

    public void onBack(View view) {
        finish();
    }
    public void refreshAllInformation()
    {
        int i = 0;
        if ( MyApplication.mNetProc.mLoginUserInf.mlistLearningRecord.size() > 0){
            Collections.sort(MyApplication.mNetProc.mLoginUserInf.mlistLearningRecord, new Comparator<LearningRecordItem>() {

                @Override
                public int compare(LearningRecordItem o1, LearningRecordItem o2) {
                    SimpleDateFormat o1formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat o2formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date o1date = null, o2date = null;
                    try {
                        o1date =  o1formatter.parse(o1.mstrSroceDate);
                        o2date =  o2formatter.parse(o2.mstrSroceDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return o2date.compareTo(o1date);
                }
            });
            findViewById(R.id.listview_learningrecord).setVisibility(View.VISIBLE);
            for (i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistLearningRecord.size(); i++)
                marrLearningRecord.add(MyApplication.mNetProc.mLoginUserInf.mlistLearningRecord.get(i));
        }
        else{
            findViewById(R.id.listview_learningrecord).setVisibility(View.GONE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}