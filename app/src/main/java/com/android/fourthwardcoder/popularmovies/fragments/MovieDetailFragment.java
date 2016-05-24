package com.android.fourthwardcoder.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.popularmovies.activities.MovieDetailActivity;
import com.android.fourthwardcoder.popularmovies.data.MovieContract;
import com.android.fourthwardcoder.popularmovies.helpers.ImageTransitionListener;
import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.helpers.Util;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Movie;
import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.models.Video;
import com.android.fourthwardcoder.popularmovies.adapters.VideosListAdapter;
import com.android.fourthwardcoder.popularmovies.activities.ReviewsActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
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
    private static final int IMAGE_FADE_DURATION = 500;
    private int mMutedColor = 0xFF333333;
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
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private NestedScrollView mNestedScrollView;

    boolean mFetchData = false;

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

        //Get Movie passed from Main Activity
        Bundle arguments = getArguments();
        if (arguments != null) {

            if (arguments.containsKey(EXTRA_MOVIE)) {
                //Got Movie Object from Main Activity
                mMovie = arguments.getParcelable(EXTRA_MOVIE);
            } else {
                //Got Movie ID, will need to fetch data
                mMovieId = arguments.getInt(EXTRA_MOVIE_ID);
                mFetchData = true;

            }
        }

        //Get CollapsingToolbarLayout
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
        //Get NestedScrollView;
        mNestedScrollView = (NestedScrollView)view.findViewById(R.id.scrollview);

        //Set image views
        mBackdropImageView = (ImageView) view.findViewById(R.id.backdropImageView);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mBackdropImageView.setAlpha(0f);
