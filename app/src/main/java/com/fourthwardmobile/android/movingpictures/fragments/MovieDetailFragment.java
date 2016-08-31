package com.fourthwardmobile.android.movingpictures.fragments;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fourthwardmobile.android.movingpictures.MovingPicturesApplication;
import com.fourthwardmobile.android.movingpictures.activities.MovieDetailActivity;
import com.fourthwardmobile.android.movingpictures.activities.SearchableActivity;
import com.fourthwardmobile.android.movingpictures.adapters.VideoListAdapter;
//import com.android.fourthwardmobile.movingpictures.adapters.VideosListAdapter;
import com.fourthwardmobile.android.movingpictures.helpers.ImageTransitionListener;
import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.fourthwardmobile.android.movingpictures.helpers.Util;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.Cast;
import com.fourthwardmobile.android.movingpictures.models.Movie;
import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.models.ReleaseDateList;
import com.fourthwardmobile.android.movingpictures.models.Video;
import com.fourthwardmobile.android.movingpictures.activities.ReviewsActivity;
import com.fourthwardmobile.android.movingpictures.network.NetworkService;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Class MovieDetailFragment
 * Author: Chris Hare
 * Created: 7/26/2015
 * <p>
 * Fragment to show the details of a particular movie
 */
public class MovieDetailFragment extends BaseDetailFragment implements Constants, Toolbar.OnMenuItemClickListener {

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



    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mReleaseYearTextView;
    TextView mRuntimeTextView;
    TextView mRatingTextView;
    TextView mCertificationTextView;
    TextView mCastTextView;
    TextView mReleaseDateTextView;
    ExpandableTextView mOverviewTextView;
    TextView mGenreTextView;
    TextView mRevenueTextView;
    TextView mReviewsTextView;

    CardView mReviewsCardView;

    //Videos
    CardView mVideoLayout;
    RecyclerView mVideosRecylerView;
    VideoListAdapter mVideoListAdapter;

    //Cast
    CardView mCastCardView;
    FrameLayout mCast1FrameView;
    FrameLayout mCast2FrameView;
    FrameLayout mCast3FrameView;

    ImageView mCast1ImageView;
    ImageView mCast2ImageView;
    ImageView mCast3ImageView;

    TextView mCast1TextView;
    TextView mCast2TextView;
    TextView mCast3TextView;
    TextView mCastShowAllTextView;

    //Crew
    TextView mDirectorListTextView;
    TextView mWriterListTextView;
    TextView mCrewShowAllTextView;

    boolean mFetchData = false;

    private NetworkService mNetworkService;
    private Subscription mMovieSubscription;


    private static final String ARG_ID = "id";
    /*****************************************************************/
    /*                       Constructor                             */
    /*****************************************************************/
    public MovieDetailFragment() {

    }

    public static MovieDetailFragment newInstance(int id) {

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ID,id);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get reference to applications network service interface
        mNetworkService = ((MovingPicturesApplication)getActivity().getApplication()).getNetworkService();

        if(savedInstanceState != null) {

            mMovie = savedInstanceState.getParcelable(EXTRA_MOVIE);
        }
        else {
            //Get Movie passed from Main Activity
            Bundle arguments = getArguments();
            if (arguments != null) {

                //Got MovieO ID, will need to fetch data
                mMovieId = arguments.getInt(ARG_ID);
                mFetchData = true;
            }
        }

