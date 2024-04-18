package com.usacamp.utils;

public class MyPointItem {
    public MyPointItem( String strComment , Integer nPoint , String strDate){
        mnPoint = nPoint;
        mstrComment = strComment;
        mstrDate = strDate;
    }
    Integer mnPoint;
    String mstrComment;
    String mstrDate;

    public Integer getMnPoint() {
        return mnPoint;
    }

    public void setMnPoint(Integer mnPoint) {
        this.mnPoint = mnPoint;
    }

    public String getMstrComment() {
        return mstrComment;
    }

    public void setMstrComment(String mstrComment) {
        this.mstrComment = mstrComment;
    }

    public String getMstrDate() {
        return mstrDate;
    }

    public void setMstrDate(String mstrDate) {
        this.mstrDate = mstrDate;
    }
}
