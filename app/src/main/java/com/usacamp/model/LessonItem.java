package com.usacamp.model;

import java.util.ArrayList;

public class LessonItem {
    public LessonItem(int nIdx, int nLevelId, int nLessonId, ArrayList<PartItem> listParts){
        mIdx = nIdx;
        mnLevelId = nLevelId;
        mnLessonId = nLessonId;
        mlistParts = listParts;
    }
    public int mIdx;
    public int mnLevelId;
    public int mnLessonId;
    public ArrayList<PartItem> mlistParts;
}
