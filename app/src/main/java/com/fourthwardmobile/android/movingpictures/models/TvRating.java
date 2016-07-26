package com.fourthwardmobile.android.movingpictures.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TvRating implements Parcelable {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("rating")
    @Expose
    private String rating;

    /**
     *
     * @return
     * The iso31661
     */
    public String getIso31661() {
        return iso31661;
    }

    /**
     *
     * @param iso31661
     * The iso_3166_1
     */
    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    /**
     *
     * @return
     * The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }


    protected TvRating(Parcel in) {
        iso31661 = in.readString();
        rating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iso31661);
        dest.writeString(rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TvRating> CREATOR = new Parcelable.Creator<TvRating>() {
        @Override
        public TvRating createFromParcel(Parcel in) {
            return new TvRating(in);
        }

        @Override
        public TvRating[] newArray(int size) {
            return new TvRating[size];
        }
    };
}
