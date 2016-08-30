package com.fourthwardmobile.android.movingpictures.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fourthwardmobile.android.movingpictures.adapters.ShowAllListAdapter;
import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.fourthwardmobile.android.movingpictures.helpers.Util;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.Credits;
import com.fourthwardmobile.android.movingpictures.models.MediaBasic;
import com.fourthwardmobile.android.movingpictures.models.MediaList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Class ShowAllListActivity
 * Author: Chris Hare
 * Created: 8/15/15
 * <p/>
 * Display's a listview of Movies/TV of a person
 */
public class ShowAllListFragment extends Fragment implements Constants {

    /********************************************************************/
    /*                         Constants                                */
    /********************************************************************/
    private static final String TAG = ShowAllListFragment.class.getSimpleName();

    /********************************************************************/
    /*                         Local Data                               */
    /********************************************************************/
   // ListView mListView;
    //CreditListAdapterOld mAdapter;
    ShowAllListAdapter mAdapter;
            RecyclerView mRecyclerView;
    int mEntType;
    int mListType;
    int mId;
    String mQuery;
    Credits mCredits;
    ArrayList<MediaBasic> mQueryResults;

    LinearLayout mProgressLayout;

    //Local Arguments
    private static final String ARG_ID = "id";
    private static final String ARG_ENT_TYPE = "ent_type";
    private static final String ARG_LIST_TYPE = "list_type";
    private static final String ARG_QUERY = "query";

    public static ShowAllListFragment newInstance(int id,int entType, int listType, String query) {

        //Store data in bungle for the fragment
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        //Store entertainment type; movie or tv
        args.putInt(ARG_ENT_TYPE, entType);
        args.putInt(ARG_LIST_TYPE, listType);
        //Argument for Search results
        args.putString(ARG_QUERY,query);

        //Create Fragment and store arguments to it.
        ShowAllListFragment fragment = new ShowAllListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        //Check if we've rotated
        if (savedInstance != null) {
            mId = savedInstance.getInt(ARG_ID);
            mEntType = savedInstance.getInt(ARG_ENT_TYPE);
            mListType = savedInstance.getInt(ARG_LIST_TYPE);
            mQuery = savedInstance.getString(ARG_QUERY);

        } else {
            Bundle bundle = getArguments();
            mId = bundle.getInt(ARG_ID);
            mEntType = bundle.getInt(ARG_ENT_TYPE);
            mListType = bundle.getInt(ARG_LIST_TYPE);
            mQuery = bundle.getString(ARG_QUERY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(com.fourthwardmobile.android.movingpictures.R.layout.fragment_show_all_list, container, false);

        //mListView = (ListView) view.findViewById(R.id.listView);
        mRecyclerView = (RecyclerView)view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.show_all_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mProgressLayout = (LinearLayout)view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.progress_layout);
        if (mEntType == ENT_TYPE_PERSON) {
            if (mListType == LIST_TYPE_MOVIE_CAST || mListType == LIST_TYPE_MOVIE_CREW) {
                getCredits(MovieDbAPI.PATH_MOVIE);
            } else if(mListType == LIST_TYPE_TV_CAST || mListType == LIST_TYPE_TV_CREW ) {
                getCredits(MovieDbAPI.PATH_TV);
            }
        } else if (mEntType == ENT_TYPE_MOVIE || mEntType == ENT_TYPE_TV) {
            getPersonsFilmography();
        } else if(mEntType == ENT_TYPE_SEARCH)
           getSearchResult();

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(ARG_ENT_TYPE, mEntType);
        savedInstanceState.putInt(ARG_LIST_TYPE,mListType);
        savedInstanceState.putInt(ARG_ID, mId);
        savedInstanceState.putString(ARG_QUERY,mQuery);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void getCredits(String entPath) {
        Log.e(TAG,"getCredits() Now fill create network call");

        Call<Credits> call = MovieDbAPI.getMovieApiService().getCredits(entPath,mId);

        setApiCall(call);
    }

    private void getPersonsFilmography() {

        //See if we want to fetch Movie or TV credits
        Log.e(TAG,"getPersonsFilmography() with ent type = " + mEntType);
        String creditPath = (mEntType == ENT_TYPE_TV) ? MovieDbAPI.PATH_TV_CREDIT : MovieDbAPI.PATH_MOVIE_CREDIT;
        Call<Credits> call = MovieDbAPI.getMovieApiService().getPersonsFilmography(mId,creditPath);

        setApiCall(call);
    }

    private void getSearchResult() {

        Call<MediaList> call = MovieDbAPI.getMovieApiService().getSearchResultList(mQuery);

        call.enqueue(new retrofit2.Callback<MediaList>() {

            @Override
            public void onResponse(Call<MediaList> call, Response<MediaList> response) {

                if(response.isSuccessful()) {

                    mQueryResults = (ArrayList)response.body().getMediaResults();

                    Log.e(TAG,"onRespnse with queryResult size = " + mQueryResults.size());
                    setSearchAdapater();
                }
            }

            @Override
            public void onFailure(Call<MediaList> call, Throwable t) {

            }
        });
    }
    private void setApiCall(Call<Credits> call) {
        call.enqueue(new retrofit2.Callback<Credits>() {

            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {

                if(response.isSuccessful()) {

                    mCredits = response.body();

                    setAdapter();

                } else {

                    //parse the response to find the error. Display a message
//                    APIError error = ErrorUtils.parseError(response);
//                    Toast.makeText(getContext(),error.message(),Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                Log.e(TAG, "onFailure() " + t.getMessage());
                Toast.makeText(getContext(),getContext().getString(com.fourthwardmobile.android.movingpictures.R.string.toast_network_error),Toast.LENGTH_LONG);
            }
        });
    }
    private void setAdapter() {

        //Remove progress indicator
        mProgressLayout.setVisibility(View.GONE);

        if(mCredits != null) {

            mAdapter = new ShowAllListAdapter(getContext(), mCredits, mEntType,mListType, new ShowAllListAdapter.ShowAllListAdapterOnClickHandler() {
                @Override
                public void onClick(int id, int position, ShowAllListAdapter.CreditListAdapterViewHolder vh) {

                    Log.e(TAG,"onCreditClick() id = " + id + " ent type = " + mEntType);
                    Util.startDetailActivity(getContext(),id,mEntType,vh.thumbImageView);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void setSearchAdapater() {

        //Remove progress indicator
        mProgressLayout.setVisibility(View.GONE);

        if(mQueryResults != null) {

            mAdapter = new ShowAllListAdapter(getContext(), mQueryResults, mEntType,mListType, new ShowAllListAdapter.ShowAllListAdapterOnClickHandler() {
                @Override
                public void onClick(int id, int position, ShowAllListAdapter.CreditListAdapterViewHolder vh) {

                    Log.e(TAG,"onCreditClick() id = " + id + " ent type = " + mEntType);
                    MediaBasic media = mQueryResults.get(position);
                    Util.startDetailActivity(getContext(),id,Util.convertStringMediaTypeToEnt(media.getMediaType()),vh.thumbImageView);
                }
            });
            mRecyclerView.setAdapter(mAdapter);

        }
    }

}



