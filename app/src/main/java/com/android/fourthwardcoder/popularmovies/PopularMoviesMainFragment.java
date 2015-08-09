package com.android.fourthwardcoder.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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
    public static final String EXTRA_MOVIE = "com.android.fourthwardcoder.popularmovies.extra_movie";

    private static final String PICASSO_API_KEY = APIKeys.PICASSO_API_KEY;
    private static final String BASE_DISCOVER_URL = "http://api.themoviedb.org/3/discover/movie";
    private static final String BASE_GENRE_URL = "http://api.themoviedb.org/3/genre/movie/list";
    private static final String BASE_MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String SORT_PARM = "sort_by";
    private static final String API_KEY_PARAM = "api_key";

    private static final String PREF_SORT = "sort";
    //Tag for the time Sort Dialog
    private static final String DIALOG_SORT = "dialogSort";

    //Constant for request code to Sort Dialog
    public static final int REQUEST_SORT = 0;
    /**************************************************/
	/*                Local Data                      */
    /**************************************************/
    GridView mGridView;
    ArrayList<Movie> mMovieList;
    //ArrayList<Pair<Integer,String>> mGenreList;
    String mSortOrder;
    SharedPreferences.Editor prefsEditor;

    public PopularMoviesMainFragment() {
    }

    /**************************************************/
    /*               Override Methods                 */
    /**************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retain fragment across Activity re-creation
        setRetainInstance(true);

        //Set Option Menu
        setHasOptionsMenu(true);

        //Get instanace of Shared Preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
                .getApplicationContext());
        prefsEditor = sharedPrefs.edit();

        //Get list of sort types from array resource
        Resources res = getResources();
        String[] sortList = res.getStringArray(R.array.sort_url_list);

        //Get stored sort preference from shared resources. If non exists set it to sort by
        //popularity.
        mSortOrder = sharedPrefs.getString(PREF_SORT,sortList[0]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Get main Gridview and set up click listener
        mGridView = (GridView)view.findViewById(R.id.gridView);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get selected movie from the GridView
                Movie movie = mMovieList.get(position);
                //Start intent to bring up Details Activity
                Intent i = new Intent(getActivity(),MovieDetailActivity.class);
                i.putExtra(EXTRA_MOVIE, movie);
                startActivity(i);
            }
        });
        //If there is a gridview then start up thread to pull in movie data. Send in sort
        //type.
        if(mGridView != null)
            new FetchPhotosTask().execute(mSortOrder);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);

        //Pass the resource ID of the menu and populate the Menu
        //instance with the items defined in the xml file
        inflater.inflate(R.menu.menu_sort, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_item_sort:
                //Get support Fragment Manager
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                SortDialogFragment dialog = SortDialogFragment.newInstance(mSortOrder);
                //Make PopularMoviesMainFragment the target fragment of the SortDialogFragment instance
                dialog.setTargetFragment(PopularMoviesMainFragment.this, REQUEST_SORT);
                dialog.show(fm, DIALOG_SORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Get results from Dialog boxes and other Activities
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_SORT) {
            //Get change in sort from dialog and store it in Shared Preferences
            mSortOrder = data.getStringExtra(SortDialogFragment.EXTRA_SORT);
            prefsEditor.putString(PREF_SORT,mSortOrder);
            prefsEditor.commit();

            //Fetch new set of movies based on sort order
            if(mGridView != null)
                new FetchPhotosTask().execute(mSortOrder);

        }
    }
    /*****************************************************/
    /*                Inner Classes                      */
    /*****************************************************/
    /**
     * Class FetchPhotosTask
     *
     */
    private class FetchPhotosTask extends AsyncTask<String,Void,ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            //We only pass one param for the sort order, so get the first one.
            String sortOrder = params[0];

            //Build URI String to query the database for a list of movies
            Uri movieUri = Uri.parse(BASE_DISCOVER_URL).buildUpon()
                    .appendQueryParameter(SORT_PARM, sortOrder)
                    .appendQueryParameter(API_KEY_PARAM, PICASSO_API_KEY)
                    .build();

            String movieJsonStr = queryMovieDatabase(movieUri);

            //Error pulling movies, return null
            if(movieJsonStr == null)
                return null;

            return parseJsonMovies(movieJsonStr);

        }

        /**
         * queryMoviewDatabase()
         * @param builtUri: URI string use to poll data from movie database
         * @return
         */
        private String queryMovieDatabase(Uri builtUri) {

            String moviesJsonStr = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

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

        /**
         *
         * @param moviesJsonStr
         * @return
         */
        private ArrayList<Movie> parseJsonMovies(String moviesJsonStr) {

            final String TAG_RESULTS = "results";
            final String TAG_ID = "id";
            final String TAG_TITLE = "title";
            final String TAG_OVERVIEW = "overview";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_BACKDROP_PATH = "backdrop_path";
            final String TAG_RELEASE_DATE = "release_date";
            final String TAG_RATING = "vote_average";
            final String TAG_GENRE_IDS = "genre_ids";
            final String TAG_CREDITS = "credits";
            final String TAG_CAST = "cast";
            final String TAG_CREW = "crew";
            final String TAG_JOB = "job";
            final String TAG_JOB_DIRECTOR = "Director";
            final String TAG_NAME = "name";
            final String TAG_RUNTIME = "runtime";
            final String TAG_GENRES = "genres";

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

                //Log.e(TAG,"Results Array: " + resultsArray.get(0));

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
                    movie.setRating(result.getDouble(TAG_RATING));


                    //Get Uri for basic movie info
                    //Build URI String to query the databaes for the list of genres
                    Uri movieUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                            .appendPath(String.valueOf(movie.getId()))
                            .appendQueryParameter(API_KEY_PARAM, PICASSO_API_KEY)
                            .build();

                    String movieJsonStr = queryMovieDatabase(movieUri);
                    if(movieJsonStr != null) {
                        //Log.e(TAG, "Movie: " + movieJsonStr);

                        JSONObject movieObj = new JSONObject(movieJsonStr);
                        movie.setRuntime(movieObj.getString(TAG_RUNTIME));
                        JSONArray genreArray = movieObj.getJSONArray(TAG_GENRES);

                        ArrayList<String> genreList = new ArrayList<String>();
                        for(int j = 0; j<genreArray.length();j++) {
                            String genre = genreArray.getJSONObject(j).getString(TAG_NAME);
                            genreList.add(genre);
                        }

                        movie.setGenreList(genreList);
                    }
                    //Get Uri for credits of movie
                    //Build URI String to query the databaes for the list of genres
                    Uri creditUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                            .appendPath(String.valueOf(movie.getId()))
                            .appendPath(TAG_CREDITS)
                            .appendQueryParameter(API_KEY_PARAM, PICASSO_API_KEY)
                            .build();

                    String creditJsonStr = queryMovieDatabase(creditUri);

                    if(creditJsonStr != null) {

                        // Log.e(TAG,"Credits: " + creditJsonStr);
                        JSONObject creditObj = new JSONObject(creditJsonStr);

                        //Pull out Crew information
                        JSONArray crewArray = creditObj.getJSONArray(TAG_CREW);

                        ArrayList<String> directorList = new ArrayList<String>();
                        for (int j = 0; j < crewArray.length(); j++) {
                            String job = crewArray.getJSONObject(j).getString(TAG_JOB);

                            //Find director
                            if (job.equals(TAG_JOB_DIRECTOR))
                                directorList.add(crewArray.getJSONObject(j).getString(TAG_NAME));

                        }
                        //Add director list to movie
                        if (directorList.size() > 0)
                            movie.setDirectors(directorList);

                        //Pull out Cast Information
                        JSONArray castArray = creditObj.getJSONArray(TAG_CAST);
                        ArrayList<String> actorList = new ArrayList<String>();

                        for (int j = 0; j < castArray.length(); j++) {
                            String name = castArray.getJSONObject(j).getString(TAG_NAME);

                            actorList.add(name);

                        }

                        //Add cast list to movie
                        if (actorList.size() > 0)
                            movie.setActors(actorList);
                    }
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

            if(getActivity() != null && mGridView != null) {

                if(movieList != null) {
                    MovieImageAdapter adapter = new MovieImageAdapter(movieList);
                    mGridView.setAdapter(adapter);
                }
                else {
                    Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                    connectToast.show();
                }

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
