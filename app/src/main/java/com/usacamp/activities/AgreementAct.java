package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.usacamp.R;
import com.usacamp.constants.Constants;

public class AgreementAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        WebView pWebview = (WebView) findViewById(R.id.agreementWebview);
        pWebview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        pWebview.loadUrl(Constants.AGREENEBT_URL);
    }

    public void onBack(View view) {
        finish();
    }
}
