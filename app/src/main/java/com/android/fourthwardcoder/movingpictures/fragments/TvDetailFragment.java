package com.android.fourthwardcoder.movingpictures.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.R;
//import com.android.fourthwardcoder.movingpictures.adapters.VideosListAdapter;
import com.android.fourthwardcoder.movingpictures.activities.MovieDetailActivity;
import com.android.fourthwardcoder.movingpictures.activities.TvDetailActivity;
import com.android.fourthwardcoder.movingpictures.adapters.VideoListAdapter;
import com.android.fourthwardcoder.movingpictures.helpers.ImageTransitionListener;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Cast;
import com.android.fourthwardcoder.movingpictures.models.Network;
import com.android.fourthwardcoder.movingpictures.models.TvShow;
import com.android.fourthwardcoder.movingpictures.models.Video;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Class TvDetailFragment
 * Author: Chris Hare
 * Created: 9/25/2015
 * <p/>
 * Fragment to show the details of a TV show.
 */
public class TvDetailFragment extends Fragment implements Constants {

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

    ImageView mBackdropImageView;
    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mReleaseYearTextView;
    TextView mRuntimeTextView;
   // TextView mCreatedByTextView;
    TextView mCastTextView;
    TextView mRatingTextView;
    ExpandableTextView mOverviewTextView;
    TextView mGenreTextView;
    TextView mNetworksTextView;
    TextView mReleaseDateTextView;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    CardView mDetailCardView;
    RelativeLayout mDetailLayout;
    FloatingActionButton mFavoritesFAB;

    //Videos
    CardView mVideoLayout;
    RecyclerView mVideosRecylerView;
    VideoListAdapter mVideoListAdapter;

    //Cast
    CardView mCastCardView;
    ImageView mCast1ImageView;
    ImageView mCast2ImageView;
    ImageView mCast3ImageView;
    TextView mCast1TextView;
    TextView mCast2TextView;
    TextView mCast3TextView;
    TextView mCastShowAllTextView;

    private static final String ARG_ID = "id";

    public TvDetailFragment() {
    }

    public static TvDetailFragment newInstance(int id) {

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ID,id);
        TvDetailFragment fragment = new TvDetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {

        } else {
         Bundle bundle = getArguments();
            mTvId = bundle.getInt(ARG_ID);
            Log.e(TAG,"onCreate() with id = " + mTvId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = null;
        if(getActivity() instanceof TvDetailActivity) {
             view = inflater.inflate(R.layout.fragment_tv_detail, container, false);
            //Set up back UP navigation arrow
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
        } else {
            view = inflater.inflate(R.layout.fragment_tv_detail_two_pane, container, false);
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
                        Util.addToFavoritesDb(getActivity(),mTvShow.getContentValues());

                    } else {
                        Log.e(TAG, "remove from favorite");
                        mFavoritesFAB.setTag(false);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.white));
                        toastStr = getString(R.string.removed) + " " + mTvShow.getName() + " "
                                + getString(R.string.from_favorites);
                        Util.removeFromFavoritesDb(getActivity(),mTvShow.getId());
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
        mOverviewTextView = (ExpandableTextView) view.findViewById(R.id.detail_overview_exp_text_view);
        mGenreTextView = (TextView) view.findViewById(R.id.genreTextView);
        mNetworksTextView = (TextView) view.findViewById(R.id.networksTextView);
        mReleaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);

