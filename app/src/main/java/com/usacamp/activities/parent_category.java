package com.usacamp.activities;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.utils.ParentAdapter;
import com.usacamp.model.ParentItem;

import java.util.ArrayList;

public class parent_category extends BaseActivity {
    public ParentAdapter mParentAdapter;
    public ListView mParentTypeListView;
    ArrayList<ParentItem> tempParentList = new ArrayList<ParentItem>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_category);
        int catIdx = getIntent().getIntExtra("category", 0);
        TextView titleTxt = (TextView) findViewById(R.id.parent_category_title);

        switch (catIdx){
            case 0:
                titleTxt.setText("查看全部");
                break;
            case 1:
                titleTxt.setText("家长必读");
                break;
            case 2:
                titleTxt.setText("英语学习");
                break;
            case 3:
                titleTxt.setText("游美新闻");
                break;
            case 4:
                titleTxt.setText("会员特权");
                break;
        }

        if(catIdx == 0)
            tempParentList = MyApplication.mNetProc.mLoginUserInf.mlistHotParentItem;
        else
            tempParentList = MyApplication.mNetProc.mLoginUserInf.mlistParentTypeItem;

        mParentAdapter = new ParentAdapter(this, R.layout.parentitemxml, tempParentList);
        mParentTypeListView = findViewById(R.id.listparenttype);
        mParentTypeListView.setAdapter(mParentAdapter);
        mParentTypeListView.setItemsCanFocus(true);

        mParentTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int nId, long l) {
                ParentItem item = tempParentList.get(nId);
                String strParameter = "getArticleById?id=" + item.mIdx;
                MyApplication.mNetProc.getArticlDetail( strParameter);
            }
        });
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
