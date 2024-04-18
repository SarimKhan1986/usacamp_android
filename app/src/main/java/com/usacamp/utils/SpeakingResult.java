package com.usacamp.utils;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class SpeakingResult implements Parcelable {

    public SpeakingResult(int currentidx, int readcnt, int starcnt, int campid, int lessonid , int partid,List<Sentence> listResult)
    {
        sentence_idx = currentidx;
        readCnt = readcnt;
        starCnt = starcnt;
        result = listResult;
        level_id = campid;
        lesson_id = lessonid;
        part_id = partid;
    }

    public int sentence_idx;
    public int readCnt;
    public int starCnt;
    public int level_id;
    public int lesson_id;
    public int part_id;
    public List<Sentence> result = new ArrayList<>();

    protected SpeakingResult(Parcel in) {
        sentence_idx = in.readInt();
        readCnt = in.readInt();
        starCnt = in.readInt();
        level_id = in.readInt();
        lesson_id =in.readInt();
        part_id = in.readInt();
        in.readList(result, Sentence.class.getClassLoader());
    }

    public static final Creator<SpeakingResult> CREATOR = new Creator<SpeakingResult>() {
        @Override
        public SpeakingResult createFromParcel(Parcel in) {
            return new SpeakingResult(in);
        }

        @Override
        public SpeakingResult[] newArray(int size) {
            return new SpeakingResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(sentence_idx);
        dest.writeInt(readCnt);
        dest.writeInt(starCnt);
        dest.writeInt(level_id );
        dest.writeInt(lesson_id );
        dest.writeInt(part_id );
        dest.writeList(result);
    }
}
