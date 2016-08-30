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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.fourthwardmobile.android.movingpictures.R;
//import com.android.fourthwardmobile.movingpictures.adapters.VideosListAdapter;
import com.fourthwardmobile.android.movingpictures.activities.SearchableActivity;
import com.fourthwardmobile.android.movingpictures.activities.TvDetailActivity;
import com.fourthwardmobile.android.movingpictures.adapters.VideoListAdapter;
import com.fourthwardmobile.android.movingpictures.helpers.ImageTransitionListener;
import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.fourthwardmobile.android.movingpictures.helpers.Util;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.Cast;
import com.fourthwardmobile.android.movingpictures.models.Network;
import com.fourthwardmobile.android.movingpictures.models.TvRating;
import com.fourthwardmobile.android.movingpictures.models.TvShow;
import com.fourthwardmobile.android.movingpictures.models.Video;
import com.fourthwardmobile.android.movingpictures.network.NetworkService;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Class TvDetailFragment
 * Author: Chris Hare
 * Created: 9/25/2015
 * <p/>
 * Fragment to show the details of a TV show.
 */
public class TvDetailFragment extends BaseDetailFragment implements Constants, Toolbar.OnMenuItemClickListener {

    /************************************************************/
    /*                      Constants                           */
    /************************************************************/
    private static final String TAG = TvDetailFragment.class.getSimpleName();

    /************************************************************/
    /*                      Local Data                          */
    /************************************************************/
    int mTvId;
    TvShow mTvShow;
    // ListView mListView;
    //VideosListAdapter mVideoListAdapter;

    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mReleaseYearTextView;
    TextView mRuntimeTextView;
    // TextView mCreatedByTextView;
    TextView mCastTextView;
    TextView mRatingTextView;
    TextView mCertificationTextView;
    ExpandableTextView mOverviewTextView;
    TextView mGenreTextView;
    TextView mNetworksTextView;
    TextView mReleaseDateTextView;

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

    boolean mFetchData = false;

    private static final String ARG_ID = "id";
    private static final String ARG_TV_SHOW = "tv_show";

    private NetworkService mNetworkService;
    private Subscription mTvShowSubscription;

    public TvDetailFragment() {
    }

    public static TvDetailFragment newInstance(int id) {

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ID, id);
        TvDetailFragment fragment = new TvDetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get reference to applications network service interface
        mNetworkService = ((MovingPicturesApplication)getActivity().getApplication()).getNetworkService();

