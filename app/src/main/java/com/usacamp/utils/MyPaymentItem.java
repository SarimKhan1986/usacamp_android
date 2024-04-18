package com.usacamp.utils;

public class MyPaymentItem {
    public MyPaymentItem(String strComment , Integer price , Integer point, String strDate, int type){
        mnPoint = point;
        mstrComment = strComment;
        mstrDate = strDate;
        mnPrice = price;
        mnEventType = type;
    }
    Integer mnPoint;
    String mstrComment;
    String mstrDate;
    Integer mnPrice;
    Integer mnEventType;
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

    public Integer getMnPrice() {return mnPrice;}

    public void setMnPrice() {this.mnPrice = mnPrice;}

    public Integer getMnEventType() {return mnEventType;}

    public void setMnEventTypen() {this.mnEventType= mnEventType;}
}
