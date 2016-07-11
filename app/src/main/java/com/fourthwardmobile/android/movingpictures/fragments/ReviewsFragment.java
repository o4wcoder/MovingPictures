package com.fourthwardmobile.android.movingpictures.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.Review;
import com.fourthwardmobile.android.movingpictures.adapters.ReviewsListAdapter;

import java.util.ArrayList;


/**
 * Class ReviewsFragment
 * Author: Chris Hare
 * Created: 8/15/15
 * <p/>
 * Fragment to hold reviews of a MovieOld/TV show.
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

        View view = inflater.inflate(com.fourthwardmobile.android.movingpictures.R.layout.fragment_movie_reviews, container, false);

        mListView = (ListView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.reviewsListView);
        mListView.setEmptyView(view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.emptyReviewsLayout));

        Intent i = getActivity().getIntent();

        mReviewList = (ArrayList) i.getParcelableArrayListExtra(EXTRA_REVIEW_LIST);
        if (mListView != null) {

            ReviewsListAdapter adapter = new ReviewsListAdapter(getActivity(), mReviewList);
            mListView.setAdapter(adapter);
        }
        return view;
    }

}
