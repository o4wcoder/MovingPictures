package com.android.fourthwardcoder.movingpictures.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.adapters.VideoListAdapter;
//import com.android.fourthwardcoder.movingpictures.adapters.VideosListAdapter;
import com.android.fourthwardcoder.movingpictures.data.MovieContract;
import com.android.fourthwardcoder.movingpictures.helpers.ImageTransitionListener;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Cast;
import com.android.fourthwardcoder.movingpictures.models.Credits;
import com.android.fourthwardcoder.movingpictures.models.Movie;
import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.models.Video;
import com.android.fourthwardcoder.movingpictures.models.VideoList;
import com.android.fourthwardcoder.movingpictures.models.VideoOld;
import com.android.fourthwardcoder.movingpictures.activities.ReviewsActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Class MovieDetailFragment
 * Author: Chris Hare
 * Created: 7/26/2015
 * <p>
 * Fragment to show the details of a particular movie
 */
public class MovieDetailFragment extends Fragment implements Constants {

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

        //Get MovieOld passed from Main Activity
        Bundle arguments = getArguments();
        if (arguments != null) {

//            if (arguments.containsKey(EXTRA_MOVIE)) {
//                //Got MovieOld Object from Main Activity
//                mMovie = arguments.getParcelable(EXTRA_MOVIE);
//                Log.e(TAG,"Got movie id from args = "+ mMovie.getId());
//            } else {
            //Got MovieOld ID, will need to fetch data
            mMovieId = arguments.getInt(EXTRA_MOVIE_ID);
            Log.e(TAG, "onCreateView(): got movie id = " + mMovieId);

            mFetchData = true;

            //  }
        }


        //Get CollapsingToolbarLayout
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        //Get NestedScrollView;
        mNestedScrollView = (NestedScrollView) view.findViewById(R.id.scrollview);

