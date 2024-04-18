package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.usacamp.R;

public class PresentWebView extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_web_view);

        String content = getIntent().getStringExtra("webcontent");
        WebView webView = (WebView) findViewById(R.id.presentwebview);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(content);
        //webView.loadDataWithBaseURL(Constants.MAIN_URL, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + content, "text/html", "UTF-8", null);
    }
    public void onBack(View view){
        finish();
    }
}
