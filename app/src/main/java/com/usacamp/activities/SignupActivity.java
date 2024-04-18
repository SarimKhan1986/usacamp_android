package com.usacamp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MessageAlert;
//

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends BaseActivity {

    EditText edt_phonenumber, edt_verifyCode, edt_password, edt_passwordAgain;
    private Handler mHandler;
    private int mnTime = 60;
    private static final int MSG = 1;
    private static final int BINDMSG = 2;
    private String mstrVerifyCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Zhuge
        //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Enter_Signup_Page);

        edt_phonenumber = (EditText) findViewById(R.id.edit_signup_phone_number);
        edt_verifyCode = (EditText) findViewById(R.id.edit_signup_verifycode);
        edt_password = (EditText) findViewById(R.id.edit_signup_pass);
        edt_passwordAgain = (EditText) findViewById(R.id.edit_signup_pass_again);
    }
    public void getVerifyCode(){
        edt_verifyCode.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edt_verifyCode, InputMethodManager.SHOW_IMPLICIT);

        String strPhone = edt_phonenumber.getText().toString();
        MyApplication.mNetProc.sendSms(Constants.SMS_TYPE_SINGUP,"sendSms", "phone=" + strPhone + "&type=1");

        //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Signup_VerifyCode);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    public void onGetSignUpVerifyCode(View view){
        if (mnTime == 60)
            getVerifyCode();
    }

    public void onSignUpPerform (View view)
    {
        if(edt_phonenumber.getText().toString().equals("")) {
            MessageAlert.showMessage(this, "请输入正确的\n手机号");
        }
        //verify security code
        if(mstrVerifyCode == "") {
            MessageAlert.showMessage(this, false, "验证号错误");
            return;
        }

        if (!mstrVerifyCode.equals(String.valueOf(edt_verifyCode.getText()))) {
            MessageAlert.showMessage(this, false, "验证号错误");
            //Zhuge
//            JSONObject eventObject = new JSONObject();
//            try {
//                eventObject.put(Constants.Zhuge_Property_Fail_Reason, Constants.Zhuge_Value_CallBack_Signup_Fail_Sms);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_CallBack_Signup_Fail, eventObject);
            return;
        }
        if(edt_password.getText().toString().equals("")) {
            MessageAlert.showMessage(this, "您的密码格式不正确，请输入8-16位密码");
            return;
        }
        if(edt_password.getText().toString().length() > 16 || edt_password.getText().toString().length() < 8) {
            MessageAlert.showMessage(this, "您的密码格式不正确，请输入8-16位密码");
            return;
        }
        if (!edt_password.getText().toString().equals(edt_passwordAgain.getText().toString()))
        {
            MessageAlert.showMessage(this, "密码输入不一致，请确认");
            return;
        }
        //MyApplication.mNetProc.mfDefaultLogin = false;

        MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_SIGNUP, "loginWithPhone", "phone=" +  edt_phonenumber.getText().toString() +
                    "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=3" + "&password=" + edt_password.getText().toString(), Constants.SINGIN_TYPE_PASS, edt_phonenumber.getText().toString());//signup

    }
//    public void signupOK()
//    {
//        Intent addChildAct = new Intent(this, AddChildActivity.class);
//        startActivity(addChildAct);
//        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_CallBack_Signup_Success);
//
//        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Signup_NextButton);
//    }
    public void finishSMS(int nErrorCode, String strVerifyCode){
        switch (nErrorCode){
            case 0:
                mstrVerifyCode = new String(Base64.decode(strVerifyCode.getBytes(), Base64.DEFAULT));
                //edt_verifyCode.setText(mstrVerifyCode);
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        mnTime--;
                        TextView textView = (TextView)findViewById(R.id.text_get_verifycode);
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
                MessageAlert.showMessage(this, "请输入正确的\n手机号");
                break;
            case 1003://already Logged in
                MessageAlert.showMessage(this, "该手机号已注册，请确认");
                break;
            case 1004:
                //MessageAlert.showMessage(this, "请输入正确的\n手机号");
                break;
        }
    }
    public void onBack(View view) {
        finish();
    }

    public void onTogglePassword(View view)
    {
        if(((ToggleButton) findViewById(R.id.tg_password)).isChecked())
            edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            edt_password.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void onTogglePasswordAgain(View view)
    {
        if(((ToggleButton) findViewById(R.id.tg_password_again)).isChecked())
            edt_passwordAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            edt_passwordAgain.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

}
