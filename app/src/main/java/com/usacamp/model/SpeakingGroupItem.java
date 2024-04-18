package com.usacamp.model;

import java.util.ArrayList;

public class SpeakingGroupItem {
    public SpeakingGroupItem(String title, ArrayList<SpeakingRowItem> r) {
        mstrTitle = title;
        mRow = r;
    }

    public String mstrTitle;
    public ArrayList<SpeakingRowItem> mRow;


}
