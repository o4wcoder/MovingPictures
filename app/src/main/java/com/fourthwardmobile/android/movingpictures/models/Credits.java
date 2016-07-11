package com.fourthwardmobile.android.movingpictures.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Credits implements Parcelable {

    private static final String TAG = Credits.class.getSimpleName();

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<Cast> cast = new ArrayList<Cast>();
    @SerializedName("crew")
    @Expose
    private List<Crew> crew = new ArrayList<Crew>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The cast
     */
    public List<Cast> getCast() {
        return cast;
    }

    /**
     *
     * @param cast
     * The cast
     */
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    /**
     *
     * @return
     * The crew
     */
    public List<Crew> getCrew() {
        return crew;
    }

    /**
     *
     * @param crew
     * The crew
     */
    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public ArrayList<IdNamePair> getDirectorList() {

        return getDepartmentList(MovieDbAPI.TAG_DEPARTMENT_DIRECTING);
    }

    public ArrayList<IdNamePair> getWriterList() {

        return getDepartmentList(MovieDbAPI.TAG_DEPARTMENT_WRITING);
    }

    private ArrayList<IdNamePair> getDepartmentList(String tag) {

        ArrayList<IdNamePair> list = new ArrayList<>();
        for(Crew crew : getCrew()) {

            if(crew.getDepartment().equals(tag)) {
                IdNamePair job = new IdNamePair(crew.getId());
                job.setName(crew.getName());
                list.add(job);
            }
        }

        return list;
    }



    protected Credits(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            cast = new ArrayList<Cast>();
            in.readList(cast, Cast.class.getClassLoader());
        } else {
            cast = null;
        }
        if (in.readByte() == 0x01) {
            crew = new ArrayList<Crew>();
            in.readList(crew, Crew.class.getClassLoader());
        } else {
            crew = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (cast == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cast);
        }
        if (crew == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(crew);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Credits> CREATOR = new Parcelable.Creator<Credits>() {
        @Override
        public Credits createFromParcel(Parcel in) {
            return new Credits(in);
        }

        @Override
        public Credits[] newArray(int size) {
            return new Credits[size];
        }
    };
}
