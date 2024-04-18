package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.usacamp.R;

public class ActFinishLevelTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_finish_level_test);

        int point = getIntent().getIntExtra("point" , 0);
        int questionCnt = getIntent().getIntExtra("questioncount",0);

        String tempStr = String.valueOf(point);// + " / " + questionCnt;

        TextView txtPoint = (TextView)findViewById(R.id.pointleveltest);
        txtPoint.setText(tempStr);

        findViewById(R.id.golearn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent learnAct =  new Intent(ActFinishLevelTest.this, MainActivity.class);
                startActivity(learnAct);
                MainActivity.mainActivityInstance.goFragmentLearn();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .5), (int) (height * .6));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
