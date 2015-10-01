package com.android.fourthwardcoder.popularmovies.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.adapters.FilmographyPagerAdapter;
import com.android.fourthwardcoder.popularmovies.fragments.PersonFilmographyFragment;
import com.android.fourthwardcoder.popularmovies.helpers.Util;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;

import java.util.ArrayList;


public class PersonFilmographyTabActivity extends AppCompatActivity implements Constants{

    /*************************************************************************/
    /*                             Constants                                 */
    /*************************************************************************/
    private static final String TAG = PersonFilmographyTabActivity.class.getSimpleName();


    /*************************************************************************/
    /*                             Local Data                                */
    /*************************************************************************/
    ArrayList<Fragment> fragList = new ArrayList<Fragment>();
    Fragment fragment = null;
    Fragment tabFragment = null;
    ActionBar mActionBar;

    int mTabPosition;
    int mPersonId = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmography);

        Log.e(TAG, "onCreate");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {
            Log.e(TAG,"!!!!!!!!!!! Got something in tab activity saved!!");
        }
        //Get PersonId from Intent
        mPersonId = getIntent().getIntExtra(EXTRA_PERSON_ID, 0);
        //Change status bar color
        Util.setStatusBarColor(this);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.filmography_tab_movies)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.filmography_tab_tv)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new FilmographyPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(),mPersonId);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        Log.e(TAG,"onSaveInstanceState saving person " + mPersonId);
        savedInstanceState.putInt(EXTRA_PERSON_ID,mPersonId);
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState (Bundle savedInstanceState) {

        Log.e(TAG,"!!!!!!!!!!!!! Hit onRestoreInstance");
    }



}
