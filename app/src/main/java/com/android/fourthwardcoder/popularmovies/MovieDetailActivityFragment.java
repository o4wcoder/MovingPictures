package com.android.fourthwardcoder.popularmovies;

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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Class MovieDetailActivityFragment
 * Author: Chris Hare
 * Created: 7/26/2015
 *
 * Fragment to show the details of a particular movie
 */
public class MovieDetailActivityFragment extends Fragment {

    /*****************************************************************/
    /*                        Constants                              */
    /*****************************************************************/
    private static final String TAG = MovieDetailActivityFragment.class.getSimpleName();
    public static final String EXTRA_MOVIE_ID = "com.android.fourthwardcoder.popularmovies.extra_movie_id";
    public static final String EXTRA_ACTOR_ID = "com.android.fourthwardcoder.popularmovies.extra_actor_id";

    /*****************************************************************/
    /*                        Local Data                             */
    /*****************************************************************/
    Movie mMovie;
    ListView mListView;
    VideosListAdapter mVideoListAdapter;

    /*****************************************************************/
    /*                       Constructor                             */
    /*****************************************************************/
    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //Get Movie passed from Main Activity
        mMovie = getActivity().getIntent().getParcelableExtra(PopularMoviesMainFragment.EXTRA_MOVIE);

        //Set title of Movie on Action Bar
        getActivity().setTitle(mMovie.getTitle());

        //Set image views
        ImageView backdropImageView = (ImageView)view.findViewById(R.id.backdropImageView);
        Picasso.with(getActivity()).load(mMovie.getBackdropPath()).into(backdropImageView);

        ImageView posterImageView = (ImageView)view.findViewById(R.id.posterImageView);
        Picasso.with(getActivity()).load(mMovie.getPosterPath()).into(posterImageView);

        //Set textviews with Movie details
        TextView titleTextView = (TextView)view.findViewById(R.id.titleTextView);
        titleTextView.setText(mMovie.getTitle());

        TextView releaseYearTextView = (TextView)view.findViewById(R.id.releaseYearTextView);
        releaseYearTextView.setText(mMovie.getReleaseYear());

        TextView runtimeTextView = (TextView)view.findViewById(R.id.runtimeTextView);
        runtimeTextView.setText(mMovie.getRuntime() + " min");

        TextView ratingTextView = (TextView)view.findViewById(R.id.ratingTextView);
        ratingTextView.setText(String.valueOf(mMovie.getRating()) + "/10");

        TextView directorTextView = (TextView)view.findViewById(R.id.directorTextView);
        Spanned director = Html.fromHtml("<b>" + getString(R.string.director) + "</b>" + " " +
                mMovie.getDirectorString());
        directorTextView.setText(director);

        TextView castTextView = (TextView)view.findViewById(R.id.castTextView);
        setCastLinks(castTextView);

        TextView releaseDateTextView = (TextView)view.findViewById(R.id.releaseDateTextView);
        Spanned releaseDate = Html.fromHtml("<b>" + getString(R.string.release_date) + "</b>" + " " +
                mMovie.getReleaseDate());
        releaseDateTextView.setText(releaseDate);

        TextView overviewTextView = (TextView)view.findViewById(R.id.overviewTextView);
        Spanned synopsis = Html.fromHtml("<b>" + getString(R.string.synopsis) + "</b>" + " " +
                mMovie.getOverview());
        overviewTextView.setText(synopsis);

        TextView genreTextView = (TextView)view.findViewById(R.id.genreTextView);
        Spanned genre = Html.fromHtml("<b>" + getString(R.string.genre) + "</b>" + " " +
                mMovie.getGenreString());

        genreTextView.setText(genre);

        TextView revenueTextView = (TextView)view.findViewById(R.id.revenueTextView);
        Spanned revenue = Html.fromHtml("<b>" + getString(R.string.revenue) + "</b>" + " " +
                mMovie.getRevenue());
        revenueTextView.setText(revenue);

