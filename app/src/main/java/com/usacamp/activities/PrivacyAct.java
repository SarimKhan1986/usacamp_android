package com.usacamp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.usacamp.R;
import com.usacamp.constants.Constants;

public class PrivacyAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        WebView pWebview = (WebView) findViewById(R.id.privacywebview);

        pWebview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        pWebview.loadUrl(Constants.PRIVACY_URL);
    }

    public void onBack(View view) {
        Log.d("goback","goback");
        finish();
    }
}
