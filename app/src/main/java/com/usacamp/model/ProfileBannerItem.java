package com.usacamp.model;

public class ProfileBannerItem {
    public ProfileBannerItem(int Id, String tl, String btxt, String lk, int numtype, int numstatus, String date){
        mIdx = Id;
        title = tl;
        buttontxt = btxt;
        link = lk;
        type = numtype;
        status = numstatus;
        modifieddate = date;
    }
    public int mIdx;
    public String title;
    public String buttontxt;
    public String link;
    public int type;
    public int status;
    public String modifieddate;
}
