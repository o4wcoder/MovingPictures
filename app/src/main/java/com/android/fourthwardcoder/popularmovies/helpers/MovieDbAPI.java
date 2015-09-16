package com.android.fourthwardcoder.popularmovies.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;

import com.android.fourthwardcoder.popularmovies.interfaces.APIKeys;
import com.android.fourthwardcoder.popularmovies.models.Credit;
import com.android.fourthwardcoder.popularmovies.models.Movie;
import com.android.fourthwardcoder.popularmovies.models.Person;
import com.android.fourthwardcoder.popularmovies.models.SimpleMovie;
import com.android.fourthwardcoder.popularmovies.models.Video;

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
public class MovieDbAPI {

    /*******************************************************************************/
    /*                                Constants                                    */
    /**
     * ***************************************************************************
     */
    private static final String TAG = MovieDbAPI.class.getSimpleName();

    public static final String NUM_QUERY_PAGES = "3";

    //API Key for the Movies DB API. Replace "APIKeys.MOVIE_DB_API_KEY" with your own API Key
    public static final String API_KEY_MOVIE_DB = APIKeys.MOVIE_DB;
    //Base URLs
    public static final String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/" + NUM_QUERY_PAGES;
    //Full URLs for the movie DB
    public static final String BASE_DISCOVER_URL = "http://api.themoviedb.org/" + NUM_QUERY_PAGES + "/discover/movie";
    //Base URL for movie images
    public static final String BASE_MOVIE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/";

    //Movie image sizes
    public static final String IMAGE_185_SIZE = "w185/";
    public static final String IMAGE_342_SIZE = "w342/";
    public static final String IMAGE_500_SIZE = "w500/";

    //Extra append paths for the movie URI
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_UPCOMING = "upcoming";
    public static final String PATH_NOW_PLAYING = "now_playing";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_PERSON = "person";
    public static final String PATH_IMAGES = "images";
    public static final String PATH_TAGGED_IMAGES = "tagged_images";
    public static final String PATH_MOVIE_CREDIT = "movie_credits";

    //Extra append patch for youtube
    public static final String PATH_WATCH = "watch";

    //Extra append params for the movie URI
    public static final String PARAM_SORT = "sort_by";
    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_WITH_CAST = "with_cast";

    //Extra append params for youtbue
    public static final String PARAM_V = "v";
    //private static final String PARM_CERT_COUNTRY = "certification_country";

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
            ;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
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
     *
     * @param context   Context of calling Activity
     * @param movieUri  URI string use to poll data from movie database
     * @return          ArralyList of Simple Movies
     */
    public static ArrayList<SimpleMovie> getMovieList(Context context, Uri movieUri) {

        String movieJsonStr = queryMovieDatabase(movieUri);

        //Error pulling movies, return null
        if(movieJsonStr == null)
            return null;

        //Part data and return list of movies
        return parseJsonMovieList(context,movieJsonStr);
    }

    /**
     *
     * @param id
     * @return
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
     *
     * @param id
     * @return
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
     *
     * @param movieId
     * @return
     */
    public static ArrayList<Video> getVideoList(int movieId) {

        Uri videoUri = buildVideoUri(movieId);
        String videoJsonStr = queryMovieDatabase(videoUri);

        if(videoJsonStr == null)
            return null;
        else
            return parseJsonVideoList(videoJsonStr);
    }

    /**
     *
     * @param movieId
     * @return
     */
    public static ArrayList<Credit> getCastList(int movieId) {

        Uri creditUri = buildMovieCreditsUri(movieId);
        String creditJsonStr = queryMovieDatabase(creditUri);

        if(creditJsonStr == null)
            return null;
        else
            return parseJsonCastList(creditJsonStr);

    }

    /**
     *
     * @param personId
     * @return
     */
    public static ArrayList<Credit> getPersonCreditList(int personId) {

        Uri creditUri = buildPersonCreditsUri(personId);
        String creditJsonStr = queryMovieDatabase(creditUri);

        if(creditJsonStr == null)
            return null;
        else
            return parseJsonPersonCreditList(creditJsonStr);
    }

    /**
     *
     * @param context
     * @return
     */
    public static int getScreenSize(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        return screenLayout;
    }

