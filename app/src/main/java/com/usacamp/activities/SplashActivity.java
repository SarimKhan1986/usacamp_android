package com.usacamp.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.utils.MessageAlert;

import org.json.JSONException;

public class SplashActivity extends BaseActivity {

    RelativeLayout popDlglt;

    private static final int MSG = 1;
    private int mnTime = 3;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWidthofPhone();

        TextView textView = findViewById(R.id.splashtext);
        //String text = "游美英语（以下简称“我们”）感谢您对我们的产品和服务的信任，我们深知个人信息对您的重要性，所以我们非常重视保护用户的隐私，也将按法律法规要求，采取相应安全保护措施，尽力保护您的个人信息安全可控。\n因此，“游美英语”产品和服务提供者，北京知恩教育科技有限公司（下称“知恩教育”）制定本 <隐私政策>（以下简称“本政策”）\n。通过本政策，我们主要向您阐述我们收集哪些信息、所收集信息的用途、您所享有的权利等重要信息。\n我们未来可能会根据信息处理情境的变化更新或修改本政策。\n我们会通过站内提示通知您所有的更新情况。\n本政策适用于“游美英语”儿童英语启蒙产品及服务的各个方面，包括“游美英语”儿童英语启蒙的安卓和IOS移动端。\n请您在使用“游美英语”儿童英语启蒙产品和服务前仔细阅读并理解本政策。\n在此我们承诺，我们将网络安全标准，采取相应的安全保护措施来保护您的个人信息。\n一旦您开始使用“游美英语”产品或服务，即表示您已充分理解并同意我们按照本政策收集、使用、保存和共享您的相关信息。\n如您对本政策内容有任何疑问、意见或建议，您可以通过“游美英语”网站（https://www.usacamp.cn）页面的客服咨询电话，或通过文末的联系方式直接与我们联系。\n您可以阅读完整版《用户协议》和《隐私政策》了解详细信息\n";
        String text = "游美英语(以下简称“我们”)感谢您对我们的产品和服务的信任，我们深知个人信息对您的重要性，所以我们非常重视保护用户的隐私，也将按法律法规要求，采取相应安全保护措施，尽力保护您的个人信息安全可控。 因此，“游美英语”产品和服务提供者，北京知恩教育科技有限公司(下称“知恩教育”)制定本<隐私政策>(以下简称“本政策”)。\n您可以查看完整版《隐私协议》,《用户协议》" ;
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                gotoagreement();
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                gotoprivacy();
            }
        };
        int startInx1 = text.indexOf("《用户协议》");
        int endInx1 = startInx1 + ("《用户协议》").length();
        spannableString.setSpan(clickableSpan1, startInx1, endInx1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#149F38")), startInx1, endInx1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), startInx1, endInx1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int startInx2 = text.indexOf("《隐私协议》");
        int endInx2 = startInx2 + ("《隐私协议》").length();
        spannableString.setSpan(clickableSpan2, startInx2, endInx2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#149F38")), startInx2, endInx2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), startInx2, endInx2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        MyApplication.mNetProc.getEncryptKey("getEncryptKey");

        popDlglt = (RelativeLayout) findViewById(R.id.popdlg);
        if (!MyApplication.getInstance().getIsLoggedIn()) {
            popDlglt.setVisibility(View.VISIBLE);
        } else {
            popDlglt.setVisibility(View.GONE);
            OnPassedPopDlg();
        }
    }

    @Override
    protected void onResume() {
        Log.d("splashResume", "resume");
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this);

        MyApplication.getInstance().reCallAppData();

    }

    @SuppressLint("HandlerLeak")
    public void OnPassedPopDlg() {
        mHandler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void handleMessage(Message msg) {
                mnTime--;
                if (mnTime == 0) {
                    if (MyApplication.mNetProc.mLoginUserInf.mlistHotParentItem.size() == 0) {
                        try {
                            MyApplication.getInstance().saveErrorInfo("Splash", "None");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MessageAlert.showAlertDlg(SplashActivity.this);
                        MyApplication.getInstance().isFirstLoginFailure = true;
                    } else {
                        MyApplication.getInstance().isFirstLoginFailure = false;
                        if(State.getCurrent_login_state() == State.loginType.logout){
                            Intent mainInt = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(mainInt);
                            MyApplication.getInstance().isLogout = true;
                        } else {
                            MyApplication.getInstance().isLogout = false;
                            MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_LOGIN, "loginWithPhone", "phone=" + MyApplication.getInstance().getPhoneNumberFromPref() +
                                    "&hardTypeId=" + MyApplication.getInstance().getHardWareTypeFromPref() + "&hardware=" +  MyApplication.getInstance().getHardWareSerialNumberFromPref() + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_SMS, MyApplication.getInstance().getPhoneNumberFromPref());

                        }
//                        finish();
                    }

                } else
                    sendMessageDelayed(obtainMessage(MSG), 1000);
            }
        };

        mHandler.sendMessage(mHandler.obtainMessage(MSG));

    }

    public void gotoprivacy() {
        Intent privacyInt = new Intent(this, PrivacyAct.class);
        startActivity(privacyInt);
    }

    public void gotoagreement() {
        Intent agreementInt = new Intent(this, AgreementAct.class);
        startActivity(agreementInt);
    }

    public void onCancel(View v) {
        finish();
    }

    public void onOK(View v) {
        popDlglt.setVisibility(View.GONE);
        MyApplication.getInstance().saveIsLoggedIn(true);
        OnPassedPopDlg();

    }

    public void getWidthofPhone() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int dpWidth = (int) ((int) width / displayMetrics.density);
        int dpHeight = (int) ((int) height / displayMetrics.density);
        MyApplication.getInstance().setPhoneWidthHeight(width, height);

        Log.d("dpWidth", String.valueOf(dpWidth));
        Log.d("dpWidth", String.valueOf(dpHeight));

    }

}
