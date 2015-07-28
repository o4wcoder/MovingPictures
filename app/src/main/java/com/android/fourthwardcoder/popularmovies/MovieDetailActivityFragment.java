package com.android.fourthwardcoder.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    /*****************************************************************/
    /*                        Constants                              */
    /*****************************************************************/
    private static final String TAG = MovieDetailActivityFragment.class.getSimpleName();

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //Get Movie passed from Main Activity
        Movie movie = getActivity().getIntent().getParcelableExtra(PopularMoviesMainFragment.EXTRA_MOVIE);
        Log.e(TAG, "Got Movie in Detail" + movie.getTitle());

        Log.e(TAG,"Backdrop: " + movie.getBackdropPath());

        getActivity().setTitle(movie.getTitle());

        ImageView backdropImageView = (ImageView)view.findViewById(R.id.backdropImageView);
        Picasso.with(getActivity()).load(movie.getBackdropPath()).into(backdropImageView);


        TextView overviewTextView = (TextView)view.findViewById(R.id.overviewTextView);
        overviewTextView.setText(movie.getOverview());

        TextView releaseDateTextView = (TextView)view.findViewById(R.id.releaseDateTextView);
        releaseDateTextView.setText(movie.getReleaseDate());

        return view;
    }
}