        //This is a Movie type
        mEntType = ENT_TYPE_MOVIE;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view;
        Log.e(TAG,"onCreateView()");
        //Set up back UP navigation arrow
        if(getActivity() instanceof MovieDetailActivity) {

            view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

            //Set Options normal options menu for not tablet/two pane mode
            setHasOptionsMenu(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white, null));
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "Back pressed");

                        //Kill this activity
                        getActivity().finish();
                    }
                });
            }
            //Set Toolbar so we can see menu options
            ((MovieDetailActivity) getActivity()).setSupportActionBar(mToolbar);

        } else {
            view = inflater.inflate(R.layout.fragment_movie_detail_two_pane, container, false);
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

            mToolbar.inflateMenu(R.menu.menu_share);
            mToolbar.setOnMenuItemClickListener(this);
        }

        //Create animations when shared element transition is finished
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

        //Get CollapsingToolbarLayout
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        mDetailCardView = (CardView) view.findViewById(R.id.color_detail_cardview);
        mDetailLayout = (RelativeLayout) view.findViewById(R.id.color_detail_layout);

        //Set image views
        mBackdropImageView = (ImageView) view.findViewById(R.id.backdropImageView);
        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);



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
                        Util.addToFavoritesDb(getActivity(),mMovie.getContentValues());

                    } else {
                        Log.e(TAG, "remove from favorite");
                        mFavoritesFAB.setTag(false);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.white));
                        toastStr = getString(R.string.removed) + " " + mMovie.getTitle() + " "
                                + getString(R.string.from_favorites);
                        Util.removeFromFavoritesDb(getActivity(),mMovie.getId());
                    }
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            toastStr, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        //Set up Video layout and RecyclerView for Horizontal Scrolling
        mVideoLayout = (CardView) view.findViewById(R.id.video_list_layout);
        mVideosRecylerView = (RecyclerView) view.findViewById(R.id.video_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mVideosRecylerView.setLayoutManager(layoutManager);
        mVideosRecylerView.setHasFixedSize(true);
        mVideosRecylerView.setNestedScrollingEnabled(false);



        //Get textviews for Movie Details
        mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) view.findViewById(R.id.releaseYearTextView);
        mRuntimeTextView = (TextView) view.findViewById(R.id.runtimeTextView);
        mRatingTextView = (TextView) view.findViewById(R.id.ratingTextView);
        mCertificationTextView = (TextView) view.findViewById(R.id.certificationTextView);
        mCastTextView = (TextView) view.findViewById(R.id.castTextView);
        mReleaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);
        mOverviewTextView = (ExpandableTextView) view.findViewById(R.id.detail_overview_exp_text_view);
        mGenreTextView = (TextView) view.findViewById(R.id.genreTextView);
        mRevenueTextView = (TextView) view.findViewById(R.id.revenueTextView);
        mReviewsTextView = (TextView) view.findViewById(R.id.reviewsTextView);
        mReviewsCardView = (CardView) view.findViewById(R.id.detail_reviews_layout);







        View.OnClickListener castClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG,"Got onClick from cast listenter");
                if (v.equals(mCast1FrameView)) {
                      Util.startDetailActivity(getActivity(),mMovie.getCredits().getCast().get(0).getId(),ENT_TYPE_PERSON,mCast1FrameView);
                } else if (v.equals(mCast2FrameView)) {
                    Util.startDetailActivity(getActivity(),mMovie.getCredits().getCast().get(1).getId(),ENT_TYPE_PERSON,mCast2FrameView);
                } else if (v.equals(mCast3FrameView)) {
                    Util.startDetailActivity(getActivity(),mMovie.getCredits().getCast().get(2).getId(),ENT_TYPE_PERSON,mCast3FrameView);
                }
            }
        };

        //Get the 3 Top Billed Cast layouts and child views
        mCastCardView = (CardView) view.findViewById(R.id.cast_list_layout);
        View cast1View = view.findViewById(R.id.detail_cast_layout1);
        mCast1ImageView = (ImageView) cast1View.findViewById(R.id.thumb_image_view);
        mCast1FrameView = (FrameLayout)cast1View.findViewById(R.id.thumb_frame);
        mCast1FrameView.setOnClickListener(castClickListener);
        mCast1TextView = (TextView) cast1View.findViewById(R.id.thumb_text_view);

        View cast2View = view.findViewById(R.id.detail_cast_layout2);
        mCast2ImageView = (ImageView) cast2View.findViewById(R.id.thumb_image_view);
        mCast2FrameView = (FrameLayout)cast2View.findViewById(R.id.thumb_frame);
        mCast2FrameView.setOnClickListener(castClickListener);
        mCast2TextView = (TextView) cast2View.findViewById(R.id.thumb_text_view);

        View cast3View = view.findViewById(R.id.detail_cast_layout3);
        mCast3ImageView = (ImageView) cast3View.findViewById(R.id.thumb_image_view);
        mCast3FrameView = (FrameLayout)cast3View.findViewById(R.id.thumb_frame);
        mCast3FrameView.setOnClickListener(castClickListener);
        mCast3TextView = (TextView) cast3View.findViewById(R.id.thumb_text_view);
        mCastShowAllTextView = (TextView) view.findViewById(R.id.detail_cast_show_all_textview);
        mCastShowAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.showListActivity(getActivity(), mMovie.getId(),
                        mMovie.getTitle(), ENT_TYPE_PERSON,LIST_TYPE_MOVIE_CAST);
            }
        });


        mDirectorListTextView = (TextView)view.findViewById(R.id.detail_crew_director_textview);
        mWriterListTextView = (TextView)view.findViewById(R.id.detail_crew_writer_textview);
        mCrewShowAllTextView = (TextView)view.findViewById(R.id.detail_crew_show_all_textview);
        mCrewShowAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showListActivity(getActivity(),mMovie.getId(),
                        mMovie.getTitle(),ENT_TYPE_PERSON,LIST_TYPE_MOVIE_CREW);
            }
        });

        //Now that we have all the views set up we can either go fetch the data or use the saved
        //instance
        //!!! This must be done last !!!

        //If we just got the movie id, we need to go and fetch the data
        if (mFetchData) {
            if (mVideosRecylerView != null) {
                getMovie();
            }
        } else {
            //Got the entire Movie object saved from instance state. Just set the layout.
            Log.e(TAG,"Set layout without getting movie id!!");
            setLayout();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putParcelable(EXTRA_MOVIE, mMovie);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Log.e(TAG,"onOptionsItemSelected() Fragment");
        switch (item.getItemId()) {
            case R.id.action_share:
                   Log.e(TAG,"Menu share click");
                     shareMovie();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Used when calling menu items from Fragment when in two pane/tablet mode. Otherwise
     * it goes through the normal onOptionsItemSelected method
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.e(TAG,"onMenuItemClick()");

        switch(item.getItemId()) {
            case R.id.action_share:
                Log.e(TAG,"Share click in fragment");
                shareMovie();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Clean up Rx Subscription
        if(mMovieSubscription != null && !mMovieSubscription.isUnsubscribed()) {
            mMovieSubscription.unsubscribe();
        }
    }

    private void getMovie() {

        Observable<Movie> movieObservable = mNetworkService.getMovieApiService().getMovie(mMovieId);

        mMovieSubscription = movieObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        //Want to make sure activity is valid before showing any toasts
                        if(getActivity() != null) {
                            mNetworkService.processNetworkError(getContext(),e);
                        }
                    }

                    @Override
                    public void onNext(Movie movie) {
                          //Store global copy of movie
                          mMovie = movie;
                          setLayout();
                    }
                });
    }
    
    /**
     * Set the layout of the Fragment
     */
    private void setLayout() {

        if (getActivity() != null && mMovie != null && mVideosRecylerView != null) {

            Picasso.with(getActivity()).load(MovieDbAPI.getFullBackdropPath(mMovie.getBackdropPath()))
                    .error(R.drawable.movie_backdrop_thumbnail)
                    .into(mBackdropImageView, new Callback() {
                @Override
                public void onSuccess() {

                    //Set up color scheme
                    setPaletteColors(mMovie.getTitle());
                    //Start Shared Image transition now that we have the backdrop
                    startPostponedEnterTransition(mPosterImageView);

                }

                @Override
                public void onError() {

                    //If we can't get a backdrop, then still setup the pallet and start the
                    //postponed transition so the UI does not freeze.

                    //Set up color scheme
                    setPaletteColors(mMovie.getTitle());
                    //Start Shared Image transition now that we have the backdrop
                    startPostponedEnterTransition(mPosterImageView);

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
//                Spanned director = Html.fromHtml("<b>" + getString(R.string.director) + "</b>" + " " +
//                        Util.buildPersonListString(mMovie.getCredits().getDirectorList()));
//                mDirectorTextView.setText(director);
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

            if (mMovie.getRevenue() > 0) {
                Locale locale = new Locale("en", "US");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                String strRevenue = numberFormat.format(mMovie.getRevenue());

                Spanned revenue = Html.fromHtml("<b>" + getString(R.string.revenue) + "</b>" + " " + strRevenue);


                mRevenueTextView.setText(revenue);
            } else {
                mRevenueTextView.setVisibility(View.GONE);
            }

            /*
             * Set up Videos
             */
            if (mMovie.getVideos().getVideos().size() > 0) {
                mVideoListAdapter = new VideoListAdapter(getActivity(), (ArrayList) mMovie.getVideos().getVideos(), new VideoListAdapter.VideoListAdapterOnClickHandler() {
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

            /*
             * Set Up Cast Info
             */
            ArrayList<Cast> castList = (ArrayList) mMovie.getCredits().getCast();

            if (castList != null) {

                if (castList.size() >= 3) {
                    Util.loadPosterThumbnail(getContext(), castList.get(0).getProfilePath(), mCast1ImageView,ENT_TYPE_PERSON);
                    Util.loadPosterThumbnail(getContext(), castList.get(1).getProfilePath(), mCast2ImageView,ENT_TYPE_PERSON);
                    Util.loadPosterThumbnail(getContext(), castList.get(2).getProfilePath(), mCast3ImageView,ENT_TYPE_PERSON);
                    mCast1TextView.setText(castList.get(0).getName());
                    mCast2TextView.setText(castList.get(1).getName());
                    mCast3TextView.setText(castList.get(2).getName());
                } else if (castList.size() == 2) {
                    Util.loadPosterThumbnail(getContext(), castList.get(0).getProfilePath(), mCast1ImageView,ENT_TYPE_PERSON);
                    Util.loadPosterThumbnail(getContext(), castList.get(1).getProfilePath(), mCast2ImageView,ENT_TYPE_PERSON);
                    mCast1TextView.setText(castList.get(0).getName());
                    mCast2TextView.setText(castList.get(1).getName());
                } else if (castList.size() == 3) {
                    Util.loadPosterThumbnail(getContext(), castList.get(0).getProfilePath(), mCast1ImageView,ENT_TYPE_PERSON);
                    mCast1TextView.setText(castList.get(0).getName());
                } else {
                    //Cast size is 0. Don't show cast card.
                    mCastCardView.setVisibility(View.GONE);
                }
            } else {
                //Did not return any cast. Don't show cast card.
                mCastCardView.setVisibility(View.GONE);
            }


            Util.setCrewLinks(getContext(), mMovie.getCredits().getDirectorList(), mDirectorListTextView, getString(R.string.director));
            Util.setCrewLinks(getContext(), mMovie.getCredits().getWriterList(), mWriterListTextView, getString(R.string.writers));

            if (mMovie.getReviews().getReviews().size() > 0) {
                mReviewsTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ReviewsActivity.class);
//                        i.putExtra(EXTRA_MOVIE_ID, mMovie.getId());
                        i.putParcelableArrayListExtra(EXTRA_REVIEW_LIST, (ArrayList) mMovie.getReviews().getReviews());
                        i.putExtra(EXTRA_ENT_TYPE, ENT_TYPE_MOVIE);
                        startActivity(i);
                    }
                });
            } else {
                mReviewsCardView.setVisibility(View.GONE);
            }

             //See if this is a favorite movie and set the state of the star button
            Util.setFavoritesButton(mFavoritesFAB,getActivity(),mMovie.getId());

            // }


            //Pull out the Movie US Certification/Rating
            for (ReleaseDateList list : mMovie.getReleaseDates().getResults()) {

                if (list.getIso31661().equals(MovieDbAPI.CERT_US)) {

                    if(!(list.getReleaseDates().get(0).getCertification().equals("")))
                        mCertificationTextView.setText(list.getReleaseDates().get(0).getCertification());
                    else
                        mCertificationTextView.setText("NR");
                }
            }

        }

    }

    private void shareMovie() {

        Util.shareMedia(getActivity(),ENT_TYPE_MOVIE, mMovie.getId(),getString(R.string.share_movie_subject,mMovie.getTitle()));
    }


}
