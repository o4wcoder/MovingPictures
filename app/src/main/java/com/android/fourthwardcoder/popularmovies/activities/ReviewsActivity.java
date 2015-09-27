package com.android.fourthwardcoder.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.helpers.Util;


public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change status bar color
        Util.setStatusBarColor(this);

        setContentView(R.layout.activity_movie_reviews);
    }

}
