package com.usacamp.model;

import java.util.ArrayList;

public class SpeakingIntroItem {
    public SpeakingIntroItem(int id, ArrayList<SpeakingGroupItem> g) {
        mnId = id;
        mGroup = g;
    }

    public int mnId;
    public ArrayList<SpeakingGroupItem> mGroup;


}

