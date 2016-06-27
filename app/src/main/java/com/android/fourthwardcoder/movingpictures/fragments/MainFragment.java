package com.android.fourthwardcoder.movingpictures.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.adapters.EntListAdapter;
import com.android.fourthwardcoder.movingpictures.data.FavoritesContract;
import com.android.fourthwardcoder.movingpictures.helpers.APIError;
import com.android.fourthwardcoder.movingpictures.helpers.ErrorUtils;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.MovieBasic;
import com.android.fourthwardcoder.movingpictures.models.MovieList;
import com.android.fourthwardcoder.movingpictures.models.MovieOld;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Class MainFragment
 * Author: Chris Hare
 * Created: 7/25/2015
 * <p/>
 * Main Fragment of the Popular Movies App
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Constants {

    /**************************************************/
    /*                 Constants                      */
    /**************************************************/
    //Log tag used for debugging
    private static final String TAG = MainFragment.class.getSimpleName();

    //Shared Preference for storing sort type
    private static final String PREF_SORT = "sort";
    //Tag for the time Sort Dialog
    private static final String DIALOG_SORT = "dialogSort";

    //Constant for request code to Sort Dialog
    public static final int REQUEST_SORT = 0;

    //Default sort type
    private static final int DEFAULT_SORT = 0;

    //ID for MovieOld Favorites Loader
    private static final int MOVIE_FAVORITES_LOADER = 0;

    //Sort order argument
    private static final String ARG_ENT_TYPE = "ent_type";
    private static final String ARG_SORT = "arg_sort";

    /**************************************************/
    /*                Local Data                      */
    /**************************************************/
    //  GridView mGridView;
    RecyclerView mRecyclerView;
    // ArrayList<MovieOld> mList = null;
    ArrayList<MovieBasic> mList = null;
    int mEntType;
    int mSortOrder;
    SharedPreferences.Editor prefsEditor;
    //ProgressDialog to be displayed while the data is being fetched and parsed
    ProgressDialog mProgressDialog;

    public static MainFragment newInstance(@EntertainmentType int entType, int sortOrder) {
        Bundle args = new Bundle();
        args.putInt(ARG_ENT_TYPE, entType);
        args.putInt(ARG_SORT, sortOrder);
        MainFragment fragment = new MainFragment();

        fragment.setArguments(args);
        return fragment;
    }
    /**************************************************/
    /*               Override Methods                 */

    /**************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.e(TAG, "onCreate");
//        //Set Option Menu
//        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mList = savedInstanceState.getParcelableArrayList(EXTRA_MOVIE_LIST);
            mEntType = savedInstanceState.getInt(ARG_ENT_TYPE);
        } else {
            Bundle bundle = getArguments();
            mEntType = bundle.getInt(ARG_ENT_TYPE);
            mSortOrder = bundle.getInt(ARG_SORT);
        }
        //Get instanace of Shared Preferences
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
//                .getApplicationContext());
//        prefsEditor = sharedPrefs.edit();

        //Get stored sort preference from shared resources. If non exists set it to sort by
        //popularity.
        // mSortOrder = sharedPrefs.getInt(PREF_SORT, DEFAULT_SORT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Set Up RecyclerView in Grid Layout
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list_recycler_view);
        //Set Layout Manager for RecyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //Make sure we have a gridview


        if (mRecyclerView != null) {

            //We don't have any movies, go fetch them
            if (mList == null)
                //Start up thread to pull in movie data. Send in sort
                //type. If we are pulling favorites (4) then use a cursorLoader. Otherwise
                //use AsynTAsk
                if (mSortOrder == 4) {
                    //Get Favorites
                    Log.e(TAG, "In OnCreateView: Got sort order 4, calling loader to get favorites");
                    getLoaderManager().initLoader(MOVIE_FAVORITES_LOADER, null, this);
                } else {
                    getApiList();
                }
            else {
                //Hit this when we retained our instance of the fragment on a rotation.
                //Just apply the current list of movies
                Log.e(TAG, "-----------Apply current movie list --------------");
                setAdapter(mList);

            }
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //Done processing the movie query, kill Progress Dialog on main UI
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, "onActivityCreated()");

        //Need to set the details view to the first movie when in 2 pane mode. Hit this
        //situation when we first come up on a tablet in portrait and rotate to landscape
        if (mList != null) {
            if (mList.size() > 0) {
                Log.e(TAG, "Calling on loadfinished");
                ((Callback) getActivity()).onLoadFinished(mList.get(0).getId());
            }
        }
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        //super.onCreateOptionsMenu(menu, inflater);
//
//        //Pass the resource ID of the menu and populate the Menu
//        //instance with the items defined in the xml file
//        inflater.inflate(R.menu.menu_sort, menu);
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.menu_item_sort:
//                //Get support Fragment Manager
//                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
//                SortDialogFragment dialog = SortDialogFragment.newInstance(mSortOrder);
//                //Make MainFragment the target fragment of the SortDialogFragment instance
//                dialog.setTargetFragment(MainFragment.this, REQUEST_SORT);
//                dialog.show(fm, DIALOG_SORT);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    //Get results from Dialog boxes and other Activities
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//        if (resultCode != Activity.RESULT_OK)
//            return;
//
//        if (requestCode == REQUEST_SORT) {
//            //Get change in sort from dialog and store it in Shared Preferences
//            mSortOrder = data.getIntExtra(SortDialogFragment.EXTRA_SORT, DEFAULT_SORT);
//            prefsEditor.putInt(PREF_SORT, mSortOrder);
//            prefsEditor.commit();
//
//            Resources res = getResources();
//            String[] sortList = res.getStringArray(R.array.sort_list);
//            String sortOrder = sortList[mSortOrder];
//            getActivity().setTitle(sortOrder);
//            //Fetch new set of movies based on sort order
//            if (mRecyclerView != null)
//                if (mSortOrder == 4) {
//                    //Get Favorites
//                    getLoaderManager().restartLoader(MOVIE_FAVORITES_LOADER, null, this);
//                } else {
//                    if(Util.isNetworkAvailable(getActivity())) {
//                        //new FetchPhotosTask().execute(mSortOrder);
//                        getMovieList(mSortOrder);
//                    }
//                    else {
//                        Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
//                                getString(R.string.toast_network_error), Toast.LENGTH_LONG);
//                        connectToast.show();
//                    }
//                }
//        }
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.e(TAG, "Inside onCreateLoader");
        Uri movieFavoritesUri = FavoritesContract.MovieEntry.buildMovieUri();

        return new CursorLoader(getActivity(),
                movieFavoritesUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.e(TAG, "inside onLoadFinished");
        //  setMovieAdapter(convertCursorToMovieList(cursor));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ArrayList<MovieOld> convertCursorToMovieList(Cursor cursor) {

        if (cursor != null) {
            ArrayList<MovieOld> movieList = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                MovieOld movie = MovieOld.convertCursorToMovie(cursor);
                Log.e(TAG, "Conver favorite movie " + movie.getTitle());
                movieList.add(movie);
            }

            return movieList;
        } else {
            return null;
        }

    }

    //    private String[] getNowPlayingDates() {
//
//        String[] dateRange = new String[2];
//
//        Calendar calendar = Calendar.getInstance();
//        String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
//        Log.e(TAG,"!!!!! Today's date = " + todaysDate);
//
//
//        calendar.add(Calendar.MONTH,-1);
//        String lastMonthDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
//        Log.e(TAG,"!!!!! Last months date = " + lastMonthDate);
//        dateRange[0] = lastMonthDate;
//        dateRange[1] = todaysDate;
//
//        return dateRange;
//    }
    private void getApiList() {


        if (getActivity() != null) {
//            Resources res = getResources();
//            String[] sortList = res.getStringArray(R.array.sort_url_list);
//            String sortOrder = sortList[sortPos];

            Call<MovieList> call = null;

            Log.e(TAG,"Get API list with ent type = " + mEntType + " Sort type = " + mSortOrder);
            call = MovieDbAPI.getMovieApiService().getMovieList(getUriPath(), getSortType());


            if(call != null) {
                call.enqueue(new retrofit2.Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                        if (response.isSuccessful()) {
                            Log.e(TAG, "onResponse()");
                            setAdapter((ArrayList) response.body().getMovies());
                        } else {
                            Log.e(TAG, "!!! Response was not sucessful!!!");
                            //parse the response to find the error. Display a message
                            APIError error = ErrorUtils.parseError(response);
                            Log.e(TAG, "Error message = " + error.message());
                            Toast.makeText(getContext(), error.message(), Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Log.e(TAG, "onFailure() " + t.getMessage());
                        Toast.makeText(getContext(), getContext().getString(R.string.toast_network_error), Toast.LENGTH_LONG);

                    }
                });
            }
        }
    }

    /**
     * Set the adapter of the movie list
     *
     * @param movieList ArrayList of Movies
     */
    private void setAdapter(ArrayList<MovieBasic> movieList) {

        Log.e(TAG, "setMovieAdapter() Inside");
        if (getActivity() != null && mRecyclerView != null) {

            //If we've got movies in the list, then send them to the adapter from the
            //GridView
            if (movieList != null) {

                //Store global copy
                mList = movieList;
                Log.e(TAG, "setMovieAdapter(): MovieBasic list is not null. Set adapter with list size = " + mList.size() + " Ent type = " + mEntType);
                //MovieImageListViewAdapter adapter = new MovieImageListViewAdapter(getActivity().getApplicationContext(), movieList);
                EntListAdapter adapter = new EntListAdapter(getActivity(), mList, mEntType,new EntListAdapter.MovieListAdapterOnClickHandler() {
                    @Override
                    public void onClick(MovieBasic movie, EntListAdapter.MovieListAdapterViewHolder vh) {
                        Log.e(TAG, "onClick RecyclerView movie list");
                        //                //Get selected movie from the GridView
                        //Start intent to bring up Details Activity

                        Log.e(TAG, "onClick() got image, callback to activity to start detail activity");
                        ((Callback) getActivity()).onItemSelected(mEntType, movie.getId(), vh.movieThumbImageView);
                    }
                });

                mRecyclerView.setAdapter(adapter);
                mRecyclerView.scheduleLayoutAnimation();
                Log.e(TAG, "MovieOld list not null, if there is anthing in the list set 1st pos");
                //If we are in two pane mode, set the first movie in the details fragment
                if (mList.size() > 0) {
                    Log.e(TAG, "Calling on loadfinished");
                    ((Callback) getActivity()).onLoadFinished(mList.get(0).getId());
                }
            } else {
                //If we get here, then the movieList was empty and something went wrong.
                //Most likely a network connection problem
                Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                connectToast.show();
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putParcelableArrayList(EXTRA_MOVIE_LIST, mList);
        savedInstanceState.putInt(ARG_ENT_TYPE,mEntType);
        super.onSaveInstanceState(savedInstanceState);
    }


    private String getUriPath() {

        if (mEntType == ENT_TYPE_MOVIE) {
            return MovieDbAPI.PATH_MOVIE;
        } else if (mEntType == ENT_TYPE_TV) {
            return MovieDbAPI.PATH_TV;
        } else if (mEntType == ENT_TYPE_PERSON) {
            return MovieDbAPI.PATH_PERSON;
        }

        //Return nothing is somehow we don't have the type of query
        return "";
    }
    private String getSortType() {

        if (mEntType == ENT_TYPE_MOVIE) {
            switch (mSortOrder) {

                case SORT_POPULAR:
                    return MovieDbAPI.PATH_POPULAR;
                case SORT_NOW_PLAYING:
                    return MovieDbAPI.PATH_NOW_PLAYING;
                case SORT_UPCOMING:
                    return MovieDbAPI.PATH_UPCOMING;
                default:
                    return MovieDbAPI.PATH_POPULAR;
            }
        } else if (mEntType == ENT_TYPE_TV) {
            switch (mSortOrder) {
                case SORT_POPULAR:
                    return MovieDbAPI.PATH_POPULAR;
                case SORT_AIRING_TONIGHT:
                    return MovieDbAPI.PATH_AIRING_TODAY;
                default:
                    return MovieDbAPI.PATH_POPULAR;
            }
        } else if (mEntType == ENT_TYPE_PERSON)
            return MovieDbAPI.PATH_POPULAR;

        //Something bad went wrong, return nothing
        return "";
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
        void onItemSelected(@Constants.EntertainmentType int entType, int movieId, ImageView imageView);

        void onLoadFinished(int id);
    }
}
