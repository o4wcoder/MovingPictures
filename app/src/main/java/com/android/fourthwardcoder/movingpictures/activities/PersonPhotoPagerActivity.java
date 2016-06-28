package com.android.fourthwardcoder.movingpictures.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.fragments.PersonSinglePhotoFragment;
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
public class PersonPhotoPagerActivity extends AppCompatActivity implements Constants, PersonSinglePhotoFragment.OnPhotoClick {

    /**********************************************************************/
    /*                             Constants                              */
    /**********************************************************************/
    private static final String TAG = PersonPhotoPagerActivity.class.getSimpleName();

    /**********************************************************************/
    /*                             Local Data                             */
    /**********************************************************************/
    ViewPager mPager;
    ArrayList<PersonPhoto> mPhotoList;
    FrameLayout mBottomPanel;
    Toolbar mToolbar;
    boolean mIsToolbarVisible = true;

    //Argument for saving state of toolbars
    private final String ARG_TOOLBAR_VISIBLE = "arg_toolbar_visible";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_pager);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPager = (ViewPager)findViewById(R.id.pager);
        mBottomPanel = (FrameLayout)findViewById(R.id.person_photo_bottom_panel);

        if(savedInstanceState != null) {

            mIsToolbarVisible = savedInstanceState.getBoolean(ARG_TOOLBAR_VISIBLE);
            setToolbarVisibility();
        }
        //Change status bar color
      //  Util.setStatusBarColor(this);

        mPhotoList = getIntent().getParcelableArrayListExtra(EXTRA_PERSON_PHOTO);
        String personName = getIntent().getStringExtra(EXTRA_PERSON_NAME);

        setTitle(personName);



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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean(ARG_TOOLBAR_VISIBLE,mIsToolbarVisible);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setToolbarVisibility() {

        if(mIsToolbarVisible) {
            mToolbar.setVisibility(View.VISIBLE);
            mBottomPanel.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.INVISIBLE);
            mBottomPanel.setVisibility(View.INVISIBLE);
        }

    }
    @Override
    public void onSetBottomPanelVisibility() {

        if(mIsToolbarVisible) {
            Animation topAnimHide = AnimationUtils.loadAnimation(this,R.anim.slide_down_from_top);
            Animation bottomAnimHide = AnimationUtils.loadAnimation(this, R.anim.slide_down_from_bottom);
            mToolbar.startAnimation(topAnimHide);
            mBottomPanel.startAnimation(bottomAnimHide);

            //Set the toolbars visibility and switch flag.

            mIsToolbarVisible = false;
            setToolbarVisibility();
        }
        else {
            Animation topAnimHide = AnimationUtils.loadAnimation(this,R.anim.slide_up_from_top);
            Animation animShow = AnimationUtils.loadAnimation(this, R.anim.slide_up_from_bottom);

            mToolbar.startAnimation(topAnimHide);
            mBottomPanel.startAnimation(animShow);

            //Set the toolbars visibility and switch flag.
            mIsToolbarVisible = true;
            setToolbarVisibility();
        }

    }
}
