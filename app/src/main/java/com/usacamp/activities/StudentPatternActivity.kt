package com.usacamp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.usacamp.R
import com.usacamp.constants.Constants
import com.usacamp.fragment.FragmentLearn
import com.usacamp.fragment.FragmentSpeaking
import com.usacamp.utils.AdapterImageSlider
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class StudentPatternActivity : BaseActivity(){
    lateinit var slearnBtn : TextView
    lateinit var sspeakingBtn : TextView
    lateinit var sbannerpage : ViewPager2
    lateinit var adapterSlider:AdapterImageSlider
    lateinit var sphotoImg : CircularImageView
    val sfraLearn:FragmentLearn = FragmentLearn()
    val sfraSpeaking:FragmentSpeaking = FragmentSpeaking()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_pattern)
//        MyApplication.getInstance().getCurrentActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        instance = this;
        slearnBtn = findViewById(R.id.stxtlearn)
        sspeakingBtn = findViewById(R.id.stxtspeaking)
        sphotoImg = findViewById(R.id.scircularstudentpic)
//        refreshNameAge()
//        refreshPhoto()

        slearnBtn.setOnClickListener {
            setTitleView(0)
            val fm: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fm.beginTransaction().replace(R.id.scontainer, sfraLearn)
            fragmentTransaction.commit() }

        sspeakingBtn.setOnClickListener {
            setTitleView(1)
            val fm: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fm.beginTransaction().replace(R.id.scontainer, sfraSpeaking)
            fragmentTransaction.commit()
        }

        findViewById<ImageView>(R.id.simgmsg).setOnClickListener{
            val msgCentAct = Intent(this, MessageCenterAct::class.java)
            startActivity(msgCentAct)
        }

        findViewById<ImageView>(R.id.simgsetting).setOnClickListener{
            val settingActivity = Intent(this, SettingActivity::class.java)
            startActivity(settingActivity)
        }

        findViewById<ImageView>(R.id.simgpattern).setOnClickListener{
            val switchInt = Intent(this, SwitchPatternActivity::class.java)
            switchInt.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(switchInt)
        }

        findViewById<ImageView>(R.id.simgtest).setOnClickListener{
            val systemAct = Intent(this, SystemTestAct::class.java)
            startActivity(systemAct)
        }

        findViewById<ImageView>(R.id.simglearnhistory).setOnClickListener{
            val learningrecordActivity = Intent(this, LearningRecordActivity::class.java)
            startActivity(learningrecordActivity)
        }
        findViewById<ImageView>(R.id.simgpoint).setOnClickListener{
            val api = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID)

            val req = WXLaunchMiniProgram.Req()
            req.userName = Constants.wechat_miniapp_id // 填小程序原始id
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版

            api.sendReq(req)
        }
        findViewById<ImageView>(R.id.simgshare).setOnClickListener{
            val strRequestParameter = "userid=" + MyApplication.mNetProc.mLoginUserInf.mstrUserId + "&active_dev_id=" + MyApplication.mNetProc.mLoginUserInf.mstrActive_dev_id +
                    "&kind=0"

            MyApplication.mNetProc.getShareInfo("getShareInfo", strRequestParameter)
        }
        findViewById<ImageView>(R.id.simgchangechildinfo).setOnClickListener{
            val modifychildActivity = Intent(this, ModifyChildProfileActivity::class.java)
            startActivity(modifychildActivity)
        }
        slearnBtn.callOnClick()
        sbannerpage = findViewById<View>(R.id.svgbanner) as ViewPager2
        adapterSlider = AdapterImageSlider(MyApplication.mNetProc.mLoginUserInf.mlistbanner, R.layout.item_slider_image)
        sbannerpage.setAdapter(adapterSlider)
    }
