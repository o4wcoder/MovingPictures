package com.android.fourthwardcoder.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chare on 8/25/2015.
 */
public class DBUtil {

    /*******************************************************************************/
    /*                                Constants                                    */
    /*******************************************************************************/
    private static final String TAG = DBUtil.class.getSimpleName();

    public static final String NUM_QUERY_PAGES = "3";

    public static final String BASE_MOVIE_URL = "http://api.themoviedb.org/"+ NUM_QUERY_PAGES +"/movie/";

    public static final String API_KEY_PARAM = "api_key";

    //JSON TAGS
    public static final String TAG_RESULTS = "results";
    public static final String TAG_ID = "id";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_CONTENT = "content";
    /**
     * Takes the URI passed in and fetches the data from the movie DB. Returns that data
     * as a JSON String
     *
     * @param builtUri URI string use to poll data from movie database
     * @return         JSONString of movie data from the query
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
            moviesJsonStr = buffer.toString();;
        }
        catch(IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        finally {
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
}
