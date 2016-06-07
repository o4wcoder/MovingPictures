package com.android.fourthwardcoder.movingpictures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.fragments.MainFragment;
import com.android.fourthwardcoder.movingpictures.fragments.MovieDetailFragment;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Movie;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Class MainActivity
 * Author: Chris Hare
 * Created: 7/25/2015
 * <p/>
 * Main Activity of the PopularMovies App
 */
public class MainActivity extends AppCompatActivity implements MainFragment.Callback,
        Constants {

    /*************************************************************/
    /*                       Constants                           */
    /*************************************************************/
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    /*************************************************************/
    /*                       Local Data                          */
    /*************************************************************/
    boolean mTwoPane;

    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        Util.setStatusBarColor(this);

        //Set toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if ((findViewById(R.id.movie_detail_container) != null)) {
            //The detail container view will be present only in the large-screen layouts
            //(res/layout-sw600dp). If this view is present, then the activity should
            //be two-pane mode.
            mTwoPane = true;
            Log.e(TAG,"onCreate: mTwoPane = true");
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(),
                                DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            Log.e(TAG,"onCreate: onTwoPane = false");
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
    public void onItemSelected(Movie movie, ImageView imageView) {

        if (mTwoPane) {
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment
            //transaction
            Log.e(TAG,"onItemSelected(): with twoPane");
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_MOVIE, movie);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent i = new Intent(this, MovieDetailActivity.class);
            i.putExtra(EXTRA_MOVIE, movie);


            //Start shared element transition for the movie poster
            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            new Pair<View, String>(imageView, getString(R.string.trans_movie_poster)));

            startActivity(i,activityOptions.toBundle());

        }
    }

    @Override
    public void onLoadFinished(Movie movie) {

        Log.e(TAG,"In onLoadFinsished()");
        if (mTwoPane) {
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment
            //transaction
            Log.e(TAG,"In two pane, setting details fragment");
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_MOVIE, movie);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            //Set first movie in detail pane. Needed to use "commitAllowingStateLoss"
            //instead of just "commit" because calling this directly when the loader
            //was done causes an "illegal state exception"
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commitAllowingStateLoss();
        }
        else {
            Log.e(TAG,"onLoadFinished(): NOt in two pane!!!!!!!!");
        }
    }
}
