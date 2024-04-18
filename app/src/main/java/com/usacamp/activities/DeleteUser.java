package com.usacamp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MessageAlert;

public class DeleteUser extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        String strContent = "\"确认注销\" <font color='red'>" + MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber + "</font>." + "账号, " + Constants.DelUser_privacy_conttent;
        ((TextView)findViewById(R.id.delusercontent)).setText(Html.fromHtml(strContent), TextView.BufferType.SPANNABLE);
        ((TextView)findViewById(R.id.delusercontent)).setMovementMethod(new ScrollingMovementMethod());
    }

    public void onBack(View view) {
        finish();
    }

    public void onAgreePrivacy(View v) {
        if(MyApplication.mNetProc.mLoginUserInf.isDeleteUser) {
            ((ImageView) findViewById(R.id.imguncheckImg)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.imgcheckImg)).setVisibility(View.GONE);
            MyApplication.mNetProc.mLoginUserInf.isDeleteUser = false;
        } else {
            ((ImageView) findViewById(R.id.imguncheckImg)).setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.imgcheckImg)).setVisibility(View.VISIBLE);
            MyApplication.mNetProc.mLoginUserInf.isDeleteUser = true;
        }
    }

    public void OnPrivacyAct(View v)
    {
        Intent privacyAct = new Intent(DeleteUser.this, PrivacyAct.class);
        startActivity(privacyAct);
    }

    public void onDeleteUser(View v)
    {
        if(MyApplication.mNetProc.mLoginUserInf.isDeleteUser) {
            MyApplication.mNetProc.sendSms(Constants.SMS_TYPE_USER_DELETE,"sendSms", "phone=" + MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber + "&type=0");
            Intent deleteuserAct = new Intent(DeleteUser.this, ActDeleteUserConfirm.class);
            startActivity(deleteuserAct);
        } else {
            MessageAlert.showMessage(this, "请阅读并同意“注销协议”");
        }
    }
}
