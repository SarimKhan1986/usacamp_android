package com.usacamp.model;

public class ParentItem {
    public ParentItem(int Id, int type_id, String title, String short_desc, String image_link, int article_status){
        mIdx = Id;
        mType_id = type_id;
        mstrTitle = title;
        mstrContent = short_desc;
        mstrImageUrl = image_link;
        mIsImportant = article_status;
    }
    public int mIdx;
    public int mType_id;
    public String mstrTitle;
    public String mstrContent;
    public String mstrImageUrl;
    public int mIsImportant;
}
