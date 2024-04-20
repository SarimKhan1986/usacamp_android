package com.usacamp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.IntentCompat;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
//import com.iflytek.cloud.SpeechUtility;
import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.net.NetProcess;
import com.usacamp.net.ThreadPoolUtils;
import com.usacamp.services.OssService;
import com.usacamp.services.UIDisplayer;
import com.usacamp.utils.ErrorMsg;
import com.usacamp.utils.MessageAlert;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.RequestCallback;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.jakewharton.processphoenix.ProcessPhoenix;
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static MyApplication mInstance;
    public SharedPreferences preferences;
    public String prefName = "LoginInf";
    //net procedure
    public static NetProcess mNetProc = null;
    public int phoneWidth = 0, phoneHeight = 0;
    private Activity mCurrentActivity = null;
    private final Set<Activity> runningActivities = new HashSet<>();
    public boolean isTablet = false;
    public String SERVER_API, SERVER_URL, WECHAT_APPID;
    public boolean isOfflineMode = false;
    public String downloadedLevelsInOfflineMode = "";
    public boolean isFirstLoginFailure = false;
    public boolean isLogout = true;

    public  String mstrIV = "usacampiv2024130";
    public  String mstrPassword = "";
    String TAG = "usacamp_application";

    public  String strPhoneNumber = "";

    public  String strActivityRuleLink = "https://www.usacamp.cn/Wechat/h5_playrule";

    public void killActivities(Set<Class<?>> activitiesToRemove) {
        for (Activity stackActivity : runningActivities) {
            if (activitiesToRemove.contains(stackActivity.getClass())) {
                stackActivity.finish();
            }
        }
    }

    public void killAllActivities() {
        for (Activity stackActivity : runningActivities) {
            stackActivity.finish();
        }
    }

    public MyApplication() {
        mInstance = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate() {
        super.onCreate();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ConnectionQuality();
//        }
        mInstance = this;
        preferences = this.getSharedPreferences(prefName, 0);

        if (BuildConfig.BUILD_TYPE.equals(Constants.Build_Release)) {
            State.setState(State.app_state.real);
        } else {
            //dev
            State.setState(State.app_state.dev);
        }

        isTablet = getResources().getBoolean(R.bool.isTablet);
        //isOfflineMode = getOfflineMode();
        //isDownloaded = getDownLoadedFlag();
        mNetProc = new NetProcess();
        getCommonUserInf();

//        SpeechUtility.createUtility(this, "appid=" + Constants.SPEAKING_APPID);
//        JVerificationInterface.init(this, new RequestCallback<String>() {
//            @Override
//            public void onResult(int code, String msg) {
//                Log.d(TAG, "code = " + code + " msg = " + msg);
//
//            }
//        });
//
//        JVerificationInterface.setDebugMode(true);
//        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
//        if (!verifyEnable) {
//            Log.d(TAG, "当前网络环境不支持认证");
//            ///return;
//        }

        this.registerActivityLifecycleCallbacks(this);

    }

    private void removePref() {

        preferences.edit().clear().apply();
    }

    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String GetUTCdatetimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }

    public static Date StringDateToDate(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try {
            dateToReturn = (Date) dateFormat.parse(StrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToReturn;
    }

    public void sendErrorMsg() {
        if (preferences != null) {
            String json = preferences.getString("error", "");
            if (!json.isEmpty()) {
                MyApplication.mNetProc.netErrorLog("netErrorLog", "data=" + json);

            }
        }
    }

    public void clearErrorLog() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("error");
        editor.commit();
    }

    public void saveErrorInfo(String funcName, String callStatus) throws JSONException {
        ErrorMsg msg = null;
        int devType = (isTablet ? Constants.DEVICE_KIND.Android_Pad.ordinal() : Constants.DEVICE_KIND.Android.ordinal());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            msg = new ErrorMsg(MyApplication.mNetProc.mLoginUserInf.mstrUserId, String.valueOf(devType), BuildConfig.VERSION_NAME,
                    funcName, GetUTCdatetimeAsString(), callStatus, ConnectionQuality());
        }

        String oldError = preferences.getString("error", "");
        Gson gson = new Gson();
        Type errorListType = new TypeToken<ArrayList<ErrorMsg>>() {
        }.getType();
        ArrayList<ErrorMsg> errorObjArray = new ArrayList<ErrorMsg>();
        if (!oldError.isEmpty())
            errorObjArray = gson.fromJson(oldError, errorListType);
        errorObjArray.add(msg);

        String jsonResult = gson.toJson(errorObjArray);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("error", jsonResult);
        editor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getMobileDataNetworkStatus() {
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getCurrentActivity(),new String[]
//                    { Manifest.permission.ACCESS_COARSE_LOCATION },1);
//
//        }
//        @SuppressLint("HardwareIds") String phonenumber = telephonyManager.getLine1Number();
//
//        CellInfoGsm cellinfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
//        CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
//        int networkSpeed = cellSignalStrengthGsm.getDbm();
//

        ///////
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        int upSpeed = nc.getLinkUpstreamBandwidthKbps();
        return downSpeed;
    }

    public int getWifiStatus() {
        try {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            int state = wifiManager.getWifiState();
            int speed = wifiManager.getConnectionInfo().getLinkSpeed();
            int frequency = wifiManager.getConnectionInfo().getFrequency();
            int rssi = wifiManager.getConnectionInfo().getRssi();
            int level = WifiManager.calculateSignalLevel(rssi, 5);
            int percentage = (int) ((level / 10.0) * 100);
            return level;
        } catch (Exception e) {
            return 0;
        }
    }

    public int networkState = 1;
    ; // 0:no internet, 1: wifi, 2:mobile data

    @RequiresApi(api = Build.VERSION_CODES.M)
    public String ConnectionQuality() {
        NetworkInfo info = getInfo(getApplicationContext());
        if (info == null || !info.isConnected()) {
            networkState = 0;
            return "no Internet";
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            networkState = 1;
            //netSpeed = getWifiStatus();
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            if (level <= 2)
                return "POOR";
            else if (level == 3)
                return "MODERATE";
            else if (level == 4)
                return "GOOD";
            else if (level == 5)
                return "EXCELLENT";
            else
                return "UNKNOWN";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            networkState = 2;
            int networkClass = getNetworkClass(getNetworkType(getApplicationContext()));
            if (networkClass == 1)
                return "POOR";
            else if (networkClass == 2)
                return "GOOD";
            else if (networkClass == 3)
                return "EXCELLENT";
            else
                return "UNKNOWN";
        } else
            return "UNKNOWN";
    }

    public NetworkInfo getInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    public int getNetworkClass(int networkType) {
        try {
            return getNetworkClassReflect(networkType);
        } catch (Exception ignored) {
        }

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return 1;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return 2;
            case TelephonyManager.NETWORK_TYPE_LTE:
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return 3;
            default:
                return 0;
        }
    }

    private int getNetworkClassReflect(int networkType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getNetworkClass = TelephonyManager.class.getDeclaredMethod("getNetworkClass", int.class);
        if (!getNetworkClass.isAccessible()) {
            getNetworkClass.setAccessible(true);
        }
        return (Integer) getNetworkClass.invoke(null, networkType);
    }

    public int getNetworkType(Context context) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType();
    }

    ///
    public void logOut() {
        removePref();
        MyApplication.mNetProc.mLoginUserInf.initialize();
        State.setCurrent_login_state(State.loginType.logout);

        if(State.getCurrentPattern() == State.pattern.parent.getValue()) {
            Intent mainInt = new Intent(MyApplication.getInstance().getCurrentActivity(), MainActivity.class);
            MyApplication.getInstance().getCurrentActivity().startActivity(mainInt);
            MainActivity.mainActivityInstance.goFragmentHome();
            MyApplication.mNetProc.getMediaInfo2("getMediaInfo2", "userid=0");
        } else {
            Intent loginInt = new Intent(MyApplication.getInstance().getCurrentActivity(), AuroraLogin.class);
            MyApplication.getInstance().getCurrentActivity().startActivity(loginInt);
            StudentPatternActivity.instance.finish();
        }

    }
    public Activity getCurrentActivity() {
        return mCurrentActivity ;
    }
    public void setCurrentActivity (Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity ;

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }
    public int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? 1 : -1;
        }

        return res;
    }

    public void getCommonUserInf(){
        isOfflineMode = getOfflineMode();
        downloadedLevelsInOfflineMode = getLevelListInOfflineMode();
        if(getUserIdFromPref().equals("")) {
            State.setCurrent_login_state(State.loginType.logout);
            mNetProc.getAppData("getAppData", "userid=0");
        } else {
            State.setCurrent_login_state(State.loginType.prelogin);
            mNetProc.getAppData("getAppData", "userid=" + getUserIdFromPref());

        }
        //MyApplication.mNetProc.getForeignTeacherVideo("getForeignTeacherVideo", "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id);
    }
    public void quit()
    {
        System.exit(0);

    }

    public void reCallAppData(){
        //Thread.currentThread().interrupt();
        getCommonUserInf();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void saveIsLoggedIn(boolean flag) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("pass", flag);
        editor.commit();
    }

    public boolean getIsLoggedIn() {
        if (preferences != null) {
            boolean flag = preferences.getBoolean(
                    "pass", false);
            return flag;
        }
        return false;
    }
    public void saveLoginMode(int v)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("loginMode", v);
        editor.commit();
    }
    public void saveOfflineMode(boolean v)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("offlineMode", v);
        editor.commit();
    }
    public void saveLevelListInOfflineMode(int id)
    {
        SharedPreferences.Editor editor = preferences.edit();
        downloadedLevelsInOfflineMode += id + ",";
        editor.putString("DownloadedLevels", downloadedLevelsInOfflineMode);
        editor.commit();
    }
    public void removeLevellistInOfflineMode()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("DownloadedLevels");
        editor.commit();
    }
    public String getLevelListInOfflineMode()
    {
        if (preferences != null) {
            return preferences.getString(
                    "DownloadedLevels", "");
        }
        return "";
    }

    public boolean getOfflineMode()
    {
        if (preferences != null) {
            return preferences.getBoolean(
                    "offlineMode", false);
        }
        return false;
    }
    public void saveDownLoadedFlag(boolean v)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("downloaded", v);
        editor.commit();
    }
    public boolean getDownLoadedFlag()
    {
        if (preferences != null) {
            return preferences.getBoolean(
                    "downloaded", false);
        }
        return false;
    }
    public int getLoginMode()
    {
        if (preferences != null) {
            return preferences.getInt(
                    "loginMode", 0);
        }
        return 0;
    }
    public void saveSpeechFlag(boolean flag)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("speech", flag);
        editor.commit();
    }

    public boolean getSpeechFlag()
    {
        if (preferences != null) {
            boolean flag = preferences.getBoolean(
                    "speech", false);
            return flag;
        }
        return false;
    }
    public void saveLogin() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userPhonenumber", MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
        editor.putString("userId", MyApplication.mNetProc.mLoginUserInf.mstrUserId);
        editor.putInt("loginMode", State.getCurrentPattern());
        if(isTablet)
            editor.putString("hardTypeId", Constants.DEVICE_KIND.Android_Pad.toString());
        else
            editor.putString("hardTypeId", Constants.DEVICE_KIND.Android.toString());
        editor.putString("hardware", MyApplication.mNetProc.mLoginUserInf.mstrDeviceID);
        editor.commit();
    }

    public String getPhoneNumberFromPref() {
        if (preferences != null) {
            return preferences.getString(
                    "userPhonenumber", "");
        }
        return "";
    }

    public String getHardWareTypeFromPref() {
        if (preferences != null) {
            return preferences.getString(
                    "hardTypeId", "");

        }
        return "";
    }
    public String getUserIdFromPref()
    {
        if (preferences != null) {
            return preferences.getString(
                    "userId", "");
        }
        return "";
    }

    public String getHardWareSerialNumberFromPref() {
        String serialNumber = "123456789";//Build.SERIAL != Build.UNKNOWN ? Build.SERIAL : Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        MyApplication.mNetProc.mLoginUserInf.mstrDeviceID = serialNumber;
        return serialNumber;
    }

    public void setPasswordTOPref(String v)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userPassword", v);
        editor.commit();
    }

    public String getPasswordFromPref(){
        if (preferences != null) {
            return preferences.getString(
                    "userPassword", "");
        }
        return "";
    }
    public void setPhoneWidthHeight(int width, int height)
    {
        phoneWidth = width;
        phoneHeight = height;
    }

    public int getPhoneWidth()
    {
        return phoneWidth;
    }

    public int getPhoneHeight()
    {
        return phoneHeight;
    }
    public void loginOk() {
        mNetProc.mLoginUserInf.mbAlreadyLogin = true;
        saveLogin();
        getTeacherVideoList();
        State.setPattern(State.current_pattern);
    }

    private void getTeacherVideoList()
    {
        String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                "&purchase_level=0";
        MyApplication.mNetProc.getLevelList("getLevelList", strRequestParameter);
        String strRequestParameter1 = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id;
        MyApplication.mNetProc.getPurchaseLevels("getPurchasedLevelList", strRequestParameter1);

//                if(State.getCurrent_login_state() == State.loginType.logout) {
//
//                    mNetProc.getMediaInfo2("getMediaInfo2", "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId);
//                    State.setCurrent_login_state(State.loginType.prelogin);
//                }

    }

    public void signupOK()
    {
        mNetProc.mLoginUserInf.mbAlreadyLogin = true;
        saveLogin();
        Intent AuroraSignupInt = new Intent(this, AuroraSingupPassword.class);
        AuroraSignupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(AuroraSignupInt);
        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_CallBack_Signup_Success);

        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Signup_NextButton);

        try {
            MainActivity.mainActivityInstance.refreshCommonInf();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getTeacherVideoList();
    }

    public void finishWechatLogin(String strWechatOpenId){

        MyApplication.mNetProc.mLoginUserInf.mstrOpenID = strWechatOpenId;
        if( !mNetProc.mLoginUserInf.mbWechatAuroraSind)
            MyApplication.mNetProc.loginWithWeixin("loginWithWeixin",
                "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&openid=" + strWechatOpenId);
        else {
            MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_WECHATBIND, "loginWithPhone", "phone=" + mNetProc.mLoginUserInf.muserPhoneNumber +
                    "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_SMS, mNetProc.mLoginUserInf.muserPhoneNumber);

        }
    }

    public boolean isRunningActivity(String activityName) {
        for (Activity stackActivity : runningActivities) {
            Log.d("activityname", stackActivity.getClass().getSimpleName());
            if (stackActivity.getClass().getSimpleName().contains(activityName)) {
                return true;
            }
        }
        return false;
    }

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        runningActivities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override public void onActivityDestroyed(Activity activity) {
        runningActivities.remove(activity);
    }

    public OssService initOSS(String endpoint, String bucket, UIDisplayer displayer) {

//        移动端是不安全环境，不建议直接使用阿里云主账号ak，sk的方式。建议使用STS方式。具体参
//        https://help.aliyun.com/document_detail/31920.html
//        注意：SDK 提供的 PlainTextAKSKCredentialProvider 只建议在测试环境或者用户可以保证阿里云主账号AK，SK安全的前提下使用。具体使用如下
//        主账户使用方式
//        String AK = "******";
//        String SK = "******";
//        credentialProvider = new PlainTextAKSKCredentialProvider(AK,SK)
//        以下是使用STS Sever方式。
//        如果用STS鉴权模式，推荐使用OSSAuthCredentialProvider方式直接访问鉴权应用服务器，token过期后可以自动更新。
//        详见：https://help.aliyun.com/document_detail/31920.html
//        OSSClient的生命周期和应用程序的生命周期保持一致即可。在应用程序启动时创建一个ossClient，在应用程序结束时销毁即可。

        OSSCredentialProvider credentialProvider;
        //使用自己的获取STSToken的类
        credentialProvider = new OSSAuthCredentialsProvider(Constants.OSS_STS_SERVER_URL);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
        OSSLog.enableLog();
        return new OssService(oss, bucket, displayer);

    }
}
