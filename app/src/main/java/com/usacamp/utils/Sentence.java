package com.usacamp.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Sentence implements Parcelable {
    public Sentence(int id, String s)
    {
        sentence_id = id;
        sentence = s;

    }
    public void setResultInformation(int duration,int starcount, int score, String path)
    {
        sound_duration = duration;
        star = starcount;
        mark = score;
        sound_path = path;

    }

    public void setAudioPath(String p)
    {
        sound_path = p;
    }

    public int sentence_id = 0;
    public String sentence = "";
    public int sound_duration = 0;
    public int star = 0;
    public int mark = 0;
    public String sound_path = "";
    protected Sentence(Parcel in) {
        sentence_id = in.readInt();
        sentence = in.readString();
        sound_duration = in.readInt();
        star = in.readInt();
        mark = in.readInt();
        sound_path = in.readString();
    }

    public static final Creator<Sentence> CREATOR = new Creator<Sentence>() {
        @Override
        public Sentence createFromParcel(Parcel in) {
            return new Sentence(in);
        }

        @Override
        public Sentence[] newArray(int size) {
            return new Sentence[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(sentence_id);
        dest.writeString(sentence);
        dest.writeInt(sound_duration);
        dest.writeInt(star);
        dest.writeInt(mark);
        dest.writeString(sound_path);
        //dest.writeInt(isread);
    }
}
