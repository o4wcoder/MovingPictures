package com.android.fourthwardcoder.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.fragments.MovieDetailFragment;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;

import java.util.Stack;

/**
 * Class MovieDetailActivity
 * Author: Chris Hare
 * Created: 7/26/2015
 *
 * Activity to show the details of a particular movie
 */
public class MovieDetailActivity extends AppCompatActivity implements Constants{

    final static String TAG = MovieDetailActivity.class.getSimpleName();

    /**********************************************************************/
    /*                         Local Data                                 */
    /**********************************************************************/
    //Stack to hold parent of activiy to aid in Up navigation to different parents
    public static Stack<Class<?>> parents = new Stack<Class<?>>();
    //Class mParent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG,"In onCreate");
        //Add parent that called Movie Activity to stack
        parents.push(getClass());

        setContentView(R.layout.activity_movie_detail);

        if(savedInstanceState == null) {

            Bundle arguments = new Bundle();
            int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID,0);
            arguments.putInt(EXTRA_MOVIE_ID,movieId);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            Log.e(TAG,"onCreate got movie ID "  + movieId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container,fragment)
                    .commit();
        }
        else {
            Log.e(TAG,"onCreate saved instance state NOT null!");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
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
            case R.id.action_settings:
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
