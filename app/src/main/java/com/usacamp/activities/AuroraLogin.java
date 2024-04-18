package com.usacamp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class
AuroraLogin extends BaseActivity {
    String ActivityTitle = "";
    EditText edt_sms_number, edt_code, edt_pass_number, edt_pass;
    ImageView img_sms_number_remove, img_pass_number_remove;
    TextView txt_verifycode;
    Handler mHandler;
    Button btn_login_sms, btn_login_pass;
    LinearLayout ly_other_login;
    private int mnTime = 60;
    private Dialog mdlgProgress = null;
    String TAG = "PhoneActivityTAG";
    private static final int MSG = 1;
    private String mstrVerifyCode = "";
    private TextView txtOtherLogin;
    private ImageView imgOtherLogin;
    private boolean isOtherLogin = false;
    private ViewFlipper viewFlipper;
    private LinearLayout sms_ly, pass_ly;
    private CheckBox privacyRD ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.orora_login);
        setToolbarProperties();
        txtOtherLogin = findViewById(R.id.txt_other_login);
        imgOtherLogin = findViewById(R.id.img_other_login);
        edt_sms_number = findViewById(R.id.et_aurora_number);
        edt_code = findViewById(R.id.et_aurora_code);
        img_sms_number_remove = findViewById(R.id.img_aurora_edit_remove);
        txt_verifycode = findViewById(R.id.txt_verifycode);
        edt_pass_number = findViewById(R.id.et_aurora_pass_phonenumber);
        edt_pass = findViewById(R.id.et_aurora_password);
        img_pass_number_remove = findViewById(R.id.img_aurora_pass_edit_remove);
        btn_login_sms = findViewById(R.id.btn_aurora_login);
        btn_login_pass = findViewById(R.id.btn_aurora_login_pass);
        sms_ly = findViewById(R.id.orora_login_sms);
        pass_ly = findViewById(R.id.orora_login_pass);
        ly_other_login = findViewById(R.id.ly_other_login);
        privacyRD = findViewById(R.id.privacyRd);
        //sms//
        img_sms_number_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_sms_number.setText("");
                img_sms_number_remove.setVisibility(View.GONE);
            }
        });

        edt_sms_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_sms_number.getText().toString().length() > 0)
                    img_sms_number_remove.setVisibility(View.VISIBLE);
                else
                    img_sms_number_remove.setVisibility(View.GONE);

                if (VailidPhoneNumber()) {
                    txt_verifycode.setEnabled(true);
                    txt_verifycode.setTextColor(Color.parseColor("#1D9F39"));
                } else {
                    txt_verifycode.setEnabled(false);
                    txt_verifycode.setTextColor(Color.parseColor("#999999"));
                }

            }
        });
        edt_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_code.getText().length() == 6) {
                    btn_login_sms.setBackground(getResources().getDrawable(R.drawable.green_blue_radius));
                    btn_login_sms.setEnabled(true);
                } else {
                    btn_login_sms.setBackground(getResources().getDrawable(R.drawable.gray_radius_aurora));
                    btn_login_sms.setEnabled(false);
                }
            }
        });
        txt_verifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_verifycode.setTextColor(Color.parseColor("#999999"));
                if (mnTime == 60)
                    getVerifyCode();
            }
        });
        //pass//
        img_pass_number_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_pass_number.setText("");
                img_pass_number_remove.setVisibility(View.GONE);
            }
        });

        edt_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_pass_number.getText().toString().length() > 0)
                    img_pass_number_remove.setVisibility(View.VISIBLE);
                else
                    img_pass_number_remove.setVisibility(View.GONE);

                if (ValidInfomation()) {
                    btn_login_pass.setEnabled(true);
                    btn_login_pass.setBackground(getResources().getDrawable(R.drawable.green_blue_radius));
                } else {
                    btn_login_pass.setEnabled(false);
                    btn_login_pass.setBackground(getResources().getDrawable(R.drawable.gray_radius_aurora));
                }

            }
        });
        edt_pass_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edt_pass_number.getText().toString().length() > 0)
                    img_pass_number_remove.setVisibility(View.VISIBLE);
                else
                    img_pass_number_remove.setVisibility(View.GONE);

                if (ValidInfomation()) {
                    btn_login_pass.setEnabled(true);
                    btn_login_pass.setBackground(getResources().getDrawable(R.drawable.green_blue_radius));
                } else {
                    btn_login_pass.setEnabled(false);
                    btn_login_pass.setBackground(getResources().getDrawable(R.drawable.gray_radius_aurora));
                }

            }
        });

    }
    private void setToolbarProperties() {
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setTitle(ActivityTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    public void onBack(View view) {
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isOtherLogin = false;
        ((MyApplication) getApplicationContext()).setCurrentActivity(this);

    }

    private boolean VailidPhoneNumber() {
        return edt_sms_number.getText().toString().length() == 11 ;
    }

    public void finishSMS(int nErrorCode, String strVerifyCode) {

        if (mdlgProgress != null) {
            mdlgProgress.dismiss();

            switch (nErrorCode) {
                case 0:
                    mstrVerifyCode = new String(Base64.decode(strVerifyCode.getBytes(), Base64.DEFAULT));
//                    edt_code.setText(mstrVerifyCode);
                    mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            txt_verifycode.setTextColor(Color.parseColor("#999999"));
                            mnTime--;
                            txt_verifycode.setText(mnTime + "s后重新获取");

                            if (mnTime == 0) {
                                txt_verifycode.setText("获取验证码");
                                txt_verifycode.setTextColor(Color.parseColor("#1D9F39"));
                                mnTime = 60;
                            } else
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

    public void getVerifyCode() {

        edt_code.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edt_code, InputMethodManager.SHOW_IMPLICIT);
        mdlgProgress = MessageAlert.showProgressDialog(this, "加载中");

        String strPhone = edt_sms_number.getText().toString();

        MyApplication.mNetProc.sendSms(Constants.SMS_TYPE_LOGIN, "sendSms", "phone=" + strPhone + "&type=2");

        //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Login_VerifyCode);
    }

    public void DoSMSLogin(View v) {
        if(privacyRD.isChecked()) {
            MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber = edt_sms_number.getText().toString();
            if (mstrVerifyCode.equals(edt_code.getText().toString())) {

                if (MyApplication.mNetProc.mLoginUserInf.mbWechatAuroraSind)
                    MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_WECHATBIND, "loginWithPhone", "phone=" + edt_sms_number.getText().toString() +
                            "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_SMS, edt_sms_number.getText().toString());
                else
                    MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_LOGIN, "loginWithPhone", "phone=" + edt_sms_number.getText().toString() +
                            "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_SMS, edt_sms_number.getText().toString());

            } else {
                MessageAlert.showMessage(this, false, "验证码输入错误");
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Fail_Reason, Constants.Zhuge_Value_CallBack_Login_Fail_Sms);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_CallBack_Login_Fail, eventObject);
            }
        } else {
            MessageAlert.showMessage(this, false, "请先勾选页面的“同意《用户服务协议》《隐私政策》");
        }
    }

    public void DoPassLogin(View v) {
        if(privacyRD.isChecked()) {
            MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber = edt_pass_number.getText().toString();
            // MyApplication.mNetProc.mfDefaultLogin = false;
            if (MyApplication.mNetProc.mLoginUserInf.mbWechatAuroraSind)
                MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_WECHATBIND, "loginWithPhone", "phone=" + MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber +
                        "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=1" + "&password=" + edt_pass.getText().toString(), Constants.SINGIN_TYPE_PASS, MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
            else
                MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_LOGIN, "loginWithPhone", "phone=" + MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber +
                        "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=1" + "&password=" + edt_pass.getText().toString(), Constants.SINGIN_TYPE_PASS, MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
        } else {
            MessageAlert.showMessage(this, false, "请先勾选页面的“同意《用户服务协议》《隐私政策》");
        }
    }

    public void ShowSMSDlg(View v) {
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

    public void DoPassWordLoginView(View v) {
        sms_ly.clearAnimation();
        pass_ly.clearAnimation();
        TranslateAnimation animate_smsly = new TranslateAnimation(
                0,
                -sms_ly.getWidth(),
                0,
                0);
        animate_smsly.setDuration(300);
        //animate_smsly.setFillAfter(true);
        animate_smsly.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sms_ly.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        sms_ly.startAnimation(animate_smsly);

        TranslateAnimation animate_passly = new TranslateAnimation(
                pass_ly.getWidth(),
                0,
                0,
                0);
        animate_passly.setDuration(300);
        //animate_passly.setFillAfter(true);
        animate_passly.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                pass_ly.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pass_ly.startAnimation(animate_passly);

    }

    public void DOSmsLoginView(View v) {
        sms_ly.clearAnimation();
        pass_ly.clearAnimation();
        TranslateAnimation animate_passly = new TranslateAnimation(
                0,
                -pass_ly.getWidth(),
                0,
                0);
        animate_passly.setDuration(300);
        //animate_passly.setFillAfter(true);
        animate_passly.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pass_ly.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pass_ly.startAnimation(animate_passly);

        TranslateAnimation animate_smsly = new TranslateAnimation(
                sms_ly.getWidth(),
                0,
                0,
                0);
        animate_smsly.setDuration(300);
        //animate_smsly.setFillAfter(true);
        animate_smsly.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sms_ly.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        sms_ly.startAnimation(animate_smsly);

    }

    public void DoAutoSendSms() {
        edt_sms_number.setText(MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
        getVerifyCode();
    }

    public void gotoprivacy(View v) {
        Intent privacyInt = new Intent(this, PrivacyAct.class);
        startActivity(privacyInt);
    }

    public void gotoagreement(View v) {
        Intent agreementInt = new Intent(this, AgreementAct.class);
        startActivity(agreementInt);
    }

    public void DoOtherLogin(View v) {
        if(privacyRD.isChecked()) {
            if (!isOtherLogin) {
                txtOtherLogin.setVisibility(View.GONE);
                imgOtherLogin.setVisibility(View.VISIBLE);
            } else {
                onWechat();
            }
            isOtherLogin = true;
        } else {
            MessageAlert.showMessage(this, false, "请先勾选页面的“同意《用户服务协议》《隐私政策》");
        }
    }

    public void onWechat() {
        boolean isAppInstalled = appInstalledOrNot("com.tencent.mm");
        if (isAppInstalled) {
            Intent intent = new Intent(this, WeChatLoginActivity.class);
            startActivityForResult(intent, Constants.WE_CHAT_LOGIN);
            Intent wechatLoginActivity = new Intent(this, WeChatLoginActivity.class);
            startActivity(wechatLoginActivity);
        } else {
            MessageAlert.showMessage(this, "你在这部电话机上没有安装任何工具。 请安装并重试。");
        }
    }

    public void DOForgotPass(View v) {
        Intent resetAct = new Intent(this, PasswordReset.class);
        startActivity(resetAct);
    }

    private boolean appInstalledOrNot(String uri) {
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

    wechataccesstoken mWechatToken;

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

    private boolean ValidInfomation() {
        if (edt_pass_number.getText().toString().length() != 11)//phone number length
            return false;
        return edt_pass.getText().toString().length() >= 8 && edt_pass.getText().toString().length() <= 16;
    }

    public void DoLoginForceSmsMethod() {
        ((TextView)findViewById(R.id.txt_sms_login_view)).callOnClick();
        edt_sms_number.setText(MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
        DoAutoSendSms();
    }
    public void onTogglePassword1(View view)
    {
        if(((ToggleButton) findViewById(R.id.tg_aurora_password)).isChecked())
            edt_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            edt_pass.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void DoHideWechatLogin()
    {
        ly_other_login.setVisibility(View.INVISIBLE);
    }
}

