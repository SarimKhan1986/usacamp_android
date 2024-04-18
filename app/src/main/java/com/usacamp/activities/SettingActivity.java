package com.usacamp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.net.wechataccesstoken;
import com.usacamp.utils.MessageAlert;
import com.usacamp.utils.Util;
import com.usacamp.wxapi.WeChatLoginActivity;


import java.io.File;
import java.util.List;

public class SettingActivity extends BaseActivity {
    Button wechatBtn;
    final String TAG = "VersionUp";
    boolean isUpdate = false;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        LinearLayout llserviceagreement = (LinearLayout)findViewById(R.id.llayout_serviceagreement);
        LinearLayout llser = (LinearLayout)findViewById(R.id.llayout_ser);
        LinearLayout llaboutus = (LinearLayout)findViewById(R.id.llayout_aboutus);
        LinearLayout lldeleteuser = (LinearLayout)findViewById(R.id.llayout_deleteuser);
        LinearLayout llresetpass = (LinearLayout)findViewById(R.id.llayout_resetpass);
        LinearLayout llspattern = (LinearLayout)findViewById(R.id.llayout_pattern);
//        CheckBox offlineModeCheckBox = findViewById(R.id.offlineModeCheckBox);
        TextView stxtpattern = (TextView)findViewById(R.id.stxtpattern);
//        Button RemoveOfflineContentBtn = findViewById(R.id.offlineContentRemove);
//        offlineModeCheckBox.setChecked(MyApplication.getInstance().isOfflineMode);
        if(State.getCurrentPattern() == State.pattern.student.getValue())
            stxtpattern.setText(" 当前模式为学生模式");
        else
            stxtpattern.setText(" 当前模式为家长模式");

        //LinearLayout llversionup = (LinearLayout)findViewById(R.id.versionLayout);
        wechatBtn = (Button)findViewById(R.id.setting_wechat_bind);

//        if(MyApplication.getInstance().compareVersionNames(BuildConfig.VERSION_NAME, MyApplication.mNetProc.mLoginUserInf.versionInfo.versionCode) == -1)
//        {
//            isUpdate = true;
//            findViewById(R.id.versionNotice).setVisibility(View.VISIBLE);
//        } else {
//            isUpdate = false;
//            findViewById(R.id.versionNotice).setVisibility(View.INVISIBLE);
//        }

        resetWechatBindText();
        llspattern.setOnClickListener(v -> {
            Intent switchInt = new Intent(this, SwitchPatternActivity.class);
            switchInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(switchInt);
        });
//        RemoveOfflineContentBtn.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String path = getFilesDir() + "/content";
//                File f = new File(path);
//                deleteDirectory(f);
//
//                MyApplication.getInstance().removeLevellistInOfflineMode();
//            }
//        });

        wechatBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.mNetProc.mLoginUserInf.mbWechatBind == 0) {
                    boolean isAppInstalled = appInstalledOrNot("com.tencent.mm");
//                    isAppInstalled = true;

                    if(isAppInstalled) {
                        Intent intent = new Intent(SettingActivity.this, WeChatLoginActivity.class);
                        startActivityForResult(intent, Constants.WE_CHAT_LOGIN);
                        Intent wechatLoginActivity = new Intent(SettingActivity.this, WeChatLoginActivity.class);
                        startActivity(wechatLoginActivity);
                    } else {
                        MessageAlert.showMessage(SettingActivity.this ,"你在这部电话机上没有安装任何工具。 请安装并重试。");
                    }
                } else {
                    LayoutInflater factory = LayoutInflater.from(SettingActivity.this);
                    final View deleteDialogView = factory.inflate(R.layout.custom_wechat_bind, null);
                    final AlertDialog deleteDialog = new AlertDialog.Builder(SettingActivity.this).create();
                    deleteDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    deleteDialog.setView(deleteDialogView, 70, 0, 70, 0);

                    deleteDialogView.findViewById(R.id.dlg_confirmbtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyApplication.mNetProc.resetWechatBind( "resetWechatBind", "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id);
                            deleteDialog.dismiss();
                        }
                    });
                    deleteDialogView.findViewById(R.id.dlg_cancelbtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog.dismiss();
                        }
                    });

                    deleteDialog.show();
                }
            }
        });
        llserviceagreement.setOnClickListener(new LinearLayout.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this, ServiceAgreementActivity.class);
                startActivity(intent);
            }
        });
        llaboutus.setOnClickListener(new LinearLayout.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
//        llversionup.setOnClickListener(new LinearLayout.OnClickListener(){
//            public void onClick(View v){
//                if(isUpdate)
//                    new Util.DownloadNewVersion().execute();
//            }
//        });
        llser.setOnClickListener(new LinearLayout.OnClickListener(){
            public void onClick(View v){
                Intent privacyInt = new Intent(SettingActivity.this, PrivacyAct.class);
                startActivity(privacyInt);
            }
        });
        lldeleteuser.setOnClickListener(new LinearLayout.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent deleteuserInt = new Intent(SettingActivity.this, DeleteUser.class);
                startActivity(deleteuserInt);
            }
        });
        llresetpass.setOnClickListener(new LinearLayout.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(MyApplication.mNetProc.mLoginUserInf.mbPassword == 1){
                    Intent passresetAct = new Intent(SettingActivity.this, PasswordReset.class);
                    startActivity(passresetAct);
                } else {
                    Intent passchangeAct = new Intent(SettingActivity.this, PasswordChange.class);
                    startActivity(passchangeAct);
                }
            }
        });
//        offlineModeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                MyApplication.getInstance().isOfflineMode = b;
//                MyApplication.getInstance().saveOfflineMode(b);
//            }
//        });
    }
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
    public void onBack(View view) {
        finish();
    }
    public void onLogout(View view){
        finish();
        MyApplication.getInstance().logOut();
        //Zhuge
        ////ZhugeSDK.getInstance().track(this, Constants.Zhuge_Event_Click_Profile_Setting_LogOut);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    public void resetWechatBindText()
    {
        if(MyApplication.mNetProc.mLoginUserInf.mbWechatBind == 0) {
            wechatBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton24));
            wechatBtn.setTextColor(Color.parseColor("#4CAF50"));
            wechatBtn.setText(Constants.Wechat_UnBinded_Text);
        } else {
            wechatBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbutton21));
            wechatBtn.setTextColor(Color.parseColor("#959494"));
            wechatBtn.setText(Constants.Wechat_Binded_Text);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Constants.WE_CHAT_LOGIN) {
            final String code = data.getStringExtra(Constants.WE_CHAT_AUTH_CODE);
            wechataccesstoken wechatToken = new wechataccesstoken(this, 1);
            wechatToken.wechataccesstoken(code);
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

    private void VersionUpdate()
    {
        //versionLayout.setVisibility(View.VISIBLE);
//        LayoutInflater factory = LayoutInflater.from(this);
//        VersionDialogView = factory.inflate(R.layout.versionupgradeprogressdlg, null);
//        versionDialog = new AlertDialog.Builder(this).create();
//        versionDialog.setCanceledOnTouchOutside(false);
//        versionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        versionDialog.setView(VersionDialogView, 0, 0, 0, 0);
//        versionDialog.show();
        new Util.DownloadNewVersion().execute();
    }

}
