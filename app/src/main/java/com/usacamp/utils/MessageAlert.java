    package com.usacamp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.usacamp.R;
import com.usacamp.activities.MyApplication;
import com.usacamp.activities.SpeakingActivity;
import com.usacamp.activities.VideoActivity;
import com.usacamp.constants.State;

    public class MessageAlert {
    private static Toast mToastMessage = null;
    public static Dialog mDlgProgress = null;
    private static Dialog mDlgfirstLogin = null;
    private static Dialog mDlgNetworkConnection = null;

    public static void showMessage(Activity activity, String strMessage){
        if (mToastMessage != null){
            mToastMessage.cancel();
            mToastMessage = null;
        }

        View layoutMessage = activity.getLayoutInflater().inflate(R.layout.layout_message_dlg, (ViewGroup)activity.findViewById(R.id.layout_message_root));
        TextView text = layoutMessage.findViewById(R.id.text_message);
        text.setText(strMessage);

        mToastMessage = new Toast(activity);
        mToastMessage.setGravity(Gravity.CENTER, 0, 0);
        mToastMessage.setDuration(Toast.LENGTH_SHORT);
        mToastMessage.setView(layoutMessage);
        mToastMessage.show();
    }

    public static Dialog showFirstDlg(Activity activity)
    {
        if (mDlgfirstLogin != null){
            mDlgfirstLogin.dismiss();
            mDlgfirstLogin = null;
        }

        android.app.AlertDialog.Builder keyBuilder = new android.app.AlertDialog.Builder(activity);
        final View lessonView = LayoutInflater.from(activity).inflate(R.layout.firstlogindlg, null);
        keyBuilder.setView(lessonView);
        android.app.AlertDialog dialog = keyBuilder.create();
        lessonView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lessonView.findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent videoAct =  new Intent(activity, VideoActivity.class);
                int num = 0;
                String linkvideo = "";
                for(int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistMedia.size() ; i ++ )
                {
                    if(MyApplication.mNetProc.mLoginUserInf.mlistMedia.get(i).mIsMain == 1) {
                        num++;
                        if(num == 2)
                            linkvideo = MyApplication.mNetProc.mLoginUserInf.mlistMedia.get(i).mstrVideoFile;
                    }
                }
                videoAct.putExtra("video_path",linkvideo);
                activity.startActivity(videoAct);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return mDlgfirstLogin;
    }
    public static void showMessage(Activity activity, boolean fTrue, String strMessage){
        if (mToastMessage != null){
            mToastMessage.cancel();
            mToastMessage = null;
        }

        View layoutMessage = activity.getLayoutInflater().inflate(R.layout.layout_image_notice, (ViewGroup)activity.findViewById(R.id.layout_notice_root));
        ImageView img = (ImageView)layoutMessage.findViewById(R.id.image_notice);
        if (fTrue)
            img.setImageResource(R.drawable.check_mark);
        else
            img.setImageResource(R.drawable.cross_marker);
        TextView text = (TextView) layoutMessage.findViewById(R.id.text_notice_type);
        text.setText(strMessage);
        mToastMessage = new Toast(activity);
        mToastMessage.setGravity(Gravity.CENTER, 0, 0);
        mToastMessage.setDuration(Toast.LENGTH_LONG);
        mToastMessage.setView(layoutMessage);
        mToastMessage.show();
    }
    public static void cancelMessage(){
        if (mToastMessage != null){
            mToastMessage.cancel();
            mToastMessage = null;
        }
    }
        public Dialog m_progressDlg = null;
    public static Dialog showProgressDialog(Activity activity, String strMessage) {

        if (mDlgProgress != null){
            mDlgProgress.dismiss();
            mDlgProgress = null;
        }

        mDlgProgress = new Dialog(activity);
        mDlgProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDlgProgress.setCanceledOnTouchOutside(false);
        View layoutProgress = activity.getLayoutInflater().inflate(R.layout.layout_progress_dlg, null);
        TextView text = layoutProgress.findViewById(R.id.text_progress_type);
        text.setText(strMessage);
        mDlgProgress.setContentView(layoutProgress);

        mDlgProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDlgProgress.show();

        return mDlgProgress;
    }
    public static boolean isConnectionSetting = false;
    public static void showAlertDlg(Activity activity) {
        if (mDlgNetworkConnection != null){
            mDlgNetworkConnection.dismiss();
            mDlgNetworkConnection = null;
        }

        android.app.AlertDialog.Builder keyBuilder = new android.app.AlertDialog.Builder(activity);
        final View lessonView = LayoutInflater.from(activity).inflate(R.layout.networkconnectiondlg, null);
        if(isConnectionSetting)
        {
            ((TextView)lessonView.findViewById(R.id.connectretrybtn)).setText(R.string.connectionnetsettingopen);
            ((TextView)lessonView.findViewById(R.id.connecttiletxt)).setText(R.string.connectionnetconfigtitle1);
            ((TextView)lessonView.findViewById(R.id.networktypetxt)).setVisibility(View.VISIBLE);
            switch (MyApplication.getInstance().networkState) {
                case 0:
                    ((TextView)lessonView.findViewById(R.id.networktypetxt)).setText(R.string.connectionnetconfigwifi);
                    break;
                case 1:
                    ((TextView)lessonView.findViewById(R.id.networktypetxt)).setText(R.string.connectionnetconfigwifi);
                    break;
                case 2:
                    ((TextView)lessonView.findViewById(R.id.networktypetxt)).setText(R.string.connectioncurrentnetmobiledata);
                    break;

                default:
                    break;
            }
        } else {
            ((TextView)lessonView.findViewById(R.id.connectretrybtn)).setText(R.string.connectrtetry);
            ((TextView)lessonView.findViewById(R.id.connecttiletxt)).setText(R.string.connectionnetconfigtitle);
            ((TextView)lessonView.findViewById(R.id.networktypetxt)).setVisibility(View.GONE);


        }
        keyBuilder.setView(lessonView);
        android.app.AlertDialog dialog = keyBuilder.create();
        lessonView.findViewById(R.id.connectretrybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnectionSetting) {
                    isConnectionSetting = true;
                    MessageAlert.showProgressDialog(MyApplication.getInstance().getCurrentActivity(), "连接中，请等待");
                    MyApplication.getInstance().reCallAppData();
                } else {
                    isConnectionSetting = false;
                    switch (MyApplication.getInstance().networkState) {
                        case 0:
                        case 1:
                            MyApplication.getInstance().getCurrentActivity().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            break;
                        case 2:
                            MyApplication.getInstance().getCurrentActivity().startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                            break;
                    }

                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


}
