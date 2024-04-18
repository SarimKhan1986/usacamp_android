package com.usacamp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.usacamp.R;

import java.util.Calendar;

public class ModifyChildProfileActivity extends BaseActivity {
    private Activity mactivity;
    public ToggleButton mModifyChildBoyTbtn;
    public ToggleButton mModifyChildGirlTbtn;
    public EditText mModifyChildNameEdt ;
    public EditText mModifyChildNickNameEdt ;
    public TextView mModifyChildBirthdayEdt ;
    public Boolean mbGender = true;
    public String mstrBirthday ="";
    public String mstrNickName="";
    public String mstrName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mactivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_child_profile);
        mModifyChildBoyTbtn =this.findViewById(R.id.tbtn_modifychild_boy);
        mModifyChildGirlTbtn = this.findViewById(R.id.tbtn_modifychild_girl);
        mModifyChildNickNameEdt = this.findViewById(R.id.edt_modifychild_nickname);
        mModifyChildBirthdayEdt =this.findViewById(R.id.edt_modifychild_birthday);
        mModifyChildNickNameEdt.setText(MyApplication.mNetProc.mLoginUserInf.mstrName);

        mstrBirthday = MyApplication.mNetProc.mLoginUserInf.mstrBirthday;
        if(mstrBirthday == null) {
            mModifyChildBirthdayEdt.setText("");
        }else {
            String[] separated = mstrBirthday.split("-");
            if (separated.length == 3) {
                String txtName = separated[0].trim() + " 年" + separated[1].trim() + "月" + separated[2].trim() + "日";
                mModifyChildBirthdayEdt.setText(txtName);
            }
        }
        mbGender = MyApplication.mNetProc.mLoginUserInf.mstrGender.equals("0");
        togglebackchange(mbGender);
        findViewById(R.id.tbtn_modifychild_boy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mbGender = true;
                togglebackchange(mbGender);
            }
        });
        findViewById(R.id.tbtn_modifychild_girl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mbGender = false;
                togglebackchange(mbGender);
            }
        });
        findViewById(R.id.edt_modifychild_birthday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final Calendar c = Calendar.getInstance();
                int nyear = c.get(Calendar.YEAR);
                int nmonth = c.get(Calendar.MONTH);
                int nday = c.get(Calendar.DATE);
                DatePickerDialog dpDlg = new DatePickerDialog(ModifyChildProfileActivity.this , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,onDatePicked , nyear , nmonth , nday);
                dpDlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpDlg.show();
            }
        });
    }
   DatePickerDialog.OnDateSetListener onDatePicked = new DatePickerDialog.OnDateSetListener() {
       @Override
       public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
           i1 += 1;
           mstrBirthday = i + "-" + i1 + "-" + i2;
           String strMsg = i + "年" + i1 + "月" + i2 + "日";
           mModifyChildBirthdayEdt.setText(strMsg);
       }
   };

    private void togglebackchange (boolean bGender)
    {
        if (bGender){
            mModifyChildBoyTbtn.setBackgroundResource(R.drawable.btn_outline_green);
            mModifyChildBoyTbtn.setTextColor(Color.WHITE);
            mModifyChildGirlTbtn.setBackgroundResource(R.drawable.btn_outline_black);
            mModifyChildGirlTbtn.setTextColor(Color.parseColor("#303030"));

        }
        else
        {
            mModifyChildGirlTbtn.setBackgroundResource(R.drawable.btn_outline_green);
            mModifyChildGirlTbtn.setTextColor(Color.WHITE);
            mModifyChildBoyTbtn.setBackgroundResource(R.drawable.btn_outline_black);
            mModifyChildBoyTbtn.setTextColor(Color.parseColor("#303030"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onBack(View view) {
        finish();
    }
    public void onSelectChildBirthday(View view) {

    }
    //set Profile
   public void onModifyChild(View view) {
       mstrName = mModifyChildNickNameEdt.getText().toString();
       if(mstrBirthday.equals("") || mstrName.equals(""))
           return;
       int gender = mbGender ? 0 : 1;
       String mstrrequest = null;
       mstrrequest = "userid="+MyApplication.mNetProc.mLoginUserInf.mstrUserId+"&active_dev_id="+MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id + "&name=" + mstrName +"&nickname="+ mstrNickName + "&gender="+ gender +"&birthday="+mstrBirthday;
       MyApplication.mNetProc.setProfile("setProfile", mstrrequest);
    }

    public void showToast(){
        View layoutMessage = getLayoutInflater().inflate(R.layout.layout_image_notice, (ViewGroup)findViewById(R.id.layout_notice_root));
       ImageView img = (ImageView)layoutMessage.findViewById(R.id.image_notice);
       img.setImageResource(R.drawable.check_mark);
       TextView text = (TextView) layoutMessage.findViewById(R.id.text_notice_type);
       text.setText("修改成功");
       Toast toastMessage = new Toast(ModifyChildProfileActivity.this);
       toastMessage.setGravity(Gravity.CENTER, 0, 0);
       toastMessage.setDuration(Toast.LENGTH_SHORT);
       toastMessage.setView(layoutMessage);
       toastMessage.show();
    }

}