        if (savedInstanceState != null) {

            mTvShow = savedInstanceState.getParcelable(ARG_TV_SHOW);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mTvId = bundle.getInt(ARG_ID);
                mFetchData = true;
            }
        }

        //This is a TvShow type
        mEntType = ENT_TYPE_TV;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = null;
        if (getActivity() instanceof TvDetailActivity) {

            //Set Options menu if we are not in two pane mode
            setHasOptionsMenu(true);

            view = inflater.inflate(R.layout.fragment_tv_detail, container, false);
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
            //Set up back UP navigation arrow
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
            ((TvDetailActivity) getActivity()).setSupportActionBar(mToolbar);
        } else {
            view = inflater.inflate(R.layout.fragment_tv_detail_two_pane, container, false);
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
                if (mTvShow != null) {
                    String toastStr = "";
                    if (v.getTag().equals(false)) {
                        Log.e(TAG, "Set to favorite");
                        mFavoritesFAB.setTag(true);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.yellow));
                        toastStr = getString(R.string.added) + " " + mTvShow.getName() + " "
                                + getString(R.string.to_favorites);
                        Util.addToFavoritesDb(getActivity(), mTvShow.getContentValues());

                    } else {
                        Log.e(TAG, "remove from favorite");
                        mFavoritesFAB.setTag(false);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.white));
                        toastStr = getString(R.string.removed) + " " + mTvShow.getName() + " "
                                + getString(R.string.from_favorites);
                        Util.removeFromFavoritesDb(getActivity(), mTvShow.getId());
                    }
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            toastStr, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });


        //Set up Video Layout and RecyclerView for Horizontal Scrolling
        mVideoLayout = (CardView) view.findViewById(R.id.video_list_layout);
        mVideosRecylerView = (RecyclerView) view.findViewById(R.id.video_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mVideosRecylerView.setLayoutManager(layoutManager);
        mVideosRecylerView.setHasFixedSize(true);
        mVideosRecylerView.setNestedScrollingEnabled(false);

        //Get textviews for TvShow Details
        mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) view.findViewById(R.id.releaseYearTextView);
        mRuntimeTextView = (TextView) view.findViewById(R.id.runtimeTextView);
        // mCreatedByTextView = (TextView) view.findViewById(R.id.createdByTextView);
        mCastTextView = (TextView) view.findViewById(R.id.castTextView);
        mRatingTextView = (TextView) view.findViewById(R.id.ratingTextView);
        mCertificationTextView = (TextView) view.findViewById(R.id.certificationTextView);
        mOverviewTextView = (ExpandableTextView) view.findViewById(R.id.detail_overview_exp_text_view);
        mGenreTextView = (TextView) view.findViewById(R.id.genreTextView);
        mNetworksTextView = (TextView) view.findViewById(R.id.networksTextView);
        mReleaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);

        View.OnClickListener castClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.equals(mCast1FrameView)) {
                    Util.startDetailActivity(getActivity(), mTvShow.getCredits().getCast().get(0).getId(), ENT_TYPE_PERSON, mCast1FrameView);
                } else if (v.equals(mCast2FrameView)) {
                    Util.startDetailActivity(getActivity(), mTvShow.getCredits().getCast().get(1).getId(), ENT_TYPE_PERSON, mCast2FrameView);
                } else if (v.equals(mCast3FrameView)) {
                    Util.startDetailActivity(getActivity(), mTvShow.getCredits().getCast().get(2).getId(), ENT_TYPE_PERSON, mCast3FrameView);
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

                Util.showListActivity(getActivity(), mTvShow.getId(), mTvShow.getName(),
                        ENT_TYPE_PERSON, LIST_TYPE_TV_CAST);
            }
        });

        if (mFetchData)
            getTvShow();
        else
            setLayout();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putParcelable(ARG_TV_SHOW, mTvShow);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Log.e(TAG,"onOptionsItemSelected() Fragment");
        switch (item.getItemId()) {
            case R.id.action_share:
                Log.e(TAG,"Menu share click");
                shareTvShow();
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
                shareTvShow();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Clean up Rx Subscription
        if(mTvShowSubscription != null && !mTvShowSubscription.isUnsubscribed()) {
            mTvShowSubscription.unsubscribe();
        }
    }

    /**
     * Set a history string of a tv show from release till end data. Format will
     * be like (2000 - 2009)
     *
     * @param tvShow tv
     * @return
     */
    private static String getDateHistory(TvShow tvShow) {

        String[] yearStart = tvShow.getFirstAirDate().split("-");
        String startYear = yearStart[0];

        String endYear = "?";
        if (tvShow.getLastAirDate() != null) {
            String[] yearEnd = tvShow.getLastAirDate().split("-");
            endYear = yearEnd[0];
        }

        //If TV show has not ended don't add a end date
        if (tvShow.getStatus().equals(MovieDbAPI.STATUS_ENDED) || tvShow.getStatus().equals(MovieDbAPI.STATUS_CANCELED))
            return "(" + startYear + " - " + endYear + ")";
        else
            return "(" + startYear + " - ";
    }

    private void setLayout() {

        if ((getActivity() != null) && (mTvShow != null)) {

            //Set title of MovieOld on Action Bar
            String historyDate = getDateHistory(mTvShow);

            Picasso.with(getActivity()).load(MovieDbAPI.getFullBackdropPath(mTvShow.getBackdropPath())).into(mBackdropImageView, new Callback() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "Loaded backdrop");
                    //Set up color scheme
                    setPaletteColors(mTvShow.getName());
                    //Start Shared Image transition now that we have the backdrop
                    startPostponedEnterTransition(mPosterImageView);

                }

                @Override
                public void onError() {
                    //Just get the default image since there was not backdrop image available
                    Log.e(TAG, "Picasso onError()!!!");
                    Picasso.with(getActivity()).load(R.drawable.movie_backdrop_thumbnail).into(mBackdropImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            //set up color scheme
                            setPaletteColors(mTvShow.getName());
                            //Still want to start shared transition even it the backdrop image was not loaded.
                            startPostponedEnterTransition(mPosterImageView);
                        }

                        @Override
                        public void onError() {
                            //If we failed here, it's bad. Just do the shared transition as to not freeze up the UI
                            startPostponedEnterTransition(mPosterImageView);
                        }
                    });

                }
            });

            Picasso.with(getActivity()).load(MovieDbAPI.getFullPosterPath(mTvShow.getPosterPath())).into(mPosterImageView);

            mTitleTextView.setText(mTvShow.getName());
            mReleaseYearTextView.setText(historyDate);
            mRuntimeTextView.setText(mTvShow.getEpisodeRunTime() + " min");

