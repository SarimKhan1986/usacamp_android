package com.usacamp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.usacamp.R;
import com.usacamp.activities.LearnActivity;
import com.usacamp.activities.MainActivity;
import com.usacamp.activities.MessageCenterAct;
import com.usacamp.activities.MyApplication;
import com.usacamp.activities.PurchaseCampActivity;
import com.usacamp.activities.ReportActivity;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.model.MediaItem;
import com.usacamp.utils.AdapterImageSlider;
import com.usacamp.utils.Age;
import com.usacamp.utils.HomeAdapterVideo;
import com.usacamp.utils.ItemOffsetDecoration;
import com.usacamp.utils.MessageAlert;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.LayoutParams;
public class FragmentHome extends Fragment {
    private AdapterImageSlider adapterSlider;
    private HomeAdapterVideo adapterVideoSlider;
    private LinearLayout layout_dots;
    ImageView[] dots ;
    private Runnable runnable = null;
    private final Handler handler = new Handler();
    private RecyclerView cardSlider3;
    private ViewPager2 viewSlider;
    private TextView devmodeTxt;
    Timer autoTimer = null;
    private View root_view;

    public FragmentHome() {
        // Required empty public constructor
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {}
    protected void onLoginButton()
    {
        Log.d("loginframe", "onLoginButton");
        if (!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
            Log.d("loginframe", "logoutstatus");
            //Zhuge Event
            ////ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Login_Frame);

            JSONObject eventObject = new JSONObject();
            try {
                eventObject.put(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Enter_LoginPage_Home);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ////ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Enter_Login_Page, eventObject);

            MainActivity.mainActivityInstance.loginAuth(false);

        }else {
            ((MainActivity)getActivity()).goFragmentProfile();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            refreshCommonInf();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshNotifyInf();
    }
    @Override
    public void onStart()
    {
        super.onStart();

    }

    public void setMediaInforamtion() {

        if(MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
            ((ConstraintLayout)root_view.findViewById(R.id.loginct)).setVisibility(View.VISIBLE);
            ((Group)root_view.findViewById(R.id.buttongroup)).setVisibility(View.VISIBLE);
            ((ConstraintLayout)root_view.findViewById(R.id.logoutct)).setVisibility(View.GONE);
        } else {
            ((ConstraintLayout)root_view.findViewById(R.id.loginct)).setVisibility(View.GONE);
            ((Group)root_view.findViewById(R.id.buttongroup)).setVisibility(View.GONE);
            ((ConstraintLayout)root_view.findViewById(R.id.logoutct)).setVisibility(View.VISIBLE);
        }

        cardSlider3 = (RecyclerView) root_view.findViewById(R.id.kk_pager3);

        ArrayList<MediaItem> mainVideoItems = new ArrayList<>();
        ArrayList<MediaItem> smallVideoItems =  new ArrayList<>();
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistMedia.size(); i++)
        {
            MediaItem item = MyApplication.mNetProc.mLoginUserInf.mlistMedia.get(i);
            if (item.mIsMain == 1)
                mainVideoItems.add(item);
            else
                smallVideoItems.add(item);
        }
        //MainVideo
        adapterVideoSlider = new HomeAdapterVideo(mainVideoItems, 0);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(2);
        cardSlider3.addItemDecoration(itemDecoration);
        cardSlider3.setHasFixedSize(true);
        cardSlider3.setLayoutManager(new GridLayoutManager(root_view.getContext(), mainVideoItems.size()));
        cardSlider3.setAdapter(adapterVideoSlider);

        initComponent();
    }

    private void onLevelIntroduce()
    {
        String strrequestparameter ="param=lesson_introduce";
        MyApplication.mNetProc.getConfig("getConfig" , strrequestparameter );
//        Intent introAct =  new Intent(getContext(), CampIntroduce.class);
//        getContext().startActivity(introAct);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.fragment_home, container,false);
        getActivity().getIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 3000ms
                MainActivity.mainActivityInstance.showStudyButton();
            }
        }, 3000);
        devmodeTxt = (TextView)root_view.findViewById(R.id.txt_dev);
        if(State.getState() == State.app_state.dev)
            devmodeTxt.setVisibility(View.VISIBLE);
        else
            devmodeTxt.setVisibility(View.GONE);
        root_view.findViewById(R.id.simgshare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                        "&kind=0";

                MyApplication.mNetProc.getShareInfo("getShareInfo", strRequestParameter);
                //Zhuge
                //定义与事件相关的属性信息
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_Welfare_Classification_Share);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ////ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Welfare,  eventObject);
            }
        });
        root_view.findViewById(R.id.frameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButton();
            }
        });
        root_view.findViewById(R.id.btn_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent msgCentAct = new Intent(root_view.getContext() , MessageCenterAct.class);
                startActivity(msgCentAct);

                //Zhuge
                //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Main_MessageCenter);
            }
        });
        root_view.findViewById(R.id.btn_newreport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                Intent reportActivity = new Intent(root_view.getContext(), ReportActivity.class);
                reportActivity.putExtra("type", 1);
                startActivity(reportActivity);
            }
        });
        root_view.findViewById(R.id.mainintrobutton).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onLevelIntroduce();
                //Zhuge
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Name, Constants.Zhuge_Value_FunctionBar_Name_Intro);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Main_FunctionBar,  eventObject);
            }
        });

        root_view.findViewById(R.id.simgtest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent learnActvity = new Intent(root_view.getContext(), LearnActivity.class);
                learnActvity.putExtra("LearnMode", 2);
                startActivity(learnActvity);

                //Zhuge
                //ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Test_TestButton);
            }
        });
        root_view.findViewById(R.id.loginct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strrequestparameter ="param=general_problem";
                MyApplication.mNetProc.getConfig("getConfig" , strrequestparameter );
            }
        });
        root_view.findViewById(R.id.logoutct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strrequestparameter ="param=general_problem";
                MyApplication.mNetProc.getConfig("getConfig" , strrequestparameter );
            }
        });
        root_view.findViewById(R.id.mainpurchasebutton).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                Intent i = new Intent(root_view.getContext(), DownloadContentAct.class);
