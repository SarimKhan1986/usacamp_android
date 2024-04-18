package com.usacamp.utils;

public class LearningRecordItem {
    public LearningRecordItem(String comment, String partid, String score,String date,String levelid, String lessonid, int t, int idx){
        id = idx;
        mstrComment = comment;
        mstrPart_id = partid;
        mstrScore = score;
        mstrSroceDate = date;
        mstrLevel_id = levelid;
        mstrLesson_id = lessonid;
        kind = t;
    }
    int id;
    String mstrComment;
    String mstrPart_id;
    String mstrScore;
    String mstrLevel_id;
    String mstrLesson_id;
    int kind;
    public String mstrSroceDate;
}

