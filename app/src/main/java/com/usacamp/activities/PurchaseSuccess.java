package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.usacamp.R;

public class PurchaseSuccess extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase_success);
        String  gettedPoint = "获得" + MyApplication.mNetProc.mLoginUserInf.mnPlusPoint + "积分";
        ((TextView)findViewById(R.id.getpointtxt)).setText(gettedPoint);
    }

    public void onGoToHome (View view)
    {
        finish();
        Intent homeAct = new Intent(PurchaseSuccess.this, MainActivity.class);
        startActivity(homeAct);
    }

    public void gotolearn(View view)
    {
        finish();
        Intent homeAct = new Intent(PurchaseSuccess.this, MainActivity.class);
        startActivity(homeAct);
        MainActivity.mainActivityInstance.goFragmentLearn();
    }

    public void gotoprofile(View view)
    {
        finish();
        Intent homeAct = new Intent(PurchaseSuccess.this, MainActivity.class);
        startActivity(homeAct);
        MainActivity.mainActivityInstance.goFragmentProfile();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
