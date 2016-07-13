package com.fourthwardmobile.android.movingpictures.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.adapters.ReviewListAdapter;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.Review;

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
   // ListView mListView;

    ArrayList<Review> mReviewList;

    public ReviewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_all_list, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.show_all_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        Intent i = getActivity().getIntent();

        mReviewList = (ArrayList) i.getParcelableArrayListExtra(EXTRA_REVIEW_LIST);
        if (recyclerView != null) {

            ReviewListAdapter adapter = new ReviewListAdapter(getActivity(), mReviewList);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

}
