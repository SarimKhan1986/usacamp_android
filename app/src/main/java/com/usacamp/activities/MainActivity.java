package com.usacamp.activities;

//import static com.sdk.base.module.manager.SDKManager.getContext;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import cn.jiguang.api.utils.JCollectionAuth;
import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jiguang.verifysdk.api.RequestCallback;
import cn.jiguang.verifysdk.api.VerifyListener;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.iflytek.cloud.SpeechUtility;
import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.fragment.FragmentHome;
import com.usacamp.fragment.FragmentProfile;
import com.usacamp.fragment.FragmentStudy;
import com.usacamp.net.Aurora_RealNumber;
import com.usacamp.net.wechataccesstoken;
import com.usacamp.utils.MessageAlert;
import com.usacamp.utils.NonSwipeableViewPager;
import com.usacamp.utils.Util;
import com.usacamp.wxapi.WeChatLoginActivity;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {

    public static MainActivity mainActivityInstance = null;
    int pager_number = 3;
//    private BottomNavigationView navigation;
    private BottomNavigationView navigation;
    public NonSwipeableViewPager viewPager;
    public FragmentHome mfragHome = null;
    public FragmentStudy mfragStudy= null;
    public FragmentProfile mfragProfile= null;
    private Button centrebutton = null;
    String mstrSavedUserID;
    String mstrSavedHardTypeId;
    String mstrSavedHardware;
    wechataccesstoken mWechatToken;
    String mstrSavedPassword;
    private static final String LOGIN_CODE = "login_code";
    private static final String LOGIN_CONTENT = "login_content";
    private static final String LOGIN_OPERATOR = "login_operator";
    private final boolean autoFinish = true;
    private boolean isOtherLogin = false;
    final String TAG = "usacamp_mainAct";
    public MyAdapter myAdaper;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            JSONObject eventObject = new JSONObject();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MessageAlert.cancelMessage();
                    goFragmentHome();
                    //Zhuge

//                    try {
//                        eventObject.put(Constants.Zhuge_Property_Name, Constants.Zhuge_Value_Bottom_NavBar_Home);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ////ZhugeSDK.getInstance().track(MainActivity.this, Constants.Zhuge_Event_Click_Main_Bottom_NavBar, eventObject);
                    return true;
                case R.id.navigation_learn:
                    MessageAlert.cancelMessage();
                    if (!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                        loginAuth(false);
//                        JSONObject eventObject1 = new JSONObject();
//                        try {
//                            eventObject.put(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Enter_LoginPage_Learning);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        ////ZhugeSDK.getInstance().track(MainActivity.this, Constants.Zhuge_Event_Enter_Login_Page, eventObject);

                        //Zhuge
//                        try {
//                            eventObject.put(Constants.Zhuge_Property_Name, Constants.Zhuge_Value_Bottom_NavBar_Learn);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        ////ZhugeSDK.getInstance().track(MainActivity.this, Constants.Zhuge_Event_Click_Main_Bottom_NavBar, eventObject);
                        return false;
                    }else
                        goFragmentLearn();
                    return false;
                case R.id.navigation_profile:
                    MessageAlert.cancelMessage();
                    if (!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                        loginAuth(false);

//                        JSONObject eventObject1 = new JSONObject();
//                        try {
//                            eventObject1.put(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Enter_LoginPage_Profile);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        ////ZhugeSDK.getInstance().track(MainActivity.this, Constants.Zhuge_Event_Enter_Login_Page, eventObject);

                        return false;
                    }else
                        goFragmentProfile();

                    //Zhuge

//                    try {
//                        eventObject.put(Constants.Zhuge_Property_Name, Constants.Zhuge_Value_Bottom_NavBar_Profile);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ////ZhugeSDK.getInstance().track(MainActivity.this, Constants.Zhuge_Event_Click_Main_Bottom_NavBar, eventObject);
            }
            return false;
        }
    };

    private boolean getSavedLoginInf() {

        mstrSavedUserID = MyApplication.getInstance().getPhoneNumberFromPref();
        mstrSavedHardTypeId = MyApplication.getInstance().getHardWareTypeFromPref();
        mstrSavedHardware = MyApplication.getInstance().getHardWareSerialNumberFromPref();
        mstrSavedPassword = MyApplication.getInstance().getPasswordFromPref();

        if ( mstrSavedUserID == null || mstrSavedUserID.equals(""))
            return false;

        if ( mstrSavedHardTypeId == null || mstrSavedHardTypeId.equals(""))
            return false;

        return mstrSavedHardware != null && !mstrSavedHardware.equals("");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mainActivityInstance = this;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ) {
//                Log.v(TAG,"Permission is granted");
//            } else {
//                Log.v(TAG,"Permission is revoked");
//                ActivityCompat.requestPermissions(mainActivityInstance, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                ActivityCompat.requestPermissions(mainActivityInstance, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
//        }

        super.onCreate(savedInstanceState);

        //SpeechUtility.createUtility(this, "appid=" + Constants.SPEAKING_APPID);

        MyApplication.getInstance().sendErrorMsg();
        setContentView(R.layout.activity_main);
//        MyApplication.getInstance().getCurrentActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        if (MyApplication.mNetProc.mLoginUserInf.misNew == 1)
            MessageAlert.showFirstDlg(this);
        centrebutton = (Button) findViewById(R.id.centrebtn);

//Zhuge

//        ////ZhugeSDK.getInstance().openLog();
//        ////ZhugeSDK.getInstance().setLogLevel(Log.VERBOSE);
//
//        ZhugeParam param = new ZhugeParam.Builder().appKey(Constants.Zhuge_id)
//                .appChannel(Constants.Zhuge_channel)
//                .did(MyApplication.getInstance().getHardWareSerialNumberFromPref())
//                .build();
//
//        ////ZhugeSDK.getInstance(). initWithParam(this, param);

        viewPager = findViewById(R.id.viewpager);
        myAdaper = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myAdaper);
        viewPager.setOffscreenPageLimit(pager_number);
        navigation = findViewById(R.id.customBottomBar);
        ////////
