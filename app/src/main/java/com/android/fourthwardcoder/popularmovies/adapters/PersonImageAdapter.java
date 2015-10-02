package com.android.fourthwardcoder.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.fourthwardcoder.popularmovies.models.PersonPhoto;
import com.android.fourthwardcoder.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Class PersonImageAdapter
 * Author: Chris Hare
 * Created: 9/1/2015
 * <p/>
 * Adapter to show a person's image
 */
public class PersonImageAdapter extends ArrayAdapter<PersonPhoto> {

    /****************************************************************/
    /*                       Constants                              */
    /****************************************************************/
    private static final String TAG = PersonImageAdapter.class.getSimpleName();
    /****************************************************************/
    /*                       Local Data                             */
    /****************************************************************/
    Context mContext;

    public PersonImageAdapter(Context context, ArrayList<PersonPhoto> photos) {
        super(context, 0, photos);

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
        PersonPhoto photo = getItem(position);

        //Call Picasso to load it into the imageView
        Picasso.with(mContext).load(photo.getThumbnailImagePath()).into(holder.imageView);

        return convertView;
    }

    /**********************************************************************/
    /*                          Inner Classes                             */

    /**********************************************************************/
    private static class ViewHolder {

        ImageView imageView;
    }
}
