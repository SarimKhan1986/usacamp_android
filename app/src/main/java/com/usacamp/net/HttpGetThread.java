package com.usacamp.net;

import java.io.IOException;
import java.io.InputStream;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpGetThread implements Runnable {

	private final Handler hand;
	private final String mstrParameter;
	private final int mnRetCode;
	private final CampNetConnection campCon = new CampNetConnection();

	public HttpGetThread(Handler hand, String strParameter, int nRetCode) {
		this.hand = hand;
		mstrParameter = strParameter;
		mnRetCode = nRetCode;
	}

	@Override
	public void run() {
		Message msg = hand.obtainMessage();

		try {
			String result = campCon.doGet(mstrParameter);
			msg.what = mnRetCode;
			msg.obj = result;
		} catch (IOException e) {
			msg.what = RequestReturnCode.REQ_ERROR;
		}
		hand.sendMessage(msg);
	}
}