        //Set image views
        mBackdropImageView = (ImageView) view.findViewById(R.id.backdropImageView);

        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);

        mDetailCardView = (CardView) view.findViewById(R.id.movie_detail_cardview);
        mDetailLayout = (RelativeLayout) view.findViewById(R.id.layout_movie_detail);

        mFavoritesFAB = (FloatingActionButton) view.findViewById(R.id.favorites_fab);
        mFavoritesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMovie != null) {
                    String toastStr = "";
                    if (v.getTag().equals(false)) {
                        Log.e(TAG, "Set to favorite");
                        mFavoritesFAB.setTag(true);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.yellow));
                        toastStr = getString(R.string.added) + " " + mMovie.getTitle() + " "
                                + getString(R.string.to_favorites);
                        addMovieToFavoritesDb();

                    } else {
                        Log.e(TAG, "remove from favorite");
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
                    Log.e(TAG, "Tansition start");
                    mFavoritesFAB.setVisibility(View.GONE);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    Log.e(TAG, "Transition end. Scan in FAB");
                    mFavoritesFAB.setVisibility(View.VISIBLE);
                    if (getActivity() != null) {
                        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                                R.anim.scale_in_image);
                        mFavoritesFAB.startAnimation(scaleAnimation);
                    }
                }
            });
        }


        //Set up VideoOld RecyclerView for Horizontal Scrolling
        mVideosRecylerView = (RecyclerView) view.findViewById(R.id.movie_detail_video_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mVideosRecylerView.setLayoutManager(layoutManager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white, null));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "Back pressed");

                    //Kill this activity
                    getActivity().finish();
                }
            });
        }

        //Set textviews with MovieOld details
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

                Util.showCastListActivity(getActivity(), mMovie, ENT_TYPE_MOVIE);
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
        if (mFetchData) {
            if (mVideosRecylerView != null) {
                if (Util.isNetworkAvailable(getActivity())) {
                    // new FetchMovieTask().execute(mMovieId);
                    getMovie(mMovieId);
                } else {
                    Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                    connectToast.show();
                }
            }
        } else {
            //Got the entire MovieOld object passed to fragment. Just set the layout.
            Log.e(TAG,"Set layout without getting movie id!!");
            setLayout();
        }

        View.OnClickListener castClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.equals(mCast1ImageView)) {
                      Util.startActorDetailActivity(getActivity(),mMovie.getCredits().getCast().get(0).getId(),mCast1ImageView);
                } else if (v.equals(mCast2ImageView)) {
                    Util.startActorDetailActivity(getActivity(),mMovie.getCredits().getCast().get(1).getId(),mCast2ImageView);
                } else if (v.equals(mCast3ImageView)) {
                    Util.startActorDetailActivity(getActivity(),mMovie.getCredits().getCast().get(2).getId(),mCast3ImageView);
                }
            }
        };

        //Get the 3 Top Billed Cast layouts and child views
        mCastCardView = (CardView) view.findViewById(R.id.castLayout);
        View cast1View = view.findViewById(R.id.detail_cast_layout1);
        mCast1ImageView = (ImageView) cast1View.findViewById(R.id.cast_thumb_image_view);
        mCast1ImageView.setOnClickListener(castClickListener);
        mCast1TextView = (TextView) cast1View.findViewById(R.id.cast_thumb_text_view);

        View cast2View = view.findViewById(R.id.detail_cast_layout2);
        mCast2ImageView = (ImageView) cast2View.findViewById(R.id.cast_thumb_image_view);
        mCast2ImageView.setOnClickListener(castClickListener);
        mCast2TextView = (TextView) cast2View.findViewById(R.id.cast_thumb_text_view);

        View cast3View = view.findViewById(R.id.detail_cast_layout3);
        mCast3ImageView = (ImageView) cast3View.findViewById(R.id.cast_thumb_image_view);
        mCast3ImageView.setOnClickListener(castClickListener);
        mCast3TextView = (TextView) cast3View.findViewById(R.id.cast_thumb_text_view);

        //Fill in list of videos
        getVideos(mMovieId);

        return view;
    }

    private void getMovie(int id) {

        Log.e(TAG, "getMovie() with movie id = " + id);
        Call<Movie> call = MovieDbAPI.getMovieApiService().getMovie(id);

        Log.e(TAG, "Call url = " + call.request().url());

        call.enqueue(new retrofit2.Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse()");
                    mMovie = response.body();
                    //Now get movie credits
                    getCredits(mMovieId);

                } else {

                    //!!!TODO. Do something with the failed response
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG, "onFailure() " + t.getMessage());

            }
        });
    }



    private void getVideos(int id) {

    Call<VideoList> call = MovieDbAPI.getMovieApiService().getVideoList(id);
    call.enqueue(new retrofit2.Callback<VideoList>()

    {
        @Override
        public void onResponse (Call < VideoList > call, Response < VideoList > response){
        if (response.isSuccessful()) {

            if (response.body().getVideos().size() > 0) {
                mVideoListAdapter = new VideoListAdapter(getActivity(), (ArrayList) response.body().getVideos(), new VideoListAdapter.VideoListAdapterOnClickHandler() {
                    @Override
                    public void onVideoClick(Video video, VideoListAdapter.VideoListAdapterViewHolder vh) {

                        //Get youtube url from video and send it to view intent
                        Uri youtubeUri = MovieDbAPI.buildYoutubeUri(video);
                        Intent i = new Intent(Intent.ACTION_VIEW, youtubeUri);

                        startActivity(i);
                    }
                });
                mVideosRecylerView.setAdapter(mVideoListAdapter);

            } else {
                mVideoLayout.setVisibility(View.GONE);
            }
        } else {

        }
    }

        @Override
        public void onFailure (Call < VideoList > call, Throwable t){

    }
    }

    );
}
    private void getCredits(int id) {

        Call<Credits> call = MovieDbAPI.getMovieApiService().getCredits(id);
        Log.e(TAG,"getCredits() Inside");
        call.enqueue(new retrofit2.Callback<Credits>() {

            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                Log.e(TAG,"getCredits() onResponse");
                if(response.isSuccessful()) {


                    Credits credits = response.body();
                    if(mMovie != null) {
                        mMovie.setCredits(credits);
                        //We should have all the details of the movie now. Set the layout.
                        setLayout();
                        setCastLayout(credits);
                    }
                }
                else {
                    //!!!TODO. Do something with the failed response
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


//        //Dont' display the share menu option if there are no videos to share
//        if(mMovie.getVideos() != null) {
//            if (mMovie.getVideos().size() > 0) {
//                inflater.inflate(R.menu.menu_share, menu);
//
//                //Retrieve teh share menu item
//                MenuItem menuItem = menu.findItem(R.id.action_share);
//
//                //Get the provider and hold onto it to set/change the share intent.
//                ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat
//                        .getActionProvider(menuItem);
//
//                //Attach and intent to this ShareActionProvider
//                if (shareActionProvider != null) {
//                    shareActionProvider.setShareIntent(Util.createShareVideoIntent(getActivity(), mMovie));
//                } else {
//                    Log.e(TAG, "Share Action Provider is null!");
//                }
//            }
//        }
    }


    /**
     * Add this MovieOld to the Favorites DB
     */
    private void addMovieToFavoritesDb() {

//        ContentValues movieValues = mMovie.getContentValues();
//
//        //Insert MovieOld data to the content provider
//        Uri inserted = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
    }

    /**
     * Remove this MovieOld from the Favorites DB
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

        //Get projection with MovieOld ID
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
     * Set the layout of the Fragment
     */
    private void setLayout() {

        Log.e(TAG,"setLayout() Inside");
        if (getActivity() != null && mVideosRecylerView != null) {
          //  if (mMovie != null) {
                 //  Log.e(TAG,"movie ")
                //Got the data, can create share menu if there are videos
                //   setHasOptionsMenu(true);
                //Set title of MovieOld on Action Bar
              //  getActivity().setTitle(mMovie.getTitle());
                Log.e(TAG,"setLayout() call Picasso to pull backgdrop");
                Picasso.with(getActivity()).load(MovieDbAPI.getFullBackdropPath(mMovie.getBackdropPath())).into(mBackdropImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                         Log.e(TAG,"Loaded backdrop");
                        //Set up color scheme
                        setPaletteColors();
                        //Start Shared Image transition now that we have the backdrop
                        startPostponedEnterTransition();

                    }

                    @Override
                    public void onError() {
                        //Just get the default image since there was not backdrop image available
                        Log.e(TAG,"Picasso onError()!!!");
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

                Picasso.with(getActivity()).load(MovieDbAPI.getFullPosterPath(mMovie.getPosterPath())).into(mPosterImageView);

                mFavoritesFAB.setContentDescription(getString(R.string.acc_movie_details_favorite_button));
                mTitleTextView.setText(mMovie.getTitle());
                mReleaseYearTextView.setText(mMovie.getReleaseYear());
//
                mRuntimeTextView.setText(mMovie.getRuntime() + " min");
                mRatingTextView.setText(String.valueOf(mMovie.getVoteAverage()) + "/10");
            //    mRatingTextView.setContentDescription(getString(R.string.acc_movie_rating, mMovie.getVoteAverage(), "10"));
//
                Spanned director = Html.fromHtml("<b>" + getString(R.string.director) + "</b>" + " " +
                        Util.buildPersonListString(mMovie.getCredits().getDirectorList()));
                mDirectorTextView.setText(director);
//
//                //Util.setCastLinks(getActivity(), mMovie, mCastTextView, ENT_TYPE_MOVIE);
//
                Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
                        Util.reverseDateString(mMovie.getReleaseDate()));
                mReleaseDateTextView.setText(releaseDate);

                Spanned synopsis = Html.fromHtml("<b>" + getString(R.string.synopsis) + "</b>" + " " +
                        mMovie.getOverview());
                mOverviewTextView.setText(synopsis);

               Spanned genre = Html.fromHtml("<b>" + getString(R.string.genre) + "</b>" + " " +
                        mMovie.getGenreListString());

                mGenreTextView.setText(genre);

                if(mMovie.getRevenue() >0) {
                    Spanned revenue = Html.fromHtml("<b>" + getString(R.string.revenue) + "</b>" + " " +
                            mMovie.getRevenue());
                    mRevenueTextView.setText(revenue);
                }
                else {
                    mRevenueTextView.setVisibility(View.GONE);
                }

//
//                //        //See if this is a favorite movie and set the state of the star button
//                Log.e(TAG, "Check if favorite movie");
//                if (checkIfFavorite()) {
//                    //  mFavoritesToggleButton.setChecked(true);
//                    Log.e(TAG, "Already favorite, set tag to true");
//                    mFavoritesFAB.setTag(true);
//                    mFavoritesFAB.setColorFilter(getResources().getColor(R.color.yellow));
//                } else {
//                    //  mFavoritesToggleButton.setChecked(false);
//                    Log.e(TAG, "Not favorite, set tag to false");
//                    mFavoritesFAB.setTag(false);
//                    mFavoritesFAB.setColorFilter(getResources().getColor(R.color.white));
//                }
//
//                Log.e(TAG,"Start up FetchPersonTask!!!");
//                new FetchPersonTask().execute(mMovie);
            }
       // }

    }

    private void setCastLayout(Credits credits) {


        ArrayList<Cast> castList = (ArrayList) credits.getCast();

        if (castList != null) {
            Log.e(TAG,"setCast List. Cast list not null with size = " + castList.size());
            if (castList.size() >= 3) {
                getCastThumbnails(castList.get(0).getProfilePath(), mCast1ImageView);
                getCastThumbnails(castList.get(1).getProfilePath(), mCast2ImageView);
                getCastThumbnails(castList.get(2).getProfilePath(), mCast3ImageView);
                mCast1TextView.setText(castList.get(0).getName());
                mCast2TextView.setText(castList.get(1).getName());
                mCast3TextView.setText(castList.get(2).getName());
            } else if (castList.size() == 2) {
                getCastThumbnails(castList.get(0).getProfilePath(), mCast1ImageView);
                getCastThumbnails(castList.get(1).getProfilePath(), mCast2ImageView);
                mCast1TextView.setText(castList.get(0).getName());
                mCast2TextView.setText(castList.get(1).getName());
            } else if (castList.size() == 3) {
                getCastThumbnails(castList.get(0).getProfilePath(), mCast1ImageView);
                mCast1TextView.setText(castList.get(0).getName());
            }
        } else {
            //Did not return any cast. Don't show cast card.
            mCastCardView.setVisibility(View.GONE);
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
        Log.e(TAG,"startPostponedEnterTransition() Inside");
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


    private void getCastThumbnails(String uri, final ImageView imageView) {

        Picasso.with(getActivity()).load(MovieDbAPI.getFullPosterPath(uri)).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.e(TAG,"getCastThumbnail() onSuccess()");
            }

            @Override
            public void onError() {
                Log.e(TAG,"getCastThumbnail() onError()");
                imageView.setImageResource(R.drawable.person_no_pic_thumnail);
            }
        });
    }
    /*********************************************************************/
    /*                         Inner Classes                             */
    /*********************************************************************/
//    private class FetchMovieTask extends AsyncTask<Integer, Void, Movie> {
//
//        //ProgressDialog to be displayed while the data is being fetched and parsed
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//
//            //Start ProgressDialog on Main Thread UI before precessing begins
//            progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.progress_downloading_movies), true);
//        }
//
//        @Override
//        protected MovieOld doInBackground(Integer... params) {
//
//            //Get ID of movie
//            int movieId = params[0];
//             Log.e(TAG,"Fetch MovieOld TAsk doInBackground with movie id " + movieId);
//            //Query and build MovieOld Object
//            return MovieDbAPI.getMovie(movieId);
//        }
//
//        @Override
//        protected void onPostExecute(MovieOld movie) {
//
//            //Done processing the movie query, kill Progress Dialog on main UI
//            progressDialog.dismiss();
//
//            mMovie = movie;
//            setLayout();
//
//
//        }
//    }

//    private class FetchPersonTask extends AsyncTask<MovieOld, Void, ArrayList<Person>> {
//
//        @Override
//        protected ArrayList<Person> doInBackground(MovieOld... params) {
//
//            ArrayList<Person> personList = null;
//            //Get ID of movie
//            MovieOld movie = params[0];
//            int numOfActors = 3;
//
//            if(movie.getActors() != null) {
//                if (movie.getActors().size() < 3)
//                    numOfActors = movie.getActors().size();
//
//                personList = new ArrayList<>(numOfActors);
//                for (int i = 0; i < numOfActors; i++) {
//                    Person person = MovieDbAPI.getPerson(movie.getActors().get(i).getId());
//                    if (person != null)
//                        personList.add(person);
//
//                }
//
//                return personList;
//            }
//
//            return null;
//
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Person> personList) {
//
//            Log.e(TAG,"Set cast image and textviews");
//
//            if(personList != null) {
//                if (personList.size() == 3) {
//                    getCastThumbnails(personList.get(0).getProfileImagePath(), mCast1ImageView);
//                    getCastThumbnails(personList.get(1).getProfileImagePath(), mCast2ImageView);
//                    getCastThumbnails(personList.get(2).getProfileImagePath(), mCast3ImageView);
//                    mCast1TextView.setText(personList.get(0).getName());
//                    mCast2TextView.setText(personList.get(1).getName());
//                    mCast3TextView.setText(personList.get(2).getName());
//                } else if (personList.size() == 2) {
//                    getCastThumbnails(personList.get(0).getProfileImagePath(), mCast1ImageView);
//                    getCastThumbnails(personList.get(1).getProfileImagePath(), mCast2ImageView);
//                    mCast1TextView.setText(personList.get(0).getName());
//                    mCast2TextView.setText(personList.get(1).getName());
//                } else if (personList.size() == 3) {
//                    getCastThumbnails(personList.get(0).getProfileImagePath(), mCast1ImageView);
//                    mCast1TextView.setText(personList.get(0).getName());
//                }
//            } else {
//                //Did not return any cast. Don't show cast card.
//                mCastCardView.setVisibility(View.GONE);
//            }
//
//        }
//
//    }
}
