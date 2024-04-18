package com.usacamp.constants;

import android.content.Intent;
import com.usacamp.R;
import com.usacamp.activities.MainActivity;
import com.usacamp.activities.MyApplication;
import com.usacamp.activities.StudentPatternActivity;

public class State {
    private static app_state current_state = app_state.real;
    private static speaking_state current_speaking_state = speaking_state.normal;
    private static study_state current_study_state = study_state.normal;
    private static loginType current_login_state = loginType.prelogin;
    public static int current_pattern = pattern.parent.getValue();
    public enum pattern {
        parent(0),
        student(1);
        private int current;
        pattern(int i) {
            current = i;
        }
        public int getValue()
        {
            return current;
        }
    }
    public enum app_state  {
        real,
        dev,
        offline
    }

    public enum study_state {
        normal,
        speaking
    }
    public enum speaking_state {
        normal,
        history
    }
    public enum loginType {
        prelogin,
        logout
    }
    public static void setCurrent_login_state(loginType t)
    {
        current_login_state = t;
    }

    public static loginType getCurrent_login_state()
    {
        return current_login_state;
    }
    public static void setState(app_state st)
    {
        current_state = st;
        switch (current_state){
            case dev:
                MyApplication.getInstance().SERVER_API = Constants.SERVER_API_1_3_DEV;
                MyApplication.getInstance().SERVER_URL = Constants.DEV_URL;
                MyApplication.getInstance().WECHAT_APPID = Constants.REAL_AppID;
                break;
            case real:
                MyApplication.getInstance().SERVER_API = Constants.SERVER_API_1_3;
                MyApplication.getInstance().SERVER_URL = Constants.MAIN_URL;
                MyApplication.getInstance().WECHAT_APPID = Constants.REAL_AppID;
                break;
        }

    }
    public static void setStudyState(study_state st){
        current_study_state = st;
    }

    public static void setSpeakingState(speaking_state st)
    {
        current_speaking_state = st;
    }

    public static app_state getState()
    {
        return current_state;
    }
    public static study_state getStudyState() {return current_study_state;}
    public static speaking_state getSpeakingState(){return current_speaking_state;}

    public static int getCurrentPattern(){return current_pattern;}
    public static void setPattern(int p) {

        if (p != current_pattern) {
            current_pattern = p;

            MyApplication.mNetProc.setLoginMode("setLoginMode", "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id + "&login_mode=" + current_pattern);

        }
        switch (current_pattern) {
            case 0:
                Intent mainInt = new Intent(MyApplication.getInstance().getCurrentActivity(), MainActivity.class);
                mainInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MyApplication.getInstance().getCurrentActivity().startActivity(mainInt);
                MyApplication.getInstance().getCurrentActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                try {
                    StudentPatternActivity.instance.finish();
                } catch (NullPointerException e){}
                break;
            case 1:
                Intent studentInt = new Intent(MyApplication.getInstance().getCurrentActivity(), StudentPatternActivity.class);
                studentInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MyApplication.getInstance().getCurrentActivity().startActivity(studentInt);
                MyApplication.getInstance().getCurrentActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                try {
                    MainActivity.mainActivityInstance.finish();
                } catch (NullPointerException e){}
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + current_pattern);
        }
    }
}
