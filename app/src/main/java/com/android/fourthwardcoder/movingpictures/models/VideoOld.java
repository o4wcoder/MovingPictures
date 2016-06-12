package com.android.fourthwardcoder.movingpictures.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class VideoOld
 * Author: Chris Hare
 * Created: 8/28/2015.
 * <p/>
 * Class to hold the details of videos/trailers of a movie
 */
public class VideoOld implements Parcelable {

    String key;
    String name;
    String type;
    int size;

    public VideoOld(String key) {
       this.key = key;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected VideoOld(Parcel in) {
        name = in.readString();
        key = in.readString();
        type = in.readString();
        size = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(type);
        dest.writeInt(size);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VideoOld> CREATOR = new Parcelable.Creator<VideoOld>() {
        @Override
        public VideoOld createFromParcel(Parcel in) {
            return new VideoOld(in);
        }

        @Override
        public VideoOld[] newArray(int size) {
            return new VideoOld[size];
        }
    };
}

