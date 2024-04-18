package com.usacamp.model;

public class MediaItem {
    public MediaItem(int nIdx, int nIsMain, String strTitle, String strImage, String strVideoFile, String strDuration, String strFileSize, String strDesc, String strCreatedDate, int ordernum){
        mIdx = nIdx;
        mIsMain = nIsMain;
        mstrTitle = strTitle;
        mstrImage = strImage;
        mstrVideoFile = strVideoFile;
        mstrDuration = strDuration;
        mstrFileSize = strFileSize;
        mstrDesc = strDesc;
        mstrCreatedDate = strCreatedDate;
        mnOrderNum = ordernum;
    }
    public int mIdx;
    public int mIsMain;
    public String mstrTitle;
    public String mstrImage;
    public String mstrVideoFile;
    public String mstrDuration;
    public String mstrFileSize;
    public String mstrDesc;
    public String mstrCreatedDate;
    public int mnOrderNum;
}
