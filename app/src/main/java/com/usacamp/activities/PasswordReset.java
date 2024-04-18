package com.usacamp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MessageAlert;

public class PasswordReset extends BaseActivity {
    EditText edt_reset_phone, edt_reset_verify;
    private Dialog mdlgProgress = null;
    private String mstrVerifyCode = "";
    private ImageView img_number_remove;
    private TextView txt_verifycode;
    private Button btn_pass_reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        edt_reset_phone = (EditText)findViewById(R.id.edit_pass_reset_phonenumber);
        edt_reset_verify = (EditText)findViewById(R.id.edit_pass_reset_verify);
        img_number_remove = (ImageView)findViewById(R.id.img_aurora_edit_remove) ;
        txt_verifycode = (TextView)findViewById(R.id.text_pass_reset_verify);
        btn_pass_reset = (Button)findViewById(R.id.btn_pass_reset);
        ((TextView) findViewById(R.id.text_pass_reset_verify)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mnTime == 60)
                    getVerifyCode();
            }
        });
        img_number_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_reset_phone.setText("");
                img_number_remove.setVisibility(View.GONE);
            }
        });

        edt_reset_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt_reset_phone.getText().toString().length() > 0)
                    img_number_remove.setVisibility(View.VISIBLE);
                else
                    img_number_remove.setVisibility(View.GONE);

                if(VailidPhoneNumber()) {
                    txt_verifycode.setEnabled(true);
                    txt_verifycode.setTextColor(Color.parseColor("#1D9F39"));
                } else {
                    txt_verifycode.setEnabled(false);
                    txt_verifycode.setTextColor(Color.parseColor("#999999"));
                }

            }
        });
        edt_reset_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt_reset_verify.getText().length() == 6) {
                    btn_pass_reset.setBackground(getResources().getDrawable(R.drawable.green_blue_radius));
                    btn_pass_reset.setEnabled(true);
                } else {
                    btn_pass_reset.setBackground(getResources().getDrawable(R.drawable.gray_radius_aurora));
                    btn_pass_reset.setEnabled(false);
                }
            }
        });
        txt_verifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_verifycode.setTextColor(Color.parseColor("#999999"));
                if(mnTime == 60)
                    getVerifyCode();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    private static final int MSG = 1;
    private int mnTime = 60;
    Handler mHandler;

    public void getVerifyCode(){
        edt_reset_verify.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edt_reset_verify, InputMethodManager.SHOW_IMPLICIT);
        mdlgProgress = MessageAlert.showProgressDialog(this, "加载中");

        String strPhone = ((TextView)findViewById(R.id.edit_pass_reset_phonenumber)).getText().toString();
        MyApplication.mNetProc.sendSms(Constants.SMS_TYPE_PASSWORD_RESET,"sendSms", "phone=" + strPhone + "&type=2");
    }
    public void onPasswordReset(View view) {
        String strPhone = edt_reset_phone.getText().toString();
        String strVeriCode = edt_reset_verify.getText().toString();

        if (strPhone.equals("")) {
            MessageAlert.showMessage(this, "请输入正确的\n手机号");
            return;
        }

        if (mstrVerifyCode.equals(strVeriCode) && mstrVerifyCode != "") {
           // MyApplication.mNetProc.mfDefaultLogin = false;

            MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber = strPhone;
            MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_LOGIN,"loginWithPhone", "phone=" + strPhone +
                    "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_FORGOTPASS, strPhone);

        } else {
            MessageAlert.showMessage(this, false, "验证码输入错误");
        }
    }
    public void finishSMS(int nErrorCode, String strVerifyCode){
        if (mdlgProgress!=null){
            mdlgProgress.dismiss();

            switch (nErrorCode){
                case 0:
                    mstrVerifyCode = new String(Base64.decode(strVerifyCode.getBytes(), Base64.DEFAULT));
//                    edt_reset_verify.setText(mstrVerifyCode);
                    mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            mnTime--;
                            txt_verifycode.setText(mnTime + "s后重新获取");

                            if (mnTime == 0){
                                txt_verifycode.setText("获取验证码");
                                txt_verifycode.setTextColor(Color.parseColor("#1D9F39"));
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
            }
        }
    }
    public void onBack(View view) {
        finish();
    }
    private boolean VailidPhoneNumber()
    {
        return edt_reset_phone.getText().toString().length() == 11;
    }
    public void ShowSMSDlg(View v)
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View smsDlgView = factory.inflate(R.layout.aurora_sms_dlialog, null);
        final AlertDialog smsDialog = new AlertDialog.Builder(this).create();
        smsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        smsDialog.setView(smsDlgView, 70, 0, 70, 0);

        smsDlgView.findViewById(R.id.txt_aurora_sms_dlg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsDialog.dismiss();
            }
        });

        smsDialog.show();
    }

    public void GotoChangePassword()
    {
        Intent changeAct = new Intent(this,PasswordChange.class);
        startActivity(changeAct);
    }

}
