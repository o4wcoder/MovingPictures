package com.android.fourthwardcoder.movingpictures.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.models.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Class VideosListAdapter
 * Author: Chris Hare
 * Created: 8/28/2015
 * <p/>
 * List Adapater for Videos of a Movie or TV Show.
 */
public class VideosListAdapter extends ArrayAdapter<Video> {

    /****************************************************************/
    /*                         Constants                            */
    /****************************************************************/
    private final static String TAG = VideosListAdapter.class.getSimpleName();
    /****************************************************************/
    /*                         Local Data                           */
    /****************************************************************/
    Context mContext;

    public VideosListAdapter(Context context, ArrayList<Video> videoList) {
        super(context, 0, videoList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.video_list_item, parent, false);
            //Get Video TextView info
            holder.videoThumbImageView = (ImageView)convertView.findViewById(R.id.videoThumbImageView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.videoNameTextView);
            //holder.infoTextView = (TextView) convertView.findViewById(R.id.videoInfoTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Get each Movie using the position in the ArrayAdapter
        Video video = getItem(position);

        Picasso.with(mContext).load(MovieDbAPI.buildYoutubeThumbnailUri(video)).into(holder.videoThumbImageView);
        holder.nameTextView.setText(video.getName());
//        holder.infoTextView.setText(video.getType() + ": " + String.valueOf(video.getSize()) + "p");

        return convertView;
    }

    /**********************************************************************/
    /*                          Inner Classes                             */

    /**********************************************************************/
    private static class ViewHolder {

        ImageView videoThumbImageView;
        TextView nameTextView;
        TextView infoTextView;
    }
}
