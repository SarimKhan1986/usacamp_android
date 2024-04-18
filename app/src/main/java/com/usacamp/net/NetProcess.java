package com.usacamp.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Base64;
import java.lang.Math;
import android.util.Log;

import com.google.gson.JsonObject;
import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.activities.AddChildActivity;
import com.usacamp.activities.AuroraLogin;
import com.usacamp.activities.BannerDetail;
import com.usacamp.activities.BindActivity;
import com.usacamp.activities.LearnActivity;
import com.usacamp.activities.LearningRecordActivity;
import com.usacamp.activities.LoginActivity;
import com.usacamp.activities.MainActivity;
import com.usacamp.activities.ModifyChildProfileActivity;
import com.usacamp.activities.MyApplication;
import com.usacamp.activities.MyPointActivity;
import com.usacamp.activities.PasswordReset;
import com.usacamp.activities.PurchaseSuccess;
import com.usacamp.activities.SettingActivity;
import com.usacamp.activities.ShareType;
import com.usacamp.activities.SignupActivity;
import com.usacamp.activities.SpeakingActivity;
import com.usacamp.activities.SpeakingResultActivity;
import com.usacamp.activities.SplashActivity;
import com.usacamp.activities.StudentPatternActivity;
import com.usacamp.activities.SwitchPatternActivity;
import com.usacamp.activities.articledetail;
import com.usacamp.activities.mycampopoup;
import com.usacamp.activities.parent_category;
import com.usacamp.activities.payhistory;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.model.ImageObj;
import com.usacamp.model.ActCenterItem;
import com.usacamp.model.DeviceInfo;
import com.usacamp.utils.EasyAES;
import com.usacamp.utils.LearningRecordItem;
import com.usacamp.model.LessonItem;
import com.usacamp.model.LevelItem;
import com.usacamp.utils.MakeJsonForSpeaking;
import com.usacamp.model.MediaItem;
import com.usacamp.utils.MessageAlert;
import com.usacamp.model.MessageItem;
import com.usacamp.utils.MyPaymentItem;
import com.usacamp.utils.MyPointItem;
import com.usacamp.model.ParentItem;
import com.usacamp.model.PartItem;
import com.usacamp.utils.PayConf;
import com.usacamp.model.ProfileBannerItem;
import com.usacamp.model.ProgressItem;
import com.usacamp.model.ScoreItem;
import com.usacamp.model.TestItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.usacamp.constants.State.*;

public class NetProcess {

    //public boolean mfDefaultLogin;
    public UserInformation mLoginUserInf = new UserInformation();
    public boolean mfNetworkEnable = false;
    private int mbSmsType = 0;   //0: Login, 1: signup, 2: password Reset, 3:password reset, 4:bind
    private int mnBindType = 0; //1 login Bind , 1 setting bind
    private boolean isLoadWebView = true;
    private final int mnStudyType = 0; //0 - hearing, 1 - speaking
    private int mloginType = 1; //1 - login, 2-signup, 3 - wechat bind
    private int mbSigninType = 0; //0 - SMS, 1 - Password
    private int mnPassChangeType = 0 ; //0 - AuroraSignup, 1 - PassChange
    private int mnTestReusltType = 0; // 0 - Quiz, 1 - level Test
    public NetProcess() {
        // Required empty public constructor

    }

