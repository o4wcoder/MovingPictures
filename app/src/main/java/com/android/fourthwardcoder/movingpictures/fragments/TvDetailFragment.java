package com.android.fourthwardcoder.movingpictures.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.adapters.VideosListAdapter;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.TvShow;
import com.android.fourthwardcoder.movingpictures.models.Video;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

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
    ListView mListView;
    VideosListAdapter mVideoListAdapter;

    ImageView mBackdropImageView;
    ImageView mPosterImageView;
    TextView mTitleTextView;
    TextView mReleaseYearTextView;
    TextView mRuntimeTextView;
    TextView mCreatedByTextView;
    TextView mCastTextView;
    TextView mRatingTextView;
    ExpandableTextView mOverviewTextView;
    TextView mGenreTextView;
    TextView mNetworksTextView;
    TextView mReleaseDateTextView;

    public TvDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTvId = getActivity().getIntent().getIntExtra(EXTRA_TV_ID, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_detail, container, false);

        //Set image views
        mBackdropImageView = (ImageView) view.findViewById(R.id.backdropImageView);
        mPosterImageView = (ImageView) view.findViewById(R.id.posterImageView);

        mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) view.findViewById(R.id.releaseYearTextView);
        mRuntimeTextView = (TextView) view.findViewById(R.id.runtimeTextView);
        mCreatedByTextView = (TextView) view.findViewById(R.id.createdByTextView);
        mCastTextView = (TextView) view.findViewById(R.id.castTextView);
        mRatingTextView = (TextView) view.findViewById(R.id.ratingTextView);
        mOverviewTextView = (ExpandableTextView) view.findViewById(R.id.overviewContentExpandableTextView);
        mGenreTextView = (TextView) view.findViewById(R.id.genreTextView);
        mNetworksTextView = (TextView) view.findViewById(R.id.networksTextView);
        mReleaseDateTextView = (TextView) view.findViewById(R.id.releaseDateTextView);

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

        if (mListView != null)
            if(Util.isNetworkAvailable(getActivity())) {
                new FetchTvTask().execute(mTvId);
            }
            else {
                Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                connectToast.show();
            }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        //Dont' display the share menu option if there are no videos to share
        if(mTvShow.getVideos() != null) {
            if (mTvShow.getVideos().size() > 0) {
                inflater.inflate(R.menu.menu_share, menu);

                //Retrieve teh share menu item
                MenuItem menuItem = menu.findItem(R.id.action_share);

                //Get the provider and hold onto it to set/change the share intent.
                ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat
                        .getActionProvider(menuItem);

                //Attach and intent to this ShareActionProvider
                if (shareActionProvider != null) {
                    shareActionProvider.setShareIntent(Util.createShareVideoIntent(getActivity(),mTvShow));
                } else {
                    Log.e(TAG, "Share Action Provider is null!");
                }
            }
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

        String[] yearStart = tvShow.getReleaseDate().split("-");
        String startYear = yearStart[0];

        String[] yearEnd = tvShow.getLastAirDate().split("-");
        String endYear = yearEnd[0];

        //If TV show has not ended don't add a end date
        if (tvShow.getStatus().equals(MovieDbAPI.STATUS_ENDED))
            return "(" + startYear + " - " + endYear + ")";
        else
            return "(" + startYear + " - ";
    }

    /*********************************************************************/
    /*                         Inner Classes                             */
    /*********************************************************************/
    private class FetchTvTask extends AsyncTask<Integer, Void, TvShow> {

        @Override
        protected TvShow doInBackground(Integer... params) {

            int tvId = params[0];
            return MovieDbAPI.getTvShow(tvId);
        }

        @Override
        protected void onPostExecute(TvShow tvShow) {

            if ((getActivity() != null) && (tvShow != null)) {


                mTvShow = tvShow;
                //Set share menu if there are videos
                setHasOptionsMenu(true);
                //Set title of Movie on Action Bar
                String historyDate = getDateHistory(tvShow);
                getActivity().setTitle(tvShow.getTitle() + " " + historyDate);

                Picasso.with(getActivity()).load(tvShow.getBackdropPath()).into(mBackdropImageView);
                Picasso.with(getActivity()).load(tvShow.getPosterPath()).into(mPosterImageView);

                mTitleTextView.setText(mTvShow.getTitle());
                mReleaseYearTextView.setText(historyDate);
                mRuntimeTextView.setText(mTvShow.getRuntime() + " min");

                Spanned createdBy = Html.fromHtml("<b>" + getString(R.string.created_by) + "</b>" + " " +
                        tvShow.getCreatedByString());
                mCreatedByTextView.setText(createdBy);

                Util.setCastLinks(getActivity(), mTvShow, mCastTextView, ENT_TYPE_TV);

                mRatingTextView.setText(String.valueOf(mTvShow.getRating()) + "/10");

                Spanned synopsis = Html.fromHtml("<b>" + getString(R.string.synopsis) + "</b>" + " " +
                        mTvShow.getOverview());
                mOverviewTextView.setText(synopsis);

                Spanned genre = Html.fromHtml("<b>" + getString(R.string.genre) + "</b>" + " " +
                        mTvShow.getGenreString());
                mGenreTextView.setText(genre);

                Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
                        Util.reverseDateString(mTvShow.getReleaseDate()));
                mReleaseDateTextView.setText(releaseDate);

                Spanned networks = Html.fromHtml("<b>" + getString(R.string.networks) + "</b>" + " " +
                        mTvShow.getNetworksString());
                mNetworksTextView.setText(networks);

                mVideoListAdapter = new VideosListAdapter(getActivity(), tvShow.getVideos());
                mListView.setAdapter(mVideoListAdapter);
            }
        }
    }
}
