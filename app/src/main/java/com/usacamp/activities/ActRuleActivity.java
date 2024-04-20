package com.usacamp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.usacamp.R;

public class ActRuleActivity extends BaseActivity {

    private WebView myBrowser;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_rule);
        myBrowser = (WebView) findViewById(R.id.mybrowser);
        final MyJavaScriptInterface myJavaScriptInterface
                = new MyJavaScriptInterface(this);
        myBrowser.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");

        myBrowser.getSettings().setJavaScriptEnabled(true);

        if(MyApplication.getInstance().strActivityRuleLink != "")
        {
            myBrowser.loadUrl(MyApplication.getInstance().strActivityRuleLink);
//            myBrowser.loadUrl("https://dev.usacamp.cn/uscamp/Wechat/h5_playrule");
        }
    }

    private void onLevelIntroduce()
    {
        String strrequestparameter ="param=activity_rule";
        MyApplication.mNetProc.getConfig("getConfig" , strrequestparameter );
//        Intent introAct =  new Intent(getContext(), CampIntroduce.class);
//        getContext().startActivity(introAct);
    }

    public void onClose(View view) { finish(); }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void showToast(String toast){
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
