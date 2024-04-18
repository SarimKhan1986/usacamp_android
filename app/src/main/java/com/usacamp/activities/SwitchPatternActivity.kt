package com.usacamp.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import cn.com.chinatelecom.account.api.CtAuth.mHandler
import com.usacamp.R
import com.usacamp.activities.MyApplication.*
import com.usacamp.constants.Constants
import com.usacamp.constants.State
import com.usacamp.utils.MessageAlert

class SwitchPatternActivity : BaseActivity() {
    lateinit var simgStudent : ImageView
    lateinit var simgParent : ImageView
    lateinit var sVerifyPanel : Group
    var smsVerifyCode = "xxxxxx"
    var mnTime = 60
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_pattern)

        simgParent = findViewById(R.id.simgparrentpattern)
        simgStudent = findViewById(R.id.simgstudentpattern)
        sVerifyPanel= findViewById(R.id.group4)

        when(State.getCurrentPattern()){
            State.pattern.parent.value -> {
                simgParent.setImageResource(R.drawable.sparentselectedimg)
                simgStudent.setImageResource(R.drawable.sstudentimg)
            }
            State.pattern.student.value -> {
                simgParent.setImageResource(R.drawable.sparentimg)
                simgStudent.setImageResource(R.drawable.sstudentselectedimg)
            }
        }

        findViewById<TextView>(R.id.sphonenumber).text = mNetProc.mLoginUserInf.muserPhoneNumber.substring(0, 3)+"***"+ mNetProc.mLoginUserInf.muserPhoneNumber.substring(8, 11)
        simgStudent.setOnClickListener{

            if(State.getCurrentPattern() != State.pattern.student.value)
                State.setPattern(State.pattern.student.value)
            else
                finish()

        }

        simgParent.setOnClickListener{
            if(State.getCurrentPattern() != State.pattern.parent.value)
                showVerifyDlg();
            else
                finish()
        }

        findViewById<TextView>(R.id.button31).setOnClickListener {

            if(smsVerifyCode.equals(findViewById<EditText>(R.id.et_pattern_code).text.toString())) {
//                finish()
                State.setPattern(State.pattern.parent.value)
            }
        }
        findViewById<TextView>(R.id.button30).setOnClickListener {

            sVerifyPanel.visibility = View.GONE
        }
        findViewById<TextView>(R.id.txt_pattern_verifycode).setOnClickListener {
            if (mnTime == 60) getVerifyCode()
        }
    }

    override fun onResume() {
        super.onResume()
        getInstance().currentActivity = this;
    }
//
//    override fun onBackPressed() {
//        return
//    }

    fun showVerifyDlg()
    {
        sVerifyPanel.visibility = View.VISIBLE
    }

    fun getVerifyCode() {
        val strPhone: String = mNetProc.mLoginUserInf.muserPhoneNumber
        mNetProc.sendSms(Constants.SMS_TYPE_PATTERN_VERIFY, "sendSms", "phone=$strPhone&type=2")
    }
    fun finishSMS(nErrorCode: Int, strVerifyCode: String) {

        when (nErrorCode) {
            0 -> {
                smsVerifyCode = String(Base64.decode(strVerifyCode.toByteArray(), Base64.DEFAULT))
                mHandler = @SuppressLint("HandlerLeak")
                object : Handler() {
                    @SuppressLint("HandlerLeak", "SetTextI18n")
                    override fun handleMessage(msg: Message) {
                        mnTime--
                        findViewById<TextView>(R.id.txt_pattern_verifycode).text = mnTime.toString() + "s后重新获取"
                        findViewById<TextView>(R.id.txt_pattern_verifycode).setTextColor(Color.parseColor("#999999"))
                        if (mnTime == 0) {
                            findViewById<TextView>(R.id.txt_pattern_verifycode).text = "获取验证码"
                            findViewById<TextView>(R.id.txt_pattern_verifycode).setTextColor(Color.parseColor("#1D9F39"))
                            mnTime = 60
                        } else sendMessageDelayed(obtainMessage(1), 1000)
                    }
                }
                mHandler.sendMessage(mHandler.obtainMessage(1))
                MessageAlert.showMessage(this, true, "验证码已发")
            }

        }
    }
}