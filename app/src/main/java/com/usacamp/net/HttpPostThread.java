package com.usacamp.net;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.ArrayList;

public class HttpPostThread implements Runnable {

	private final Handler hand;
	private final String mstrFunc;
	private final String mstrParameter;
	private final int mnRetCode;
	private final CampNetConnection campCon = new CampNetConnection();

	private final boolean mfAttachedfile;
	private   ArrayList<String> mlistAttachedFilePath;

	public HttpPostThread(Handler hand, String strFunc, String strParameter, int nRetCode) {
		this.hand = hand;
		mstrFunc = strFunc;
		mstrParameter = strParameter;
		mnRetCode = nRetCode;

		mfAttachedfile = false;
	}

	public HttpPostThread(Handler hand, String strFunc, String strParameter, ArrayList<String> listAttachedFilePath, int nRetCode) {
		this.hand = hand;
		mstrFunc = strFunc;
		mstrParameter = strParameter;
		mnRetCode = nRetCode;
		mfAttachedfile = true;
		mlistAttachedFilePath  = new ArrayList<String>();
		mlistAttachedFilePath  = listAttachedFilePath;
	}

	@Override
	public void run() {
		Message msg = hand.obtainMessage();
		if (mfAttachedfile == false) { // not attached file
			try {
				String strResult = campCon.doPost(mstrFunc, mstrParameter);
				msg.what = mnRetCode;
				msg.obj = strResult;
			} catch(IOException e){
				msg.what = RequestReturnCode.REQ_ERROR;
			}
			hand.sendMessage(msg);
		}
		else { // attached file
			try {
				String strResult = campCon.doPostWithAttachedFile(mstrFunc, mstrParameter, mlistAttachedFilePath);
				msg.what = mnRetCode;
				msg.obj = strResult;
			} catch(IOException e){
				msg.what = RequestReturnCode.REQ_ERROR;
			}
			hand.sendMessage(msg);
		}
	}


}