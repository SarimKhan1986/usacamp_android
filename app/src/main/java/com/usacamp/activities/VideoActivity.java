package com.usacamp.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.usacamp.R;
import com.usacamp.constants.Constants;


import org.json.JSONException;
import org.json.JSONObject;

public  class  VideoActivity extends BaseActivity implements SensorEventListener{

    public Uri muriVideoPath;
    public  VideoView mvidView = null;
   public  ImageView mVideoCloseImg;
    private final OrientationEventListener mListenerOrientChange = null;
    private final int    mOrientation = -1;
    SensorManager mSm;
    private Sensor mSensor;
    public final Runnable mGoneRun = new Runnable() {
        @Override
        public void run() {
            mVideoCloseImg.setVisibility(View.GONE);
        }
    } ;
    private ProgressDialog pd;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mvidView = (VideoView) findViewById(R.id.video_view);
        mVideoCloseImg = (ImageView) findViewById(R.id.img_video_close);
        mVideoCloseImg.setBackground(new ColorDrawable(Color.TRANSPARENT));

        String strVideoPath = getIntent().getStringExtra("video_path");
        if (strVideoPath==null) {
            // Alex
           // MessageAlert.showMessage(this, "不能看");
           // finish();
        }else {
            {
                muriVideoPath = Uri.parse(strVideoPath.trim());
                Log.d("videopath", muriVideoPath.toString());
                final MediaController controller = new MediaController(this);
                controller.setEnabled(true);
                pd = new ProgressDialog(this);
                pd.setMessage("Buffering video please wait...");
                pd.show();

                controller.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if ( motionEvent.getAction() == MotionEvent.ACTION_UP ) {
                            mVideoCloseImg.removeCallbacks(mGoneRun);
                            mVideoCloseImg.postDelayed(mGoneRun, 3000);
                          }
                        return VideoActivity.super.onTouchEvent(motionEvent);
                    }
                });
                controller.setAnchorView(mvidView);
                controller.setBackgroundColor(Color.WHITE);
                mvidView.setVideoURI(muriVideoPath);
                mvidView.setMediaController(controller);
                mvidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        finish();
                    }
                });

                mvidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        pd.dismiss();
                        controller.show();
                        mVideoCloseImg.setVisibility(View.VISIBLE);
                        mVideoCloseImg.postDelayed(mGoneRun, 3000);
                    }
                });

                mvidView.start();

            }
            ////ZhugeSDK.getInstance().startTrack(Constants.Zhuge_Event_Enter_Main_Student_ListVideo);
        }

        mVideoCloseImg.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                mVideoCloseImg.removeCallbacks( mGoneRun );
                finish ();
                //Zhuge
                JSONObject eventObject = new JSONObject();

                try {
                    eventObject.put(Constants.Zhuge_Property_Name, Constants.Zhuge_Value_Student_Style);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ////ZhugeSDK.getInstance().endTrack(Constants.Zhuge_Event_Enter_Main_Student_ListVideo, eventObject);
            }
        } );

        mSm = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    protected void onResume() {
        super.onResume();

       ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
        mSm.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( event.getAction() == MotionEvent.ACTION_UP ) {
            if (mVideoCloseImg.isShown()) {
                mVideoCloseImg.setVisibility(View.GONE);
                mVideoCloseImg.removeCallbacks( mGoneRun );
            }
            else {
                mVideoCloseImg.setVisibility(View.VISIBLE);
                mVideoCloseImg.postDelayed( mGoneRun, 3000);
            }
        }

       return super.onTouchEvent ( event );
    }
    private final float[] mRotationMatrix = new float[16];

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            // is interpreted by Open GL as the inverse of the
            // rotation-vector, which is what we want.
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix , sensorEvent.values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
