package com.usacamp.exceptions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;

public class WeChatAppRegister extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WXAPIFactory.createWXAPI(context, MyApplication.getInstance().WECHAT_APPID).registerApp(MyApplication.getInstance().WECHAT_APPID);
    }
}
