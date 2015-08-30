package com.android.fourthwardcoder.popularmovies;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chare on 8/26/2015.
 */
public class ReviewsListAdapter extends ArrayAdapter<Review> {

    /**********************************************************************/
    /*                           Constants                                */
    /**********************************************************************/

    /**********************************************************************/
    /*                           Local Data                               */
    /**********************************************************************/
    private Context mContext;

    public ReviewsListAdapter(Context context, ArrayList<Review> reviewList) {
        super(context,0,reviewList);

        mContext = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.reviews_list_item, parent, false);
            //Get imageView
            holder.textView = (TextView) convertView.findViewById(R.id.reviewsTextView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        //Get each Movie using the position in the ArrayAdapter
        Review review = getItem(position);

       Spanned reviewStr = Html.fromHtml("<b>" + mContext.getString(R.string.author) + "</b>" + " " +
                review.getAuthor() + "<br><br>" + review.getContent());

                holder.textView.setText(reviewStr);


        return convertView;
    }

    /**********************************************************************/
    /*                          Inner Classes                             */
    /**********************************************************************/
    private static class ViewHolder {

        TextView textView;
    }
}