    /**
     *
     * @param context
     * @return
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

    /*************************************************************************/
    /*                         Private Methods                               */
    /*************************************************************************/

    /*
    /* URI Builder Methods
    /*/

    /**
     *
     * @param movieId
     * @return
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
     *
     * @param movieId
     * @return
     */
    private static Uri buildVideoUri(int movieId) {

        Uri videosUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
            .appendPath(MovieDbAPI.PATH_MOVIE)
            .appendPath(String.valueOf(movieId))
            .appendPath(MovieDbAPI.PATH_VIDEOS)
            .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
            .build();

        return videosUri;
    }

    /**
     *
     * @param personId
     * @return
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
     *
     * @param movieId
     * @return
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
     *
     * @param personId
     * @return
     */
    private static Uri buildPersonCreditsUri(int personId) {

        Uri personCreditsUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(MovieDbAPI.PATH_PERSON)
                .appendPath(String.valueOf(personId))
                .appendPath(MovieDbAPI.PATH_MOVIE_CREDIT)
                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                .build();

        return personCreditsUri;
    }

    /**
     *
     * @param creditJsonStr
     * @return
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
     *
     * @param personJsonStr
     * @return
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
            person.setHomepage(obj.getString(MovieDbAPI.TAG_HOMEPAGE));



        } catch (JSONException e) {
            Log.e(TAG,"Caught JSON exception " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return person;
    }

    /**
     *
     * @param personCreditsJsonStr
     * @return
     */
    private static ArrayList<Credit> parseJsonPersonCreditList(String personCreditsJsonStr) {

        //List of Reviews that get parsed from Movie DB JSON return
        ArrayList<Credit> creditList = null;

        try {
            JSONObject obj = new JSONObject(personCreditsJsonStr);
            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_CAST);

            creditList = new ArrayList<>(resultsArray.length());

            for(int i = 0; i< resultsArray.length(); i++) {

                JSONObject result = resultsArray.getJSONObject(i);
                Credit credit = new Credit(result.getInt(MovieDbAPI.TAG_ID));;
                credit.setCharacter(result.getString(MovieDbAPI.TAG_CHARACTER));
                credit.setPosterPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE
                        + result.getString(MovieDbAPI.TAG_POSTER_PATH));

                String releaseDate = result.getString(MovieDbAPI.TAG_RELEASE_DATE);
                Log.e(TAG,"Release Date: " + releaseDate);
                if((releaseDate != null) && (releaseDate != "") && (releaseDate != "null")) {
                    String dateArray[] = releaseDate.split("-");

                    credit.setReleaseYear(Integer.parseInt(dateArray[0]));
                }
                else
                    Log.e(TAG,"Got null release date");
                credit.setTitle(result.getString(MovieDbAPI.TAG_TITLE));

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
     *
     * @param movieJsonStr
     * @return
     */
    private static Movie parseJsonMovie(String movieJsonStr) {

        Movie movie = new Movie();

        try {
            if (movieJsonStr != null) {
                Log.e(TAG, "Movie: " + movieJsonStr);


                JSONObject movieObj = new JSONObject(movieJsonStr);

                movie.setId(movieObj.getInt(MovieDbAPI.TAG_ID));
                movie.setTitle(movieObj.getString(MovieDbAPI.TAG_TITLE));
                movie.setOverview(movieObj.getString(MovieDbAPI.TAG_OVERVIEW));
                movie.setPosterPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE + movieObj.getString(MovieDbAPI.TAG_POSTER_PATH));
                movie.setBackdropPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_500_SIZE + movieObj.getString(MovieDbAPI.TAG_BACKDROP_PATH));
                movie.setReleaseDate(movieObj.getString(MovieDbAPI.TAG_RELEASE_DATE));
                movie.setRating(movieObj.getDouble(MovieDbAPI.TAG_RATING));
                movie.setRuntime(movieObj.getString(MovieDbAPI.TAG_RUNTIME));
                JSONArray genreArray = movieObj.getJSONArray(MovieDbAPI.TAG_GENRES);

                //Get genres of the movie
                ArrayList<String> genreList = new ArrayList<String>();
                for (int j = 0; j < genreArray.length(); j++) {
                    String genre = genreArray.getJSONObject(j).getString(MovieDbAPI.TAG_NAME);
                    genreList.add(genre);
                }

                movie.setGenreList(genreList);

                int iRevenue = movieObj.getInt(MovieDbAPI.TAG_REVENUE);
                movie.setRevenue(NumberFormat.getIntegerInstance().format(iRevenue));
            }
            //Get Uri for credits of movie
            //Build URI String to query the databaes for the list of credits
            Uri creditUri = buildMovieCreditsUri(movie.getId());

            String creditJsonStr = MovieDbAPI.queryMovieDatabase(creditUri);

            if (creditJsonStr != null) {

                // Log.e(TAG,"Credits: " + creditJsonStr);
                JSONObject creditObj = new JSONObject(creditJsonStr);

                //Pull out Crew information
                JSONArray crewArray = creditObj.getJSONArray(MovieDbAPI.TAG_CREW);

                ArrayList<String> directorList = new ArrayList<String>();
                for (int j = 0; j < crewArray.length(); j++) {
                    String job = crewArray.getJSONObject(j).getString(MovieDbAPI.TAG_JOB);

                    //Find director
                    if (job.equals(MovieDbAPI.TAG_JOB_DIRECTOR))
                        directorList.add(crewArray.getJSONObject(j).getString(MovieDbAPI.TAG_NAME));

                }
                //Add director list to movie
                if (directorList.size() > 0)
                    movie.setDirectors(directorList);

                //Pull out Cast Information
                JSONArray castArray = creditObj.getJSONArray(MovieDbAPI.TAG_CAST);
                ArrayList<Integer> actorIdList = new ArrayList<Integer>();
                ArrayList<String> actorNameList = new ArrayList<String>();
                //Pull out actors of the movie
                for (int j = 0; j < castArray.length(); j++) {
                    Log.e(TAG,castArray.getJSONObject(j).toString());
                    int id = castArray.getJSONObject(j).getInt(MovieDbAPI.TAG_ID);
                    String name = castArray.getJSONObject(j).getString(MovieDbAPI.TAG_NAME);

                    actorIdList.add(id);
                    actorNameList.add(name);
                }

                //Add cast list to movie
                if ((actorIdList.size() > 0) && (actorNameList.size() > 0))
                    Log.e(TAG, "Setting actor lists to movie object");
                movie.setActorIds(actorIdList);
                movie.setActorNames(actorNameList);

                //Get Videos
                movie.setVideoList(getVideoList(movie.getId()));

            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

        return movie;

    }

    /**
     * Parses the JSON String returned from the query to the Movie DB. Pulls data out for
     * a each movie returned and puts that data into a Movie object. These movie objects are
     * returned in an ARRAYList
     * @param context       Context of the calling Activity
     * @param moviesJsonStr Full return JSON String of movie data
     * @return              ArrayList of Movies
     */
    private static ArrayList<SimpleMovie> parseJsonMovieList(Context context, String moviesJsonStr) {

        //List of Movies that get parsed Movie DB JSON return
        ArrayList<SimpleMovie> movieList = null;

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
                int movieId = result.getInt(MovieDbAPI.TAG_ID);
                String posterPath = MovieDbAPI.BASE_MOVIE_IMAGE_URL + getPosterSize(context) + result.getString(MovieDbAPI.TAG_POSTER_PATH);
                Log.e(TAG,"PosterPath: " + posterPath);
                SimpleMovie movie = new SimpleMovie(movieId,posterPath);

                //Add movie to movie list array.
                movieList.add(movie);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieList;
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

        try {
            JSONObject obj = new JSONObject(videosJsonStr);
            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_RESULTS);

            videoList = new ArrayList<>(resultsArray.length());

            for(int i = 0; i< resultsArray.length(); i++) {

                JSONObject result = resultsArray.getJSONObject(i);
                Video video = new Video();;
                video.setName(result.getString(MovieDbAPI.TAG_NAME));
                video.setKey(result.getString(MovieDbAPI.TAG_KEY));
                video.setType(result.getString(MovieDbAPI.TAG_TYPE));
                video.setSize(result.getInt(MovieDbAPI.TAG_SIZE));

                //Log.e(TAG, review.toString());
                videoList.add(video);
            }
        } catch (JSONException e) {
            Log.e(TAG,"Caught JSON exception " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return videoList;
    }

}
