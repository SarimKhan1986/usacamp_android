package com.usacamp.activities;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.model.ProgressItem;
import com.usacamp.utils.MessageAlert;
//
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ShareConfirm extends BaseActivity {
    IWXAPI mIwxapi;
    Bitmap shareBmp;
    boolean isCompleteShare = false;
    private String shareMsg;
    Uri shareImagePathUri;
    private final String[] shareMsgs = {
            "游美英语是学习课程，是由哥伦比亚大学多领域专家为像中国这样非英语母语国家的孩子解决英语学习难题而联合开发的一整套学习课程。",
            "游美英语是学习方法，是可以教会孩子遵循听、说、读、写自然规律，逐渐培养自学、自练、自测、自评良好习惯的一整套学习方法。",
            "游美英语App可随时随地使用、反复学习，帮助孩子真正建立母语思维，全面提升英语听说读写能力！",
            "给孩子们推荐一套理念先进、方法有效、内容丰富、体系完整的在线英语学习课程——游美英语！",
            "游美英语是一套真正能够让孩子学会英语的课程，轻松快乐的学好英语让孩子根本停不下来，作为家长的你还在等什么？",
            "别再花冤枉钱了，也别再浪费孩子宝贵的时间了，选择比努力更重要！游美英语——让孩子轻松学会英语！"
    };
    
    String mstrContent = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_confirm);
