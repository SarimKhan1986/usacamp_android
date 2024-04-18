package com.usacamp.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.print.PrinterCapabilitiesInfo;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.net.ImageDownloaderTask;
//

import java.io.InputStream;
import java.net.URL;

public class BannerDetail extends BaseActivity {
    private TextView txt_title;
    private String link = null, backColor = "#FFFFFF", fontColor = "#000000";
    WebView webViewBanner = null;
    Handler mHandler = null;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detail);
        mHandler = new Handler();
        txt_title = (TextView)findViewById(R.id.bannerActTitle);
        link = getIntent().getStringExtra("bannerlink");
        if(link == null || link.equals(""))
            return;
        backColor = getIntent().getStringExtra("backgroundcolor");
        fontColor = getIntent().getStringExtra("fontcolor");
        if(backColor.equals(""))
            backColor = "#FFFFFF";
        if(fontColor.equals(""))
            fontColor = "#000000";

        webViewBanner = (WebView) findViewById(R.id.webview_banner);
        webViewBanner.getSettings().setLoadWithOverviewMode(true);
        webViewBanner.setClickable(true);
        webViewBanner.getSettings().setJavaScriptEnabled(true);
        webViewBanner.addJavascriptInterface(new JavaScriptInterface(), "interface");
        webViewBanner.setWebViewClient(new myWebClient());
        webViewBanner.getSettings().setUseWideViewPort(true);
        webViewBanner.getSettings().setLoadsImagesAutomatically(true);
        webViewBanner.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        ((RelativeLayout)findViewById(R.id.appbar)).setBackgroundColor(Color.parseColor(backColor));
        ((TextView)findViewById(R.id.bannerActTitle)).setTextColor(Color.parseColor(fontColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webViewBanner.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webViewBanner.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webViewBanner.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if(MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin)
            link += "?u=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&a=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id;

        webViewBanner.loadUrl(link);

        //////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Enter_Main_TopBanner);

    }

    private Drawable loadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }

    public void onBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this);
        webViewBanner.reload();
    }
    int numCliked = 0;
    private void saveImageToPicture(String link)
    {
//        Drawable drawable = getResources().getDrawable(R.drawable.trainwechatlinkqr);
//
//        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//        String imagePath = MediaStore.Images.Media.insertImage(
//                getContentResolver(),
//                bitmap,
//                "usacamp_traincampQrCode1",
//                "usacamp_traincampQrCode1"
//        );
//        Toast.makeText(this, "已成功保存到相册", Toast.LENGTH_LONG).show();
//        numCliked++;
//        Log.d("numClicked!", String.valueOf(numCliked));
        new ImageDownloaderTask(link, this).execute(link);
    }

    private void saveClipboard(String msg)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("usacampCopyWechatAccount", msg);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();
    }

    public class JavaScriptInterface {
        public JavaScriptInterface() {

        }

        @JavascriptInterface
        public void finishHTMLloading(String title) {
            boolean post = mHandler.post(new Runnable() {
                public void run() {
                    txt_title.setText(title);
                }
            });
        }

        @JavascriptInterface
        public void onClickNormalShareButton (){
            String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                    "&kind=0";

            MyApplication.mNetProc.getShareInfo("getShareInfo", strRequestParameter);
        }

        @JavascriptInterface
        public void onSaveImgToGallery(String link){
            saveImageToPicture(link);
        }

        @JavascriptInterface
        public void onCopyToClipboard (String msg){
            saveClipboard(msg);
        }

        @JavascriptInterface
        public void onClick99YuanShareButton()
        {

        }
        @JavascriptInterface
        public void onClickGoLoginButton ()
        {
            finish();
            Intent loginAct = new Intent(BannerDetail.this, AuroraLogin.class);
            startActivity(loginAct);
        }
        @JavascriptInterface
        public void onClickStudyButton ()
        {

        }
        @JavascriptInterface
        public void onClickTestButton ()
        {

        }
    }
    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            Log.d("banner_", "page Started");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // TODO Auto-generated method stub
            Log.d("banner_", "shouldOverrideUrlLoading");
            return false;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            Log.d("banner_", "page finished");
            String strtmp = "javascript:SendMessageLoadingFinish ();";
            view.loadUrl(strtmp);

        }
        @Override
        public void onLoadResource(WebView view, String url) {
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
            Log.d("banner_ssl", er.toString());
            handler.proceed();
        }
        @Override
        public void onReceivedHttpError(
                WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Log.d("banner_HttpError", errorResponse.toString());
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d("banner_ReceivedError", error.toString());
        }

    }


}