//                startActivity(i);
                Intent i = new Intent(root_view.getContext(), PurchaseCampActivity.class);
                i.putExtra(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Entry_Purchase_Home);
                startActivity(i);
                //Zhuge
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Name, Constants.Zhuge_Value_FunctionBar_Name_Purchase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Main_FunctionBar,  eventObject);

                //Zhuge

                //ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Main_BuyCamp);
            }
        });

        //Zhuge
        //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Enter_Main_Tab);
        try {
            refreshCommonInf();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root_view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void initComponent() {

        layout_dots = (LinearLayout) root_view.findViewById(R.id.layout_dots);
        viewSlider = (ViewPager2) root_view.findViewById(R.id.pager);
        adapterSlider = new AdapterImageSlider(MyApplication.mNetProc.mLoginUserInf.mlistbanner, R.layout.item_slider_image);
        viewSlider.setAdapter(adapterSlider);
        LayoutParams params = viewSlider.getLayoutParams();
        params.height = (int)(MyApplication.getInstance().getPhoneWidth() / 351.0f * 100.0f);
        viewSlider.setLayoutParams(params);
        dots = new ImageView[MyApplication.mNetProc.mLoginUserInf.mlistbanner.size()];
        createBottomDots(layout_dots, MyApplication.mNetProc.mLoginUserInf.mlistbanner.size());
        viewSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int pos) {
                addBottomDots(pos);
            }

        });
        startAutoSlider(MyApplication.mNetProc.mLoginUserInf.mlistbanner.size());
    }

    void createBottomDots(LinearLayout layout_dots, int size)
    {
        layout_dots.removeAllViews();
        for (int i = 0; i < size; i++) {
            dots[i] = new ImageView( getActivity());
            int width_height = 10;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(width_height * 2, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.dotslider);
            layout_dots.addView(dots[i]);
        }

        if(dots.length != 0)
            dots[0].setImageResource(R.drawable.shapecircle);

    }
    private void addBottomDots( int current) {

        for(int i = 0 ; i < dots.length ; i++) {
            if(i == current)
                dots[i].setImageResource(R.drawable.shapecircle);
            else
                dots[i].setImageResource(R.drawable.dotslider);
        }
    }

    public int homeBannerCount = 0;
    private void startAutoSlider(final int count) {

        if(count <= 1) {

            if(autoTimer !=null) {
                autoTimer.cancel();
                autoTimer.purge();
            }
            if(runnable != null)
                handler.removeCallbacks(runnable);
            return;
        }

        if(homeBannerCount != count) {
            homeBannerCount = count;
            if(autoTimer !=null) {
                autoTimer.cancel();
                autoTimer.purge();
                autoTimer = null;
            }

            if(runnable != null)
                handler.removeCallbacks(runnable);

            runnable = new Runnable() {
                @Override
                public void run() {
                    int pos = viewSlider.getCurrentItem();
                    pos = pos + 1;
                    if (pos >= count) pos = 0;
                    viewSlider.setCurrentItem(pos);
                    addBottomDots(pos);
                }
            };
            autoTimer = new Timer();
            autoTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            }, 2000, 3000);
        }
    }

    public void refreshUserPic() {
        if (MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin && !MyApplication.mNetProc.mLoginUserInf.mstrPic.equals("")) {
            Uri uri= Uri.parse(MyApplication.mNetProc.mLoginUserInf.mstrPic);
            RequestCreator reqCreator = Picasso.with(root_view.getContext()).load(uri);
            reqCreator.into( (CircularImageView) (root_view.findViewById(R.id.img_charac_boy)));
        } else
            ((CircularImageView)root_view.findViewById(R.id.img_charac_boy)).setImageResource(R.drawable.lucy_girl);
    }

    public void refreshNotifyInf() {

        if (MyApplication.mNetProc.mLoginUserInf.mnUnreadTotalCount > 0) {
            root_view.findViewById(R.id.text_message_cnt).setVisibility(View.VISIBLE);
        } else {
            root_view.findViewById(R.id.text_message_cnt).setVisibility(View.INVISIBLE);
        }
    }

    public void refreshCommonInf() throws IOException {
        //show user inf
        refreshName();
        refreshUserPic();
        //show message count
        refreshNotifyInf();

        setMediaInforamtion();
    }

    public void refreshName(){
        if (MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin){
            ((TextView)root_view.findViewById(R.id.txt_welcome)).setText(MyApplication.mNetProc.mLoginUserInf.mstrName);
            ((TextView)root_view.findViewById(R.id.txt_not_registered)).setText(Age.calculateAge());
            ((TextView)root_view.findViewById(R.id.mainlogintoptxt)).setText("欢迎WELCOME");

        } else {
            ((TextView)root_view.findViewById(R.id.txt_not_registered)).setText("登录/注册");
            ((TextView)root_view.findViewById(R.id.txt_welcome)).setText("欢迎 WELCOME");
            ((TextView)root_view.findViewById(R.id.mainlogintoptxt)).setText("登录即可免费试用");
        }

    }

}

