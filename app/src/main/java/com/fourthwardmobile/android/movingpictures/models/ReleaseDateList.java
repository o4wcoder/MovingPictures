package com.fourthwardmobile.android.movingpictures.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReleaseDateList implements Parcelable {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("release_dates")
    @Expose
    private List<ReleaseDate> releaseDates = new ArrayList<ReleaseDate>();

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
     * The releaseDates
     */
    public List<ReleaseDate> getReleaseDates() {
        return releaseDates;
    }

    /**
     *
     * @param releaseDates
     * The release_dates
     */
    public void setReleaseDates(List<ReleaseDate> releaseDates) {
        this.releaseDates = releaseDates;
    }


    protected ReleaseDateList(Parcel in) {
        iso31661 = in.readString();
        if (in.readByte() == 0x01) {
            releaseDates = new ArrayList<ReleaseDate>();
            in.readList(releaseDates, ReleaseDate.class.getClassLoader());
        } else {
            releaseDates = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iso31661);
        if (releaseDates == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(releaseDates);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReleaseDateList> CREATOR = new Parcelable.Creator<ReleaseDateList>() {
        @Override
        public ReleaseDateList createFromParcel(Parcel in) {
            return new ReleaseDateList(in);
        }

        @Override
        public ReleaseDateList[] newArray(int size) {
            return new ReleaseDateList[size];
        }
    };
}
