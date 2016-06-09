package com.android.fourthwardcoder.movingpictures.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.helpers.Util;

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
        //Util.setStatusBarColor(this);

        setContentView(R.layout.activity_person_photos);
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
