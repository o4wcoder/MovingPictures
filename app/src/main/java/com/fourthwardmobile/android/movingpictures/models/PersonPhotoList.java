package com.fourthwardmobile.android.movingpictures.models;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonPhotoList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("profiles")
    @Expose
    private List<Profile> profiles = new ArrayList<Profile>();

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
     * The profiles
     */
    public List<Profile> getProfiles() {
        return profiles;
    }

    /**
     *
     * @param profiles
     * The profiles
     */
    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

}
