package com.android.fourthwardcoder.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.helpers.Util;

/**
 *  Class PersonPhotosActivity
 *  Author: Chris Hare
 *
 *  Activity to hold a single persons photo
 */
public class PersonPhotosActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change status bar color
        Util.setStatusBarColor(this);

        setContentView(R.layout.activity_person_photos);
    }
}
