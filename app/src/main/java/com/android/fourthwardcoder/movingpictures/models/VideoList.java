package com.android.fourthwardcoder.movingpictures.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VideoList {

    @SerializedName("results")
    @Expose
    private List<Video> results = new ArrayList<Video>();

    /**
     *
     * @return
     * The results
     */
    public List<Video> getVideos() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setVideos(List<Video> results) {
        this.results = results;
    }

}
