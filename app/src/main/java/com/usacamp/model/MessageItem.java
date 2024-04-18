package com.usacamp.model;

public class MessageItem {
    public MessageItem(int nIdx, int nUserId, String strTitle, String strContent, String strDate, int nIsRead, int nIsActive){
        mIdx = nIdx;
        mUserId = nUserId;
        mstrTitle = strTitle;
        mstrContent = strContent;
        mstrDate = strDate;
        mIsRead = nIsRead;
        mIsActive = nIsActive;
    }
    public int mIdx;
    public int mUserId;
    public String mstrTitle;
    public String mstrContent;
    public String mstrDate;
    public int mIsRead;
    public int mIsActive;
}
