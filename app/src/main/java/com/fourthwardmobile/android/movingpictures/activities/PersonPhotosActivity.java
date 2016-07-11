package com.fourthwardmobile.android.movingpictures.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fourthwardmobile.android.movingpictures.fragments.PersonPhotosFragment;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;

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


        setContentView(com.fourthwardmobile.android.movingpictures.R.layout.activity_person_photos);

        //If we are being restored from a previous state, then don't recreate the fragment
        //or it will get builts twice. Just return
        if(savedInstanceState != null)
            return;

        int personId = getIntent().getIntExtra(EXTRA_ID, 0);
        String personName = getIntent().getStringExtra(EXTRA_PERSON_NAME);
        PersonPhotosFragment fragment = PersonPhotosFragment.newInstance(personId,personName);
        //fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(com.fourthwardmobile.android.movingpictures.R.id.detail_container, fragment)
                .commit();

    }
}
