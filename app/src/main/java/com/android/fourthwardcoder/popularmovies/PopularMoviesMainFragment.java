package com.android.fourthwardcoder.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.util.Pair;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesMainFragment extends Fragment {


    /**************************************************/
	/*                 Constants                      */
    /**************************************************/
    private static final String TAG = PopularMoviesMainFragment.class.getSimpleName();
    public static final String EXTRA_MOVIE = "extra_movie";

    private static final String PICASSO_API_KEY = "e80f27e43348054952d67e7d0353ac38";
    private static final String BASE_MOVE_URL = "http://api.themoviedb.org/3/discover/movie";
    private static final String BASE_GENRE_URL = "http://api.themoviedb.org/3/genre/movie/list";
    private static final String SORT_PARM = "sort_by";
    private static final String API_KEY_PARAM = "api_key";
    private static final String sortOrder = "popularity.desc";

    /**************************************************/
	/*                Local Data                      */
    /**************************************************/
    GridView mGridView;
    ArrayList<Movie> mMovieList;

    public PopularMoviesMainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retain fragment across Activity re-creation
        setRetainInstance(true);

        //Set Option Menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView)view.findViewById(R.id.gridView);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get selected moview from the GridView
                Movie movie = mMovieList.get(position);
                //Start intent to bring up Details Activity
                Intent i = new Intent(getActivity(),MovieDetailActivity.class);
                i.putExtra(EXTRA_MOVIE, movie);
                startActivity(i);
            }
        });

        if(mGridView != null)
            new FetchPhotosTask().execute();

        return view;
    }

    private class FetchPhotosTask extends AsyncTask<Void,Void,ArrayList<Movie>> {

        private String moviesJsonStr;


        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                populateGenreTable();

                Uri builtUri = Uri.parse(BASE_MOVE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARM, sortOrder)
                        .appendQueryParameter(API_KEY_PARAM, PICASSO_API_KEY)
                        .build();

                Log.e(TAG, builtUri.toString());

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

                Log.e(TAG, moviesJsonStr);

            }
            catch(IOException e) {
                Log.e(TAG,e.getMessage());
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
                return parseJsonMovies(moviesJsonStr);
            }

        }

        private void populateGenreTable() {

            ArrayList<Pair> genreList;

            //Get genres http://api.themoviedb.org/3/genre/movie/list?api_key=e80f27e43348054952d67e7d0353ac38



        }
        private ArrayList<Movie> parseJsonMovies(String moviesJsonStr) {

            final String TAG_RESULTS = "results";
            final String TAG_ID = "id";
            final String TAG_TITLE = "title";
            final String TAG_OVERVIEW = "overview";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_BACKDROP_PATH = "backdrop_path";
            final String TAG_RELEASE_DATE = "release_date";
            final String TAG_RATING = "vote_average";

            final String BASE_URL = "http://image.tmdb.org/t/p/";
            final String IMAGE_185_SIZE = "w185/";
            final String IMAGE_342_SIZE = "w342/";
            final String IMAGE_500_SIZE = "w500/";

            ArrayList<Movie> movieList = null;

            try {
                JSONObject obj = new JSONObject(moviesJsonStr);

                //Get Array List o
                // f all movies
                JSONArray resultsArray = obj.getJSONArray(TAG_RESULTS);

                Log.e(TAG,"Results Array: " + resultsArray.get(0));

                movieList = new ArrayList<>(resultsArray.length());

                for(int i=0;i <resultsArray.length(); i++) {

                    JSONObject result = resultsArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setId(result.getInt(TAG_ID));
                    movie.setTitle(result.getString(TAG_TITLE));
                    movie.setOverview(result.getString(TAG_OVERVIEW));
                    movie.setPosterPath(BASE_URL + IMAGE_185_SIZE + result.getString(TAG_POSTER_PATH));
                    movie.setBackdropPath(BASE_URL + IMAGE_500_SIZE + result.getString(TAG_BACKDROP_PATH));
                    movie.setReleaseDate(result.getString(TAG_RELEASE_DATE));
                    movie.setRating(result.getInt(TAG_RATING));

                    //Add movie to movie list array.
                    movieList.add(movie);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieList) {

            String strTitle = movieList.get(0).getTitle();
            Log.e(TAG, "OnPostExecute with first moview " + strTitle);

            if(getActivity() != null && mGridView != null) {
                MovieImageAdapter adapter = new MovieImageAdapter(movieList);
                mGridView.setAdapter(adapter);
            }
        }
    }

    private class MovieImageAdapter extends ArrayAdapter<Movie> {

        public MovieImageAdapter(ArrayList<Movie> movies) {
            super(getActivity(),0,movies);

            //Store Global List of movies
            mMovieList = movies;
        }

        //Override the getView to return an ImageView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.movie_image, parent, false);
            }

            //Get imageView
            ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_imageView);


            //Get each Movie using the position in the ArrayAdapter
            Movie movie = getItem(position);

            //Call Picasso to load it into the imageView
            Picasso.with(getActivity()).load(movie.getPosterPath()).into(imageView);

            return convertView;
        }
    }

}
