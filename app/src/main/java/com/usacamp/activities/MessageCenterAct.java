package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MessageAdapter;
import com.usacamp.model.MessageItem;
//

public class MessageCenterAct extends BaseActivity {
    public ListView mlistBraodMessage;
    public MessageAdapter mAdapterBraod = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message_center);
        mlistBraodMessage = findViewById(R.id.list_broad_message);
        mAdapterBraod = new MessageAdapter(
                this, R.layout.layout_message_item, MyApplication.mNetProc.mLoginUserInf.mlistBroadMessage);
        mlistBraodMessage.setAdapter(mAdapterBraod);
        mlistBraodMessage.setItemsCanFocus(true);
        mlistBraodMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageItem dtlmessage = MyApplication.mNetProc.mLoginUserInf.mlistBroadMessage.get(i);

                Intent MessageDtlAct = new Intent(MessageCenterAct.this, MessageDetailAct.class);
                MessageDtlAct.putExtra("title", dtlmessage.mstrTitle);
                MessageDtlAct.putExtra("content", dtlmessage.mstrContent);
                MessageDtlAct.putExtra("type", "2");
                MessageDtlAct.putExtra("messageid", String.valueOf(dtlmessage.mIdx));
                startActivity(MessageDtlAct);

                if(dtlmessage.mIsRead == 0)
                {
                    //Zhuge
                   // ////ZhugeSDK.getInstance().track(MessageCenterAct.this, Constants.Zhuge_Event_Click_Main_MessageCenter_UnreadMsg);
                } else {

                }
            }
        });

        //Zhuge
        //////ZhugeSDK.getInstance().track(this, Constants.Zhuge_Event_Enter_Main_MessageCenter);
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
