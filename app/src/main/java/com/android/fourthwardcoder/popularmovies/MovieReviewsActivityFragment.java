package com.android.fourthwardcoder.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieReviewsActivityFragment extends Fragment {

    /***********************************************************************/
    /*                           Constants                                 */
    /***********************************************************************/
     private static final String TAG = MovieReviewsActivityFragment.class.getSimpleName();


    /***********************************************************************/
    /*                          Local Data                                 */
    /***********************************************************************/
    ListView mListView;
    ArrayList<Review> mReviewList;

    public MovieReviewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        mListView = (ListView)view.findViewById(R.id.reviewsListView);

        int movieId = getActivity().getIntent().getIntExtra(MovieDetailActivityFragment.EXTRA_MOVIE_ID,0);

        Log.e(TAG,"got movie id " + movieId);

        if(mListView != null) {

           new FetchReviewsTask().execute(movieId);
        }
        return view;
    }

    private class FetchReviewsTask extends AsyncTask<Integer,Void,ArrayList<Review>> {


        @Override
        protected ArrayList<Review> doInBackground(Integer... params) {

            //Get ID of movie
            int movieId = params[0];

            Uri reviewsUri = Uri.parse(DBUtil.BASE_MOVIE_URL).buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .appendPath(DBUtil.PATH_REVIEWS)
                    .appendQueryParameter(DBUtil.PARM_API_KEY, DBUtil.API_KEY_MOVIE_DB)
                    .build();

            Log.e(TAG,reviewsUri.toString());
            String reviewsJsonStr = DBUtil.queryMovieDatabase(reviewsUri);

            if(reviewsJsonStr == null)
                return null;

            Log.e(TAG, "REview: " + reviewsJsonStr);

            //List of Reviews that get parsed from Movie DB JSON return
            ArrayList<Review> reviewList = null;

            try {
                JSONObject obj = new JSONObject(reviewsJsonStr);
                JSONArray resultsArray = obj.getJSONArray(DBUtil.TAG_RESULTS);

                reviewList = new ArrayList<>(resultsArray.length());

                for(int i = 0; i< resultsArray.length(); i++) {

                    JSONObject result = resultsArray.getJSONObject(i);
                    Review review = new Review();;
                    review.setAuthor(result.getString(DBUtil.TAG_AUTHOR));
                    review.setContent(result.getString(DBUtil.TAG_CONTENT));

                    Log.e(TAG, review.toString());
                    reviewList.add(review);
                }
            } catch (JSONException e) {
                Log.e(TAG,"Caught JSON exception " + e.getMessage());
                e.printStackTrace();
                return null;
            }

            return reviewList;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> movieList) {

            ReviewsListAdapter adapter = new ReviewsListAdapter(getActivity(),movieList);
            mListView.setAdapter(adapter);
        }

    }
}
