package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import com.usacamp.R;
import com.usacamp.constants.Constants;


public class FinishTestAct extends BaseActivity {

    int recomLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_test);
        Button btnGoPurchase = findViewById(R.id.btn_gopurchase);
        recomLevel = getIntent().getIntExtra("recommandLevel", 0);
        TextView txtCamp = findViewById(R.id.text_testrecommand);
        txtCamp.setText("CAMP " + recomLevel);
        if(MyApplication.mNetProc.mLoginUserInf.mPurchasedList.contains("CAMP " + recomLevel))
            btnGoPurchase.setText("去学习");
        else
            btnGoPurchase.setText("马上购买课程");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .6), (int) (height * .7));

        //Zhuge
        //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_PopUp_Test_TestingPage_PopWindow);

        ImageView imgTestClose = findViewById(R.id.img_testcloseQuiz);
           btnGoPurchase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(MyApplication.mNetProc.mLoginUserInf.mPurchasedList.contains("CAMP " + recomLevel))
                {
                    Intent purchaseAct = new Intent(v.getContext(), MainActivity.class);
                    startActivity(purchaseAct);
                    MainActivity.mainActivityInstance.goFragmentLearn();

                }else {
                    Intent purchaseAct = new Intent(v.getContext(), PurchaseCampActivity.class);
                    purchaseAct.putExtra(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Entry_Purchase_Test);
                    startActivity(purchaseAct);
                }
            }
        });

        imgTestClose.setOnClickListener(new View.OnClickListener() {

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
