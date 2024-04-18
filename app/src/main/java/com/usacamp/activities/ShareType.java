package com.usacamp.activities;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.model.ProgressItem;
import com.usacamp.utils.MessageAlert;
import com.usacamp.utils.ShareSliderAdapter;
import com.usacamp.model.ShareTypeItem;
import com.usacamp.wxapi.WeChatLoginActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShareType extends BaseActivity {
    private LinearLayout dotsly;
    ImageView[] dots_item ;
    private ViewPager2 shareSliderPager;
    ShareSliderAdapter shareSliderAdapter;
    String TAG = "ShareType";
    int currentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_type);
        //////

        dotsly = (LinearLayout) findViewById(R.id.layout_dots);
        shareSliderPager = (ViewPager2) findViewById(R.id.pager);
        viewpagerW = shareSliderPager.getMeasuredWidth();
        viewpagerH = shareSliderPager.getMeasuredHeight();
        ArrayList<ShareTypeItem> items = new ArrayList<>();
        registerReceiver();
        shareSliderAdapter = new ShareSliderAdapter(this, new ArrayList<ShareTypeItem>());
        //////////
        String currentProgress = "CAMP1-1-1";

        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size(); i++) {
            ProgressItem progressItem = MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i);
            if (progressItem.mnLevelId == MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel) {
                currentProgress = "CAMP" + MyApplication.mNetProc.mLoginUserInf.mnSelectedLevel + "-" + progressItem.mnCurrentLessonId ;
                if(progressItem.mnLessonId >= Constants.DIFF_PART_LEVEL) {
                    if (progressItem.mnCurrentPartId == Constants.BIG_PARTS_COUNT)
                        currentProgress += "-quiz";
                    else
                        currentProgress += "-" + progressItem.mnCurrentPartId;
                }else{
                    if (progressItem.mnCurrentPartId == Constants.SMALL_PARTS_COUNT)
                        currentProgress += "-quiz";
                    else
                        currentProgress += "-" + progressItem.mnCurrentPartId;
                }
                break;
            }
        }

        ///////////

        items.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            items.add(new ShareTypeItem(0, getDrawable(R.drawable.shareimage1), false, "", "", 0, null));
            items.add(new ShareTypeItem(0, getDrawable(R.drawable.sharenewback2), true, MyApplication.mNetProc.mLoginUserInf.mstrName, currentProgress, Color.parseColor("#00B396"),null));
            items.add(new ShareTypeItem(1, getDrawable(R.drawable.sharenewback1), true, MyApplication.mNetProc.mLoginUserInf.mstrName, currentProgress, Color.parseColor("#00B396"),null));
            items.add(new ShareTypeItem(2, getDrawable(R.drawable.shareimage3), true , MyApplication.mNetProc.mLoginUserInf.mstrName, "", Color.parseColor("#FFA535"),null));
            items.add(new ShareTypeItem(3, getDrawable(R.drawable.shareimage4), true , MyApplication.mNetProc.mLoginUserInf.mstrName, "", Color.parseColor("#37C595"),null));
            //items.add(new ShareTypeItem(4, getDrawable(R.drawable.shareimage5), true , MyApplication.mNetProc.mLoginUserInf.mstrName, "", Color.parseColor("#34A91C"),null));
        }
        float scaleFactor = 0.9f;

        shareSliderPager.setOffscreenPageLimit(4);
        shareSliderPager.setClipToPadding(false);
        shareSliderPager.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        if (MyApplication.getInstance().isTablet)
            compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        else
            compositePageTransformer.addTransformer(new MarginPageTransformer(5));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(scaleFactor + r * (1 - scaleFactor));
            page.setScaleX(scaleFactor + Math.abs(position) * (1 - scaleFactor));
        });

        shareSliderAdapter.setItems(items);
        shareSliderPager.setPageTransformer(compositePageTransformer);
        shareSliderPager.setAdapter(shareSliderAdapter);

        // displaying selected image first
        dots_item = new ImageView[items.size()];

        addBottomDots(dotsly, items.size(), 0);
        shareSliderPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentIndex = position;

                addBottomDots(dotsly, items.size(), currentIndex);
            }
        });
        ((Button)findViewById(R.id.shareBtn)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                boolean isAppInstalled = appInstalledOrNot("com.tencent.mm");

               if (isAppInstalled) {
                    Bitmap bitmap = makeImage(currentIndex);
                    String savedImgPath = saveImage(bitmap);
                    Intent intent = new Intent(ShareType.this, ShareConfirm.class);

                    intent.putExtra("saveImagePath", savedImgPath);
                    startActivity(intent);
                } else {
                    MessageAlert.showMessage(ShareType.this, "你在这部电话机上没有安装任何工具。 请安装并重试。");
                }

            }
        });

        ((Button)findViewById(R.id.button28)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAppInstalled = appInstalledOrNot("com.tencent.mm");

                if (isAppInstalled) {
                    Intent customInt = new Intent(ShareType.this, ShareCustom.class);
                    startActivity(customInt);
                } else {
                    MessageAlert.showMessage(ShareType.this, "你在这部电话机上没有安装任何工具。 请安装并重试。");
                }
            }
        });
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_META_DATA);
        if (packageInfoList != null) {
            for (PackageInfo packageInfo : packageInfoList) {
                String packageName = packageInfo.packageName;

                if (packageName != null && packageName.equals(uri)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
//        Log.d(TAG, String.valueOf(dots_item.length));
        layout_dots.removeAllViews();
        for (int i = 0; i < size; i++) {
            Log.d(TAG, String.valueOf(size));
            dots_item[i] = new ImageView( this);
            int width_height = 10;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height * 2, width_height));
            params.setMargins(10, 0, 10, 0);
            dots_item[i].setLayoutParams(params);
            dots_item[i].setImageResource(R.drawable.dotslider);
            layout_dots.addView(dots_item[i]);
        }

        if (size > 0) {
            dots_item[current].setImageResource(R.drawable.button_shape);
        }

    }
    private String saveImage(Bitmap bitmap)
    {
        //Toast.makeText(this, "saveImage", Toast.LENGTH_SHORT).show();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, String.valueOf(System.currentTimeMillis()), null);
       // Toast.makeText(this, "saveImageEnd", Toast.LENGTH_SHORT).show();
        return path;
//        String imagePath = MediaStore.Images.Media.insertImage(
//                getContentResolver(),
//                bitmap,
//                "usacamp_shareImg",
//                "usacamp_shareImg"
//        );
    }
    public void onBack(View view) {
        finish();
    }

    public void onActRule(View view){
        Intent intentActRule = new Intent(this, ActRuleActivity.class);
        startActivity(intentActRule);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;

    }

    private Bitmap makeImage (int position)
    {
       // Toast.makeText(this, "makeImage", Toast.LENGTH_SHORT).show();
        //View linearview = shareSliderPager.getChildAt(0);
//        ConstraintLayout shareView = linearview.getRootView().findViewById(R.id.shareCL);
        View shareView = shareSliderAdapter.getItemView(position);
//        linearview.setDrawingCacheEnabled(true);
//        linearview.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        linearview.layout(0, 0, linearview.getWidth(), linearview.getHeight());

        final Bitmap bitmap = Bitmap.createBitmap(shareView.getWidth(),
                shareView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        shareView.draw(canvas);
        //Toast.makeText(this, "makeImageEnd", Toast.LENGTH_SHORT).show();
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
    int viewpagerW = 0;
    int viewpagerH = 0;
    void applyConstraintLayout()
    {
        View v = null;
        ViewGroup.LayoutParams params = null;
        for(int i = 0 ;i < 4 ; i++) {
            v = shareSliderAdapter.getContainermView(i);
            params = v.getLayoutParams();
            Log.d("PhoneWidth", String.valueOf(MyApplication.getInstance().getPhoneWidth()));
            Log.d("PhoneHeight", String.valueOf(MyApplication.getInstance().getPhoneHeight()));
            params.height = v.getMeasuredHeight() - 20;
            params.width = (int)(params.height / 908.0f * 510.0f);

            Log.d("params.height", String.valueOf(params.height));
            Log.d("params.height", String.valueOf(params.width));
            v.setLayoutParams(params);
        }

        shareSliderPager.setPadding((MyApplication.getInstance().getPhoneWidth() - params.width)/2,0,(MyApplication.getInstance().getPhoneWidth() - params.width)/2 ,0);
    }
    private void registerReceiver() {
        registerReceiver(receiver, new IntentFilter(Constants.SHARE_PAGE_RESIZE_MAG));
    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception ignore) {}
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("transerfertest", "receifver");
            handleBroadcast(intent);
        }
    };
    private void handleBroadcast(@NonNull Intent from) {
        Log.d("transerfertest", "receifver1");
        final String action = from.getAction();
        if (Constants.SHARE_PAGE_RESIZE_MAG.equalsIgnoreCase(action)) {
            applyConstraintLayout();
            shareSliderPager.setCurrentItem(0);
        }
    }
}