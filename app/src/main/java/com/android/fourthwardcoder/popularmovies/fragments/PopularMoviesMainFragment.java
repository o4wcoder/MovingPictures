package com.android.fourthwardcoder.popularmovies.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.adapters.MovieImageAdapter;
import com.android.fourthwardcoder.popularmovies.data.MovieContract;
import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.SimpleMovie;

import java.util.ArrayList;

/**
 * Class PopularMoviesMainFragment
 * Author: Chris Hare
 * Created: 7/25/2015
 *
 * Main Fragment of the Popular Movies App
 */
public class PopularMoviesMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Constants {


    /**************************************************/
	/*                 Constants                      */
    /**************************************************/
    //Log tag used for debugging
    private static final String TAG = PopularMoviesMainFragment.class.getSimpleName();


    //Shared Preference for storing sort type
    private static final String PREF_SORT = "sort";
    //Tag for the time Sort Dialog
    private static final String DIALOG_SORT = "dialogSort";

    //Constant for request code to Sort Dialog
    public static final int REQUEST_SORT = 0;

    //Default sort type
    private static final int DEFAULT_SORT = 0;

    //ID for Movie Favorites Loader
    private static final int MOVIE_FAVORITES_LOADER = 0;


    /**************************************************/
	/*                Local Data                      */
    /**************************************************/
    GridView mGridView;
    ArrayList<SimpleMovie> mMovieList = null;
    //ArrayList<Pair<Integer,String>> mGenreList;
    int mSortOrder;
    SharedPreferences.Editor prefsEditor;

    Loader<Cursor> mDbLoader;

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
        Log.e(TAG,"onCreate");
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
        Log.e(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Get main Gridview and set up click listener
        mGridView = (GridView)view.findViewById(R.id.gridView);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get selected movie from the GridView
                SimpleMovie movie = mMovieList.get(position);
                //Start intent to bring up Details Activity
               // Intent i = new Intent(getActivity(),MovieDetailActivity.class);
                //i.putExtra(EXTRA_MOVIE_ID, movie.getId());
                //startActivity(i);
                ((Callback) getActivity()).onItemSelected(movie.getId());
            }
        });

        //Make sure we have a gridview
        if(mGridView != null) {

            //We don't have any movies, go fetch them
            if (mMovieList == null)
                //Start up thread to pull in movie data. Send in sort
                //type. If we are pulling favorites (4) then use a cursorLoader. Otherwise
                //use AsynTAsk
               if(mSortOrder == 4) {
                //Get Favorites
                   Log.e(TAG,"In OnCreateView: Got sort order 4, calling loader to get favorites");
                   getLoaderManager().initLoader(MOVIE_FAVORITES_LOADER,null,this);
               }
               else {
                   new FetchPhotosTask().execute(mSortOrder);
               }
            else {
                //Hit this when we retained our instance of the fragment on a rotation.
                //Just apply the current list of movies
                Log.e(TAG,"Apply current movie list");
                MovieImageAdapter adapter = new MovieImageAdapter(getActivity().getApplicationContext(),mMovieList);
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
                if(mSortOrder == 4) {
                    //Get Favorites
                    Log.e(TAG,"CAlling init LOader");
                    getLoaderManager().restartLoader(MOVIE_FAVORITES_LOADER, null, this);
                    Log.e(TAG,"initLoader called");
                }
                else {
                    new FetchPhotosTask().execute(mSortOrder);
                }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.e(TAG,"Inside onCreateLoader");
        Uri movieFavoritesUri = MovieContract.MovieEntry.buildMovieUri();

        return new CursorLoader(getActivity(),
                movieFavoritesUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    //    cursor.moveToFirst();
      //  while(cursor.moveToNext())
        //   Log.e(TAG,"Loaded Favorite movie id " + cursor.getInt(MovieContract.COL_MOVIE_ID));

        Log.e(TAG,"inside onLoadFinished");
        setMovieAdapter(convertCursorToSimpleMovieList(cursor));


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ArrayList<SimpleMovie> convertCursorToSimpleMovieList(Cursor cursor) {

        ArrayList<SimpleMovie> movieList = new ArrayList<>(cursor.getCount());

        while(cursor.moveToNext()) {
            SimpleMovie movie = new SimpleMovie(cursor.getInt(MovieContract.COL_MOVIE_ID),cursor.getString(MovieContract.COL_MOVIE_POSTER_PATH));
            Log.e(TAG,"Created simple movie " + movie.getId());
            movieList.add(movie);
        }

        return movieList;

    }

    private void setMovieAdapter(ArrayList<SimpleMovie> movieList) {

        if(getActivity() != null && mGridView != null) {

            //If we've got movies in the list, then send them to the adapter from the
            //GridView
            if(movieList != null) {

                //Store global copy
                mMovieList = movieList;
                MovieImageAdapter adapter = new MovieImageAdapter(getActivity().getApplicationContext(),movieList);
                mGridView.setAdapter(adapter);


                if(mMovieList.size() > 0)
                   ((Callback) getActivity()).onLoadFinished(mMovieList.get(0).getId());

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
    private class FetchPhotosTask extends AsyncTask<Integer,Void,ArrayList<SimpleMovie>> {

        //ProgressDialog to be displayed while the data is being fetched and parsed
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            //Start ProgressDialog on Main Thread UI before precessing begins
            progressDialog = ProgressDialog.show(getActivity(),"",getString(R.string.progress_downloading_movies),true);
        }

        @Override
        protected ArrayList<SimpleMovie> doInBackground(Integer... params) {

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
                    movieUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                            .appendPath(MovieDbAPI.PATH_MOVIE)
                            .appendPath(MovieDbAPI.PATH_POPULAR)
                            .appendQueryParameter(MovieDbAPI.PARAM_SORT, sortOrder)
                            .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                            .build();
                    break;
                //Sort by Upcoming
                case 1:
                    //Build URI String to query the database for a list of upcoming movies
                    movieUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                            .appendPath(MovieDbAPI.PATH_MOVIE)
                            .appendPath(MovieDbAPI.PATH_UPCOMING)
                            .appendQueryParameter(MovieDbAPI.PARAM_SORT, sortOrder)
                            .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                            .build();
                    break;
                //Sort by Now Playing
                case 2:
                    //Build URI String to query the database for a list of now playing movies
                    movieUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                            .appendPath(MovieDbAPI.PATH_MOVIE)
                            .appendPath(MovieDbAPI.PATH_NOW_PLAYING)
                            .appendQueryParameter(MovieDbAPI.PARAM_SORT, sortOrder)
                            .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                            .build();
                    break;
                //Sort by All Time Top Grossing
                case 3:
                    //Build URI String to query the database for the list of top grossing movies
                    movieUri = Uri.parse(MovieDbAPI.BASE_DISCOVER_URL).buildUpon()
                            .appendQueryParameter(MovieDbAPI.PARAM_SORT, sortOrder)
                            .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                            .build();
                    break;
            }

            return MovieDbAPI.getMovieList(getActivity(), movieUri);
        }

        @Override
        protected void onPostExecute(ArrayList<SimpleMovie> movieList) {

            //Done processing the movie query, kill Progress Dialog on main UI
            progressDialog.dismiss();

            setMovieAdapter(movieList);

        }
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(int movieId);

        public void onLoadFinished(int movieId);
    }





}
