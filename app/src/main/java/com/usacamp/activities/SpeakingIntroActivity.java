package com.usacamp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.net.HttpHandler;
import com.usacamp.model.SpeakingGroupItem;
import com.usacamp.model.SpeakingIntroItem;
import com.usacamp.utils.SpeakingIntroIteminGroupAdapter;
import com.usacamp.model.SpeakingRowItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SpeakingIntroActivity extends BaseActivity {
    ListView introList1,introList2, introList3;
    private Handler mHandler;
    Button tmpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_intro);
        introList1 = (ListView) findViewById(R.id.speakinginroducelist1);
        introList2 = (ListView) findViewById(R.id.speakinginroducelist2);
        introList3 = (ListView) findViewById(R.id.speakinginroducelist3);
        tmpBtn = (Button)findViewById(R.id.level1);
        tmpBtn.setTextColor(Color.parseColor("#4CAF50"));

        mHandler = new Handler();
        new GetJsonFile().execute();

    }
    void parseJson(String s)
    {
        try {
            JSONObject jsonObj = new JSONObject(s);
            JSONArray jsonArray = jsonObj.getJSONArray("content");
            for(int i = 0; i < jsonArray.length() ;i++)
            {
                JSONObject jsoncampObj = jsonArray.getJSONObject(i);
                JSONArray jArray = new JSONArray(jsoncampObj.getString("camp" + (i + 1)));
                
                Log.d("jsonData", jArray.toString());
                ArrayList<SpeakingGroupItem> tempGroupList = new ArrayList<SpeakingGroupItem>();
                if (jArray != null && jArray.length() > 0 ) {
                    for (int j = 0; j < jArray.length(); j++) {
                        JSONObject jsonItem = jArray.getJSONObject(j);

                        String tempTag = jsonItem.getString("tag");
                        JSONArray jRowArray = new JSONArray(jsonItem.getString("body"));
                        ArrayList<SpeakingRowItem> tempRowItemList = new ArrayList<SpeakingRowItem>();
                        for (int n = 0 ; n < jRowArray.length() ; n++)
                        {
                            JSONObject jsonRow = jRowArray.getJSONObject(n);
                            SpeakingRowItem tempRowItem = new SpeakingRowItem(jsonRow.getString("title"), jsonRow.getString("comment"));
                            tempRowItemList.add(tempRowItem);
                        }
                        SpeakingGroupItem tempGroupItem = new SpeakingGroupItem(tempTag, tempRowItemList);
                        tempGroupList.add(tempGroupItem);
                    }
                }

                SpeakingIntroItem tempIntroItem = new SpeakingIntroItem(i, tempGroupList);
                MyApplication.mNetProc.mLoginUserInf.mSpeakingItemList.add(tempIntroItem);
            }
            showUI(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void setListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();if (listAdapter == null) return;int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            View.MeasureSpec.UNSPECIFIED);int totalHeight = 0;
        View view = null;for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public void onBack(View view) {
        finish();
    }
    public void onButtonCamp(View v)
    {
        tmpBtn.setTextColor(Color.BLACK);
        Button b = (Button)v;
        b.setTextColor(Color.parseColor("#4CAF50"));
        String midtxt = (String) b.getText();
        midtxt = midtxt.substring(4);
        tmpBtn = b;
        showUI(Integer.parseInt(midtxt.trim()) - 1);
    }
    void showUI(int index)
    {
        if(MyApplication.mNetProc.mLoginUserInf.mSpeakingItemList.size() > 0) {
            if(!isContainIndex(index))
            {
                introList1.setAdapter(null);
                introList2.setAdapter(null);
                introList3.setAdapter(null);
            } else {
                SpeakingIntroIteminGroupAdapter adapter1 = new SpeakingIntroIteminGroupAdapter(this, R.layout.layout_speakingintro_item, MyApplication.mNetProc.mLoginUserInf.mSpeakingItemList.get(index).mGroup.get(0).mRow, 1);
                introList1.setAdapter(adapter1);
                SpeakingIntroIteminGroupAdapter adapter2 = new SpeakingIntroIteminGroupAdapter(this, R.layout.layout_speakingintro_item, MyApplication.mNetProc.mLoginUserInf.mSpeakingItemList.get(index).mGroup.get(1).mRow, 1);
                introList2.setAdapter(adapter2);
                SpeakingIntroIteminGroupAdapter adapter3 = new SpeakingIntroIteminGroupAdapter(this, R.layout.layout_speakingintro_item, MyApplication.mNetProc.mLoginUserInf.mSpeakingItemList.get(index).mGroup.get(2).mRow, 1);
                introList3.setAdapter(adapter3);
                setListViewHeightBasedOnChildren(introList1);
                setListViewHeightBasedOnChildren(introList2);
                setListViewHeightBasedOnChildren(introList3);
            }
        }
    }

    boolean isContainIndex(int index)
    {
        for(int i = 0 ; i < MyApplication.mNetProc.mLoginUserInf.mSpeakingItemList.size() ;i++)
        {
            if(MyApplication.mNetProc.mLoginUserInf.mSpeakingItemList.get(i).mnId == index)
                return true;
        }
        return false;
    }

    private class GetJsonFile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = MyApplication.getInstance().SERVER_URL + Constants.SpeakingJson;
            String jsonStr = sh.makeServiceCall(url);

            //Log.e("jsonData", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        parseJson(jsonStr);
                    }
                });

            } else {
                Log.e("jsonData", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
//                    R.layout.list_item, new String[]{ "email","mobile"},
//                    new int[]{R.id.email, R.id.mobile});
//            lv.setAdapter(adapter);
        }
    }
}