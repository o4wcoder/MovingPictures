package com.android.fourthwardcoder.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chare on 9/1/2015.
 */
public class PersonPhoto implements Parcelable {

    int personId;
    String thumbnailImagePath;
    String fullImagePath;

    public PersonPhoto(int personId) {
        this.personId = personId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getThumbnailImagePath() {
        return thumbnailImagePath;
    }

    public void setThumbnailImagePath(String thumbnailImagePath) {
        this.thumbnailImagePath = thumbnailImagePath;
    }

    public String getFullImagePath() {
        return fullImagePath;
    }

    public void setFullImagePath(String fullImagePath) {
        this.fullImagePath = fullImagePath;
    }

    protected PersonPhoto(Parcel in) {
        personId = in.readInt();
        thumbnailImagePath = in.readString();
        fullImagePath = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(personId);
        dest.writeString(thumbnailImagePath);
        dest.writeString(fullImagePath);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PersonPhoto> CREATOR = new Parcelable.Creator<PersonPhoto>() {
        @Override
        public PersonPhoto createFromParcel(Parcel in) {
            return new PersonPhoto(in);
        }

        @Override
        public PersonPhoto[] newArray(int size) {
            return new PersonPhoto[size];
        }
    };
}