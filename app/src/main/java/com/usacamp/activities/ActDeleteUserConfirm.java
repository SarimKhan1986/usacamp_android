package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.usacamp.R;
import com.usacamp.utils.MessageAlert;

public class ActDeleteUserConfirm extends BaseActivity {
    private EditText edtCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user_confirm);
        String str = "我们已向您的账户手机" + MyApplication.mNetProc.mLoginUserInf.muserPhoneNumber;
        ((TextView)findViewById(R.id.deluserconfrimtxt1)).setText(str);
        edtCode = (EditText)findViewById(R.id.deluserconfrimet);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int) (height * .7));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onClose(View view) { finish(); }

    public void onConfirmDeleteUser(View v)
    {
//        edtCode.setText(MyApplication.mNetProc.mLoginUserInf.mDeleteUserConfrimCode);
        if(MyApplication.mNetProc.mLoginUserInf.mDeleteUserConfrimCode.equals(edtCode.getText().toString().trim()))
            MyApplication.mNetProc.getToken("getToken", "hardware=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id);
        else
            MessageAlert.showMessage(ActDeleteUserConfirm.this, "代码错误");
    }

}
