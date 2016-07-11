package com.fourthwardmobile.android.movingpictures.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fourthwardmobile.android.movingpictures.helpers.Util;

/**
 * Class ReviewsActivity
 * Author: Chris Hare
 * Created: 8/25/2015
 * <p/>
 * Main Activity for reviews of a movie or a TV show
 */
public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change status bar color
        Util.setStatusBarColor(this);

        setContentView(com.fourthwardmobile.android.movingpictures.R.layout.activity_movie_reviews);
        final Toolbar toolbar = (Toolbar) findViewById(com.fourthwardmobile.android.movingpictures.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
