package com.usacamp.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.usacamp.R;

public class ActDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);
        Intent intent = getIntent();
        TextView txtview = (TextView)findViewById(R.id.txt_actdetail_content);
        String mstrActDetail = ((Intent) intent).getStringExtra("actdetail_content");
        txtview.setText(mstrActDetail);
    }
    public void onBack(View view) {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
