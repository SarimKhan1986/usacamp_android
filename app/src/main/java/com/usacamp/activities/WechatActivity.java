//package com.usacamp.activities;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.usacamp.R;
//import com.usacamp.constants.Constants;
//import com.usacamp.net.wechataccesstoken;
//import com.usacamp.wxapi.WeChatLoginActivity;
//
//public class WechatActivity extends Activity {
//
//    Button mWechatLoginBtn ;
//    String  mStrWeChatAuthCode;
//    String  mStrWeChatErrorCode;
//    wechataccesstoken mWechatToken;
//    Boolean mfReceiveed = false;
//    Dialog mdlgProgress = null;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wechat_login);
//
//        mWechatLoginBtn = (Button)findViewById(R.id.btn_wechat_login);
//        mWechatLoginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onWechatConfirm(view);
//            }
//        });
//    }
//
//    public void onWechatConfirm(View view) {
//
//        Intent intent = new Intent(this, WeChatLoginActivity.class);
//        startActivityForResult(intent, Constants.WE_CHAT_LOGIN);
//        //finish();
//    }
//
//    public void onBack(View view) {
//        finish();
//    }
//
//    public void finishWechatLogin(String strWechatOpenId){
//        // exit progress
//        if (mdlgProgress != null)
//            mdlgProgress.dismiss();
//
//        MyApplication.mNetProc.mLoginUserInf.mstrOpenID = strWechatOpenId;
//        MyApplication.mNetProc.loginWithWeixin(this,"loginWithWeixin", "openid=" +  strWechatOpenId +
//                "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//
//        if (requestCode == Constants.WE_CHAT_LOGIN) {
//            final String code = data.getStringExtra(Constants.WE_CHAT_AUTH_CODE);
//            mWechatToken = new wechataccesstoken(LoginActivity.class);
//            mWechatToken.wechataccesstoken(code);
//        }
//    }
//    public void loginOk(){
//
//        String strVeriCode = ((TextView)findViewById(R.id.edit_login_verify)).getText().toString();
//        String strPhone = ((TextView)findViewById(R.id.edit_login_phonenumber)).getText().toString();
//
//        SharedPreferences prefSavedLoginInf = getSharedPreferences("LoginInf", 0);
//        SharedPreferences.Editor edit = prefSavedLoginInf.edit();
//
//        edit.putString( "phone", strPhone );
//        edit.putString( "hardTypeId", "2" );
//        edit.putString( "hardware", MyApplication.mNetProc.mLoginUserInf.mstrDeviceID );
//        edit.commit();
//
//        Intent mainIntent = new Intent(this, MainActivity.class);
//        startActivity(mainIntent);
//    }
//
//}
