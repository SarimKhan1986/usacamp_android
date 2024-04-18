package com.usacamp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.model.ActCenterItem;
//

import java.util.ArrayList;

public class ActCenterActivity extends BaseActivity {
    private ListView mlistActCenter;
    private ArrayList<ActCenterItem> marrActCenter;
    private Activity mactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_center);

        mactivity = this;

        //Zhuge

       // ////ZhugeSDK.getInstance().track(this, Constants.Zhuge_Event_Enter_Profile_Activity_center);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onBack(View view) {
        finish();
    }
    public void onActParent (View view) {
        Intent actParent = new Intent(ActCenterActivity.this, ParentClassActivity.class);
        startActivity(actParent);
    }
    public void refreshAllInformation()
    {

    }
}
