package com.android.fourthwardcoder.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.fragments.MovieDetailActivityFragment;
import com.android.fourthwardcoder.popularmovies.fragments.PopularMoviesMainFragment;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;

/**
 * Class PopularMoviesMainActivity
 * Author: Chris Hare
 * Created: 7/25/2015
 *
 * Main Activity of the PopularMovies App
 */
public class PopularMoviesMainActivity extends AppCompatActivity implements PopularMoviesMainFragment.Callback,
        Constants {

    /*************************************************************/
    /*                       Constants                           */
    /*************************************************************/
    private static final String TAG = PopularMoviesMainActivity.class.getSimpleName();

    private static final String DETAILFRAGMENT_TAG = "DFTAG";


    /*************************************************************/
    /*                       Local Data                          */
    /*************************************************************/
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar();

        if(findViewById(R.id.movie_detail_container) != null) {

            //The detail container view will be present only in the large-screen layouts
            //(res/layout-sw600dp). If this view is present, then the activity should
            //be two-pane mode.
            mTwoPane = true;

            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment transaction.
            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container,new MovieDetailActivityFragment(),
                                DETAILFRAGMENT_TAG).commit();
            }
        }
        else {
            mTwoPane = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int movieId) {

        Log.e(TAG, "Inside onItemSelected Callback");
        if (mTwoPane) {
            Log.e(TAG,"Got two pane");
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment
            //transaction
            Bundle args = new Bundle();
            args.putInt(EXTRA_MOVIE_ID, movieId);

            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Log.e(TAG,"Have single pane with content uri "+ movieId);

            Intent i = new Intent(this,MovieDetailActivity.class);
            i.putExtra(EXTRA_MOVIE_ID, movieId);
            startActivity(i);
        }
    }
}
