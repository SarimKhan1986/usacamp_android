package com.usacamp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import org.w3c.dom.Text;

public class SpeakingShareActivity extends BaseActivity {
    IWXAPI mIwxapi;
    int nlevelIdx, nlessonIdx, npartIdx, nReadCount,nHighScoreCount ;
    ConstraintLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_share);
        nlevelIdx = getIntent().getIntExtra("SpeakingShareLevelId", 0);
        nlessonIdx = getIntent().getIntExtra("SpeakingShareLessonId", 0);
        npartIdx = getIntent().getIntExtra("SpeakingSharePartId", 0);
        nReadCount = getIntent().getIntExtra("SpeakingShareReadCount", 0);
        nHighScoreCount = getIntent().getIntExtra("SpeakingShareHighCount", 0);
        String partAB = "A";
        if(npartIdx == 2) partAB = "B" ;

        String strName = MyApplication.mNetProc.mLoginUserInf.mstrName + "学习到" + "Camp" + nlevelIdx + "-" + nlessonIdx + "-" + partAB;
        ((TextView)findViewById(R.id.speaking_share_name_text)).setText(strName);
        ((TextView)findViewById(R.id.speaking_share_complete_count)).setText(String.valueOf(nReadCount));
        ((TextView)findViewById(R.id.speaking_share_incomplete_count)).setText(String.valueOf(nHighScoreCount));
        relativeLayout = (ConstraintLayout) findViewById(R.id.speaking_share_ry);
        applyQrCode((ImageView)findViewById(R.id.speaking_share_qrcode));

        mIwxapi = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID, true);
        mIwxapi.registerApp(MyApplication.getInstance().WECHAT_APPID);
    }
    public void SaveShareHistory() {
       String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                "&level_id=" + nlevelIdx + "&lesson_id=" + nlessonIdx + "&part_id=" + npartIdx + "&kind=1" + "&readCnt=" + nReadCount + "&starCnt=" + nHighScoreCount;

        MyApplication.mNetProc.saveShareContent("saveShareContent", strRequestParameter);
    }

    public void onBack(View view) {
        finish();
    }
    private void applyQrCode(ImageView view)
    {
        Bitmap qrCodeBitmap = null;

        String strUrl = MyApplication.getInstance().SERVER_URL + "Code/sharedCode?p=" + MyApplication.mNetProc.mLoginUserInf.mnbase64UserID + "&w=1";
        int qrCodeWidth = 200;
        if(MyApplication.getInstance().isTablet)
            qrCodeWidth = 300;
        else
            qrCodeWidth = 300;

        qrCodeBitmap = QRGenerater.QRGenerater(strUrl, qrCodeWidth, qrCodeWidth);

        view.setImageBitmap(qrCodeBitmap);
    }
    private Bitmap makeImage ()
    {
        View linearview = relativeLayout;
//        linearview.setDrawingCacheEnabled(true);
//        linearview.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        linearview.layout(0, 0, linearview.getWidth(), linearview.getHeight());

        final Bitmap bitmap = Bitmap.createBitmap(linearview.getWidth(),
                linearview.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        linearview.draw(canvas);

        return getResizedBitmap(bitmap, 1000);

    }
    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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
        return Bitmap.createScaledBitmap(image, width, height, false);
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
}