package com.android.fourthwardcoder.movingpictures.models;

import android.util.Log;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Credits {

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


}
