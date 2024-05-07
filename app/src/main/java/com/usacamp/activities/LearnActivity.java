package com.usacamp.activities;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.iflytek.cloud.resource.Resource;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.services.ScreenOnOffService;
import com.usacamp.model.LessonItem;
import com.usacamp.utils.LessonPtGridAdapter;
import com.usacamp.utils.LessonsMenuGridAdapter;
import com.usacamp.utils.MessageAlert;
import com.usacamp.model.ProgressItem;
import com.usacamp.utils.sekbartextmerge;
//

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LearnActivity extends BaseActivity {

    public static int LESSON_ACT = 0;
    public final int[] PART_DRAWABLE_UNAVAILABLE = {
            R.drawable.lesson112,
            R.drawable.lesson222,
            R.drawable.lesson332,
            R.drawable.lesson442,
            R.drawable.lesson552,
            R.drawable.lesson662,
            R.drawable.lesson772,
            R.drawable.lesson882,
            R.drawable.quiz22
    };
    public final int[] PART_DRAWABLE_AVAILABLE = {
            R.drawable.lesson111,
            R.drawable.lesson221,
            R.drawable.lesson331,
            R.drawable.lesson441,
            R.drawable.lesson551,
            R.drawable.lesson661,
            R.drawable.lesson771,
            R.drawable.lesson881,
            R.drawable.quiz11
    };
    public final int[] PART_DRAWABLE_SELECTED = {
            R.drawable.lesson113,
            R.drawable.lesson223,
            R.drawable.lesson333,
            R.drawable.lesson443,
            R.drawable.lesson553,
            R.drawable.lesson663,
            R.drawable.lesson773,
            R.drawable.lesson883,
            R.drawable.quiz33
    };

    ImageView fabhome;
    ImageView previoudBtn ;
    ImageView refreshBtn ;
    ImageView nextBtn ;
    ImageView textBtn ;
    ImageView campBtn;
    ImageView fabInflate; //fabQuiz;
    public ImageView[] fabParts = new ImageView[Constants.BIG_PARTS_COUNT];
    ImageView fabMenu;

    public TextView lessonTxt ;
    private WebView webView;
    private GridView gridLessonMenu;
    private LessonsMenuGridAdapter madptLessonGrid;
    private boolean isPageLoadedComplete = false;
    boolean isFABOpen=false;
    public static Map<Integer, ArrayList<String>> placementTestLinks = new HashMap<Integer, ArrayList<String>>();

    private Handler mHandler;

    public int recommandLevel = 1;
    int mnLearnMode;    //0: learn, 1: trial, 2: system test, 3: level test
    private int mnCurrLevel;
    private int mnCurrLesson;
    private int mnCurrPart;
    private final Timer myTimer = new Timer();
    private int mnCurrTestIdx = 0;

    private int mnProgressLesson;
    private int mnProgressPart;

    private int mnQuizTestType = 1; //1: quiz, 2: test

    private int mnPartsCount = Constants.BIG_PARTS_COUNT;

    private final boolean[] marrLessongAvailable = new boolean[Constants.LESSON_COUNT];
    private final boolean[] marrPartAvailable = new boolean[Constants.BIG_PARTS_COUNT];

    private int partPoint = 0;
    private int partQuestion = 0;

    String mstrCurrentUrl;
    LessonItem mCurrLesson;

    Dialog mdlgFinish;
    Dialog mdlgTrialFinish;

    Dialog mdlgMyCamp = null;
    GridView mgridMyCamp = null;
    LessonPtGridAdapter mMyCampAdapter = null;
    LinearLayout m_testPanel = null;
    private  boolean  mfTextShow = true;
    private sekbartextmerge m_progressBar;
    String mstrPoint = "";
    Intent i0;

    public ConstraintLayout leveltestResultPanel, quizResultPanel;
    Button shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        webView = (WebView) findViewById(R.id.MainWebView);
        m_progressBar = (sekbartextmerge) findViewById(R.id.progressBar);
        i0 = new Intent(this, ScreenOnOffService.class);
        i0.setAction("com.usacamp.services.ScreenOnOffService");

        startService(i0);
        m_progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_progressBar.setThumbText(seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });
        //getWindow().addFlags(WindowManager.LayoutParams.PREVENT_POWER_KEY);
        /////////////////

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;

        float sideWidth = (width - height / 520.0f * 740.0f) / 2;
        ImageView sideView1 = (ImageView) findViewById(R.id.imageView1);
        ImageView sideView2 = (ImageView) findViewById(R.id.imageView2);

        sideView1.getLayoutParams().width = (int) sideWidth;
        sideView2.getLayoutParams().width = (int) sideWidth;
        /////////////////
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        }
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        //Zhuge
        //webView.addJavascriptInterface(new ZhugeSDK.ZhugeJS(),"WebContentTracker");
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setDomStorageEnabled(false);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //webView.setInitialScale(1);
        webView.getSettings().setAllowFileAccess(true);
        webView.setClickable(true);
        webView.addJavascriptInterface(new JavaScriptInterface(), "interface");
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.setWebViewClient(new myWebClient());
        webView.setWebChromeClient(new myChromeClient());
        lessonTxt = (TextView) findViewById(R.id.text_lesson_title);
        previoudBtn = (ImageView) findViewById(R.id.previous);
        refreshBtn = (ImageView) findViewById(R.id.refresh);
        nextBtn = (ImageView) findViewById(R.id.next);
        textBtn= (ImageView) findViewById(R.id.text);
        m_testPanel = findViewById(R.id.testscorepanel);
        mnLearnMode = getIntent().getIntExtra("LearnMode", 0);
        Log.d("mLearnMode", String.valueOf(MyApplication.mNetProc.mLoginUserInf.muserType));
        if (mnLearnMode == 2) {
            placementTestLinks = makePlacementTestList();
            lessonTxt.setText("TEST");
            mnCurrTestIdx = 0;
            mstrCurrentUrl = placementTestLinks.get(1).get(0);
            mnQuizTestType = 2;
            //Zhuge
            //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Enter_Test_TestingPage);

            //Zhuge
            //////ZhugeSDK.getInstance().startTrack(Constants.Zhuge_Event_Period_Test_TestingPage);

        }else{
            mnCurrLevel = getIntent().getIntExtra("LevelId", 0);
            mnCurrLesson = getIntent().getIntExtra("LessonId", 0);
        }


        mHandler = new Handler();

        campBtn = (ImageView) findViewById(R.id.mycampp) ;

        findViewById(R.id.lyt_touch_pad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFABOpen)
                    closeFABMenu();
                if (findViewById(R.id.layout_learn_menu).getVisibility() == View.VISIBLE)
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                findViewById(R.id.lyt_touch_pad).setVisibility(View.INVISIBLE);
            }
        });

        fabInflate = (ImageView) findViewById(R.id.fabInflate);
        fabParts[0] = (ImageView) findViewById(R.id.fab1);
        fabParts[1] = (ImageView) findViewById(R.id.fab2);
        fabParts[2] = (ImageView) findViewById(R.id.fab3);
        fabParts[3] = (ImageView) findViewById(R.id.fab4);
        fabParts[4] = (ImageView) findViewById(R.id.fab5);
        fabParts[5] = (ImageView) findViewById(R.id.fab6);
        fabParts[6] = (ImageView) findViewById(R.id.fab7);
        fabParts[7] = (ImageView) findViewById(R.id.fab8);
        fabParts[8] = (ImageView) findViewById(R.id.fab_quiz);
        fabhome = (ImageView) findViewById(R.id.home);
        fabMenu = (ImageView) findViewById(R.id.fabmenu);
        leveltestResultPanel = (ConstraintLayout) findViewById(R.id.leveltestresult);
        shareBtn = (Button) findViewById(R.id.btn_just_record1);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                        "&kind=0";

                MyApplication.mNetProc.getShareInfo("getShareInfo", strRequestParameter);
                //quizResultPanel.setVisibility(View.GONE);
            }

        });
        ((ImageView) findViewById(R.id.img_closeQuiz1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizResultPanel.setVisibility(View.GONE);
            }
        });
        quizResultPanel = (ConstraintLayout) findViewById(R.id.quizresult);
        madptLessonGrid = new LessonsMenuGridAdapter(this, R.layout.layout_lesson_detail);
        gridLessonMenu = (GridView)findViewById(R.id.grid_lessons);
        gridLessonMenu.setAdapter(madptLessonGrid);
        madptLessonGrid.notifyDataSetChanged();
        fabhome.setEnabled(false);
        previoudBtn.setEnabled(false);
        refreshBtn.setEnabled(false);
        nextBtn.setEnabled(false);
        textBtn.setEnabled(false);
        campBtn.setEnabled(false);
        if (mnCurrLevel < Constants.DIFF_PART_LEVEL)
            mnPartsCount = Constants.SMALL_PARTS_COUNT;
        else
            mnPartsCount = Constants.BIG_PARTS_COUNT;

        //lesson for current level
        if (mnLearnMode == 0 ) {

            for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size(); i++) {
                ProgressItem progressItem = MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i);
                if (progressItem.mnLevelId == mnCurrLevel) {
                    mnCurrLesson = progressItem.mnCurrentLessonId - 1;
                    mnCurrPart = progressItem.mnCurrentPartId - 1;
                    mnProgressLesson = progressItem.mnLessonId - 1;
                    mnProgressPart = progressItem.mnPartId - 1;

                    if (mnCurrLesson < 0) {
                        mnCurrLesson = 0;
                        mnCurrPart = 0;
                    }

                    if (mnProgressLesson < 0) {
                        mnProgressLesson = 0;
                        mnProgressPart = 0;
                    }
                    break;
                }
            }

            if (mnProgressLesson == 24 && mnLearnMode != 1) {
                findViewById(R.id.lobtn_test).setEnabled(true);
                findViewById(R.id.lobtn_test).setBackgroundResource(R.drawable.green_dark_radius_outline);
            } else {
                findViewById(R.id.lobtn_test).setBackgroundResource(R.drawable.gray_transparent_radius);
                findViewById(R.id.lobtn_test).setEnabled(false);
            }

            if (MyApplication.mNetProc.mLoginUserInf.muserType == 2 || MyApplication.mNetProc.mLoginUserInf.muserType == 5) {
                if (MyApplication.mNetProc.mLoginUserInf.isLevelTestOPen == 0) {
                    findViewById(R.id.lobtn_test).setEnabled(false);
                    findViewById(R.id.lobtn_test).setBackgroundResource(R.drawable.gray_transparent_radius);

                } else if (MyApplication.mNetProc.mLoginUserInf.isLevelTestOPen == 1) {
                    findViewById(R.id.lobtn_test).setEnabled(true);
                    findViewById(R.id.lobtn_test).setBackgroundResource(R.drawable.green_dark_radius_outline);
                }
            }
            //refresh Lesson Menu
            for (int i = 0; i <= mnProgressLesson; i++) {
                if (i > Constants.LESSON_COUNT - 1)
                    continue;
                marrLessongAvailable[i] = true;
                madptLessonGrid.setLessonStatus(i, Constants.LESSON_AVAILABLE);
            }

            madptLessonGrid.setLessonStatus(mnCurrLesson, Constants.LESSON_SELECTED);
            if (mnCurrLesson < mnProgressLesson) {
                for (int i = 0; i < mnPartsCount; i++) {
                    marrPartAvailable[i] = true;
                }

            } else {
                for (int i = 0; i < mnPartsCount; i++) {
                    marrPartAvailable[i] = i <= mnProgressPart;
                }
            }
            refreshParts();
            if(MyApplication.getInstance().isOfflineMode) {
                refreshOfflineWebView();
            } else {
                String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                        "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + (mnCurrPart + 1) + "&kind=" + Constants.STUDY_TYPE_HEARING;

                MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);

            }
        } else if (mnLearnMode == 1) {

            ArrayList<Integer> arrAvailableParts = getIntent().getIntegerArrayListExtra("AvailableParts");
            for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.size(); i++) {
                LessonItem lessonItem = MyApplication.mNetProc.mLoginUserInf.mlistAvailableLessons.get(i);
                if (lessonItem.mnLevelId == mnCurrLevel) {
                    mCurrLesson = lessonItem;
                }
            }

            marrLessongAvailable[mCurrLesson.mnLessonId - 1] = true;
            madptLessonGrid.setLessonStatus(mCurrLesson.mnLessonId - 1, Constants.LESSON_SELECTED);
            for (int i = 0; i < arrAvailableParts.size(); i++)
                marrPartAvailable[arrAvailableParts.get(i) - 1] = true;
            refreshParts();

            mstrCurrentUrl = mCurrLesson.mlistParts.get(0).mstrUrl;
            webView.loadUrl(mstrCurrentUrl);
            setTitle();
        }else if(mnLearnMode == 2){
            findViewById(R.id.lyt_top_menu).animate().alpha(1);
            findViewById(R.id.lyt_top_menu).setVisibility(View.VISIBLE);
            fabInflate.setVisibility(View.INVISIBLE);
            fabhome.setEnabled(true);
            textBtn.setEnabled(true);
            campBtn.setVisibility(View.GONE);
            previoudBtn.setVisibility(View.GONE);
            refreshBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            webView.loadUrl(mstrCurrentUrl);
        }

        gridLessonMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int nSelId, long l) {
                if(mnLearnMode == 3) //leveltest
                    QuitLevelTest();
                if (!marrLessongAvailable[nSelId])
                    return;
                Log.d("mnCurrLesson", String.valueOf(mnCurrLesson));
                if (nSelId == mnCurrLesson)
                    return;

                webView.clearCache(true);
                webView.stopLoading();
                webView.clearFormData();

                String strTitle;
                strTitle = String.format("CAMP "+ mnCurrLevel + " %02d-%d", nSelId + 1, 1);

                ((TextView)findViewById(R.id.text_lesson_title)).setText(strTitle);
                madptLessonGrid.setLessonStatus(nSelId, Constants.LESSON_SELECTED);

                if (nSelId < mnProgressLesson){
                    for (int i = 0; i < mnPartsCount; i++){
                        marrPartAvailable[i] = true;
                    }

                }else if(nSelId == mnProgressLesson){
                    for (int i = 0; i < mnPartsCount; i++){
                        marrPartAvailable[i] = i <= mnProgressPart;
                    }
                }
                refreshParts();
                previoudBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                refreshBtn.setVisibility(View.VISIBLE);
                mnCurrLesson = nSelId;
                //定义与事件相关的属性信息
//                JSONObject eventObject = new JSONObject();
//                try {
//                    eventObject.put(Constants.Zhuge_Property_Camp_Name,  strTitle);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                ////ZhugeSDK.getInstance().track(getApplicationContext(), Constants.Zhuge_Event_Click_Learn_LearningPage_Lesson_Button,  eventObject);
                fabParts[0].callOnClick();
            }
        });

        fabhome.setOnClickListener(new View.OnClickListener(){
                                       public void onClick(View v){
                                           webView.clearCache(true);
                                           webView.stopLoading();
                                           webView.clearFormData();
                                           stopService(i0);
                                           finish();

//                //Zhuge
//                if(mnLearnMode == 2)
//                    ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Test_TestingPage_Exit);
//                else if(mnLearnMode == 0)
//                    ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Learn_LearningPage_Exit);
                                       }
                                   }
        );
        previoudBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (mnLearnMode == 2) {
                    if (mnCurrTestIdx > 0) {
                        mnCurrTestIdx--;
                        if (MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                            mstrCurrentUrl = MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.get(mnCurrTestIdx).mstrPath;
                        }else
                            mstrCurrentUrl = MyApplication.mNetProc.mLoginUserInf.mlistSystemTestUrl.get(mnCurrTestIdx).mstrPath;
                        webView.loadUrl(mstrCurrentUrl);
                    }else
                        MessageAlert.showMessage(LearnActivity.this, "没有以前的部分！");
                } else {

                    int nNewPartId;
                    if (mnCurrPart != 0) {
                        nNewPartId = mnCurrPart - 1;
                        if (marrPartAvailable[nNewPartId]) {
                            if (mnLearnMode == 1) {
                                if(mnCurrPart != 0)
                                    mnCurrPart --;

                                playExperience(mnCurrPart);
                            } else {
                                mnCurrPart = nNewPartId;
                                if(MyApplication.getInstance().isOfflineMode) {
                                    refreshOfflineWebView();
                                } else {
                                    String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                            "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + (mnCurrPart + 1) + "&kind=" + Constants.STUDY_TYPE_HEARING;

                                    MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                                }
                            }
                            findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                        } else {
                            MessageAlert.showMessage(LearnActivity.this, "您没有权限！");
                        }
                    } else {
                        MessageAlert.showMessage(LearnActivity.this, "没有以前的部分！");
                    }
                }
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                webView.reload();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                if (mnLearnMode == 2) {
                    if (MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin){
                        if (mnCurrTestIdx < MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.size() - 1) {
                            mnCurrTestIdx++;
                            mstrCurrentUrl = MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.get(mnCurrTestIdx).mstrPath;
                            webView.loadUrl(mstrCurrentUrl);
                        } else
                            MessageAlert.showMessage(LearnActivity.this, "没有以后的部分！");
                    }else {
                        if (mnCurrTestIdx < MyApplication.mNetProc.mLoginUserInf.mlistSystemTestUrl.size() - 1) {
                            mnCurrTestIdx++;
                            mstrCurrentUrl = MyApplication.mNetProc.mLoginUserInf.mlistSystemTestUrl.get(mnCurrTestIdx).mstrPath;
                            webView.loadUrl(mstrCurrentUrl);
                        } else
                            MessageAlert.showMessage(LearnActivity.this, "没有以后的部分！");
                    }
                } else {
                    int nNewPartId;

                    if (mnCurrPart + 1 != mnPartsCount) {
                        nNewPartId = mnCurrPart + 1;
                        if (marrPartAvailable[nNewPartId]) {
                            if (mnLearnMode == 1) {
                                if(mnCurrPart != mnPartsCount)
                                    mnCurrPart ++;

                                playExperience(mnCurrPart);
                            } else {
                                mnCurrPart = nNewPartId;
                                if(MyApplication.getInstance().isOfflineMode) {
                                    refreshOfflineWebView();
                                } else {
                                    String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                            "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + (mnCurrPart + 1) + "&kind=" + Constants.STUDY_TYPE_HEARING;

                                    MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                                }
                            }
                            findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                        } else {
                            MessageAlert.showMessage(LearnActivity.this, "您没有权限！");
                        }
                    } else {
                        MessageAlert.showMessage(LearnActivity.this, "没有以后的部分！");
                    }
                }
            }

        });

        textBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(!mfTextShow)
                    textBtn.setImageResource(R.drawable.textdisable);
                else
                    textBtn.setImageResource(R.drawable.textenable);

                mfTextShow = !mfTextShow;

                String strtmp = "javascript:ShowText(" +mfTextShow + ");";
                webView.loadUrl(strtmp);
            }
        });

        fabMenu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (findViewById(R.id.layout_learn_menu).getVisibility() == View.VISIBLE)
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                else
                    findViewById(R.id.layout_learn_menu).setVisibility(View.VISIBLE);
            }
        });

        fabInflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
                findViewById(R.id.lyt_touch_pad).setVisibility(View.VISIBLE);
            }
        });

        fabParts[0].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (marrPartAvailable[0]){
                    mnCurrPart = 0;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);
                    }else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 1 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });

        fabParts[1].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (marrPartAvailable[1]) {
                    mnCurrPart = 1;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);
                    } else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 2 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });

        fabParts[2].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (marrPartAvailable[2]) {
                    mnCurrPart = 2;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);

                    } else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 3 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });

        fabParts[3].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (marrPartAvailable[3]) {
                    mnCurrPart = 3;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);

                    } else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 4 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });

        fabParts[4].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (marrPartAvailable[4]) {
                    mnCurrPart = 4;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);

                    } else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 5 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });

        fabParts[5].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (marrPartAvailable[5]) {
                    mnCurrPart = 5;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);

                    } else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 6 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });

        fabParts[6].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (marrPartAvailable[6]) {
                    mnCurrPart = 6;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);

                    } else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 7 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });

        fabParts[7].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (marrPartAvailable[7]) {
                    mnCurrPart = 7;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);

                    } else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 8 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });

        fabParts[8].setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                mnQuizTestType = 1;
                if (marrPartAvailable[8]) {
                    mnCurrPart = 8;
                    if (mnLearnMode == 1) {
                        playExperience(mnCurrPart);

                    } else {
                        if(MyApplication.getInstance().isOfflineMode) {
                            refreshOfflineWebView();
                        } else {
                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + 9 + "&kind=" + Constants.STUDY_TYPE_HEARING;

                            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                        }
                    }
                    findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
                    closeFABMenu();
                }
            }
        });


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    @Override
//    public void onBackPressed() {
//        // Do Here what ever you want do on back press;
//    }


    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        return false;
    }
    private void showFABMenu(){
        isFABOpen = true;

        fabInflate.animate().rotationBy(90);

        findViewById(R.id.lyt_bottom_menu).setVisibility(View.VISIBLE);
        findViewById(R.id.lyt_top_menu).setVisibility(View.VISIBLE);
        fabhome.setEnabled(true);
        previoudBtn.setEnabled(true);
        refreshBtn.setEnabled(true);
        nextBtn.setEnabled(true);
        textBtn.setEnabled(true);
        campBtn.setEnabled(true);

        float rOff = getResources().getDimension(R.dimen.learn_move_quiz);
        for (int i = 0; i < mnPartsCount; i++){
            rOff += getResources().getDimension(R.dimen.learn_move_off);
            fabParts[mnPartsCount - i - 1].animate().translationX(-rOff);
        }
        rOff += getResources().getDimension(R.dimen.learn_move_off);
        fabMenu.animate().translationX(-rOff);
    }

    private void closeFABMenu(){
        isFABOpen=false;
//        fabBGLayout.setVisibility(View.GONE);

        fabInflate.animate().rotationBy(-90);
        fabhome.setEnabled(false);
        previoudBtn.setEnabled(false);
        refreshBtn.setEnabled(false);
        nextBtn.setEnabled(false);
        textBtn.setEnabled(false);
        campBtn.setEnabled(false);
        findViewById(R.id.lyt_top_menu).setVisibility(View.GONE);

        fabMenu.animate().translationX(0);
        fabParts[0].animate().translationX(0);
        fabParts[1].animate().translationX(0);
        fabParts[2].animate().translationX(0);
        fabParts[3].animate().translationX(0);
        fabParts[4].animate().translationX(0);
        fabParts[5].animate().translationX(0);
        fabParts[6].animate().translationX(0);
        fabParts[7].animate().translationX(0);
        fabParts[8].animate().translationX(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen)
                    findViewById(R.id.lyt_bottom_menu).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        //findViewById(R.id.lyt_bottom_menu).animate().alpha(0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void goPurchaseActivity()
    {
        Intent purchaseActivity = new Intent(this, PurchaseCampActivity.class);
        purchaseActivity.putExtra(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Entry_Purchase_Learn);
        startActivity(purchaseActivity);
    }

    public void setTitle(){
        String strTitle = String.format("CAMP %d %d-", mnCurrLevel, mnCurrLesson + 1);
        if (mnCurrPart == mnPartsCount - 1){
            strTitle += "QUIZ";
        }else
            strTitle += String.format("%02d", mnCurrPart + 1);

        for(int i = 0 ; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size();i++) {
            if(MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mnLevelId == mnCurrLevel)
            {
                MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).setMnCurrentLessonId(mnCurrLesson + 1);
                MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).setMnCurrentPartId(mnCurrPart + 1);
                if(mnCurrLesson >= mnProgressLesson) {
                    if (mnProgressPart < mnCurrPart)
                        mnProgressPart = mnCurrPart;

                    MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).setMnPartId(mnProgressPart + 1);

                }
            }
        }
        lessonTxt.setText(strTitle);
    }
    private String offlineLink = "";
    public void refreshWebView(){
        if(m_progressBar.getVisibility() == View.GONE)
            m_progressBar.setVisibility(View.VISIBLE);

        webView.loadUrl(MyApplication.mNetProc.mLoginUserInf.mstrLessonURL);
        refreshParts();
        setTitle();
//        webView.loadUrl("file:///assets/M3_L50_V1.html");
    }
    public void refreshOfflineWebView(){
        offlineLink = "file:///" + getApplicationContext().getFilesDir();// + "basecamp/L" + lessonId + "/M3_L" + lessonId + "_V" + String.valueOf(mnCurrPart + 1) + ".html";
        if(1 <= mnCurrLevel && mnCurrLevel <= 4) {
            int lnum = mnCurrLesson + 24 * (mnCurrLevel - 1);
            String strLessonId = getLessonName(lnum);
            offlineLink += "/content/basecamp/L" + strLessonId + "/M3_L" + strLessonId + "_V" + String.valueOf(mnCurrPart + 1) + ".html";
        } else if (5 <= mnCurrLevel && mnCurrLevel <=8) {
            int lnum = mnCurrLesson + 24 * (mnCurrLevel - 5);
            String strLessonId = getLessonName(lnum);
            offlineLink += "/content/camp750/L" + strLessonId + "/M2_L" + strLessonId + "_V" + String.valueOf(mnCurrPart + 1) + ".html";
        } else if (9 <= mnCurrLevel && mnCurrLevel <= 12) {
            int lnum = mnCurrLesson + 24 * (mnCurrLevel - 9);
            String strLessonId = getLessonName(lnum);
            offlineLink += "/content/camp1500/L" + strLessonId + "/M1_L" + (lnum + 1) + "_V" + String.valueOf(mnCurrPart) + ".html";
        }

        webView.loadUrl(offlineLink);
        refreshParts();
        setTitle();
    }
    private String getLessonName(int id)
    {
        String lessonId = "";

        if(id + 1 < 10)
            lessonId = "0" +  String.valueOf(id + 1);
        else
            lessonId = String.valueOf(id + 1);

        return lessonId;
    }
    public void refreshParts(){

        // ////ZhugeSDK.getInstance().startTrack(Constants.Zhuge_Event_Period_Learn_LearningPage);

        for (int i = 0; i < mnPartsCount; i++){

            if (marrPartAvailable[i])
                if (mnCurrPart == i){
                    fabParts[i].setImageResource(PART_DRAWABLE_SELECTED[i]);
                    if(mnCurrLevel < Constants.DIFF_PART_LEVEL)
                    {
                        fabParts[mnPartsCount - 1].setImageResource(PART_DRAWABLE_SELECTED[Constants.BIG_PARTS_COUNT - 1]);
                    }

                }else {
                    fabParts[i].setImageResource(PART_DRAWABLE_AVAILABLE[i]);
                    if(mnCurrLevel < Constants.DIFF_PART_LEVEL)
                    {
                        fabParts[mnPartsCount - 1].setImageResource(PART_DRAWABLE_AVAILABLE[Constants.BIG_PARTS_COUNT - 1]);
                    }

                }
            else {
                fabParts[i].setImageResource(PART_DRAWABLE_UNAVAILABLE[i]);
                if(mnCurrLevel < Constants.DIFF_PART_LEVEL)
                {
                    fabParts[mnPartsCount - 1].setImageResource(PART_DRAWABLE_UNAVAILABLE[Constants.BIG_PARTS_COUNT - 1]);
                }

            }

        }

    }

    public void onMyCamp(View view){

        String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                "&type=1" + "&level_id=" + mnCurrLevel;
        MyApplication.mNetProc.getQuizTestResult("getQuizTestResult", strRequestParameter);
    }

    private void AvailableNextLesson()
    {
        if (!marrLessongAvailable[mnCurrLesson + 1]){marrLessongAvailable[mnCurrLesson + 1] = true; madptLessonGrid.setLessonStatus(mnCurrLesson + 1, Constants.LESSON_AVAILABLE);}
        for(int i = 0 ; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size() ; i++){
            if(MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mnLevelId == mnCurrLevel) {
                MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mnPartId = 1;
                if(mnProgressLesson < 25 )
                    MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mnLessonId = mnProgressLesson + 2;
            }
        }
        String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnProgressLesson + 2) + "&part_id=" + 1 + "&kind=" + Constants.STUDY_TYPE_HEARING;

        MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, false);

    }
    public void ContinueLesson(){
//        if(MyApplication.mNetProc.mLoginUserInf.muserType == 2 || MyApplication.mNetProc.mLoginUserInf.muserType == 5) //체험사용자
//            return;
        if(mnCurrLesson == 23) {
            if(mnCurrLevel == 12)
                return;
            PerformTest();
            return;
        }
        mnCurrLesson++;
        mnCurrPart = 0;

        if (!marrLessongAvailable[mnCurrLesson]){
            marrLessongAvailable[mnCurrLesson] = true;
            madptLessonGrid.setLessonStatus(mnCurrLesson, Constants.LESSON_SELECTED);
        } else
            madptLessonGrid.setLessonStatus(mnCurrLesson, Constants.LESSON_SELECTED);

        if (mnCurrLesson < mnProgressLesson){
            for (int i = 0; i < mnPartsCount; i++){
                marrPartAvailable[i] = true;
            }
        }else if(mnCurrLesson >= mnProgressLesson){
            if(mnCurrLevel > 8) {
                if (mnProgressPart == 8)
                    mnProgressPart = 0;
            }else {
                if (mnProgressPart == 7)
                    mnProgressPart = 0;
            }
            mnProgressLesson++;
            for (int i = 0; i < mnPartsCount; i++){
                marrPartAvailable[i] = i <= mnProgressPart;
            }

        }
        refreshParts();
        if(MyApplication.getInstance().isOfflineMode) {
            refreshOfflineWebView();
        } else {
            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + (mnCurrPart + 1) + "&kind=" + Constants.STUDY_TYPE_HEARING;

            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
        }

    }
    public void endPart(){
        mnCurrPart++;
        if (mnCurrPart < mnPartsCount) {

            if (!marrPartAvailable[mnCurrPart]) {
                marrPartAvailable[mnCurrPart] = true;

                fabParts[mnCurrPart].setImageResource(PART_DRAWABLE_AVAILABLE[mnCurrPart]);
            }

        }
        if(MyApplication.getInstance().isOfflineMode) {
            refreshOfflineWebView();
        }else {
            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                    "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + (mnCurrPart + 1) + "&kind=" + Constants.STUDY_TYPE_HEARING;

            MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
        }
        //Zhuge
//        String strCampCourse = "Camp" + mnCurrLevel + "-" + (mnCurrLesson + 1) + "-" + (mnCurrPart + 1);
//        JSONObject eventObject = new JSONObject();
//        try {
//            eventObject.put(Constants.Zhuge_Property_Duration, strCampCourse);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ////ZhugeSDK.getInstance().endTrack(Constants.Zhuge_Event_Period_Learn_LearningPage, eventObject);
    }
    boolean isTestAgain = false;
    public void OnRefreshLevelTest(View v)
    {
                totalPot -= partPoint;
                String correctedCountStr = String.format("%03d", totalPot);
                Log.d("correctedCountStr", correctedCountStr);
                ((TextView) findViewById(R.id.testscoretxt1)).setText(correctedCountStr.substring(0, 1));
                ((TextView) findViewById(R.id.testscoretxt2)).setText(correctedCountStr.substring(1, 2));
                ((TextView) findViewById(R.id.testscoretxt3)).setText(correctedCountStr.substring(2, 3));

                totalQuestion -= partQuestion;
                String questionCountStr = String.format("%03d", totalQuestion);
                Log.d("testQuestionCount", questionCountStr);
                ((TextView) findViewById(R.id.testquestioncount)).setText(questionCountStr + "/100");

                partPoint = 0;
                partQuestion = 0;
                
        findViewById(R.id.leveltestbtn).setVisibility(View.GONE);
        isTestAgain = true;
        webView.loadUrl(MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.get(levelTestposition).mstrPath);
    }
    public void OnNextLevelTest(View v)
    {
        findViewById(R.id.leveltestbtn).setVisibility(View.GONE);
        isTestAgain = false;
        levelTestposition++;
        Log.d("testQuestionCount", "OnNextLevelTest");
        //((LearnActivity)MyApplication.getInstance().getCurrentActivity()).finishLevelTest();
        webView.loadUrl(MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.get(levelTestposition).mstrPath);
    }
    public class JavaScriptInterface {
        public JavaScriptInterface() {

        }

        @JavascriptInterface
        public void EndPartFromJS() {
            boolean post = mHandler.post(new Runnable() {
                public void run() {
                    endPart();
                }
            });
        }

        @JavascriptInterface
        public String EndLessonFromJS(String strPoint) {

            mstrPoint = strPoint;
            boolean post = mHandler.post(new Runnable() {
                public void run() {
                    if(mnLearnMode == 1) {
                        Intent finishTrialAct = new Intent(LearnActivity.this, FinishTrialAct.class);
                        startActivity(finishTrialAct);
                    } else {
                        if(!MyApplication.getInstance().isOfflineMode) {
                            ((LearnActivity)MyApplication.getInstance().getCurrentActivity()).finishLesson();

                            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                    "&type=" + mnQuizTestType + "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) +
                                    "&part_id=" + (mnCurrPart + 1) + "&score=" + mstrPoint;

                            MyApplication.mNetProc.saveQuizTestResult("saveQuizTestResult", strRequestParameter, Constants.TEST_RESULT_LESSON);
                        }
//                        AvailableNextLesson();
                    }
                }
            });
            int score = Integer.parseInt(strPoint);

            String index;
            float percent = (float) Math.floor((float)score/1000f * 100f);
            if (percent>69 && percent<80) {
                index = "bronze" ; // bronze
            } else if (percent>79 && percent<90) {
                index = "silver" ; // silver
            } else if (percent>89 && percent<101) {
                index = "gold" ; //gold
            } else {
                index = "none"; //none
            }

            return index;

        }
        @JavascriptInterface
        public void ContinueLessonFromJS(){
            boolean post = mHandler.post(new Runnable() {
                public void run() {
                    ContinueLesson();
                }
            });

        }

        @JavascriptInterface
        public void finishHTMLloading() {

            mHandler.post(new Runnable() {
                public void run() {
                    Log.d("finishHtml", "finishLoading");
                    if(m_progressBar.getVisibility() == View.VISIBLE)
                        m_progressBar.setVisibility(View.GONE);
                    String strtmp = "javascript:ShowText(" + mfTextShow + ");";
                    webView.loadUrl(strtmp);

                }
            });
        }

        @JavascriptInterface
        public void refreshLessonFromJS() {

            mHandler.post(new Runnable() {

                public void run() {
                    mnCurrPart = 0;
                    if(MyApplication.getInstance(). isOfflineMode) {
                        refreshOfflineWebView();
                    } else {
                        String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                                "&level_id=" + mnCurrLevel + "&lesson_id=" + (mnCurrLesson + 1) + "&part_id=" + (mnCurrPart + 1) + "&kind=" + Constants.STUDY_TYPE_HEARING;

                        MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter, true);
                    }

                }
            });
        }

        @JavascriptInterface
        public void refreshQuizFromJS() {

            webView.reload();
        }
        int levelskip = 1;
        int partskip = 0;
        int themePoint = 0;
        int currenLevelTestCount = 0;

        @JavascriptInterface
        public void sendTestResult(String point, String questionCount)
        {
            if(mnLearnMode == 2) {
                int intPoint = Integer.parseInt(point);
                int intQuestCnt = Integer.parseInt(questionCount);

                if (intPoint > (intQuestCnt - 3))
                    themePoint++;
                partskip++;
                currenLevelTestCount = placementTestLinks.get(levelskip).size();
                if (partskip == currenLevelTestCount) {
                    if (themePoint == currenLevelTestCount) {
                        if (levelskip == 11) {
                            recommandLevel = 11;
                            gotoPurchaseLevel(recommandLevel);
                            return;
                        } else {
                            levelskip = levelskip + 2;
                            partskip = 0;

                        }
                    } else {
                        if (levelskip != 1)
                            recommandLevel = levelskip - 1;

                        gotoPurchaseLevel(recommandLevel);
                        return;
                    }
                }

                boolean post = mHandler.post(new Runnable() {
                    public void run() {
                        webView.loadUrl(placementTestLinks.get(levelskip).get(partskip));
                    }
                });
            } else if(mnLearnMode == 3)
            {
                if(levelTestposition == MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.size() - 1) {
                    boolean post2 = mHandler.post(new Runnable() {
                        public void run() {
                            findViewById(R.id.leveltestbtn).setVisibility(View.INVISIBLE);
                        }
                    });
                    ((LearnActivity)MyApplication.getInstance().getCurrentActivity()).finishLevelTest();

                } else {

                    partPoint = Integer.parseInt(point);
                    partQuestion = Integer.parseInt(questionCount);

                    if(!isTestAgain) {
//                        totalPot += Integer.parseInt(point);
//                        totalQuestion += Integer.parseInt(questionCount);
                    }
                    if(!point.equals(questionCount)) {
                        boolean post1 = mHandler.post(new Runnable() {
                            public void run() {
                                findViewById(R.id.leveltestbtn).setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        levelTestposition++;
                        Log.d("testQuestionCount", "sendTestResult");
                        //ShowQuestionInScorePanel();
                        boolean post = mHandler.post(new Runnable() {
                            public void run() {
                                findViewById(R.id.leveltestbtn).setVisibility(View.GONE);
                                webView.loadUrl(MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.get(levelTestposition).mstrPath);
                            }
                        });
                    }
                }
            }
        }

        @JavascriptInterface
        public void AddCorrectPoint() {
//            if (!isTestAgain)
            {
                totalPot += 1;
                String correctedCountStr = String.format("%03d", totalPot);
                Log.d("correctedCountStr", correctedCountStr);
                ((TextView) findViewById(R.id.testscoretxt1)).setText(correctedCountStr.substring(0, 1));
                ((TextView) findViewById(R.id.testscoretxt2)).setText(correctedCountStr.substring(1, 2));
                ((TextView) findViewById(R.id.testscoretxt3)).setText(correctedCountStr.substring(2, 3));

            }
//            else{
//                totalPot = 0;
//                String correctedCountStr = String.format("%03d", totalPot);
//                Log.d("correctedCountStr", correctedCountStr);
//                ((TextView) findViewById(R.id.testscoretxt1)).setText(correctedCountStr.substring(0, 1));
//                ((TextView) findViewById(R.id.testscoretxt2)).setText(correctedCountStr.substring(1, 2));
//                ((TextView) findViewById(R.id.testscoretxt3)).setText(correctedCountStr.substring(2, 3));
//            }
        }
        @JavascriptInterface
        public void AddQuestionCount(String question) {
//            if (!isTestAgain)
            {
                Log.d("testQuestionCount", question);
                if(totalQuestion == 100) {
                    ((LearnActivity) MyApplication.getInstance().getCurrentActivity()).finishLevelTest();
                    return ;
                }
                totalQuestion ++;
                String questionCountStr = String.format("%03d", totalQuestion);
                Log.d("testQuestionCount", questionCountStr);
                ((TextView) findViewById(R.id.testquestioncount)).setText(questionCountStr + "/100");
            }
//            else{
//                totalQuestion = 1;
//                String questionCountStr = String.format("%03d", totalQuestion);
//                Log.d("testQuestionCount", questionCountStr);
//                ((TextView) findViewById(R.id.testquestioncount)).setText(questionCountStr + "/100");
//            }
        }
    }

    public void finishLesson(){
//        if(!mstrPoint.equals("1000"))
//            return;
        if(mnLearnMode == 3)
            return;
        if(MyApplication.mNetProc.mLoginUserInf.isShared.equals("1"))
            return;
//        Intent finishQuizIntent = new Intent(LearnActivity.this, FInishQuiz.class);
//        finishQuizIntent.putExtra("Finish_Lesson", true);
//        finishQuizIntent.putExtra("Level_Id", mnCurrLevel);
//        finishQuizIntent.putExtra("Lesson_Id", mnCurrLesson);
//        finishQuizIntent.putExtra("Part_Id", mnCurrPart + 1);
//        startActivity(finishQuizIntent);

        quizResultPanel.setVisibility(View.VISIBLE);
    }

    public void finishLevelTest()
    {
        Log.d("finishlevelTest", "finishlevelTest");
        //findViewById(R.id.leveltestbtn).setVisibility(View.INVISIBLE);
        String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                "&type=2" + "&level_id=" + mnCurrLevel + "&lesson_id=25" + "&part_id=8" + "&score=" + totalPot;

        MyApplication.mNetProc.saveQuizTestResult("saveQuizTestResult", strRequestParameter, Constants.TEST_RESULT_LEVELTEST);
        OpenLevelTestResultPanel(totalPot);
//        Intent finishLevelTestAct = new Intent(this, ActFinishLevelTest.class);
//        finishLevelTestAct.putExtra("point", totalPot);
//        finishLevelTestAct.putExtra("questioncount", totalQuestion);
//        startActivity(finishLevelTestAct);
    }

    public void OpenLevelTestResultPanel(int point)
    {
//        Toast.makeText(getApplicationContext(), "testQuestionCount", Toast.LENGTH_LONG).show();

        Activity act = MyApplication.getInstance().getCurrentActivity();
        act.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                leveltestResultPanel.setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.pointleveltest1)).setText(String.valueOf(point));
            } });
    }
    public void OnLevelTestButton(View v)
    {
        leveltestResultPanel.setVisibility(View.GONE);
        Intent learnAct =  new Intent(this, MainActivity.class);
        startActivity(learnAct);
        MainActivity.mainActivityInstance.goFragmentLearn();
    }
    int levelTestposition = 0;
    int totalPot = 0;
    int totalQuestion = 1;
    public void refreshLevelTest(){
        levelTestposition = 0;
        totalPot = 0;

        if(MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.size() == 0)
            return;

        webView.loadUrl(MyApplication.mNetProc.mLoginUserInf.mlistLevelTestUrl.get(levelTestposition).mstrPath);
        lessonTxt.setText("CAMP " + mnCurrLevel + " TEST");
    }

    public void setMyCampInf(){
        if (mdlgMyCamp != null){
            mMyCampAdapter.setLessonPt(MyApplication.mNetProc.mLoginUserInf.mlistScores);
            mMyCampAdapter.notifyDataSetChanged();
        }
    }
    int soundIrellgalCount = 0;
    @Override
    protected void onPause() {
        webView.pauseTimers();

        webView.loadUrl("javascript:SoundPauseandResume(" + true + ")");
        super.onPause();
    }

    @Override
    public void onResume() {
        soundIrellgalCount++;
        webView.resumeTimers();
        webView.loadUrl("javascript:SoundPauseandResume(" + false + ")");
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                "&level_id=" + mnCurrLevel;
        webView.destroy();
        MyApplication.mNetProc.saveLevel("saveLevel", strRequestParameter);
        myTimer.cancel();
        //Zhuge
//        String strCampCourse = "Camp" + mnCurrLevel + "-" + (mnCurrLesson + 1) + "-" + (mnCurrPart + 1);
//        JSONObject eventObject = new JSONObject();
//        try {
//            eventObject.put(Constants.Zhuge_Property_Duration, strCampCourse);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ////ZhugeSDK.getInstance().endTrack(Constants.Zhuge_Event_Period_Learn_LearningPage, eventObject);

        //Zhuge
//        JSONObject eventObject1 = new JSONObject();
//        try {
//            eventObject1.put(Constants.Zhuge_Property_Duration, "Camp" + mnCurrLevel);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ////ZhugeSDK.getInstance().endTrack(Constants.Zhuge_Event_Period_Learn_LearningPage_Camp, eventObject1);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    public void DoTest(View v){
        PerformTest();
    }
    private void PerformTest()
    {
        partPoint = 0;
        partQuestion = 0;

        totalPot = 0;
        String correctedCountStr = String.format("%03d", totalPot);
        Log.d("correctedCountStr", correctedCountStr);
        ((TextView) findViewById(R.id.testscoretxt1)).setText(correctedCountStr.substring(0, 1));
        ((TextView) findViewById(R.id.testscoretxt2)).setText(correctedCountStr.substring(1, 2));
        ((TextView) findViewById(R.id.testscoretxt3)).setText(correctedCountStr.substring(2, 3));

        totalQuestion = 1;
        String questionCountStr = String.format("%03d", totalQuestion);
        Log.d("testQuestionCount", questionCountStr);
        ((TextView) findViewById(R.id.testquestioncount)).setText(questionCountStr + "/100");

        if(mnCurrLevel == 12) // camp 12 doesnt have level test.
        {
            MessageAlert.showMessage(this, "级别12不带水平测试。");
            return;
        }
        mnCurrLesson = 24;//Level Test
        mnLearnMode = 3; //level test
        String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                "&level_id=" + MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel;

        MyApplication.mNetProc.getLevelTestUrl("getLevelTestUrl", strRequestParameter);

        closeFABMenu();
        DisableBtnInTest();

//        if(State.getState() == State.app_state.dev)
        {
            if(mnCurrLevel <= 6)
            {
                m_testPanel.setVisibility(View.VISIBLE);
            }
            else{
                m_testPanel.setVisibility(View.INVISIBLE);
            }
        }
//        else
//        {
//            m_testPanel.setVisibility(View.INVISIBLE);
//        }
    }
    private void DisableBtnInTest()
    {
        madptLessonGrid.setLessonStatus(mnCurrLesson, Constants.LESSON_SELECTED);
        for (int i = 0; i < mnPartsCount; i++){
            fabParts[i].setImageResource(PART_DRAWABLE_UNAVAILABLE[i]);
            marrPartAvailable[i] = false;
            if(mnCurrLevel < Constants.DIFF_PART_LEVEL)
            {
                fabParts[mnPartsCount - 1].setImageResource(PART_DRAWABLE_UNAVAILABLE[Constants.BIG_PARTS_COUNT - 1]);
                marrPartAvailable[mnPartsCount - 1] = false;
            }
        }
        ((TextView)findViewById(R.id.lobtn_test_text)).setTextColor(Color.RED);
        previoudBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        refreshBtn.setVisibility(View.GONE);
        findViewById(R.id.layout_learn_menu).setVisibility(View.INVISIBLE);
    }
    private void EnableBtnInTest()
    {
        previoudBtn.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
        refreshBtn.setVisibility(View.VISIBLE);
    }
    public void playExperience(int indexPart)
    {
        mstrCurrentUrl = mCurrLesson.mlistParts.get(indexPart).mstrUrl;
        webView.loadUrl(mstrCurrentUrl);
        refreshParts();
        setTitle();
    }

    public static Map<Integer, ArrayList<String>> makePlacementTestList()
    {
        Map<Integer, ArrayList<String>> tempTestLinks = new HashMap<Integer, ArrayList<String>>();

        for(int n = 1 ; n <= 12 ; n++) {
            ArrayList<String> strPathList = new ArrayList<String>();
            for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistSystemTestUrl.size(); i++) {
                if (MyApplication.mNetProc.mLoginUserInf.mlistSystemTestUrl.get(i).mnLevel == n)
                {
                    strPathList.add(MyApplication.mNetProc.mLoginUserInf.mlistSystemTestUrl.get(i).mstrPath);
                }
            }
            tempTestLinks.put(n, strPathList);
        }

        return tempTestLinks;
    }

    private void gotoPurchaseLevel (int recomLevel)
    {
        Intent finishPlacementTestDlg = new Intent(this, FinishTestAct.class);
        finishPlacementTestDlg.putExtra("recommandLevel", recomLevel);
        startActivity(finishPlacementTestDlg);

        //Zhuge
        //////ZhugeSDK.getInstance().endTrack(Constants.Zhuge_Event_Period_Test_TestingPage, null);

    }

    void QuitLevelTest(){
        mnLearnMode = 0;
        totalQuestion = 0;

        ((TextView)findViewById(R.id.lobtn_test_text)).setTextColor(getApplicationContext().getResources().getColor(R.color.green_dark));
        m_testPanel.setVisibility(View.INVISIBLE);
//        findViewById(R.id.lyt_top_menu).setVisibility(View.VISIBLE);
//        findViewById(R.id.fabInflate).setVisibility(View.VISIBLE);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub

            //Start this timer when you create you task

//            myTimer.schedule(new loaderTask(), 10000); // 3000 is delay in millies
            isPageLoadedComplete =false;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request) {
            // TODO Auto-generated method stub

//            view.loadUrl(MyApplication.mNetProc.mLoginUserInf.mstrLessonURL);
//            MessageAlert.showAlertDlg(LearnActivity.this, "url!", MyApplication.mNetProc.mLoginUserInf.mstrLessonURL + "////////////////////" + view.getUrl());

            return false;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            isPageLoadedComplete = true;

            if(m_progressBar.getVisibility() == View.VISIBLE)
                m_progressBar.setVisibility(View.GONE);

        }
        @Override
        public void onLoadResource(WebView view, String url) {
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            handler.proceed();
        }
        @Override
        public void onReceivedHttpError(
                WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        }

    }
    public class myChromeClient extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {

            } else {
//                if (m_progressBar.getVisibility() == View.GONE) {
//                    m_progressBar.setVisibility(View.VISIBLE);
//                }
                m_progressBar.setProgress(newProgress);
            }
        }
    }

    class loaderTask extends TimerTask {
        public void run() {
            if (isPageLoadedComplete) {
            } else {
                boolean post = mHandler.post(new Runnable() {
                    public void run() {
                    }
                });

                //show error message as per you need.
            }


        }
    }
}

