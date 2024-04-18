package com.usacamp.model;

public class ReportItem {
    String mstrImagePath;
    boolean mfDeletable = false;
    public ReportItem(String strImagePath) {
        mstrImagePath = strImagePath;
    }
    public void setDeletable(boolean fDelable){
        mfDeletable = fDelable;
    }
}
