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
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.MovieBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 6/8/2016.
 */
public class EntListAdapter extends RecyclerView.Adapter<EntListAdapter.MovieListAdapterViewHolder> {

    private static String TAG = EntListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<MovieBasic> mMovieList;
    private @Constants.EntertainmentType int mEntType;
    private EntListAdapter.MovieListAdapterOnClickHandler mClickHandler;

    public EntListAdapter(Context context, ArrayList<MovieBasic> movieList, @Constants.EntertainmentType int entType,  MovieListAdapterOnClickHandler clickHandler) {

        mContext = context;
        mMovieList = movieList;
        mEntType = entType;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.ent_list_item,parent,false);
        final MovieListAdapterViewHolder vh = new MovieListAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder holder, int postition) {


       MovieBasic movie = mMovieList.get(postition);
       // Log.e(TAG,"onBindViewHolder with movie poster = " + movie.getPosterPath());
        if(mEntType == Constants.ENT_TYPE_PERSON) {
            Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(movie.getProfilePath())).into(holder.movieThumbImageView);
            Log.e(TAG,"Person name = " + movie.getName());
            holder.imageGradient.setVisibility(View.VISIBLE);
            holder.personNameTextView.setText(movie.getName());
        }
        else
            Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(movie.getPosterPath())).into(holder.movieThumbImageView);
    }

    @Override
    public int getItemCount() {

        return mMovieList.size();
    }

    /***********************************************************************************************/
    /*                                    Inner Classes                                            */
    /***********************************************************************************************/
    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView movieThumbImageView;
        public TextView personNameTextView;
        public View imageGradient;

        public MovieListAdapterViewHolder(View view) {
            super(view);

            movieThumbImageView = (ImageView)view.findViewById(R.id.movie_thumbnail_image_view);
            movieThumbImageView.setOnClickListener(this);

            personNameTextView = (TextView)view.findViewById(R.id.ent_list_person_name_textview);
            imageGradient = (View)view.findViewById(R.id.ent_list_image_gradient);

        }
        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mMovieList.get((getAdapterPosition())),this);
        }
    }

    public interface MovieListAdapterOnClickHandler {
        void onClick(MovieBasic movie, MovieListAdapterViewHolder vh);
    }
}
