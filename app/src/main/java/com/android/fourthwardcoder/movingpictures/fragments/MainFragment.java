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
import com.android.fourthwardcoder.movingpictures.models.MediaBasic;
import com.android.fourthwardcoder.movingpictures.models.MovieList;

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
    ArrayList<MediaBasic> mList = null;
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
//        //Set Option Menu
//        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mList = savedInstanceState.getParcelableArrayList(EXTRA_MOVIE_LIST);
            mEntType = savedInstanceState.getInt(ARG_ENT_TYPE);
            mSortOrder = savedInstanceState.getInt(ARG_SORT);
        } else {
            Bundle bundle = getArguments();
            mEntType = bundle.getInt(ARG_ENT_TYPE);
            mSortOrder = bundle.getInt(ARG_SORT);
        }
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
                if (mEntType == ENT_TYPE_FAVORITE) {
                    //Get Favorites
                    getLoaderManager().initLoader(MOVIE_FAVORITES_LOADER, null, this);
                } else {
                    getApiList();
                }
            else {
                //Hit this when we retained our instance of the fragment on a rotation.
                //Just apply the current list of movies. If this is the favorites, then
                //Need to adjust the ent type
                if(mEntType == ENT_TYPE_FAVORITE) {

                    setAdapter(mList, convertFavoriteSortToMediaType());
                }
                else
                    setAdapter(mList,mEntType);

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

    private int convertFavoriteSortToMediaType() {

        switch(mSortOrder) {
            case SORT_MOVIES:
                return ENT_TYPE_MOVIE;
            case SORT_TV:
                return ENT_TYPE_TV;
            case SORT_PERSON:
                return ENT_TYPE_PERSON;
            default:
                return ENT_TYPE_MOVIE;

        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.e(TAG, "Inside onCreateLoader with sortOrder = " + mSortOrder);
        Uri movieFavoritesUri = FavoritesContract.FavoritesEntry.buildMovieUri();

        String selection = FavoritesContract.FavoritesEntry.COLUMN_MEDIA_TYPE +"=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(convertFavoriteSortToMediaType());

        return new CursorLoader(getActivity(),
                movieFavoritesUri,
                null,
                selection,
                selectionArgs,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.e(TAG, "inside onLoadFinished with cursor size = " + cursor.getCount());

        //For Favorites, we will use the sort order as the ent type as it contains
        setAdapter(convertCursorToList(cursor),convertFavoriteSortToMediaType());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ArrayList<MediaBasic> convertCursorToList(Cursor cursor) {

        if (cursor != null) {
            ArrayList<MediaBasic> mediaList = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                MediaBasic media = new MediaBasic(cursor);
                Log.e(TAG,"convertCusorToList() MediaBasic from cursor. Id = " + media.getId() +
                        " " + " poster path = " + media.getPosterPath());
               // Log.e(TAG, "Conver favorite movie " + movie.getTitle());
                mediaList.add(media);
            }

            return mediaList;
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

            Log.e(TAG,"getApiList() with ent type = " + mEntType + " Sort type = " + mSortOrder);
            call = MovieDbAPI.getMovieApiService().getMovieList(getUriPath(), getSortType());


            if(call != null) {
                call.enqueue(new retrofit2.Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                        if (response.isSuccessful()) {
                            Log.e(TAG, "onResponse()");
                            setAdapter((ArrayList) response.body().getMovies(),mEntType);
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
                        if(getActivity() != null)
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
    private void setAdapter(ArrayList<MediaBasic> movieList, final int entType) {

        final int localEntType;
        Log.e(TAG, "setMovieAdapter() Inside");
        if (getActivity() != null && mRecyclerView != null) {

            //If we've got movies in the list, then send them to the adapter from the
            //GridView
            if (movieList != null) {

                if (movieList.size() > 0) {
                    //Store global copy
                    mList = movieList;

                    //If this is from favorites, then change the ent type
                    if(entType == ENT_TYPE_FAVORITE)
                        localEntType = convertFavoriteSortToMediaType();
                    Log.e(TAG, "setMovieAdapter(): MediaBasic list is not null. Set adapter with list size = " + mList.size() + " Ent type = " + entType);
                    //MovieImageListViewAdapter adapter = new MovieImageListViewAdapter(getActivity().getApplicationContext(), movieList);
                    EntListAdapter adapter = new EntListAdapter(getActivity(), mList, entType, new EntListAdapter.MovieListAdapterOnClickHandler() {
                        @Override
                        public void onClick(MediaBasic movie, EntListAdapter.MovieListAdapterViewHolder vh) {
                            Log.e(TAG, "onClick RecyclerView movie list");
                            //                //Get selected movie from the GridView
                            //Start intent to bring up Details Activity

                            Log.e(TAG, "onClick() got image, callback to activity to start detail activity");
                            ((Callback) getActivity()).onItemSelected(entType, movie.getId(), vh.movieThumbImageView);
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
//                    Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
//                            getString(R.string.toast_network_error), Toast.LENGTH_LONG);
//                    connectToast.show();
                }
            } else {
                Log.e(TAG,"setAdapter() List was empty. Nothing to set in adapter");
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putParcelableArrayList(EXTRA_MOVIE_LIST, mList);
        savedInstanceState.putInt(ARG_ENT_TYPE,mEntType);
        savedInstanceState.putInt(ARG_SORT,mSortOrder);
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
