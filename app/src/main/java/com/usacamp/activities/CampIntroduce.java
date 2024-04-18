package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
//

public class CampIntroduce extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_introduce);
        WebView introWebView = (WebView)findViewById(R.id.introwebview);

        introWebView.loadDataWithBaseURL("file:///android_res/drawable/", "<img src='intro2.webp' style='width:100%' />", "text/html", "utf-8", null);
        introWebView.getSettings().setBuiltInZoomControls(true);
        introWebView.getSettings().setDisplayZoomControls(false);

        //Zhuge

        //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Enter_Main_Intro_Page);
    }
    public void onBack(View view) {
        finish();
    }
}
