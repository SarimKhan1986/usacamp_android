package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MyPaymentAdapter;
import com.usacamp.utils.MyPaymentItem;


import java.util.ArrayList;

public class payhistory extends BaseActivity {
    private ListView mlistPayment_ListView ;
    private ArrayList<MyPaymentItem> marrPaymentRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payhistory);

        String strrequestparameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id;
        MyApplication.mNetProc.getOrderInfo("getOrderInfo" , strrequestparameter );
        marrPaymentRecord = new ArrayList<MyPaymentItem>();
        //Zhuge
        ////ZhugeSDK.getInstance().track(this, Constants.Zhuge_Event_Enter_Profile_MyPayment);
    }
    public void refreshAllInformation()
    {
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistMyPaymentHistoryItems.size(); i++)
            marrPaymentRecord.add(MyApplication.mNetProc.mLoginUserInf.mlistMyPaymentHistoryItems.get(i));

        mlistPayment_ListView = findViewById(R.id.paylistview);
        MyPaymentAdapter adapterlearningrecord = new MyPaymentAdapter(this, R.layout.layout_mypayment_item, marrPaymentRecord);
        mlistPayment_ListView.setAdapter(adapterlearningrecord);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onBack(View view) {
        finish();
    }
}