//            getActivity().getWindow().getSharedElementEnterTransition().addListener(new ImageTransitionListener() {
//                @Override
//                public void onTransitionEnd(Transition transition) {
//                    mBackdropImageView.animate().setDuration(IMAGE_FADE_DURATION).alpha(1f);
//                }
//            });
//        }
        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);


        mFavoritesToggleButton = (CheckBox) view.findViewById(R.id.favoritesToggleButton);

        //See if this is a favorite movie and set the state of the star button
        if(mMovie != null) {
            if (checkIfFavorite())
                mFavoritesToggleButton.setChecked(true);
            else
                mFavoritesToggleButton.setChecked(false);
        }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white, null));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG,"Back pressed");

                    //Kill this activity
                    getActivity().finish();
                }
            });
        }

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
                i.putExtra(EXTRA_ENT_TYPE, ENT_TYPE_MOVIE);
                startActivity(i);
            }
        });

        mListView = (ListView) view.findViewById(R.id.videosListView);
        mListView.setScrollContainer(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get youtube url from video and send it to view intent
                Video video = mVideoListAdapter.getItem(position);
                Uri youtubeUri = MovieDbAPI.buildYoutubeUri(video);
                Intent i = new Intent(Intent.ACTION_VIEW, youtubeUri);

                startActivity(i);
            }
        });

        mVideoLayout = (LinearLayout) view.findViewById(R.id.videosLayout);

        //If we just got the movie id, we need to go and fetch the data
        if(mFetchData) {
            if (mListView != null) {
                if (Util.isNetworkAvailable(getActivity())) {
                    new FetchMovieTask().execute(mMovieId);
                } else {
                    Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                    connectToast.show();
                }
            }
        }
        else {
            //Got the entire Movie object passed to fragment. Just set the layout.
            setLayout();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        //Dont' display the share menu option if there are no videos to share
        if(mMovie.getVideos() != null) {
            if (mMovie.getVideos().size() > 0) {
                inflater.inflate(R.menu.menu_share, menu);

                //Retrieve teh share menu item
                MenuItem menuItem = menu.findItem(R.id.action_share);

                //Get the provider and hold onto it to set/change the share intent.
                ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat
                        .getActionProvider(menuItem);

                //Attach and intent to this ShareActionProvider
                if (shareActionProvider != null) {
                    shareActionProvider.setShareIntent(Util.createShareVideoIntent(getActivity(), mMovie));
                } else {
                    Log.e(TAG, "Share Action Provider is null!");
                }
            }
        }
    }




    /**
     * Add this Movie to the Favorites DB
     */
    private void addMovieToFavoritesDb() {

        ContentValues movieValues = mMovie.getContentValues();

        //Insert Movie data to the content provider
        Uri inserted = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
    }

    /**
     * Remove this Movie from the Favorites DB
     */
    private void removeMovieFromDb() {

        //Put togeter SQL selection
        String selection = MovieContract.MovieEntry.COLUMN_ID + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(mMovie.getId());

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
                        MovieContract.MovieEntry.COLUMN_ID
                };

        //Put together SQL selection
        String selection = MovieContract.MovieEntry.COLUMN_ID + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(mMovie.getId());

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

    /**
     * Set the layout of the Fragment
     */
    private void setLayout() {

        if (getActivity() != null && mListView != null) {
            if (mMovie != null) {

                //Got the data, can create share menu if there are videos
             //   setHasOptionsMenu(true);
                //Set title of Movie on Action Bar
                getActivity().setTitle(mMovie.getTitle());

                Picasso.with(getActivity()).load(mMovie.getBackdropPath()).into(mBackdropImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                        Bitmap bitmap = ((BitmapDrawable)mBackdropImageView.getDrawable()).getBitmap();

                        if(bitmap != null) {
                            Palette p = Palette.generate(bitmap, 12);
                            mMutedColor = p.getDarkMutedColor(0xFF333333);

                            //Set title and colors for collapsing toolbar
                            mCollapsingToolbarLayout.setTitle(mMovie.getTitle());
                            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

                            //Set content descriptioni for toolbar/title
                            mCollapsingToolbarLayout.setContentDescription(mMovie.getTitle());

                            //Set pallet colors when toolbar is collapsed
                            int primaryColor = getResources().getColor(R.color.appPrimaryColor);
                            int primaryDarkColor = getResources().getColor(R.color.appDarkPrimaryColor);
                            mCollapsingToolbarLayout.setContentScrimColor(p.getMutedColor(primaryColor));
                            mCollapsingToolbarLayout.setStatusBarScrimColor(p.getDarkMutedColor(primaryDarkColor));
                        }
                        Log.e(TAG,"onSuccess() Got background image. Call support startPostponedEnterTransition");
                        startPostponedEnterTransition();

                    }

                    @Override
                    public void onError() {

                    }
                });;
                Picasso.with(getActivity()).load(mMovie.getPosterPath()).into(mPosterImageView);

                mFavoritesToggleButton.setContentDescription(getString(R.string.acc_movie_details_favorite_button));
                mTitleTextView.setText(mMovie.getTitle());
                mReleaseYearTextView.setText(mMovie.getReleaseYear());

                mRuntimeTextView.setText(mMovie.getRuntime() + " min");
                mRatingTextView.setText(String.valueOf(mMovie.getRating()) + "/10");
                mRatingTextView.setContentDescription(getString(R.string.acc_movie_rating,mMovie.getRating(),"10"));

                Spanned director = Html.fromHtml("<b>" + getString(R.string.director) + "</b>" + " " +
                        mMovie.getDirectorString());
                mDirectorTextView.setText(director);

                Util.setCastLinks(getActivity(), mMovie, mCastTextView, ENT_TYPE_MOVIE);

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

                if (mMovie.getVideos().size() > 0) {
                    mVideoListAdapter = new VideosListAdapter(getActivity(), mMovie.getVideos());
                    mListView.setAdapter(mVideoListAdapter);
                    setListViewSize(mListView);
                } else
                    mVideoLayout.setVisibility(View.GONE);


            }
        }

    }

    private void startPostponedEnterTransition() {
        mPosterImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.e(TAG,"onPreDraw(): Start postponed enter transition!!!!");
                mPosterImageView.getViewTreeObserver().removeOnPreDrawListener(this);

                //Must call this inside a PreDrawListener or the Enter Transition will not work
                //Need to make sure imageview is ready before starting transition.
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
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
             Log.e(TAG,"Fetch Movie TAsk doInBackground with movie id " + movieId);
            //Query and build Movie Object
            return MovieDbAPI.getMovie(movieId);
        }

        @Override
        protected void onPostExecute(Movie movie) {

            //Done processing the movie query, kill Progress Dialog on main UI
            progressDialog.dismiss();

            mMovie = movie;
            setLayout();


        }
    }
}
