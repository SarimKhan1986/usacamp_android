package com.usacamp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends BaseActivity {

    Toast mToast = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        ((TextView)findViewById(R.id.txt_appversion)).setText("V" + BuildConfig.VERSION_NAME + "版本");
        ((TextView)findViewById(R.id.txt_websitelink)).setText(MyApplication.getInstance().SERVER_URL);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onBack(View view) {
        finish();
    }

}
