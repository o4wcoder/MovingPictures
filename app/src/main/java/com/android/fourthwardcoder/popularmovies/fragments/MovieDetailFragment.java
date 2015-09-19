package com.android.fourthwardcoder.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.popularmovies.activities.MovieCastActivity;
import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Movie;
import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.models.Video;
import com.android.fourthwardcoder.popularmovies.adapters.VideosListAdapter;
import com.android.fourthwardcoder.popularmovies.activities.MovieReviewsActivity;
import com.android.fourthwardcoder.popularmovies.activities.PersonDetailActivity;
import com.squareup.picasso.Picasso;


/**
 * Class MovieDetailFragment
 * Author: Chris Hare
 * Created: 7/26/2015
 *
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
    TextView mOverviewTextView;
    TextView mGenreTextView;
    TextView mRevenueTextView;
    TextView mReviewsTextView;

    CheckBox mFavoritesToggleButton;

    /*****************************************************************/
    /*                       Constructor                             */
    /*****************************************************************/
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        //retain the instance on rotation
        setRetainInstance(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //Get Movie passed from Main Activity
        Bundle arguments = getArguments();
        if(arguments != null) {
            mMovieId = arguments.getInt(EXTRA_MOVIE_ID);
        }
        //mMovieId = getActivity().getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);

        //Set image views
        mBackdropImageView = (ImageView)view.findViewById(R.id.backdropImageView);
        mPosterImageView = (ImageView)view.findViewById(R.id.posterImageView);

        mFavoritesToggleButton = (CheckBox)view.findViewById(R.id.favoritesToggleButton);
        mFavoritesToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.e(TAG,"Favorites changed to " + isChecked);
                if(mMovie != null) {
                    String toastStr = "";
                    if(isChecked) {

                        toastStr = getString(R.string.added) + " " + mMovie.getTitle() + " "
                                + getString(R.string.to_favorites);

                    }
                    else {
                        toastStr = getString(R.string.removed) + " " + mMovie.getTitle() + " "
                                + getString(R.string.from_favorites);
                    }
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            toastStr, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //Set textviews with Movie details
        mTitleTextView = (TextView)view.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView)view.findViewById(R.id.releaseYearTextView);
        mRuntimeTextView = (TextView)view.findViewById(R.id.runtimeTextView);
        mRatingTextView = (TextView)view.findViewById(R.id.ratingTextView);
        mDirectorTextView = (TextView)view.findViewById(R.id.directorTextView);
        mCastTextView = (TextView)view.findViewById(R.id.castTextView);
        mReleaseDateTextView = (TextView)view.findViewById(R.id.releaseDateTextView);
        mOverviewTextView = (TextView)view.findViewById(R.id.overviewTextView);
        mGenreTextView = (TextView)view.findViewById(R.id.genreTextView);
        mRevenueTextView = (TextView)view.findViewById(R.id.revenueTextView);
        mReviewsTextView = (TextView)view.findViewById(R.id.reviewsTextView);

        mReviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MovieReviewsActivity.class);
                i.putExtra(EXTRA_MOVIE_ID,mMovie.getId());
                startActivity(i);
            }
        });

        mListView = (ListView)view.findViewById(R.id.videosListView);
        mListView.setScrollContainer(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Video video = (Video)mVideoListAdapter.getItem(position);

                Uri youtubeUri = Uri.parse(MovieDbAPI.BASE_YOUTUBE_URL).buildUpon()
                        .appendPath(MovieDbAPI.PATH_WATCH)
                        .appendQueryParameter(MovieDbAPI.PARAM_V,video.getKey())
                        .build();

                Log.e(TAG,"Youtube path: " + youtubeUri.toString());

                Intent i = new Intent(Intent.ACTION_VIEW,youtubeUri);

                startActivity(i);
            }
        });

        if(mListView != null) {
            new FetchMovieTask().execute(mMovieId);
        }

        return view;
    }
    public void getListViewSize(ListView myListView) {
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

    private void setCastLinks(TextView textView) {

        Spanned cast = Html.fromHtml("<b>" + getString(R.string.cast) + "</b>" + " " +
                mMovie.getActorsString() + ", " + getString(R.string.more));

        SpannableString castSS = new SpannableString(cast);

        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Log.e(TAG,"Got actor 1 " + mMovie.getActorNames().get(0));
                Log.e(TAG,"With ID " + mMovie.getActorIds().get(0));
                startActorDetailActivity(mMovie.getActorIds().get(0));
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Log.e(TAG,"Got actor 2 " + mMovie.getActorNames().get(1));
                Log.e(TAG,"With ID " + mMovie.getActorIds().get(1));
                startActorDetailActivity(mMovie.getActorIds().get(1));
            }
        };

        ClickableSpan span3 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Log.e(TAG,"Got actor 3 " + mMovie.getActorNames().get(2));
                Log.e(TAG,"With ID " + mMovie.getActorIds().get(2));
                startActorDetailActivity(mMovie.getActorIds().get(2));
            }
        };

        //More test to the full list of cast members
        ClickableSpan span4 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent i = new Intent(getActivity(), MovieCastActivity.class);
                i.putExtra(EXTRA_MOVIE_ID,mMovieId);
                startActivity(i);
            }
        };

        if(mMovie.getActorNames() == null)
            Log.e(TAG,"Person list null!!!");

        if(mMovie.getActorIds() == null)
            Log.e(TAG,"Person id list null!!!");

        int span1Start = 6;
        int span1End = span1Start + mMovie.getActorNames().get(0).length();
        int span2Start = span1End + 2;
        int span2End = span2Start + mMovie.getActorNames().get(1).length();
        int span3Start = span2End + 2;
        int span3End = span3Start + mMovie.getActorNames().get(2).length();
        int span4Start = span3End +2;
        int span4End = span4Start + getString(R.string.more).length();
        Log.e(TAG,"Total String length " + castSS.length());
        Log.e(TAG,"End 3 span " + span3End);
        castSS.setSpan(span1, span1Start, span1End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        castSS.setSpan(span2, span2Start,span2End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        castSS.setSpan(span3, span3Start,span3End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        castSS.setSpan(span4, span4Start,span4End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(castSS);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void startActorDetailActivity(int id) {

        Intent i = new Intent(getActivity(),PersonDetailActivity.class);
        i.putExtra(EXTRA_PERSON_ID,id);
        startActivity(i);
    }
    private class FetchMovieTask extends AsyncTask<Integer,Void,Movie> {

        //ProgressDialog to be displayed while the data is being fetched and parsed
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            //Start ProgressDialog on Main Thread UI before precessing begins
            progressDialog = ProgressDialog.show(getActivity(),"",getString(R.string.progress_downloading_movies),true);
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

            if(getActivity() != null && mListView != null) {
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

                    setCastLinks(mCastTextView);

                    Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
                            mMovie.getReleaseDate());
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

                    mVideoListAdapter = new VideosListAdapter(getActivity(), movie.getVideoList());
                    mListView.setAdapter(mVideoListAdapter);
                    getListViewSize(mListView);
                }
            }
        }

    }
}
