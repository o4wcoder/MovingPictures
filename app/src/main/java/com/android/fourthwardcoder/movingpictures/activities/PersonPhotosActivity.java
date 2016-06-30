package com.android.fourthwardcoder.movingpictures.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.fragments.PersonDetailFragment;
import com.android.fourthwardcoder.movingpictures.fragments.PersonPhotosFragment;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;

/**
 *  Class PersonPhotosActivity
 *  Author: Chris Hare
 *
 *  Activity to hold a single persons photo
 */
public class PersonPhotosActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_person_photos);

        //If we are being restored from a previous state, then don't recreate the fragment
        //or it will get builts twice. Just return
        if(savedInstanceState != null)
            return;

        int personId = getIntent().getIntExtra(EXTRA_ID, 0);
        String personName = getIntent().getStringExtra(EXTRA_PERSON_NAME);
        PersonPhotosFragment fragment = PersonPhotosFragment.newInstance(personId,personName);
        //fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.detail_container, fragment)
                .commit();

    }
}
