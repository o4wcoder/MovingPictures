package com.android.fourthwardcoder.movingpictures.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VideoList implements Parcelable {

    @SerializedName("results")
    @Expose
    private List<Video> results = new ArrayList<Video>();

    /**
     *
     * @return
     * The results
     */
    public List<Video> getVideos() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setVideos(List<Video> results) {
        this.results = results;
    }


    protected VideoList(Parcel in) {
        if (in.readByte() == 0x01) {
            results = new ArrayList<Video>();
            in.readList(results, Video.class.getClassLoader());
        } else {
            results = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VideoList> CREATOR = new Parcelable.Creator<VideoList>() {
        @Override
        public VideoList createFromParcel(Parcel in) {
            return new VideoList(in);
        }

        @Override
        public VideoList[] newArray(int size) {
            return new VideoList[size];
        }
    };
}
