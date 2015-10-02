package com.android.fourthwardcoder.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.helpers.Util;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;

/**
 * Class: TvDetailActivity
 * Author: Chris Hare
 * Created: 9/25/2015
 * <p/>
 * Activity for the TV Show Details.
 */
public class TvDetailActivity extends AppCompatActivity implements Constants {

    /**********************************************************************/
    /*                          Constants                                 */
    /**********************************************************************/
    private final static String TAG = TvDetailActivity.class.getSimpleName();

    int mPersonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change status bar color
        Util.setStatusBarColor(this);

        setContentView(R.layout.activity_tv_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPersonId = getIntent().getIntExtra(EXTRA_PERSON_ID, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
