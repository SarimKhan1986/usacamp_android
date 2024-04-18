package com.usacamp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.usacamp.R;
import com.usacamp.constants.Constants;
import com.usacamp.utils.MD5;
import com.usacamp.utils.Util;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseConfirm extends BaseActivity {
    public  float mWxPayAmount = 0;
    private IWXAPI mWxApi;
    public static final String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
    private final ArrayList<Integer> marrSelLessons = new ArrayList<Integer>( );
    TextView realPricetxt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_confirm);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .5), (int) (height * .3));

        realPricetxt = (TextView) findViewById(R.id.realpricetxt) ;
        realPricetxt.setText("¥" + mWxPayAmount);
        mWxApi = WXAPIFactory.createWXAPI(this, MyApplication.getInstance().WECHAT_APPID );
    }

    public void onBack(View view) {
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApplication) getApplicationContext()).setCurrentActivity(this) ;
    }
    public void onCloseWindow(View view)
    {
        finish();
    }
    public void onRepairSel(View view) {
        finish();
    }

    public void onPayNow(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        submitOrder(mWxPayAmount * 100, 1, "127.0.0.1");
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

        // process the result ???
//        Intent puchaseSuccess = new Intent(this, PurchaseSuccess.class);
//        startActivity(puchaseSuccess);
    }

    public void submitOrder(double realPayPrice, int tradeType, String ip) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        int realpayPrice=(int)realPayPrice;
        Map<String, String> map = new HashMap<>();
        map.put("appid", MyApplication.getInstance().WECHAT_APPID);
        map.put("body", "游美少儿英语");
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
            byte[] buf = Util.httpPost(url, new String(toXml(map).getBytes(),"ISO8859-1"));
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


}