//            Spanned createdBy = Html.fromHtml("<b>" + getString(R.string.created_by) + "</b>" + " " +
//                    mTvShow.getCreatedByString());
//            mCreatedByTextView.setText(createdBy);

            // Util.setCastLinks(getActivity(), mTvShow, mCastTextView, ENT_TYPE_TV);

            mRatingTextView.setText(String.valueOf(mTvShow.getVoteAverage() + "/10"));

            Spanned synopsis = Html.fromHtml("<b>" + getString(R.string.synopsis) + "</b>" + " " +
                    mTvShow.getOverview());
            mOverviewTextView.setText(synopsis);

            Spanned genre = Html.fromHtml("<b>" + getString(R.string.genre) + "</b>" + " " +
                    mTvShow.getGenreListString());
            mGenreTextView.setText(genre);

            Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
                    Util.reverseDateString(mTvShow.getFirstAirDate()));
            mReleaseDateTextView.setText(releaseDate);

            Spanned networks = Html.fromHtml("<b>" + getString(R.string.networks) + "</b>" + " " +
                    getNetworkList(mTvShow.getNetworks()));
            mNetworksTextView.setText(networks);


                        /*
             * Set up Videos
             */
            if (mTvShow.getVideos().getVideos().size() > 0) {
                mVideoListAdapter = new VideoListAdapter(getActivity(), (ArrayList) mTvShow.getVideos().getVideos(), new VideoListAdapter.VideoListAdapterOnClickHandler() {
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
            ArrayList<Cast> castList = (ArrayList) mTvShow.getCredits().getCast();

            if (castList != null) {
                Log.e(TAG, "setCast List. Cast list not null with size = " + castList.size());
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

            //See if this is a favorite movie and set the state of the star button
            Util.setFavoritesButton(mFavoritesFAB, getActivity(), mTvShow.getId());

            mCertificationTextView.setText("NR");
            if (mTvShow.getContentRatings() != null) {

                for (TvRating tvRating : mTvShow.getContentRatings().getResults()) {

                    if (tvRating.getIso31661().equals(MovieDbAPI.CERT_US)) {

                        if (!(tvRating.equals("")))
                            mCertificationTextView.setText(tvRating.getRating());

                    }
                }
            } else {
                mCertificationTextView.setText("NR");
            }
        }
    }

    private void getTvShow() {

        Observable<TvShow> tvShowObservable = mNetworkService.getMovieApiService().getTvShow(mTvId);

        mTvShowSubscription = tvShowObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TvShow>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        //Want to make sure activity is valid before showing any toasts
                        if(getActivity() != null) {
                            mNetworkService.processNetworkError(getContext(), e);
                        }
                    }

                    @Override
                    public void onNext(TvShow tvShow) {
                        mTvShow = tvShow;
                        setLayout();
                    }
                });
    }

    private String getNetworkList(List<Network> list) {

        String strList = "";

        //Set up display string for networks.
        for (int i = 0; i < list.size(); i++) {
            strList += list.get(i).getName() + ", ";
        }

        if (list.size() > 0)
            strList = strList.substring(0, strList.length() - 2);

        return strList;

    }

    private void shareTvShow() {

        Util.shareMedia(getActivity(),ENT_TYPE_TV, mTvShow.getId(),getString(R.string.share_tv_subject,mTvShow.getName()));
    }


}
