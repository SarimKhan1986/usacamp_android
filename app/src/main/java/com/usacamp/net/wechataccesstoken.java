package com.usacamp.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;
import com.usacamp.libs.DoConnection;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * Created by MeiShanJin on 1/12/2018.
 */

public class wechataccesstoken {

    private String mStrWechatCode;
    public  String mStrWechatOpenId;
    Activity mWechatActivity = null;
    int mType = 0;//0- Wechat login, 1 - Only wechat bind
    public wechataccesstoken(Activity activity, int type){
        mWechatActivity = activity;
        mType = type;
    }

    public void wechataccesstoken(String value) {
        Log.d("wechataccesstoken1", value);
        mStrWechatCode = value;
        String GetUrl = Constants.URL_DO_WECHAT_ACCESSTOKEN + "appid=" + MyApplication.getInstance().WECHAT_APPID +
                "&secret=" + Constants.appSecret +
                "&code=" + value +
                "&grant_type=authorization_code";
        new wechataccesstoken.DoAccessTokenAsyncTask().execute(GetUrl);

    }

    private class DoAccessTokenAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DoConnection().getHttpClient();

            HttpGet httpget = new HttpGet(params[0]);
            String res = null;

            try {

                HttpResponse response = httpclient.execute(httpget);
                res = EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String response) {

            if(response == null) {

                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(response);
                Log.d("wechataccesstoken", jsonResponse.toString());
                mStrWechatOpenId = jsonResponse.getString("openid");

                if(mType == 0)
                    MyApplication.getInstance().finishWechatLogin(mStrWechatOpenId);
                else if( mType == 1)
                    MyApplication.mNetProc.setWechatNind(1,"setWechatBind", "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&openid=" + mStrWechatOpenId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
