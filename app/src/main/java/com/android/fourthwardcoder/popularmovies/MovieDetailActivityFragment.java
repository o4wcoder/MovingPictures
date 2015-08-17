package com.android.fourthwardcoder.popularmovies;

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
        Movie movie = getActivity().getIntent().getParcelableExtra(PopularMoviesMainFragment.EXTRA_MOVIE);

        //Set title of Movie on Action Bar
        getActivity().setTitle(movie.getTitle());

        //Set image views
        ImageView backdropImageView = (ImageView)view.findViewById(R.id.backdropImageView);
        Picasso.with(getActivity()).load(movie.getBackdropPath()).into(backdropImageView);

        ImageView posterImageView = (ImageView)view.findViewById(R.id.posterImageView);
        Picasso.with(getActivity()).load(movie.getPosterPath()).into(posterImageView);

        //Set textviews with Movie details
        TextView titleTextView = (TextView)view.findViewById(R.id.titleTextView);
        titleTextView.setText(movie.getTitle());

        TextView releaseYearTextView = (TextView)view.findViewById(R.id.releaseYearTextView);
        releaseYearTextView.setText(movie.getReleaseYear());

        TextView runtimeTextView = (TextView)view.findViewById(R.id.runtimeTextView);
        runtimeTextView.setText(movie.getRuntime() + " min");

        TextView ratingTextView = (TextView)view.findViewById(R.id.ratingTextView);
        ratingTextView.setText(String.valueOf(movie.getRating()) + "/10");

        TextView directorTextView = (TextView)view.findViewById(R.id.directorTextView);
        Spanned director = Html.fromHtml("<b>" + getString(R.string.director) + "</b>" + " " +
                movie.getDirectorString());
        directorTextView.setText(director);

        TextView castTextView = (TextView)view.findViewById(R.id.castTextView);
        Spanned cast = Html.fromHtml("<b>" + getString(R.string.cast) + "</b>" + " " +
                movie.getActorsString());
        castTextView.setText(cast);

        TextView releaseDateTextView = (TextView)view.findViewById(R.id.releaseDateTextView);
        Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
                movie.getReleaseDate());
        releaseDateTextView.setText(releaseDate);

        TextView overviewTextView = (TextView)view.findViewById(R.id.overviewTextView);
        Spanned synopsis = Html.fromHtml("<b>" + getString(R.string.synopsis) + "</b>" + " " +
                movie.getOverview());
        overviewTextView.setText(synopsis);

        TextView genreTextView = (TextView)view.findViewById(R.id.genreTextView);
        Spanned genre = Html.fromHtml("<b>" + getString(R.string.genre) + "</b>" + " " +
                movie.getGenreString());
        genreTextView.setText(genre);

        TextView revenueTextView = (TextView)view.findViewById(R.id.revenueTextView);
        Spanned revenue = Html.fromHtml("<b>" + getString(R.string.revenue) + "</b>" + " " +
                movie.getRevenue());
        revenueTextView.setText(revenue);
        return view;
    }
}
