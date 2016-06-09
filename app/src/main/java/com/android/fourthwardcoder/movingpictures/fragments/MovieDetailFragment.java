package com.android.fourthwardcoder.movingpictures.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.activities.CastListActivity;
import com.android.fourthwardcoder.movingpictures.adapters.VideoListAdapter;
import com.android.fourthwardcoder.movingpictures.data.MovieContract;
import com.android.fourthwardcoder.movingpictures.helpers.ImageTransitionListener;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Movie;
import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.models.Person;
import com.android.fourthwardcoder.movingpictures.models.Video;
import com.android.fourthwardcoder.movingpictures.activities.ReviewsActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Class MovieDetailFragment
 * Author: Chris Hare
 * Created: 7/26/2015
 * <p/>
 * Fragment to show the details of a particular movie
 */
public class MovieDetailFragment extends Fragment implements Constants,
        VideoListAdapter.VideoListAdapterOnClickHandler{

    /*****************************************************************/
    /*                        Constants                              */
    /*****************************************************************/
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private static final int IMAGE_FADE_DURATION = 500;
   // private int mMutedColor = 0xFF333333;
    /*****************************************************************/
    /*                        Local Data                             */
    /*****************************************************************/
    Movie mMovie;
    int mMovieId;
    //ListView mListView;
    //VideosListAdapter mVideoListAdapter;
    VideoListAdapter mVideoListAdapter;

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

    TextView mCastShowAllTextView;
   // CheckBox mFavoritesToggleButton;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private NestedScrollView mNestedScrollView;
    FloatingActionButton mFavoritesFAB;
    RelativeLayout mDetailLayout;

    CardView mVideoLayout;
    CardView mDetailCardView;
    CardView mCastCardView;
    RecyclerView mVideosRecylerView;

    //Cast Image and Text
    ImageView mCast1ImageView;
    ImageView mCast2ImageView;
    ImageView mCast3ImageView;
    TextView mCast1TextView;
    TextView mCast2TextView;
    TextView mCast3TextView;

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

        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);

        mDetailCardView = (CardView)view.findViewById(R.id.movie_detail_cardview);
        mDetailLayout = (RelativeLayout) view.findViewById(R.id.layout_movie_detail);

        mFavoritesFAB = (FloatingActionButton)view.findViewById(R.id.favorites_fab);
        mFavoritesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMovie != null) {
                    String toastStr = "";
                    if (v.getTag().equals(false)) {
                        Log.e(TAG,"Set to favorite");
                        mFavoritesFAB.setTag(true);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.yellow));
                        toastStr = getString(R.string.added) + " " + mMovie.getTitle() + " "
                                + getString(R.string.to_favorites);
                        addMovieToFavoritesDb();

                    } else {
                        Log.e(TAG,"remove from favorite");
                        mFavoritesFAB.setTag(false);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.white));
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

            getActivity().getWindow().getSharedElementEnterTransition().addListener(new ImageTransitionListener() {

                @Override
                public void onTransitionStart(Transition transition) {
                    Log.e(TAG,"Tansition start");
                   mFavoritesFAB.setVisibility(View.GONE);
                }
                @Override
                public void onTransitionEnd(Transition transition) {
                    Log.e(TAG,"Transition end. Scan in FAB");
                  mFavoritesFAB.setVisibility(View.VISIBLE);
                    if(getActivity() != null) {
                        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                                R.anim.scale_in_image);
                        mFavoritesFAB.startAnimation(scaleAnimation);
                    }
                }
            });
        }


        //Set up Video RecyclerView for Horizontal Scrolling
        mVideosRecylerView = (RecyclerView)view.findViewById(R.id.movie_detail_video_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mVideosRecylerView.setLayoutManager(layoutManager);

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
        mOverviewTextView = (ExpandableTextView) view.findViewById(R.id.detail_overview_exp_text_view);
        mGenreTextView = (TextView) view.findViewById(R.id.genreTextView);
        mRevenueTextView = (TextView) view.findViewById(R.id.revenueTextView);
        mReviewsTextView = (TextView) view.findViewById(R.id.reviewsTextView);
        mCastShowAllTextView = (TextView) view.findViewById(R.id.detail_cast_show_all_textview);
        mCastShowAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.showCastListActivity(getActivity(),mMovie,ENT_TYPE_MOVIE);
            }
        });

        mReviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ReviewsActivity.class);
                i.putExtra(EXTRA_MOVIE_ID, mMovie.getId());
                i.putExtra(EXTRA_ENT_TYPE, ENT_TYPE_MOVIE);
                startActivity(i);
            }
        });


        mVideoLayout = (CardView) view.findViewById(R.id.videosLayout);

        //If we just got the movie id, we need to go and fetch the data
        if(mFetchData) {
            if (mVideosRecylerView != null) {
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

        View.OnClickListener castClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.equals(mCast1ImageView)) {
                      Util.startActorDetailActivity(getActivity(),mMovie.getActors().get(0).getId(),mCast1ImageView);
                } else if (v.equals(mCast2ImageView)) {
                    Util.startActorDetailActivity(getActivity(),mMovie.getActors().get(1).getId(),mCast2ImageView);
                } else if (v.equals(mCast3ImageView)) {
                    Util.startActorDetailActivity(getActivity(),mMovie.getActors().get(2).getId(),mCast3ImageView);
                }
            }
        };

        //Get the 3 Top Billed Cast layouts and child views
        mCastCardView = (CardView)view.findViewById(R.id.castLayout);
        View cast1View = view.findViewById(R.id.detail_cast_layout1);
        mCast1ImageView = (ImageView)cast1View.findViewById(R.id.cast_thumb_image_view);
        mCast1ImageView.setOnClickListener(castClickListener);
        mCast1TextView = (TextView)cast1View.findViewById(R.id.cast_thumb_text_view);

        View cast2View = view.findViewById(R.id.detail_cast_layout2);
        mCast2ImageView = (ImageView)cast2View.findViewById(R.id.cast_thumb_image_view);
        mCast2ImageView.setOnClickListener(castClickListener);
        mCast2TextView = (TextView)cast2View.findViewById(R.id.cast_thumb_text_view);

        View cast3View = view.findViewById(R.id.detail_cast_layout3);
        mCast3ImageView = (ImageView)cast3View.findViewById(R.id.cast_thumb_image_view);
        mCast3ImageView.setOnClickListener(castClickListener);
        mCast3TextView = (TextView)cast3View.findViewById(R.id.cast_thumb_text_view);

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
//    private void setListViewSize(ListView myListView) {
//        ListAdapter myListAdapter = myListView.getAdapter();
//        if (myListAdapter == null) {
//            //do nothing return null
//            return;
//        }
//        //set listAdapter in loop for getting final size
//        int totalHeight = 0;
//        for (int size = 0; size < myListAdapter.getCount(); size++) {
//            View listItem = myListAdapter.getView(size, null, myListView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        //setting listview item in adapter
//        ViewGroup.LayoutParams params = myListView.getLayoutParams();
//        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
//        myListView.setLayoutParams(params);
//        // print height of adapter on log
//        Log.i("height of listItem:", String.valueOf(totalHeight));
//    }

    /**
     * Set the layout of the Fragment
     */
    private void setLayout() {

        if (getActivity() != null && mVideosRecylerView != null) {
            if (mMovie != null) {

                //Got the data, can create share menu if there are videos
                //   setHasOptionsMenu(true);
                //Set title of Movie on Action Bar
              //  getActivity().setTitle(mMovie.getTitle());

                Picasso.with(getActivity()).load(mMovie.getBackdropPath()).into(mBackdropImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                        //Set up color scheme
                        setPaletteColors();
                        //Start Shared Image transition now that we have the backdrop
                        startPostponedEnterTransition();

                    }

                    @Override
                    public void onError() {
                        //Just get the default image since there was not backdrop image available
                        Picasso.with(getActivity()).load(R.drawable.movie_thumbnail).into(mBackdropImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                //set up color scheme
                                setPaletteColors();
                                //Still want to start shared transition even it the backdrop image was not loaded.
                                startPostponedEnterTransition();
                            }

                            @Override
                            public void onError() {
                                //If we failed here, it's bad. Just do the shared transition as to not freeze up the UI
                                startPostponedEnterTransition();
                            }
                        });

                    }
                });
                ;
                Picasso.with(getActivity()).load(mMovie.getPosterPath()).into(mPosterImageView);

                mFavoritesFAB.setContentDescription(getString(R.string.acc_movie_details_favorite_button));
                mTitleTextView.setText(mMovie.getTitle());
                mReleaseYearTextView.setText(mMovie.getReleaseYear());

                mRuntimeTextView.setText(mMovie.getRuntime() + " min");
                mRatingTextView.setText(String.valueOf(mMovie.getRating()) + "/10");
                mRatingTextView.setContentDescription(getString(R.string.acc_movie_rating, mMovie.getRating(), "10"));

                Spanned director = Html.fromHtml("<b>" + getString(R.string.director) + "</b>" + " " +
                        mMovie.getDirectorString());
                mDirectorTextView.setText(director);

                //Util.setCastLinks(getActivity(), mMovie, mCastTextView, ENT_TYPE_MOVIE);

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
                    Log.e(TAG, "setLayout(): Got some videos, setup adapters");
