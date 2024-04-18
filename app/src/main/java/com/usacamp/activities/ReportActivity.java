package com.usacamp.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.FilePath;
import com.usacamp.utils.MessageAlert;
import com.usacamp.utils.ReportGridAdapter;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReportActivity extends BaseActivity {

    GridView mgridReportFile;
    ReportGridAdapter madapterReport = null;

    TextView titleView, subtitleView, buttonTextView;
    EditText contentTextView;
    boolean mfLongClick = false;

    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        type = getIntent().getIntExtra("type", 0);
        mgridReportFile = (GridView)findViewById(R.id.grid_report_file);
        titleView = (TextView) findViewById(R.id.reportTitle);
        subtitleView = (TextView) findViewById(R.id.reporttxt1);
        contentTextView = (EditText) findViewById(R.id.edit_report) ;
        buttonTextView = (TextView) findViewById(R.id.reportBtn) ;
        madapterReport = new ReportGridAdapter(this, R.layout.layout_report_item);
        mgridReportFile.setAdapter(madapterReport);

        mgridReportFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == madapterReport.mlistReport.size()) {
//
//                     Intent intent = new Intent();
//                    //sets the select file to all types of files
//                    intent.setType("file/*");
//                    //allows to select data and return it
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    //starts new activity to select file and return data
//                    startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(pickPhoto, PICK_FILE_REQUEST);
                    return;

                }else if (i < madapterReport.mlistReport.size()){
                    if (!mfLongClick)
                        madapterReport.deleteReport(i);
                }
                madapterReport.notifyDataSetChanged();
                mfLongClick = false;
            }
        });
        mgridReportFile.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mfLongClick = true;
                madapterReport.setDeletable(i);
                madapterReport.notifyDataSetChanged();
                return false;
            }
        });

        //Zhuge
        ////ZhugeSDK.getInstance().track(this, Constants.Zhuge_Event_Enter_Profile_FeedBack);
    }

    public void onBack(View view) {
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

                Uri selectedFileUri = data.getData();

                //mstrSelectedFilePath = FilePath.getPath( ReportActivity.this, selectedFileUri);

                if (selectedFileUri == null || selectedFileUri.equals("")) {
                    return;
                }

                madapterReport.addReport(selectedFileUri);
                RequestCreator reqCreator = Picasso.with(ReportActivity.this).load(selectedFileUri);
                reqCreator.into( (ImageView) findViewById(R.id.img_report_image));
                madapterReport.notifyDataSetChanged();
            }
        }
    }
    public int type = 0;
    public void onSendReport(View view){

        String strReportContent =  ((EditText)findViewById(R.id.edit_report)).getText().toString();
        if ( strReportContent.equals("") == true  ) {
            MessageAlert.showMessage(ReportActivity.this, "请输入反馈内容");
            return;
        }

        String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                "&content=" + strReportContent + "&isnew=" + type;

        int i = 0;
        ArrayList<String> listFilePath = new ArrayList<String>();
        String strTempPath = null;
        while ( i < madapterReport.mlistReport.size() ) {
            strTempPath = FilePath.getPath( ReportActivity.this,  madapterReport.mlistReport .get(i));
            if ( strTempPath == null || strTempPath.equals("") == true )
                continue;
            listFilePath.add(strTempPath);
            i++;
        }

        MyApplication.mNetProc.sendReport("sendReport", strRequestParameter, listFilePath);
        finish();

        //Zhuge
        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put(Constants.Zhuge_Property_Feedback, strReportContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ////ZhugeSDK.getInstance().track(this, Constants.Zhuge_Event_Click_Profile_FeedBack_SubmitButton,  eventObject);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
        if(type == 1){
            titleView.setText("举报或投诉");
            subtitleView.setText("举报或投诉内容");
            contentTextView.setHint("请尽可能详细的描述举报或投诉的内容，我们会尽快与您取得联系！");
            buttonTextView.setText("提交内容");
        } else {
            titleView.setText("问题反馈");
            subtitleView.setText("问题或建议");
            contentTextView.setHint("请尽可能详细的描述您的问题，我们会尽快与您取得联系！");
            buttonTextView.setText("上传图片");
        }
    }
}
