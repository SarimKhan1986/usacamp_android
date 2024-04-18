package com.usacamp.model;

public class ActCenterItem {
    public ActCenterItem(String strTitle , Integer nId , String strDate){
        mnId = nId;
        mstrTitle = strTitle;
        mstrDate = strDate;
    }
    Integer mnId;
    String mstrTitle;
    String mstrDate;

    public Integer getMnId() {
        return mnId;
    }

    public void setMnId(Integer mnId) {
        this.mnId = mnId;
    }

    public String getMstrTitle() {
        return mstrTitle;
    }

    public void setMstrTitle(String mstrTitle) {
        this.mstrTitle = mstrTitle;
    }

    public String getMstrDate() {
        return mstrDate;
    }

    public void setMstrDate(String mstrDate) {
        this.mstrDate = mstrDate;
    }
}
