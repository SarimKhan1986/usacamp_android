package com.usacamp.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.usacamp.BuildConfig;
import com.usacamp.activities.LoginActivity;
import com.usacamp.activities.MainActivity;
import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;
import com.usacamp.libs.DoConnection;
import com.usacamp.utils.RSADecrypt;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by MeiShanJin on 1/12/2018.
 */

public class Aurora_RealNumber {
    String param1;
    public Aurora_RealNumber(String value){
        param1 = value;
        String GetUrl = Constants.URL_AURORA_REALNUMBER;
        new Aurora_RealNumber.DoAccessTokenAsyncTask().execute(GetUrl);

    }

    private class DoAccessTokenAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DoConnection().getHttpClient();

            HttpPost httppost = new HttpPost(params[0]);
            String res = null;

            try {
                String userPass = Constants.Aurora_AppKey + ":" + Constants.Aurora_MasterSecret;
                String basicAuth = "Basic " + Base64.encodeToString(userPass.getBytes(), Base64.DEFAULT);
                httppost.setHeader("Authorization", basicAuth);
                httppost.setHeader("Content-Type", "application/json");
                httppost.setHeader("Accept-Charset","utf-8");

//                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("loginToken", param1));
//                nameValuePairs.add(new BasicNameValuePair("exID", "12345666"));
//                UrlEncodedFormEntity en = new UrlEncodedFormEntity(nameValuePairs, "utf-8");
//                httppost.setEntity(en);

                JSONObject paramjson = new JSONObject();
                paramjson.put("loginToken", param1);
                paramjson.put("exID", "1234566");
                httppost.setEntity(new StringEntity(paramjson.toString(),"UTF-8"));

                HttpResponse response = httpclient.execute(httppost);

                res = EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
                Log.d("Aurora_RealNumber", jsonResponse.toString());
                String tmpPhoneStr = "", myPhoneNumber;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String EncryptedPhone = (String) jsonResponse.get("phone");
                    tmpPhoneStr = RSADecrypt.decrypt(EncryptedPhone);
                }

                myPhoneNumber = tmpPhoneStr.substring(tmpPhoneStr.length() - 11);
                MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber = myPhoneNumber;
                if(myPhoneNumber.length() != 11)
                    MainActivity.mainActivityInstance.onNormalLogin();
                if(MyApplication.mNetProc.mLoginUserInf.mbWechatAuroraSind) {
                    MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_WECHATBIND, "loginWithPhone", "phone=" + MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber +
                            "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_SMS, MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
                } else
                    MyApplication.mNetProc.loginWithPhone(Constants.LOGIN_TYPE_LOGIN,"loginWithPhone", "phone=" +  MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber +
                        "&hardTypeId=" + "2" + "&hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrDeviceID + "&version=" + BuildConfig.VERSION_NAME + "&type=0", Constants.SINGIN_TYPE_SMS, MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber);
                Log.d("Aurora_RealNumber", myPhoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
