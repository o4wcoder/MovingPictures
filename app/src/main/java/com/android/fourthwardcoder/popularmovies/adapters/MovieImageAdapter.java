package com.android.fourthwardcoder.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.models.SimpleMovie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Class MovieImageAdapter
 * Author: Chris Hare
 * Created: 8/27/2015
 * <p/>
 * ArrayAdapter used for the GridView that displays the movie posters on the main activity
 */
public class MovieImageAdapter extends ArrayAdapter<SimpleMovie> {

    /****************************************************************/
    /*                       Constants                              */
    /****************************************************************/
    private static final String TAG = MovieImageAdapter.class.getSimpleName();
    /****************************************************************/
    /*                       Local Data                             */
    /****************************************************************/
    Context mContext;

    public MovieImageAdapter(Context context, ArrayList<SimpleMovie> movies) {
        super(context, 0, movies);

        mContext = context;

    }

    //Override the getView to return an ImageView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.movie_image, parent, false);

            //Get imageView
            holder.imageView = (ImageView) convertView.findViewById(R.id.movie_imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Get each Movie using the position in the ArrayAdapter
        SimpleMovie movie = getItem(position);

        //Call Picasso to load it into the imageView
        Picasso.with(mContext).load(movie.getPosterPath()).into(holder.imageView);

        return convertView;
    }

    /**********************************************************************/
    /*                          Inner Classes                             */

    /**********************************************************************/
    private static class ViewHolder {

        ImageView imageView;
    }
}