//
//        int resourceId = getResources().getIdentifier("design_bottom_navigation_height", "dimen", this.getPackageName());
//        int height = 0;
//        if (resourceId > 0) {
//            height = getResources().getDimensionPixelSize(resourceId);
//        }
//        //height in pixels
//        // if you want the height in dp
//        float density = getResources().getDisplayMetrics().density;
//        float dp = height / density;
//
//        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) centrebutton.getLayoutParams();
//        params.height = height;
//        Toast.makeText(this, dp + "", Toast.LENGTH_SHORT).show();
//        params.setMargins(0,0,0, 0);
//        centrebutton.setLayoutParams(params);
//        centrebutton.requestLayout();
        ////////

        centrebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageAlert.cancelMessage();
                if (!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin)
                    loginAuth(false);
                else
                    goFragmentLearn();
            }
        });

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        goFragmentHome();

        versionUpdate();
    }
    public void showStudyButton()
    {
        Log.d("showStudyButton" ,"showStudyButton");
        ((Group)findViewById(R.id.tooltipgroup)).setVisibility(View.VISIBLE);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 3000ms
                MainActivity.mainActivityInstance.hideStudyButton();
            }
        }, 5000);
    }
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
    void hideStudyButton()
    {((Group)findViewById(R.id.tooltipgroup)).setVisibility(View.GONE);}
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    private boolean doubleBackToExitPressedOnce = false;
    private Handler backButtonPressHandler = new Handler(Looper.getMainLooper());
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            return;
        }
        doubleBackToExitPressedOnce = true;
        showExitMessage();
        // Reset the double tap flag after 3 seconds
        backButtonPressHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                },
                3000
        );
    }
    private void showExitMessage() {
        MessageAlert.showMessage(this, "在按一次退出游美英语");
    }

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mfragHome = new FragmentHome();
                case 1:
                    return mfragStudy = new FragmentStudy();
                case 2:
                    return mfragProfile = new FragmentProfile();
            }
            return null;
        }

        @Override
        public int getCount() {
            return pager_number;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

    }
    public void goFragmentHome(){
        viewPager.setCurrentItem(0);
        navigation.getMenu().getItem(0).setChecked(true);
        //centrebutton.setBackgroundResource(R.drawable.menu_play_diabled);

    }
    public void goFragmentProfile(){
        Log.d("loginframe", "goFragmentProfile");
        viewPager.setCurrentItem(2);
        navigation.getMenu().getItem(2).setChecked(true);
        //centrebutton.setBackgroundResource(R.drawable.menu_play_diabled);
    }
    public void goFragmentLearn(){
        viewPager.setCurrentItem(1);
        navigation.getMenu().getItem(1).setChecked(true);
        //centrebutton.setBackgroundResource(R.drawable.menu_play_enabled);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isOtherLogin =false;
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        int nFragmentId = intent.getIntExtra("fragment_id", 0);
        if (nFragmentId > 0)
            viewPager.setCurrentItem(nFragmentId - 1);
    }

     public void refreshCommonInf() throws IOException {

        mfragHome.refreshCommonInf();
    }

    public void refreshAllInformation() throws IOException {
        String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                "&purchase_level=0";

        MyApplication.mNetProc.getLevelList("getLevelList", strRequestParameter);

        String strRequestParameter1 = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id;
        MyApplication.mNetProc.getPurchaseLevels("getPurchasedLevelList", strRequestParameter1);

    }
    public void refreshLearnInformation(){
        try {
            mfragStudy.refreshAllInformation();
        } catch (NullPointerException e)
        {

        }

    }

    public void refreshProfileInformation() {mfragProfile.refreshAllInformation();}

    public void refreshProfileStudyDegree() {mfragProfile.refreshStudyDegree();}

    public void refreshMediaInfo() {try {mfragHome.setMediaInforamtion();} catch (NullPointerException e){}}

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void versionUpdate(){
        if(MyApplication.getInstance().compareVersionNames(BuildConfig.VERSION_NAME, MyApplication.mNetProc.mLoginUserInf.versionInfo.versionCode) == -1)
        {
            switch (MyApplication.mNetProc.mLoginUserInf.versionInfo.status) {
                case 0:
                    break;
                case 1:
                    LayoutInflater factory = LayoutInflater.from(this);
                    final View VersionDialogView = factory.inflate(R.layout.verionupgrade, null);
                    final AlertDialog versionDialog = new AlertDialog.Builder(this).create();
                    versionDialog.setCanceledOnTouchOutside(false);
                    versionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    versionDialog.setView(VersionDialogView, 80, 450, 80, 450);
                    ((TextView)VersionDialogView.findViewById(R.id.versiontile)).setText(MyApplication.mNetProc.mLoginUserInf.versionInfo.versionName);
                    ((TextView)VersionDialogView.findViewById(R.id.versionContent)).setText(MyApplication.mNetProc.mLoginUserInf.versionInfo.content);
                    VersionDialogView.findViewById(R.id.btnlater).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            versionDialog.dismiss();
                        }
                    });
                    VersionDialogView.findViewById(R.id.btnupgrade).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Util.DownloadNewVersion().execute();
                            versionDialog.dismiss();
                        }
                    });

                    versionDialog.show();
                    break;
                case 2:
                    LayoutInflater factoryred = LayoutInflater.from(this);
                    final View VersionDialogViewred = factoryred.inflate(R.layout.verionupgradered, null);
                    final AlertDialog versionDialogred = new AlertDialog.Builder(this).create();
                    versionDialogred.setCanceledOnTouchOutside(false);
                    versionDialogred.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    versionDialogred.setView(VersionDialogViewred, 80, 450, 80, 450);
                    ((TextView)VersionDialogViewred.findViewById(R.id.versiontile)).setText(MyApplication.mNetProc.mLoginUserInf.versionInfo.versionName);
                    ((TextView)VersionDialogViewred.findViewById(R.id.versionContent)).setText(MyApplication.mNetProc.mLoginUserInf.versionInfo.content);

                    VersionDialogViewred.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new Util.DownloadNewVersion().execute();
                            versionDialogred.dismiss();

                        }
                    });

                    versionDialogred.show();

                    break;
                case 3:
                    break;
            }
        }
    }
    //////Aurora////////////

    private void delPreLoginCache() {
        JVerificationInterface.clearPreLoginCache();
    }

