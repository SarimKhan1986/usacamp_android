package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.usacamp.R;

public class MessageDetailAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        TextView titletxt = findViewById(R.id.notice_title);
        TextView contenttxt = findViewById(R.id.notice_content);

        titletxt.setText(getIntent().getStringExtra("title"));
        contenttxt.setText(getIntent().getStringExtra("content"));
        String messageID = getIntent().getStringExtra("messageid");
        String messageType = getIntent().getStringExtra("type");

        MyApplication.mNetProc.saveReadNotify( "saveReadNotify", "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                "&notify_id=" + messageID + "&type=" + messageType);
    }

    public void onBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
}
