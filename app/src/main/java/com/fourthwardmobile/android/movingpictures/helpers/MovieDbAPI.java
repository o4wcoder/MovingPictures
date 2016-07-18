package com.fourthwardmobile.android.movingpictures.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;

import com.fourthwardmobile.android.movingpictures.BuildConfig;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.interfaces.MovieService;

import com.fourthwardmobile.android.movingpictures.models.Video;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class MovieDbAPI
 * Author: Chris Hare
 * Created: 8/25/2015.
 *
 * Helper class for doing all access to the Movies DB API. This contains all defines for URLs,
 * paths, and JSON tags. Contains helper methods polling the MovieOld DB and returning data.
 */
public class MovieDbAPI implements Constants {

    /*******************************************************************************/
    /*                                Constants                                    */
    /**
     * ***************************************************************************
     */
    private static final String TAG = MovieDbAPI.class.getSimpleName();

    public static final String NUM_QUERY_PAGES = "3";

    //API Key for the Movies DB API.
    public static final String API_KEY_MOVIE_DB = BuildConfig.MOVIE_DB_API_KEY;
    //Base URLs
    public static final String MOVIE_DB_URL = "http://api.themoviedb.org/";
    public static final String MOVIE_DB_HTTP_URL = "https://www.themoviedb.org/";
    public static final String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/" + NUM_QUERY_PAGES;
    //Full URLs for the movie DB
    public static final String BASE_DISCOVER_URL = "http://api.themoviedb.org/" + NUM_QUERY_PAGES + "/discover/movie";
    //Base URL for movie images
    public static final String BASE_MOVIE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/";
    public static final String BASE_YOUTUBE_THUMB_URL = "http://img.youtube.com/vi/";

    //MovieOld image sizes
    public static final String IMAGE_185_SIZE = "w185/";
    public static final String IMAGE_342_SIZE = "w342/";
    public static final String IMAGE_500_SIZE = "w500/";

    //US Certificatiaon (Movie Rating)
    public static final String CERT_US = "US";

    //Extra append paths for the movie URI
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TV = "tv";
    public static final String PATH_UPCOMING = "upcoming";
    public static final String PATH_NOW_PLAYING = "now_playing";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_AIRING_TODAY = "airing_today";
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

    //Extra append params for youtube
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
    public static final String TAG_JOB_WRITER = "Writer";
    public static final String TAG_DEPARTMENT_DIRECTING = "Directing";
    public static final String TAG_DEPARTMENT_WRITING = "Writing";
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
    public static final String STATUS_CANCELED = "Canceled";




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
     * @return        Poster size to be polled from MovieOld DB
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


    public static String getSmallFullPosterPath(String imageFile) {
        return MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE + imageFile;
    }
    public static String getFullPosterPath(String imageFile) {

        return MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_342_SIZE + imageFile;
    }

    public static String getFullBackdropPath(String imageFile) {

        return MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_500_SIZE + imageFile;
    }

    /*********************************************************************************************/
    /*                                    Retrofit Calls                                         */
    /*********************************************************************************************/
    public static MovieService getMovieApiService() {

        //Intercept the OkHttp Call from Retrofit and append the API Key to ever request
        OkHttpClient httpClient =  new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(MovieDbAPI.PARAM_API_KEY,MovieDbAPI.API_KEY_MOVIE_DB)
                        .build();

                Log.e(TAG,"URL with API_KEY = " + url);
                //Append the API key to the original URL
                Request.Builder requestBuilder = original.newBuilder().url(url).method(original.method(),
                        original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDbAPI.MOVIE_DB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(MovieService.class);
    }

}
