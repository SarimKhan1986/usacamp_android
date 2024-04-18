package com.usacamp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.usacamp.R;

public class DlgCustom_Wechat_Bind extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public DlgCustom_Wechat_Bind(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_wechat_bind);
        yes = (Button) findViewById(R.id.dlg_confirmbtn);
        no = (Button) findViewById(R.id.dlg_cancelbtn);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dlg_confirmbtn:
                break;
            case R.id.dlg_cancelbtn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}