//                    mVideoListAdapter = new VideosListAdapter(getActivity(), mMovie.getVideos());
//                    mListView.setAdapter(mVideoListAdapter);
//                    setListViewSize(mListView);

                    mVideoListAdapter = new VideoListAdapter(getActivity(), mMovie.getVideos(), this);
                    mVideosRecylerView.setAdapter(mVideoListAdapter);
                } else
                    mVideoLayout.setVisibility(View.GONE);


                //        //See if this is a favorite movie and set the state of the star button
                Log.e(TAG, "Check if favorite movie");
                if (checkIfFavorite()) {
                    //  mFavoritesToggleButton.setChecked(true);
                    Log.e(TAG, "Already favorite, set tag to true");
                    mFavoritesFAB.setTag(true);
                    mFavoritesFAB.setColorFilter(getResources().getColor(R.color.yellow));
                } else {
                    //  mFavoritesToggleButton.setChecked(false);
                    Log.e(TAG, "Not favorite, set tag to false");
                    mFavoritesFAB.setTag(false);
                    mFavoritesFAB.setColorFilter(getResources().getColor(R.color.white));
                }

                Log.e(TAG,"Start up FetchPersonTask!!!");
                new FetchPersonTask().execute(mMovie);
            }
        }

    }

    private void setPaletteColors() {

        Bitmap bitmap = ((BitmapDrawable)mBackdropImageView.getDrawable()).getBitmap();

        if(bitmap != null) {
            Palette p = Palette.generate(bitmap, 12);
            //   mMutedColor = p.getDarkMutedColor(0xFF333333);

            //Set title and colors for collapsing toolbar
            mCollapsingToolbarLayout.setTitle(mMovie.getTitle());
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            //Set content descriptioni for toolbar/title
            mCollapsingToolbarLayout.setContentDescription(mMovie.getTitle());

            //Set pallet colors when toolbar is collapsed
            int primaryColor = getResources().getColor(R.color.appPrimaryColor);
            int primaryDarkColor = getResources().getColor(R.color.appDarkPrimaryColor);
            int accentColor = getResources().getColor(R.color.appAccentColor);
            mCollapsingToolbarLayout.setContentScrimColor(p.getMutedColor(primaryColor));
            mCollapsingToolbarLayout.setStatusBarScrimColor(p.getDarkMutedColor(primaryDarkColor));

            mDetailLayout.setBackgroundColor(p.getMutedColor(primaryColor));
            mDetailCardView.setCardBackgroundColor(p.getMutedColor(primaryColor));
            mFavoritesFAB.setBackgroundTintList(ColorStateList.valueOf(p.getVibrantColor(accentColor)));


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

    @Override
    public void onVideoClick(Video video, VideoListAdapter.VideoListAdapterViewHolder vh) {

        //Get youtube url from video and send it to view intent
        Uri youtubeUri = MovieDbAPI.buildYoutubeUri(video);
        Intent i = new Intent(Intent.ACTION_VIEW, youtubeUri);

        startActivity(i);
    }

    private void getCastThumbnails(String uri, final ImageView imageView) {

        Picasso.with(getActivity()).load(uri).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

                imageView.setImageResource(R.drawable.person_no_pic_thumnail);
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

    private class FetchPersonTask extends AsyncTask<Movie, Void, ArrayList<Person>> {

        @Override
        protected ArrayList<Person> doInBackground(Movie... params) {

            ArrayList<Person> personList = null;
            //Get ID of movie
            Movie movie = params[0];
            int numOfActors = 3;

            if(movie.getActors() != null) {
                if (movie.getActors().size() < 3)
                    numOfActors = movie.getActors().size();

                personList = new ArrayList<>(numOfActors);
                for (int i = 0; i < numOfActors; i++) {
                    Person person = MovieDbAPI.getPerson(movie.getActors().get(i).getId());
                    if (person != null)
                        personList.add(person);

                }

                return personList;
            }

            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<Person> personList) {

            Log.e(TAG,"Set cast image and textviews");

            if(personList != null) {
                if (personList.size() == 3) {
                    getCastThumbnails(personList.get(0).getProfileImagePath(), mCast1ImageView);
                    getCastThumbnails(personList.get(1).getProfileImagePath(), mCast2ImageView);
                    getCastThumbnails(personList.get(2).getProfileImagePath(), mCast3ImageView);
                    mCast1TextView.setText(personList.get(0).getName());
                    mCast2TextView.setText(personList.get(1).getName());
                    mCast3TextView.setText(personList.get(2).getName());
                } else if (personList.size() == 2) {
                    getCastThumbnails(personList.get(0).getProfileImagePath(), mCast1ImageView);
                    getCastThumbnails(personList.get(1).getProfileImagePath(), mCast2ImageView);
                    mCast1TextView.setText(personList.get(0).getName());
                    mCast2TextView.setText(personList.get(1).getName());
                } else if (personList.size() == 3) {
                    getCastThumbnails(personList.get(0).getProfileImagePath(), mCast1ImageView);
                    mCast1TextView.setText(personList.get(0).getName());
                }
            } else {
                //Did not return any cast. Don't show cast card.
                mCastCardView.setVisibility(View.GONE);
            }

        }

    }
}
