package com.usacamp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.QRGenerater;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareActivity extends BaseActivity {

    boolean mfIsFinished = false;
    int mnLevelId = 0;
    int mnLessonId = 0;
    int mnPartId = 0;
    IWXAPI mIwxapi;
    String mstrContent = "";
    ImageView qrImage;
    LinearLayout beforeSharedlyt, afterSharedlyt;
    LinearLayout mlytShareInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mIwxapi = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID, true);
        mIwxapi.registerApp(MyApplication.getInstance().WECHAT_APPID);

        mfIsFinished = getIntent().getBooleanExtra("Finish_Lesson", false);
        mnLevelId = getIntent().getIntExtra("Level_Id", 0);
        mnLessonId = getIntent().getIntExtra("Lesson_Id", 0);
        beforeSharedlyt = (LinearLayout) findViewById(R.id.shareLayout);
        afterSharedlyt = (LinearLayout) findViewById(R.id.sharedafter);
        mlytShareInformation = (LinearLayout)findViewById(R.id.lyt_share_information);

        if(mfIsFinished) {
            ((TextView)findViewById(R.id.text_finished_lesson)).setText(getShareText());
            ((TextView)findViewById(R.id.text_user_name)).setText(MyApplication.mNetProc.mLoginUserInf.mstrName);
            mlytShareInformation.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(MyApplication.mNetProc.mLoginUserInf.mstrPic);
            RequestCreator reqCreator = Picasso.with(this).load(uri);
            reqCreator.into((CircularImageView) findViewById(R.id.sharepic));
        } else
            mlytShareInformation.setVisibility(View.INVISIBLE);

        //Zhuge

        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put(Constants.Zhuge_Property_Entrance, getIntent().getStringExtra(Constants.Zhuge_Property_Entrance));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Enter_Main_Share_Page,  eventObject);
    }

    private String getShareText()
    {
        if(mnLevelId >=9 )
            mnPartId = 9;
        else
            mnPartId = 8;
        return String.format("CAMP%d-%d", mnLevelId, mnLessonId);
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshAllInformation();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void refreshAllInformation(){

//        Bitmap QRCodeBitmap = null;
//
//        String strUrl = Constants.MAIN_URL + "Code/sharedCode?p=" + MyApplication.mNetProc.mLoginUserInf.mnbase64UserID + "&w=1";
//
//        QRCodeBitmap = QRGenerater.QRGenerater(strUrl, 1000, 1000);

    }
    private void applyQrCode(ImageView view)
    {
        Bitmap qrCodeBitmap = null;

        String strUrl = MyApplication.getInstance().SERVER_URL + "Code/sharedCode?p=" + MyApplication.mNetProc.mLoginUserInf.mnbase64UserID + "&w=1";

        qrCodeBitmap = QRGenerater.QRGenerater(strUrl, 300 , 300);

        view.setImageBitmap(qrCodeBitmap);
    }
    public void onBack(View view) {
        finish();
    }

    public void SaveShareHistory(){
        if(mfIsFinished) {
            String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                    "&level_id=" + mnLevelId + "&lesson_id=" + mnLessonId + "&part_id=" + mnPartId +
                    "&content=" + mstrContent;
            MyApplication.mNetProc.saveShareContent( "saveShareContent", strRequestParameter);

        }
    }

    public void onWeixinPresent(View view){
        Bitmap shareBmp = makeImage();
//初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(shareBmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

//设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, shareBmp.getWidth(), shareBmp.getHeight(), true);
        shareBmp.recycle();

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
//调用api接口，发送数据到微信
        mIwxapi.sendReq(req);
        SaveShareHistory();

        //Zhuge

        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put(Constants.Zhuge_Property_Sharing_Type, Constants.Zhuge_Value_Share_Click_WechatFriend);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Main_Share_Button,  eventObject);

    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    public void onSharePresent(View view){
        Bitmap shareBmp = makeImage();

        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(shareBmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

//设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, shareBmp.getWidth(), shareBmp.getHeight(), true);
        shareBmp.recycle();

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
//调用api接口，发送数据到微信
        mIwxapi.sendReq(req);
        SaveShareHistory();

        //Zhuge

        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put(Constants.Zhuge_Property_Sharing_Type, Constants.Zhuge_Value_Share_Click_Circle_Friend);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Main_Share_Button,  eventObject);
    }

    public void onActRule(View view){
        Intent intentActRule = new Intent(this, ActRuleActivity.class);
        startActivity(intentActRule);
    }

    private Bitmap makeImage ()
    {
        LayoutInflater inflater =
                (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View linearview = inflater.inflate(R.layout.activity_share_after, null);
        ImageView qrcodeImg = (ImageView) linearview.findViewById(R.id.shareqrcope);
        applyQrCode(qrcodeImg);
        if(mfIsFinished)
        {
            linearview.findViewById(R.id.shareprogress).setVisibility(View.VISIBLE);
            String strTmp = MyApplication.mNetProc.mLoginUserInf.mstrName + "同学已学习到" + getShareText();
            ((TextView)linearview.findViewById(R.id.shareprogress)).setText(strTmp);
        } else {
            linearview.findViewById(R.id.shareprogress).setVisibility(View.INVISIBLE);
        }

        linearview.setDrawingCacheEnabled(true);
        linearview.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearview.layout(0, 0, linearview.getMeasuredWidth(), linearview.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(linearview.getMeasuredWidth(),
                linearview.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        linearview.draw(canvas);
        return getResizedBitmap(bitmap, 1000);

    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private File saveBitMap(Context context, View drawView){
        File pictureFileDir = new File(Environment.getExternalStorageDirectory().toString());
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis()+".png";
        File pictureFile = new File(filename);
        Bitmap bitmap = (Bitmap) makeImage();
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }
}
