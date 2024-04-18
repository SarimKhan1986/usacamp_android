package com.usacamp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.usacamp.R;
import com.usacamp.activities.MyApplication;
import com.usacamp.activities.StudentPatternActivity;
import com.usacamp.activities.SwitchPatternActivity;
import com.usacamp.constants.State;

public class FragmentStudy extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View root_view;
    // TODO: Rename and change types of parameters
    private FragmentSpeaking mfraSpeaking;
    private FragmentLearn mfraLearn;
    private TextView learnBtn, speakingBtn;
    private ImageView btnswitch;
    public FragmentStudy() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void refreshAllInformation()
    {
        mfraLearn.refreshAllInformation();
    }

    public void refreshTeachVideoTitle()
    {
        mfraLearn.setTeacherTitleVideo();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_study, container, false);
        learnBtn = (TextView) root_view.findViewById(R.id.btn_learn_tab);
        speakingBtn = (TextView) root_view.findViewById(R.id.btn_speaking_tab);
        btnswitch = (ImageView) root_view.findViewById(R.id.imageView);
        mfraSpeaking = new FragmentSpeaking();
        mfraLearn = new FragmentLearn();
        initialize();
        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setTitleView(0);
               getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mfraLearn).commit();
            }
        });

        speakingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitleView(1);
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mfraSpeaking).commit();

            }
        });

        btnswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchInt = new Intent(root_view.getContext(), SwitchPatternActivity.class);
                switchInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                root_view.getContext().startActivity(switchInt);
            }
        });
        return root_view;
    }
    public void initialize()
    {
        setTitleView(0);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mfraLearn).commit();
    }
    void setTitleView(int selectedIndex)
    {
        if(selectedIndex == 0)
        {
            learnBtn.setTextColor(Color.BLACK);
            speakingBtn.setTextColor(Color.GRAY);
            if(MyApplication.getInstance().isTablet) {
                learnBtn.setTextSize(34);
                speakingBtn.setTextSize(30);
            } else {
                learnBtn.setTextSize(24);
                speakingBtn.setTextSize(20);
            }
            learnBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.tabindicatoricon);
            speakingBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        } else
        {
            learnBtn.setTextColor(Color.GRAY);
            speakingBtn.setTextColor(Color.BLACK);
            if(MyApplication.getInstance().isTablet) {
                learnBtn.setTextSize(30 );
                speakingBtn.setTextSize(34);
            } else {
                learnBtn.setTextSize(20);
                speakingBtn.setTextSize(24);
            }
            learnBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            speakingBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.tabindicatoricon);
        }
    }

}