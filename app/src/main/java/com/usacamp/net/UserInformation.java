package com.usacamp.net;

import com.usacamp.model.ImageObj;
import com.usacamp.model.ActCenterItem;
import com.usacamp.model.DeviceInfo;
import com.usacamp.utils.LearningRecordItem;
import com.usacamp.model.LessonItem;
import com.usacamp.model.LevelItem;
import com.usacamp.model.MediaItem;
import com.usacamp.model.MessageItem;
import com.usacamp.utils.MyPaymentItem;
import com.usacamp.utils.MyPointItem;
import com.usacamp.model.ParentItem;
import com.usacamp.utils.PayConf;
import com.usacamp.model.ProfileBannerItem;
import com.usacamp.model.ProgressItem;
import com.usacamp.model.ScoreItem;
import com.usacamp.utils.Sentence;
import com.usacamp.model.SpeakingIntroItem;
import com.usacamp.model.TestItem;
import com.usacamp.utils.VersionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInformation {

    public void initialize()
    {
        mbAlreadyLogin = false;
        mstrName = "";
        mstrNickName = "";
        mstrBirthday = "";
        mstrStudydgree = "0";
        mstrGender = "";
        mstrOpenID = "";  //weixin ID
        mstrAppVersion = ""; //App Version
        mstrCustomerService = "";//
        mstrUserId = "";
        mSharePointValue = 0;
        misTrainCamp = 0 ;//
        mnSelectedLevel = 0;    //selected_level
        mnbase64UserID = "0";
        muserPhoneNumber = "";
        mbUnsetPassword = false;
        muserPassword = "";
        mstrPic = "";      //picture path
        mCreatedTime = "";
        mAvailableDays = 0;
        bExperience = 0; // 체험이벤트구입기발
        dExperience = ""; // 체험이벤트 구입시간
        pExperience = 14; // 체험이벤트기간
        mbWechatBind = 0; //위챗바인드 기발
        mbWechatAuroraSind = false;
        mbPassword = 0; //암호있음 1 암호없음 0
        mdFirstLogin = ""; //첫로그인 날자
        muserType2 = 0; // 0: logout 1:purchased 2: nonpurchased
        muserType = 0;//0 : 일반 리용자, 1 : 스캔한 리용자, 2: 3일동안 무료 사용자, 3 : 대리상, 4: 회사 직원, 5 : 스캔한 리용자이면서 3일동안 무료 사용자
        mnManualDays = 0;
        mnPurchasedDays = 0;
        mDeleteUserConfrimCode = "";
        mPayedPrice = 0.0f;
        isDeleteUser = false;
        mnUserPayType = 0;
        mstrSharedUserId = "";
        mstrAgentUserId = "";
        currentProgressPartMap.clear();
        mnUnreadTotalCount = 0;   //total count of unread messages
        mnUnreadPrivateCount = 0;   //count of unread braoad messages
        mnUnreadBroadCount = 0;   //count of unread private messages
        mlistBroadMessage.clear();
        currentParentType = 0;
        showArticleCount = 5;
        mSpeakingItemList.clear();
        mHearingItemList.clear();
        mlistTeacherMedia.clear();
        mlistProgress.clear();
        mlistLearningRecord.clear();
        mlistMyPoint.clear();
        mlistActCenter.clear();
        mlistAvailableLessons.clear();
        mlistLevels.clear();
        mlistScores.clear();
        mlistProfileBannerItems.clear();
        mlistMyPaymentHistoryItems.clear();
        mRemoveUserToken = "";
        isLevelTestOPen = 0;
        mstrLessonURL = "";
        mstrSpeakingLessonURL = "";
        mnLearnCnt = 0;
        mSentenceList.clear();
        mnPlusPoint = 0;
        mnUserPoint = 0;
        mnUsedPoint = 0;
        isShared = "";
        mnRate = 100;
        myTotalPoint = 0;
        mstrActDetail = "";
        mSelCampCount = 0;
        mSelCampList.clear();
        mPurchasedList.clear();
        mTempLoginPriviousAct = ""; // 0: empty, 1: trial, 2: test, 3: share, 4:purchase
        CurrentProgressItem = null;
        mnStudyDays = 0;
        mnQuizCount = 0;
        mnSharingCount = 0;
    }

    public String mstrDeviceID = "offline_test";
    public ArrayList<DeviceInfo> mlistDevice = new ArrayList<DeviceInfo>();
    public ArrayList<PayConf> mlistPayConf = new ArrayList<PayConf>();

    public boolean mbAlreadyLogin = false;
    public int mnStudyDays = 0;
    public int mnSharingCount = 0;
    public int mnQuizCount = 0;

    //profile
    public String mstrName = "";
    public String mstrNickName = "";
    public String mstrBirthday = "";
    public String mstrStudydgree = "0";
    public String mstrRoleID = "";
    public String mstrGender = "";
    public String mstrOpenID = "";  //weixin ID
    public String mstrAppVersion = ""; //App Version
    public String mstrCustomerService = "";//
    public String mstrCompany = "";
    public String mstrUserId = "0";
    public int mSharePointValue = 0;
    public int misNew = 0;//
    public int misTrainCamp = 0 ;//
    public String mstrActive_dev_id = ""; //active_dev_id
    public String mstrTuturial = "";
    public String mstrTos = "";
    public int mnSelectedLevel = 0;    //selected_level
    public String mnbase64UserID = "0";
    public String muserPhoneNumber = "";
    public boolean mbUnsetPassword = false;

    public String muserPassword = "";
    public String mstrPic = "";      //picture path
    public String mCreatedTime = "";
    public int mAvailableDays = 0;
    public int bExperience = 0; // 체험이벤트구입기발
    public String dExperience = ""; // 체험이벤트 구입시간
    public int pExperience = 14; // 체험이벤트기간
    public int mbWechatBind = 0; //위챗바인드 기발
    public boolean mbWechatAuroraSind = false;
    public int mbPassword = 0; //암호있음 1 암호없음 0

    public String mdFirstLogin = ""; //첫로그인 날자
    public int muserType2 = 0; // 0: logout 1:purchased 2: nonpurchased
    public int muserType = 0;//0 : 일반 리용자, 1 : 스캔한 리용자, 2: 3일동안 무료 사용자, 3 : 대리상, 4: 회사 직원, 5 : 스캔한 리용자이면서 3일동안 무료 사용자,6:고급에이젼트, 7:파트너
    //    public boolean isUsePoint = false;
    public int mnManualDays = 0;
    public int mnPurchasedDays = 0;
    public String mDeleteUserConfrimCode = "";
    public float mPayedPrice = 0.0f;
    public boolean isDeleteUser = false;
    //pay type
    public int mnUserPayType = 0;
    public String mstrSharedUserId = "";
    public String mstrAgentUserId = "";
    public Map<Integer, Integer> currentProgressPartMap = new HashMap<Integer, Integer>();
    //notify
    public int mnUnreadTotalCount = 0;   //total count of unread messages
    public int mnUnreadPrivateCount = 0;   //count of unread braoad messages
    public int mnUnreadBroadCount = 0;   //count of unread private messages
    public ArrayList<MessageItem> mlistBroadMessage = new ArrayList<MessageItem>();
    public ArrayList<ParentItem> mlistHotParentItem = new ArrayList<ParentItem>();
    public ArrayList<ParentItem> mlistParentTypeItem = new ArrayList<ParentItem>();
    public int currentParentType = 0;
    public int showArticleCount = 5;
    public int mnLoginMode = 0;
    public ArrayList<ImageObj> mlistbanner = new ArrayList<ImageObj>();

    public ArrayList<ImageObj> mlist_oralPracticeBanner = new ArrayList<ImageObj>();

    public ArrayList<SpeakingIntroItem> mSpeakingItemList = new ArrayList<SpeakingIntroItem>();
    public ArrayList<SpeakingIntroItem> mHearingItemList = new ArrayList<SpeakingIntroItem>();
    public Map<Integer, String> mMapParenType = new HashMap<Integer, String>();
    public ArrayList<MessageItem> mlistPrivateMessage = new ArrayList<MessageItem>();
    //media
    public ArrayList<MediaItem> mlistMedia = new ArrayList<MediaItem>();
    //teacher videos
    public ArrayList<MediaItem> mlistTeacherMedia = new ArrayList<MediaItem>();
    //progress
    public ArrayList<ProgressItem> mlistProgress = new ArrayList<ProgressItem>();

    public ArrayList<LearningRecordItem> mlistLearningRecord = new ArrayList<LearningRecordItem>();
    public ArrayList<MyPointItem> mlistMyPoint = new ArrayList<MyPointItem>();
    public ArrayList<ActCenterItem> mlistActCenter = new ArrayList<ActCenterItem>();

    public ArrayList<LessonItem> mlistAvailableLessons = new ArrayList<LessonItem>();

    public ArrayList<LevelItem> mlistLevels = new ArrayList<LevelItem>();

    public ArrayList<ScoreItem> mlistScores = new ArrayList<ScoreItem>();

    //System test url
    public ArrayList<TestItem> mlistSystemTestUrl = new ArrayList<TestItem>();
    //public Map<Integer, ArrayList<TestItem>> mlistSystemTestUrlMap =  new HashMap<Integer,ArrayList<TestItem>>();
    //Level test url
    public ArrayList<TestItem> mlistLevelTestUrl = new ArrayList<TestItem>();
    public ArrayList<ProfileBannerItem> mlistProfileBannerItems = new ArrayList<>();
    public ArrayList<MyPaymentItem> mlistMyPaymentHistoryItems= new ArrayList<>();
    public String mRemoveUserToken = "";
    public int isLevelTestOPen = 0;
    //Lesson URL
    public String mstrLessonURL = "";
    public String mstrSpeakingLessonURL = "";

    public int mnLearnCnt = 0;
    public ArrayList<Sentence> mSentenceList = new ArrayList<Sentence>();
    //Quiz point
    public int mnPlusPoint = 0;
    public int mnUserPoint = 0;
    public int mnUsedPoint = 0;
    public String isShared = "";
    public int mnRate = 100;
    public int myTotalPoint = 0;

    public int mnIsActive = 1;
    public String mstrTrainCampLink = "";
    public String mstrActDetail;
    public int mSelCampCount = 0;
    public ArrayList<Integer> mSelCampList = new ArrayList<Integer>( );
    public ArrayList<String> mPurchasedList = new ArrayList<String>();
    public String mTempLoginPriviousAct = ""; // 0: empty, 1: trial, 2: test, 3: share, 4:purchase
    public ProgressItem CurrentProgressItem;
    public VersionInfo versionInfo = new VersionInfo();

}

