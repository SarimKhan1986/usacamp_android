package com.usacamp.model;

public class ProgressItem {
    public ProgressItem(int nLevelId, String strBegin, String strEnd, int nLessonId, int nPartId, int nCurrentLessonId, int nCurrentPartId, int nType, int nOralLessonId, int nOralPartId, int nOralCurrentLessonId, int nOralCurrentPartId){
        mnLevelId = nLevelId;
        mstrBegin = strBegin;
        mstrEnd = strEnd;
        mnLessonId = nLessonId;
        mnPartId = nPartId;
        mnCurrentLessonId = nCurrentLessonId;
        mnCurrentPartId = nCurrentPartId;
        mnType = nType;
        mnOralLessonId = nOralLessonId;
        mnOralPartId = nOralPartId;
        mnOralCurrentLessonId = nOralCurrentLessonId;
        mnOralCurrentPartId = nOralCurrentPartId;
    }
    public void setMnLevelId(int v) {mnLevelId = v;}
    public int getMnLevelId() {return mnLevelId;}
    public void setMstrBegin(String v) {mstrBegin = v;}
    public String getMstrBegin() {return mstrBegin;}
    public void setMstrEnd(String v) {mstrEnd = v;}
    public String getMstrEnd() {return mstrEnd;}
    public void setMnLessonId(int v) {mnLessonId = v;}
    public int getMnLessonId(){return mnLessonId;}
    public void setMnPartId(int v) {mnPartId = v;}
    public int getMnPartId() {return mnPartId;}
    public void setMnCurrentLessonId(int v) {mnCurrentLessonId = v;}
    public int getMnCurrentLessonId() {return mnCurrentLessonId;}
    public void setMnCurrentPartId(int v) {mnCurrentPartId = v;}
    public int getMnCurrentPartId() {return mnCurrentPartId;}
    public void setMnType(int v) {mnType = v;}
    public int getMnType() {return mnType;}
    public void setMnOralCurrentLessonId(int v) {mnOralCurrentLessonId = v;}
    public void setMnOralCurrentPartId(int v) {mnOralCurrentPartId = v;}
    public int getMnOralCurrentLessonId() {return mnOralCurrentLessonId;}
    public int getMnOralCurrentPartIdId() { return mnOralCurrentPartId ;}
    public int mnLevelId;
    public String mstrBegin;
    public String mstrEnd;
    public int mnLessonId;
    public int mnPartId;
    public int mnCurrentLessonId;
    public int mnCurrentPartId;
    public int mnType;
    public int mnOralLessonId;
    public int mnOralPartId;
    public int mnOralCurrentLessonId;
    public int mnOralCurrentPartId;
}

