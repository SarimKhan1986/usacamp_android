package com.usacamp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.net.wechataccesstoken;
import com.usacamp.utils.MessageAlert;
import com.usacamp.wxapi.WeChatLoginActivity;
//

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
public class LoginActivity extends BaseActivity {

    public EditText mPhoneNumberEdt, mPhoneNumberEdt_Pass;
    public EditText mVerifyNumberEdt,mPasswordEdt;
    public static LoginActivity Instance = null;
    public ImageView mCrossMarker;
    public LinearLayout mlobtnVerify;
    public Button mLoginBtn;
    public RadioButton rd_pass, rd_sms;
    private Dialog mdlgProgress = null;

    private String mstrVerifyCode = "";
    private boolean bLoginFlag = false ; // false - password Login & Signup , True - sms Login & Signup
    private final boolean mfPhoneOk = false;
    private final boolean mfVerifyCodeOk = false;
    wechataccesstoken mWechatToken;

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Instance = this;

        //Zhuge
//        if(getIntent().getExtras() != null) {
//            JSONObject eventObject = new JSONObject();
//            try {
//                eventObject.put(Constants.Zhuge_Property_Entrance, getIntent().getStringExtra(Constants.Zhuge_Property_Entrance));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            ////ZhugeSDK.getInstance().track(getApplicationContext(), Constants.Zhuge_Event_Enter_Login_Page, eventObject);
//        }

        ((RadioGroup) findViewById(R.id.toggleGroup)).setOnCheckedChangeListener(ToggleListener);
        rd_pass = (RadioButton) findViewById(R.id.btn_pass);
        rd_sms = (RadioButton) findViewById(R.id.btn_sms);
        mPhoneNumberEdt = (EditText) findViewById(R.id.edit_login_phonenumber);
        mVerifyNumberEdt = (EditText)findViewById(R.id.edit_login_verify);
        mPhoneNumberEdt_Pass = (EditText)findViewById(R.id.edit_login_phonenumber_pass);
        mPasswordEdt = (EditText) findViewById(R.id.edit_login_verify_pass);
//        mCrossMarker = (ImageView) findViewById(R.id.img_login_cross);
        mlobtnVerify = (LinearLayout)findViewById(R.id.lobtn_login_verify);
        mLoginBtn = (Button)findViewById(R.id.btn_login);

        if(!MainActivity.mainActivityInstance.mstrSavedPassword.equals(""))
        {
            mPhoneNumberEdt_Pass.setText(MainActivity.mainActivityInstance.mstrSavedUserID);
            mPasswordEdt.setText(MainActivity.mainActivityInstance.mstrSavedPassword);
        }
        //initial phonenumber of Edit
//        mCrossMarker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mPhoneNumberEdt.setText("");
//            }
//        });

        mlobtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mPhoneNumberEdt.getText().toString();
                if (str.equals(""))
                    MessageAlert.showMessage(LoginActivity.this, "请输入正确的\n手机号");
                else if (mnTime == 60)
                    getVerifyCode();
            }
        });

