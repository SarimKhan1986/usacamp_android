package com.usacamp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.activities.ActCenterActivity;
import com.usacamp.activities.CustomServiceActivity;
import com.usacamp.activities.LearningRecordActivity;
import com.usacamp.activities.ModifyChildProfileActivity;
import com.usacamp.activities.MyApplication;
import com.usacamp.activities.MyPointActivity;
import com.usacamp.activities.PresentWebView;
import com.usacamp.activities.PurchaseCampActivity;
import com.usacamp.activities.ReportActivity;
import com.usacamp.activities.SettingActivity;
import com.usacamp.activities.payhistory;
import com.usacamp.constants.Constants;
import com.usacamp.utils.Age;
import com.usacamp.utils.FileCompressor;
import com.usacamp.utils.FilePath;
import com.usacamp.utils.MessageAlert;
import com.usacamp.utils.MyPointItem;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
//import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final int PICK_FILE_REQUEST = 1;
    private final String mstrSelectedPicPath = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View root_view;
    private ArrayList<MyPointItem> marrMyPoint;
    private OnFragmentInteractionListener mListener;
    private ImageView presentLy;
    private int currentNumPresent = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile;
    FileCompressor mCompressor;

    public FragmentProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {}
    public static Object newInstance() {
        FragmentProfile fragment = new FragmentProfile();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_profile, null);
        //ButterKnife.bind(this.root_view);
        mCompressor = new FileCompressor(this.root_view.getContext());

        root_view.findViewById(R.id.btn_modify_child).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                Intent modifychildActivity = new Intent(root_view.getContext(), ModifyChildProfileActivity.class);
                startActivity(modifychildActivity);
            }
        });
        root_view.findViewById(R.id.llayout_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                Intent settingActivity = new Intent(root_view.getContext(), SettingActivity.class);
                startActivity(settingActivity);

                //Zhuge

                //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Settings);
            }
        });
        root_view.findViewById(R.id.llayout_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id+
                        "&kind=0";

                MyApplication.mNetProc.getShareInfo("getShareInfo", strRequestParameter);
                //Zhuge
                //定义与事件相关的属性信息
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_Welfare_Classification_Share);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Welfare,  eventObject);
            }
        });
        root_view.findViewById(R.id.llayout_actcenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                Intent actcenterActivity = new Intent(root_view.getContext(), ActCenterActivity.class);
                startActivity(actcenterActivity );

                //Zhuge
                //定义与事件相关的属性信息
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_Welfare_Classification_ActivityCenter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Welfare,  eventObject);
            }
        });
        root_view.findViewById(R.id.llayout_mypoint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                Intent mypointActivity = new Intent(root_view.getContext(), MyPointActivity.class);
                startActivity(mypointActivity );

                //Zhuge
                //定义与事件相关的属性信息
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_MyAccount_Classification_Point);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_MyAccount,  eventObject);
            }
        });
        root_view.findViewById(R.id.llayout_customservice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                Intent customserviceActivity = new Intent(root_view.getContext(), CustomServiceActivity.class);
                startActivity(customserviceActivity );
                //Zhuge
                //定义与事件相关的属性信息
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_Attentiveservice_Classification_Customer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Attentive,  eventObject);
            }
        });
        root_view.findViewById(R.id.llayout_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                Intent reportActivity = new Intent(root_view.getContext(), ReportActivity.class);
                reportActivity.putExtra("type", 0);
                startActivity(reportActivity);

                //Zhuge
                //定义与事件相关的属性信息
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_Attentiveservice_Classification_FeedBack);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Attentive,  eventObject);
            }
        });
        root_view.findViewById(R.id.llayout_learningrecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    MessageAlert.showMessage(getActivity(), "请你登录");
                    return;
                }
                Intent learningrecordActivity = new Intent(root_view.getContext(), LearningRecordActivity.class);
                startActivity(learningrecordActivity);

                //Zhuge
                //定义与事件相关的属性信息
                JSONObject eventObject = new JSONObject();
                try {
                    eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_MyAccount_Classification_Learning_Record);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_MyAccount,  eventObject);
            }
        });
        root_view.findViewById(R.id.llypointstore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {

                    IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), MyApplication.getInstance().WECHAT_APPID);

                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = Constants.wechat_miniapp_id; // 填小程序原始id
                    //req.path = "";                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                    api.sendReq(req);

                    //Zhuge
                    //定义与事件相关的属性信息
                    JSONObject eventObject = new JSONObject();
                    try {
                        eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_Welfare_Classification_PointMall);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Welfare,  eventObject);

                    //Zhuge

                    //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Enter_Profile_PointMall);
                }
            }
        });
        root_view.findViewById(R.id.llypayhistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
                    Intent payHistoryInt = new Intent(root_view.getContext(), payhistory.class);
                    startActivity(payHistoryInt);

                    //Zhuge
                    //定义与事件相关的属性信息
                    JSONObject eventObject = new JSONObject();
                    try {
                        eventObject.put(Constants.Zhuge_Property_Classification, Constants.Zhuge_Value_MyAccount_Classification_Payment_History);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_MyAccount,  eventObject);
                    return;
                }
            }
        });

        root_view.findViewById(R.id.scircularprofilepic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UpdateProfilePic();
            }
        });

        //Zhuge
        //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Enter_Profile_Tab);
        return root_view;
    }


    private void UpdateProfilePic( ) {

        if ( MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin == false) {
                MessageAlert.showMessage(getActivity(), "请你登录");
            return;
        }

        selectImage();
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
                String strSelectedPicPath = FilePath.getPath(getActivity(), selectedFileUri);

                if (strSelectedPicPath == null || strSelectedPicPath.equals("")) {
                    return;
                }

                String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id;
                 ArrayList<String> listPicUpload = new ArrayList<String>();
                listPicUpload.add(strSelectedPicPath);

                MyApplication.mNetProc.picUpload("picUpload", strRequestParameter, listPicUpload);
            }
            else if(requestCode == REQUEST_TAKE_PHOTO)
            {

                String strSelectedPicPath = mPhotoFile.getPath();

                if (strSelectedPicPath == null || strSelectedPicPath.equals("")) {
                    return;
                }

                String strRequestParameter = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id;
                ArrayList<String> listPicUpload = new ArrayList<String>();
                listPicUpload.add(strSelectedPicPath);

                MyApplication.mNetProc.picUpload("picUpload", strRequestParameter, listPicUpload);
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAllInformation();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void refreshStudyDegree()
    {
        ((TextView)(root_view.findViewById(R.id.txt_class))).setText(MyApplication.mNetProc.mLoginUserInf.mstrStudydgree + "级");
    }
    public void refreshAllInformation()
    {
        if (MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin){
            refreshStudyDegree();
            ((TextView)(root_view.findViewById(R.id.txt_profilename))).setText(MyApplication.mNetProc.mLoginUserInf.mstrName);
            if(MyApplication.mNetProc.mLoginUserInf.mstrBirthday.equals("") || MyApplication.mNetProc.mLoginUserInf.mstrBirthday.equals("0000-00-00")) {
                ((TextView) (root_view.findViewById(R.id.txt_profilebirthday))).setVisibility(View.GONE);

            } else {

                ((TextView) (root_view.findViewById(R.id.txt_profilebirthday))).setText(Age.calculateAge());
                ((TextView) (root_view.findViewById(R.id.txt_profilebirthday))).setVisibility(View.VISIBLE);
            }
            if(MyApplication.mNetProc.mLoginUserInf.muserType == 3 || MyApplication.mNetProc.mLoginUserInf.muserType == 4 || MyApplication.mNetProc.mLoginUserInf.muserType == 6 || MyApplication.mNetProc.mLoginUserInf.muserType == 7)
                ((TextView)root_view.findViewById(R.id.txt_profile_leftDay)).setText("永久");
            else
                ((TextView)root_view.findViewById(R.id.txt_profile_leftDay)).setText(getExpireDays() +"天");
            refreshUserPic();

            presentLy = (ImageView) root_view.findViewById(R.id.presentrt);

            presentLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (MyApplication.mNetProc.mLoginUserInf.muserType2)
                    {
                        case 1://binded
                            Intent PresentbindInt = new Intent(root_view.getContext(), PresentWebView.class);
                            PresentbindInt.putExtra("webcontent", MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(currentNumPresent).link);
                            startActivity(PresentbindInt);

                            //Zhuge
                            //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Banner_GetIt);
                            break;
                        case 2://nonbinded
                            Intent PresentunbindInt = new Intent(root_view.getContext(), PresentWebView.class);
                            PresentunbindInt.putExtra("webcontent", MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(currentNumPresent).link);
                            startActivity(PresentunbindInt);

                            //Zhuge
                            //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Banner_Follow);
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5://nonpuchased
                            Intent buyInt = new Intent(root_view.getContext(), PurchaseCampActivity.class);
                            buyInt.putExtra(Constants.Zhuge_Property_Entrance, Constants.Zhuge_Value_Entry_Purchase_Profile);
                            startActivity(buyInt);

                            //Zhuge
                            //ZhugeSDK.getInstance().track(root_view.getContext(), Constants.Zhuge_Event_Click_Profile_Banner_Purchase);
                            break;
                    default:

                }
                }
            });
            switch (MyApplication.mNetProc.mLoginUserInf.muserType2)
            {
                case 1://binded
                    currentNumPresent = 1;
                    for(int ii = 0 ;ii < MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.size() ; ii++)
                    {
                        if(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(ii).type == 1)
                        {
                            currentNumPresent = ii;
                            if(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(ii).status == 0)
                            {
                                presentLy.setImageDrawable(getResources().getDrawable(R.drawable.profilebanner1));
//                                preTxt.setText(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(ii).title);
//                                prebtn.setText(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(ii).buttontxt);
                                presentLy.setVisibility(View.VISIBLE);
                            } else {
                                presentLy.setVisibility(View.GONE);
                            }
                        }
                    }
                    break;
                case 2://nonbinded

                    for(int jj = 0 ;jj < MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.size() ; jj++)
                    {
                        if(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(jj).type == 2)
                        {
                            currentNumPresent = jj;
                            if(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(jj).status == 0)
                            {
                                presentLy.setImageDrawable(getResources().getDrawable(R.drawable.profilebanner3));
                                presentLy.setVisibility(View.VISIBLE);
//                                preTxt.setText(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(jj).title);
//                                prebtn.setText(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(jj).buttontxt);
                            } else {
                                presentLy.setVisibility(View.GONE);
                            }
                        }
                    }
                    break;
                case 3:
                    presentLy.setVisibility(View.GONE);
                    break;
                case 4:
                    presentLy.setVisibility(View.GONE);
                    break;
                case 5://nonpuchased

                    for(int j = 0 ;j < MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.size() ; j++)
                    {
                        if(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(j).type == 0)
                        {
                            currentNumPresent = j;
                            if(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(j).status == 0)
                            {
                                presentLy.setImageDrawable(getResources().getDrawable(R.drawable.profilebanner2));
                                presentLy.setVisibility(View.VISIBLE);
//                                preTxt.setText(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(j).title);
//                                prebtn.setText(MyApplication.mNetProc.mLoginUserInf.mlistProfileBannerItems.get(j).buttontxt);
                            } else {
                                presentLy.setVisibility(View.GONE);
                            }
                        }
                    }
                    break;
                default:

            }
        }
        else{
            ((TextView)(root_view.findViewById(R.id.txt_profilename))).setText("");
            ((TextView)(root_view.findViewById(R.id.txt_profilebirthday))).setText("");
        }
    }
    private int getExpireDays() {
        int expireDays = 0;
        if (MyApplication.mNetProc.mLoginUserInf.muserType == 3 || MyApplication.mNetProc.mLoginUserInf.muserType == 4) {
            expireDays = 0;
            return expireDays;
        }

        SimpleDateFormat createdDate = new SimpleDateFormat("yyyy-MM-dd");
        if(MyApplication.mNetProc.mLoginUserInf.mlistProgress.size() != 0) {
            Calendar calendar = Calendar.getInstance();
            try {
                ArrayList<Integer> arrayDayList = new ArrayList<Integer>();
                for(int i = 0 ; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size() ; i++) {
                    int expireDays_tmp = 0;
                    Date purchasedDate = createdDate.parse(MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mstrEnd);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(purchasedDate);
                    calendar3.add(Calendar.DAY_OF_YEAR, 1);
                    Date paidTime = calendar3.getTime();
                    long diff = paidTime.getTime() - calendar.getTime().getTime();
                    expireDays_tmp = (int) (diff / (24 * 60 * 60 * 1000));
                    arrayDayList.add(expireDays_tmp);
                }
                expireDays = Collections.max(arrayDayList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (expireDays < 0)
            expireDays = 0;

        return expireDays;
    }


    public void refreshUserPic() {
        if (MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin && !MyApplication.mNetProc.mLoginUserInf.mstrPic.equals("")) {
            Uri uri= Uri.parse(MyApplication.mNetProc.mLoginUserInf.mstrPic);
            RequestCreator reqCreator = Picasso.with(root_view.getContext()).load(uri);
            reqCreator.into( (CircularImageView) (root_view.findViewById(R.id.scircularprofilepic)));

        } else {
            ((CircularImageView)(root_view.findViewById(R.id.scircularprofilepic))).setImageDrawable(getResources().getDrawable(R.drawable.lucy_girl));
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"拍照", "相册中选择",
                "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.root_view.getContext());
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

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(root_view.getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(root_view.getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
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

    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity((Activity) root_view.getContext()).withPermissions(Constants.permissions())
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
                }).withErrorListener(error -> Toast.makeText(root_view.getContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
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
                if (ContextCompat.checkSelfPermission((Activity) root_view.getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
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
                if (ContextCompat.checkSelfPermission((Activity) root_view.getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
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
            /**
             * Showing Alert Dialog with Settings option
             * Navigates user to app settings
             * NOTE: Keep proper title and message depending on your app
             */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(root_view.getContext());
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
        Uri uri = Uri.fromParts("package", root_view.getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Create file with current timestamp name
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = root_view.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }




}
