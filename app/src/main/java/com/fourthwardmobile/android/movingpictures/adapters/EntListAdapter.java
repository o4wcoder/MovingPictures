package com.fourthwardmobile.android.movingpictures.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.MediaBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 6/8/2016.
 */
public class EntListAdapter extends RecyclerView.Adapter<EntListAdapter.MovieListAdapterViewHolder> {

    private static String TAG = EntListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<MediaBasic> mMovieList;
    private @Constants.EntertainmentType int mEntType;
    private EntListAdapter.MovieListAdapterOnClickHandler mClickHandler;

    public EntListAdapter(Context context, ArrayList<MediaBasic> movieList, @Constants.EntertainmentType int entType, MovieListAdapterOnClickHandler clickHandler) {

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


       MediaBasic movie = mMovieList.get(postition);

        if(mEntType == Constants.ENT_TYPE_PERSON) {
            Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(movie.getProfilePath())).into(holder.movieThumbImageView);

           // holder.imageGradient.setVisibility(View.VISIBLE);
            holder.personNameTextView.setVisibility(View.VISIBLE);
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

        public CardView movieCardView;
        public ImageView movieThumbImageView;
        public TextView personNameTextView;
      //  public View imageGradient;

        public MovieListAdapterViewHolder(View view) {
            super(view);

            movieCardView = (CardView)view.findViewById(R.id.ent_cardview);
            view.setOnClickListener(this);
            movieThumbImageView = (ImageView)view.findViewById(R.id.movie_thumbnail_image_view);
           // movieThumbImageView.setOnClickListener(this);

            personNameTextView = (TextView)view.findViewById(R.id.ent_list_person_name_textview);
          //  imageGradient = (View)view.findViewById(R.id.ent_list_image_gradient);

        }
        @Override
        public void onClick(View v) {
            Log.e(TAG,"onClick()");
            mClickHandler.onClick(mMovieList.get((getAdapterPosition())),this);
        }
    }

    public interface MovieListAdapterOnClickHandler {
        void onClick(MediaBasic movie, MovieListAdapterViewHolder vh);
    }
}
