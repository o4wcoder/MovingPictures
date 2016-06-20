package com.android.fourthwardcoder.movingpictures.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReleaseDates {

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

}