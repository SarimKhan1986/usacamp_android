package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.usacamp.R;

public class VersionUpgrade extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.verionupgrade);

        TextView texttitle = (TextView) findViewById(R.id.versiontile);
        texttitle.setText(MyApplication.mNetProc.mLoginUserInf.versionInfo.versionName);
        TextView textcontent = (TextView) findViewById(R.id.versionContent);
        textcontent.setText(MyApplication.mNetProc.mLoginUserInf.versionInfo.content);

        Button dialogButtonLater = (Button) findViewById(R.id.btnlater);
        // if button is clicked, close the custom dialog
        dialogButtonLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        Button dialogButtonUpgrade = (Button) findViewById(R.id.btnupgrade);
        // if button is clicked, close the custom dialog
        dialogButtonUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .7), (int) (height * .8));

    }
}
