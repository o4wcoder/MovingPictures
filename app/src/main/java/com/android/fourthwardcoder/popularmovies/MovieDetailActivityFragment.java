package com.android.fourthwardcoder.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Class MovieDetailActivityFragment
 * Author: Chris Hare
 * Created: 7/26/2015
 *
 * Fragment to show the details of a particular movie
 */
public class MovieDetailActivityFragment extends Fragment {

    /*****************************************************************/
    /*                        Constants                              */
    /*****************************************************************/
    private static final String TAG = MovieDetailActivityFragment.class.getSimpleName();
    public static final String EXTRA_MOVIE_ID = "com.android.fourthwardcoder.popularmovies.extra_movie_id";

    /*****************************************************************/
    /*                        Local Data                             */
    /*****************************************************************/
    Movie mMovie;

    /*****************************************************************/
    /*                       Constructor                             */
    /*****************************************************************/
    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //Get Movie passed from Main Activity
        mMovie = getActivity().getIntent().getParcelableExtra(PopularMoviesMainFragment.EXTRA_MOVIE);

        //Set title of Movie on Action Bar
        getActivity().setTitle(mMovie.getTitle());

        //Set image views
        ImageView backdropImageView = (ImageView)view.findViewById(R.id.backdropImageView);
        Picasso.with(getActivity()).load(mMovie.getBackdropPath()).into(backdropImageView);

        ImageView posterImageView = (ImageView)view.findViewById(R.id.posterImageView);
        Picasso.with(getActivity()).load(mMovie.getPosterPath()).into(posterImageView);

        //Set textviews with Movie details
        TextView titleTextView = (TextView)view.findViewById(R.id.titleTextView);
        titleTextView.setText(mMovie.getTitle());

        TextView releaseYearTextView = (TextView)view.findViewById(R.id.releaseYearTextView);
        releaseYearTextView.setText(mMovie.getReleaseYear());

        TextView runtimeTextView = (TextView)view.findViewById(R.id.runtimeTextView);
        runtimeTextView.setText(mMovie.getRuntime() + " min");

        TextView ratingTextView = (TextView)view.findViewById(R.id.ratingTextView);
        ratingTextView.setText(String.valueOf(mMovie.getRating()) + "/10");

        TextView directorTextView = (TextView)view.findViewById(R.id.directorTextView);
        Spanned director = Html.fromHtml("<b>" + getString(R.string.director) + "</b>" + " " +
                mMovie.getDirectorString());
        directorTextView.setText(director);

        TextView castTextView = (TextView)view.findViewById(R.id.castTextView);
        Spanned cast = Html.fromHtml("<b>" + getString(R.string.cast) + "</b>" + " " +
                mMovie.getActorsString());
        castTextView.setText(cast);

        TextView releaseDateTextView = (TextView)view.findViewById(R.id.releaseDateTextView);
        Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
                mMovie.getReleaseDate());
        releaseDateTextView.setText(releaseDate);

        TextView overviewTextView = (TextView)view.findViewById(R.id.overviewTextView);
        Spanned synopsis = Html.fromHtml("<b>" + getString(R.string.synopsis) + "</b>" + " " +
                mMovie.getOverview());
        overviewTextView.setText(synopsis);

        TextView genreTextView = (TextView)view.findViewById(R.id.genreTextView);
        Spanned genre = Html.fromHtml("<b>" + getString(R.string.genre) + "</b>" + " " +
                mMovie.getGenreString());
        genreTextView.setText(genre);

        TextView revenueTextView = (TextView)view.findViewById(R.id.revenueTextView);
        Spanned revenue = Html.fromHtml("<b>" + getString(R.string.revenue) + "</b>" + " " +
                mMovie.getRevenue());
        revenueTextView.setText(revenue);

        TextView reviewsTextView = (TextView)view.findViewById(R.id.reviewsTextView);

        reviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MovieReviewsActivity.class);
                i.putExtra(EXTRA_MOVIE_ID,mMovie.getId());
                startActivity(i);
            }
        });
        return view;
    }
}
