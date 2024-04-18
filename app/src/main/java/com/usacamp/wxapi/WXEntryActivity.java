package com.usacamp.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.usacamp.activities.LoginActivity;
import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	private static final String TAG = "Wechat Response";

    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID);
		boolean handleIntent = api.handleIntent(getIntent(), this);

		if(handleIntent == false){
			finish();
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
//		finish();
	}

	@SuppressLint("LongLogTag")
	@Override
	public void onResp(BaseResp resp) {
		SendAuth.Resp response = (SendAuth.Resp) resp;
		Log.d("WeChatShare", response.toString());
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK :
				if (resp instanceof SendAuth.Resp) {
					sendAuthResult(((SendAuth.Resp)resp));
				}

				break;

			case BaseResp.ErrCode.ERR_USER_CANCEL :
				Toast.makeText(this, "用户取消了请求。", Toast.LENGTH_LONG).show();
				finish();
//				Intent goMain = new Intent(getApplicationContext(), LoginActivity.class);
//				getApplicationContext().startActivity(goMain);
				break;

			case BaseResp.ErrCode.ERR_AUTH_DENIED :
				Toast.makeText(this, "用户拒绝了请求。", Toast.LENGTH_LONG).show();
				finish();
//				Intent goMain1 = new Intent(getApplicationContext(), LoginActivity.class);
//				getApplicationContext().startActivity(goMain1);
				break;


		}
		if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
			WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
			String extraData =launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
		}

	}

	private void sendAuthResult(SendAuth.Resp resp) {
		Intent intent = new Intent();
		intent.setAction(Constants.WE_CHAT_AUTH_RESULT);
		intent.putExtra(Constants.WE_CHAT_AUTH_CODE, resp.code);
		intent.putExtra(Constants.WE_CHAT_ERROR_CODE, resp.errCode);
		sendBroadcast(intent);

		finish();
	}
}