package com.usacamp.utils;

import java.util.ArrayList;

public class SpeakingJsonScore {
    SpeakingJsonScore(int v1, int v2, int v3, ArrayList<SpeakingJsonSentence> v4){
        sentence_idx = v1;
        readCnt =v2;
        starCnt = v3;
        result = v4;
    }
    public int sentence_idx;
    public int readCnt;
    public int starCnt;
    public ArrayList<SpeakingJsonSentence> result = new ArrayList<SpeakingJsonSentence>();
}