        TextView reviewsTextView = (TextView)view.findViewById(R.id.reviewsTextView);

        reviewsTextView.setOnClickListener(new View.OnClickListener() {
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

                Uri youtubeUri = Uri.parse(DBUtil.BASE_YOUTUBE_URL).buildUpon()
                        .appendPath(DBUtil.PATH_WATCH)
                        .appendQueryParameter(DBUtil.PARAM_V,video.getKey())
                        .build();

                Log.e(TAG,"Youtube path: " + youtubeUri.toString());

                Intent i = new Intent(Intent.ACTION_VIEW,youtubeUri);

                startActivity(i);
            }
        });

        if(mListView != null) {
            new FetchVideosTask().execute(mMovie.getId());
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
                mMovie.getActorsString());

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
                startActorDetailActivity(mMovie.getActorIds().get(3));
            }
        };

        if(mMovie.getActorNames() == null)
            Log.e(TAG,"Actor list null!!!");

        if(mMovie.getActorIds() == null)
            Log.e(TAG,"Actor id list null!!!");

        int span1Start = 6;
        int span1End = span1Start + mMovie.getActorNames().get(0).length();
        int span2Start = span1End + 2;
        int span2End = span2Start + mMovie.getActorNames().get(1).length();
        int span3Start = span2End + 2;
        int span3End = span3Start + mMovie.getActorNames().get(2).length();
        Log.e(TAG,"Total String length " + castSS.length());
        Log.e(TAG,"End 3 span " + span3End);
        castSS.setSpan(span1, span1Start,span1End , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        castSS.setSpan(span2, span2Start,span2End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        castSS.setSpan(span3, span3Start,span3End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(castSS);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void startActorDetailActivity(int id) {

        Intent i = new Intent(getActivity(),ActorDetailActivity.class);
        i.putExtra(EXTRA_ACTOR_ID,id);
        startActivity(i);
    }
    private class FetchVideosTask extends AsyncTask<Integer,Void,ArrayList<Video>> {


        @Override
        protected ArrayList<Video> doInBackground(Integer... params) {

            //Get ID of movie
            int movieId = params[0];

            Uri videosUri = Uri.parse(DBUtil.BASE_MOVIE_URL).buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .appendPath(DBUtil.PATH_VIDEOS)
                    .appendQueryParameter(DBUtil.PARM_API_KEY, DBUtil.API_KEY_MOVIE_DB)
                    .build();

            Log.e(TAG, videosUri.toString());
            String videosJsonStr = DBUtil.queryMovieDatabase(videosUri);

            if(videosJsonStr == null)
                return null;

            Log.e(TAG, "Videos: " + videosJsonStr);

            //List of Reviews that get parsed from Movie DB JSON return
            ArrayList<Video> videoList = null;


            try {
                JSONObject obj = new JSONObject(videosJsonStr);
                JSONArray resultsArray = obj.getJSONArray(DBUtil.TAG_RESULTS);

                videoList = new ArrayList<>(resultsArray.length());

                for(int i = 0; i< resultsArray.length(); i++) {

                    JSONObject result = resultsArray.getJSONObject(i);
                    Video video = new Video();;
                    video.setName(result.getString(DBUtil.TAG_NAME));
                    video.setKey(result.getString(DBUtil.TAG_KEY));
                    video.setType(result.getString(DBUtil.TAG_TYPE));
                    video.setSize(result.getInt(DBUtil.TAG_SIZE));

                    //Log.e(TAG, review.toString());
                    videoList.add(video);
                }
            } catch (JSONException e) {
                Log.e(TAG,"Caught JSON exception " + e.getMessage());
                e.printStackTrace();
                return null;
            }

            return videoList;
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videoList) {

            if(videoList != null) {
                mVideoListAdapter = new VideosListAdapter(getActivity(), videoList);
                mListView.setAdapter(mVideoListAdapter);
                getListViewSize(mListView);
            }
        }

    }
}