//    private String token;
//
//    public void getToken() {
//        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
//        if (!verifyEnable) {
//            onNormalLogin();
//            return;
//        }
//
//        showLoadingDialog();
//        JVerificationInterface.getToken(this, 5000, new VerifyListener() {
//            @Override
//            public void onResult(final int code, final String content, final String operator) {
//
//                if (code == 2000) {
//                    token = content;
//                    Log.d(TAG, "[1" + code + "]token=" + content + ", operator=" + operator);
//                    preLogin();
//                } else {
//                    Log.d(TAG, "[1" + code + "]message=" + content);
//                    dismissLoadingDialog();
//                    onNormalLogin();
//                }
//
//            }
//        });
//    }
    boolean isPreLoginSuccess = false;
    public void preLogin() {
//        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
//        if (!verifyEnable) {
//            Log.d(TAG,"[2016],msg = 当前网络环境不支持认证");
//            onNormalLogin();
//            return;
//        }
        if(alertDialog == null)
            showLoadingDialog();
        JVerificationInterface.preLogin(this, 5000, new PreLoginListener() {
            @Override
            public void onResult(final int code, final String content) {

                Log.d(TAG, "[" + code + "]message=" + content);
                if(code == 7000)
                {
                    setUIConfig(false);
                    JVerificationInterface.loginAuth(MainActivity.this, autoFinish, new VerifyListener() {
                        @Override
                        public void onResult(final int code, final String content, final String operator) {
                            Log.d(TAG, "[" + code + "]message=" + content + ", operator=" + operator);
                            Bundle bundle = new Bundle();
                            bundle.putInt(LOGIN_CODE, code);
                            bundle.putString(LOGIN_CONTENT, content);
                            bundle.putString(LOGIN_OPERATOR, operator);
                            //这里通过static bundle保存数据是为了防止出现授权页方向和MainActivity不相同时，MainActivity被销毁重新初始化导致回调数据无法展示到MainActivity

                            Log.d(TAG, "[" + code + "]message=" + content + ", operator=" + operator);
                            new Aurora_RealNumber(content);
                            dismissLoadingDialog();
                        }
                    }, new AuthPageEventListener() {
                        @Override
                        public void onEvent(int cmd, String msg) {
                            Log.d(TAG, "[onEvent111]. [" + cmd + "]message=" + msg);
                        }
                    });
                }
                else
                {
                    dismissLoadingDialog();
                    onNormalLogin();
                }

            }
        });
    }

    public void loginAuth(boolean isDialogMode) {
        JCollectionAuth.enableAutoWakeup(this, false);
        JVerificationInterface.init(this, new RequestCallback<String>() {
            @Override
            public void onResult(int code, String msg) {
                Log.d(TAG, "code = " + code + " msg = " + msg);

            }
        });

        JVerificationInterface.setDebugMode(false);
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);

        if (!verifyEnable) {
            Log.d(TAG, "[2016],msg = 当前网络环境不支持认证");
            onNormalLogin();
            return;
        }
        preLogin();
    }

    private void setUIConfig(boolean isDialogMode) {
        JVerifyUIConfig portrait = getPortraitConfig(isDialogMode);
//        JVerifyUIConfig landscape = getLandscapeConfig(isDialogMode);
//
        //支持授权页设置横竖屏两套config，在授权页中触发横竖屏切换时，sdk自动选择对应的config加载。
        JVerificationInterface.setCustomUIWithConfig(portrait, null);
    }

    private JVerifyUIConfig getPortraitConfig(boolean isDialogMode) {
        JVerifyUIConfig.Builder configBuilder = new JVerifyUIConfig.Builder();

        //自定义按钮示例1
        TextView mTitleTxt = new TextView(this);
        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mLayoutParams1.setMargins(0, dp2Pix(this, 80.0f), 0, 0);
        mTitleTxt.setText("登录/注册");
        mTitleTxt.setTextSize(24.0f);
        mTitleTxt.setTypeface(mTitleTxt.getTypeface(), Typeface.BOLD);
        mTitleTxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mTitleTxt.setTextColor(Color.BLACK);
        mTitleTxt.setClickable(false);
        mTitleTxt.setLayoutParams(mLayoutParams1);
        //自定义按钮示例2
        Button mBtn2 = new Button(this);
        RelativeLayout.LayoutParams mLayoutParams2 = new RelativeLayout.LayoutParams(dp2Pix(this, 300), dp2Pix(this, 50));
        mLayoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mBtn2.setBackground(getResources().getDrawable(R.drawable.aurora_otherloginbutton));
        mLayoutParams2.setMargins(0, dp2Pix(this, 320), 0, 0);
        mBtn2.setText("其他手机号码登录");
        mBtn2.setTypeface(mTitleTxt.getTypeface(), Typeface.BOLD);
        mBtn2.setTextColor(Color.BLACK);
        mBtn2.setLayoutParams(mLayoutParams2);

        ImageView loadingView = new ImageView(getApplicationContext());
        loadingView.setImageResource(R.drawable.umcsdk_load_dot_white);
        RelativeLayout.LayoutParams loadingParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingView.setLayoutParams(loadingParam);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.umcsdk_anim_loading);
        //wechat

        LinearLayout OtherLy = new LinearLayout(this);
        TextView otherLoginTxt = new TextView(this);
        otherLoginTxt.setText("其他方式登录");
        RelativeLayout.LayoutParams otherLoginTxtParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLoginTxtParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        otherLoginTxt.setLayoutParams(otherLoginTxtParam);
        ImageView wechatImg = new ImageView(this);
        wechatImg.setImageDrawable(getResources().getDrawable(R.drawable.orora_wechat));
        RelativeLayout.LayoutParams wechatImgParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        wechatImgParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        wechatImg.setLayoutParams(wechatImgParam);
        wechatImg.setVisibility(View.GONE);
        OtherLy.addView(otherLoginTxt);
        OtherLy.addView(wechatImg);
        RelativeLayout.LayoutParams otherLyparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLyparam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        otherLyparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        otherLyparam.setMargins(0,0, 0, dp2Pix(this, 100));
        OtherLy.setLayoutParams(otherLyparam);

        if(MyApplication.mNetProc.mLoginUserInf.mbWechatAuroraSind)
            OtherLy.setVisibility(View.INVISIBLE);
        else
            OtherLy.setVisibility(View.VISIBLE);
        //导航栏按钮示例
        ImageView navBackImg = new ImageView(this);
        navBackImg.setImageDrawable(getResources().getDrawable(R.drawable.newback));
        RelativeLayout.LayoutParams navBtnParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        navBtnParam.setMargins(dp2Pix(this, 20.0f), dp2Pix(this, 20.0f), 0, 0);
        navBtnParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        navBackImg.setLayoutParams(navBtnParam);
        if (isDialogMode) {
            //窗口竖屏
            mLayoutParams1.setMargins(dp2Pix(this, 100), dp2Pix(this, 250.0f), 0, 0);
            mTitleTxt.setLayoutParams(mLayoutParams1);

            mLayoutParams2.setMargins(0, dp2Pix(this, 250.0f), dp2Pix(this, 100.0f), 0);
            mBtn2.setLayoutParams(mLayoutParams2);

            //自定义返回按钮示例
            ImageButton sampleReturnBtn = new ImageButton(getApplicationContext());
            sampleReturnBtn.setImageResource(R.drawable.umcsdk_return_bg);
            RelativeLayout.LayoutParams returnLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            returnLP.setMargins(10, 10, 0, 0);
            sampleReturnBtn.setLayoutParams(returnLP);

            configBuilder.setAuthBGImgPath("main_bg")
                    .setNavColor(0xff0086d0)
                    .setNavText("登录")
                    .setNavTextColor(0xffffffff)
                    .setNavReturnImgPath("umcsdk_return_bg")
                    .setLogoWidth(70)
                    .setLogoHeight(70)
                    .setLogoHidden(false)
                    .setNumberColor(0xff333333)
                    .setLogBtnText("本机号码一键登录")
                    .setLogBtnTextColor(0xffffffff)
                    .setLogBtnImgPath("aurora_loginbutton")
                    //私条款url为网络网页或本地网页地址(sd卡的地址，需自行申请sd卡读权限)，
                    // 如：assets下路径："file:///android_asset/t.html"，
                    // sd卡下路径："file:"+Environment.getExternalStorageDirectory().getAbsolutePath() +"/t.html"
                    .setAppPrivacyOne("应用自定义服务条款一", "https://www.jiguang.cn/about")
                    .setAppPrivacyTwo("应用自定义服务条款二", "https://www.jiguang.cn/about")
                    .setAppPrivacyColor(0xff666666, 0xff0085d0)
                    .setUncheckedImgPath("umcsdk_uncheck_image")
                    .setCheckedImgPath("umcsdk_check_image")
                    .setSloganTextColor(0xff999999)
                    .setLogoOffsetY(25)
                    .setLogoImgPath("logo_cm")
                    .setNumFieldOffsetY(130)
                    .setSloganOffsetY(160)
                    .setLogBtnOffsetY(184)
                    .setNumberSize(18)
                    .setPrivacyState(false)
                    .setNavTransparent(false)
                    .setPrivacyOffsetY(5)
                    .setDialogTheme(360, 390, 0, 0, false)
                    .setLoadingView(loadingView, animation)
                    .enableHintToast(true, Toast.makeText(getApplicationContext(), "checkbox未选中，自定义提示", Toast.LENGTH_SHORT))
                    .addCustomView(mTitleTxt, true, new JVerifyUIClickCallback() {
                        @Override
                        public void onClicked(Context context, View view) {
                            Toast.makeText(context, "自定义的按钮1，会finish授权页", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addCustomView(mBtn2, false, new JVerifyUIClickCallback() {
                        @Override
                        public void onClicked(Context context, View view) {
                            Toast.makeText(context, "自定义的按钮2，不会finish授权页", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addCustomView(sampleReturnBtn, true, null);
        } else {
            configBuilder.setAuthBGImgPath("main_bg")
                    .setNumberColor(0xff333333)
                    .setSloganHidden(true)
                    .setLogoHidden(true)
                    .setNumberTextBold(true)
                    .setLogBtnText("本机号码一键登录")
                    .setLogBtnTextColor(0xffffffff)
                    .setLogBtnHeight(50)
                    .setLogBtnTextBold(true)
                    .setLogBtnWidth(300)
                    .setLogBtnOffsetY(250)
                    .setLogBtnImgPath("aurora_loginbutton")
                    //私条款url为网络网页或本地网页地址(sd卡的地址，需自行申请sd卡读权限)，
                    // 如：assets下路径："file:///android_asset/t.html"，
                    // sd卡下路径："file:"+Environment.getExternalStorageDirectory().getAbsolutePath() +"/t.html"
                    .setAppPrivacyOne("用户协议", Constants.AGREENEBT_URL)
                    .setAppPrivacyTwo("隐私协议", Constants.PRIVACY_URL)
                    .setPrivacyTopOffsetY(380)
                    .setPrivacyTextWidth(280)
                    .setPrivacyStatusBarHidden(true)
                    .setPrivacyTextCenterGravity(true)
                    .setPrivacyText("同意",  "首次登陆自动注册")
                    .setPrivacyCheckboxHidden(false)
                    .setPrivacyWithBookTitleMark(false)
                    .setPrivacyCheckboxInCenter(false)
                    .setPrivacyTextSize(13)
                    .setPrivacyCheckboxSize(13)
                    .setPrivacyVirtualButtonTransparent(true)
                    .setPrivacyState(false)
                    .setAppPrivacyColor(0xff666666, 0xff149F38)
                    .setNumFieldOffsetY(180)
                    .setNumberSize(30)
                    .setNavTransparent(true)
                    .addCustomView(mTitleTxt, false ,null)
                    .addCustomView(mBtn2, true, new JVerifyUIClickCallback() {
                        @Override
                        public void onClicked(Context context, View view) {
                            onNormalLogin();
                           // Toast.makeText(context, "自定义的按钮2，不会finish授权页", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addNavControlView(navBackImg, new JVerifyUIClickCallback() {
                        @Override
                        public void onClicked(Context context, View view) {
                            JVerificationInterface.dismissLoginAuthActivity();
                        }
                    });

        }
        return configBuilder.build();
    }

    private JVerifyUIConfig getLandscapeConfig(boolean isDialogMode) {
        JVerifyUIConfig.Builder configBuilder = new JVerifyUIConfig.Builder();

        //自定义按钮示例1
        ImageView mBtn = new ImageView(this);
        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(dp2Pix(getApplicationContext(), 40), dp2Pix(getApplicationContext(), 40));
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

        //自定义按钮示例2
        ImageView mBtn2 = new ImageView(this);
        RelativeLayout.LayoutParams mLayoutParams2 = new RelativeLayout.LayoutParams(dp2Pix(getApplicationContext(), 40), dp2Pix(getApplicationContext(), 40));
        mLayoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        if (isDialogMode) {
            //窗口横屏
            mLayoutParams1.setMargins(dp2Pix(this, 200), dp2Pix(this, 235.0f), 0, 0);
            mBtn.setLayoutParams(mLayoutParams1);

            mLayoutParams2.setMargins(0, dp2Pix(this, 235.0f), dp2Pix(this, 200.0f), 0);
            mBtn2.setLayoutParams(mLayoutParams2);

            //自定义返回按钮示例
            ImageButton sampleReturnBtn = new ImageButton(getApplicationContext());
            sampleReturnBtn.setImageResource(R.drawable.umcsdk_return_bg);
            RelativeLayout.LayoutParams returnLP = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            returnLP.setMargins(10, 10, 0, 0);
            sampleReturnBtn.setLayoutParams(returnLP);

            configBuilder.setAuthBGImgPath("main_bg")
                    .setNavColor(0xff0086d0)
                    .setNavText("登录")
                    .setNavTextColor(0xffffffff)
                    .setNavReturnImgPath("umcsdk_return_bg")
                    .setLogoWidth(70)
                    .setLogoHeight(70)
                    .setLogoHidden(false)
                    .setNumberColor(0xff333333)
                    .setLogBtnText("本机号码一键登录")
                    .setLogBtnTextColor(0xffffffff)
                    .setLogBtnImgPath("umcsdk_login_btn_bg")
                    //私条款url为网络网页或本地网页地址(sd卡的地址，需自行申请sd卡读权限)，
                    // 如：assets下路径："file:///android_asset/t.html"，
                    // sd卡下路径："file:"+Environment.getExternalStorageDirectory().getAbsolutePath() +"/t.html"
                    .setAppPrivacyOne("应用自定义服务条款一", "https://www.jiguang.cn/about")
                    .setAppPrivacyTwo("应用自定义服务条款二", "https://www.jiguang.cn/about")
                    .setAppPrivacyColor(0xff666666, 0xff0085d0)
                    .setUncheckedImgPath("umcsdk_uncheck_image")
                    .setCheckedImgPath("umcsdk_check_image")
                    .setSloganTextColor(0xff999999)
                    .setLogoOffsetY(25)
                    .setLogoImgPath("logo_cm")
                    .setNumFieldOffsetY(120)
                    .setSloganOffsetY(155)
                    .setLogBtnOffsetY(180)
                    .setPrivacyOffsetY(10)
                    .setDialogTheme(500, 350, 0, 0, false)
                    .enableHintToast(true, null)
                    .addCustomView(mBtn, true, new JVerifyUIClickCallback() {
                        @Override
                        public void onClicked(Context context, View view) {
                            Toast.makeText(context, "自定义的按钮1，会finish授权页", Toast.LENGTH_SHORT).show();
                        }
                    }).addCustomView(mBtn2, false, new JVerifyUIClickCallback() {
                @Override
                public void onClicked(Context context, View view) {
                    Toast.makeText(context, "自定义的按钮2，不会finish授权页", Toast.LENGTH_SHORT).show();
                }
            })
                    .addCustomView(sampleReturnBtn, true, null);
        } else {
            //全屏横屏
            mLayoutParams1.setMargins(dp2Pix(this, 200), dp2Pix(this, 100.0f), 0, 0);
            mBtn.setLayoutParams(mLayoutParams1);

            mLayoutParams2.setMargins(0, dp2Pix(this, 100.0f), dp2Pix(this, 200.0f), 0);
            mBtn2.setLayoutParams(mLayoutParams2);

            //导航栏按钮示例
            Button navBtn = new Button(this);
            navBtn.setText("导航栏按钮");
            navBtn.setTextColor(0xff3a404c);
            navBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            RelativeLayout.LayoutParams navBtnParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            navBtnParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            navBtn.setLayoutParams(navBtnParam);

            configBuilder
                    .setAuthBGImgPath("main_bg")
                    .setNavReturnImgPath("umcsdk_return_bg")
                    .setNumberColor(0xff333333)
                    .setNumberTextBold(true)
                    .setLogBtnText("本机号码一键登录")
                    .setLogBtnTextColor(0xffffffff)
                    //私条款url为网络网页或本地网页地址(sd卡的地址，需自行申请sd卡读权限)，
                    // 如：assets下路径："file:///android_asset/t.html"，
                    // sd卡下路径："file:"+Environment.getExternalStorageDirectory().getAbsolutePath() +"/t.html"
                    .setAppPrivacyOne("用户协议", Constants.AGREENEBT_URL)
                    .setAppPrivacyTwo("隐私协议", Constants.PRIVACY_URL)
                    .setAppPrivacyColor(0xff666666, 0xff0085d0)
                    .setNumFieldOffsetY(150)
                    .setLogBtnOffsetY(210)
                    .setPrivacyOffsetY(10)
                    .addCustomView(mBtn, true, new JVerifyUIClickCallback() {
                        @Override
                        public void onClicked(Context context, View view) {
                            Toast.makeText(context, "自定义的按钮1，会finish授权页", Toast.LENGTH_SHORT).show();
                        }
                    }).addCustomView(mBtn2, false, new JVerifyUIClickCallback() {
                @Override
                public void onClicked(Context context, View view) {
                    Toast.makeText(context, "自定义的按钮2，不会finish授权页", Toast.LENGTH_SHORT).show();
                }
            });

        }
        return configBuilder.build();
    }


    private AlertDialog alertDialog;

    public void showLoadingDialog() {
        dismissLoadingDialog();
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        alertDialog.show();
        alertDialog.setContentView(R.layout.loading_alert);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private int dp2Pix(Context context, float dp) {
        try {
            float density = context.getResources().getDisplayMetrics().density;
            return (int) (dp * density + 0.5F);
        } catch (Exception e) {
            return (int) dp;
        }
    }

    public void onNormalLogin()
    {
        Intent auroraLogin = new Intent(this, AuroraLogin.class);
        startActivity(auroraLogin);
    }

    public void onNextSignupStep()
    {
        MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_SIGNUP, "loginWithPhone", "phone=" +  MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber +
                "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=3" , Constants.SINGIN_TYPE_SMS, MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);//signup


    }
    public void onWechat() {
        boolean isAppInstalled = appInstalledOrNot("com.tencent.mm");
        if(isAppInstalled) {
            Intent intent = new Intent(this, WeChatLoginActivity.class);
            startActivityForResult(intent, Constants.WE_CHAT_LOGIN);
            Intent wechatLoginActivity = new Intent(this, WeChatLoginActivity.class);
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

//       @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("wechataccesstoken", "code1");
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//
//        if (requestCode == Constants.WE_CHAT_LOGIN) {
//            Log.d("wechataccesstoken", "code");
//            final String code = data.getStringExtra(Constants.WE_CHAT_AUTH_CODE);
//            mWechatToken = new wechataccesstoken(this, 0);
//            mWechatToken.wechataccesstoken(code);
//        }
//    }

}

