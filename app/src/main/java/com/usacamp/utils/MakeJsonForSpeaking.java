package com.usacamp.utils;
import com.google.gson.Gson;

public class MakeJsonForSpeaking {
    void MakeJsonForSpeaking()
    {

    }

    public static String convertJsonFromObject(SpeakingResult v)
    {
        Gson gson = new Gson();
        String json = gson.toJson(v);
        return json;
    }

    public static SpeakingResult convertObjectFromJson(String str)
    {
        Gson gson = new Gson();
        return gson.fromJson(str, SpeakingResult.class);
    }

    public static Sentence convertSentenceFromJson(String str)
    {
        Gson gson = new Gson();
        return gson.fromJson(str, Sentence.class);
    }
}