        View.OnClickListener castClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.equals(mCast1ImageView)) {
                    Util.startDetailActivity(getActivity(),mTvShow.getCredits().getCast().get(0).getId(),ENT_TYPE_PERSON,mCast1ImageView);
                } else if (v.equals(mCast2ImageView)) {
                    Util.startDetailActivity(getActivity(),mTvShow.getCredits().getCast().get(1).getId(),ENT_TYPE_PERSON,mCast2ImageView);
                } else if (v.equals(mCast3ImageView)) {
                    Util.startDetailActivity(getActivity(),mTvShow.getCredits().getCast().get(2).getId(),ENT_TYPE_PERSON,mCast3ImageView);
                }
            }
        };

        //Get the 3 Top Billed Cast layouts and child views
        mCastCardView = (CardView) view.findViewById(R.id.cast_list_layout);
        View cast1View = view.findViewById(R.id.detail_cast_layout1);
        mCast1ImageView = (ImageView) cast1View.findViewById(R.id.thumb_image_view);
        mCast1ImageView.setOnClickListener(castClickListener);
        mCast1TextView = (TextView) cast1View.findViewById(R.id.thumb_text_view);

        View cast2View = view.findViewById(R.id.detail_cast_layout2);
        mCast2ImageView = (ImageView) cast2View.findViewById(R.id.thumb_image_view);
        mCast2ImageView.setOnClickListener(castClickListener);
        mCast2TextView = (TextView) cast2View.findViewById(R.id.thumb_text_view);

        View cast3View = view.findViewById(R.id.detail_cast_layout3);
        mCast3ImageView = (ImageView) cast3View.findViewById(R.id.thumb_image_view);
        mCast3ImageView.setOnClickListener(castClickListener);
        mCast3TextView = (TextView) cast3View.findViewById(R.id.thumb_text_view);
        mCastShowAllTextView = (TextView) view.findViewById(R.id.detail_cast_show_all_textview);
        mCastShowAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Util.showListActivity(getActivity(), mTvShow.getId(),mTvShow.getName(),
                       ENT_TYPE_PERSON,LIST_TYPE_TV_CAST);
            }
        });
      //  if (mListView != null)
            getTvShow();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        //Dont' display the share menu option if there are no videos to share
