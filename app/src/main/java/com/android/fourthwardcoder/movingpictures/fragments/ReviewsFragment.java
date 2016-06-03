package com.android.fourthwardcoder.movingpictures.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.models.Review;
import com.android.fourthwardcoder.movingpictures.adapters.ReviewsListAdapter;

import java.util.ArrayList;


/**
 * Class ReviewsFragment
 * Author: Chris Hare
 * Created: 8/15/15
 * <p/>
 * Fragment to hold reviews of a Movie/TV show.
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

        mListView = (ListView) view.findViewById(R.id.reviewsListView);
        mListView.setEmptyView(view.findViewById(R.id.emptyReviewsLayout));

        Intent i = getActivity().getIntent();
        final int id = i.getIntExtra(EXTRA_MOVIE_ID, 0);
        final int entType = i.getIntExtra(EXTRA_ENT_TYPE, 0);


        if (mListView != null) {

            if(Util.isNetworkAvailable(getActivity())) {
                new FetchReviewsTask().execute(id, entType);
            }
            else {
                Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                connectToast.show();
            }
        }
        return view;
    }

    private class FetchReviewsTask extends AsyncTask<Integer, Void, ArrayList<Review>> {

        @Override
        protected ArrayList<Review> doInBackground(Integer... params) {

            //Get ID of movie or tv show
            int id = params[0];
            int entType = params[1];

            return MovieDbAPI.getReviewList(id, entType);
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviewsList) {

            if ((getActivity() != null) && (reviewsList != null)) {
                ReviewsListAdapter adapter = new ReviewsListAdapter(getActivity(), reviewsList);
                mListView.setAdapter(adapter);
            }
        }
    }
}
