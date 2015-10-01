package com.android.fourthwardcoder.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.helpers.Util;

/**
 * Class CastListActivity
 * Author: Chris Hare
 * Created: 9/25/15
 *
 * Activity to hold the Cast List of a Movie or TV show
 */
public class CastListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_cast);

        //Set Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change status bar color
        Util.setStatusBarColor(this);
    }
}
