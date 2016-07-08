package com.android.fourthwardcoder.movingpictures.fragments;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.adapters.EntListAdapter;
import com.android.fourthwardcoder.movingpictures.data.FavoritesContract;
import com.android.fourthwardcoder.movingpictures.helpers.APIError;
import com.android.fourthwardcoder.movingpictures.helpers.ErrorUtils;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.MediaBasic;
import com.android.fourthwardcoder.movingpictures.models.MediaList;

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
    LinearLayout mProgressLayout;

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

        View view = inflater.inflate(R.layout.fragment_grid_recycler_view, container, false);

        //Set Up RecyclerView in Grid Layout
        mRecyclerView = (RecyclerView) view.findViewById(R.id.grid_recycler_view);
        //Set Layout Manager for RecyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.list_columns
        )));

        mProgressLayout = (LinearLayout)view.findViewById(R.id.progress_layout);
        //Make sure we have a gridview
        if (mRecyclerView != null) {

            //We don't have any movies, go fetch them
            if (mList == null)
                //Start up thread to pull in movie data. Send in sort
                //type. If we are pulling favorites (4) then use a cursorLoader. Otherwise
                //fetch the data
                if (mEntType == ENT_TYPE_FAVORITE) {
                    //Get Favorites
                    getLoaderManager().initLoader(MOVIE_FAVORITES_LOADER, null, this);
                } else {
                    getApiList();
                }
            else {
                //Hit this when we retained our instance of the fragment on a rotation.
                //Just apply the current list of movies.
                setAdapter(mList);

            }
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //Done processing the movie query, kill Progress Dialog on main UI
//        if (mProgressDialog != null) {
//            if (mProgressDialog.isShowing())
//                mProgressDialog.dismiss();
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Log.e(TAG, "onActivityCreated()");
//
//        //Need to set the details view to the first movie when in 2 pane mode. Hit this
//        //situation when we first come up on a tablet in portrait and rotate to landscape
//        if (mList != null) {
//            if (mList.size() > 0) {
//                Log.e(TAG, "Calling on loadfinished");
//                if (mEntType == ENT_TYPE_FAVORITE)
//                    ((Callback) getActivity()).onLoadFinished(convertFavoriteSortToMediaType(), mSortOrder, mList.get(0).getId());
//                else
//                    ((Callback) getActivity()).onLoadFinished(mEntType, mSortOrder,mList.get(0).getId());
//            }
//        }
    }


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
        setAdapter(convertCursorToList(cursor));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ArrayList<MediaBasic> convertCursorToList(Cursor cursor) {

        if (cursor != null) {
            ArrayList<MediaBasic> mediaList = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                MediaBasic media = new MediaBasic(cursor);
                mediaList.add(media);
            }

            return mediaList;
        } else {
            return null;
        }

    }

    private void getApiList() {


        if (getActivity() != null) {

            Call<MediaList> call = null;

            Log.e(TAG,"getApiList() with ent type = " + mEntType + " Sort type = " + mSortOrder);
            call = MovieDbAPI.getMovieApiService().getMediaList(getUriPath(), getSortType());


            if(call != null) {
                call.enqueue(new retrofit2.Callback<MediaList>() {
                    @Override
                    public void onResponse(Call<MediaList> call, Response<MediaList> response) {

                        if (response.isSuccessful()) {

                            setAdapter((ArrayList) response.body().getMediaResults());
                        } else {
                            Log.e(TAG, "!!! Response was not sucessful!!!");
                            //parse the response to find the error. Display a message
                            APIError error = ErrorUtils.parseError(response);
                            Log.e(TAG, "Error message = " + error.message());
                            Toast.makeText(getContext(), error.message(), Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(Call<MediaList> call, Throwable t) {
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
     */
    private void setAdapter(ArrayList<MediaBasic> mediaList) {


        //Remove progress indicator
        mProgressLayout.setVisibility(View.GONE);

        if (getActivity() != null && mRecyclerView != null) {

            //If we've got movies in the list, then send them to the adapter from the
            //GridView
            if (mediaList != null) {

                if (mediaList.size() > 0) {
                    //Store global copy
                    mList = mediaList;


                    //If this is favorites, switch the ent type to either movies, tv, or person
                    //depending on the favorites sort type.
                    final @EntertainmentType int localEntType;
                    if (mEntType == ENT_TYPE_FAVORITE) {
                        localEntType = convertFavoriteSortToMediaType();
                    } else {
                        localEntType = mEntType;
                    }
                    Log.e(TAG, "setMovieAdapter(): MediaBasic list is not null. Set adapter with list size = " + mList.size() + " Ent type = " + localEntType);
                    //MovieImageListViewAdapter adapter = new MovieImageListViewAdapter(getActivity().getApplicationContext(), movieList);
                    EntListAdapter adapter = new EntListAdapter(getActivity(), mList, localEntType, new EntListAdapter.MovieListAdapterOnClickHandler() {
                        @Override
                        public void onClick(MediaBasic movie, EntListAdapter.MovieListAdapterViewHolder vh) {
                            Log.e(TAG, "onClick RecyclerView movie list");
                            //                //Get selected movie from the GridView
                            //Start intent to bring up Details Activity

                            Log.e(TAG, "onClick() got image, callback to activity to start detail activity");
                            ((Callback) getActivity()).onItemSelected(localEntType, movie.getId(), vh.movieThumbImageView);
                        }
                    });

                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.scheduleLayoutAnimation();

                    //If we are in two pane mode, set the first movie in the details fragment
                    if (mList.size() > 0) {
                        Log.e(TAG, "Calling onLoadfinished with ent type = " + localEntType + ", Sort order = " + mSortOrder);
//                      //Calll back to Main Activity to set the first detail fragment in the list if we are in
                        //tablet 2 pane mode. Note we are using the unmodified version ot the ENT type because
                        //we need to know if we are set to favorites or not.
                        ((Callback) getActivity()).onLoadFinished(mEntType, mSortOrder, mList.get(0).getId());
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
        void onItemSelected(@Constants.EntertainmentType int entType, int idd, ImageView imageView);

        void onLoadFinished(@Constants.EntertainmentType int entType, int sortOrder, int id);
    }
}
