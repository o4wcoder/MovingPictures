package com.fourthwardmobile.android.movingpictures.models;

import java.util.HashMap;

/**
 * Created by Chris Hare on 8/31/2016.
 */
public class User {
    private String name;
    private String email;
    private HashMap<String, Object> timestampJoined;
    private boolean hasLoggedInWithPassword;

    public User() {}

    public User(String name, String email, HashMap<String,Object> timestampJoined) {

        this.name = name;
        this.email = email;
        this.timestampJoined = timestampJoined;
        this.hasLoggedInWithPassword = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampJoined;
    }

    public void setTimestampCreated(HashMap<String, Object> timestampCreated) {
        this.timestampJoined = timestampCreated;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }
}
