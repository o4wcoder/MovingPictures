package com.android.fourthwardcoder.movingpictures.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReleaseDates implements Parcelable {

    @SerializedName("results")
    @Expose
    private List<ReleaseDateList> results = new ArrayList<ReleaseDateList>();

    /**
     *
     * @return
     * The results
     */
    public List<ReleaseDateList> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<ReleaseDateList> results) {
        this.results = results;
    }


    protected ReleaseDates(Parcel in) {
        if (in.readByte() == 0x01) {
            results = new ArrayList<ReleaseDateList>();
            in.readList(results, ReleaseDateList.class.getClassLoader());
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
    public static final Parcelable.Creator<ReleaseDates> CREATOR = new Parcelable.Creator<ReleaseDates>() {
        @Override
        public ReleaseDates createFromParcel(Parcel in) {
            return new ReleaseDates(in);
        }

        @Override
        public ReleaseDates[] newArray(int size) {
            return new ReleaseDates[size];
        }
    };
}