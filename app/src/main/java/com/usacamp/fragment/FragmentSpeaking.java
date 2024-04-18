package com.usacamp.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.usacamp.R;
import com.usacamp.activities.BannerDetail;
import com.usacamp.activities.LearningRecordActivity;
import com.usacamp.activities.MyApplication;
import com.usacamp.activities.SpeakingActivity;
import com.usacamp.activities.SpeakingIntroActivity;
import com.usacamp.constants.State;
import com.usacamp.model.ProgressItem;
import com.usacamp.utils.AdapterImageSlider;

public class FragmentSpeaking extends FragmentStudy {

    private boolean bSpeakingAvailable = false;
    View root_view;
    private ViewPager2 speaking_viewPager;
    private Group textGroup;
    private AdapterImageSlider adapterSlider;

    public FragmentSpeaking() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_speaking, container, false);
        textGroup = root_view.findViewById(R.id.group2);
        if(State.getCurrentPattern() == State.pattern.student.getValue())
            textGroup.setVisibility(View.GONE);
        else
            textGroup.setVisibility(View.VISIBLE);

        ((ConstraintLayout) root_view.findViewById(R.id.ct_speaking_study)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.setSpeakingState(State.speaking_state.normal);
                if (bSpeakingAvailable) {
                    Intent speakingAct = new Intent(root_view.getContext(), SpeakingActivity.class);
                    root_view.getContext().startActivity(speakingAct);
                }
            }
        });
        ((ConstraintLayout) root_view.findViewById(R.id.constraintLayout2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.setSpeakingState(State.speaking_state.normal);
                if (bSpeakingAvailable) {
                    Intent speakingAct = new Intent(root_view.getContext(), SpeakingActivity.class);
                    root_view.getContext().startActivity(speakingAct);
                }
            }
        });

        ((ConstraintLayout) root_view.findViewById(R.id.speaking_history)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent histroyAct = new Intent(root_view.getContext(), LearningRecordActivity.class);
                histroyAct.putExtra("HistoryType", 1);
                root_view.getContext().startActivity(histroyAct);
            }
        });
        ((LinearLayout) root_view.findViewById(R.id.ct_speaking_about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speakingIntroAct = new Intent(root_view.getContext(), SpeakingIntroActivity.class);
                root_view.getContext().startActivity(speakingIntroAct);
            }
        });

        speaking_viewPager = (ViewPager2) root_view.findViewById(R.id.speaking_viewPager);
        adapterSlider = new AdapterImageSlider(MyApplication.mNetProc.mLoginUserInf.mlist_oralPracticeBanner, R.layout.item_slider_image);
        speaking_viewPager.setAdapter(adapterSlider);
        return root_view;
    }

    public void setTitleInformation() {
        TextView txtcontentlearn = (TextView) root_view.findViewById(R.id.speaking_start_content);

        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size(); i++) {
            ProgressItem progressItem = MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i);
            if (progressItem.mnLevelId == 1) {
                bSpeakingAvailable = true;
                String partAB = "A";
                if (progressItem.mnOralCurrentPartId == 1)
                    partAB = "A";
                else
                    partAB = "B";

                String tempStr = "Camp1-" + progressItem.mnOralCurrentLessonId + "-" + partAB;
                txtcontentlearn.setText(tempStr);
                break;
            }
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {}
    @Override
    public void onResume() {
        super.onResume();
        setTitleInformation();
    }
}