package com.android.fourthwardcoder.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.fragments.MovieDetailFragment;
import com.android.fourthwardcoder.popularmovies.helpers.Util;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Movie;

import java.util.Stack;

/**
 * Class MovieDetailActivity
 * Author: Chris Hare
 * Created: 7/26/2015
 * <p/>
 * Activity to show the details of a particular movie
 */
public class MovieDetailActivity extends AppCompatActivity implements Constants {

    final static String TAG = MovieDetailActivity.class.getSimpleName();

    /**********************************************************************/
    /*                         Local Data                                 */
    /**********************************************************************/
    //Stack to hold parent of activity to aid in Up navigation to different parents
    public static Stack<Class<?>> parents = new Stack<Class<?>>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        //Change status bar color
        Util.setStatusBarColor(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Start postpone element transition
        supportPostponeEnterTransition();

        //Add parent that called Movie Activity to stack
        parents.push(getClass());

        if(savedInstanceState == null) {
            Bundle arguments = new Bundle();
            if(getIntent().getExtras().containsKey(EXTRA_MOVIE)) {
                //Get Movie object from Main Activity
                Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
                arguments.putParcelable(EXTRA_MOVIE, movie);
            }
            else {
                //Must have just the id of the movie
                int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID,0);
                arguments.putInt(EXTRA_MOVIE_ID,movieId);
            }
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                //When navigating back, get parent that called this activity.
                //Start parent activity.
                Intent parentActivityIntent = new Intent(this, parents.pop());
                parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(parentActivityIntent);
                //Kill this activity
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
