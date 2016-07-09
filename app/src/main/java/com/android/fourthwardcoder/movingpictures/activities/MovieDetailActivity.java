package com.android.fourthwardcoder.movingpictures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.fragments.MovieDetailFragment;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;

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
        setContentView(R.layout.activity_single_fragment);
        //Change status bar color
       // Util.setStatusBarColor(this);
      //  final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Start postpone element transition
        supportPostponeEnterTransition();

        //Add parent that called MovieOld Activity to stack
        parents.push(getClass());

        if (savedInstanceState == null) {

            //Must have just the id of the movie
            int movieId = getIntent().getIntExtra(EXTRA_ID, 0);
            Log.e(TAG,"onCreate with extra id = " + movieId);
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movieId);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
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
