package com.android.fourthwardcoder.movingpictures.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.fragments.PersonSinglePhotoFragment;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.PersonPhoto;

import java.util.ArrayList;

/**
 * Class PersonPhotoPageActivityActivity
 * Author: Chris Hare
 * Created: 8/26/2015
 *
 * Activity to show the GridView of photos of a person.
 */
public class PersonPhotoPagerActivity extends AppCompatActivity implements Constants {

    /**********************************************************************/
    /*                             Constants                              */
    /**********************************************************************/
    private static final String TAG = PersonPhotoPagerActivity.class.getSimpleName();

    /**********************************************************************/
    /*                             Local Data                             */
    /**********************************************************************/
    ViewPager mPager;
    ArrayList<PersonPhoto> mPhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_pager);

//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change status bar color
      //  Util.setStatusBarColor(this);

        mPhotoList = getIntent().getParcelableArrayListExtra(EXTRA_PERSON_PHOTO);
        String personName = getIntent().getStringExtra(EXTRA_PERSON_NAME);

        setTitle(personName);

        mPager = (ViewPager)findViewById(R.id.pager);

        //Get Activities instance of the Fragment Manager
        FragmentManager fm = getSupportFragmentManager();

        mPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                PersonPhoto personPhoto = mPhotoList.get(position);

                return PersonSinglePhotoFragment.newInstance(personPhoto.getFullImagePath());
            }

            @Override
            public int getCount() {
                return mPhotoList.size();
            }
        });

        //Start pager on the position of the photo selected so we just don't start on the first one
        int photoPos = getIntent().getIntExtra(EXTRA_PERSON_PHOTO_ID,0);
        mPager.setCurrentItem(photoPos);
    }
}
