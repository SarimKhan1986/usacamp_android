package com.usacamp.utils;

import com.usacamp.activities.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Age {

    public static String calculateAge()
    {
        SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-dd");
        String strAge = "";
        try {
            Date date = endDate.parse(MyApplication.mNetProc.mLoginUserInf.mstrBirthday);
            long diff = System.currentTimeMillis() - date.getTime();

            int days = (int) (diff / (24 * 60 * 60 * 1000));
            int year = (int) (days / 365);
            int month = (int) (days % 365) / 30;
            strAge = year + "岁 " + month + "个月";
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strAge;
    }
}
