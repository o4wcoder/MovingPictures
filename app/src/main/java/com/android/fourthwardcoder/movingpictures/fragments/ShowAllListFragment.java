package com.android.fourthwardcoder.movingpictures.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.adapters.CreditListAdapter;
import com.android.fourthwardcoder.movingpictures.helpers.APIError;
import com.android.fourthwardcoder.movingpictures.helpers.ErrorUtils;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Credits;
import com.android.fourthwardcoder.movingpictures.models.Movie;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Class ShowAllListFragment
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
    CreditListAdapter mAdapter;
            RecyclerView mRecyclerView;
    int mEntType;
    int mListType;
    int mId;
    Credits mCredits;

    public static ShowAllListFragment newInstance(int id,int entType, int listType) {

        //Store data in bungle for the fragment
        Bundle args = new Bundle();
        args.putInt(EXTRA_ID, id);
        //Store entertainment type; movie or tv
        args.putInt(EXTRA_ENT_TYPE, entType);
        args.putInt(EXTRA_LIST_TYPE, listType);

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
            mId = savedInstance.getInt(EXTRA_ID);
            mEntType = savedInstance.getInt(EXTRA_ENT_TYPE);
            mListType = savedInstance.getInt(EXTRA_LIST_TYPE);

        } else {
            Bundle bundle = getArguments();
            mId = bundle.getInt(EXTRA_ID);
            mEntType = bundle.getInt(EXTRA_ENT_TYPE);
            mListType = bundle.getInt(EXTRA_LIST_TYPE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_filmograhy, container, false);

        //mListView = (ListView) view.findViewById(R.id.listView);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.person_credit_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                CreditOld credit = (CreditOld) mAdapter.getItem(position);
//                Intent i;
//                if (mEntType == ENT_TYPE_MOVIE) {
//                    i = new Intent(getActivity(), MovieDetailActivity.class);
//                    i.putExtra(EXTRA_MOVIE_ID, credit.getId());
//                } else {
//                    i = new Intent(getActivity(), TvDetailActivity.class);
//                    i.putExtra(EXTRA_TV_ID, credit.getId());
//                }
//
//                ImageView imageView = (ImageView)view.findViewById(R.id.posterImageView);
//                //Start shared element transition for the movie poster
//                ActivityOptionsCompat activityOptions =
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                                new Pair<View, String>(imageView, getString(R.string.trans_movie_poster)));
//
//                startActivity(i,activityOptions.toBundle());
//                //startActivity(i);
//
//            }
//        });

//        if (mListView != null) {
//
//            if(Util.isNetworkAvailable(getActivity())) {
//                new FetchFilmographyTask().execute(mId, mEntType);
//            }
//            else {
//                Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
//                        getString(R.string.toast_network_error), Toast.LENGTH_LONG);
//                connectToast.show();
//            }
//        }

        if (mEntType == ENT_TYPE_PERSON) {
            if (mListType == LIST_TYPE_MOVIE_CAST || mListType == LIST_TYPE_MOVIE_CREW) {
                getCredits(MovieDbAPI.PATH_MOVIE);
            } else if(mListType == LIST_TYPE_TV_CAST || mListType == LIST_TYPE_TV_CREW ) {
                getCredits(MovieDbAPI.PATH_TV);
            }
        } else if (mEntType == ENT_TYPE_MOVIE || mEntType == ENT_TYPE_TV) {
            getPersonsFilmography();
        }

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(EXTRA_ENT_TYPE, mEntType);
        savedInstanceState.putInt(EXTRA_LIST_TYPE,mListType);
        savedInstanceState.putInt(EXTRA_ID, mId);
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

    private void setApiCall(Call<Credits> call) {
        call.enqueue(new retrofit2.Callback<Credits>() {

            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {

                if(response.isSuccessful()) {

                    mCredits = response.body();

                    setAdapter();

                } else {

                    //parse the response to find the error. Display a message
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(getContext(),error.message(),Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                Log.e(TAG, "onFailure() " + t.getMessage());
                Toast.makeText(getContext(),getContext().getString(R.string.toast_network_error),Toast.LENGTH_LONG);
            }
        });
    }
    private void setAdapter() {

        if(mCredits != null) {

            mAdapter = new CreditListAdapter(getContext(), mCredits, mEntType,mListType, new CreditListAdapter.CreditListAdapterOnClickHandler() {
                @Override
                public void onCreditClick(int id, CreditListAdapter.CreditListAdapterViewHolder vh) {

                    Log.e(TAG,"onCreditClick() id = " + id + " ent type = " + mEntType);
                    Util.startDetailActivity(getContext(),id,mEntType,vh.thumbImageView);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

}



