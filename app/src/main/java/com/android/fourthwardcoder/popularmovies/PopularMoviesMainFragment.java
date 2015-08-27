package com.android.fourthwardcoder.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
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

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Class PopularMoviesMainFragment
 * Author: Chris Hare
 * Created: 7/25/2015
 *
 * Main Fragment of the Popular Movies App
 */
public class PopularMoviesMainFragment extends Fragment {


    /**************************************************/
	/*                 Constants                      */
    /**************************************************/
    //Log tag used for debugging
    private static final String TAG = PopularMoviesMainFragment.class.getSimpleName();

    //Extra for passing the Movie object to the movies detail activity
    public static final String EXTRA_MOVIE = "com.android.fourthwardcoder.popularmovies.extra_movie";

    //API Key for the Movies DB API. Replace "APIKeys.MOVIE_DB_API_KEY" with your own API Key
    private static final String MOVIE_DB_API_KEY = APIKeys.MOVIE_DB_API_KEY;

    //Full URLs for the movie DB
    private static final String BASE_DISCOVER_URL = "http://api.themoviedb.org/" + DBUtil.NUM_QUERY_PAGES + "/discover/movie";

    //Extra append paths for the movie URI
    private static final String UPCOMING_PATH = "upcoming";
    private static final String NOW_PLAYING = "now_playing";
    private static final String POPULAR_PATH = "popular";

    //Extra append params for the movie URI
    private static final String SORT_PARAM = "sort_by";
    //private static final String CERT_COUNTRY_PARAM = "certification_country";

    //Shared Preference for storing sort type
    private static final String PREF_SORT = "sort";
    //Tag for the time Sort Dialog
    private static final String DIALOG_SORT = "dialogSort";

    //Constant for request code to Sort Dialog
    public static final int REQUEST_SORT = 0;

    //Default sort type
    private static final int DEFAULT_SORT = 0;


    /**************************************************/
	/*                Local Data                      */
    /**************************************************/
    GridView mGridView;
    ArrayList<Movie> mMovieList = null;
    //ArrayList<Pair<Integer,String>> mGenreList;
    int mSortOrder;
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

        //Get stored sort preference from shared resources. If non exists set it to sort by
        //popularity.
        mSortOrder = sharedPrefs.getInt(PREF_SORT, DEFAULT_SORT);
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

        //Make sure we have a gridview
        if(mGridView != null) {

            //We don't have any movies, go fetch them
            if (mMovieList == null)
                //Start up thread to pull in movie data. Send in sort
                //type.
                new FetchPhotosTask().execute(mSortOrder);
            else {
                //Hit this when we retained our instance of the fragment on a rotation.
                //Just apply the current list of movies
                Log.e(TAG,"Apply current movie list");
                MovieImageAdapter adapter = new MovieImageAdapter(mMovieList);
                mGridView.setAdapter(adapter);
            }
        }
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
            mSortOrder = data.getIntExtra(SortDialogFragment.EXTRA_SORT,DEFAULT_SORT);
            prefsEditor.putInt(PREF_SORT, mSortOrder);
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
     * Inner-class that extend AysncTask to pull movie data over the network.
     * That data is return from the Movie DB API as JSON data. It is then parsed
     * and stored in Movie objects that are put into and ArrayList. This is then displayed
     * on a GridView.
     *
     * Input: Integer sort type
     * Return: ArrayList of Movies
     */
    private class FetchPhotosTask extends AsyncTask<Integer,Void,ArrayList<Movie>> {

        //ProgressDialog to be displayed while the data is being fetched and parsed
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            //Start ProgressDialog on Main Thread UI before precessing begins
            progressDialog = ProgressDialog.show(getActivity(),"",getString(R.string.progress_message),true);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Integer... params) {

            Uri movieUri = null;
            //We only pass one param for the sort order, so get the first one.
            int sortPos = params[0];

            //Get the sord order parameter from the sort order type
            Resources res = getResources();
            String[] sortList = res.getStringArray(R.array.sort_url_list);
            String sortOrder = sortList[sortPos];

            switch(sortPos) {

                //Sort by Popularity
                case 0:
                    //Build URI String to query the database for a list of popular movies
                    movieUri = Uri.parse(DBUtil.BASE_MOVIE_URL).buildUpon()
                            .appendPath(POPULAR_PATH)
                            .appendQueryParameter(SORT_PARAM, sortOrder)
                            .appendQueryParameter(DBUtil.API_KEY_PARAM, MOVIE_DB_API_KEY)
                            .build();
                    break;
                //Sort by Upcoming
                case 1:
                    //Build URI String to query the database for a list of upcoming movies
                    movieUri = Uri.parse(DBUtil.BASE_MOVIE_URL).buildUpon()
                            .appendPath(UPCOMING_PATH)
                            .appendQueryParameter(SORT_PARAM, sortOrder)
                            .appendQueryParameter(DBUtil.API_KEY_PARAM, MOVIE_DB_API_KEY)
                            .build();
                    break;
                //Sort by Now Playing
                case 2:
                    //Build URI String to query the database for a list of now playing movies
                    movieUri = Uri.parse(DBUtil.BASE_MOVIE_URL).buildUpon()
                            .appendPath(NOW_PLAYING)
                            .appendQueryParameter(SORT_PARAM, sortOrder)
                            .appendQueryParameter(DBUtil.API_KEY_PARAM, MOVIE_DB_API_KEY)
                            .build();
                    break;
                //Sort by All Time Top Grossing
                case 3:
                    //Build URI String to query the database for the list of top grossing movies
                    movieUri = Uri.parse(BASE_DISCOVER_URL).buildUpon()
                            .appendQueryParameter(SORT_PARAM, sortOrder)
                            .appendQueryParameter(DBUtil.API_KEY_PARAM, MOVIE_DB_API_KEY)
                            .build();
                    break;
            }


            //Log.e(TAG,movieUri.toString());
            //Get full Json result from querying the Movie DB
            String movieJsonStr = DBUtil.queryMovieDatabase(movieUri);

            //Error pulling movies, return null
            if(movieJsonStr == null)
                return null;

            //Part data and return list of movies
            return parseJsonMovies(movieJsonStr);

        }


