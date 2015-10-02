package com.android.fourthwardcoder.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.popularmovies.data.MovieContract;
import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.helpers.Util;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Movie;
import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.models.Video;
import com.android.fourthwardcoder.popularmovies.adapters.VideosListAdapter;
import com.android.fourthwardcoder.popularmovies.activities.ReviewsActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;


/**
 * Class MovieDetailFragment
 * Author: Chris Hare
 * Created: 7/26/2015
 * <p/>
 * Fragment to show the details of a particular movie
 */
public class MovieDetailFragment extends Fragment implements Constants {

    /*****************************************************************/
    /*                        Constants                              */
    /*****************************************************************/
    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    /*****************************************************************/
    /*                        Local Data                             */
    /*****************************************************************/
    Movie mMovie;
    int mMovieId;
    ListView mListView;
    VideosListAdapter mVideoListAdapter;

    ImageView mBackdropImageView;
    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mReleaseYearTextView;
    TextView mRuntimeTextView;
    TextView mRatingTextView;
    TextView mDirectorTextView;
    TextView mCastTextView;
    TextView mReleaseDateTextView;
    ExpandableTextView mOverviewTextView;
    TextView mGenreTextView;
    TextView mRevenueTextView;
    TextView mReviewsTextView;
    LinearLayout mVideoLayout;
    CheckBox mFavoritesToggleButton;

    /*****************************************************************/
    /*                       Constructor                             */

