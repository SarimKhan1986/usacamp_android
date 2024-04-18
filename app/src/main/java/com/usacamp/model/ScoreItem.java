package com.usacamp.model;

public class ScoreItem {
    public ScoreItem (int nLevelId, int nLessonId, int nScore, String strAnswerTime){
        mnLevelId = nLevelId;
        mnLessonId = nLessonId;
        mnScore = nScore;
        mstrAnswerTime = strAnswerTime;
    }
    public int mnLevelId;
    public int mnLessonId;
    public int mnScore;
    public String mstrAnswerTime;
}
