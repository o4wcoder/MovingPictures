package com.android.fourthwardcoder.movingpictures.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;

//import com.android.fourthwardcoder.popularmovies.interfaces.APIKeys;
import com.android.fourthwardcoder.movingpictures.interfaces.APIKeys;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Credit;
import com.android.fourthwardcoder.movingpictures.models.IdNamePair;
import com.android.fourthwardcoder.movingpictures.models.Movie;
import com.android.fourthwardcoder.movingpictures.models.Person;
import com.android.fourthwardcoder.movingpictures.models.Review;
import com.android.fourthwardcoder.movingpictures.models.TvShow;
import com.android.fourthwardcoder.movingpictures.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class MovieDbAPI
 * Author: Chris Hare
 * Created: 8/25/2015.
 *
 * Helper class for doing all access to the Movies DB API. This contains all defines for URLs,
 * paths, and JSON tags. Contains helper methods polling the Movie DB and returning data.
 */
public class MovieDbAPI implements Constants {

    /*******************************************************************************/
    /*                                Constants                                    */
    /**
     * ***************************************************************************
     */
    private static final String TAG = MovieDbAPI.class.getSimpleName();

    public static final String NUM_QUERY_PAGES = "3";

    //API Key for the Movies DB API. Replace "APIKeys.MOVIE_DB_API_KEY" with your own API Key
    public static final String API_KEY_MOVIE_DB = APIKeys.MOVIE_DB_API_KEY;
    //Base URLs
    public static final String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/" + NUM_QUERY_PAGES;
    //Full URLs for the movie DB
    public static final String BASE_DISCOVER_URL = "http://api.themoviedb.org/" + NUM_QUERY_PAGES + "/discover/movie";
    //Base URL for movie images
    public static final String BASE_MOVIE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/";
    public static final String BASE_YOUTUBE_THUMB_URL = "http://img.youtube.com/vi/";

    //Movie image sizes
    public static final String IMAGE_185_SIZE = "w185/";
    public static final String IMAGE_342_SIZE = "w342/";
    public static final String IMAGE_500_SIZE = "w500/";

    //Extra append paths for the movie URI
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TV = "tv";
    public static final String PATH_UPCOMING = "upcoming";
    public static final String PATH_NOW_PLAYING = "now_playing";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_PERSON = "person";
    public static final String PATH_IMAGES = "images";
    public static final String PATH_TAGGED_IMAGES = "tagged_images";
    public static final String PATH_MOVIE_CREDIT = "movie_credits";
    public static final String PATH_TV_CREDIT = "tv_credits";

    //Extra append patch for youtube
    public static final String PATH_WATCH = "watch";

    //Extra append params for the movie URI
    public static final String PARAM_SORT = "sort_by";
    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_WITH_CAST = "with_cast";

    //Extra append params for youtbue
    public static final String PARAM_V = "v";
    public static final String YOUTUBE_THUMB_SIZE = "0.jpg";

    //JSON TAGS
    public static final String TAG_RESULTS = "results";
    public static final String TAG_ID = "id";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_CONTENT = "content";
    public static final String TAG_TITLE = "title";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_POSTER_PATH = "poster_path";
    public static final String TAG_BACKDROP_PATH = "backdrop_path";
    public static final String TAG_RELEASE_DATE = "release_date";
    public static final String TAG_RATING = "vote_average";
    public static final String TAG_CREDITS = "credits";
    public static final String TAG_CAST = "cast";
    public static final String TAG_CREW = "crew";
    public static final String TAG_JOB = "job";
    public static final String TAG_JOB_DIRECTOR = "Director";
    public static final String TAG_NAME = "name";
    public static final String TAG_RUNTIME = "runtime";
    public static final String TAG_GENRES = "genres";
    public static final String TAG_REVENUE = "revenue";
    public static final String TAG_SIZE = "size";
    public static final String TAG_TYPE = "type";
    public static final String TAG_KEY = "key";
    public static final String TAG_PROFILE_PATH = "profile_path";
    public static final String TAG_PLACE_OF_BIRTH = "place_of_birth";
    public static final String TAG_HOMEPAGE = "homepage";
    public static final String TAG_DEATHDAY = "deathday";
    public static final String TAG_BIRTHDAY = "birthday";
    public static final String TAG_BIOGRAPHY = "biography";
    public static final String TAG_FILE_PATH = "file_path";
    public static final String TAG_PROFILES = "profiles";
    public static final String TAG_CHARACTER = "character";
    public static final String TAG_FIRST_AIR_DATE = "first_air_date";
    public static final String TAG_LAST_AIR_DATE = "last_air_date";
    public static final String TAG_EPISODE_RUN_TIME = "episode_run_time";
    public static final String TAG_STATUS = "status";
    public static final String TAG_CREATED_BY = "created_by";
    public static final String TAG_NETWORKS = "networks";

