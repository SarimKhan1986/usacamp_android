package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.usacamp.R;

import androidx.appcompat.app.AppCompatActivity;

public class ActBuyLevelInHome extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_buy_level_in_home);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .7), (int) (height * .7));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onClose(View view) { finish(); }
    public void gotoPurchaseLevel(View v)
    {
        Intent purActInt = new Intent(this, PurchaseCampActivity.class);
        startActivity(purActInt);
    }

}
