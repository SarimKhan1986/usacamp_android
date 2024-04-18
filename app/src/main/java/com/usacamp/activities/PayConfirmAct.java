package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.usacamp.R;

public class PayConfirmAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_confirm);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .5), (int) (height * .4));
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
