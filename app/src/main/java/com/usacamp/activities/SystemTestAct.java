package com.usacamp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MessageAlert;


public class SystemTestAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_test);

        findViewById(R.id.lobtn_start_testing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if ( MyApplication.mNetProc.mfNetworkEnable == true ) {
                    if(MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                        Intent learnActvity = new Intent(SystemTestAct.this, LearnActivity.class);
                        learnActvity.putExtra("LearnMode", 2);
                        startActivity(learnActvity);
                    } else {
                        MainActivity.mainActivityInstance.loginAuth(false);
                        //Intent loginAct = new Intent(SystemTestAct.this, LoginActivity.class);
                        //loginAct.putExtra(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Enter_LoginPage_Test);
                        //MyApplication.mNetProc.mLoginUserInf.mTempLoginPriviousAct = "test";
                        //startActivity(loginAct);
                    }
                }
                else {
                    MessageAlert.showMessage( SystemTestAct.this, "网错误，不能下载课件！");
                }

                //Zhuge
                ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Test_TestButton);
            }
        });

    }
    public void onBack(View view) {
        finish();
    }
}