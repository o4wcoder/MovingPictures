package com.android.fourthwardcoder.popularmovies.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.fragments.PersonSinglePhotoFragment;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.PersonPhoto;

import java.util.ArrayList;


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

        mPhotoList = getIntent().getParcelableArrayListExtra(EXTRA_PERSON_PHOTO);
        String personName = getIntent().getStringExtra(EXTRA_PERSON_NAME);
        Log.e(TAG, "Got person name " + personName);
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_person_single_photo, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
