package com.android.fourthwardcoder.movingpictures.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.fourthwardcoder.movingpictures.data.MovieContract;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    //Number of cast members to be displayed on the Details Activity
    public static final int NUM_CAST_DISPLAY = 3;

    public static final String TAG = Movie.class.getSimpleName();

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

    ArrayList<String> genres;
    ArrayList<IdNamePair> directors;
    ArrayList<IdNamePair> actors;
    ArrayList<Video> videos;

    //Fields calculated internally
    String genreString;
    String directorString;
    String actorsString;

    /**************************************************************/
    /*                      Constructors                          */
    /**************************************************************/
    public Movie(int id) {
        this.id = id;
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

        if(dateArray != null)
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

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;

        this.genreString = Util.buildListString(genres);
    }

    public String getGenreString() {
        return genreString;
    }


    public ArrayList<IdNamePair> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<IdNamePair> directors) {
        this.directors = directors;

        this.directorString = Util.buildPersonListString(directors);
    }

    public ArrayList<IdNamePair> getActors() {
        return actors;
    }


    public void setActors(ArrayList<IdNamePair> actors) {

        this.actors = actors;

        String strActors = "";

        //We want to show at least 3 actors, but some movies have less
        int numActors = NUM_CAST_DISPLAY;
        if(actors.size() < NUM_CAST_DISPLAY)
            numActors = actors.size();

        //Set up display string for actors. Just display the first 3 top billed
        for(int i = 0; i < numActors; i++) {
            strActors += actors.get(i).getName() + ", ";
        }

        if(actors.size() > 0)
            this.actorsString = strActors.substring(0,strActors.length() - 2);
    }

    public String getDirectorString() {
        return directorString;
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

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }


    /**
     *
     * @return
     */
    public ContentValues getContentValues() {

        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_ID, this.id);
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, this.title);
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, this.overview);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, this.posterPath);
        movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, this.backdropPath);
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, this.releaseDate);
        movieValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, this.runtime);
        movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, this.rating);
        movieValues.put(MovieContract.MovieEntry.COLUMN_REVENUE, this.revenue);

        //Store Array lists as JSON Strings. No need to create complicated SQL tabels
        //a
        // s these values will never change once the data has been fetched.
        movieValues.put(MovieContract.MovieEntry.COLUMN_GENRE_JSON,new Gson().toJson(genres));
        movieValues.put(MovieContract.MovieEntry.COLUMN_DIRECTOR_JSON, new Gson().toJson(directors));
        movieValues.put(MovieContract.MovieEntry.COLUMN_ACTOR_JSON, new Gson().toJson(actors));
        movieValues.put(MovieContract.MovieEntry.COLUMN_VIDEO_JSON, new Gson().toJson(videos));




        return movieValues;
    }

    public static Movie convertCursorToMovie(Cursor cursor) {

        Movie movie = new Movie(cursor.getInt(MovieContract.COL_MOVIE_ID));
        movie.setTitle(cursor.getString(MovieContract.COL_MOVIE_TITLE));
        movie.setOverview(cursor.getString(MovieContract.COL_MOVIE_OVERVIEW));
        movie.setPosterPath(cursor.getString(MovieContract.COL_MOVIE_POSTER_PATH));
        movie.setBackdropPath(cursor.getString(MovieContract.COL_MOVIE_BACKDROP_PATH));
        movie.setReleaseDate(cursor.getString(MovieContract.COL_MOVIE_RELEASE_DATE));
        movie.setRuntime(cursor.getString(MovieContract.COL_MOVIE_RUNTIME));
        movie.setRating(cursor.getDouble(MovieContract.COL_MOVIE_RATING));
        movie.setRevenue(cursor.getString(MovieContract.COL_MOVIE_REVENUE));

        //Get Json String and put them into ArrayLists
        Type stringType = new TypeToken<ArrayList<String>>(){}.getType();
        Type idNamePairType = new TypeToken<ArrayList<IdNamePair>>(){}.getType();
        Type videoType = new TypeToken<ArrayList<Video>>(){}.getType();

        //Parse genres
        String genreJsonString = cursor.getString(MovieContract.COL_MOVIE_GENRE_JSON);
        movie.setGenres((ArrayList<String>)new Gson().fromJson(genreJsonString,stringType));
        //Parse directors
        String directorJsonString = cursor.getString(MovieContract.COL_MOVIE_DIRECTOR_JSON);
        movie.setDirectors((ArrayList<IdNamePair>) new Gson().fromJson(directorJsonString, idNamePairType));
        //Parse actors
        String actorJsonString = cursor.getString(MovieContract.COL_MOVIE_ACTOR_JSON);
        movie.setActors((ArrayList<IdNamePair>) new Gson().fromJson(actorJsonString, idNamePairType));
        //Parse videos
        String videoJsonString = cursor.getString(MovieContract.COL_MOVIE_VIDEO_JSON);
        movie.setVideos((ArrayList<Video>) new Gson().fromJson(videoJsonString, videoType));
        return movie;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        releaseDate = in.readString();
        releaseYear = in.readString();
        runtime = in.readString();
        rating = in.readDouble();
        revenue = in.readString();
        if (in.readByte() == 0x01) {
            genres = new ArrayList<String>();
            in.readList(genres, String.class.getClassLoader());
        } else {
            genres = null;
        }
        if (in.readByte() == 0x01) {
            directors = new ArrayList<IdNamePair>();
            in.readList(directors, IdNamePair.class.getClassLoader());
        } else {
            directors = null;
        }
        if (in.readByte() == 0x01) {
            actors = new ArrayList<IdNamePair>();
            in.readList(actors, IdNamePair.class.getClassLoader());
        } else {
            actors = null;
        }
        if (in.readByte() == 0x01) {
            videos = new ArrayList<Video>();
            in.readList(videos, Video.class.getClassLoader());
        } else {
            videos = null;
        }
        genreString = in.readString();
        directorString = in.readString();
        actorsString = in.readString();
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
        dest.writeString(runtime);
        dest.writeDouble(rating);
        dest.writeString(revenue);
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
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
        if (videos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(videos);
        }
        dest.writeString(genreString);
        dest.writeString(directorString);
        dest.writeString(actorsString);
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