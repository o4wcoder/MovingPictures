package com.android.fourthwardcoder.popularmovies.models;

/**
 * Class Credit
 * Author: Chris Hare
 * Created: 9/2/2015.
 *
 * Class to hold a person's movie credit details
 */
public class Credit implements Comparable{

    int id;
    String title;
    String posterPath;
    int releaseYear;
    String character;

    public Credit(int movieId) {
        this.id = movieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }


    @Override
    public int compareTo(Object object) {

        int compareYear=((Credit)object).getReleaseYear();
        return compareYear - this.releaseYear;
    }
}
