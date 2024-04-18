package com.usacamp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MD5;
import com.usacamp.utils.MessageAlert;
import com.usacamp.utils.PayConf;
import com.usacamp.utils.PurchaseLevelGridAdapter;
import com.usacamp.utils.SeekbarImageMerge;
import com.usacamp.utils.Util;


import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.usacamp.utils.Util.httpPost;

public class PurchaseCampActivity extends BaseActivity{

    private int mnSelCnt = 0;
    private final ArrayList<Integer> marrSelLessons = new ArrayList<Integer>( );
    GridView mgridPurchaseLevelInf = null;
    private PurchaseLevelGridAdapter madptLevelGrid = null;
    int discountPrice = 0 , originalPrice = 0;
    int mypoint = 0;
    int mypointtomoney = 0;
    public  float mWxPayAmount = 0;
    private IWXAPI mWxApi;
    private boolean isSelectEntire = false;
    public static final String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
    ImageView imgUnChecked, imgChecked;
    TextView txtdisplaypoint;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase_camp);
        imgUnChecked = (ImageView) findViewById(R.id.imguncheckImg);
        imgChecked = (ImageView) findViewById(R.id.imgcheckImg);
        mgridPurchaseLevelInf = (GridView)findViewById(R.id.grid_purchase_levels);
        madptLevelGrid = new PurchaseLevelGridAdapter(this, R.layout.layout_purchase_level_item);
        mgridPurchaseLevelInf.setAdapter(madptLevelGrid);

        txtdisplaypoint = (TextView)findViewById(R.id.pointdisplaytxt);
        mWxApi = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID );
        mWxApi.registerApp(MyApplication.getInstance().WECHAT_APPID);
        ((TextView)findViewById(R.id.text_date)).setText(getExpireDays() + "天");
        String displayStr  = "使用" + mypoint + "积分，抵扣" + mypointtomoney + "元";
        txtdisplaypoint.setText(displayStr);
        ((LinearLayout)findViewById(R.id.purchase_entire_ly)).setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                onEntireLevelSelect();
            }
        });