    /**
     *
     * @param strParameters
     * @return
     */
    public int getEncryptKey(String strParameters){
        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getEncryptKey));
        return 1;
    }

    public int getHardwardTypeList(String strParameters){
        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getHardwardTypeList));
        return 1;
    }

    public int getGuideBanner(String strParameters){
        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getGuideBanner));
        return 1;
    }

    public int getMainArticleList(String strParameters){
        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getMainArticleList));
        return 1;
    }
    public int getArticleTypeList(String strParameters){

        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getArticleType1));
        return 1;
    }
    public int getHotArticleTypeList(String strParameters){

        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getHotArticleType));
        return 1;
    }
    public int getArticlDetail( String strParameters){

        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getArticleDetail));
        return 1;
    }
    public int getFirstMedia(String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getFirstMedia));
        return 1;
    }
    public int getMediaInfo(String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strFunc, RequestReturnCode.REQ_getMediaInfo));
        return 1;
    }
    public int getMediaInfo2(String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc,strParameters, RequestReturnCode.REQ_getMediaInfo2));
        return 1;
    }
    public int getShareInfo(String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc,strParameters, RequestReturnCode.REQ_getShareInfo));
        return 1;
    }
    public int getAppData(String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getAppData));
        return 1;
    }
    public int getForeignTeacherVideo(String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getForeignTeacherVideo));
        return 1;
    }
    public int changePwd( String strFunc, String strParameters, int enterType)
    {
        mnPassChangeType = enterType;
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_changePassword));
        return 1;
    }

    public int setWechatNind( int type, String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_setWechatBind));
        mnBindType = type;
        return 1;
    }
    public int saveOralResult(String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_saveOralResult));
        return 1;
    }
    public int getOralLearnInfo(String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getOralLearnInfo));
        return 1;
    }
    public int resetWechatBind( String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_resetWechatBind));

        return 1;
    }

    /**
     *
     * @param strParameters
     * @return
     */
    public int getPayConf(String strParameters){
        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getPayConf));
        return 1;
    }

    /**
     *
     * @param bType
     * @param strFunc
     * @param strParameters
     * @return
     */
    public int sendSms( int bType, String strFunc, String strParameters){
        mbSmsType = bType;
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_sendSms));
        return 1;
    }

    /**
     *

     * @param type
     * @param strFunc
     * @param strParameters
     * @return
     */
    public int loginWithPhone( int type, String strFunc, String strParameters , int bsigninType, String phonenumber){
        MyApplication.getInstance().strPhoneNumber = phonenumber;
        mloginType = type;
        mbSigninType = bsigninType;
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_loginWithPhone));
        return 1;
    }

    public int netErrorLog( String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_netErrorLog));
        return 1;
    }

    public int setLoginMode( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_setLoginMode));
        return 1;
    }

    public int getOrderInfo( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getOrderInfo));
        return 1;
    }

    public int getAppVersion( String strParameters){
        ThreadPoolUtils.execute(new HttpGetThread(mhandler, strParameters, RequestReturnCode.REQ_getAppVersion));
        return 1;
    }
    public int getToken( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getToken));
        return 1;
    }
    public int requestTrainCamp( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_requestTrainCamp));
        return 1;
    }
    public int removeUser( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getRemoveUser));
        return 1;
    }
    public int loginWithUserID( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_UserIDLogin));
        return 1;
    }

    public int loginWithWeixin( String strFunc, String strParameters)
    {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_WeixinLogin));
        return 1;
    }

    public int bindWeixinPhone( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_bindWeixinPhone));
        return 1;
    }

    public int getNotify( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getNotify));
        return 1;
    }

    public int saveReadNotify( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_saveReadNotify));
        return 1;
    }
    /**
     *
     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getConfig(String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getConfig));
        return 1;
    }

    public int getSharePointValue(  String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getSharePointValue));
        return 1;
    }
    /**
     *
     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getTutorial(String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getTutorial));
        return 1;
    }

    /**
     *
     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getTos(String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getTos));
        return 1;
    }
    /**
     *

     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getLearnList( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getLearnList));
        return 1;
    }

    /**
     *

     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getPointHistory( String strFunc, String strParameters){

        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getPointHistory));
        return 1;
    }
    /**
     *

     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getEventList( String strFunc, String strParameters){

        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getEventList));
        return 1;
    }

    /**
     *

     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getEventContent ( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getEventContent ));
        return 1;
    }

    /**
     * @param strFunc
     * @param strParameters
     * @return
     */
    public int setProfile(String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_setProfile));
        return 1;
    }

    public int savePayment(String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_savePayment));
        return 1;
    }
    /**
     *
     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getAvailableLessons(String strFunc, String strParameters){

        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getAvailableLessons));
        return 1;
    }
    public int getPurchaseLevels(String strFunc, String strParameters){

        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getPurchaseLevels));
        return 1;
    }

    /**
     *
     * @param strFunc
     * @param strParameters
     * @return
     */
    public int getLevelList(String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getLevelList));
        return 1;
    }

    public int getSystemTestUrl(String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getSystemTestUrl));
        return 1;
    }

    public int getLevelTestUrl( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getLevelTestUrl));
        return 1;
    }

    public int getQuizTestResult( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getQuizTestResult));
        return 1;
    }

    public int getLessonURL( String strFunc, String strParameters, boolean isWebView){
        isLoadWebView = isWebView;

        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getLessonURL));
        return 1;
    }

    public int getZipContents(String strFunc, String strParameters) {
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_getLessonURL));
        return 1;
    }

    public int saveQuizTestResult( String strFunc, String strParameters, int type){
        mnTestReusltType = type;
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_saveQuizTestResult));
        return 1;
    }

    public int saveShareContent( String strFunc, String strParameters){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_saveShareContent));
        return 1;
    }

    public int saveLevel( String strFunc, String strParameters){

        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, RequestReturnCode.REQ_saveLevel));
        return 1;
    }


	public int picUpload( String strFunc, String strParameters, ArrayList<String> listAttachedFiles){
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, listAttachedFiles, RequestReturnCode.REQ_picUploadResult));
        return 1;
    }


    public int sendReport( String strFunc, String strParameters, ArrayList<String> listReportFilePath){
//        mdlgProgress = MessageAlert.showProgressDialog(mCurrentActivity, "上传中...");
        ThreadPoolUtils.execute(new HttpPostThread(mhandler, strFunc, strParameters, listReportFilePath, RequestReturnCode.REQ_sendReportResult));
        return 1;
    }
    /**
     *
     */
    Handler mhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String strReturn = "";

            super.handleMessage(msg);
            if (msg.what == RequestReturnCode.REQ_ERROR)
            {
                if(MyApplication.getInstance().getCurrentActivity().getLocalClassName().contains("SplashActivity")) {
                    MyApplication.getInstance().isFirstLoginFailure = true;
                    MessageAlert.showAlertDlg(MyApplication.getInstance().getCurrentActivity());
                    try {
                        MyApplication.getInstance().saveErrorInfo("Splash", String.valueOf(msg.what));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("SendReport", msg.toString());
                    MessageAlert.showMessage(MyApplication.getInstance().getCurrentActivity(), "网上出现错误！");
                  //  MessageAlert.showAlertDlg(MyApplication.getInstance().getCurrentActivity());
                    try {
                        MyApplication.getInstance().saveErrorInfo(MyApplication.getInstance().getCurrentActivity().toString(), String.valueOf(msg.what));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               // MessageAlert.showAlertDlg(MyApplication.getInstance().getCurrentActivity());
                //MessageAlert.showMessage( MyApplication.getInstance().getCurrentActivity(), "网上出现错误！");
                mfNetworkEnable = false;
            }else
            {
                JSONObject jsonObject ;
                String result = (String) msg.obj;

                if(msg.what == RequestReturnCode.REQ_getEncryptKey) {
                    MyApplication.getInstance().mstrPassword = parseJsonForgetEncryptKey(result);
                    return;
                }

                try {
                    if (msg.what == RequestReturnCode.REQ_loginWithPhone
                            || msg.what == RequestReturnCode.REQ_WeixinLogin
                            || msg.what == RequestReturnCode.REQ_UserIDLogin
                            || msg.what == RequestReturnCode.REQ_getLevelList
                            || msg.what == RequestReturnCode.REQ_getPurchaseLevels)
                    {
                        String tempStr = DecodeAES(result);
                        if(!isValid(tempStr))
                        {
                            return;
                        }else{
                            jsonObject = new JSONObject(tempStr);
                        }
                    }else{
                        jsonObject = new JSONObject(result);
                    }

                    String mState = jsonObject.getString("errcode");
                    if(mState.equals("2001")) {//active on other device
                        if ( MyApplication.getInstance().getCurrentActivity() != null ) {
                            MessageAlert.showMessage(MyApplication.getInstance().getCurrentActivity(), "您的账号已在另一个设备上登录，请重新登录");
                            MyApplication.getInstance().logOut();
                            return;
                        }
                    }

                    if (msg.what == RequestReturnCode.REQ_getHardwardTypeList)
                    {
                        strReturn = parseJsonForHardwareType(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getPayConf)
                    {
                        strReturn = parseJsonForPayConf(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_sendSms)
                    {
                        strReturn = parseJsonForSMS(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getFirstMedia)
                    {
                        strReturn = parseJsonForGetFirstMedia(jsonObject);
                    }else if(msg.what == RequestReturnCode.REQ_getMediaInfo)
                    {
                        strReturn = parseJsonForGetMediaInfo(jsonObject);
                    }
                    else if(msg.what == RequestReturnCode.REQ_getMediaInfo2)
                    {
                        strReturn = parseJsonForGetMediaInfo2(jsonObject);
                    } else if (msg.what == RequestReturnCode.REQ_loginWithPhone) {
                        strReturn = parseJsonForLoginAction(jsonObject);
                    }
                    else if (msg.what == RequestReturnCode.REQ_WeixinLogin)
                    {
                        strReturn = parseJsonForWeixinAction(jsonObject);
                    }
                    else if (msg.what == RequestReturnCode.REQ_getConfig)
                    {
                        parseJsonForMainProblemAction(jsonObject);
                        //parseJsonForAboutUsAction(jsonObject);
                        //strReturn = parseJsonForCustomServiceAction(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getSharePointValue)
                    {
                        parseJsonForAboutUsAction(jsonObject);
                    }
                    else if (msg.what == RequestReturnCode.REQ_getTutorial)
                    {
                    }
                    else if (msg.what == RequestReturnCode.REQ_getTos)
                    {
                        strReturn = parseJsonForServiceAgreementAction(jsonObject);
                    }
					else if (msg.what == RequestReturnCode.REQ_getLearnList)
                    {
                        strReturn = parseJsonForLearnRecordAction(jsonObject);

                    }
                    else if (msg.what == RequestReturnCode.REQ_getPointHistory)
                    {
                        strReturn = parseJsonForMyPointAction(jsonObject);
                    }
                    else if (msg.what == RequestReturnCode.REQ_setProfile) {
                        strReturn = parseJsonForSetProfile(jsonObject);
                    }
                    else if (msg.what == RequestReturnCode.REQ_getEventList) {
                        strReturn = parseJsonForActCenterAction(jsonObject);
                    }
                    else if (msg.what == RequestReturnCode.REQ_getEventContent) {
                        strReturn = parseJsonForActDetailAction(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getAvailableLessons) {
                        strReturn = parseJsonForAvailableLessons(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getSystemTestUrl) {
                        strReturn = parseJsonForSystemTestUrl(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getLevelTestUrl) {
                        strReturn = parseJsonForLevelTestUrl(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getLevelList) {
                        strReturn = parseJsonForLevelList(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getQuizTestResult) {
                        strReturn = parseJsonForGetQuizTestResult(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_getLessonURL) {
                        strReturn = parseJsonForLessonURL(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_saveQuizTestResult) {
                        strReturn = parseJsonForSaveQuizTestRslt(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_saveLevel) {
                        strReturn = parseJsonForSaveLevel(jsonObject);
                    }else if (msg.what == RequestReturnCode.REQ_picUploadResult) {
                        strReturn = parseJsonForpicUpload(jsonObject);
                    } else if (msg.what == RequestReturnCode.REQ_sendReportResult) {
                        strReturn = parseJsonForSendReportAction( jsonObject);
                    } else if (msg.what == RequestReturnCode.REQ_saveReadNotify ) {
                        strReturn = parseJsonForSaveReadNotify( jsonObject);
                    } else if (msg.what == RequestReturnCode.REQ_saveShareContent ) {
                        strReturn = parseJsonSaveShareHistory( jsonObject);
                    } else if (msg.what == RequestReturnCode.REQ_savePayment ) {
                        strReturn = parseJsonSavePayment( jsonObject);
                    } else if (msg.what == RequestReturnCode.REQ_getPurchaseLevels ) {
                        strReturn = parseJsonGetPurchaseLevels( jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getMainArticleList ) {
                        strReturn = parseJsonGetMainArticleList(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getArticleType1) {
                        strReturn = parseJsonGetArticleType(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getArticleDetail) {
                        strReturn = parseJsonGetArticleDetail(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getHotArticleType) {
                        strReturn = parseJsonGetHotArticleType(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getGuideBanner){
                        strReturn = parseJsonGetBannerType(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getOrderInfo){
                        strReturn = parseJsongetOrderInfo(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getToken){
                        strReturn = parseJsonGetToken(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getRemoveUser) {
                        strReturn = parseJsonRemoveUser(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_changePassword) {
                        strReturn = parseChangePassword(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_setWechatBind){
                        strReturn = parseJsonForsetWechatBind(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_resetWechatBind){
                        strReturn = parseJsonForresetWechatBind(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getAppVersion) {
                        strReturn = parseJsonForgetAppVersion(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_requestTrainCamp) {
                        strReturn = parseJsonForrequestTrainCamp(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_saveOralResult) {
                        strReturn = parseJsonForsaveOralResult(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getOralLearnInfo) {
                        strReturn = parseJsonForgetOralLearnInfo(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getForeignTeacherVideo) {
                        strReturn = parseJsonForeignTeacherVideo(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getAppData) {
                        strReturn = parseJsongetAppData(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_getShareInfo) {
                        strReturn = parseJsonForgetShareInfo(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_setLoginMode) {
                        strReturn = parseJsonForsetLoginMode(jsonObject);
                    } else if(msg.what == RequestReturnCode.REQ_netErrorLog) {
                        strReturn = parseJsonFornetErrorLog(jsonObject);
                    }

                    mfNetworkEnable = true;

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    mfNetworkEnable = false;
                    strReturn = "Exception!";
                }

                //  0803
                if (strReturn != null && strReturn.equals("") == false )
                    MessageAlert.showMessage( MyApplication.getInstance().getCurrentActivity(), strReturn );

            }
        }
    };

    /**
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private String parseJsonForHardwareType(JSONObject jsonObject) throws JSONException {
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")){
            String strInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strInfo);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    DeviceInfo devInf = new DeviceInfo(jsonItem.getInt("Id"), jsonItem.getString("type"));
                    mLoginUserInf.mlistDevice.add(devInf);
                }
            }
        }
        return "";
    }
    private String parseJsonForrequestTrainCamp(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")){
            MyApplication.mNetProc.mLoginUserInf.misTrainCamp = 1;
//            MainActivity.mainActivityInstance.refreshStateTrainCampButton();
//            return "恭喜您报名成功！\n" +
//                    "我们会尽快与您联系！";
        }
       return jsonObject.getString("errmsg");
    }
    /**
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private  String parseJsonForPayConf(JSONObject jsonObject) throws JSONException {
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            String strInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strInfo);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonLevel = jsonArray.getJSONObject(i);
                    PayConf payConf = new PayConf(i, jsonLevel.getInt("type"), jsonLevel.getInt("count"), jsonLevel.getInt("origin_price"), jsonLevel.getInt("discount_price"));

                    mLoginUserInf.mlistPayConf.add(payConf);
                }
            }
        }
        return "";
    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private String parseJsonForSMS(JSONObject jsonObject) throws JSONException {
        String strErrMsg = null;
        String strVerifyCode = null;
        int nState = jsonObject.getInt("errcode");
        strErrMsg = jsonObject.getString("errmsg");
        switch (nState){
            case 0:
                strVerifyCode = jsonObject.getString("code");

                break;
            case 1001:
            case 1002:

                break;
        }
        switch (mbSmsType) {
            case Constants.SMS_TYPE_LOGIN:
                ((AuroraLogin) MyApplication.getInstance().getCurrentActivity()).finishSMS(nState, strVerifyCode);
                break;
            case Constants.SMS_TYPE_SINGUP:
                ((SignupActivity) MyApplication.getInstance().getCurrentActivity()).finishSMS(nState, strVerifyCode);
                break;
            case Constants.SMS_TYPE_USER_DELETE:
                mLoginUserInf.mDeleteUserConfrimCode = new String(Base64.decode(strVerifyCode.getBytes(), Base64.DEFAULT));
                break;
            case Constants.SMS_TYPE_PASSWORD_RESET:
                ((PasswordReset) MyApplication.getInstance().getCurrentActivity()).finishSMS(nState, strVerifyCode);
                break;
            case Constants.SMS_TYPE_WECHAT_BIND:
                ((BindActivity) MyApplication.getInstance().getCurrentActivity()).finishSMS(nState, strVerifyCode);
            case Constants.SMS_TYPE_PATTERN_VERIFY:
                ((SwitchPatternActivity) MyApplication.getInstance().getCurrentActivity()).finishSMS(nState, strVerifyCode);
                break;
        }
        if(nState == 0)
            return "验证码已发送";
        else
            return strErrMsg;
    }
    private String parseJsonForGetMediaInfo(JSONObject jsonObject) throws JSONException {
        String strErrMsg = null;
        String strVerifyCode = null;
        int nState = jsonObject.getInt("errcode");
        switch (nState){
            case 0:
                String strHome = jsonObject.getString("home");
                JSONObject jsonHome = new JSONObject(strHome);
                String strMedia = jsonHome.getString("video");
                JSONArray jsonArray = new JSONArray(strMedia);
                mLoginUserInf.mlistMedia.clear();
                if (jsonArray != null && jsonArray.length() > 0 ) {
                    for (int i = 0 ; i < jsonArray.length() ; i ++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        MediaItem item = new MediaItem(i, jsonItem.getInt("ismain"), jsonItem.getString("title"), jsonItem.getString("image"), jsonItem.getString("file"),
                                jsonItem.getString("duration"), jsonItem.getString("filesize"), jsonItem.getString("desc"), jsonItem.getString("created"), jsonItem.getInt("location"));
                        mLoginUserInf.mlistMedia.add(item);
                    }
                }
                mLoginUserInf.mlistbanner.clear();
                String strBanner = jsonHome.getString("banner");
                JSONObject jsonBanner = new JSONObject(strBanner);
                String strBannerOut = jsonBanner.getString("banner_logout");
                JSONArray jsonArraybannerout = new JSONArray(strBannerOut);
                if (jsonArraybannerout != null && jsonArraybannerout.length() > 0 ) {
                    for (int i = 0 ; i < jsonArraybannerout.length() ; i ++) {
                        JSONObject jsonbannerItem = jsonArraybannerout.getJSONObject(i);
                        ImageObj item = new ImageObj(i, null,  jsonbannerItem.getString("image_link"), jsonbannerItem.getString("link"),jsonbannerItem.getInt("ordernum"),jsonbannerItem.getString("bg_color"),jsonbannerItem.getString("font_color"), jsonbannerItem.getInt("usertype"));
                        mLoginUserInf.mlistbanner.add(item);

                    }
                }
                ///Oral_Practice///
                String strOralCamp = jsonObject.getString("oral_practice");
                JSONObject jsonOral = new JSONObject(strOralCamp);

                mLoginUserInf.mlist_oralPracticeBanner.clear();
                String strOralCampBanner = jsonOral.getString("banner");
                JSONObject jsonOralBanner = new JSONObject(strOralCampBanner);
                String strOralBanner_Out = jsonOralBanner.getString("banner_logout");
                JSONArray jsonArrayOralbanner_out = new JSONArray(strOralBanner_Out);
                if (jsonArrayOralbanner_out != null && jsonArrayOralbanner_out.length() > 0 ) {
                    for (int i = 0 ; i < jsonArrayOralbanner_out.length() ; i ++) {
                        JSONObject jsonbannerItem = jsonArrayOralbanner_out.getJSONObject(i);
                        ImageObj item = new ImageObj(i, null,  jsonbannerItem.getString("image_link"), jsonbannerItem.getString("link"),jsonbannerItem.getInt("ordernum"),jsonbannerItem.getString("bg_color"),jsonbannerItem.getString("font_color"), jsonbannerItem.getInt("usertype"));
                        mLoginUserInf.mlist_oralPracticeBanner.add(item);

                    }
                }
                break;
            case 1001:
            case 1002:
                strErrMsg = jsonObject.getString("errmsg");
                break;
        }

        return "";
    }

    private String parseJsonForGetMediaInfo2(JSONObject jsonObject) throws JSONException {
        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");
        Log.d("parseJsonForLoginAction", jsonObject.toString());
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            //home_video
            String strHome = jsonObject.getString("home");
            JSONObject jsonHome = new JSONObject(strHome);
            String strMedia = jsonHome.getString("video");
            JSONArray jsonhomevideoArray = new JSONArray(strMedia);
            mLoginUserInf.mlistMedia.clear();
            if (jsonhomevideoArray != null && jsonhomevideoArray.length() > 0) {
                for (int i = 0; i < jsonhomevideoArray.length(); i++) {
                    JSONObject jsonItem = jsonhomevideoArray.getJSONObject(i);
                    MediaItem item = new MediaItem(i, jsonItem.getInt("ismain"), jsonItem.getString("title"), jsonItem.getString("image"), jsonItem.getString("file"),
                            jsonItem.getString("duration"), jsonItem.getString("filesize"), jsonItem.getString("desc"), jsonItem.getString("created"), jsonItem.getInt("location"));
                    mLoginUserInf.mlistMedia.add(item);
                }
            }
            //banner
            mLoginUserInf.mlistbanner.clear();
            String strBanner = jsonHome.getString("banner");
            JSONArray jsonArraybannerout = new JSONArray(strBanner);
            if (jsonArraybannerout != null && jsonArraybannerout.length() > 0) {
                for (int i = 0; i < jsonArraybannerout.length(); i++) {
                    JSONObject jsonbannerItem = jsonArraybannerout.getJSONObject(i);
                    ImageObj item = new ImageObj(i, null, jsonbannerItem.getString("image_link"), jsonbannerItem.getString("link"), jsonbannerItem.getInt("ordernum"), jsonbannerItem.getString("bg_color"), jsonbannerItem.getString("font_color"), jsonbannerItem.getInt("usertype"));
                    mLoginUserInf.mlistbanner.add(item);

                }
            }

            ///Oral_Practice///
            mLoginUserInf.mlist_oralPracticeBanner.clear();

            String stroral = jsonObject.getString("oral_practice");
            JSONObject oralObj = new JSONObject(stroral);

            JSONArray jsonArraybanner = new JSONArray(oralObj.getString("banner"));
            if (jsonArraybanner != null && jsonArraybanner.length() > 0) {
                for (int i = 0; i < jsonArraybanner.length(); i++) {
                    JSONObject jsonbannerItem = jsonArraybanner.getJSONObject(i);
                    ImageObj item = new ImageObj(i, null, jsonbannerItem.getString("image_link"), jsonbannerItem.getString("link"), jsonbannerItem.getInt("ordernum"), jsonbannerItem.getString("bg_color"), jsonbannerItem.getString("font_color"), jsonbannerItem.getInt("usertype"));
                    mLoginUserInf.mlist_oralPracticeBanner.add(item);

                }
            }
            if(State.getCurrentPattern() == pattern.parent.getValue())
                MainActivity.mainActivityInstance.refreshMediaInfo();
            else
                StudentPatternActivity.instance.refreshBanner();
        }
        return "";
    }

    private String parseJsonForGetFirstMedia(JSONObject jsonObject) throws JSONException {
        String strErrMsg = null;
        String strVerifyCode = null;
        int nState = jsonObject.getInt("errcode");
        switch (nState){
            case 0:
                String strMedia = jsonObject.getString("video");
                JSONArray jsonArray = new JSONArray(strMedia);
                mLoginUserInf.mlistMedia.clear();
                if (jsonArray != null && jsonArray.length() > 0 ) {
                    for (int i = 0 ; i < jsonArray.length() ; i ++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        MediaItem item = new MediaItem(i, jsonItem.getInt("ismain"), jsonItem.getString("title"), jsonItem.getString("image"), jsonItem.getString("file"),
                            jsonItem.getString("duration"), jsonItem.getString("filesize"), jsonItem.getString("desc"), jsonItem.getString("created"), jsonItem.getInt("location"));
                        mLoginUserInf.mlistMedia.add(item);

                    }
                }

                break;
            case 1001:
            case 1002:
                strErrMsg = jsonObject.getString("errmsg");
                break;
        }

        return "";
    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private String parseJsonForLoginAction(JSONObject jsonObject) throws JSONException, IOException {

        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");
        Log.d("parseJsonForLoginAction", jsonObject.toString());
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {

            int _difference = DifferenceTimestamp(jsonObject.getLong("timestamp"));
            if(_difference == 1)
            {
                return "错误";
            }

            mLoginUserInf.muserType = jsonObject.getInt("userType");
            mLoginUserInf.muserType2 = jsonObject.getInt("userType2");
            //profile
            String strProfile = jsonObject.getString("profile");
            JSONObject jsonProfile = new JSONObject(strProfile);
            mLoginUserInf.mstrName = jsonProfile.getString("name");
            mLoginUserInf.muserPhoneNumber = jsonProfile.getString("phone");

            String phonenumber = MyApplication.getInstance().getPhoneNumberFromPref();
            if(phonenumber == "")
            {
                phonenumber = MyApplication.getInstance().strPhoneNumber;
            }

            if(!mLoginUserInf.muserPhoneNumber.equals(phonenumber))
            {
                return "错误";
            }

            mLoginUserInf.mstrNickName = jsonProfile.getString("nickname");
            mLoginUserInf.mstrUserId = jsonProfile.getString("Id");
            mLoginUserInf.mnbase64UserID = jsonProfile.getString("Id");
            mLoginUserInf.mnSelectedLevel = jsonProfile.getInt("selected_level");
            mLoginUserInf.mstrBirthday = jsonProfile.getString("birthday");
            mLoginUserInf.mstrStudydgree = jsonProfile.getString("study_degree");
            mLoginUserInf.mstrRoleID = jsonProfile.getString("role_id");
            mLoginUserInf.mstrGender = jsonProfile.getString("gender");
            mLoginUserInf.mstrPic = jsonProfile.getString("pic");
            mLoginUserInf.mCreatedTime = jsonProfile.getString("created");
            mLoginUserInf.mAvailableDays = jsonProfile.getInt("availdays");
            mLoginUserInf.bExperience = jsonProfile.getInt("bExperience");
            mLoginUserInf.dExperience = jsonProfile.getString("experience_date");
            mLoginUserInf.pExperience = jsonProfile.getInt("experiencedays");
            mLoginUserInf.mbWechatBind = jsonProfile.getInt("wechatbind");
            mLoginUserInf.mnStudyDays = jsonProfile.getInt("study_cnt");
            mLoginUserInf.mbPassword = jsonProfile.getInt("password");
            mLoginUserInf.mdFirstLogin = jsonProfile.getString("firstlogin_date");
            mLoginUserInf.mstrActive_dev_id = jsonObject.getString("active_dev_id");
            mLoginUserInf.misNew = Integer.parseInt(jsonObject.getString("isNewUser"));
            mLoginUserInf.misTrainCamp = Integer.parseInt(jsonObject.getString("isTrainCamp"));
            mLoginUserInf.myTotalPoint = jsonProfile.getInt("point");
            mLoginUserInf.mnIsActive = jsonProfile.getInt("isactive");
            Log.d("isactive", String.valueOf(jsonProfile.getInt("isactive")));
            //notify
            String strNotify = jsonObject.getString("notify");
            JSONObject jsonNotify = new JSONObject(strNotify);
            String strUnread = jsonNotify.getString("unread");
            JSONObject jsonUnread = new JSONObject(strUnread);
            mLoginUserInf.mnUnreadTotalCount = jsonUnread.getInt("totalUnread");
            mLoginUserInf.mnUnreadPrivateCount = jsonUnread.getInt("privateUnread");
            mLoginUserInf.mnUnreadBroadCount = jsonUnread.getInt("broadUnread");

            mLoginUserInf.mlistBroadMessage.clear();
            String strBroadNotify = jsonNotify.getString("broadNotify");
            JSONArray jsonArray = new JSONArray(strBroadNotify);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    String strDate = jsonItem.getString("created");
                    String strMonth, strDay;
                    strMonth = strDate.substring(5, 7);
                    strDay = strDate.substring(8, 10);
                    strDate = strMonth + "/" + strDay;

                    MessageItem item = new MessageItem(jsonItem.getInt("Id"), 0, jsonItem.getString("title"),
                            jsonItem.getString("content"), strDate, jsonItem.getInt("isread"), 0);
                    mLoginUserInf.mlistBroadMessage.add(item);
                }
            }

            mLoginUserInf.mlistPrivateMessage.clear();
            String strPrivateNotify = jsonNotify.getString("privateNotify");
            jsonArray = new JSONArray(strPrivateNotify);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    String strDate = jsonItem.getString("created");
                    String strMonth, strDay;
                    strMonth = strDate.substring(5, 7);
                    strDay = strDate.substring(8, 10);
                    strDate = strMonth + "/" + strDay;
                    MessageItem item = new MessageItem(jsonItem.getInt("Id"), jsonItem.getInt("user_id"), jsonItem.getString("title"),
                            jsonItem.getString("content"), strDate, jsonItem.getInt("isread"), 0);
                    mLoginUserInf.mlistPrivateMessage.add(item);
                }
            }

            mLoginUserInf.mlistProgress.clear();
            String strProgress = jsonObject.getString("progress");
            jsonArray = new JSONArray(strProgress);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);

                    ProgressItem item = new ProgressItem(jsonItem.getInt("level_id"), jsonItem.getString("begin_time"), jsonItem.getString("end_time"),
                            jsonItem.getInt("lesson_id"), jsonItem.getInt("part_id"), jsonItem.getInt("cur_lesson_id"), jsonItem.getInt("cur_part_id"), jsonItem.getInt("levelType"), jsonItem.getInt("o_lesson_id"),jsonItem.getInt("o_part_id"),jsonItem.getInt("o_cur_lesson_id"),jsonItem.getInt("o_cur_part_id"));
                    mLoginUserInf.mlistProgress.add(item);
                }
            }
            //pattern
            current_pattern = jsonProfile.getInt("login_mode");

            //Zhuge

            //定义用户属性
            JSONObject personObject = new JSONObject();
            personObject.put("avatar", mLoginUserInf.mstrPic);
            personObject.put("name", mLoginUserInf.mstrName);
            personObject.put("phone", mLoginUserInf.muserPhoneNumber);

            //标识用户
            //ZhugeSDK.getInstance().identify(MyApplication.getInstance().getCurrentActivity(), mLoginUserInf.mstrUserId, personObject);

            if (mloginType == Constants.LOGIN_TYPE_LOGIN) { //login
                if(Constants.SINGIN_TYPE_FORGOTPASS == mbSigninType)
                {
                    ((PasswordReset)MyApplication.getInstance().getCurrentActivity()).GotoChangePassword();
                    return "";
                }
                MyApplication.getInstance().loginOk();

            } else if (mloginType == Constants.LOGIN_TYPE_SIGNUP)//signup
            {
                MyApplication.getInstance().signupOK();

            } else if (mloginType == Constants.LOGIN_TYPE_WECHATBIND) //wechat Bind
            {
                setWechatNind(0, "setWechatBind", "userid=" + mLoginUserInf.mstrUserId + "&openid=" + mLoginUserInf.mstrOpenID + "&active_dev_id=" + mLoginUserInf.mstrActive_dev_id);

                MyApplication.mNetProc.mLoginUserInf.mbWechatAuroraSind = false;

                MyApplication.getInstance().loginOk();
            }
            return "";
        } else if (mState.equals("1009")) {

            MessageAlert.showMessage(MyApplication.getInstance().getCurrentActivity(), jsonObject.getString("errmsg"));
            if (mloginType == Constants.LOGIN_TYPE_LOGIN) {
                //Zhuge
                JSONObject eventObject = new JSONObject();
                eventObject.put(Constants.Zhuge_Property_Fail_Reason, Constants.Zhuge_Value_CallBack_Login_Fail_Pass);
                //ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_CallBack_Login_Fail, eventObject);
            } else if (mloginType == Constants.LOGIN_TYPE_SIGNUP) {
                //Zhuge
                JSONObject eventObject = new JSONObject();
                eventObject.put(Constants.Zhuge_Property_Fail_Reason, Constants.Zhuge_Value_CallBack_Signup_Fail_Pass);
                //ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_CallBack_Signup_Fail, eventObject);
            }

        } else if(mState.equals("1008")) { // not register
            if(mbSigninType == Constants.SINGIN_TYPE_PASS)
                ((AuroraLogin)MyApplication.getInstance().getCurrentActivity()).DoLoginForceSmsMethod();
            else
                MainActivity.mainActivityInstance.onNextSignupStep();
            return "";
        }

        return mErrMsg;
    }

    private String parseJsonForWeixinAction(JSONObject jsonObject) throws JSONException, IOException {
        Log.d("wechatloginffff", jsonObject.toString());
        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");

        if (mState == null)
            return "错误";

        if(mState.equals("1007")) {
            //MyApplication.mNetProc.mLoginUserInf.mbWechatAuroraSind = true;
            if(MyApplication.getInstance().getCurrentActivity().getClass().getName().contains("Main")) {
                ((MainActivity) MyApplication.getInstance().getCurrentActivity()).loginAuth(false);
                return "当前账号还未绑定微信，无法通过微信账号登录！";
            }
            else{
                ((AuroraLogin) MyApplication.getInstance().getCurrentActivity()).DoHideWechatLogin();
                return "当前账号还未绑定微信，无法通过微信账号登录！";
            }

        }

        if (mState.equals("0"))
        {
            int _difference = DifferenceTimestamp(jsonObject.getLong("timestamp"));
            if(_difference == 1)
            {
                return "错误";
            }

            mLoginUserInf.muserType=jsonObject.getInt("userType");
            mLoginUserInf.muserType2=jsonObject.getInt("userType2");
            //profile
            String strProfile = jsonObject.getString("profile");
            JSONObject jsonProfile = new JSONObject(strProfile);
            mLoginUserInf.mstrName = jsonProfile.getString("name");
            mLoginUserInf.muserPhoneNumber = jsonProfile.getString("phone");
            mLoginUserInf.mstrNickName = jsonProfile.getString("nickname");
            mLoginUserInf.mstrUserId = jsonProfile.getString("Id");
            mLoginUserInf.mnbase64UserID = jsonProfile.getString("Id");
            mLoginUserInf.mnSelectedLevel=jsonProfile.getInt("selected_level");
            mLoginUserInf.mstrBirthday = jsonProfile.getString("birthday");
            mLoginUserInf.mstrStudydgree = jsonProfile.getString("study_degree");
            mLoginUserInf.mstrRoleID = jsonProfile.getString("role_id");
            mLoginUserInf.mstrGender = jsonProfile.getString("gender");
            mLoginUserInf.mstrPic =jsonProfile.getString("pic");
            mLoginUserInf.mCreatedTime=jsonProfile.getString("created");
            mLoginUserInf.mAvailableDays=jsonProfile.getInt("availdays");
            mLoginUserInf.bExperience=jsonProfile.getInt("bExperience");
            mLoginUserInf.mnStudyDays = jsonProfile.getInt("study_cnt");
            mLoginUserInf.dExperience=jsonProfile.getString("experience_date");
            mLoginUserInf.pExperience=jsonProfile.getInt("experiencedays");
            mLoginUserInf.mbWechatBind = jsonProfile.getInt("wechatbind");
            mLoginUserInf.mbPassword = jsonProfile.getInt("password");
            mLoginUserInf.mdFirstLogin = jsonProfile.getString("firstlogin_date");
            mLoginUserInf.mstrActive_dev_id =jsonObject.getString("active_dev_id");
            mLoginUserInf.misNew =Integer.parseInt(jsonObject.getString("isNewUser"));
            mLoginUserInf.misTrainCamp = Integer.parseInt(jsonObject.getString("isTrainCamp"));
            mLoginUserInf.myTotalPoint=jsonProfile.getInt("point");
            mLoginUserInf.mnIsActive = jsonProfile.getInt("isactive");

            //notify
            String strNotify = jsonObject.getString("notify");
            JSONObject jsonNotify = new JSONObject(strNotify);
            String strUnread = jsonNotify.getString("unread");
            JSONObject jsonUnread = new JSONObject(strUnread);
            mLoginUserInf.mnUnreadTotalCount = jsonUnread.getInt("totalUnread");
            mLoginUserInf.mnUnreadPrivateCount = jsonUnread.getInt("privateUnread");
            mLoginUserInf.mnUnreadBroadCount = jsonUnread.getInt("broadUnread");

            mLoginUserInf.mlistBroadMessage.clear();
            String strBroadNotify = jsonNotify.getString("broadNotify");
            JSONArray jsonArray = new JSONArray(strBroadNotify);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    String strDate = jsonItem.getString("created");
                    String strMonth, strDay;
                    strMonth = strDate.substring(5, 7);
                    strDay = strDate.substring(8, 10);
                    strDate = strMonth + "/" + strDay;

                    MessageItem item = new MessageItem(jsonItem.getInt("Id"), 0, jsonItem.getString("title"),
                            jsonItem.getString("content"), strDate, jsonItem.getInt("isread"),0);
                    mLoginUserInf.mlistBroadMessage.add(item);
                }
            }

            mLoginUserInf.mlistPrivateMessage.clear();
            String strPrivateNotify = jsonNotify.getString("privateNotify");
            jsonArray = new JSONArray(strPrivateNotify);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    String strDate = jsonItem.getString("created");
                    String strMonth, strDay;
                    strMonth = strDate.substring(5, 7);
                    strDay = strDate.substring(8, 10);
                    strDate = strMonth + "/" + strDay;
                    MessageItem item = new MessageItem(jsonItem.getInt("Id"), jsonItem.getInt("user_id"), jsonItem.getString("title"),
                            jsonItem.getString("content"), strDate, jsonItem.getInt("isread"), 0);
                    mLoginUserInf.mlistPrivateMessage.add(item);
                }
            }

            mLoginUserInf.mlistProgress.clear();
            String strProgress = jsonObject.getString("progress");
            jsonArray = new JSONArray(strProgress);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);

                    ProgressItem item = new ProgressItem(jsonItem.getInt("level_id"), jsonItem.getString("begin_time"), jsonItem.getString("end_time"),
                            jsonItem.getInt("lesson_id"), jsonItem.getInt("part_id"), jsonItem.getInt("cur_lesson_id"), jsonItem.getInt("cur_part_id"), jsonItem.getInt("levelType"), jsonItem.getInt("o_lesson_id"),jsonItem.getInt("o_part_id"),jsonItem.getInt("o_cur_lesson_id"),jsonItem.getInt("o_cur_part_id"));
                    mLoginUserInf.mlistProgress.add(item);
                }
            }
            //pattern
            current_pattern = jsonProfile.getInt("login_mode");
            MyApplication.getInstance().loginOk();
            JSONObject eventObject = new JSONObject();
            eventObject.put(Constants.Zhuge_Property_Login_Method, Constants.Zhuge_Value_CallBack_Login_Success_Wechat);
            //ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_CallBack_Login_Success,  eventObject);
            return "";
        } else {

            return mErrMsg;

        }

    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
	private String parseJsonForAboutUsAction(JSONObject jsonObject) throws JSONException {
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strConfig = jsonObject.getString("info");
            JSONObject jsonConfig = new JSONObject(strConfig);
            mLoginUserInf.mstrAppVersion = jsonConfig.getString("android_version");
            mLoginUserInf.mstrCompany = jsonConfig.getString("company");
            mLoginUserInf.mSharePointValue = jsonConfig.getInt("point_for_share");
        }
        return "";
    }

    private String parseJsonForSharepointValue(JSONObject jsonObject) throws JSONException {
        String mState = jsonObject.getString("errcode");

        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strConfig = jsonObject.getString("info");
            JSONObject jsonConfig = new JSONObject(strConfig);
            mLoginUserInf.mstrAppVersion = jsonConfig.getString("android_version");
            mLoginUserInf.mstrCompany = jsonConfig.getString("company");
        }
        return "";
    }

    private String parseJsonForCustomServiceAction(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strConfig = jsonObject.getString("info");
            JSONObject jsonConfig = new JSONObject(strConfig);
            mLoginUserInf.mstrCustomerService = jsonConfig.getString("customer_service");
        }
        return "";
    }
    private String parseJsonForMainProblemAction(JSONObject jsonObject) throws  JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strConfig = jsonObject.getString("info");
            Intent detailInt = new Intent(MyApplication.getInstance().getCurrentActivity().getApplicationContext(),  BannerDetail.class);
            detailInt.putExtra("bannerlink", strConfig);
            detailInt.putExtra("backgroundcolor", "");
            detailInt.putExtra("fontcolor", "");

            MyApplication.getInstance().getCurrentActivity().startActivity(detailInt);

        }

	    return "";
    }
    private String parseJsonForServiceAgreementAction(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strTos = jsonObject.getString("info");
            mLoginUserInf.mstrTos =strTos;
        }
        return "";
    }
    private String parseJsonForLearnRecordAction(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";

        if (mState.equals("0"))
        {
            mLoginUserInf.mlistLearningRecord.clear();
            String strLearnrecordList = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strLearnrecordList);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = jsonArray.length() - 1 ; i >= 0 ; i --) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    LearningRecordItem item = new LearningRecordItem( jsonItem.getString("comment"),jsonItem.getString("part_id"),jsonItem.getString("score") , jsonItem.getString("scoreDate"), jsonItem.getString("level_id"), jsonItem.getString("lesson_id"), jsonItem.getInt("kind"), jsonItem.getInt("Id"));
                    mLoginUserInf.mlistLearningRecord.add(item);
                }
            }
            if(MyApplication.getInstance().getCurrentActivity().getLocalClassName().contains("LearningRecordActivity"))
                ((LearningRecordActivity)MyApplication.getInstance().getCurrentActivity()).refreshAllInformation();
        }
        return "";
    }
    private String parseJsonForMyPointAction(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";

        if (mState.equals("0"))
        {
            mLoginUserInf.mlistMyPoint.clear();
            String strLearnrecordList = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strLearnrecordList);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = jsonArray.length() - 1 ; i >= 0 ; i --) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    MyPointItem item = new MyPointItem( jsonItem.getString("comment"),jsonItem.getInt("point"), jsonItem.getString("date"));
                    mLoginUserInf.mlistMyPoint.add(item);
                }
            }

            if(MyApplication.getInstance().getCurrentActivity().getLocalClassName().contains("MyPointActivity"))
                ((MyPointActivity)MyApplication.getInstance().getCurrentActivity()).refreshAllInformation();
        }
        return "";
    }
    private String parseJsonForActCenterAction(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";

        if (mState.equals("0"))
        {
            mLoginUserInf.mlistActCenter.clear();
            String strActCenterList = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strActCenterList);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    ActCenterItem item = new ActCenterItem( jsonItem.getString("title"),jsonItem.getInt("Id"), jsonItem.getString("created"));
                    mLoginUserInf.mlistActCenter.add(item);
                }
            }
//            ((ActCenterActivity)MyApplication.getInstance().getCurrentActivity()).refreshAllInformation();
        }
        return "";
    }
    private String parseJsonForActDetailAction(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strActCenterDetail = jsonObject.getString("info");
            mLoginUserInf.mstrActDetail = strActCenterDetail;

        }
        return "";
    }

    /**
     * Get Trial Lessong
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private  String parseJsonForAvailableLessons(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strInfo);
            mLoginUserInf.mlistAvailableLessons.clear();
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonLessonItem = jsonArray.getJSONObject(i);
                    String strParts = jsonLessonItem.getString("parts");
                    JSONArray jsonPartsArray = new JSONArray(strParts);
                    ArrayList<PartItem> listParts = new ArrayList<PartItem>();
                    if (jsonPartsArray != null && jsonPartsArray.length() > 0 ) {
                        for (int j = 0; j < jsonPartsArray.length(); j++) {
                            JSONObject jsonPart = jsonPartsArray.getJSONObject(j);
                            PartItem part = new PartItem(j, jsonPart.getInt("part_id"), jsonPart.getString("url"));
                            listParts.add(part);
                        }
                    }
                    LessonItem lesson = new LessonItem(jsonLessonItem.getInt("Id"), jsonLessonItem.getInt("level_id"), jsonLessonItem.getInt("lesson_id"),
                            listParts);
                    mLoginUserInf.mlistAvailableLessons.add(lesson);
                }
            }
        }
        return "";
    }

    private  String parseJsonForSystemTestUrl(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strInfo);
            mLoginUserInf.mlistSystemTestUrl.clear();
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonTest = jsonArray.getJSONObject(i);
                    TestItem testItem = new TestItem(jsonTest.getInt("Id"), jsonTest.getInt("level"), jsonTest.getInt("lesson_id"),
                            jsonTest.getInt("part_id"), jsonTest.getString("path"));
                    mLoginUserInf.mlistSystemTestUrl.add(testItem);
                }
            }
        }
        return "";
    }

    private String parseJsonForgetAppVersion(JSONObject jsonObject) throws JSONException {
        String mState = jsonObject.getString("errcode");
        Log.d("JsonForgetAppVersion", jsonObject.toString());
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            //app version
            String strVersion = jsonObject.getString("info");
            JSONObject jsonVersion = new JSONObject(strVersion);
            String strAppVersion = jsonVersion.getString("android");
            JSONArray jsonAppVersionArray = new JSONArray(strAppVersion);
            JSONObject jsonAppVersion = jsonAppVersionArray.getJSONObject(0);
            mLoginUserInf.versionInfo.setParam(jsonAppVersion.getString("name"), jsonAppVersion.getString("version"), jsonAppVersion.getInt("status"),jsonAppVersion.getString("link"),jsonAppVersion.getString("apk_file"), jsonAppVersion.getString("created"), jsonAppVersion.getString("modified"), jsonAppVersion.getString("content"));
        }
        return "";
    }
    private String parseJsonForLevelTestUrl(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strInfo);
            mLoginUserInf.mlistLevelTestUrl.clear();
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonTest = jsonArray.getJSONObject(i);
                    TestItem testItem = new TestItem(jsonTest.getInt("Id"), jsonTest.getInt("level"), jsonTest.getInt("lesson_id"),
                            jsonTest.getInt("part_id"), jsonTest.getString("path"));
                    mLoginUserInf.mlistLevelTestUrl.add(testItem);
                }
            }

            ((LearnActivity)MyApplication.getInstance().getCurrentActivity()).refreshLevelTest();
            return "";
        }

        return mErrMsg;
    }

    private String parseJsonForLevelList(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");

        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            int _difference = DifferenceTimestamp(jsonObject.getLong("timestamp"));
            if(_difference == 1)
            {
                return "错误";
            }

            mLoginUserInf.mlistProgress.clear();
            String strProgress = jsonObject.getString("progress");
            JSONArray jsonArrayProgress = new JSONArray(strProgress);
            if (jsonArrayProgress != null && jsonArrayProgress.length() > 0 ) {
                for (int i = 0 ; i < jsonArrayProgress.length() ; i ++) {
                    JSONObject jsonItem = jsonArrayProgress.getJSONObject(i);

                    ProgressItem item = new ProgressItem(jsonItem.getInt("level_id"), jsonItem.getString("begin_time"), jsonItem.getString("end_time"),
                            jsonItem.getInt("lesson_id"), jsonItem.getInt("part_id"), jsonItem.getInt("cur_lesson_id"), jsonItem.getInt("cur_part_id"), jsonItem.getInt("levelType"), jsonItem.getInt("o_lesson_id"),jsonItem.getInt("o_part_id"),jsonItem.getInt("o_cur_lesson_id"),jsonItem.getInt("o_cur_part_id"));
                    mLoginUserInf.mlistProgress.add(item);
                }
            }
            String strInfo = jsonObject.getString("info");
            mLoginUserInf.isLevelTestOPen = jsonObject.getInt("openLevelTest");
            JSONArray jsonArray = new JSONArray(strInfo);
            mLoginUserInf.mlistLevels.clear();
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonLevel = jsonArray.getJSONObject(i);
                    LevelItem level = new LevelItem(jsonLevel.getInt("Id"), jsonLevel.getString("level"),jsonLevel.getInt("status"),"", false);
                    mLoginUserInf.mlistLevels.add(level);
                }
                if(State.getCurrentPattern() == pattern.parent.getValue()) {
                    try {
                        MainActivity.mainActivityInstance.refreshLearnInformation();
                        MainActivity.mainActivityInstance.refreshProfileInformation();
                    }catch (NullPointerException e) {
                    }
                } else {
                    try {
                        StudentPatternActivity.instance.refreshCampList();
                    } catch (NullPointerException e) {}
                }
            }
        }
        return "";
    }

    private  String parseJsonForGetQuizTestResult(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strInfo);
            mLoginUserInf.mlistScores.clear();
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonLevel = jsonArray.getJSONObject(i);
                    ScoreItem scoreItem = new ScoreItem(jsonLevel.getInt("level_id"), jsonLevel.getInt("lesson_id"),
                            jsonLevel.getInt("score"), jsonLevel.getString("answer_time"));

                    mLoginUserInf.mlistScores.add(scoreItem);
                }
            }
            MyApplication.getInstance().getCurrentActivity().startActivity(new Intent(MyApplication.getInstance().getCurrentActivity(), mycampopoup.class));
        }
        return "";
    }

    private  String parseJsonForSetProfile(JSONObject jsonObject) throws JSONException, IOException {
        String mState = jsonObject.getString("errcode");

        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            String strProfile = jsonObject.getString("info");
            JSONObject jsonProfile = new JSONObject(strProfile);
            mLoginUserInf.mstrName = jsonProfile.getString("name");
            mLoginUserInf.mstrNickName = jsonProfile.getString("nickname");
            mLoginUserInf.mstrBirthday = jsonProfile.getString("birthday");
            mLoginUserInf.mstrGender = jsonProfile.getString("gender");

            if(State.getCurrentPattern() == pattern.parent.getValue()) {
                Intent mainActivity = new Intent(MyApplication.getInstance().getCurrentActivity(), MainActivity.class);
                MyApplication.getInstance().getCurrentActivity().startActivity(mainActivity);
                if (MyApplication.getInstance().getCurrentActivity().getClass().getName().contains("ModifyChild")) {
                    ((ModifyChildProfileActivity) MyApplication.getInstance().getCurrentActivity()).showToast();
                    MainActivity.mainActivityInstance.goFragmentProfile();
                    MainActivity.mainActivityInstance.refreshProfileInformation();
                    MainActivity.mainActivityInstance.mfragHome.refreshName();
                } else {
                    MainActivity.mainActivityInstance.goFragmentHome();
                    MainActivity.mainActivityInstance.refreshProfileInformation();
                    MainActivity.mainActivityInstance.mfragHome.refreshName();

                }
            } else {
                MyApplication.getInstance().getCurrentActivity().finish();
                StudentPatternActivity.instance.refreshNameAge();
            }

        }
        return "";
    }

    private  String parseJsonForLessonURL(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            mLoginUserInf.isShared = jsonObject.getString("shared");
            if (isLoadWebView) {
                mLoginUserInf.mstrLessonURL = jsonObject.getString("url");

                ((LearnActivity)MyApplication.getInstance().getCurrentActivity()).refreshWebView();
            } else{
                mLoginUserInf.mstrSpeakingLessonURL = jsonObject.getString("url");
                mLoginUserInf.mSentenceList.clear();
                JSONArray sArray = jsonObject.getJSONArray("result");
                for(int i = 0; i < sArray.length(); i++)
                    mLoginUserInf.mSentenceList.add(MakeJsonForSpeaking.convertSentenceFromJson(sArray.get(i).toString()));

                ((SpeakingActivity)MyApplication.getInstance().getCurrentActivity()).refreshWebView();
            }
            return "";
        } else
            return mErrMsg;
    }

    private  String parseJsonSaveShareHistory(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            MyApplication.mNetProc.mLoginUserInf.myTotalPoint = Integer.parseInt(jsonObject.getString("userPoint"));
            if(State.getCurrentPattern() == pattern.student.getValue())
                StudentPatternActivity.instance.refreshNameAge();
            return "";
        }else
            return mErrMsg;
    }
    private String parseJsonSavePayment(JSONObject jsonObject) throws JSONException{

        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            mLoginUserInf.myTotalPoint = jsonObject.getInt("point");
            mLoginUserInf.mnPlusPoint = jsonObject.getInt("plus_point");
            Intent intent = new Intent(MyApplication.getInstance().getCurrentActivity(), PurchaseSuccess.class);
            MyApplication.getInstance().getCurrentActivity().startActivity(intent);

            try {
                MainActivity.mainActivityInstance.refreshAllInformation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private  String parseJsonForSaveQuizTestRslt(JSONObject jsonObject) throws JSONException{

        String mState = jsonObject.getString("errcode");
        Log.d("leveltest","done api" +mState );
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            mLoginUserInf.mnPlusPoint = jsonObject.getInt("plusPoint");
            mLoginUserInf.mnUserPoint = jsonObject.getInt("userPoint");
            mLoginUserInf.myTotalPoint =  mLoginUserInf.mnUserPoint;
            mLoginUserInf.mstrStudydgree = jsonObject.getString("study_degree");

            if(State.getCurrentPattern() == pattern.student.getValue()) {
                StudentPatternActivity.instance.refreshNameAge();
                return "";
            }else
                MainActivity.mainActivityInstance.refreshProfileStudyDegree();

//            if(mnTestReusltType == Constants.TEST_RESULT_LESSON)
//                ((LearnActivity)MyApplication.getInstance().getCurrentActivity()).finishLesson();
//            else if(mnTestReusltType == Constants.TEST_RESULT_LEVELTEST)
//                ((LearnActivity)MyApplication.getInstance().getCurrentActivity()).finishLevelTest();
        }
        return "";
    }
    private  String parseJsonForSaveLevel(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {
            return "";
        }
        return "";
    }

    private  String parseJsonForSendReportAction(JSONObject jsonObject) throws JSONException{
        Log.d("SendReport", jsonObject.toString());
        String mState = jsonObject.getString("errcode");

        if (mState == null)
            return "错误";
        if (mState.equals("0"))
        {

        }
        return "提交成功！我们会尽快与您联系，谢谢";
    }
    private String parseJsonGetPurchaseLevels(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";

        if (mState.equals("0"))
        {
            int _difference = DifferenceTimestamp(jsonObject.getLong("timestamp"));
            if(_difference == 1)
            {
                return "错误";
            }

            mLoginUserInf.mPurchasedList.clear();
            String arrayInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(arrayInfo);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    mLoginUserInf.mPurchasedList.add(jsonItem.getString("level"));
                }
            }

        }
        return "";
    }

    private String parseJsonGetMainArticleList(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            String arrayInfo = jsonObject.getString("info");
            mLoginUserInf.mlistHotParentItem.clear();
            mLoginUserInf.mMapParenType.clear();
            JSONArray jsonArray = new JSONArray(arrayInfo);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    ParentItem tempItem = new ParentItem(jsonItem.getInt("Id"), jsonItem.getInt("type_id"), jsonItem.getString("title"), jsonItem.getString("short_desc"), jsonItem.getString("image_link"), jsonItem.getInt("article_status"));
                    mLoginUserInf.mlistHotParentItem.add(tempItem);
                }
            }

            String typeInfo = jsonObject.getString("articleType");
            JSONArray jsonArticleType = new JSONArray(typeInfo);
            if(jsonArticleType != null && jsonArticleType.length() >0)
            {
                for(int j = 0; j < jsonArticleType.length(); j++)
                {
                    JSONObject jsonItem = jsonArticleType.getJSONObject(j);
                    mLoginUserInf.mMapParenType.put(jsonItem.getInt("Id"), jsonItem.getString("title"));
                }
            }
        }
        return "";
    }
    private String parseJsonGetArticleType(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            String arrayInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(arrayInfo);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    ParentItem tempItem = new ParentItem(jsonItem.getInt("Id"), jsonItem.getInt("type_id"), jsonItem.getString("title"), jsonItem.getString("short_desc"), jsonItem.getString("image_link"), jsonItem.getInt("article_status"));
                    mLoginUserInf.mlistParentTypeItem.add(tempItem);
                }
            }
        }
        Intent parentTypeInt = new Intent(MyApplication.getInstance().getCurrentActivity(), parent_category.class);
        parentTypeInt.putExtra("category", mLoginUserInf.currentParentType);
        MyApplication.getInstance().getCurrentActivity().startActivity(parentTypeInt);
        return "";
    }
    private String parseJsonGetHotArticleType(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            mLoginUserInf.mlistHotParentItem.clear();
            mLoginUserInf.mMapParenType.clear();
            String arrayInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(arrayInfo);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    ParentItem tempItem = new ParentItem(jsonItem.getInt("Id"), jsonItem.getInt("type_id"), jsonItem.getString("title"), jsonItem.getString("short_desc"), jsonItem.getString("image_link"), jsonItem.getInt("article_status"));
                    mLoginUserInf.mlistHotParentItem.add(tempItem);
                }
            }
            String typeInfo = jsonObject.getString("articleType");
            JSONArray jsonArticleType = new JSONArray(typeInfo);
            if(jsonArticleType != null && jsonArticleType.length() >0)
            {
                for(int j = 0; j < jsonArticleType.length(); j++)
                {
                    JSONObject jsonItem = jsonArticleType.getJSONObject(j);
                    mLoginUserInf.mMapParenType.put(jsonItem.getInt("Id"), jsonItem.getString("title"));
                }
            }
            mLoginUserInf.showArticleCount = Integer.parseInt(jsonObject.getString("showcount"));
//            getMediaInfo("getMediaInfo", "getMediaInfo");
        }

        return "";
    }
    private String parseJsonGetBannerType(JSONObject jsonObject) throws  JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            String strInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strInfo);
            mLoginUserInf.mlistProfileBannerItems.clear();
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    ProfileBannerItem item = new ProfileBannerItem(jsonItem.getInt("Id"), jsonItem.getString("title"), jsonItem.getString("button"),
                            jsonItem.getString("link"), jsonItem.getInt("type"), jsonItem.getInt("status"), jsonItem.getString("modified"));
                    mLoginUserInf.mlistProfileBannerItems.add(item);
                }
            }

        }
        return "";
    }
    private String parseJsongetOrderInfo(JSONObject jsonObject) throws  JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            String strInfo = jsonObject.getString("info");
            JSONArray jsonArray = new JSONArray(strInfo);
            mLoginUserInf.mlistMyPaymentHistoryItems.clear();
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    String strContentJson = jsonItem.getString("levels");
                    JSONArray jsonContentArray = new JSONArray(strContentJson);
                    String strContent = "";
                    for(int j = 0 ; j < jsonContentArray.length(); j++)
                    {
                        strContent += "CAMP" + jsonContentArray.get(j) + " " ;
                    }
                    MyPaymentItem item = new MyPaymentItem(strContent, jsonItem.getInt("pay_price"), jsonItem.getInt("used_point"),
                           jsonItem.getString("buy_time"), jsonItem.getInt("event_type"));
                    mLoginUserInf.mlistMyPaymentHistoryItems.add(item);
                }
            }

        }
        ((payhistory)MyApplication.getInstance().getCurrentActivity()).refreshAllInformation();
        return "";
    }
    private String parseJsonGetArticleDetail(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            String info = jsonObject.getString("info");
            Intent parentDetailInt = new Intent(MyApplication.getInstance().getCurrentActivity(), articledetail.class);
            parentDetailInt.putExtra("webcontent", info);
            MyApplication.getInstance().getCurrentActivity().startActivity(parentDetailInt);
        }

        return "";
    }
    private String parseJsonGetToken(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            mLoginUserInf.mRemoveUserToken = jsonObject.getString("info");
            String strParameter = "hardware=" + mLoginUserInf.mstrActive_dev_id + "&token=" + mLoginUserInf.mRemoveUserToken + "&phone=" + mLoginUserInf.muserPhoneNumber;
            removeUser("removeUser", strParameter);
        }

        return "";
    }
    private String parseJsonGetAppVersion(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            mLoginUserInf.mRemoveUserToken = jsonObject.getString("info");
        }

        return "";
    }
    private String parseJsonRemoveUser(JSONObject jsonObject) throws JSONException{
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";
        if (mState.equals("0")) {
            MyApplication.getInstance().logOut();
            MessageAlert.showMessage(MainActivity.mainActivityInstance, "注销成功");
        }

        return "";
    }
    private String parseChangePassword(JSONObject jsonObject) throws JSONException
    {
        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");
        if (mState == null)
            return "错误";
        if(mState.equals("0"))
        {
            if(mnPassChangeType == Constants.PASS_CHANGE_ENTER_TYPE_AURORA)
            {
                Intent addChildAct = new Intent(MyApplication.getInstance().getCurrentActivity(), AddChildActivity.class);
                MyApplication.getInstance().getCurrentActivity().startActivity(addChildAct);
            } else if(mnPassChangeType == Constants.PASS_CHANGE_ENTER_TYPE_ONLYCHANGE) {

                MyApplication.getInstance().logOut();
            }
        }
        return "";
    }
    private String parseJsonForsetWechatBind(JSONObject jsonObject) throws JSONException
    {
        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");
        if (mState == null)
            return "错误";
        if(mState.equals("0"))
        {//
            if(mnBindType == 1) {
                mLoginUserInf.mbWechatBind = 1;
                ((SettingActivity) MyApplication.getInstance().getCurrentActivity()).resetWechatBindText();
            }
        }

        return mErrMsg;
    }
    private String parseJsonForresetWechatBind(JSONObject jsonObject) throws JSONException
    {
        String mState = jsonObject.getString("errcode");
        String mErrMsg = jsonObject.getString("errmsg");
        if (mState == null)
            return "错误";
        if(mState.equals("0"))
        {
            mLoginUserInf.mbWechatBind = 0;
            ((SettingActivity)MyApplication.getInstance().getCurrentActivity()).resetWechatBindText();
        }
        return mErrMsg;
    }

    private  String parseJsonForpicUpload(JSONObject jsonObject) throws JSONException{
        String strState = jsonObject.getString("errcode");
        if (strState == null) {
            return "更新画片错误！" ;
        }

        if ( strState.equals("0"))
        {
            mLoginUserInf.mstrPic = jsonObject.getString("pic");
            MainActivity.mainActivityInstance.mfragHome.refreshUserPic();
            MainActivity.mainActivityInstance.mfragProfile.refreshUserPic();
        }
        return "更新画片成功！";
    }

    private String parseJsonForsaveOralResult(JSONObject jsonObject) throws JSONException {
        String strState = jsonObject.getString("errcode");
        if (strState == null) {
            return "错误！" ;
        }
        return "";
    }
    private String parseJsonForgetOralLearnInfo(JSONObject jsonObject) throws JSONException {
        String strState = jsonObject.getString("errcode");

        if (strState == null) {
            return "错误！" ;
        }
        if (strState.equals("0")) {
            String strInfo = jsonObject.getString("info");

            Intent resultAct = new Intent(MyApplication.getInstance().getCurrentActivity(), SpeakingResultActivity.class);
            resultAct.putExtra("Speaking_Result", MakeJsonForSpeaking.convertObjectFromJson(strInfo));
            MyApplication.getInstance().getCurrentActivity().startActivity(resultAct);
        }
        return "";
    }
    private String parseJsonForgetShareInfo(JSONObject jsonObject) throws JSONException {
        String strState = jsonObject.getString("errcode");

        if (strState == null) {
            return "错误！" ;
        }
        if (strState.equals("0")) {
            mLoginUserInf.mnSharingCount = jsonObject.getInt("share_cnt");
            mLoginUserInf.mnQuizCount = jsonObject.getInt("pass_cnt");
            mLoginUserInf.mnStudyDays = jsonObject.getInt("study_cnt");

            Intent shareTypeInt = new Intent(MyApplication.getInstance().getCurrentActivity(), ShareType.class);
            MyApplication.getInstance().getCurrentActivity().startActivity(shareTypeInt);
        }
        return "";
    }
    private String parseJsonForsetLoginMode(JSONObject jsonObject) throws JSONException {
        String strState = jsonObject.getString("errcode");

        if (strState == null)
            return "错误！" ;
        if (strState.equals("0")) {
            getMediaInfo2("getMediaInfo2", "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId);
        }
        return "";
    }

    private String parseJsonFornetErrorLog(JSONObject jsonObject) throws  JSONException {
        String strState = jsonObject.getString("errcode");

        if (strState == null)
            return "错误！" ;

        MyApplication.getInstance().clearErrorLog();
        return "";
    }
    private String parseJsongetAppData(JSONObject jsonObject) throws JSONException {
        Log.d("responsex", jsonObject.toString());
        String strState = jsonObject.getString("errcode");

        if (strState == null) {
            return "错误！" ;
        }
        if (strState.equals("0")) {
            //getPayconf
            String strPayConf = jsonObject.getString("getPayConf");
            JSONArray jsonPayConfArray = new JSONArray(strPayConf);
            if (jsonPayConfArray != null && jsonPayConfArray.length() > 0) {
                for (int i = 0; i < jsonPayConfArray.length(); i++) {
                    JSONObject jsonLevel = jsonPayConfArray.getJSONObject(i);
                    PayConf payConf = new PayConf(i, jsonLevel.getInt("type"), jsonLevel.getInt("count"), jsonLevel.getInt("origin_price"), jsonLevel.getInt("discount_price"));

                    mLoginUserInf.mlistPayConf.add(payConf);
                }
            }

            //hardwareTypeList
            String strhardList = jsonObject.getString("hardList");
            JSONArray jsonstrhardListArray = new JSONArray(strhardList);
            if (jsonstrhardListArray != null && jsonstrhardListArray.length() > 0 ) {
                for (int i = 0; i < jsonstrhardListArray.length(); i++) {
                    JSONObject jsonItem = jsonstrhardListArray.getJSONObject(i);
                    DeviceInfo devInf = new DeviceInfo(jsonItem.getInt("Id"), jsonItem.getString("type"));
                    mLoginUserInf.mlistDevice.add(devInf);
                }
            }

            //systemTestUrl
            String strsystemTestUrlInfo = jsonObject.getString("systemTestUrl");
            JSONObject strinfo = new JSONObject(strsystemTestUrlInfo);
            JSONArray jsonsystemTestUrlInfoArray= strinfo.getJSONArray("info");

            mLoginUserInf.mlistSystemTestUrl.clear();
            if (jsonsystemTestUrlInfoArray != null && jsonsystemTestUrlInfoArray.length() > 0 ) {
                for (int i = 0 ; i < jsonsystemTestUrlInfoArray.length() ; i ++) {
                    JSONObject jsonTest = jsonsystemTestUrlInfoArray.getJSONObject(i);
                    TestItem testItem = new TestItem(jsonTest.getInt("Id"), jsonTest.getInt("level"), jsonTest.getInt("lesson_id"),
                            jsonTest.getInt("part_id"), jsonTest.getString("path"));
                    mLoginUserInf.mlistSystemTestUrl.add(testItem);
                }
            }

            //guideBanner
            String strInfo = jsonObject.getString("guideBanner");
            JSONArray jsonGuidBannerArray = new JSONArray(strInfo);
            mLoginUserInf.mlistProfileBannerItems.clear();
            if (jsonGuidBannerArray != null && jsonGuidBannerArray.length() > 0 ) {
                for (int i = 0 ; i < jsonGuidBannerArray.length() ; i ++) {
                    JSONObject jsonItem = jsonGuidBannerArray.getJSONObject(i);
                    ProfileBannerItem item = new ProfileBannerItem(jsonItem.getInt("Id"), jsonItem.getString("title"), jsonItem.getString("button"),
                            jsonItem.getString("link"), jsonItem.getInt("type"), jsonItem.getInt("status"), jsonItem.getString("modified"));
                    mLoginUserInf.mlistProfileBannerItems.add(item);
                }
            }

            //media
            //home_video
            String strHome = jsonObject.getString("media");
            JSONObject jsonHome = new JSONObject(strHome);
            String strMedia = jsonHome.getString("home_video");
            JSONArray jsonhomevideoArray = new JSONArray(strMedia);
            mLoginUserInf.mlistMedia.clear();
            if (jsonhomevideoArray != null && jsonhomevideoArray.length() > 0 ) {
                for (int i = 0 ; i < jsonhomevideoArray.length() ; i ++) {
                    JSONObject jsonItem = jsonhomevideoArray.getJSONObject(i);
                    MediaItem item = new MediaItem(i, jsonItem.getInt("ismain"), jsonItem.getString("title"), jsonItem.getString("image"), jsonItem.getString("file"),
                            jsonItem.getString("duration"), jsonItem.getString("filesize"), jsonItem.getString("desc"), jsonItem.getString("created"), jsonItem.getInt("location"));
                    mLoginUserInf.mlistMedia.add(item);
                }
            }
            //banner
            mLoginUserInf.mlistbanner.clear();
            String strBanner = jsonHome.getString("home_banner");
            JSONArray jsonArraybannerout = new JSONArray(strBanner);
            if (jsonArraybannerout != null && jsonArraybannerout.length() > 0 ) {
                for (int i = 0 ; i < jsonArraybannerout.length() ; i ++) {
                    JSONObject jsonbannerItem = jsonArraybannerout.getJSONObject(i);
                    ImageObj item = new ImageObj(i, null,  jsonbannerItem.getString("image_link"), jsonbannerItem.getString("link"),jsonbannerItem.getInt("ordernum"),jsonbannerItem.getString("bg_color"),jsonbannerItem.getString("font_color"), jsonbannerItem.getInt("usertype"));
                    mLoginUserInf.mlistbanner.add(item);

                }
            }

            ///Oral_Practice///
            mLoginUserInf.mlist_oralPracticeBanner.clear();

            String stroral_banner = jsonHome.getString("oral_banner");
            JSONArray jsonArraybanner = new JSONArray(stroral_banner);
            if (jsonArraybanner != null && jsonArraybanner.length() > 0 ) {
                for (int i = 0 ; i < jsonArraybanner.length() ; i ++) {
                    JSONObject jsonbannerItem = jsonArraybanner.getJSONObject(i);
                    ImageObj item = new ImageObj(i, null,  jsonbannerItem.getString("image_link"), jsonbannerItem.getString("link"),jsonbannerItem.getInt("ordernum"),jsonbannerItem.getString("bg_color"),jsonbannerItem.getString("font_color"),  jsonbannerItem.getInt("usertype"));
                    mLoginUserInf.mlist_oralPracticeBanner.add(item);

                }
            }

            //hotArticle
            mLoginUserInf.mlistHotParentItem.clear();
            mLoginUserInf.mMapParenType.clear();
            String strHotArticle = jsonObject.getString("hotArticle");
            JSONObject jsonArticle = new JSONObject(strHotArticle);
            JSONArray jsonArray = jsonArticle.getJSONArray("info");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    ParentItem tempItem = new ParentItem(jsonItem.getInt("Id"), jsonItem.getInt("type_id"), jsonItem.getString("title"), jsonItem.getString("short_desc"), jsonItem.getString("image_link"), jsonItem.getInt("article_status"));
                    mLoginUserInf.mlistHotParentItem.add(tempItem);
                }
            }
            String typeInfo = jsonArticle.getString("articleType");
            JSONArray jsonArticleType = new JSONArray(typeInfo);
            if(jsonArticleType != null && jsonArticleType.length() >0)
            {
                for(int j = 0; j < jsonArticleType.length(); j++)
                {
                    JSONObject jsonItem = jsonArticleType.getJSONObject(j);
                    mLoginUserInf.mMapParenType.put(jsonItem.getInt("Id"), jsonItem.getString("title"));
                }
            }
            mLoginUserInf.showArticleCount = Integer.parseInt(jsonArticle.getString("showcount"));

            //trainCampLink
            mLoginUserInf.mstrTrainCampLink = jsonObject.getString("trainCampLink");

            if(MyApplication.getInstance().isFirstLoginFailure) {
                MyApplication.getInstance().isFirstLoginFailure = false;
                if (State.getCurrent_login_state() == State.loginType.logout) {
                    Intent mainInt = new Intent(MyApplication.getInstance().getCurrentActivity(), MainActivity.class);
                    MyApplication.getInstance().getCurrentActivity().startActivity(mainInt);
                } else {
                    MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_LOGIN, "loginWithPhone", "phone=" + MyApplication.getInstance().getPhoneNumberFromPref() +
                            "&hardTypeId=" + MyApplication.getInstance().getHardWareTypeFromPref() + "&hardware=" + MyApplication.getInstance().getHardWareSerialNumberFromPref() + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_SMS, MyApplication.getInstance().getPhoneNumberFromPref());
                }
            }

            if(MessageAlert.mDlgProgress != null)
                MessageAlert.mDlgProgress.dismiss();

        }


        return "";
    }
    private String parseJsonForeignTeacherVideo(JSONObject jsonObject) throws JSONException {
        String strState = jsonObject.getString("errcode");

        if (strState == null) {
            return "错误！" ;
        }
        if (strState.equals("0")) {
            JSONArray jsonArray = jsonObject.getJSONArray("info");

            mLoginUserInf.mlistTeacherMedia.clear();
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    MediaItem item = new MediaItem(jsonItem.getInt("Id"), 0, jsonItem.getString("title"), jsonItem.getString("image"), jsonItem.getString("file"),
                            jsonItem.getString("duration"), jsonItem.getString("filesize"), jsonItem.getString("desc"), "", jsonItem.getInt("ordernum"));
                    mLoginUserInf.mlistTeacherMedia.add(item);
                }
            }
//            if(State.getCurrentPattern() == pattern.parent.getValue())
//                MainActivity.mainActivityInstance.mfragStudy.refreshTeachVideoTitle();
//            else
//                StudentPatternActivity.instance.refreshTeachvideoTitle();

        }

        return "";
    }
    private String parseJsonForSaveReadNotify(JSONObject jsonObject) throws  JSONException {
        String mState = jsonObject.getString("errcode");
        if (mState == null)
            return "错误";

        if (mState.equals("0"))
        {
            String strNotify = jsonObject.getString("info");
            JSONObject jsonNotify = new JSONObject(strNotify);
            String strUnread = jsonNotify.getString("unread");
            JSONObject jsonUnread = new JSONObject(strUnread);
            mLoginUserInf.mnUnreadTotalCount = jsonUnread.getInt("totalUnread");
            mLoginUserInf.mnUnreadPrivateCount = jsonUnread.getInt("privateUnread");
            mLoginUserInf.mnUnreadBroadCount = jsonUnread.getInt("broadUnread");

            mLoginUserInf.mlistBroadMessage.clear();
            String strBroadNotify = jsonNotify.getString("broadNotify");
            JSONArray jsonArray = new JSONArray(strBroadNotify);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    String strDate = jsonItem.getString("created");
                    String strMonth, strDay;
                    strMonth = strDate.substring(5, 7);
                    strDay = strDate.substring(8, 10);
                    strDate = strMonth + "/" + strDay;

                    MessageItem item = new MessageItem(jsonItem.getInt("Id"), 0, jsonItem.getString("title"),
                            jsonItem.getString("content"), strDate, jsonItem.getInt("isread"), 0);
                    mLoginUserInf.mlistBroadMessage.add(item);
                }
            }

            mLoginUserInf.mlistPrivateMessage.clear();
            String strPrivateNotify = jsonNotify.getString("privateNotify");
            jsonArray = new JSONArray(strPrivateNotify);
            if (jsonArray != null && jsonArray.length() > 0 ) {
                for (int i = 0 ; i < jsonArray.length() ; i ++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    String strDate = jsonItem.getString("created");
                    String strMonth, strDay;
                    strMonth = strDate.substring(5, 7);
                    strDay = strDate.substring(8, 10);
                    strDate = strMonth + "/" + strDay;
                    MessageItem item = new MessageItem(jsonItem.getInt("Id"), jsonItem.getInt("user_id"), jsonItem.getString("title"),
                            jsonItem.getString("content"), strDate, jsonItem.getInt("isread"), 0);
                    mLoginUserInf.mlistPrivateMessage.add(item);
                }
            }

        }
        return "";
    }

    private String parseJsonForgetEncryptKey(String result)
    {
        return DecodeBase64(result);
    }

    private String DecodeBase64(String result)
    {
        return new String(Base64.decode(result.getBytes(), Base64.DEFAULT));
    }

    private String DecodeAES(String result)
    {
        EasyAES aes = new EasyAES(MyApplication.getInstance().mstrPassword, 256,  MyApplication.getInstance().mstrIV);
        return aes.decrypt(result);
    }

    private int DifferenceTimestamp(Long mServerTimeStamp)
    {
        Long mLocalTimestamp = System.currentTimeMillis() / 1000L;
        Long mDifference = Math.abs(mServerTimeStamp - mLocalTimestamp);
        if(mDifference < 600)
        {
            return 0;
        }
        else{
            return 1;
        }
    }

    private boolean isValid(String json)
    {
        try{
            new JSONObject(json);
        }catch(JSONException e)
        {
            return false;
        }
        return true;
    }
}


