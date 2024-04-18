package com.usacamp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.iflytek.cloud.SpeechUtility;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.services.OssService;
import com.usacamp.utils.FinalResult;
import com.usacamp.utils.MakeJsonForSpeaking;
import com.usacamp.utils.MessageAlert;
import com.usacamp.utils.Sentence;
import com.usacamp.utils.SpeakingResult;
import com.usacamp.utils.UploadHelper;
import com.usacamp.utils.XmlResultParser;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.internal.NamedRunnable;

import static com.iflytek.cloud.SpeechEvaluator.createEvaluator;

public class SpeakingActivity extends BaseActivity {
    private android.app.AlertDialog speaking_start_toast;
    FloatingActionButton fab;
    ImageButton fabMic,fabresultPlay, fabResult;
    Button fabMenu, fabHome, fabA, fabB;
    TextView playtext, clocktext;
    LinearLayout refreshly, playbtnly, nextly;
    CircularProgressBar speakingProgressBar;
    boolean isPressedFab = false ;
    float currentProgress = 0;
    Timer timer, bubbletimer, clocktimer;
    boolean isRecoding = false;
    Button preLessonButton ;
    private final static String PREFER_NAME = "ise_settings";
    // 评测语种
    private String language;
    // 评测题型
    private String category;
    // 结果等级
    private Toast toast;
    private Handler mHandler;
    private String result_level;
    private static String TAG = "speakingVoice";
    private String lastResult, sentence = "";
    private int sentenceIndex = -1;
    private WebView speakingWebView;
    private SpeechEvaluator ise;
    private int clock_num = 3;
    private final int URL_REQUEST_CODE = 0X001;
    ImageView speakingToastImg, networkToastImg, resultPanelImg, bubbleSpeechToast;
    Group buttonGroup;
    private final boolean fTextShow = true;
    MediaPlayer mp;
    TextView titletv ;
    SpeakingResult result ;
    List<Sentence> sentencesList = new ArrayList<Sentence>();
    int speakingLessonIndex = 1, speakingPartIndex = 1;
    int readDuration = 0;
    private Dialog mdlgProgress = null;
    UploadHelper uploadHelper;
    private boolean isGenrateResult = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking);
        requestPermissions();
        registerReceiver();
        isGenrateResult = false;
        boolean isSpeechHelp = MyApplication.getInstance().getSpeechFlag();

        bubbleSpeechToast = (ImageView) findViewById(R.id.bubblespeech);
        if(!isSpeechHelp) {
            MyApplication.getInstance().saveSpeechFlag(true);
            bubbletimer = new Timer();
            bubbletimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            bubbleSpeechToast.setVisibility(View.VISIBLE);
                        }

                    });

                    bubbletimer.cancel();
                    bubbletimer.purge();
                    bubbletimer = null;
                }
            },3000, 1000);
        }

        MyApplication.getInstance().onActivityCreated(this, savedInstanceState);
        speakingWebView = (WebView) findViewById(R.id.speakingwebView);
        titletv = (TextView) findViewById(R.id.speakingtitle);
        speakingWebView.getSettings().setJavaScriptEnabled(true);
        speakingWebView.getSettings().setDomStorageEnabled(false);
        speakingWebView.getSettings().setSaveFormData(true);
        speakingWebView.getSettings().setAllowContentAccess(true);
        speakingWebView.getSettings().setLoadWithOverviewMode(true);
        speakingWebView.getSettings().setUseWideViewPort(true);
        //speakingWebView.getSettings().setAppCacheEnabled(false);
        speakingWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        speakingWebView.setInitialScale(1);
        speakingWebView.setClickable(true);
        //speakingWebView.loadUrl("http://dev.usacamp.cn/test/speaking/L01/S1_L01_A.html");
        speakingWebView.addJavascriptInterface(new JavaScriptInterface(), "interface");
        fab = (FloatingActionButton) findViewById(R.id.speakingfab);
        fabA = (Button) findViewById(R.id.speakingfabA);
        fabB = (Button) findViewById(R.id.speakingfabB);
        fabHome = (Button) findViewById(R.id.speakingfabHome);
        fabMenu = (Button) findViewById(R.id.speakingfabMenu);
        buttonGroup = (Group) findViewById(R.id.buttongroup);
        ise = createEvaluator(SpeakingActivity.this, null);
        fabMic = (ImageButton) findViewById(R.id.speakingfabmic);
        resultPanelImg = (ImageView) findViewById(R.id.imageView5);
        fabResult = (ImageButton) findViewById(R.id.fabresultbutton) ;
        speakingProgressBar = (CircularProgressBar) findViewById(R.id.speakingprogressbar);
        fabresultPlay = (ImageButton) findViewById(R.id.speakingfabplay2);
        clocktext = (TextView) findViewById(R.id.clocktext);
        speakingToastImg = (ImageView) findViewById(R.id.imageView7);
        networkToastImg = (ImageView) findViewById(R.id.networktoast);
        refreshly = (LinearLayout) findViewById(R.id.refreshly);
        playbtnly = (LinearLayout) findViewById(R.id.playbtnly);
        nextly = (LinearLayout) findViewById(R.id.nextly);
        playtext = (TextView) findViewById(R.id.playtext);
        if(State.getSpeakingState() == State.speaking_state.normal) {
            speakingLessonIndex = MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(0).mnOralCurrentLessonId;
            speakingPartIndex = MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(0).mnOralCurrentPartId;
        } else {
            sentenceIndex = getIntent().getIntExtra("Skip_Position", 0);
            SpeakingResult historyResult = getIntent().getParcelableExtra("Read_Sentence");
            if(historyResult != null) {
                speakingLessonIndex = historyResult.lesson_id;
                speakingPartIndex = historyResult.part_id;
                sentencesList = historyResult.result;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            speakingWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            speakingWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        callGetLessonUrl(1,speakingLessonIndex,speakingPartIndex);
        showFabs();
        mHandler = new Handler();
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        fabA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.setSpeakingState(State.speaking_state.normal);
                callGetLessonUrl(1, speakingLessonIndex, 1);
            }
        });
        fabB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.setSpeakingState(State.speaking_state.normal);
                callGetLessonUrl(1, speakingLessonIndex, 2);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPressedFab)
                    showFabs();
                else
                    hideFabs();
            }
        });
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ise.cancel();
                ise.destroy();

                finish();

            }
        });

        fabResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clocktimer != null)
                    return;
                cancelSpeaking();
                showFinalResult();
            }
        });
        nextly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipNextSection();
            }
        });

        refreshly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshly.setVisibility(View.INVISIBLE);
                fabMic.setVisibility(View.VISIBLE);
                fabresultPlay.setVisibility(View.GONE);
                resultPanelImg.setVisibility(View.GONE);
                playtext.setText("开始跟读");
                if(mp != null) {
                    mp.release();
                    mp = null;
                }
                fabMic.callOnClick();
            }
        });

        fabresultPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp == null)
                    mp = new MediaPlayer();
                mp.reset();
                try {
                    String path = Constants.SPEAKING_AUDIO_PATH + sentencesList.get(sentenceIndex).sentence_id + ".wav";
                    mp.setDataSource(path);
                    mp.prepare();
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fabMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sentenceIndex < 0)
                    return;
                if(clocktimer != null)
                    return;
                if (bubbleSpeechToast.getVisibility() == View.VISIBLE)
                    bubbleSpeechToast.setVisibility(View.GONE);
                clock_num = 3;
                if (!isRecoding) {
                    nextly.setClickable(false);
                    isRecoding = true;
                    clocktext.setVisibility(View.VISIBLE);
                    clocktimer = new Timer();
                    clocktimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (clock_num == 0) {
                                        clocktimer.cancel();
                                        clocktimer.purge();
                                        clocktimer = null;
                                        clocktext.setVisibility(View.GONE);
                                        performFabMic();
                                    }
                                    clocktext.setText(String.valueOf(clock_num));
                                    clock_num -= 1;
                                }

                            });

                        }
                    }, 0, 1000);
                } else {
                    isRecoding = false;

                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                        timer = null;
                    }
                    readDuration = (int) currentProgress;
                    currentProgress = 0.0f;
                    speakingProgressBar.setProgress(0);

                    nextly.setClickable(true);
                    stopSpeaking();
                }
            }
        });

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder keyBuilder = new android.app.AlertDialog.Builder(SpeakingActivity.this);
                final View lessonView = LayoutInflater.from(SpeakingActivity.this).inflate(R.layout.activity_speaking_lesson_list, null);
                String packageName = getPackageName();
                keyBuilder.setView(lessonView);
                android.app.AlertDialog dialog = keyBuilder.create();
                for(int i = 1 ; i <= 24; i++) {
                    Button tmpBtn = (Button) lessonView.findViewById(getResources().getIdentifier("button" + i, "id", packageName));
                    if(i == speakingLessonIndex) {
                        tmpBtn.setBackground(getResources().getDrawable(R.drawable.green_dark_radius));
                        tmpBtn.setTextColor(Color.WHITE);
                    }
                    tmpBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(preLessonButton != null) {
                                preLessonButton.setBackground(getResources().getDrawable(R.drawable.white_radius_outline));
                                preLessonButton.setTextColor(Color.parseColor("#4BB524"));
                            }
                            ((Button)v).setBackground(getResources().getDrawable(R.drawable.green_dark_radius));
                            ((Button)v).setTextColor(Color.WHITE);
                            preLessonButton = (Button)v;
                            speakingLessonIndex = Integer.parseInt(((Button)v).getText().toString());
                            dialog.dismiss();
                            State.setSpeakingState(State.speaking_state.normal);
                            callGetLessonUrl(1,speakingLessonIndex, speakingPartIndex);

                        }
                    });
                }

                dialog.show();
            }
        });
    }

    void showFabs()
    {
        fab.setImageDrawable(getResources().getDrawable(R.drawable.speakingfabpressed));
        isPressedFab = true;
        fabHome.animate().translationY(0);
        fabMenu.animate().translationY(0);
        fabA.animate().translationY(0);
        fabB.animate().translationY(0);
    }

    void hideFabs()
    {
        fab.setImageDrawable(getResources().getDrawable(R.drawable.speakingfloatingbtn));
        isPressedFab = false;
        fabHome.animate().translationY(-200);
        fabMenu.animate().translationY(-200);
        fabA.animate().translationY(-200);
        fabB.animate().translationY(-200);
    }

    private void setParams() {
        SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        // 设置评测语言
        language = pref.getString(SpeechConstant.LANGUAGE, "en_us");
        // 设置需要评测的类型
        category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_sentence");
        // 设置结果等级（中文仅支持complete）
        result_level = pref.getString(SpeechConstant.RESULT_LEVEL, "plain");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        String vad_bos = pref.getString(SpeechConstant.VAD_BOS, "30000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        String vad_eos = pref.getString(SpeechConstant.VAD_EOS, "30000");
        // 语音输入超时时间，即用户最多可以连续说多长时间；
        String speech_timeout = pref.getString(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");

        ise.setParameter(SpeechConstant.LANGUAGE, language);
        ise.setParameter(SpeechConstant.ISE_CATEGORY, category);
        ise.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        ise.setParameter(SpeechConstant.VAD_BOS, vad_bos);
        ise.setParameter(SpeechConstant.VAD_EOS, vad_eos);
        ise.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
        ise.setParameter(SpeechConstant.RESULT_LEVEL, result_level);
        ise.setParameter(SpeechConstant.AUDIO_FORMAT_AUE,"opus");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        ise.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        String speechFileName = Constants.SPEAKING_AUDIO_PATH + sentencesList.get(sentenceIndex).sentence_id + ".wav";
        ise.setParameter(SpeechConstant.ISE_AUDIO_PATH, speechFileName);
        //通过writeaudio方式直接写入音频时才需要此设置
        //ise.setParameter(SpeechConstant.AUDIO_SOURCE,"-1");
    }

    void startSpeaking()
    {
        speakingToastImg.setVisibility(View.VISIBLE);
        if (ise == null) {
            return;
        }

        lastResult = null;

        setParams();
        Log.d("sentence",sentence);
        int ret = ise.startEvaluating(sentence, null, mEvaluatorListener);
    }
    void stopSpeaking()
    {
        if (ise.isEvaluating()) {
            ise.stopEvaluating();
            speakingToastImg.setVisibility(View.GONE);
        }
    }
    void cancelSpeaking()
    {
        if(ise.isEvaluating())
            ise.cancel();
        isRecoding = false;
        if(timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if(clocktimer != null) {
            clocktimer.cancel();
            clocktimer.purge();
            clocktimer = null;
        }
        currentProgress = 0.0f;
        readDuration = (int)currentProgress;
        speakingProgressBar.setProgress(0);
        speakingToastImg.setVisibility(View.GONE);
        nextly.setClickable(true);
        lastResult = null;
    }

    // 评测监听接口
    private final EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            Log.d(TAG, "evaluator result :" + isLast);

            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());
                lastResult = builder.toString();
                XmlResultParser resultParser = new XmlResultParser();
                FinalResult reslt = resultParser.parse(lastResult);
                Log.d(TAG, String.valueOf(reslt.total_score));

                isRecoding = false;
                if(timer != null)
                    timer.cancel();
                timer = null;
                currentProgress = 0.0f;
                speakingProgressBar.setProgress(0);

                refreshly.setVisibility(View.VISIBLE);
                showResult(reslt.total_score);

            }
        }

        @Override
        public void onError(SpeechError error) {
            if(error != null) {
                showTip("error:"+ error.getErrorCode() + "," + error.getErrorDescription());

            } else {
                Log.d(TAG, "evaluator over");
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d(TAG, "evaluator begin");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.d(TAG, "evaluator stoped");
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //showTip("当前音量：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因

        }

    };
    private void showTip(String str) {
        if(!TextUtils.isEmpty(str)) {
            toast.setText(str);
            toast.show();
        }
    }
    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]
                            {       Manifest.permission.LOCATION_HARDWARE,
                                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO},0x0010);
                }

                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void mscInit (String serverUrl){
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        StringBuffer bf = new StringBuffer();
        bf.append("appid="+Constants.SPEAKING_APPID);
        bf.append(",");
        if (!TextUtils.isEmpty(serverUrl)) {
            bf.append("server_url="+serverUrl);
            bf.append(",");
        }
        //此处调用与SpeechDemo中重复，两处只调用其一即可
        SpeechUtility.createUtility(this.getApplicationContext(), bf.toString());
        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);
    }

    private void mscUninit() {
        if (SpeechUtility.getUtility()!= null) {
            SpeechUtility.getUtility().destroy();
            try{
                new Thread().sleep(40);
            }catch (InterruptedException e) {
                Log.w(TAG,"msc uninit failed"+e.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (URL_REQUEST_CODE == requestCode) {
            Log.d(TAG,"onActivityResult>>");
            try{
                SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
                String server_url = pref.getString("url_preference","");
                String domain = pref.getString("url_edit","");
                Log.d(TAG,"onActivityResult>>domain = "+domain);
                if (!TextUtils.isEmpty(domain)) {
                    server_url = "http://"+domain+"/msp.do";
                }
                Log.d(TAG,"onActivityResult>>server_url = "+server_url);
                mscUninit();
                new Thread().sleep(40);
                //mscInit(server_url);
            }catch (Exception e) {
                showTip("reset url failed");
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        return;
//    }

    @Override
    public void onResume() {
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;

        super.onResume();
    }
    void showResult(float score)
    {
        int starCount = 0;
        if(score * 20 < 30) {
            resultPanelImg.setImageDrawable(getResources().getDrawable(R.drawable.speakingresultagain));
            starCount = 0;
        }else if(30 <= score * 20 && score *20 < 60) {
            resultPanelImg.setImageDrawable(getResources().getDrawable(R.drawable.speakingresultgood));
            starCount = 1;
        }else if(60 <= score * 20 && score * 20 < 85) {
            resultPanelImg.setImageDrawable(getResources().getDrawable(R.drawable.speakingresultgreat));
            starCount = 2;
        }else {
            resultPanelImg.setImageDrawable(getResources().getDrawable(R.drawable.speakingresultperfect));
            starCount = 3;
        }
        sentencesList.get(sentenceIndex).setResultInformation(readDuration,starCount,(int)score,Constants.SPEAKING_AUDIO_PATH+sentencesList.get(sentenceIndex).sentence_id + ".wav");
        resultPanelImg.setVisibility(View.VISIBLE);
        fabMic.setVisibility(View.GONE);
        fabresultPlay.setVisibility(View.VISIBLE);
        playtext.setText("播放录音");

//        resultPanel.setVisibility(View.VISIBLE);
        if(MyApplication.mNetProc.mLoginUserInf.mSentenceList.size() - 1 == sentenceIndex)
        {

            fabResult.setVisibility(View.VISIBLE);
            nextly.setVisibility(View.GONE);
        } else {
            fabResult.setVisibility(View.GONE);
            nextly.setVisibility(View.VISIBLE);
        }
        isGenrateResult = true;
    }
    int GetReadSentenceCount()
    {
        int count = 0;
        for(int i = 0 ; i < sentencesList.size() ; i++)
        {
            if(!sentencesList.get(i).sound_path.equals(""))
                count++;
        }
        return count;
    }
    int GetHighScoreSentenceCount()
    {
        int count = 0;
        for(int i = 0 ; i < sentencesList.size() ; i++)
        {
            if(sentencesList.get(i).star == 3)
                count++;
        }
        return count;
    }
    void initVariables()
    {
        isGenrateResult = false;
        nextly.setVisibility(View.VISIBLE);
        fabResult.setVisibility(View.GONE);
        refreshly.setVisibility(View.INVISIBLE);
        resultPanelImg.setVisibility(View.INVISIBLE);
        fabMic.setVisibility(View.VISIBLE);
        fabresultPlay.setVisibility(View.GONE);
        clocktext.setVisibility(View.GONE);
        if(State.getSpeakingState() == State.speaking_state.normal) {
            result = null;
            sentenceIndex = -1;
            sentencesList.clear();
            speakingProgressBar.setProgress(0);
            cancelSpeaking();
        }
    }
    public void refreshWebView()
    {
        speakingWebView.loadUrl(MyApplication.mNetProc.mLoginUserInf.mstrSpeakingLessonURL);
        if(State.getSpeakingState() == State.speaking_state.normal) {
            sentencesList = MyApplication.mNetProc.mLoginUserInf.mSentenceList;
        }
    }

    private void refreshTitleView(int levelIndex, int lessonIndex, int partIndex)
    {
        String partAB = "A";
        if(partIndex == 1)
            partAB = "A";
        else
            partAB = "B";
        String tmpTitle = "Camp " + levelIndex + "-" + lessonIndex + "-" + partAB;

        titletv.setText(tmpTitle);
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MyApplication.getInstance().onActivityPaused(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mdlgProgress !=null)
            mdlgProgress.dismiss();
        speakingWebView.destroy();
        unregisterReceiver();
        MyApplication.getInstance().onActivityDestroyed(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.getInstance().onActivityStarted(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().onActivityStopped(this);
    }
    private void callGetLessonUrl(int levelIndex, int lessonIndex, int partIndex)
    {
        speakingLessonIndex = lessonIndex;
        speakingPartIndex = partIndex;
        for(int i = 0 ; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size() ; i++)
        {
            if(MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mnLevelId == 1){
                MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).setMnOralCurrentLessonId(speakingLessonIndex);
                MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).setMnOralCurrentPartId(speakingPartIndex);
            }
        }

        initVariables();
        setButtonState();
        String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                "&level_id=" + levelIndex + "&lesson_id=" + lessonIndex + "&part_id=" + partIndex + "&kind=" + Constants.STUDY_TYPE_SPEAKING;

        MyApplication.mNetProc.getLessonURL("getLessonURL", strRequestParameter,false);
        refreshTitleView(levelIndex,lessonIndex,partIndex);
    }

    void setButtonState()
    {
        switch (speakingPartIndex)
        {
            case 1:
                fabA.setBackground(ContextCompat.getDrawable(this, R.drawable.presseda)) ;
                fabB.setBackground(ContextCompat.getDrawable(this, R.drawable.b)) ;
                break;
            case 2:
                fabA.setBackground(ContextCompat.getDrawable(this, R.drawable.a)) ;
                fabB.setBackground(ContextCompat.getDrawable(this, R.drawable.pressedb)) ;
                break;
        }
    }
    void saveHistory()
    {
        mdlgProgress = MessageAlert.showProgressDialog(this, "结果分析....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadAudioFilesFromLocal();
                result = new SpeakingResult(sentencesList.get(sentenceIndex).sentence_id, GetReadSentenceCount(), GetHighScoreSentenceCount(), 1, speakingLessonIndex, speakingPartIndex, sentencesList);
                if(isGenrateResult) {
                    String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                            "&level_id=" + result.level_id + "&lesson_id=" + result.lesson_id + "&part_id=" + result.part_id + "&learn_id=0" + "&score=" + MakeJsonForSpeaking.convertJsonFromObject(result);

                    MyApplication.mNetProc.saveOralResult("saveOralResult", strRequestParameter);
                }
                if (mdlgProgress != null)
                    mdlgProgress.dismiss();

                Intent speakingResultInt = new Intent(SpeakingActivity.this, SpeakingResultActivity.class);
                speakingResultInt.putExtra("Speaking_Result", result);
                startActivity(speakingResultInt);
            }
        }).start();
    }

    void skipNextSection()
    {
        if(sentenceIndex < 0 || sentencesList.size() == 0) {
            return;
        }
        if(resultPanelImg.getVisibility() == View.VISIBLE)
            resultPanelImg.setVisibility(View.GONE);

        fabMic.setVisibility(View.VISIBLE);
        fabresultPlay.setVisibility(View.GONE);
        refreshly.setVisibility(View.INVISIBLE);
        sentenceIndex++;
        skipToPosition(sentenceIndex);
    }
    void skipToPosition(int pos)
    {
        if(mp != null) {
            mp.release();
            mp = null;
        }
        if(sentenceIndex >= sentencesList.size() - 1) {
            nextly.setVisibility(View.GONE);
            fabResult.setVisibility(View.VISIBLE);
            sentenceIndex = sentencesList.size() - 1;
        } else {
            nextly.setVisibility(View.VISIBLE);
            fabResult.setVisibility(View.GONE);
        }
        speakingProgressBar.setProgress(sentencesList.get(sentenceIndex).sound_duration);
        sentence = sentencesList.get(sentenceIndex).sentence;
        String strtmp = "javascript:SkiptoSpecifiedLocation("+ pos + ")";
        speakingWebView.loadUrl(strtmp);
    }
    public class JavaScriptInterface {
        public JavaScriptInterface() {

        }

        @JavascriptInterface
        public void finishHTMLloading() {
            mHandler.post(new Runnable() {
                public void run() {
                    String strtmp = "javascript:ShowText(" + fTextShow + ");";
                    speakingWebView.loadUrl(strtmp);
                    sentenceIndex = 0;
                    skipToPosition(sentenceIndex);
//                    if(State.getSpeakingState() == State.speaking_state.normal) {
//                        try {
//                            sentenceIndex = 0;
//                            sentence = sentencesList.get(sentenceIndex).sentence;
//                            speakingProgressBar.setProgress(sentencesList.get(sentenceIndex).sound_duration);
//                        } catch (Exception e) {
//
//                        }
//                    } else {
//                        skipToPosition(sentenceIndex);
//                    }
                }
            });
        }
        @JavascriptInterface
        public void finishLoadFirstSection()
        {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if(State.getSpeakingState() == State.speaking_state.normal) {
                        sentenceIndex = 0;
                        sentence = sentencesList.get(sentenceIndex).sentence;
                    }
                }
            });
        }
    }
    private void registerReceiver() {
        registerReceiver(receiver, new IntentFilter(Constants.SPEAKING_HISTORY_CASE));
    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception ignore) {}
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("transerfertest", "receifver");
            handleBroadcast(intent);
        }
    };

    private void handleBroadcast(@NonNull Intent from) {
        Log.d("transerfertest", "receifver1");
        final String action = from.getAction();
        if (Constants.SPEAKING_HISTORY_CASE.equalsIgnoreCase(action)) {
            isGenrateResult = false;
            sentenceIndex = from.getIntExtra("Skip_Position", 0);

            skipToPosition(sentenceIndex);
        }
    }

    private void uploadAudioFile(int n)
    {
        if(uploadHelper == null)
            uploadHelper = new UploadHelper();
        String uploaduel = uploadHelper.uploadAudio(sentencesList.get(n).sound_path,1,speakingLessonIndex,speakingPartIndex);
        sentencesList.get(n).setAudioPath(uploaduel);
//        Log.d("oss_audio_file",sentencesList.get(n).sound_path );
    }

    private void removeAllAudioFileInLocal()
    {
        File dir = new File(Constants.SPEAKING_AUDIO_PATH);

        String[] children = dir.list();
        if(children == null)
            return;
        for (int i = 0; i < children.length; i++)
        {
            new File(dir, children[i]).delete();
        }
    }

    protected void uploadAudioFilesFromLocal()
    {
        for (int i = 0 ; i < sentencesList.size() ; i++)
        {
            File audioFile = new File(sentencesList.get(i).sound_path);
            if(audioFile.exists())
                uploadAudioFile(i);
        }
        removeAllAudioFileInLocal();

    }
    private void showFinalResult()
    {
        saveHistory();
    }

    private void performFabMic()
    {
        startSpeaking();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        currentProgress += 0.1f;
                        if(currentProgress >= 30) {
                            readDuration = (int)currentProgress;
                            currentProgress = 0.0f;
                            //speakingProgressBar.setProgress(0);

                            timer.cancel();
                            timer = null;
                            nextly.setClickable(true);
                            stopSpeaking();
                        }else {

                            speakingProgressBar.setProgress(currentProgress);

                        }
                    }
                });
            }}, 0, 100);
    }
}

