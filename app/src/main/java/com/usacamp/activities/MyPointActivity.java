package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MyPointAdapter;
import com.usacamp.utils.MyPointItem;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MyPointActivity extends BaseActivity {
    private ListView mlistMyPoint;
    private ArrayList<MyPointItem> marrMyPoint;
    MyPointAdapter adapterlearningrecord = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_point);
        String strrequestparameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id;
        MyApplication.mNetProc.getPointHistory("getPointHistory" , strrequestparameter );
        marrMyPoint = new ArrayList<MyPointItem>();

        mlistMyPoint = findViewById(R.id.listview_mypoint);
        adapterlearningrecord = new MyPointAdapter(this , R.layout.layout_myppoint_item, marrMyPoint);
        mlistMyPoint.setAdapter(adapterlearningrecord);
        //Zhuge
        ////ZhugeSDK.getInstance().track(this, Constants.Zhuge_Event_Enter_Profile_MyPoint);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onBack(View view) {
        finish();
    }
    public void refreshAllInformation()
    {
        int i = 0;
        int m_ntotalpoint = 0;
        if (MyApplication.mNetProc.mLoginUserInf.mlistMyPoint.size() > 0){

            Collections.sort(marrMyPoint, new Comparator<MyPointItem>() {
                @Override
                public int compare(MyPointItem o1, MyPointItem o2) {
                    SimpleDateFormat o1formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat o2formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date o1date = null, o2date = null;
                    try {
                        o1date =  o1formatter.parse(o1.getMstrDate());
                        o2date =  o2formatter.parse(o2.getMstrDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return o2date.compareTo(o1date);
                }
            });
            for (i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistMyPoint.size(); i++) {
                marrMyPoint.add(MyApplication.mNetProc.mLoginUserInf.mlistMyPoint.get(i));
                m_ntotalpoint += marrMyPoint.get(i).getMnPoint();
            }

            ((TextView)  (findViewById(R.id.txt_totalpoint))).setText(String.valueOf(m_ntotalpoint));
        }
        adapterlearningrecord.notifyDataSetChanged();
    }
}
