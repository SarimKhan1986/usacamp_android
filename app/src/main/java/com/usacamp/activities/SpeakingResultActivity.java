package com.usacamp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.constants.State;
import com.usacamp.utils.AdapterGridSectioned;
import com.usacamp.utils.MakeJsonForSpeaking;
import com.usacamp.utils.Sentence;
import com.usacamp.utils.SpeakingResult;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class SpeakingResultActivity extends BaseActivity {
    SpeakingResult result;
    TextView speakingResultTitleTxt, speakingResultNameTxt,speakingResultReadCountTxt, speakingResultHighScoreCountTxt;
    ListView speakingResultList;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_result);
        speakingResultTitleTxt = (TextView) findViewById(R.id.speaking_result_title);
        speakingResultNameTxt = (TextView) findViewById(R.id.speaking_result_name);
        speakingResultReadCountTxt = (TextView) findViewById(R.id.speaking_result_complete_count);
        speakingResultHighScoreCountTxt = (TextView) findViewById(R.id.speaking_result_incomplete_count);
        speakingResultList = (ListView) findViewById(R.id.speaking_result_list) ;
        result = getIntent().getExtras().getParcelable("Speaking_Result");
        for(int i = 0 ; i < result.result.size() ; i++)
        {
            Log.d("testpeakijng", result.result.get(i).sound_path);
        }
        ((Button)findViewById(R.id.speaking_result_share_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp != null ) {
                    mp.stop();
                    mp = null;
                }
                Intent speakingShareInt = new Intent(SpeakingResultActivity.this, SpeakingShareActivity.class);
                speakingShareInt.putExtra("SpeakingShareLevelId", result.level_id);
                speakingShareInt.putExtra("SpeakingShareLessonId", result.lesson_id);
                speakingShareInt.putExtra("SpeakingSharePartId", result.part_id);
                speakingShareInt.putExtra("SpeakingShareReadCount", result.readCnt);
                speakingShareInt.putExtra("SpeakingShareHighCount", result.starCnt);
                startActivity(speakingShareInt);
            }
        });
        setResult(result);
    }

    public void onBack(View view) {
        if(mp != null ) {
            mp.stop();
            mp = null;
        }
        finish();
    }
    void setResult(@NotNull SpeakingResult value)
    {
        String partAB ;
        if(value.part_id == 1)
            partAB = "A";
        else
            partAB = "B";
        speakingResultTitleTxt.setText("Camp" + value.level_id + "-" + value.lesson_id + "-" + partAB);
        speakingResultNameTxt.setText(MyApplication.mNetProc.mLoginUserInf.mstrName);
        speakingResultReadCountTxt.setText(String.valueOf(value.readCnt));
        speakingResultHighScoreCountTxt.setText(String.valueOf(value.starCnt));

        SpeakingResultListAdapter listAdapter = new SpeakingResultListAdapter(this, value.result);
        speakingResultList.setAdapter(listAdapter);
        speakingResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public class SpeakingResultListAdapter extends BaseAdapter {
        //list view item
        List<Sentence> speakingResultItemList = new ArrayList<>();
        LayoutInflater mInflater;

        public SpeakingResultListAdapter(Context context, List<Sentence> arrSrc){
            speakingResultItemList = arrSrc;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return speakingResultItemList.size();
        }

        @Override
        public String getItem(int n) {
            return "";
        }
        @Override
        public long getItemId(int n) {
            return n;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int n, View view, ViewGroup viewGroup) {
            Sentence rowItem = speakingResultItemList.get(n);
            if (view == null){
                view = mInflater.inflate(R.layout.layout_speaking_listview, viewGroup, false);
            }

            TextView sentenceTxt;
            TextView durationBtn;
            ImageView star1, star2, star3;
            star1 = (ImageView) view.findViewById(R.id.speaking_result_star1);
            star2 = (ImageView) view.findViewById(R.id.speaking_result_star2);
            star3 = (ImageView) view.findViewById(R.id.speaking_result_star3);
            sentenceTxt = (TextView) view.findViewById(R.id.speaking_result_sentence);
            durationBtn = (TextView) view.findViewById(R.id.speaking_result_sound_btn);
            durationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!rowItem.sound_path.equals("")) {
                        if(mp == null)
                            mp = new MediaPlayer();
                        mp.reset();
                        try {
                            mp.setDataSource(rowItem.sound_path);
                            mp.prepare();
                            mp.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            LinearLayout itemLy = (LinearLayout) view.findViewById(R.id.speaking_result_item);
            itemLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("transerfertest", "oncreate000");

//                    if(mp != null ) {
//                        mp.stop();
//                        mp = null;
//                    }
//                    State.setSpeakingState(State.speaking_state.history);
//                    if(!MyApplication.getInstance().isRunningActivity("SpeakingActivity")) {
//                        Log.d("transerfertest", "oncreate");
//                        Intent speakingAct = new Intent(SpeakingResultActivity.this, SpeakingActivity.class);
//                        speakingAct.putExtra("Skip_Position", n);
//                        speakingAct.putExtra("Read_Sentence", result);
//                        startActivity(speakingAct);
//                    } else {
//                        Log.d("transerfertest", "broadcast");
//                        Intent t= new Intent();
//                        t.setAction(Constants.SPEAKING_HISTORY_CASE);
//                        t.putExtra("Skip_Position", n);
//                        //t.putExtra("Read_Sentence", result);
//                        sendBroadcast(t);
//
//                    }
//                    finish();
                }
            });
            sentenceTxt.setText((n + 1) + "." + rowItem.sentence);
            if(!rowItem.sound_path.equals("")) {
                durationBtn.setText(String.valueOf(rowItem.sound_duration));
                durationBtn.setBackground(getResources().getDrawable(R.drawable.green_dark_radius_outline1_nopadding));
                durationBtn.setTextColor(Color.parseColor("#1D9F38"));
                durationBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.speakericon), null,null,null);
                int durationBtnWidth = 50 * rowItem.sound_duration;
                if(durationBtnWidth == 0)
                    durationBtnWidth = 50;
                durationBtn.setWidth(durationBtnWidth);
                switch (rowItem.star) {
                    case 0:
                        star1.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        star2.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        star3.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        break;
                    case 1:
                        star1.setBackground(getResources().getDrawable(R.drawable.enablestar));
                        star2.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        star3.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        break;
                    case 2:
                        star1.setBackground(getResources().getDrawable(R.drawable.enablestar));
                        star2.setBackground(getResources().getDrawable(R.drawable.enablestar));
                        star3.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        break;
                    case 3:
                        star1.setBackground(getResources().getDrawable(R.drawable.enablestar));
                        star2.setBackground(getResources().getDrawable(R.drawable.enablestar));
                        star3.setBackground(getResources().getDrawable(R.drawable.enablestar));
                        break;
                    default:
                        star1.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        star2.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        star3.setBackground(getResources().getDrawable(R.drawable.disablestar));
                        break;
                }
            } else {
                durationBtn.setBackground(getResources().getDrawable(R.color.transparent));
                durationBtn.setText("未完成");
                durationBtn.setTextColor(Color.GRAY);
                durationBtn.setCompoundDrawables(null, null,null,null);
                star1.setBackground(getResources().getDrawable(R.drawable.disablestar));
                star2.setBackground(getResources().getDrawable(R.drawable.disablestar));
                star3.setBackground(getResources().getDrawable(R.drawable.disablestar));
            }

            return view;
        }
    }

}