package com.android.fourthwardcoder.movingpictures.models;

import com.android.fourthwardcoder.movingpictures.helpers.Util;

import java.util.ArrayList;

/**
 * Class TvShow
 * Author: Chris Hare
 * Created: 9/25/2015.
 *
 * Class to hold TV Show data. Most fields come form the MovieOld class
 */
public class TvShow extends MovieOld {

    ArrayList<String> createdBy;
    String createdByString;
    String lastAirDate;
    ArrayList<String> networks;
    String networksString;
    int numEpisodes;
    String status;

    int numSeasons;

    public TvShow(int id) {
        super(id);
    }

    public ArrayList<String> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ArrayList<String> createdBy) {
        this.createdBy = createdBy;

        this.createdByString = Util.buildListString(createdBy);
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public ArrayList<String> getNetworks() {
        return networks;
    }

    public void setNetworks(ArrayList<String> networks) {
        this.networks = networks;

        this.networksString = Util.buildListString(networks);
    }

    public int getNumEpisodes() {
        return numEpisodes;
    }

    public void setNumEpisodes(int numEpisodes) {
        this.numEpisodes = numEpisodes;
    }

    public int getNumSeasons() {
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedByString() {
        return createdByString;
    }

    public void setCreatedByString(String createdByString) {
        this.createdByString = createdByString;
    }

    public String getNetworksString() {
        return networksString;
    }

    public void setNetworksString(String networksString) {
        this.networksString = networksString;
    }
}
