package com.usacamp.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MessageAlert;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    @SuppressLint("LongLogTag")
    @Override
    public void onResp(BaseResp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String purchasedCampStr = "";
                for(int i= 0 ; i < MyApplication.mNetProc.mLoginUserInf.mSelCampList.size() ; i++)
                {
                    if(i != MyApplication.mNetProc.mLoginUserInf.mSelCampList.size() - 1)
                        purchasedCampStr += (MyApplication.mNetProc.mLoginUserInf.mSelCampList.get(i)+1)  + "_";
                    else
                        purchasedCampStr += (MyApplication.mNetProc.mLoginUserInf.mSelCampList.get(i)+1);
                }
                String strParmanter = "";

                strParmanter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id + "&pay_price=" + MyApplication.mNetProc.mLoginUserInf.mPayedPrice + "&type=3" + "&used_point=" + MyApplication.mNetProc.mLoginUserInf.mnUsedPoint + "&cnt=" + MyApplication.mNetProc.mLoginUserInf.mSelCampCount + "&purchased_level_list=" + purchasedCampStr;
                //Log.d("savePurchaseHistory", strParmanter);
                MyApplication.mNetProc.savePayment( "savePayment", strParmanter);

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                MessageAlert.showMessage(this ,"取消付款");
                finish();
//                payMessageTxt.setText("用户取消付款。");

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                MessageAlert.showMessage(this ,"取消付款");
                finish();
//                payMessageTxt.setText("付款失败。 请再试一次。");
                break;
            default:
                MessageAlert.showMessage(this ,"取消付款");
                finish();
//                payMessageTxt.setText("付款失败。 请再试一次。");
                break;
        }
    }

}
