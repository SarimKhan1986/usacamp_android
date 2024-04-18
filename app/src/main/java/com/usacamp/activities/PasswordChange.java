package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MessageAlert;

public class PasswordChange extends BaseActivity {
    EditText edt_pass1, edt_pass2;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        edt_pass1 = (EditText)findViewById(R.id.et_aurora_pass1);
        edt_pass2 = (EditText)findViewById(R.id.et_aurora_pass2);
        btnNext = (Button)findViewById(R.id.btnnext);

        edt_pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(ValidPassword()) {
                    btnNext.setBackground(getResources().getDrawable(R.drawable.green_blue_radius));
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setBackground(getResources().getDrawable(R.drawable.gray_radius_aurora));
                    btnNext.setEnabled(false);
                }

            }
        });

        edt_pass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(ValidPassword()) {
//                    btnNext.setBackground(getResources().getDrawable(R.drawable.green_blue_radius));
//                    btnNext.setEnabled(true);
//                } else {
//                    btnNext.setBackground(getResources().getDrawable(R.drawable.gray_radius_aurora));
//                    btnNext.setEnabled(false);
//                }

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onBack(View view) {
        finish();
    }

    public void onTogglePassword1(View view)
    {
        if(((ToggleButton) findViewById(R.id.tg_aurora_password)).isChecked())
            edt_pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            edt_pass1.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void onTogglePassword2(View view)
    {
        if(((ToggleButton) findViewById(R.id.tg_aurora_password_confirm)).isChecked())
            edt_pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            edt_pass2.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    protected boolean ValidPassword()
    {
        if(edt_pass1.getText().length() < edt_pass2.getText().length()) {
            MessageAlert.showMessage(this, "密码不一致，请校对1");
            return false;
        }

        if(!edt_pass1.getText().toString().startsWith(edt_pass2.getText().toString()))
        {
            MessageAlert.showMessage(this, "密码不一致，请校对2");
            return false;
        }

        return edt_pass1.getText().toString().equals(edt_pass2.getText().toString());

    }

    public void onPasswordChange(View view)
    {
        if(btnNext.isEnabled()) {
            if ((edt_pass1.getText().length() < 8 || edt_pass1.getText().length() > 16) || (edt_pass2.getText().length() < 8 || edt_pass2.getText().length() > 16) ) {
                MessageAlert.showMessage(this, "密码格式不正确，请确认");
            } else {
                MyApplication.mNetProc.changePwd("changePwd", "phone=" + MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber + "&password=" + edt_pass1.getText().toString(), Constants.PASS_CHANGE_ENTER_TYPE_ONLYCHANGE);
            }
        }
    }

}