    public static final String STATUS_ENDED = "Ended";


    /******************************************************************/
    /*                        Public Methods                          */
    /******************************************************************/
    /**
     * Takes the URI passed in and fetches the data from the movie DB. Returns that data
     * as a JSON String
     *
     * @param builtUri URI string use to poll data from movie database
     * @return JSONString of movie data from the query
     */
    public static String queryMovieDatabase(Uri builtUri) {

        String moviesJsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            //Log.e(TAG, builtUri.toString());

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            moviesJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(TAG,"queryMovieDB() Exception: " + e.toString());
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }

            //Return list of Movies
            return moviesJsonStr;
        }


    }

    /**
     * Get list of movies. Uri is required as the sort parameter
     * @param context   Context of calling Activity
     * @param movieUri  URI string use to poll data from movie database
     * @return          ArralyList of Simple Movies
     */
    public static ArrayList<Movie> getMovieList(Context context, Uri movieUri) {

        String movieJsonStr = queryMovieDatabase(movieUri);

        //Error pulling movies, return null
        if(movieJsonStr == null)
            return null;

        //Part data and return list of movies
        return parseJsonMovieList(context,movieJsonStr);
    }

    /**
     * Get single movie
     * @param id Movie id
     * @return   Movie object
     */
    public static Movie getMovie(int id) {

        Uri uri = buildMovieUri(id);
        String jsonStr = MovieDbAPI.queryMovieDatabase(uri);

        if(jsonStr == null)
            return null;
        else
            return parseJsonMovie(jsonStr);
    }

    /**
     * Get a single tv show
     * @param id TV id
     * @return   TV object
     */
    public static TvShow getTvShow(int id) {

        Uri uri = buildTvUri(id);
        String jsonStr = MovieDbAPI.queryMovieDatabase(uri);

        if(jsonStr == null)
            return null;
        else
            return parseJsonTvShow(jsonStr);
    }

    /**
     * Get single person
     * @param id Person id
     * @return   Person object
     */
    public static Person getPerson(int id) {

        Uri uri = buildPersonUri(id);
        String jsonStr = MovieDbAPI.queryMovieDatabase(uri);

        if(jsonStr == null)
            return null;
        else
            return parseJsonPerson(jsonStr);
    }

    /**
     * Get list of videos from a Movie or TV Show
     * @param id Movie id
     * @return   ArrayList of Videos
     */
    public static ArrayList<Review> getReviewList(int id, int entType) {

        Uri reviewUri = buildReviewsUri(id, entType);
        String reviewJsonStr = queryMovieDatabase(reviewUri);

        if(reviewJsonStr == null)
            return null;
        else
            return parseJsonReviewList(reviewJsonStr);
    }

    /**
     * Get list of videos from a Movie
     * @param movieId Movie id
     * @return        ArrayList of Videos
     */
    public static ArrayList<Video> getVideoList(int movieId, int entType) {

        Uri videoUri = buildVideoUri(movieId, entType);
        String videoJsonStr = queryMovieDatabase(videoUri);

        if(videoJsonStr == null)
            return null;
        else
            return parseJsonVideoList(videoJsonStr);
    }

    /**
     * Get list of cast from a Movie
     * @param movieId Movie id
     * @return        ArrayList of credits
     */
    public static ArrayList<Credit> getMovieCastList(int movieId) {

        Uri creditUri = buildMovieCreditsUri(movieId);
        String creditJsonStr = queryMovieDatabase(creditUri);

        if(creditJsonStr == null)
            return null;
        else
            return parseJsonCastList(creditJsonStr);

    }

    /**
     * Get list of cast from a Movie
     * @param movieId Movie id
     * @return        ArrayList of credits
     */
    public static ArrayList<Credit> getTvCastList(int movieId) {

        Uri creditUri = buildTvCreditsUri(movieId);
        String creditJsonStr = queryMovieDatabase(creditUri);

        if(creditJsonStr == null)
            return null;
        else
            return parseJsonCastList(creditJsonStr);

    }

    /**
     * Get list of credits of a person
     * @param personId Person id
     * @return         ArrayList of credits of a person
     */
    public static ArrayList<Credit> getPersonCreditList(int personId,int creditType) {

        Uri creditUri = buildPersonCreditsUri(personId, creditType);
        String creditJsonStr = queryMovieDatabase(creditUri);

        if(creditJsonStr == null)
            return null;
        else
            return parseJsonPersonCreditList(creditJsonStr, creditType);
    }

    /**
     * Get screen size of the device
     * @param context Context of calling activity
     * @return        screen size
     */
    public static int getScreenSize(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        return screenLayout;
    }

    /**
     * Get poster resolution of poster depending on screen size
     * @param context Context of calling activity
     * @return        Poster size to be polled from Movie DB
     */
    public static String getPosterSize(Context context) {

        int screenLayout = getScreenSize(context);

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return IMAGE_185_SIZE;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return IMAGE_342_SIZE;
            default:
                return IMAGE_185_SIZE;
        }

    }

    /**
     * Build Youtube URI for SharedPreferences
     * @param video
     * @return
     */
    public static Uri buildYoutubeUri(Video video) {

        Uri youtubeUri = Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                .appendPath(PATH_WATCH)
                .appendQueryParameter(PARAM_V, video.getKey())
                .build();
        Log.e(TAG,"Youtube = " + youtubeUri.toString());
        return youtubeUri;
    }

    public static Uri buildYoutubeThumbnailUri(Video video) {

        Uri youtubeUri = Uri.parse(BASE_YOUTUBE_THUMB_URL).buildUpon()
                .appendPath(video.getKey())
                .appendPath(YOUTUBE_THUMB_SIZE)
                .build();
       // Log.e(TAG,"Youtube thumb = " + youtubeUri.toString());
        return youtubeUri;
    }
    /*************************************************************************/
    /*                         Private Methods                               */
    /*************************************************************************/

    /*
    /* URI Builder Methods
    /*/

    /**
     * Build URI to get a single movie from the database
     * @param movieId Id of the movie
     * @return        Uri to movie
     */
    private static Uri buildMovieUri(int movieId) {
        //Get Uri for basic movie info
        //Build URI String to query the databaes for a specific movie
        Uri movieUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(MovieDbAPI.PATH_MOVIE)
                .appendPath(String.valueOf(movieId))
                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                .build();

        return movieUri;
    }

    /**
     * Build URI to get a single TV show from the database
     * @param tvId Id of the TV show
     * @return     Uri to TV show
     */
    private static Uri buildTvUri(int tvId) {
        //Get Uri for basic tv info
        //Build URI String to query the databaes for a specific movie
        Uri movieUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(MovieDbAPI.PATH_TV)
                .appendPath(String.valueOf(tvId))
                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                .build();

        return movieUri;
    }

    /**
     * Build URI to get the reviews of a Movie or TV show
     * @param id      Id of the movie or TV show
     * @param entType Type of entertainment; Movie or TV
     * @return        Uri to the reviews of the Movie or TV show
     */
    private static Uri buildReviewsUri(int id, int entType) {

        String entTypePath = "";
        if(entType == ENT_TYPE_MOVIE)
            entTypePath = PATH_MOVIE;
        else
            entTypePath = PATH_TV;

        Uri reviewsUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(entTypePath)
                .appendPath(String.valueOf(id))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                .build();
        Log.e(TAG,"Review URI: " + reviewsUri);
        return reviewsUri;
    }
    /**
     * Build URI to get the videos of a Movie or TV show
     * @param id      Id of the movie or TV show
     * @param entType Type of entertainment; Movie or TV
     * @return        Uri to the videos of the Movie or TV show
     */
    private static Uri buildVideoUri(int id, int entType) {

        String entTypePath = "";
        if(entType == ENT_TYPE_MOVIE)
            entTypePath = PATH_MOVIE;
        else
            entTypePath = PATH_TV;

        Uri videosUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
            .appendPath(entTypePath)
            .appendPath(String.valueOf(id))
            .appendPath(PATH_VIDEOS)
            .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
            .build();
        //Log.e(TAG,"video URI: " + videosUri);
        return videosUri;
    }

    /**
     * Build Uri to get a Person
     * @param personId Id of person
     * @return         Uri to the Person
     */
    private static Uri buildPersonUri(int personId) {

        Uri personUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(MovieDbAPI.PATH_PERSON)
                .appendPath(String.valueOf(personId))
                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                .build();

        return personUri;
    }

    /**
     * Build Uri to Movie credits
     * @param movieId Id of movie
     * @return        Uri to Movie credits
     */
    private static Uri buildMovieCreditsUri(int movieId) {

        //Get Uri for credits of movie
        //Build URI String to query the databaes for the list of credits
        Uri movieCreditUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(MovieDbAPI.PATH_MOVIE)
                .appendPath(String.valueOf(movieId))
                .appendPath(MovieDbAPI.TAG_CREDITS)
                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                .build();

        return movieCreditUri;
    }

    /**
     * Build Uri to TV show credits
     * @param tvId Id of TV show
     * @return     Uri of TV show credits
     */
    private static Uri buildTvCreditsUri(int tvId) {

        //Get Uri for credits of movie
        //Build URI String to query the databaes for the list of credits
        Uri movieCreditUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(MovieDbAPI.PATH_TV)
                .appendPath(String.valueOf(tvId))
                .appendPath(MovieDbAPI.TAG_CREDITS)
                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                .build();

        return movieCreditUri;
    }

    /**
     * Build Uri to a Person's credits
     * @param personId Id of Person
     * @return         Uri to person's credits
     */
    private static Uri buildPersonCreditsUri(int personId,int entType) {

        String entTypePath = "";
        if(entType == ENT_TYPE_MOVIE)
            entTypePath = PATH_MOVIE_CREDIT;
        else
            entTypePath = PATH_TV_CREDIT;

        Uri personCreditsUri = Uri.parse(BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(PATH_PERSON)
                .appendPath(String.valueOf(personId))
                .appendPath(entTypePath)
                .appendQueryParameter(PARAM_API_KEY, API_KEY_MOVIE_DB)
                .build();

        return personCreditsUri;
    }

    /**
     * Parse JSON String of the cast list
     * @param creditJsonStr Json string of cast list
     * @return              return cast list info as a list of Credit Objects
     */
    private static ArrayList<Credit> parseJsonCastList(String creditJsonStr) {

        ArrayList<Credit> castList = null;

        try {
            // Log.e(TAG,"Credits: " + creditJsonStr);
            JSONObject creditObj = new JSONObject(creditJsonStr);

            //Pull out Cast Information
            JSONArray castArray = creditObj.getJSONArray(MovieDbAPI.TAG_CAST);

            castList = new ArrayList<Credit>(castArray.length());

            //Pull out actors of the movie
            for (int j = 0; j < castArray.length(); j++) {
                int id = castArray.getJSONObject(j).getInt(MovieDbAPI.TAG_ID);

                Credit credit = new Credit(id);

                credit.setTitle(castArray.getJSONObject(j).getString(MovieDbAPI.TAG_NAME));
                credit.setCharacter(castArray.getJSONObject(j).getString(MovieDbAPI.TAG_CHARACTER));
                credit.setPosterPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL
                        + MovieDbAPI.IMAGE_185_SIZE + castArray.getJSONObject(j).getString(MovieDbAPI.TAG_PROFILE_PATH));

                castList.add(credit);

            }

         } catch (JSONException e) {
             Log.e(TAG, e.getMessage());
             return null;

         }

        return castList;
    }

    /**
     * Parse JSON string of Person data
     * @param personJsonStr JSON string of person data
     * @return              Person Object containing data of the person
     */
    private static Person parseJsonPerson(String personJsonStr) {

        Person person = null;

        try {
            JSONObject obj = new JSONObject(personJsonStr);

            person = new Person(obj.getInt(MovieDbAPI.TAG_ID));

            person.setName(obj.getString(MovieDbAPI.TAG_NAME));
            person.setBiography(obj.getString(MovieDbAPI.TAG_BIOGRAPHY));
            person.setBirthday(obj.getString(MovieDbAPI.TAG_BIRTHDAY));
            person.setDeathday(obj.getString(MovieDbAPI.TAG_DEATHDAY));
            person.setBirthPlace(obj.getString(MovieDbAPI.TAG_PLACE_OF_BIRTH));
            person.setProfileImagePath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE + obj.getString(MovieDbAPI.TAG_PROFILE_PATH));

            String strPage = obj.getString(MovieDbAPI.TAG_HOMEPAGE);
            strPage = strPage.replace("http://","");
            person.setHomepage(strPage);

        } catch (JSONException e) {
            Log.e(TAG,"Caught JSON exception " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return person;
    }

    /**
     * Parse JSON String of a Person's credits
     * @param personCreditsJsonStr JSON String of Person's credits
     * @param entType              Type of entertainment; Movie or TV
     * @return                     ArrayList of Person's credits
     */
    private static ArrayList<Credit> parseJsonPersonCreditList(String personCreditsJsonStr,int entType) {

        //List of Reviews that get parsed from Movie DB JSON return
        ArrayList<Credit> creditList = null;

        try {
            JSONObject obj = new JSONObject(personCreditsJsonStr);
            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_CAST);

            creditList = new ArrayList<>(resultsArray.length());

            Log.e(TAG, "Credit json: " + personCreditsJsonStr);
            for(int i = 0; i< resultsArray.length(); i++) {

                JSONObject result = resultsArray.getJSONObject(i);
                Credit credit = new Credit(result.getInt(MovieDbAPI.TAG_ID));;
                credit.setCharacter(result.getString(MovieDbAPI.TAG_CHARACTER));
                credit.setPosterPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE
                        + result.getString(MovieDbAPI.TAG_POSTER_PATH));

                //Different release and title tags between movies and tv filmography
                String releaseTag = TAG_RELEASE_DATE;
                String titleTag = TAG_TITLE;
                if(entType == ENT_TYPE_TV) {
                    releaseTag = TAG_FIRST_AIR_DATE;
                    titleTag = TAG_NAME;
                }

                String releaseDate = result.getString(releaseTag);
                credit.setTitle(result.getString(titleTag));

                if((releaseDate != null) && (releaseDate != "") && (releaseDate != "null")) {
                    String dateArray[] = releaseDate.split("-");

                    credit.setReleaseYear(Integer.parseInt(dateArray[0]));
                }
                else
                    Log.e(TAG,"Got null release date");


                creditList.add(credit);
            }

            //Sort array from newest to oldest film
            Collections.sort(creditList);
        } catch (JSONException e) {
            Log.e(TAG,"Caught JSON exception " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return creditList;
    }

    /**
     * Parse JSON String of Movie
     * @param movieJsonStr JSON String of Movie
     * @return             Movie object
     */
    private static Movie parseJsonMovie(String movieJsonStr) {

        Movie movie = null;

        try {
            if (movieJsonStr != null) {
                //Log.e(TAG, "Movie: " + movieJsonStr);


                JSONObject movieObj = new JSONObject(movieJsonStr);

                movie = new Movie(movieObj.getInt(MovieDbAPI.TAG_ID));
                movie.setTitle(movieObj.getString(MovieDbAPI.TAG_TITLE));
                movie.setOverview(movieObj.getString(MovieDbAPI.TAG_OVERVIEW));
                movie.setPosterPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE + movieObj.getString(MovieDbAPI.TAG_POSTER_PATH));
                movie.setBackdropPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_500_SIZE + movieObj.getString(MovieDbAPI.TAG_BACKDROP_PATH));
                movie.setReleaseDate(movieObj.getString(MovieDbAPI.TAG_RELEASE_DATE));
                movie.setRating(movieObj.getDouble(MovieDbAPI.TAG_RATING));
                movie.setRuntime(movieObj.getString(MovieDbAPI.TAG_RUNTIME));

                movie.setGenres(parseList(movieObj, TAG_GENRES, TAG_NAME));

                int iRevenue = movieObj.getInt(MovieDbAPI.TAG_REVENUE);
                movie.setRevenue(NumberFormat.getIntegerInstance().format(iRevenue));

                //Get Uri for credits of movie
                //Build URI String to query the databaes for the list of credits
                Uri creditUri = buildMovieCreditsUri(movie.getId());

                String creditJsonStr = MovieDbAPI.queryMovieDatabase(creditUri);

                if (creditJsonStr != null) {

                    // Log.e(TAG,"Credits: " + creditJsonStr);
                    JSONObject creditObj = new JSONObject(creditJsonStr);

                    //Pull out Crew information
                    JSONArray crewArray = creditObj.getJSONArray(MovieDbAPI.TAG_CREW);

                    ArrayList<IdNamePair> directorList = new ArrayList<IdNamePair>();
                    for (int j = 0; j < crewArray.length(); j++) {
                        String job = crewArray.getJSONObject(j).getString(MovieDbAPI.TAG_JOB);

                        //Find director
                        if (job.equals(MovieDbAPI.TAG_JOB_DIRECTOR)) {
                            IdNamePair director = new IdNamePair(crewArray.getJSONObject(j).getInt(MovieDbAPI.TAG_ID));
                            director.setName(crewArray.getJSONObject(j).getString(MovieDbAPI.TAG_NAME));
                            directorList.add(director);
                        }
                    }
                    //Add director list to movie
                    if (directorList.size() > 0)
                        movie.setDirectors(directorList);

                    //Pull out Cast Information
                    JSONArray castArray = creditObj.getJSONArray(TAG_CAST);
                    ArrayList<IdNamePair> actorNameList = new ArrayList<IdNamePair>();
                    //Pull out actors of the movie
                    for (int j = 0; j < castArray.length(); j++) {
                        //Log.e(TAG, castArray.getJSONObject(j).toString());
                        IdNamePair cast = new IdNamePair(castArray.getJSONObject(j).getInt(TAG_ID));
                        cast.setName(castArray.getJSONObject(j).getString(TAG_NAME));

                        actorNameList.add(cast);
                    }

                    //Add cast list to movie
                    if (actorNameList.size() > 0) {
                        //Log.e(TAG, "Setting actor lists to movie object");

                        movie.setActors(actorNameList);
                    }

                    //Get Videos
                    movie.setVideos(getVideoList(movie.getId(), ENT_TYPE_MOVIE));

                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

        return movie;

    }

    /**
     * Parse JSON String of TV Show
     * @param tvJsonStr Json String of TV Show
     * @return          TVShow object
     */
    public static TvShow parseJsonTvShow(String tvJsonStr) {

        TvShow tvShow = null;

        try {
            if (tvJsonStr != null) {
                //Log.e(TAG, "TV: " + tvJsonStr);


                JSONObject tvObj = new JSONObject(tvJsonStr);

                tvShow = new TvShow(tvObj.getInt(MovieDbAPI.TAG_ID));
                tvShow.setTitle(tvObj.getString(MovieDbAPI.TAG_NAME));
                tvShow.setOverview(tvObj.getString(MovieDbAPI.TAG_OVERVIEW));
                tvShow.setPosterPath(BASE_MOVIE_IMAGE_URL + IMAGE_185_SIZE + tvObj.getString(TAG_POSTER_PATH));
                tvShow.setBackdropPath(BASE_MOVIE_IMAGE_URL + IMAGE_500_SIZE + tvObj.getString(TAG_BACKDROP_PATH));
                tvShow.setReleaseDate(tvObj.getString(TAG_FIRST_AIR_DATE));
                tvShow.setLastAirDate(tvObj.getString(TAG_LAST_AIR_DATE));
                tvShow.setRating(tvObj.getDouble(MovieDbAPI.TAG_RATING));


                tvShow.setCreatedBy(parseList(tvObj, TAG_CREATED_BY, TAG_NAME));

                JSONArray runtimeArray = tvObj.getJSONArray(TAG_EPISODE_RUN_TIME);
                tvShow.setRuntime(String.valueOf(runtimeArray.getInt(0)));

                tvShow.setGenres(parseList(tvObj, TAG_GENRES, TAG_NAME));

                tvShow.setNetworks(parseList(tvObj, TAG_NETWORKS, TAG_NAME));
                //Get status of show
                tvShow.setStatus(tvObj.getString(TAG_STATUS));
                //Get Uri for credits of tv show
                //Build URI String to query the databaes for the list of credits
                Uri creditUri = buildTvCreditsUri(tvShow.getId());

                String creditJsonStr = queryMovieDatabase(creditUri);

                if (creditJsonStr != null) {

                    // Log.e(TAG,"Credits: " + creditJsonStr);
                    JSONObject creditObj = new JSONObject(creditJsonStr);

                    //Pull out Cast Information
                    JSONArray castArray = creditObj.getJSONArray(TAG_CAST);
                    ArrayList<IdNamePair> actorNameList = new ArrayList<>(castArray.length());
                    //Pull out actors of the movie
                    for (int j = 0; j < castArray.length(); j++) {
                        //Log.e(TAG, castArray.getJSONObject(j).toString());
                        IdNamePair cast = new IdNamePair(castArray.getJSONObject(j).getInt(TAG_ID));
                        cast.setName(castArray.getJSONObject(j).getString(TAG_NAME));

                        actorNameList.add(cast);
                    }

                    //Add cast list to movie
                    if (actorNameList.size() > 0) {
                        //Log.e(TAG, "Setting actor lists to movie object");
                        tvShow.setActors(actorNameList);
                    }
                }

                //Get Videos
                tvShow.setVideos(getVideoList(tvShow.getId(), ENT_TYPE_TV));
            }
        }
        catch (JSONException e) {
            Log.e(TAG,"Caught JSON exception " + e.getMessage());
        }

        return tvShow;
    }

    /**
     * Parses the JSON String returned from the query to the Movie DB. Pulls data out for
     * a each movie returned and puts that data into a Movie object. These movie objects are
     * returned in an ARRAYList
     * @param context       Context of the calling Activity
     * @param moviesJsonStr Full return JSON String of movie data
     * @return              ArrayList of Movies
     */
    private static ArrayList<Movie> parseJsonMovieList(Context context, String moviesJsonStr) {

        //List of Movies that get parsed Movie DB JSON return
        ArrayList<Movie> movieList = null;

        try {
            JSONObject obj = new JSONObject(moviesJsonStr);

            //Get JSONArray List of all movies
            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_RESULTS);

            //Log.e(TAG,"Results Array: " + resultsArray.get(0));

            //Initialize movie array list by the number of movies returned in the query
            movieList = new ArrayList<>(resultsArray.length());

            //Loop through all of the movies returned in the query
            for(int i=0;i <resultsArray.length(); i++) {

                //Get Movie result. Create movie object and pull out particular data for
                //that movie.
                JSONObject result = resultsArray.getJSONObject(i);
               // Log.e(TAG,"MOVIE Result: " + result.toString());

                 int movieId = result.getInt(MovieDbAPI.TAG_ID);
                 Movie movie = getMovie(movieId);

                //Add movie to movie list array.
                if(movie != null)
                   movieList.add(movie);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieList;
    }

    /**
     * Parse JSON String of Reviews
     * @param reviewsJsonStr JSON String of reviews
     * @return               ArrayList of reviews
     */
    private static ArrayList<Review> parseJsonReviewList(String reviewsJsonStr) {

        //List of Reviews that get parsed from Movie DB JSON return
        ArrayList<Review> reviewList = null;
        //Log.e(TAG,"REviewJson: " +reviewsJsonStr);
        try {
            JSONObject obj = new JSONObject(reviewsJsonStr);
            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_RESULTS);

            reviewList = new ArrayList<>(resultsArray.length());

            for(int i = 0; i< resultsArray.length(); i++) {

                JSONObject result = resultsArray.getJSONObject(i);
                Review review = new Review();;
                review.setAuthor(result.getString(MovieDbAPI.TAG_AUTHOR));
                review.setContent(result.getString(MovieDbAPI.TAG_CONTENT));

                Log.e(TAG, review.toString());
                reviewList.add(review);
            }
        } catch (JSONException e) {
            Log.e(TAG,"Caught JSON exception " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return reviewList;
    }
    /**
     * Parses the JSON String returned from the query to the Movie DB. Pulls data out for
     * a each video/trailers returned and puts that data into a Video object. These video objects are
     * returned in an ARRAYList
     *
     * @param videosJsonStr
     * @return Full return JSON String of video data
     */
    private static ArrayList<Video> parseJsonVideoList(String videosJsonStr) {

        ArrayList<Video> videoList = null;
       // Log.e(TAG,"videoJson: " + videosJsonStr);

        try {
            JSONObject obj = new JSONObject(videosJsonStr);
            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_RESULTS);

            videoList = new ArrayList<>(resultsArray.length());

            for(int i = 0; i< resultsArray.length(); i++) {

                JSONObject result = resultsArray.getJSONObject(i);
                Video video = new Video(result.getString(MovieDbAPI.TAG_KEY));
                video.setName(result.getString(MovieDbAPI.TAG_NAME));
                video.setType(result.getString(MovieDbAPI.TAG_TYPE));
                video.setSize(result.getInt(MovieDbAPI.TAG_SIZE));

                Log.e(TAG, video.getName());
                videoList.add(video);
            }
        } catch (JSONException e) {
            Log.e(TAG,"Caught JSON exception " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return videoList;
    }

    /**
     * Parse a JSON Array and pull out date from one element in the array
     * @param obj      JSON Object containing Array
     * @param arrayTag Name of JSON Array in obj
     * @param innerTag Name of element in the array to pull out
     * @return         ArrayList of String elements pulled from the array
     */
    private static ArrayList<String> parseList(JSONObject obj,String arrayTag, String innerTag) {

        ArrayList<String> list = null;
        try {
            JSONArray jsonArray = obj.getJSONArray(arrayTag);

            //Get genres of the movie
            list = new ArrayList<>(jsonArray.length());
            for (int j = 0; j < jsonArray.length(); j++) {
                list.add(jsonArray.getJSONObject(j).getString(innerTag));
            }
        }
        catch (JSONException e) {
            Log.e(TAG,"Caught JSON exception " + e.getMessage());
        }

        return list;
    }
}
