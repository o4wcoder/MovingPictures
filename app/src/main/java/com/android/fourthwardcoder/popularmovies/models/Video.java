package com.android.fourthwardcoder.popularmovies.models;

/**
 * Class Video
 * Author: Chris Hare
 * Created: 8/28/2015.
 *
 * Class to hold the details of videos/trailers of a movie
 */
public class Video {

    String name;
    String key;
    String type;
    int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