//        mPhoneNumberEdt.addTextChangedListener(mPhoneWatcher);
//        mVerifyNumberEdt.addTextChangedListener(mVerifyTextWatcher);
    }
    public void onToggle(View view) {

        ((RadioGroup)view.getParent()).check(view.getId());
        if(rd_pass.isChecked()) {
            ((LinearLayout) findViewById(R.id.pass_group)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.sms_group)).setVisibility(View.GONE);
            bLoginFlag = false;
        } else if(rd_sms.isChecked()) {
            bLoginFlag = true;
            ((LinearLayout) findViewById(R.id.sms_group)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.pass_group)).setVisibility(View.GONE);
           // ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Choose_Login_VerifyCode_Method);
        }
        // app specific stuff ..
    }
    static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final RadioButton view = (RadioButton) radioGroup.getChildAt(j);

                view.setChecked(view.getId() == i);
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    public void onBack(View view) {
        finish();
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(mainIntent);
    }
    public void onLogin(View view)
    {
        if(bLoginFlag) // Sms Login
            SmsLogin();
        else
            PasswordLogin();
    }

    public void PasswordLogin()
    {
        String strPass = ((EditText)findViewById(R.id.edit_login_verify_pass)).getText().toString();
        String strPhone = ((EditText)findViewById(R.id.edit_login_phonenumber_pass)).getText().toString();
        if(strPhone.equals("")){
            MessageAlert.showMessage(this, "请输入正确的\n手机号");
            return;
        }
       // MyApplication.mNetProc.mfDefaultLogin = false;
        MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_LOGIN,"loginWithPhone", "phone=" +  strPhone +
                "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=1" + "&password=" + strPass, Constants.SINGIN_TYPE_PASS, strPhone);

        //Zhuge
        //////ZhugeSDK.getInstance().track(this,Constants.Zhuge_Event_Click_Login_Button);
    }

    public void SmsLogin() {
        String strVeriCode = ((EditText)findViewById(R.id.edit_login_verify)).getText().toString();
        String strPhone = ((EditText)findViewById(R.id.edit_login_phonenumber)).getText().toString();
//        if(strPhone.equals("18802206365") && strVeriCode.equals("000000")) {
//            MyApplication.mNetProc.mfDefaultLogin = false;
//            MyApplication.mNetProc.loginWithPhone(this, 1,"loginWithPhone", "phone=" + strPhone +
//                    "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=1.2" + "&type=0");
//            return ;
//        }
        if(strPhone.equals("")){
             MessageAlert.showMessage(this, "请输入正确的\n手机号");
             return;
        }

        if (mstrVerifyCode.equals(strVeriCode) && mstrVerifyCode != "")
        {
            //MyApplication.mNetProc.mfDefaultLogin = false;
            MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_LOGIN,"loginWithPhone", "phone=" +  strPhone +
                    "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_SMS, strPhone);

        }else {
            MessageAlert.showMessage(this, false, "验证号错误");
//            JSONObject eventObject = new JSONObject();
//            try {
//                eventObject.put(Constants.Zhuge_Property_Fail_Reason, Constants.Zhuge_Value_CallBack_Login_Fail_Sms);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_CallBack_Login_Fail,  eventObject);
        }
        //Zhuge
        //////ZhugeSDK.getInstance().track(this,Constants.Zhuge_Event_Click_Login_Button);
    }

    public void onSignup(View view)
    {
        Intent signupInt = new Intent(this, SignupActivity.class);
        startActivity(signupInt);

        //Zhuge
       // ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Signup_Button);
    }

    public void onWechat(View view) {
        boolean isAppInstalled = appInstalledOrNot("com.tencent.mm");
        if(isAppInstalled) {
            Intent intent = new Intent(this, WeChatLoginActivity.class);
            startActivityForResult(intent, Constants.WE_CHAT_LOGIN);
            Intent wechatLoginActivity = new Intent(LoginActivity.this, WeChatLoginActivity.class);
            startActivity(wechatLoginActivity);
        } else {
            MessageAlert.showMessage(this ,"你在这部电话机上没有安装任何工具。 请安装并重试。");
        }
    }

    private boolean appInstalledOrNot(String uri){
        PackageManager pm = getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_META_DATA);
        if (packageInfoList != null) {
            for (PackageInfo packageInfo : packageInfoList) {
                String packageName = packageInfo.packageName;

                if (packageName != null && packageName.equals(uri)) {
                    return true;
                }
            }
        }
        return false;
    }

