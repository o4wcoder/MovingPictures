package com.android.fourthwardcoder.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieReviewsActivityFragment extends Fragment {

    /***********************************************************************/
    /*                           Constants                                 */
    /***********************************************************************/
     private static final String TAG = MovieReviewsActivityFragment.class.getSimpleName();

    private static final String TAG_REVIEWS = "reviews";
    /***********************************************************************/
    /*                          Local Data                                 */
    /***********************************************************************/
    ListView mListView;
    ArrayList<Review> mReviewList;

    public MovieReviewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        mListView = (ListView)view.findViewById(R.id.reviewsListView);

        int movieId = getActivity().getIntent().getIntExtra(MovieDetailActivityFragment.EXTRA_MOVIE_ID,0);

        Log.e(TAG,"got movie id " + movieId);

        if(mListView != null) {

           new FetchReviewsTask().execute(movieId);
        }
        return view;
    }

    private class FetchReviewsTask extends AsyncTask<Integer,Void,ArrayList<Review>> {


        @Override
        protected ArrayList<Review> doInBackground(Integer... params) {


            int movieId = params[0];


            Uri reviewsUri = Uri.parse(DBUtil.BASE_MOVIE_URL).buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .appendPath(TAG_REVIEWS)
                    .appendQueryParameter(DBUtil.API_KEY_PARAM, APIKeys.MOVIE_DB_API_KEY)
                    .build();

            Log.e(TAG,reviewsUri.toString());
            String reviewsJsonStr = DBUtil.queryMovieDatabase(reviewsUri);

            if(reviewsJsonStr != null)
               Log.e(TAG,"REview: " + reviewsJsonStr);
            return null;
        }



    }
}