        /**
         * Parses the JSON String returned from the query to the Movie DB. Pulls data out for
         * a each movie returned and puts that data into a Movie object. These movie objects are
         * returned in an ARRAYList
         *
         * @param moviesJsonStr Full return JSON String of movie data
         * @return              ArrayList of Movies
         */
        private ArrayList<Movie> parseJsonMovies(String moviesJsonStr) {

            //JSON Result TAGs
            final String TAG_ID = "id";
            final String TAG_TITLE = "title";
            final String TAG_OVERVIEW = "overview";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_BACKDROP_PATH = "backdrop_path";
            final String TAG_RELEASE_DATE = "release_date";
            final String TAG_RATING = "vote_average";
            final String TAG_CREDITS = "credits";
            final String TAG_CAST = "cast";
            final String TAG_CREW = "crew";
            final String TAG_JOB = "job";
            final String TAG_JOB_DIRECTOR = "Director";
            final String TAG_NAME = "name";
            final String TAG_RUNTIME = "runtime";
            final String TAG_GENRES = "genres";
            final String TAG_REVENUE = "revenue";

            //Base URL for movie images
            final String BASE_URL = "http://image.tmdb.org/t/p/";

            //Movie image sizes
            final String IMAGE_185_SIZE = "w185/";
            final String IMAGE_342_SIZE = "w342/";
            final String IMAGE_500_SIZE = "w500/";

            //List of Movies that get parsed Movie DB JSON return
            ArrayList<Movie> movieList = null;

            try {
                JSONObject obj = new JSONObject(moviesJsonStr);

                //Get JSONArray List of all movies
                JSONArray resultsArray = obj.getJSONArray(DBUtil.TAG_RESULTS);

                //Log.e(TAG,"Results Array: " + resultsArray.get(0));

                //Initialize movie array list by the number of movies returned in the query
                movieList = new ArrayList<>(resultsArray.length());

                //Loop through all of the movies returned in the query
                for(int i=0;i <resultsArray.length(); i++) {

                    //Get Movie result. Create movie object and pull out particular data for
                    //that movie.
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
                    //Build URI String to query the databaes for a specific movie
                    Uri movieUri = Uri.parse(DBUtil.BASE_MOVIE_URL).buildUpon()
                            .appendPath(String.valueOf(movie.getId()))
                            .appendQueryParameter(DBUtil.API_KEY_PARAM, MOVIE_DB_API_KEY)
                            .build();

                    //Get additional information on that movie
                    String movieJsonStr = DBUtil.queryMovieDatabase(movieUri);
                    if(movieJsonStr != null) {
                        //Log.e(TAG, "Movie: " + movieJsonStr);

                        JSONObject movieObj = new JSONObject(movieJsonStr);
                        movie.setRuntime(movieObj.getString(TAG_RUNTIME));
                        JSONArray genreArray = movieObj.getJSONArray(TAG_GENRES);

                        //Get genres of the movie
                        ArrayList<String> genreList = new ArrayList<String>();
                        for(int j = 0; j<genreArray.length();j++) {
                            String genre = genreArray.getJSONObject(j).getString(TAG_NAME);
                            genreList.add(genre);
                        }

                        movie.setGenreList(genreList);

                        int iRevenue = movieObj.getInt(TAG_REVENUE);
                        movie.setRevenue(NumberFormat.getIntegerInstance().format(iRevenue));
                    }
                    //Get Uri for credits of movie
                    //Build URI String to query the databaes for the list of credits
                    Uri creditUri = Uri.parse(DBUtil.BASE_MOVIE_URL).buildUpon()
                            .appendPath(String.valueOf(movie.getId()))
                            .appendPath(TAG_CREDITS)
                            .appendQueryParameter(DBUtil.API_KEY_PARAM, MOVIE_DB_API_KEY)
                            .build();

                    String creditJsonStr = DBUtil.queryMovieDatabase(creditUri);

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

                        //Pull out actors of the movie
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

            //Done processing the movie query, kill Progress Dialog on main UI
            progressDialog.dismiss();

            if(getActivity() != null && mGridView != null) {

                //If we've got movies in the list, then send them to the adapter fro the
                //GridView
                if(movieList != null) {
                    MovieImageAdapter adapter = new MovieImageAdapter(movieList);
                    mGridView.setAdapter(adapter);
                }
                else {

                    //If we get here, then the movieList was empty and something went wrong.
                    //Most likely a network connection problem
                    Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                    connectToast.show();
                }

            }
        }
    }

    /**
     * Class MovieImageAdapter
     *
     * ArrayAdapter used for the GridView that displays the movie posters on the main activity
     */
    public class MovieImageAdapter extends ArrayAdapter<Movie> {


        public MovieImageAdapter(ArrayList<Movie> movies) {
            super(getActivity(),0,movies);

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