//    override fun onBackPressed() {
//        return
//        // your code.
//    }
    private fun getExpireDays(): Int {
        var expireDays = 0
        if (MyApplication.mNetProc.mLoginUserInf.muserType == 3 || MyApplication.mNetProc.mLoginUserInf.muserType == 4) {
            expireDays = 0
            return expireDays
        }
        val createdDate = SimpleDateFormat("yyyy-MM-dd")
        if (MyApplication.mNetProc.mLoginUserInf.mlistProgress.size != 0) {
            val calendar = Calendar.getInstance()
            try {
                val arrayDayList = ArrayList<Int>()
                for (i in MyApplication.mNetProc.mLoginUserInf.mlistProgress.indices) {
                    var expireDays_tmp =  0
                    val purchasedDate = createdDate.parse(MyApplication.mNetProc.mLoginUserInf.mlistProgress[i].mstrEnd)
                    val calendar3 = Calendar.getInstance()
                    calendar3.time = purchasedDate
                    calendar3.add(Calendar.DAY_OF_YEAR, 1)
                    val paidTime = calendar3.time
                    val diff = paidTime.time - calendar.time.time
                    expireDays_tmp = (diff / (24 * 60 * 60 * 1000)).toInt()
                    arrayDayList.add(expireDays_tmp)
                }
                expireDays = Collections.max(arrayDayList)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        if (expireDays < 0) expireDays = 0
        return expireDays
    }
    override fun onResume() {
        super.onResume()
        MyApplication.getInstance().setCurrentActivity(this);
        refreshPhoto()
        refreshNameAge()
    }
    fun setTitleView(selectedIndex: Int) {
        if (selectedIndex == 0) {
            slearnBtn.setTextColor(Color.BLACK)
            sspeakingBtn.setTextColor(Color.GRAY)
            if (MyApplication.getInstance().isTablet) {
                slearnBtn.setTextSize(34f)
                sspeakingBtn.setTextSize(30f)
            } else {
                slearnBtn.setTextSize(22f)
                sspeakingBtn.setTextSize(20f)
            }
            slearnBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.tabindicatoricon)
            sspeakingBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            slearnBtn.setTextColor(Color.GRAY)
            sspeakingBtn.setTextColor(Color.BLACK)
            if (MyApplication.getInstance().isTablet) {
                slearnBtn.setTextSize(30f)
                sspeakingBtn.setTextSize(34f)
            } else {
                slearnBtn.setTextSize(20f)
                sspeakingBtn.setTextSize(22f)
            }
            slearnBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            sspeakingBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.tabindicatoricon)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun refreshNameAge()
    {
        findViewById<TextView>(R.id.stxtname).text = MyApplication.mNetProc.mLoginUserInf.mstrName;
        if (MyApplication.mNetProc.mLoginUserInf.mstrBirthday != "") {
            val endDate = SimpleDateFormat("yyyy-MM-dd")
            var strAge = ""
            try {
                val date = endDate.parse(MyApplication.mNetProc.mLoginUserInf.mstrBirthday)
                val diff = System.currentTimeMillis() - date.time
                val days = (diff / (24 * 60 * 60 * 1000)).toInt()
                val month = (days % 365) / 30
                strAge = (days / 365).toString() + "岁 " + month + "个月"
                println(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            findViewById<TextView>(R.id.stxtage).text= strAge
        }

        findViewById<TextView>(R.id.stxtday).text = "学习游美"+ MyApplication.mNetProc.mLoginUserInf.mnStudyDays.toString() + "天"

        if (MyApplication.mNetProc.mLoginUserInf.muserType == 3 || MyApplication.mNetProc.mLoginUserInf.muserType == 4)
            findViewById<TextView>(R.id.stxtremian).text = "永久"
        else {
            findViewById<TextView>(R.id.stxtremian).text = getExpireDays().toString() + "天"
        }

        findViewById<TextView>(R.id.stxtpoint).text = MyApplication.mNetProc.mLoginUserInf.myTotalPoint.toString()

    }
    fun refreshCampList()
    {
        sfraLearn.setStudyLevelInforamtion()
    }
    fun refreshBanner()
    {
        adapterSlider.notifyDataSetChanged()
    }

    fun refreshPhoto() {
        if (MyApplication.mNetProc.mLoginUserInf.mstrPic.equals(""))
            sphotoImg.setImageDrawable(resources.getDrawable(R.drawable.lucy_girl))
        else {
            val uri = Uri.parse(MyApplication.mNetProc.mLoginUserInf.mstrPic)
            val reqCreator = Picasso.with(this).load(uri)
            reqCreator.into(sphotoImg)
        }
    }

    fun refreshTeachvideoTitle()
    {
        sfraLearn.setTeacherTitleVideo()
    }

    companion object {
        lateinit var instance: StudentPatternActivity
    }
}