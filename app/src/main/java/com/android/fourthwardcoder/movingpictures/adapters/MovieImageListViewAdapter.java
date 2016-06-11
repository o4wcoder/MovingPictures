package com.android.fourthwardcoder.movingpictures.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.models.MovieOld;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Class MovieImageListViewAdapter
 * Author: Chris Hare
 * Created: 8/27/2015
 * <p/>
 * ArrayAdapter used for the GridView that displays the movie posters on the main activity
 */
public class MovieImageListViewAdapter extends ArrayAdapter<MovieOld> {

    /****************************************************************/
    /*                       Constants                              */
    /****************************************************************/
    private static final String TAG = MovieImageListViewAdapter.class.getSimpleName();
    /****************************************************************/
    /*                       Local Data                             */
    /****************************************************************/
    Context mContext;

    public MovieImageListViewAdapter(Context context, ArrayList<MovieOld> movies) {
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

        //Get each MovieOld using the position in the ArrayAdapter
        MovieOld movie = getItem(position);
        //Log.e(TAG, "Setting movie position " + position + " name: " + movie.getTitle());

        //Set Accesability Info
        holder.imageView.setContentDescription(movie.getTitle());
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