//        MessageAlert.showMessage(ShareConfirm.this, shareImagePathUri.toString());
        mIwxapi = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID, true);
        mIwxapi.registerApp(MyApplication.getInstance().WECHAT_APPID);
        shareImagePathUri = Uri.parse(getIntent().getStringExtra("saveImagePath"));
        MessageAlert.showMessage(ShareConfirm.this, shareImagePathUri.toString());
        try {
            shareBmp = openSavedImage(shareImagePathUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((ImageView)findViewById(R.id.imageView4)).setImageBitmap(shareBmp);

        findViewById(R.id.sharemsgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DOTextCollect();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
        if(isCompleteShare) {
            LayoutInflater factory = LayoutInflater.from(MyApplication.getInstance().getCurrentActivity());
            View shareCompleteDialogView = factory.inflate(R.layout.sharecomplete, null);
            Dialog shareCompleteDialog = new Dialog(MyApplication.getInstance().getCurrentActivity());
            shareCompleteDialog.setCanceledOnTouchOutside(false);
            shareCompleteDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            shareCompleteDialog.setContentView(shareCompleteDialogView);
            shareCompleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            shareCompleteDialog.show();
            shareCompleteDialog.findViewById(R.id.sharecompletebtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareCompleteDialog.dismiss();
                    finish();
                    Set<Class<?>> hashsetAct = new HashSet<>();
                    hashsetAct.add(ShareCustom.class);
                    hashsetAct.add(ShareType.class);
                    ((MyApplication)getApplication()).killActivities(hashsetAct);
                }
            });
        }
    }
    public void onBack(View view) {
        finish();
    }
    public void SaveShareHistory(){
        File fdelete = new File(getRealPathFromUri(shareImagePathUri));
        if (fdelete.exists()) {
            if (fdelete.delete()) {

            } else {
                System.out.println("file not Deleted :" + getRealPathFromUri(shareImagePathUri));
            }
        }
        isCompleteShare = true;
        int curLesson = 1, curLevel = 1, curPart = 1;

        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size(); i++) {
            ProgressItem progressItem = MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i);
            if (progressItem.mnLevelId == MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel) {
                curLevel = MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel;
                curLesson = progressItem.mnCurrentLessonId;
                curPart = progressItem.mnCurrentPartId;
                break;
            }
        }
        Log.d("ShareConfirm1", curLevel + "-" + curLesson + curPart);

        String strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                "&level_id=" + curLevel + "&lesson_id=" + curLesson + "&part_id=" + curPart +
                "&content=" + mstrContent;
        MyApplication.mNetProc.saveShareContent( "saveShareContent", strRequestParameter);
    }
    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public void onWeixinPresent(View view){

//设置缩略图
        //Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, 800, 600, true);
//        thumbBmp.recycle();
//初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(shareBmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
//调用api接口，发送数据到微信
        mIwxapi.sendReq(req);
        SaveShareHistory();

        //Zhuge

//        JSONObject eventObject = new JSONObject();
//        try {
//            //eventObject.put(Constants.Zhuge_Property_Sharing_Type, Constants.Zhuge_Value_Share_Click_WechatFriend);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Main_Share_Button,  eventObject);

    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    public void onSharePresent(View view){
////设置缩略图
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, shareBmp.getWidth(), shareBmp.getHeight(), true);
//
//        //初始化 WXImageObject 和 WXMediaMessage 对象
//        WXImageObject imgObj = new WXImageObject(thumbBmp);
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = imgObj;
// Initialize WXImageObject and WXMediaMessage objects
//        Bitmap bmp = Bitmap.createScaledBitmap(shareBmp, 800, 600, true);
        WXImageObject imgObj = new WXImageObject(shareBmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

// Set a thumbnail
        //Bitmap thumbBmp = Bitmap.createScaledBitmap(shareBmp, shareBmp.getWidth(), shareBmp.getHeight(), true);
//        bmp.recycle();
//        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
//调用api接口，发送数据到微信
        mIwxapi.sendReq(req);
        SaveShareHistory();

        //Zhuge

//        JSONObject eventObject = new JSONObject();
//        try {
//            eventObject.put(Constants.Zhuge_Property_Sharing_Type, Constants.Zhuge_Value_Share_Click_Circle_Friend);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Click_Main_Share_Button,  eventObject);
    }
    protected void DOTextCollect()
    {
        shareMsg = shareMsgs[0];
        LayoutInflater factory = LayoutInflater.from(MyApplication.getInstance().getCurrentActivity());
        View sharetxtdlgView = factory.inflate(R.layout.share_text_dlg, null);
        Dialog sharetxtDialog = new Dialog(MyApplication.getInstance().getCurrentActivity());
        sharetxtDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        sharetxtDialog.setContentView(sharetxtdlgView);
        sharetxtDialog.getWindow().setGravity(Gravity.BOTTOM);
        sharetxtDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sharetxtDialog.show();
        (sharetxtDialog.findViewById(R.id.sharetxttext)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final int random = new Random().nextInt(6);
                shareMsg = shareMsgs[random];
                ((TextView)sharetxtDialog.findViewById(R.id.sharemsgtxt)).setText(shareMsg);

            }
        });
        (sharetxtDialog.findViewById(R.id.sharemsgapply)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("usacampShareMessage", shareMsg);
                clipboard.setPrimaryClip(clip);
                sharetxtDialog.dismiss();
            }
        });
    }

    protected Bitmap openSavedImage(Uri path) throws IOException {
//        File imgFile = new  File(path);
//        Bitmap myBitmap = null;
//        if(imgFile.exists()){
//            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//        }
        Bitmap myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
        Log.d( "bitmapSize2", String.valueOf(myBitmap.getByteCount()));
        return myBitmap;
    }

    private void removeShareImage(Uri uri)
    {
        File fdelete = new File(getFilePath(uri));

        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" );
            } else {
                System.out.println("file not Deleted :");
            }
        }
    }

    //getting real path from uri
    private String getFilePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            return picturePath;
        }
        return null;
    }

    private void emptyRecylebin () {
//        if (!Metadata.isTrashable()) {
//            return;
//        }
//        DriveResource driveResource = metadata.getDriveId().asDriveResource();
//        Task<Void> toggleTrashTask;
//        if (metadata.isTrashed()) {
//            toggleTrashTask = mDriveResourceClient.untrash(driveResource);
//        } else {
//            toggleTrashTask = mDriveResourceClient.trash(driveResource);
//        }
//        toggleTrashTask = updateUiAfterTask(toggleTrashTask);
//        handleTaskError(toggleTrashTask, R.string.unexpected_error);
//    }
    }
}