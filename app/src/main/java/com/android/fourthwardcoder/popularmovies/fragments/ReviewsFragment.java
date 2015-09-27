package com.android.fourthwardcoder.popularmovies.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.models.Review;
import com.android.fourthwardcoder.popularmovies.adapters.ReviewsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewsFragment extends Fragment implements Constants {

    /***********************************************************************/
    /*                           Constants                                 */
    /***********************************************************************/
     private static final String TAG = ReviewsFragment.class.getSimpleName();


    /***********************************************************************/
    /*                          Local Data                                 */
    /***********************************************************************/
    ListView mListView;
    ArrayList<Review> mReviewList;

    public ReviewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        mListView = (ListView)view.findViewById(R.id.reviewsListView);
        mListView.setEmptyView(view.findViewById(R.id.emptyReviewsLayout));

        Intent i = getActivity().getIntent();
        final int id = i.getIntExtra(EXTRA_MOVIE_ID,0);
        final int entType = i.getIntExtra(EXTRA_ENT_TYPE,0);


        if(mListView != null) {

           new FetchReviewsTask().execute(id,entType);
        }
        return view;
    }

    private class FetchReviewsTask extends AsyncTask<Integer,Void,ArrayList<Review>> {


        @Override
        protected ArrayList<Review> doInBackground(Integer... params) {

            //Get ID of movie or tv show
            int id = params[0];
            int entType = params[1];

            return MovieDbAPI.getReviewList(id,entType);

        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviewsList) {

            ReviewsListAdapter adapter = new ReviewsListAdapter(getActivity(),reviewsList);
            mListView.setAdapter(adapter);

        }

    }
}
