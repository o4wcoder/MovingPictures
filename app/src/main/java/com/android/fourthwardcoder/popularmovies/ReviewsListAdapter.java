package com.android.fourthwardcoder.popularmovies;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chare on 8/26/2015.
 */
public class ReviewsListAdapter extends ArrayAdapter<Review> {

    /**********************************************************************/
    /*                           Local Data                               */
    /**********************************************************************/
    private ArrayList<Review> mReviewList;
    private Context mContext;

    public ReviewsListAdapter(Context context, ArrayList<Review> reviewList) {
        super(context,0,reviewList);
        mReviewList = reviewList;
        mContext = context;

    }

    //Override the getView to return an ImageView
    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    }
    */
    /**********************************************************************/
    /*                          Inner Classes                             */
    /**********************************************************************/
    private static class ViewHolder {

        TextView textView;
    }
}
