package com.usacamp.utils;

public class VersionInfo{
    public String versionName = "";
    public String versionCode = "";
    public int status = -1;
    public String store_link = "";
    public String apk_link = "";
    public String createTIme = "";
    public String modifiedTime = "";
    public String content = "";
    public VersionInfo()
    {
        versionName = "";
        versionCode = "0";
        status = -1;
        store_link = "";
        apk_link = "";
        createTIme = "";
        modifiedTime = "";
        content = "";
    }

    public void setParam(String param1, String param2, int param3, String param4, String param5, String param6,String param7, String param8)
    {
        versionName = param1;
        versionCode = param2;
        status = param3;
        store_link = param4;
        apk_link = param5;
        createTIme = param6;
        modifiedTime = param7;
        content = param8;
    }
}
