package com.android.fourthwardcoder.movingpictures.models;

import android.util.Log;

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

        ArrayList<IdNamePair> directorList = new ArrayList<IdNamePair>();
        for(Crew crew : getCrew()) {

            if(crew.getJob().equals(MovieDbAPI.TAG_JOB_DIRECTOR)) {
                IdNamePair director = new IdNamePair(crew.getId());
                director.setName(crew.getName());
                directorList.add(director);
            }
        }

        return directorList;
    }

}
