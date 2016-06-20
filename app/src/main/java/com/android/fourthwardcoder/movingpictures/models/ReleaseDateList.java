package com.android.fourthwardcoder.movingpictures.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReleaseDateList {

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

}
