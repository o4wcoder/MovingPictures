package com.android.fourthwardcoder.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Class Movie
 * Author:  Chris Hare
 * Created: 7/26/2015
 *
 * Class to hold Movie details. Implements Parcelable to be passed between activities.
 */
public class Movie implements Parcelable {

    /******************************************************/
    /*                   Constants                        */
    /******************************************************/
    //Number of cast memembers to be displayed on the Details Activity
    private static final int NUM_CAST_DISPLAY = 3;

    /******************************************************/
    /*                   Local Data                       */
    /******************************************************/
    int id;
    String title;
    String overview;
    String posterPath;
    String backdropPath;
    String releaseDate;
    String releaseYear;
    String runtime;
    double rating;
    String revenue;

    ArrayList<String> genreList;
    String genreString;
    ArrayList<String> directors;
    String directorString;
    ArrayList<String> actors;
    String actorsString;

    /**************************************************************/
    /*                      Constructors                          */
    /**************************************************************/
    public Movie() {

    }

    /**************************************************************/
    /*                   Public Getter/Setters                    */
    /**************************************************************/
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;

        String[] dateArray = releaseDate.split("-");
        releaseYear = dateArray[0];
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(ArrayList<String> genreList) {
        this.genreList = genreList;

        String strGenres = "";
        for(int i = 0; i<genreList.size(); i++) {
            strGenres += genreList.get(i) + ", ";
        }

        if(genreList.size() > 0)
           this.genreString = strGenres.substring(0,strGenres.length() - 2);
    }

    public String getGenreString() {
        return genreString;
    }

    public void setGenreString(String genreString) {
        this.genreString = genreString;
    }

    public ArrayList<String> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<String> directors) {
        this.directors = directors;

        String strDirs = "";
        //Set up display string of directors
        for(int i = 0; i< directors.size(); i++) {
            strDirs += directors.get(i) + ", ";
        }

        if(directors.size() > 0)
            this.directorString = strDirs.substring(0,strDirs.length() - 2);
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;

        String strActors = "";

        //We want to show at least 3 actors, but some movies have less
        int numActors = NUM_CAST_DISPLAY;
        if(actors.size() < NUM_CAST_DISPLAY)
           numActors = actors.size();

        //Set up display string for actors. Just display the first 3 top billed
        for(int i = 0; i < numActors; i++) {
            strActors += actors.get(i) + ", ";
        }

        if(actors.size() > 0)
            this.actorsString = strActors.substring(0,strActors.length() - 2);
    }

    public String getDirectorString() {
        return directorString;
    }

    public void setDirectorString(String directorString) {
        this.directorString = directorString;
    }

    public String getActorsString() {
        return actorsString;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }


    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = "$" + revenue;
    }

    /**************************************************************/
    /*                      Parcelable Section                    */
    /**************************************************************/
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        releaseDate = in.readString();
        releaseYear = in.readString();
        rating = in.readDouble();
        directorString = in.readString();
        actorsString = in.readString();
        runtime = in.readString();
        revenue = in.readString();

        if (in.readByte() == 0x01) {
            genreList = new ArrayList<String>();
            in.readList(genreList, Integer.class.getClassLoader());
        } else {
            genreList = null;
        }
        genreString = in.readString();
        if (in.readByte() == 0x01) {
            directors = new ArrayList<String>();
            in.readList(directors, String.class.getClassLoader());
        } else {
            directors = null;
        }
        if (in.readByte() == 0x01) {
            actors = new ArrayList<String>();
            in.readList(actors, String.class.getClassLoader());
        } else {
            actors = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(releaseDate);
        dest.writeString(releaseYear);
        dest.writeDouble(rating);
        dest.writeString(directorString);
        dest.writeString(actorsString);
        dest.writeString(runtime);
        dest.writeString(revenue);
        if (genreList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genreList);
        }
        dest.writeString(genreString);
        if (directors == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(directors);
        }
        if (actors == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(actors);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}