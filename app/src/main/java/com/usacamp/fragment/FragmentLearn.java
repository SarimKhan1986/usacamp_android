package com.usacamp.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.usacamp.R;
import com.usacamp.activities.About_levels;
import com.usacamp.activities.ActBuyLevelInHome;
import com.usacamp.activities.DownloadContentAct;
import com.usacamp.activities.LearnActivity;
import com.usacamp.activities.MyApplication;
import com.usacamp.activities.TeacherVideoActivity;
import com.usacamp.activities.VideoActivity;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.utils.AdapterGridSectioned;
import com.usacamp.model.LevelItem;
import com.usacamp.model.ProgressItem;
import com.usacamp.utils.ItemOffsetDecoration;
import com.usacamp.utils.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class FragmentLearn extends FragmentStudy{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    private AdapterGridSectioned mAdapter;
    private View root_view;
    RoundedImageView teacherTitleVideo;
    Group textGroup;
    //Lesson Grid
    List<LevelItem> campItems = new ArrayList<LevelItem>();
    int mnStudyCampIdx;

    public FragmentLearn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void setTeacherTitleVideo (){
        if(MyApplication.mNetProc.mLoginUserInf.mlistTeacherMedia.size() != 0)
            Picasso.with(root_view.getContext()).load(MyApplication.mNetProc.mLoginUserInf.mlistTeacherMedia.get(0).mstrImage).placeholder(R.drawable.teacherphoto).into(teacherTitleVideo);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_learn, null);

        textGroup = root_view.findViewById(R.id.group3);
        if(State.getCurrentPattern() == State.pattern.student.getValue())
            textGroup.setVisibility(View.GONE);
        else
            textGroup.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);recyclerView.setHasFixedSize(true);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        root_view.findViewById(R.id.button27).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teacherAct = new Intent(MyApplication.getInstance().getCurrentActivity(), TeacherVideoActivity.class);
                MyApplication.getInstance().getCurrentActivity().startActivity(teacherAct);
            }
        });
        teacherTitleVideo = (RoundedImageView) root_view.findViewById(R.id.imageView12);

        teacherTitleVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.mNetProc.mLoginUserInf.mlistTeacherMedia.size() != 0) {
                    Intent videoIntent = new Intent(root_view.getContext(), VideoActivity.class);
                    videoIntent.putExtra("video_path", MyApplication.mNetProc.mLoginUserInf.mlistTeacherMedia.get(0).mstrVideoFile);
                    root_view.getContext().startActivity(videoIntent);
                }
            }
        });
        root_view.findViewById(R.id.btnstartlearn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyApplication.mNetProc.mLoginUserInf.mnIsActive == 0) {//inactive
                    visibleActiveQRcode();
                    return;
                }
                if(MyApplication.mNetProc.mLoginUserInf.mlistProgress.size() == 0)
                {
                    Intent buyLevelAct = new Intent(root_view.getContext(), ActBuyLevelInHome.class);
                    root_view.getContext().startActivity(buyLevelAct);
                }else {
                    if(MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel == 0)
                        MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel = MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(0).mnLevelId;

                    if(MyApplication.mNetProc.mLoginUserInf.muserType == 2 || MyApplication.mNetProc.mLoginUserInf.muserType == 5 ) {
                        if(MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel == 0)
                            MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel = 1;
                    }
                    for(int i = 0 ; i <MyApplication.mNetProc.mLoginUserInf.mlistProgress.size() ; i++)
                    {
                        if (MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mnLevelId == MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel) {
                            mnStudyCampIdx = MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel;
                            goLearnActivityWithlogin();

                            //ZHuge
                            JSONObject eventObject = new JSONObject();
                            try {
                                eventObject.put(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Entry_LearningPage_Learn_Continue);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Learn_LearningPage,  eventObject);
                            return;
                        }
                    }
                    Intent buyLevelAct = new Intent(root_view.getContext(), ActBuyLevelInHome.class);
                    root_view.getContext().startActivity(buyLevelAct);
                }
            }
        });
        root_view.findViewById(R.id.about_levels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutInt = new Intent(root_view.getContext(), About_levels.class);
                startActivity(aboutInt);
            }
        });
        //Zhuge
        //ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Enter_Learn_Tab);
        return root_view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }
    @Override
    public void onResume() {
        super.onResume();
        MyApplication.mNetProc.getForeignTeacherVideo("getForeignTeacherVideo", "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id);
        setTitleInformation();
        setStudyLevelInforamtion();
    }

    public void setTitleInformation()
    {
        TextView txttilelearn = (TextView)root_view.findViewById(R.id.learntxt) ;
        TextView txtcontentlearn = (TextView)root_view.findViewById(R.id.text_study_camp) ;

        if(MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel == 0)
        {
            txttilelearn.setText("请开始学习: ");
            txtcontentlearn.setText("级别1~级别12");
        } else {
            txttilelearn.setText("上一次学习到: ");
            for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size(); i++) {
                ProgressItem progressItem = MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i);
                if (progressItem.mnLevelId == MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel) {
                    String tempStr = "Camp" + MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel + "-" + progressItem.mnCurrentLessonId + "-" + progressItem.mnCurrentPartId;
                    txtcontentlearn.setText(tempStr);
                    break;
                }
            }
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setStudyLevelInforamtion(){

        if(MyApplication.mNetProc.mLoginUserInf.mlistLevels.size() == 0)
            return;

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        campItems.clear();
        for(int i = 0 ; i < MyApplication.mNetProc.mLoginUserInf.mlistLevels.size() ; i++)
            campItems.add(MyApplication.mNetProc.mLoginUserInf.mlistLevels.get(i));

//        if(campItems.size() < 15) {
//            campItems.add(0, new LevelItem(-1, "", -1, "初级课程", true));
//            campItems.add(5, new LevelItem(-1, "", -1, "中级课程", true));
//            campItems.add(10, new LevelItem(-1, "", -1, "高级课程", true));
//        }
        mAdapter = new AdapterGridSectioned(root_view.getContext(), campItems);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridSectioned.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LevelItem obj, int position) {
                if(MyApplication.mNetProc.mLoginUserInf.mnIsActive == 0) {//inactive
                    visibleActiveQRcode();
                    return;
                }
                if (obj.mnStatus != Constants.LEVEL_UNAVAILABLE){
                    mnStudyCampIdx = obj.mnId;
                    //Zhuge
                    JSONObject eventObject = new JSONObject();
                    try {
                        eventObject.put(Constants.Zhuge_Property_Camp_Name, "Camp" + obj.mnId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Learn_LearningPage_Camp_Button,  eventObject);
                    if(obj.mnStatus == Constants.LEVEL_ALREADY_PURCHASE) {
                        AlertDialog.Builder keyBuilder = new AlertDialog.Builder(root_view.getContext());
                        keyBuilder.setCancelable(false);
                        final View learnWarningView = getLayoutInflater().inflate(R.layout.layout_learn_waring_test, null);
                        keyBuilder.setView(learnWarningView);
                        AlertDialog dialog = keyBuilder.create();
                        learnWarningView.findViewById(R.id.learnwarningtestconfirm).setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               dialog.dismiss();
                               obj.mnStatus = Constants.LEVEL_STUDYING;

                               //ZHuge
                               JSONObject eventObject = new JSONObject();
                               try {
                                   eventObject.put(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Entry_LearningPage_Learn_Camp);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                               //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Learn_LearningPage,  eventObject);
                               goLearnActivityWithlogin();
                           }
                        });

                        learnWarningView.findViewById(R.id.learnwarningtestcancel).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        goLearnActivityWithlogin();
                        //ZHuge
                        JSONObject eventObject1 = new JSONObject();
                        try {
                            eventObject1.put(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Entry_LearningPage_Learn_Camp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Learn_LearningPage,  eventObject1);

                    }
                } else {
                    Intent buyLevelAct = new Intent(root_view.getContext(), ActBuyLevelInHome.class);
                    root_view.getContext().startActivity(buyLevelAct);
                }
            }
        });
    }

    public void refreshAllInformation(){
        setTitleInformation();
        setStudyLevelInforamtion();
    }

    private  void goLearnActivityWithlogin() {

        if(MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin){

            MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel = mnStudyCampIdx;
            if (MyApplication.getInstance().isOfflineMode && !MyApplication.getInstance().getLevelListInOfflineMode().contains(String.valueOf(mnStudyCampIdx))) {
                Intent i = new Intent(root_view.getContext(), DownloadContentAct.class);
                startActivity(i);
            } else {
                Intent LearnActvity = new Intent(root_view.getContext(), LearnActivity.class);
                LearnActvity.putExtra("LearnMode", 0);
                LearnActvity.putExtra("LevelId", mnStudyCampIdx);

                startActivity(LearnActvity);
            }
        }
    }

    private void visibleActiveQRcode()
    {
        AlertDialog.Builder keyBuilder = new AlertDialog.Builder(root_view.getContext());
        keyBuilder.setCancelable(false);
        final View learnActiveView = getLayoutInflater().inflate(R.layout.layout_learn_active, null);
        keyBuilder.setView(learnActiveView);
        AlertDialog dialog = keyBuilder.create();

        learnActiveView.findViewById(R.id.closeActive).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