//        for(int i = 0 ; i < MyApplication.mNetProc.mLoginUserInf.mlistLevels.size() ; i++ ) {
//            if(MyApplication.mNetProc.mLoginUserInf.mlistLevels.get(isubstr.mnStatus == Constants.LEVEL_ALREADY_PURCHASE )
//                marrSelLessons.add(MyApplication.mNetProc.mLoginUserInf.mlistLevels.get(i).mnId);
//        }
        mgridPurchaseLevelInf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int nId, long l) {
                if(MyApplication.mNetProc.mLoginUserInf.muserType == 3 ||  MyApplication.mNetProc.mLoginUserInf.muserType == 4) {
                    return;
                }
                switch (madptLevelGrid.getLevelStatus(nId)){
                    case Constants.LEVEL_ALREADY_FINISH:

                        break;
                    case Constants.LEVEL_SELECTED:

                        for (int i = 0; i < marrSelLessons.size(); i++)
                            if (marrSelLessons.get(i) == nId){
                                mnSelCnt--;
                                marrSelLessons.remove(i);
                                break;
                            }
                        if (!isContinuosLesson()){
                            MessageAlert.showMessage(PurchaseCampActivity.this, "课程只能连续购买。");
                            marrSelLessons.add(nId);
                            mnSelCnt++;
                        }else
                            madptLevelGrid.setLevelStatus(nId, Constants.LEVEL_UNAVAILABLE);
                        break;
                    case Constants.LEVEL_UNAVAILABLE:
                        mnSelCnt++;
                        marrSelLessons.add(nId);
                        if (isContinuosLesson()) {
                            madptLevelGrid.setLevelStatus(nId, Constants.LEVEL_SELECTED);
                        }else {
                            MessageAlert.showMessage(PurchaseCampActivity.this, "课程只能连续购买。");
                            marrSelLessons.remove(marrSelLessons.size() - 1);
                            mnSelCnt--;
                        }
                        break;
                    case Constants.LEVEL_EXPERIENCE:
                        mnSelCnt++;
                        marrSelLessons.add(nId);
                        if (isContinuosLesson()) {
                            madptLevelGrid.setLevelStatus(nId, Constants.LEVEL_SELECTED);
                        }else {
                            MessageAlert.showMessage(PurchaseCampActivity.this, "课程只能连续购买。");
                            marrSelLessons.remove(marrSelLessons.size() - 1);
                            mnSelCnt--;
                        }
                        break;
                }
                setTitleText();
            }
        });

        if(MyApplication.mNetProc.mLoginUserInf.muserType == 3 ||  MyApplication.mNetProc.mLoginUserInf.muserType == 4) {
            for(int i = 0 ; i < 12 ; i++)
                madptLevelGrid.setLevelStatus(i, Constants.LEVEL_ALREADY_PURCHASE);
        } else {
            setPurchaseLevelInforamtion();
        }

        //Zhuge
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put(Constants.Zhuge_Property_Entrance, getIntent().getStringExtra(Constants.Zhuge_Property_Entrance));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ////ZhugeSDK.getInstance().track(MyApplication.getInstance().getCurrentActivity(), Constants.Zhuge_Event_Enter_Main_BuyCamp,  eventObject);
    }


    public void onBack(View view){
        finish();
    }


    public void goPurchaseConfActivity(){
        if(marrSelLessons.size() == 0)
            return;
        mWxPayAmount = discountPrice - mypointtomoney;
        if(MyApplication.mNetProc.mLoginUserInf.mbAlreadyLogin) {
            try {
                onPayNow();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            MainActivity.mainActivityInstance.loginAuth(false);
//            Intent loginAct = new Intent(this, LoginActivity.class);
//            MyApplication.mNetProc.mLoginUserInf.mTempLoginPriviousAct = "purchase";
//            startActivity(loginAct);
//            finish();
        }
    }

    public void setPurchaseLevelInforamtion(){

//        if(MyApplication.mNetProc.mLoginUserInf.muserType == 2 || MyApplication.mNetProc.mLoginUserInf.muserType == 5)
//            return;
        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistLevels.size(); i++){
            int levelNum = Integer.parseInt(MyApplication.mNetProc.mLoginUserInf.mlistLevels.get(i).mstrLevel.substring(5));
            switch (MyApplication.mNetProc.mLoginUserInf.mlistLevels.get(i).mnStatus)
            {
                case 0:
                    madptLevelGrid.setLevelStatus(levelNum - 1, Constants.LEVEL_UNAVAILABLE);
                    break;
                case 1:
                    madptLevelGrid.setLevelStatus(levelNum - 1, Constants.LEVEL_ALREADY_PURCHASE);
                    break;
                case 2:
                    madptLevelGrid.setLevelStatus(levelNum - 1, Constants.LEVEL_ALREADY_PURCHASE);
                    break;
                case 3:
                    madptLevelGrid.setLevelStatus(levelNum - 1, Constants.LEVEL_ALREADY_PURCHASE);
                    break;
                case 4:
                    madptLevelGrid.setLevelStatus(levelNum - 1, Constants.LEVEL_UNAVAILABLE);
                    break;
                default:
                        break;
            }
        }
    }

    public void setTitleText(){
        String strTitle = "CAMP ";
        if (marrSelLessons.size() == 1){
            strTitle += (marrSelLessons.get(0) + 1);
        }else if (marrSelLessons.size() == 2){
            strTitle += (marrSelLessons.get(0) + 1);
            strTitle += ", CAMP ";
            strTitle += (marrSelLessons.get(1) + 1);
        }else if (marrSelLessons.size() > 2) {
            int[] anSelLessons = new int[marrSelLessons.size()];
            for (int i = 0; i < marrSelLessons.size(); i++)
                anSelLessons[i] = marrSelLessons.get(i);
            Arrays.sort(anSelLessons);
            strTitle += String.valueOf(anSelLessons[0] + 1);
            strTitle += " ~ CAMP";
            strTitle += String.valueOf(anSelLessons[marrSelLessons.size() - 1] + 1);
        }else
            strTitle = "";

        ((TextView)findViewById(R.id.text_selected_lessons)).setText(strTitle);

        String strOriginPrice = "0";
        String strDiscoundPrice = "0";

        for (int i = 0; i < MyApplication.mNetProc.mLoginUserInf.mlistPayConf.size(); i++) {

            PayConf payConf = MyApplication.mNetProc.mLoginUserInf.mlistPayConf.get(i);
            if (payConf.mnCount == mnSelCnt)//MyApplication.mNetProc.mLoginUserInf.mnUserPayType == payConf.mnType &&
            {
                if(MyApplication.mNetProc.mLoginUserInf.muserType == 1 ||  MyApplication.mNetProc.mLoginUserInf.muserType == 5) {
                    strOriginPrice = String.valueOf(payConf.mnOriginPrice);
                    strDiscoundPrice = String.valueOf(payConf.mnDiscountPrice);
                } else if(MyApplication.mNetProc.mLoginUserInf.muserType == 0 || MyApplication.mNetProc.mLoginUserInf.muserType == 2)
                {
                    strOriginPrice = String.valueOf(payConf.mnOriginPrice);
                    strDiscoundPrice = String.valueOf(payConf.mnOriginPrice);
                } else {
                    strOriginPrice = "0";
                    strDiscoundPrice = "0";
                }
                break;
            }
        }

        if(marrSelLessons.size()!=0) {
            discountPrice = Integer.parseInt(strDiscoundPrice);
            originalPrice = Integer.parseInt(strOriginPrice);
        } else{
            discountPrice = 0;
            originalPrice = 0;
        }

        int savedPrice = originalPrice - discountPrice;
        int payPrice = 0;
        payPrice = discountPrice - mypointtomoney;
        ((TextView)findViewById(R.id.text_origin_price)).setText("¥" + payPrice);
        if(MyApplication.mNetProc.mLoginUserInf.muserType == 1 ||  MyApplication.mNetProc.mLoginUserInf.muserType == 5) {
            ((TextView)findViewById(R.id.text_saved_price)).setText("已为您节省" + savedPrice + "元");
        }

        ((TextView)findViewById(R.id.text_date)).setText("课程有效期剩余:" + getAddExpiredDays(mnSelCnt) + "天");

    }
    private boolean isContinuosLesson(){

        int[] anSelLessons = new int[marrSelLessons.size()];
        for (int i = 0; i < marrSelLessons.size(); i++)
            anSelLessons[i] = marrSelLessons.get(i);
        Arrays.sort(anSelLessons);
        for (int i = 1; i < marrSelLessons.size(); i++){
            if (anSelLessons[i] - 1 != anSelLessons[i-1])
                return false;
        }
        return true;
    }

    public void onPurchase(View view){
        MyApplication.mNetProc.mLoginUserInf.mSelCampCount = marrSelLessons.size();
        MyApplication.mNetProc.mLoginUserInf.mSelCampList = marrSelLessons;

        if(marrSelLessons.size() == 0) {
            MessageAlert.showMessage(this, "请选择欲购买的等级。");
            return;
        }
        if (isContinuosLesson()){
            goPurchaseConfActivity();
        }else
            MessageAlert.showMessage(this, "只有连属的星级才能购买。");
    }

    @SuppressLint("WrongViewCast")
    public void onUsePoint(View view) {
        mypoint = 0;
        LayoutInflater factory = LayoutInflater.from(MyApplication.getInstance().getCurrentActivity());
        View pointDialogView = factory.inflate(R.layout.purchase_point_dlg, null);
        Dialog pointDialog = new Dialog(MyApplication.getInstance().getCurrentActivity());
        pointDialog.setCanceledOnTouchOutside(false);
        pointDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pointDialog.setContentView(pointDialogView);
        pointDialog.getWindow().setGravity(Gravity.CENTER);
        pointDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((TextView)pointDialogView.findViewById(R.id.purchase_point_txt)).setText(String.valueOf(MyApplication.mNetProc.mLoginUserInf.myTotalPoint));

        pointDialog.show();
        ((SeekbarImageMerge)pointDialogView.findViewById(R.id.seekbarmerge)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress * 1000 > MyApplication.mNetProc.mLoginUserInf.myTotalPoint)
                    mypoint = MyApplication.mNetProc.mLoginUserInf.myTotalPoint;
                else
                    mypoint = progress * 1000;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((ImageView)pointDialogView.findViewById(R.id.point_dlg_close)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                pointDialog.dismiss();
            }
        });

        ((Button)pointDialogView.findViewById(R.id.point_dlg_confirm)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mypointtomoney = mypoint / MyApplication.mNetProc.mLoginUserInf.mnRate;
                int tempPrice = discountPrice - mypointtomoney;
                if(tempPrice < 0)
                    tempPrice = 0;

                ((TextView)findViewById(R.id.text_origin_price)).setText("¥" + tempPrice);

                if(mypoint == 0) {
                    txtdisplaypoint.setTextColor(Color.parseColor("#999999"));
                    imgChecked.setVisibility(View.INVISIBLE);
                    imgUnChecked.setVisibility(View.VISIBLE);
                } else {
                    txtdisplaypoint.setTextColor(Color.parseColor("#FC9D00"));
                    imgChecked.setVisibility(View.VISIBLE);
                    imgUnChecked.setVisibility(View.INVISIBLE);
                }
                String displayStr  = "使用" + mypoint + "积分，抵扣" + mypointtomoney + "元";
                txtdisplaypoint.setText(displayStr);
                pointDialog.dismiss();
            }
        });
    }

    public void onCloseWindow(View view)
    {
        finish();
    }
    public void onRepairSel(View view) {
        finish();
    }

    public void onPayNow() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MyApplication.mNetProc.mLoginUserInf.mnUsedPoint = mypoint;
        MyApplication.mNetProc.mLoginUserInf.mPayedPrice = mWxPayAmount;
        submitOrder(mWxPayAmount * 100, 1, "127.0.0.1");//mWxPayAmount * 100
        PayReq req = new PayReq();
        req.appId = MyApplication.getInstance().WECHAT_APPID;
        req.partnerId = Constants.partnerId;
        req.prepayId = Constants.prepayId;
        req.nonceStr = Constants.nonceStr;
        req.packageValue = "Sign=WXPay";
        req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> map = new HashMap<>();

        map.put("appid", req.appId);
        map.put("noncestr", req.nonceStr);
        map.put("package", req.packageValue);
        map.put("partnerid", req.partnerId);
        map.put("prepayid", req.prepayId);
        map.put("timestamp", req.timeStamp);

        req.sign = Sign(map, Constants.appKey);

        boolean bResult = mWxApi.sendReq(req);

    }

    public void submitOrder(double realPayPrice, int tradeType, String ip) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        int realpayPrice=(int)realPayPrice;
        Map<String, String> map = new HashMap<>();
        map.put("appid", MyApplication.getInstance().WECHAT_APPID);
        map.put("body", "游美英语");
        map.put("mch_id", Constants.partnerId);
        map.put("nonce_str", Constants.nonceStr);
        map.put("notify_url", "http://www.weixin.qq.com/wxpay/pay.php");
        SimpleDateFormat format = new SimpleDateFormat("yyyymmddhhmmss");
        String sTradeNo = format.format(new Date());
        map.put("out_trade_no", sTradeNo);
        map.put("spbill_create_ip", ip);
        map.put("total_fee", realpayPrice + "");
        map.put("trade_type", "APP");

        String sign = Sign(map, Constants.appKey);

        System.out.println(sign);
        map.put("sign", sign);

        try {
            // 最关键的一步，我们要把最终发送的数据字符转为字节后,再使用“ISO8859-1”进行编码，得到“ISO8859-1”的字符串，否则有可能有“签名错误”的问题
            byte[] buf = httpPost(url, new String(toXml(map).getBytes(),"ISO8859-1"));
            String content = new String(buf);
            System.out.println(content);
            Map<String, String> xml = DecodeXml(content);
            Constants.prepayId = xml.get("prepay_id");
            //nonceStr = xml.get("nonce_str");
            //appKey = xml.get("sign");
            //Toast.makeText(this, prepayId, Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private String Sign(Map<String, String> params, String signKey) {
        StringBuilder sb = new StringBuilder();
        List<String> sortedParams = new ArrayList<>(params.keySet());
        Collections.sort(sortedParams);

        for (String key : sortedParams) {
            sb.append(key + "=" + params.get(key) + "&");
        }

        sb.append("key=" + signKey);

        String sign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return sign;
    }
    private String toXml(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        List<String> sortedParams = new ArrayList<>(params.keySet());
        //Collections.sort(sortedParams);
        for (String key : sortedParams) {
            sb.append("<" + key + ">");
            sb.append(params.get(key));
            sb.append("</" + key + ">");
        }
        sb.append("</xml>");
        System.out.println(sb.toString());
        return sb.toString();
    }
    private Map<String,String> DecodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName=parser.getName();

                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if("xml".equals(nodeName)==false){
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion",e.toString());
        }
        return null;
    }
    private int getAddExpiredDays(int selCount)
    {
        int expireDays = 0;
        int expirePurchasesDays = 0;
        expirePurchasesDays = MyApplication.mNetProc.mLoginUserInf.mnPurchasedDays;
        expirePurchasesDays += selCount * 90;
        if(expirePurchasesDays < MyApplication.mNetProc.mLoginUserInf.mnManualDays)
            expireDays = MyApplication.mNetProc.mLoginUserInf.mnManualDays;
        else
            expireDays = expirePurchasesDays;

        return expireDays;
    }
    private int getExpireDays()
    { int expireDays = 0;
        if (MyApplication.mNetProc.mLoginUserInf.muserType == 3 || MyApplication.mNetProc.mLoginUserInf.muserType == 4) {
            expireDays = 0;
            return expireDays;
        }

        SimpleDateFormat createdDate = new SimpleDateFormat("yyyy-MM-dd");
//        if(MyApplication.mNetProc.mLoginUserInf.muserType == 2 || MyApplication.mNetProc.mLoginUserInf.muserType == 5) {
//            try {
//                if(MyApplication.mNetProc.mLoginUserInf.bExperience == 1) { // 체험 이벤트를 구입하였다면
//                    if(MyApplication.mNetProc.mLoginUserInf.mPurchasedList.size() != 0) { // 레벨도 구입하였다면
//                        //$expireDays = $this->dateDifference($curDate, $level_purchased->end_time);
////                        Date createddate1 = createdDate.parse(MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(0).mstrEnd);
////                        Calendar calendar1 = Calendar.getInstance();
////                        calendar1.setTime(createddate1);
////                        calendar1.add(Calendar.DAY_OF_YEAR, 1 + MyApplication.mNetProc.mLoginUserInf.pExperience);
////                        Date end_availableDays = calendar1.getTime();
////                        Calendar calendar11 = Calendar.getInstance();
////                        long diff = end_availableDays.getTime() - calendar11.getTime().getTime();
////                        expireDays = (int) (diff / (24 * 60 * 60 * 1000));
////                        if (expireDays < 0)
////                            expireDays = 0;
////                        expireDays += mnSelCnt * 90;
////                        return expireDays;
//                    } else { // 구입하지 않았다면
//                        Date createddate3 = createdDate.parse(MyApplication.mNetProc.mLoginUserInf.dExperience);
//                        Calendar calendar3 = Calendar.getInstance();
//                        calendar3.setTime(createddate3);
//                        calendar3.add(Calendar.DAY_OF_YEAR, MyApplication.mNetProc.mLoginUserInf.pExperience + 1);
//                        Date end_availableDays = calendar3.getTime();
//                        Calendar calendar31 = Calendar.getInstance();
//                        long diff = end_availableDays.getTime() - calendar31.getTime().getTime();
//                        expireDays = (int) (diff / (24 * 60 * 60 * 1000));
//                        if (expireDays < 0)
//                            expireDays = 0;
//                        expireDays += mnSelCnt * 90;
//                        return expireDays;
//                    }
//                } else {
//                    Date createddate2 = createdDate.parse(MyApplication.mNetProc.mLoginUserInf.mCreatedTime);
//                    Calendar calendar2 = Calendar.getInstance();
//                    calendar2.setTime(createddate2);
//                    calendar2.add(Calendar.DAY_OF_YEAR, MyApplication.mNetProc.mLoginUserInf.mAvailableDays + 1);
//                    Date end_availableDays = calendar2.getTime();
//                    Calendar calendar21 = Calendar.getInstance();
//                    long diff = end_availableDays.getTime() - calendar21.getTime().getTime();
//                    expireDays = (int) (diff / (24 * 60 * 60 * 1000));
//                    if (expireDays < 0)
//                        expireDays = 0;
////                    return expireDays;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        if(MyApplication.mNetProc.mLoginUserInf.mlistProgress.size() != 0) {
            Calendar calendar = Calendar.getInstance();
            try {
                ArrayList<Integer> arrayDayList = new ArrayList<Integer>();
                for(int i = 0 ; i < MyApplication.mNetProc.mLoginUserInf.mlistProgress.size() ; i++) {
                    int expireDays_tmp = 0;
                    Date purchasedDate = createdDate.parse(MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mstrEnd);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(purchasedDate);
                    calendar3.add(Calendar.DAY_OF_YEAR, 1);
                    Date paidTime = calendar3.getTime();
                    long diff = paidTime.getTime() - calendar.getTime().getTime();
                    expireDays_tmp = (int) (diff / (24 * 60 * 60 * 1000));
                    arrayDayList.add(expireDays_tmp);
                    if(MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mnType == 3)
                        MyApplication.mNetProc.mLoginUserInf.mnManualDays = expireDays_tmp;
                    else if(MyApplication.mNetProc.mLoginUserInf.mlistProgress.get(i).mnType == 1)
                        MyApplication.mNetProc.mLoginUserInf.mnPurchasedDays = expireDays_tmp;

                }
                expireDays = Collections.max(arrayDayList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (expireDays < 0)
            expireDays = 0;

//        expireDays += mnSelCnt * 90;
        return expireDays;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(MyApplication.mNetProc.mLoginUserInf.isUsePoint) {
//            imgChecked.setVisibility(View.VISIBLE);
//            imgUnChecked.setVisibility(View.INVISIBLE);
//
//        }else{
//            imgChecked.setVisibility(View.INVISIBLE);
//            imgUnChecked.setVisibility(View.VISIBLE);
//        }
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onEntireLevelSelect()
    {
        if(isSelectEntire)
        {
            isSelectEntire = false;
            ((TextView)findViewById(R.id.purchase_entire_txt)).setTextColor(Color.parseColor("#999999"));
            ((ImageView)findViewById(R.id.purchase_entire_img)).setImageDrawable(getDrawable(R.drawable.act_rule_uncheck));

            for(int i = 0 ; i < Constants.LEVEL_COUNT1 ; i++) {
                if(madptLevelGrid.getLevelStatus(i) == Constants.LEVEL_ALREADY_PURCHASE)
                    continue;

                madptLevelGrid.setLevelStatus(i, Constants.LEVEL_UNAVAILABLE);

            }
            mnSelCnt  = 0;
            marrSelLessons.clear();
            setTitleText();
            madptLevelGrid.notifyDataSetChanged();
            return;
        } else {
            isSelectEntire = true;
            mnSelCnt = 0;
            marrSelLessons.clear();
            ((TextView)findViewById(R.id.purchase_entire_txt)).setTextColor(Color.parseColor("#F84C59"));
            ((ImageView)findViewById(R.id.purchase_entire_img)).setImageDrawable(getDrawable(R.drawable.act_rule_check));

        }

        if(MyApplication.mNetProc.mLoginUserInf.muserType == 3 ||  MyApplication.mNetProc.mLoginUserInf.muserType == 4) {
            return;
        }

        for(int i = 0 ; i < Constants.LEVEL_COUNT1 ; i++) {

            if(madptLevelGrid.getLevelStatus(i) == Constants.LEVEL_ALREADY_PURCHASE)
                continue;
            mnSelCnt++;
            marrSelLessons.add(i);
            madptLevelGrid.setLevelStatus(i, Constants.LEVEL_SELECTED);

        }

        setTitleText();
        madptLevelGrid.notifyDataSetChanged();
    }
}
