package com.usacamp.utils;

public class SpeakingJsonSentence {
    SpeakingJsonSentence(int v1, int v2, int v3, String v4, float v5){
        sentence_id = v1;
        mark = v2;
        star = v3;
        sound_path = v4;
        sound_duration = v5;
    }
    public int sentence_id;
    public int mark;
    public int star;
    public String sound_path;
    public float sound_duration;
}
