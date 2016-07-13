package com.fourthwardmobile.android.movingpictures.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.models.Review;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 7/13/2016.
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewListAdapterViewHolder>{

    private Context mContext;
    private ArrayList<Review> mReviewList;

    public ReviewListAdapter(Context context, ArrayList<Review> reviewList) {

        mContext = context;
        mReviewList = reviewList;
    }

    @Override
    public ReviewListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewTyupe) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.reviews_list_item,parent,false);
        final ReviewListAdapterViewHolder vh = new ReviewListAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ReviewListAdapterViewHolder holder, int position) {

        Review review = mReviewList.get(position);
        Spanned reviewStr = Html.fromHtml("<b>" + mContext.getString(com.fourthwardmobile.android.movingpictures.R.string.author) + "</b>" + " " +
                review.getAuthor() + "<br><br>" + review.getContent());
        holder.reviewTextView.setText(reviewStr);

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewListAdapterViewHolder extends RecyclerView.ViewHolder  {

        TextView reviewTextView;

        public ReviewListAdapterViewHolder(View view) {
            super(view);

            reviewTextView = (TextView)view.findViewById(R.id.reviewsTextView);
        }

    }


}
