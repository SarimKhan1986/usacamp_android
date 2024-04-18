package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.usacamp.R;
import com.usacamp.constants.Constants;
//

public class CustomServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_service);//activity_custom_service

        //Zhuge
        //////ZhugeSDK.getInstance().track(this, Constants.Zhuge_Event_Enter_Profile_Customer);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void refreshAllInformation(){
        //((TextView)findViewById(R.id.txt_customerservice)).setText(MyApplication.mNetProc.mLoginUserInf.mstrCustomerService);
    }
    public void onBack(View view) {
        finish();
    }
}