//    TextWatcher mPhoneWatcher = new TextWatcher() {
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            // TODO Auto-generated method stub
//            mfPhoneOk = false;
//            if (s.toString().isEmpty()) {
//                mCrossMarker.setVisibility(View.GONE);
//            }else {
//                mCrossMarker.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public void afterTextChanged(Editable s){
//            /* TODO Auto-generated method stub */
//
//        }
//    };
//
//    TextWatcher mVerifyTextWatcher = new TextWatcher() {
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            // TODO Auto-generated method stub
//            mfVerifyCodeOk = false;
//
//            String strEdit = s.toString();
//
//            if (mstrVerifyCode.equals(strEdit) && mstrVerifyCode != "") {
//                mLoginBtn.setBackgroundResource(btn_login_char_fill);
//                mfVerifyCodeOk = true;
//            }
//            else if (strEdit.length() >= mstrVerifyCode.length()){
//
//            }
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public void afterTextChanged(Editable s){
//            /* TODO Auto-generated method stub */
//
//        }
//    };

    private static final int MSG = 1;
    private int mnTime = 60;
    Handler mHandler;

    public void getVerifyCode(){

        mVerifyNumberEdt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mVerifyNumberEdt, InputMethodManager.SHOW_IMPLICIT);
        mdlgProgress = MessageAlert.showProgressDialog(this, "加载中");

        String strPhone = ((TextView)findViewById(R.id.edit_login_phonenumber)).getText().toString();

        MyApplication.mNetProc.sendSms(Constants.SMS_TYPE_LOGIN,"sendSms", "phone=" + strPhone + "&type=0");

        //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Login_VerifyCode);
    }

    public void finishSMS(int nErrorCode, String strVerifyCode){

        if (mdlgProgress!=null){
            mdlgProgress.dismiss();

            switch (nErrorCode){
                case 0:
                    mstrVerifyCode = new String(Base64.decode(strVerifyCode.getBytes(), Base64.DEFAULT));
//                    mVerifyNumberEdt.setText(mstrVerifyCode);
                    mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mnTime--;
                            TextView textView = (TextView)findViewById(R.id.text_get_verify_num);
                            textView.setText(mnTime + "s后重新获取");

                            if (mnTime == 0){
                                textView.setText("获取验证码");
                                mnTime = 60;
                            }else
                                sendMessageDelayed(obtainMessage(MSG), 1000);
                        }
                    };

                    mHandler.sendMessage(mHandler.obtainMessage(MSG));

                    MessageAlert.showMessage(this, true, "验证码已发");
                    break;
                case 1001:
                case 1002:
                    //MessageAlert.showMessage(this, "请输入正确的\n手机号");
                    break;
            }
        }
    }

//    public void loginOk() {
//
//        SharedPreferences prefSavedLoginInf = getSharedPreferences("LoginInf", 0);
//        SharedPreferences.Editor edit = prefSavedLoginInf.edit();
//
//        edit.putString("userPhonenumber", MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
//        edit.putString("hardTypeId", "2");
//        edit.putString("hardware", MyApplication.mNetProc.mLoginUserInf.mstrDeviceID);
//        if (((CheckBox) findViewById(R.id.chk_pass_remember)).isChecked()){
//
//            edit.putString("userPassword", mPasswordEdt.getText().toString());
//
//        }
//        edit.commit();
//
//        finish();
//        MainActivity.mainActivityInstance.goFragmentHome();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("wechataccesstoken", "code1");
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Constants.WE_CHAT_LOGIN) {
            Log.d("wechataccesstoken", "code");
            final String code = data.getStringExtra(Constants.WE_CHAT_AUTH_CODE);
            mWechatToken = new wechataccesstoken(this, 0);
            mWechatToken.wechataccesstoken(code);
        }
    }

    public void gotoprivacy(View v)
    {
        Intent privacyInt = new Intent(this, PrivacyAct.class);
        startActivity(privacyInt);
    }

    public void gotoagreement(View v)
    {
        Intent agreementInt = new Intent(this, AgreementAct.class);
        startActivity(agreementInt);
    }
    public void onTogglePassword(View view)
    {
        if(((ToggleButton) findViewById(R.id.tg_login_pass)).isChecked())
            mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void onGoToChangePWD(View view)
    {
        Intent resetAct = new Intent(this, PasswordReset.class);
        startActivity(resetAct);
    }
}
