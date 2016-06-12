package com.android.fourthwardcoder.movingpictures.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.models.MovieBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 6/8/2016.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private static String TAG = MovieListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<MovieBasic> mMovieList;
    private MovieListAdapter.MovieListAdapterOnClickHandler mClickHandler;

    public MovieListAdapter(Context context, ArrayList<MovieBasic> movieList, MovieListAdapterOnClickHandler clickHandler) {

        mContext = context;
        mMovieList = movieList;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item,parent,false);
        final MovieListAdapterViewHolder vh = new MovieListAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder holder, int postition) {


       MovieBasic movie = mMovieList.get(postition);
       // Log.e(TAG,"onBindViewHolder with movie poster = " + movie.getPosterPath());
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

        public MovieListAdapterViewHolder(View view) {
            super(view);

            movieThumbImageView = (ImageView)view.findViewById(R.id.movie_thumbnail_image_view);
            movieThumbImageView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            mClickHandler.onMovieClick(mMovieList.get((getAdapterPosition())),this);
        }
    }

    public interface MovieListAdapterOnClickHandler {
        void onMovieClick(MovieBasic movie, MovieListAdapterViewHolder vh);
    }
}
