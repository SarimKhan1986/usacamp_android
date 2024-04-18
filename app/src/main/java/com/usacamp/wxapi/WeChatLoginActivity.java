package com.usacamp.wxapi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;

import androidx.annotation.NonNull;

/**
 * @author JoongWon Baik
 */
public class WeChatLoginActivity extends Activity{

    IWXAPI iwxapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver();

        iwxapi = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID, true);
        //boolean registered = iwxapi.registerApp(MyApplication.getInstance().WECHAT_APPID);
        //iwxapi.handleIntent(getIntent(), this);

        SendAuth.Req req = new SendAuth.Req();
        req.scope = Constants.scope;
        req.state = Constants.state;
        iwxapi.sendReq(req);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    private void registerReceiver() {
        registerReceiver(receiver, new IntentFilter(Constants.WE_CHAT_AUTH_RESULT));
    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception ignore) {}
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
           handleBroadcast(intent);
        }
    };

    private void handleBroadcast(@NonNull Intent from) {
        Log.d("wechataccesstoken", "code2");
        final String action = from.getAction();
        if (Constants.WE_CHAT_AUTH_RESULT.equalsIgnoreCase(action)) {
            Intent result = new Intent();
            result.putExtra(Constants.WE_CHAT_AUTH_CODE, from.getStringExtra(Constants.WE_CHAT_AUTH_CODE));
            result.putExtra(Constants.WE_CHAT_ERROR_CODE, from.getStringExtra(Constants.WE_CHAT_ERROR_CODE));
            setResult(RESULT_OK, result);
            finish();
        }
    }
}
