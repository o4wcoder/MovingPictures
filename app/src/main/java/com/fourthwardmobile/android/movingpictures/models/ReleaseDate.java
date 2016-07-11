package com.fourthwardmobile.android.movingpictures.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReleaseDate implements Parcelable {
    @SerializedName("certification")
    @Expose
    private String certification;
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("type")
    @Expose
    private Integer type;

    /**
     *
     * @return
     * The certification
     */
    public String getCertification() {
        return certification;
    }

    /**
     *
     * @param certification
     * The certification
     */
    public void setCertification(String certification) {
        this.certification = certification;
    }

    /**
     *
     * @return
     * The iso6391
     */
    public String getIso6391() {
        return iso6391;
    }

    /**
     *
     * @param iso6391
     * The iso_639_1
     */
    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    /**
     *
     * @return
     * The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     *
     * @param releaseDate
     * The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     *
     * @return
     * The type
     */
    public Integer getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(Integer type) {
        this.type = type;
    }


    protected ReleaseDate(Parcel in) {
        certification = in.readString();
        iso6391 = in.readString();
        releaseDate = in.readString();
        type = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(certification);
        dest.writeString(iso6391);
        dest.writeString(releaseDate);
        if (type == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(type);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReleaseDate> CREATOR = new Parcelable.Creator<ReleaseDate>() {
        @Override
        public ReleaseDate createFromParcel(Parcel in) {
            return new ReleaseDate(in);
        }

        @Override
        public ReleaseDate[] newArray(int size) {
            return new ReleaseDate[size];
        }
    };
}