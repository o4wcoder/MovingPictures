package com.android.fourthwardcoder.movingpictures.fragments;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.R;
//import com.android.fourthwardcoder.movingpictures.adapters.VideosListAdapter;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.TvShow;
import com.android.fourthwardcoder.movingpictures.models.TvShowOld;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

    public TvDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTvId = getActivity().getIntent().getIntExtra(EXTRA_ID, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_detail, container, false);

        //Get CollapsingToolbarLayout
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        mDetailCardView = (CardView) view.findViewById(R.id.movie_detail_cardview);
        mDetailLayout = (RelativeLayout) view.findViewById(R.id.layout_movie_detail);


        //Set image views
        mBackdropImageView = (ImageView) view.findViewById(R.id.backdropImageView);
        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);

        mFavoritesFAB = (FloatingActionButton) view.findViewById(R.id.favorites_fab);

        mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) view.findViewById(R.id.releaseYearTextView);
        mRuntimeTextView = (TextView) view.findViewById(R.id.runtimeTextView);
       // mCreatedByTextView = (TextView) view.findViewById(R.id.createdByTextView);
        mCastTextView = (TextView) view.findViewById(R.id.castTextView);
        mRatingTextView = (TextView) view.findViewById(R.id.ratingTextView);
        mOverviewTextView = (ExpandableTextView) view.findViewById(R.id.overviewContentExpandableTextView);
        mGenreTextView = (TextView) view.findViewById(R.id.genreTextView);
        mNetworksTextView = (TextView) view.findViewById(R.id.networksTextView);
        mReleaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);

//        mListView = (ListView) view.findViewById(R.id.videosListView);
//        mListView.setScrollContainer(false);
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
////                VideoOld video = mVideoListAdapter.getItem(position);
////
////                Uri youtubeUri = Uri.parse(MovieDbAPI.BASE_YOUTUBE_URL).buildUpon()
////                        .appendPath(MovieDbAPI.PATH_WATCH)
////                        .appendQueryParameter(MovieDbAPI.PARAM_V, video.getKey())
////                        .build();
////
////                Log.e(TAG, "Youtube path: " + youtubeUri.toString());
////
////                Intent i = new Intent(Intent.ACTION_VIEW, youtubeUri);
////
////                startActivity(i);
//            }
//        });

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
        if (tvShow.getStatus().equals(MovieDbAPI.STATUS_ENDED))
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
            getActivity().setTitle(mTvShow.getName() + " " + historyDate);

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

//            Spanned networks = Html.fromHtml("<b>" + getString(R.string.networks) + "</b>" + " " +
//                    mTvShow.getNetworks());
//            mNetworksTextView.setText(networks);

            //mVideoListAdapter = new VideosListAdapter(getActivity(), tvShow.getVideos());
            // mListView.setAdapter(mVideoListAdapter);
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
