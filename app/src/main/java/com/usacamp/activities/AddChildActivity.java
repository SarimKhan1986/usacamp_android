package com.usacamp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.usacamp.R;
import com.usacamp.utils.MessageAlert;

import java.util.Calendar;

public class AddChildActivity extends BaseActivity {

    public ToggleButton mAddChildBoyTbtn;
    public ToggleButton mAddChildGirlTbtn;
    //public EditText mAddChildNameEdt;
    public EditText mAddChildNickNameEdt;
    public TextView mAddChildBirthdayEdt ;
    public Boolean mbGender = true;
    public Button mAddChildBtn ;
    public String mstrBirthday = "";
    public String mstrNickName = "";
    public String mstrName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        mAddChildBoyTbtn = (ToggleButton)findViewById(R.id.tbtn_addchild_boy);
        mAddChildGirlTbtn = (ToggleButton)findViewById(R.id.tbtn_addchild_girl);
        mAddChildNickNameEdt = (EditText)findViewById(R.id.edt_addchild_nickname);
        mAddChildBirthdayEdt = (TextView)findViewById(R.id.edt_addchild_birthday);
        mbGender = true;
        mAddChildBtn = (Button)findViewById(R.id.btn_addchild_increase);
        ((RadioGroup) findViewById(R.id.toggleGroup)).setOnCheckedChangeListener(ToggleListener);
        mAddChildNickNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(ValidButton()) {
                    mAddChildBtn.setEnabled(true);
                    mAddChildBtn.setBackground(getResources().getDrawable(R.drawable.green_blue_radius));
                } else {
                    mAddChildBtn.setEnabled(false);
                    mAddChildBtn.setBackground(getResources().getDrawable(R.drawable.gray_radius_aurora));
                }
            }
        });
        mAddChildBirthdayEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(ValidButton()) {
                    mAddChildBtn.setEnabled(true);
                    mAddChildBtn.setBackground(getResources().getDrawable(R.drawable.green_blue_radius));
                } else {
                    mAddChildBtn.setEnabled(false);
                    mAddChildBtn.setBackground(getResources().getDrawable(R.drawable.gray_radius_aurora));
                }
            }
        });
        mAddChildBirthdayEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int nyear = c.get(Calendar.YEAR);
                int nmonth = c.get(Calendar.MONTH);
                int nday = c.get(Calendar.DATE);
                DatePickerDialog dpDlg = new DatePickerDialog(AddChildActivity.this , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,onDatePicked , nyear , nmonth , nday);
                dpDlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpDlg.show();
            }
        });
//        mAddChildBoyTbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                mbGender = true;
//               // togglebackchange(mbGender);
//            }
//        });
//        mAddChildGirlTbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                mbGender = false;
//                //togglebackchange(mbGender);
//            }
//        });

    }
    static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);


            }
        }
    };
    public void DoSkipStep(View v)
    {
        finish();
        Intent mainInt = new Intent(this, MainActivity.class);
        startActivity(mainInt);
        MainActivity.mainActivityInstance.goFragmentHome();
    }
    private void togglebackchange (boolean bGender)
    {
        if (bGender){
            mAddChildBoyTbtn.setBackgroundResource(R.drawable.btn_outline_green);
            mAddChildBoyTbtn.setTextColor(Color.WHITE);
            mAddChildGirlTbtn.setBackgroundResource(R.drawable.btn_outline_black);
            mAddChildGirlTbtn.setTextColor(Color.parseColor("#303030"));
        }
        else
        {
            mAddChildGirlTbtn.setBackgroundResource(R.drawable.btn_outline_green);
            mAddChildGirlTbtn.setTextColor(Color.WHITE);
            mAddChildBoyTbtn.setBackgroundResource(R.drawable.btn_outline_black);
            mAddChildBoyTbtn.setTextColor(Color.parseColor("#303030"));
        }
    }
    public void onBack(View view) {
        finish();
    }
    public void gotoprivacy(View v)
    {
        Intent privacyInt = new Intent(this, PrivacyAct.class);
        startActivity(privacyInt);
    }

    public void gotoagreement(View v)
    {
        Intent agreementInt = new Intent(this, AgreementAct.class);
        startActivity(agreementInt);
    }
    //set Profile
    public void onAddChild(View view) {

        String mstrrequest = null;
        int gender = mbGender ? 0 : 1;
        mstrName = mAddChildNickNameEdt.getText().toString();
        mstrrequest = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id + "&name=" + mstrName +"&nickname="+ mstrNickName + "&gender="+ gender +"&birthday="+mstrBirthday;
        MyApplication.mNetProc.setProfile("setProfile", mstrrequest);

    }

    DatePickerDialog.OnDateSetListener onDatePicked = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            i1 += 1;
            mstrBirthday = i + "-" + i1 + "-" + i2;
            String strMsg = i + "年" + i1 + "月" + i2 + "日";
            mAddChildBirthdayEdt.setText(strMsg);
        }
    };
    public void showToast(){
        View layoutMessage = getLayoutInflater().inflate(R.layout.layout_image_notice, (ViewGroup)findViewById(R.id.layout_notice_root));
        ImageView img = (ImageView)layoutMessage.findViewById(R.id.image_notice);
        img.setImageResource(R.drawable.check_mark);
        TextView text = (TextView) layoutMessage.findViewById(R.id.text_notice_type);
        text.setText("修改成功");
        Toast toastMessage = new Toast(AddChildActivity.this);
        toastMessage.setGravity(Gravity.CENTER, 0, 0);
        toastMessage.setDuration(Toast.LENGTH_SHORT);
        toastMessage.setView(layoutMessage);
        toastMessage.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    public void onToggle(View view) {
        ((RadioGroup)view.getParent()).check(view.getId());
        // app specific stuff ..
        mbGender = view.getId() == R.id.tbtn_addchild_boy;
    }

    protected boolean ValidButton()
    {
        return mAddChildNickNameEdt.getText().toString().length() != 0 && mAddChildBirthdayEdt.getText().toString().length() != 0;

    }
}
