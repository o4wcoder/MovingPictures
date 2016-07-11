package com.fourthwardmobile.android.movingpictures.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.fourthwardmobile.android.movingpictures.models.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 6/30/2016.
 */
public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileListAdapterViewHolder> {

    private Context mContext;
    private ArrayList<Profile> mProfileList;
    private ProfileListAdapterOnClickHandler mClickHandler;

    public ProfileListAdapter(Context context, ArrayList<Profile> profileList,
                               ProfileListAdapterOnClickHandler clickHandler) {

        mContext = context;
        mProfileList = profileList;
        mClickHandler = clickHandler;
    }

    @Override
    public ProfileListAdapter.ProfileListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(com.fourthwardmobile.android.movingpictures.R.layout.profile_image,parent,false);
        final ProfileListAdapterViewHolder vh = new ProfileListAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProfileListAdapter.ProfileListAdapterViewHolder holder, int position) {

        Profile profile = mProfileList.get(position);
        Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(profile.getFilePath())).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mProfileList.size();
    }

    public class ProfileListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public ProfileListAdapterViewHolder(View view) {
            super(view);

            imageView = (ImageView)view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.movie_imageView);
            imageView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            mClickHandler.onProfileClick(getAdapterPosition(),this);
        }
    }
    public interface ProfileListAdapterOnClickHandler {
        void onProfileClick(int position, ProfileListAdapterViewHolder vh);
    }
}
