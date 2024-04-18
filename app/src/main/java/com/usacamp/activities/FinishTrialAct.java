package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.usacamp.R;

public class FinishTrialAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_trial);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .6), (int) (height * .7));

        Button btnGoPurchase = findViewById(R.id.btn_gopurchase);
        ImageView imgTestClose = findViewById(R.id.img_testcloseQuiz);
        TextView txtMenu = findViewById(R.id.btn_gomenu);
        btnGoPurchase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent purchaseAct = new Intent(v.getContext(), PurchaseCampActivity.class);
                startActivity(purchaseAct);

            }
        });

        imgTestClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
