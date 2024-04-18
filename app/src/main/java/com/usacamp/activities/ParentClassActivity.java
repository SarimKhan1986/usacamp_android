package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.ParentAdapter;
import com.usacamp.model.ParentItem;
//

import java.util.ArrayList;

public class ParentClassActivity extends BaseActivity {

    public ParentAdapter mParentAdapter;
    public ListView mParentListView;
    int mainCount = MyApplication.mNetProc.mLoginUserInf.showArticleCount;
    private final ArrayList<ParentItem> mainParentItemList = new ArrayList<ParentItem>(MyApplication.mNetProc.mLoginUserInf.showArticleCount);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_class);
        if(mainCount > MyApplication.mNetProc.mLoginUserInf.mlistHotParentItem.size())
            mainCount = MyApplication.mNetProc.mLoginUserInf.mlistHotParentItem.size();

        for(int i = 0 ; i < mainCount; i++)
            mainParentItemList.add(MyApplication.mNetProc.mLoginUserInf.mlistHotParentItem.get(i));

        mParentAdapter = new ParentAdapter(this, R.layout.parentitemxml, mainParentItemList);
        mParentListView = findViewById(R.id.listparent);
        mParentListView.setAdapter(mParentAdapter);
        mParentListView.setItemsCanFocus(true);

        mParentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int nId, long l) {
                ParentItem item = MyApplication.mNetProc.mLoginUserInf.mlistHotParentItem.get(nId);
                String strParameter = "getArticleById?id=" + item.mIdx;
                MyApplication.mNetProc.getArticlDetail( strParameter);
            }
        });

        //Zhuge

       // ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Enter_Main_Parent_Page);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void goCategory0(View view)
    {
        Intent parentTypeInt = new Intent(this, parent_category.class);
        parentTypeInt.putExtra("category", 0);
        MyApplication.mNetProc.mLoginUserInf.currentParentType = 0;
        startActivity(parentTypeInt);
    }
    public void goCategory1(View view)
    {
        MyApplication.mNetProc.mLoginUserInf.mlistParentTypeItem.clear();
        String strParmater = "getArticleListByType?type=1";
        MyApplication.mNetProc.mLoginUserInf.currentParentType = 1;
        MyApplication.mNetProc.getArticleTypeList(strParmater);
    }
    public void goCategory2(View view)
    {
        MyApplication.mNetProc.mLoginUserInf.mlistParentTypeItem.clear();
        String strParmater = "getArticleListByType?type=2";
        MyApplication.mNetProc.mLoginUserInf.currentParentType = 2;
        MyApplication.mNetProc.getArticleTypeList(strParmater);
    }
    public void goCategory3(View view)
    {
        MyApplication.mNetProc.mLoginUserInf.mlistParentTypeItem.clear();
        String strParmater = "getArticleListByType?type=3";
        MyApplication.mNetProc.mLoginUserInf.currentParentType = 3;
        MyApplication.mNetProc.getArticleTypeList(strParmater);
    }
    public void goCategory4(View view)
    {
        Intent vipInt = new Intent(this, vipmember.class);
        startActivity(vipInt);
//        MyApplication.mNetProc.mLoginUserInf.mlistParentTypeItem.clear();
//        String strParmater = "getArticleListByType?type=4";
//        MyApplication.mNetProc.mLoginUserInf.currentParentType = 4;
//        MyApplication.mNetProc.getArticleTypeList(this ,strParmater);
    }

    public void onBack(View view) {
        finish();
    }
}