    /*****************************************************************/
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

//        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //Get Movie passed from Main Activity
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovieId = arguments.getInt(EXTRA_MOVIE_ID);
        }
        //mMovieId = getActivity().getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);

        //Set image views
        mBackdropImageView = (ImageView) view.findViewById(R.id.backdropImageView);
        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);

        mFavoritesToggleButton = (CheckBox) view.findViewById(R.id.favoritesToggleButton);
        mFavoritesToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mMovie != null) {
                    String toastStr = "";
                    if (isChecked) {

                        toastStr = getString(R.string.added) + " " + mMovie.getTitle() + " "
                                + getString(R.string.to_favorites);
                        addMovieToFavoritesDb();

                    } else {
                        toastStr = getString(R.string.removed) + " " + mMovie.getTitle() + " "
                                + getString(R.string.from_favorites);
                        removeMovieFromDb();
                    }
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            toastStr, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        if (checkIfFavorite())
            mFavoritesToggleButton.setChecked(true);
        else
            mFavoritesToggleButton.setChecked(false);

        //Set textviews with Movie details
        mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) view.findViewById(R.id.releaseYearTextView);
        mRuntimeTextView = (TextView) view.findViewById(R.id.runtimeTextView);
        mRatingTextView = (TextView) view.findViewById(R.id.ratingTextView);
        mDirectorTextView = (TextView) view.findViewById(R.id.directorTextView);
        mCastTextView = (TextView) view.findViewById(R.id.castTextView);
        mReleaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);
        mOverviewTextView = (ExpandableTextView) view.findViewById(R.id.overviewContentExpandableTextView);
        mGenreTextView = (TextView) view.findViewById(R.id.genreTextView);
        mRevenueTextView = (TextView) view.findViewById(R.id.revenueTextView);
        mReviewsTextView = (TextView) view.findViewById(R.id.reviewsTextView);

        mReviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ReviewsActivity.class);
                i.putExtra(EXTRA_MOVIE_ID, mMovie.getId());
                i.putExtra(EXTRA_ENT_TYPE, TYPE_MOVIE);
                startActivity(i);
            }
        });

        mListView = (ListView) view.findViewById(R.id.videosListView);
        mListView.setScrollContainer(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Video video = mVideoListAdapter.getItem(position);

                Uri youtubeUri = Uri.parse(MovieDbAPI.BASE_YOUTUBE_URL).buildUpon()
                        .appendPath(MovieDbAPI.PATH_WATCH)
                        .appendQueryParameter(MovieDbAPI.PARAM_V, video.getKey())
                        .build();

                Log.e(TAG, "Youtube path: " + youtubeUri.toString());

                Intent i = new Intent(Intent.ACTION_VIEW, youtubeUri);

                startActivity(i);
            }
        });

        mVideoLayout = (LinearLayout) view.findViewById(R.id.videosLayout);

        if (mListView != null) {
            if(Util.isNetworkAvailable(getActivity())) {
                new FetchMovieTask().execute(mMovieId);
            }
            else {
                Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                connectToast.show();
            }
        }

        return view;
    }

    /**
     * Add this Movie to the Favorites DB
     */
    private void addMovieToFavoritesDb() {

        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieId);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, mMovie.getPosterPath());

        //Insert Movie data to the content provider
        Uri inserted = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
    }

    /**
     * Remove this Movie from the Favorites DB
     */
    private void removeMovieFromDb() {

        //Put togeter SQL selection
        String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(mMovieId);

        //Remove movie data from the content provider
        int deletedRow = getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, selection, selectionArgs);
    }

    /**
     * Check if this movie is in the Favorites Database
     *
     * @return If movie is in the Favorites DB
     */
    private boolean checkIfFavorite() {

        //Get projection with Movie ID
        String[] projection =
                {
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID
                };

        //Put together SQL selection
        String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(mMovieId);

        //Return cursor to the row that contains the movie
        Cursor cursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        if (cursor != null) {

            //If the cursor is empty, than the movie is not in the DB; else it is in the DB
            if (cursor.getCount() < 1)
                return false;
            else
                return true;
        }

        //Something went wrong. Just return false.
        return false;
    }

    /**
     * Adjust the size of the nexted listview that contains the videos list
     *
     * @param myListView listview of Videos
     */
    private void setListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    /*********************************************************************/
    /*                         Inner Classes                             */

    /*********************************************************************/
    private class FetchMovieTask extends AsyncTask<Integer, Void, Movie> {

        //ProgressDialog to be displayed while the data is being fetched and parsed
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            //Start ProgressDialog on Main Thread UI before precessing begins
            progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.progress_downloading_movies), true);
        }

        @Override
        protected Movie doInBackground(Integer... params) {

            //Get ID of movie
            int movieId = params[0];

            //Query and build Movie Object
            return MovieDbAPI.getMovie(movieId);
        }

        @Override
        protected void onPostExecute(Movie movie) {

            //Done processing the movie query, kill Progress Dialog on main UI
            progressDialog.dismiss();

            if (getActivity() != null && mListView != null) {
                if (movie != null) {
                    mMovie = movie;
                    //Set title of Movie on Action Bar
                    getActivity().setTitle(mMovie.getTitle());

                    Picasso.with(getActivity()).load(mMovie.getBackdropPath()).into(mBackdropImageView);
                    Picasso.with(getActivity()).load(mMovie.getPosterPath()).into(mPosterImageView);

                    mTitleTextView.setText(mMovie.getTitle());
                    mReleaseYearTextView.setText(mMovie.getReleaseYear());
                    mRuntimeTextView.setText(mMovie.getRuntime() + " min");
                    mRatingTextView.setText(String.valueOf(mMovie.getRating()) + "/10");

                    Spanned director = Html.fromHtml("<b>" + getString(R.string.director) + "</b>" + " " +
                            mMovie.getDirectorString());
                    mDirectorTextView.setText(director);

                    Util.setCastLinks(getActivity(), mMovie, mCastTextView, TYPE_MOVIE);

                    Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
                            Util.reverseDateString(mMovie.getReleaseDate()));
                    mReleaseDateTextView.setText(releaseDate);

                    Spanned synopsis = Html.fromHtml("<b>" + getString(R.string.synopsis) + "</b>" + " " +
                            mMovie.getOverview());
                    mOverviewTextView.setText(synopsis);

                    Spanned genre = Html.fromHtml("<b>" + getString(R.string.genre) + "</b>" + " " +
                            mMovie.getGenreString());

                    mGenreTextView.setText(genre);

                    Spanned revenue = Html.fromHtml("<b>" + getString(R.string.revenue) + "</b>" + " " +
                            mMovie.getRevenue());
                    mRevenueTextView.setText(revenue);

                    if (movie.getVideoList().size() > 0) {
                        mVideoListAdapter = new VideosListAdapter(getActivity(), movie.getVideoList());
                        mListView.setAdapter(mVideoListAdapter);
                        setListViewSize(mListView);
                    } else
                        mVideoLayout.setVisibility(View.GONE);
                }
            }
        }
    }
}
