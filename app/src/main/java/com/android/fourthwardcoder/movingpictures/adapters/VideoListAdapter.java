package com.android.fourthwardcoder.movingpictures.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.models.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 6/5/2016.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListAdapterViewHolder> {

    /****************************************************************************************/
    /*                                    Constants                                         */
    /****************************************************************************************/
    private static final String TAG = VideoListAdapter.class.getSimpleName();

    /****************************************************************************************/
    /*                                    Local Data                                        */
    /****************************************************************************************/
    private Context mContext;
    private ArrayList<Video> mVideoList;
    private VideoListAdapterOnClickHandler mClickHandler;

    public VideoListAdapter(Context context, ArrayList<Video> videoList, VideoListAdapterOnClickHandler clickHandler) {

        mContext = context;
        mVideoList = videoList;
        mClickHandler = clickHandler;

    }
    @Override
    public VideoListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.video_list_item,parent,false);
        final VideoListAdapterViewHolder vh = new VideoListAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(VideoListAdapterViewHolder holder, int position) {

        Video video = mVideoList.get(position);
        Picasso.with(mContext).load(MovieDbAPI.buildYoutubeThumbnailUri(video)).into(holder.videoThumbImageView);
        holder.nameTextView.setText(video.getName());

    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    /****************************************************************************************/
    /*                                     Inner Classes                                    */
    /****************************************************************************************/
    public class VideoListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView videoThumbImageView;
        TextView nameTextView;
        TextView infoTextView;

        public VideoListAdapterViewHolder(View view) {
            super(view);

            videoThumbImageView = (ImageView)view.findViewById(R.id.videoThumbImageView);
            videoThumbImageView.setOnClickListener(this);
            nameTextView = (TextView)view.findViewById(R.id.videoNameTextView);

        }

        @Override
        public void onClick(View v) {
            mClickHandler.onVideoClick(mVideoList.get((getAdapterPosition())),this);
        }
    }

    public interface VideoListAdapterOnClickHandler {
        void onVideoClick(Video video, VideoListAdapterViewHolder vh);
    }
}
