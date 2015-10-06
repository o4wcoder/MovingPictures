package com.android.fourthwardcoder.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chris Hare on 10/4/2015.
 */
public class IdNamePair implements Parcelable {

    int id;
    String name;

    public IdNamePair(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected IdNamePair(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<IdNamePair> CREATOR = new Parcelable.Creator<IdNamePair>() {
        @Override
        public IdNamePair createFromParcel(Parcel in) {
            return new IdNamePair(in);
        }

        @Override
        public IdNamePair[] newArray(int size) {
            return new IdNamePair[size];
        }
    };
}