//        if(mTvShow.getVideos() != null) {
//            if (mTvShow.getVideos().size() > 0) {
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
//                  //  shareActionProvider.setShareIntent(Util.createShareVideoIntent(getActivity(),mTvShow));
//                } else {
//                    Log.e(TAG, "Share Action Provider is null!");
//                }
//            }
//        }
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

        String[] yearEnd = tvShow.getLastAirDate().split("-");
        String endYear = yearEnd[0];

        //If TV show has not ended don't add a end date
        if (tvShow.getStatus().equals(MovieDbAPI.STATUS_ENDED) || tvShow.getStatus().equals(MovieDbAPI.STATUS_CANCELED) )
            return "(" + startYear + " - " + endYear + ")";
        else
            return "(" + startYear + " - ";
    }

    private void setLayout() {

        if ((getActivity() != null) && (mTvShow != null)) {



            //Set share menu if there are videos
            setHasOptionsMenu(true);
            //Set title of MovieOld on Action Bar
            String historyDate = getDateHistory(mTvShow);

            Picasso.with(getActivity()).load(MovieDbAPI.getFullBackdropPath(mTvShow.getBackdropPath())).into(mBackdropImageView, new Callback() {
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
                Log.e(TAG,"setCast List. Cast list not null with size = " + castList.size());
                if (castList.size() >= 3) {
                    Util.loadPosterThumbnail(getContext(),castList.get(0).getProfilePath(), mCast1ImageView);
                    Util.loadPosterThumbnail(getContext(),castList.get(1).getProfilePath(), mCast2ImageView);
                    Util.loadPosterThumbnail(getContext(),castList.get(2).getProfilePath(), mCast3ImageView);
                    mCast1TextView.setText(castList.get(0).getName());
                    mCast2TextView.setText(castList.get(1).getName());
                    mCast3TextView.setText(castList.get(2).getName());
                } else if (castList.size() == 2) {
                    Util.loadPosterThumbnail(getContext(),castList.get(0).getProfilePath(), mCast1ImageView);
                    Util.loadPosterThumbnail(getContext(),castList.get(1).getProfilePath(), mCast2ImageView);
                    mCast1TextView.setText(castList.get(0).getName());
                    mCast2TextView.setText(castList.get(1).getName());
                } else if (castList.size() == 3) {
                    Util.loadPosterThumbnail(getContext(),castList.get(0).getProfilePath(), mCast1ImageView);
                    mCast1TextView.setText(castList.get(0).getName());
                }
                else {
                    //Cast size is 0. Don't show cast card.
                    mCastCardView.setVisibility(View.GONE);
                }
            } else {
                //Did not return any cast. Don't show cast card.
                mCastCardView.setVisibility(View.GONE);
            }

            //See if this is a favorite movie and set the state of the star button
            Util.setFavoritesButton(mFavoritesFAB,getActivity(),mTvShow.getId());
        }
    }
    private void getTvShow() {

        Call<TvShow> call = MovieDbAPI.getMovieApiService().getTvShow(mTvId);

        call.enqueue(new retrofit2.Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {

                if(response.isSuccessful()) {
                    mTvShow = response.body();
                    setLayout();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private String getNetworkList(List<Network> list) {

        String strList = "";

        //Set up display string for networks.
        for(int i = 0; i < list.size(); i++) {
            strList += list.get(i).getName() + ", ";
        }

        if(list.size() > 0)
            strList = strList.substring(0,strList.length() - 2);

        return strList;

    }
    private void setPaletteColors() {

        Bitmap bitmap = ((BitmapDrawable)mBackdropImageView.getDrawable()).getBitmap();

        if(bitmap != null) {
            Palette p = Palette.generate(bitmap, 12);
            //   mMutedColor = p.getDarkMutedColor(0xFF333333);

            //Set title and colors for collapsing toolbar
            mCollapsingToolbarLayout.setTitle(mTvShow.getName());
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            //Set content descriptioni for toolbar/title
            mCollapsingToolbarLayout.setContentDescription(mTvShow.getName());

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
    /*********************************************************************/
    /*                         Inner Classes                             */
    /*********************************************************************/
//    private class FetchTvTask extends AsyncTask<Integer, Void, TvShowOld> {
//
//        @Override
//        protected TvShowOld doInBackground(Integer... params) {
//
//            int tvId = params[0];
//            return MovieDbAPI.getTvShow(tvId);
//        }
//
//        @Override
//        protected void onPostExecute(TvShowOld tvShow) {
//
//            if ((getActivity() != null) && (tvShow != null)) {
//
//
//                mTvShow = tvShow;
//                //Set share menu if there are videos
//                setHasOptionsMenu(true);
//                //Set title of MovieOld on Action Bar
//                String historyDate = getDateHistory(tvShow);
//                getActivity().setTitle(tvShow.getTitle() + " " + historyDate);
//
//                Picasso.with(getActivity()).load(tvShow.getBackdropPath()).into(mBackdropImageView);
//                Picasso.with(getActivity()).load(tvShow.getPosterPath()).into(mPosterImageView);
//
//                mTitleTextView.setText(mTvShow.getTitle());
//                mReleaseYearTextView.setText(historyDate);
//                mRuntimeTextView.setText(mTvShow.getRuntime() + " min");
//
//                Spanned createdBy = Html.fromHtml("<b>" + getString(R.string.created_by) + "</b>" + " " +
//                        tvShow.getCreatedByString());
//                mCreatedByTextView.setText(createdBy);
//
//               // Util.setCastLinks(getActivity(), mTvShow, mCastTextView, ENT_TYPE_TV);
//
//                mRatingTextView.setText(String.valueOf(mTvShow.getRating()) + "/10");
//
//                Spanned synopsis = Html.fromHtml("<b>" + getString(R.string.synopsis) + "</b>" + " " +
//                        mTvShow.getOverview());
//                mOverviewTextView.setText(synopsis);
//
//                Spanned genre = Html.fromHtml("<b>" + getString(R.string.genre) + "</b>" + " " +
//                        mTvShow.getGenreString());
//                mGenreTextView.setText(genre);
//
//                Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
//                        Util.reverseDateString(mTvShow.getReleaseDate()));
//                mReleaseDateTextView.setText(releaseDate);
//
//                Spanned networks = Html.fromHtml("<b>" + getString(R.string.networks) + "</b>" + " " +
//                        mTvShow.getNetworksString());
//                mNetworksTextView.setText(networks);
//
//                //mVideoListAdapter = new VideosListAdapter(getActivity(), tvShow.getVideos());
//               // mListView.setAdapter(mVideoListAdapter);
//            }
//        }
//    }
}
