package com.usacamp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MessageAlert;

public class BindActivity extends BaseActivity {

    private Button mBindBtn;
    public EditText mBind_PhoneNumberEdt_sms, mBind_PhoneNumberEdt_Pass;
    public EditText mBind_VerifyNumberEdt,mBind_PasswordEdt;
    private Handler mHandler;
    private Dialog mdlgProgress;
    private int mnTime = 60;

    private String mstrOpenId = "";
    private String mstrVerifyCode = "";
    public RadioButton rd_pass, rd_sms;
    private boolean bLoginFlag = false ; // false - password Login & Signup , True - sms Login & Signup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);

        mstrOpenId = getIntent().getStringExtra("openid");
        MyApplication.mNetProc.mLoginUserInf.mstrOpenID = mstrOpenId;
        mBind_PhoneNumberEdt_sms = (EditText)findViewById(R.id.edit_bind_phonenumber_sms);
        mBind_PhoneNumberEdt_Pass = (EditText)findViewById(R.id.edit_bind_phonenumber_pass);
        mBind_VerifyNumberEdt = (EditText)findViewById(R.id.edit_bind_verify);
        mBind_PasswordEdt = (EditText)findViewById(R.id.edit_bind_pass);

        rd_pass = (RadioButton) findViewById(R.id.btn_bind_pass);
        rd_sms = (RadioButton) findViewById(R.id.btn_bind_sms);
        //initial phonenumber of Edit

        mBindBtn = (Button)findViewById(R.id.btn_bind);
        mBindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBind(view);
            }
        });

        ((LinearLayout)findViewById(R.id.lobtn_bind_verify)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mBind_PhoneNumberEdt_sms.getText().toString();
                if (str.equals(""))
                    MessageAlert.showMessage(BindActivity.this, "请输入正确的\n手机号");
                else if (mnTime == 60)
                    getVerifyCode();
            }
        });
    }
    public void onToggleBind(View view) {

        ((RadioGroup)view.getParent()).check(view.getId());
        if(rd_pass.isChecked()) {
            ((LinearLayout) findViewById(R.id.bind_pass_group)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.bind_sms_group)).setVisibility(View.GONE);
            bLoginFlag = false;
        } else if(rd_sms.isChecked()) {
            bLoginFlag = true;
            ((LinearLayout) findViewById(R.id.bind_sms_group)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.bind_pass_group)).setVisibility(View.GONE);
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

    public void onSignup(View view)
    {
        Intent signupInt = new Intent(this, SignupActivity.class);
        startActivity(signupInt);
    }
    private static final int BINDMSG = 2;
    public void getVerifyCode(){
        mdlgProgress = MessageAlert.showProgressDialog(this, "加载中");

        String strPhone = mBind_PhoneNumberEdt_sms.getText().toString();
        MyApplication.mNetProc.sendSms(Constants.SMS_TYPE_WECHAT_BIND, "sendSms", "phone=" + strPhone + "&type=0");

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                mnTime--;
                TextView textView = (TextView)findViewById(R.id.text_bind_get_verify_num);
                textView.setText("重新获取"+ mnTime);

                if (mnTime == 0){
                    textView.setText("获取验证码");
                    mnTime = 60;
                }else
                    sendMessageDelayed(obtainMessage(BINDMSG), 1000);
            }
        };

        mHandler.sendMessage(mHandler.obtainMessage(BINDMSG));
    }
    public void onAddChildBind(View view)
    {
        Intent childInt = new Intent(this, BindActivity.class);
        startActivity(childInt);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    public void finishSMS(int nErrorCode, String strVerifyCode){
        if (mdlgProgress!=null){
            mdlgProgress.dismiss();

            switch (nErrorCode){
                case 0:
                    mstrVerifyCode = new String(Base64.decode(strVerifyCode.getBytes(), Base64.DEFAULT));
                    //mBind_VerifyNumberEdt.setText(mstrVerifyCode);

                    MessageAlert.showMessage(this, true, "验证码已发");
                    break;
                case 1001:
                case 1002:
                    MessageAlert.showMessage(this, "请输入正确的\n手机号");
                    break;
            }
        }
    }

    public void onBind(View view) {

        if(bLoginFlag) // Sms Login
            bindSMS();
        else
            bindPass();

    }

    public void bindSMS ()
    {
        String strVeriCode = mBind_VerifyNumberEdt.getText().toString();
        String strPhone = mBind_PhoneNumberEdt_sms.getText().toString();

        if(strPhone.equals("")){
            MessageAlert.showMessage(this, "请输入正确的\n手机号");
            return;
        }
        if (mstrVerifyCode.equals(strVeriCode) && mstrVerifyCode != "")
        {
           // MyApplication.mNetProc.mfDefaultLogin = false;
            MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_WECHATBIND,"loginWithPhone", "phone=" +  strPhone +
                    "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME +"&type=0", Constants.SINGIN_TYPE_SMS, strPhone);

        }else
            MessageAlert.showMessage(this, false, "验证号错误");
    }

    public void bindPass()
    {
        String strPass = mBind_PasswordEdt.getText().toString();
        String strPhone = mBind_PhoneNumberEdt_Pass.getText().toString();
        if(strPhone.equals("")){
            MessageAlert.showMessage(this, "请输入正确的\n手机号");
            return;
        }

        if (!strPass.equals(""))
        {
          //  MyApplication.mNetProc.mfDefaultLogin = false;
            MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_WECHATBIND,"loginWithPhone", "phone=" +  strPhone +
                    "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=1" + "&password=" + strPass , Constants.SINGIN_TYPE_PASS, strPhone);

        } else
            MessageAlert.showMessage(this, "请输入正确的\n密码");
    }

    public void onBack(View view) {
        finish();
    }
    public void onGetVerifyCode(View view){
        if (mnTime == 60)
            getVerifyCode();
    }
    public void onTogglePassword(View view)
    {
        if(((ToggleButton) findViewById(R.id.tg_bind_pass)).isChecked())
            mBind_PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            mBind_PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void onGoToChangePWD(View view)
    {
        Intent resetAct = new Intent(this, PasswordReset.class);
        startActivity(resetAct);
    }

//    public void bindOk() {
//
//        SharedPreferences prefSavedLoginInf = getSharedPreferences("LoginInf", 0);
//        SharedPreferences.Editor edit = prefSavedLoginInf.edit();
//        edit.putString("userPhonenumber", MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
//        edit.putString("hardTypeId", "2");
//        edit.putString("hardware", MyApplication.mNetProc.mLoginUserInf.mstrDeviceID);
//        if (((CheckBox) findViewById(R.id.chk_bind_pass_remember)).isChecked()){
//            edit.putString("userPassword", mBind_PasswordEdt.getText().toString());
//        }
//        edit.commit();
//
//        finish();
//        Intent mainInt = new Intent(this, MainActivity.class);
//        startActivity(mainInt);
//
//        MainActivity.mainActivityInstance.goFragmentHome();
//    }

}
