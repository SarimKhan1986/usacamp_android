package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.usacamp.R;

public class ActUsePointForPurchase extends BaseActivity {

    ImageView imgUnChecked, imgChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_use_point_for_purchase);
        imgUnChecked = (ImageView) findViewById(R.id.imguncheckImg);
        imgChecked = (ImageView) findViewById(R.id.imgcheckImg);

        ((TextView)findViewById(R.id.pointxt)).setText("共计积分" + MyApplication.mNetProc.mLoginUserInf.mnUserPoint);

        ((TextView)findViewById(R.id.moneytxt)).setText("可扺扣" + MyApplication.mNetProc.mLoginUserInf.mnUserPoint / MyApplication.mNetProc.mLoginUserInf.mnRate + "元");
    }
    public void onBack(View view){
        finish();
    }

    public void onCheckState(View view)
    {
//        if(MyApplication.mNetProc.mLoginUserInf.isUsePoint) {
//            MyApplication.mNetProc.mLoginUserInf.isUsePoint = false;
//            imgChecked.setVisibility(View.INVISIBLE);
//            imgUnChecked.setVisibility(View.VISIBLE);
//        }else{
//            MyApplication.mNetProc.mLoginUserInf.isUsePoint = true;
//            imgChecked.setVisibility(View.VISIBLE);
//            imgUnChecked.setVisibility(View.INVISIBLE);
//        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
