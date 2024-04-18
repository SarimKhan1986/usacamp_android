package com.usacamp.model;

public class LevelItem {
    public LevelItem (int nId, String strLevel, int nStatus, String groupStr, boolean section){
        mnId = nId;
        mstrLevel = strLevel;
        mnStatus = nStatus;
        groupTitle = groupStr;
        isSection = section;
    }
    public int mnId;
    public String mstrLevel;
    public int mnStatus;
    public String groupTitle;
    public boolean isSection = false;
}

