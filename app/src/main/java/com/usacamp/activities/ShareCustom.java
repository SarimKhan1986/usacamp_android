package com.usacamp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.FilePath;
import com.usacamp.utils.MessageAlert;
import com.usacamp.model.ProgressItem;
import com.usacamp.utils.QRGenerater;
import com.usacamp.utils.ShareCustomSliderAdapter;
import com.usacamp.model.ShareTypeItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShareCustom extends BaseActivity{
    private ViewPager sharecustomSliderPager;
    File mPhotoFile;
    int currentPosition = 0;
    int previousPosition = -1;
    ImageView mCustomPhoto;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    ShareCustomSliderAdapter sharecustomSliderAdapter;
    float scalediff;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private final String TAG = "ShareCustom";

    private ConstraintLayout releativeLayout, cardCL,cardCY;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_custom);
        sharecustomSliderPager = (ViewPager) findViewById(R.id.viewPager_sharecustom);
        mCustomPhoto = (ImageView) findViewById(R.id.sharecustomphoto);
        releativeLayout = (ConstraintLayout)findViewById(R.id.relativeLayout4);
        cardCL = (ConstraintLayout)findViewById(R.id.cardCL);
        cardCY = (ConstraintLayout)findViewById(R.id.cardly);
        ArrayList<ShareTypeItem> items = new ArrayList<>();

        sharecustomSliderAdapter = new ShareCustomSliderAdapter(this, new ArrayList<ShareTypeItem>());

        String cardtxt = "哥伦比亚大学专家团队研发\n" +
                "限时0元体验·全部课程";
        items.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            items.add(new ShareTypeItem(0, getDrawable(R.drawable.sharecustomphotoback), true, MyApplication.mNetProc.mLoginUserInf.mstrName, cardtxt, Color.parseColor("#9C5C34"), getDrawable(R.drawable.sharecustomimage0)));
            items.add(new ShareTypeItem(1, getDrawable(R.drawable.sharecustomphotoback11), true, MyApplication.mNetProc.mLoginUserInf.mstrName, cardtxt, Color.parseColor("#9C5C34"), getDrawable(R.drawable.sharecustomimage1)));
            items.add(new ShareTypeItem(2, getDrawable(R.drawable.sharecustomphotoback22), true , MyApplication.mNetProc.mLoginUserInf.mstrName, cardtxt, Color.parseColor("#FFDD00"), getDrawable(R.drawable.sharecustomimage2)));
            items.add(new ShareTypeItem(3, getDrawable(R.drawable.sharecustomphotoback33), true , MyApplication.mNetProc.mLoginUserInf.mstrName, cardtxt, Color.parseColor("#FFFFFF"), getDrawable(R.drawable.sharecustomimage3)));
            items.add(new ShareTypeItem(4, getDrawable(R.drawable.sharecustomphotoback44), true , MyApplication.mNetProc.mLoginUserInf.mstrName, cardtxt, Color.parseColor("#FF487A"), getDrawable(R.drawable.sharecustomimage4)));
        }
        sharecustomSliderAdapter.setItems(items);
        sharecustomSliderPager.setAdapter(sharecustomSliderAdapter);
        sharecustomSliderPager.setOffscreenPageLimit(0);

        sharecustomSliderAdapter.setOnItemClickListener(new ShareCustomSliderAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(View view, ShareTypeItem obj) {
                Log.d(TAG, String.valueOf(obj.idx));
                previousPosition = currentPosition;
                currentPosition = obj.idx;

                clearSelectionBackground(previousPosition);
                setSelectionBackground(currentPosition);
                setBottomImage(obj);
            }
        });
        ((Button)findViewById(R.id.sharecustommakeimgbtn)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Bitmap bitmap = makeImage();
                String savedImgPath = saveImage(bitmap);
                Intent intent = new Intent(ShareCustom.this, ShareConfirm.class);
                //ByteArrayOutputStream btArrayOutputStream = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.PNG, 100, btArrayOutputStream);
                //Log.d("bitmapSize = ", String.valueOf(btArrayOutputStream.size()));
                //intent.putExtra("image", btArrayOutputStream.toByteArray());
                intent.putExtra("saveImagePath", savedImgPath);
                startActivity(intent);
            }
        });
        setBottomImage(items.get(0));
        mCustomPhoto.setOnTouchListener(new View.OnTouchListener() {

            ConstraintLayout.LayoutParams parms;
            int startwidth;
            int startheight;
            float dx = 0, dy = 0, x = 0, y = 0;
            float angle = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final ImageView view = (ImageView) v;

                try {
                    ((BitmapDrawable) view.getDrawable()).setAntiAlias(true);
                } catch (Exception e) {
                    Log.d("nullpointer", e.getMessage());
                }

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        parms = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        startwidth = parms.width;
                        startheight = parms.height;
                        dx = event.getRawX() - parms.leftMargin;
                        dy = event.getRawY() - parms.topMargin;
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            mode = ZOOM;
                        }

                        d = rotation(event);

                        break;
                    case MotionEvent.ACTION_UP:

                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {

                            x = event.getRawX();
                            y = event.getRawY();
//                            mCustomPhoto.animate().x(x-dx).y(y-dy);
//                            parms.leftMargin = (int) (x - dx);
//                            parms.topMargin = (int) (y - dy);
//
//                            parms.rightMargin = 0;
//                            parms.bottomMargin = 0;
//                            parms.rightMargin = parms.leftMargin + (parms.width);
//                            parms.bottomMargin = parms.topMargin + (parms.height);

//                            view.setLayoutParams(parms);
                            mCustomPhoto.setTranslationX(x - dx);
                            mCustomPhoto.setTranslationY(y - dy);

                        } else if (mode == ZOOM) {

                            if (event.getPointerCount() == 2) {

                                newRot = rotation(event);
                                float r = newRot - d;
                                angle = r;

                                x = event.getRawX();
                                y = event.getRawY();

                                float newDist = spacing(event);
                                if (newDist > 10f) {
                                    float scale = newDist / oldDist * view.getScaleX();
                                    if (scale > 0.6) {
                                        scalediff = scale;
                                        view.setScaleX(scale);
                                        view.setScaleY(scale);

                                    }
                                }

                                view.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();

                                x = event.getRawX();
                                y = event.getRawY();

                                parms.leftMargin = (int) ((x - dx) + scalediff);
                                parms.topMargin = (int) ((y - dy) + scalediff);

                                parms.rightMargin = 0;
                                parms.bottomMargin = 0;
                                parms.rightMargin = parms.leftMargin + (5 * parms.width);
                                parms.bottomMargin = parms.topMargin + (10 * parms.height);

                                view.setLayoutParams(parms);


                            }
                        }
                        break;
                }

                return true;

            }
        });
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void onBack(View view) {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    public void DoTakePhoto(View v){
        UpdateProfilePic();
    }
    private void UpdateProfilePic( ) {

        if ( MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin == false) {
            MessageAlert.showMessage(this, "请你登录");
            return;
        }

        selectImage();
    }
    private void selectImage() {
        final CharSequence[] items = {"拍照", "相册中选择",
                "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("拍照")) {
//                requestStoragePermission(true);
                myPermissions(true);
            } else if (items[item].equals("相册中选择")) {
//                requestStoragePermission(false);
                myPermissions(false);
            } else if (items[item].equals("取消")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity((Activity) this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    private boolean hasCamera = false;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permission ->{
                boolean allGranted = true;

                for (Boolean isGranted : permission.values()){
                    if (!isGranted){
                        allGranted = false;
                        break;
                    }
                }

                if (allGranted){
                    // All is granted
                    if (hasCamera) {
                        dispatchTakePictureIntent();
                    } else {
                        dispatchGalleryIntent();
                    }
                } else {
                    // All is not granted
                    showSettingsDialog();
                }
            });

    private void myPermissions(boolean isCamera) {

        hasCamera = isCamera;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            String[] permissions = new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA,
            };

            List<String> permissionsTORequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission((Activity) MyApplication.getInstance().getCurrentActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsTORequest.add(permission);
                }
            }

            if (permissionsTORequest.isEmpty()) {
                // All permissions are already granted
                if (isCamera) {
                    dispatchTakePictureIntent();
                } else {
                    dispatchGalleryIntent();
                }
            } else {
                String[] permissionsArray = permissionsTORequest.toArray(new String[0]);
                boolean shouldShowRationale = false;

                for (String permission : permissionsArray) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        shouldShowRationale = true;
                        break;
                    }
                }

                requestPermissionLauncher.launch(permissionsArray);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
            };

            List<String> permissionsTORequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission((Activity) MyApplication.getInstance().getCurrentActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsTORequest.add(permission);
                }
            }

            if (permissionsTORequest.isEmpty()) {
                // All permissions are already granted
//                Toast.makeText((Activity) root_view.getContext(), "All permissions are already granted", Toast.LENGTH_SHORT).show();
                if (isCamera) {
                    dispatchTakePictureIntent();
                } else {
                    dispatchGalleryIntent();
                }
            } else {
                String[] permissionsArray = permissionsTORequest.toArray(new String[0]);
                boolean shouldShowRationale = false;

                for (String permission : permissionsArray) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        shouldShowRationale = true;
                        break;
                    }
                }

                requestPermissionLauncher.launch(permissionsArray);
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_GALLERY_PHOTO) {
                if (data == null) {
                    //no data present
                    return;
                }

                Uri selectedFileUri = data.getData();
                String strSelectedPicPath = FilePath.getPath(this, selectedFileUri);

                if (strSelectedPicPath == null || strSelectedPicPath.equals("")) {
                    return;
                }
                mCustomPhoto.setImageURI(selectedFileUri);
            }
            else if(requestCode == REQUEST_TAKE_PHOTO)
            {

                String strSelectedPicPath = mPhotoFile.getPath();

                if (strSelectedPicPath == null || strSelectedPicPath.equals("")) {
                    return;
                }
                mCustomPhoto.setImageURI(Uri.fromFile(mPhotoFile));
            }
            findViewById(R.id.button29).setVisibility(View.GONE);
            findViewById(R.id.takenphotolayout).setVisibility(View.VISIBLE);
        }
    }
    public void DoDisableTakenLayout (View v)
    {
        findViewById(R.id.takenphotolayout).setVisibility(View.GONE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setSelectionBackground(int newItem) {

        View v = sharecustomSliderPager.getChildAt(currentPosition);
        v.findViewById(R.id.sharecustomimage).setBackground(getDrawable(R.drawable.sharecustomitembackground));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void clearSelectionBackground(int oldItem) {
        if (oldItem != currentPosition) {
            View v = sharecustomSliderPager.getChildAt(oldItem);
            v.findViewById(R.id.sharecustomimage).setBackgroundColor(Color.TRANSPARENT);
        }
    }
    private String getCurrentProgressText()
    {
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
        return currentProgress;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setBottomImage(ShareTypeItem item)
    {
        ((ImageView)findViewById(R.id.customphotobottom)).setImageDrawable(item.mBottomImage);
        if(item.idx == 0)
        {
            ((ImageView)findViewById(R.id.customphotobottom)).setVisibility(View.INVISIBLE);
            ((ConstraintLayout)findViewById(R.id.cardCL)).setVisibility(View.VISIBLE);
            ((ConstraintLayout)findViewById(R.id.cardly)).setVisibility(View.GONE);
            ((ImageView)findViewById(R.id.imageView24)).setVisibility(View.VISIBLE);
        } else {
            ((ImageView)findViewById(R.id.customphotobottom)).setVisibility(View.VISIBLE);
            Spannable spannable = new SpannableString(item.cardtvtxt2);
            spannable.setSpan(new ForegroundColorSpan(item.cardcolor), 15, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(item.cardcolor), 20, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((TextView)findViewById(R.id.sharecustomtext2)).setText(spannable, TextView.BufferType.SPANNABLE);
            ((ConstraintLayout)findViewById(R.id.cardCL)).setVisibility(View.GONE);
            ((ConstraintLayout)findViewById(R.id.cardly)).setVisibility(View.VISIBLE);
            ((ImageView)findViewById(R.id.imageView24)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.sharecustomtext1)).setText(item.cardtvtxt1);
        }

        switch (item.idx)
        {
            case 0:
                String cardtxt1 = "哥伦比亚大学专家团队研发·AI英语课程\n" +
                        "限时0元体验·让孩子轻松学会英语";

                Spannable spannable1 = new SpannableString(cardtxt1);
                spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB584E")), 13, 20 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB584E")), 22, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView)findViewById(R.id.textView40)).setText(spannable1, TextView.BufferType.SPANNABLE);

                ((TextView)findViewById(R.id.textView42custom)).setText(MyApplication.mNetProc.mLoginUserInf.mstrName);
                ((TextView)findViewById(R.id.textView46custom)).setText(getCurrentProgressText());
                ((TextView)findViewById(R.id.textView28custom)).setText(String.valueOf(MyApplication.mNetProc.mLoginUserInf.mnStudyDays));
                ((TextView)findViewById(R.id.textView35custom)).setText(String.valueOf(MyApplication.mNetProc.mLoginUserInf.mnQuizCount));
                ((TextView)findViewById(R.id.textView38custom)).setText(String.valueOf(MyApplication.mNetProc.mLoginUserInf.mnSharingCount));
                ((ImageView)findViewById(R.id.qrcode2)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_red_tranparent_5));
                applyQrCode(((ImageView)findViewById(R.id.qrcode2)));
                break;
            case 1:
                ((TextView)findViewById(R.id.sharecustomtext1)).setTextColor(Color.BLACK);
                ((TextView)findViewById(R.id.sharecustomtext2)).setTextColor(Color.BLACK);
                ((TextView)findViewById(R.id.sharecustombutton)).setTextColor(item.cardcolor);

                ((TextView)findViewById(R.id.sharecustombutton)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_pink_transparent));
                ((ImageView)findViewById(R.id.sharecustomqrcode)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_pink));
                applyQrCode(((ImageView)findViewById(R.id.sharecustomqrcode)));
                break;
            case 2 :
                ((TextView)findViewById(R.id.sharecustomtext1)).setTextColor(Color.WHITE);
                ((TextView)findViewById(R.id.sharecustomtext2)).setTextColor(Color.WHITE);
                ((TextView)findViewById(R.id.sharecustombutton)).setTextColor(item.cardcolor);
                ((TextView)findViewById(R.id.sharecustombutton)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_yello_dark_tranparent));
                ((ImageView)findViewById(R.id.sharecustomqrcode)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_yello_dark));
                applyQrCode(((ImageView)findViewById(R.id.sharecustomqrcode)));
                break;
            case 3:
                ((TextView)findViewById(R.id.sharecustomtext1)).setTextColor(Color.BLACK);
                ((TextView)findViewById(R.id.sharecustomtext2)).setTextColor(Color.BLACK);
                ((TextView)findViewById(R.id.sharecustombutton)).setTextColor(Color.parseColor("#333333"));
                ((TextView)findViewById(R.id.sharecustombutton)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_white_transparent));
                ((ImageView)findViewById(R.id.sharecustomqrcode)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_white));
                applyQrCode(((ImageView)findViewById(R.id.sharecustomqrcode)));
                break;
            case 4:
                ((TextView)findViewById(R.id.sharecustomtext1)).setTextColor(Color.BLACK);
                ((TextView)findViewById(R.id.sharecustomtext2)).setTextColor(Color.BLACK);
                ((TextView)findViewById(R.id.sharecustombutton)).setTextColor(item.cardcolor);
                ((TextView)findViewById(R.id.sharecustombutton)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_red_tranparent));
                ((ImageView)findViewById(R.id.sharecustomqrcode)).setBackground(MyApplication.getInstance().getCurrentActivity().getDrawable(R.drawable.share_outline_red));
                applyQrCode(((ImageView)findViewById(R.id.sharecustomqrcode)));
                break;
        }

    }

    private void applyQrCode(ImageView view)
    {
        Bitmap qrCodeBitmap = null;

        String strUrl = MyApplication.getInstance().SERVER_URL + "Code/sharedCode?p=" + MyApplication.mNetProc.mLoginUserInf.mnbase64UserID + "&w=1";
        int qrCodeWidth = 200;
        if(MyApplication.getInstance().isTablet)
            qrCodeWidth = 200;
        else
            qrCodeWidth = 230;

        qrCodeBitmap = QRGenerater.QRGenerater(strUrl, qrCodeWidth, qrCodeWidth);

        view.setImageBitmap(qrCodeBitmap);
    }
    private Bitmap makeImage ()
    {
        View linearview = releativeLayout;
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

    private String saveImage(Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, String.valueOf(System.currentTimeMillis()), null);
        return path;
//        String imagePath = MediaStore.Images.Media.insertImage(
//                getContentResolver(),
//                bitmap,
//                "usacamp_shareImg",
//                "usacamp_shareImg"
//        );
    }
}