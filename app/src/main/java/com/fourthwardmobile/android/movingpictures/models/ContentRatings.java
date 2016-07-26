package com.fourthwardmobile.android.movingpictures.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentRatings implements Parcelable {

    @SerializedName("results")
    @Expose
    private List<TvRating> results = new ArrayList<TvRating>();

    /**
     *
     * @return
     * The results
     */
    public List<TvRating> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<TvRating> results) {
        this.results = results;
    }


    protected ContentRatings(Parcel in) {
        if (in.readByte() == 0x01) {
            results = new ArrayList<TvRating>();
            in.readList(results, TvRating.class.getClassLoader());
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
    public static final Parcelable.Creator<ContentRatings> CREATOR = new Parcelable.Creator<ContentRatings>() {
        @Override
        public ContentRatings createFromParcel(Parcel in) {
            return new ContentRatings(in);
        }

        @Override
        public ContentRatings[] newArray(int size) {
            return new ContentRatings[size];
        }
    };
}
