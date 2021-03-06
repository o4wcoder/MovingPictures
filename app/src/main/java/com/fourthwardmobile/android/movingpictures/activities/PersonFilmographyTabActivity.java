package com.fourthwardmobile.android.movingpictures.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fourthwardmobile.android.movingpictures.adapters.FilmographyPagerAdapter;
import com.fourthwardmobile.android.movingpictures.helpers.Util;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;

/**
 * Class PersonFilmographyTabActivity
 * Author: Chris Hare
 * Created: 9/30/15
 * <p/>
 * Activity to hold the pager that holds the tabs of the person's fimography. They are
 * two tabs; Movies and TV.
 */
public class PersonFilmographyTabActivity extends AppCompatActivity implements Constants {

    /*************************************************************************/
    /*                             Constants                                 */
    /*************************************************************************/
    private static final String TAG = PersonFilmographyTabActivity.class.getSimpleName();

    /*************************************************************************/
    /*                             Local Data                                */
    /*************************************************************************/
    int mPersonId = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fourthwardmobile.android.movingpictures.R.layout.activity_filmography);

        //Set toolbar
        final Toolbar toolbar = (Toolbar) findViewById(com.fourthwardmobile.android.movingpictures.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mPersonId = savedInstanceState.getInt(EXTRA_ID);
        } else {
            //Get PersonId from Intent
            mPersonId = getIntent().getIntExtra(EXTRA_ID, 0);
        }
        //Change status bar color
        Util.setStatusBarColor(this);

        //Create TabLayout for the Filmography (Movies or TV Shows)
        TabLayout tabLayout = (TabLayout) findViewById(com.fourthwardmobile.android.movingpictures.R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(com.fourthwardmobile.android.movingpictures.R.string.filmography_tab_movies)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(com.fourthwardmobile.android.movingpictures.R.string.filmography_tab_tv)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Create ViewPager to swipe between the tabs
        final ViewPager viewPager = (ViewPager) findViewById(com.fourthwardmobile.android.movingpictures.R.id.pager);
        final PagerAdapter adapter = new FilmographyPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(), mPersonId);
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

        savedInstanceState.putInt(EXTRA_ID, mPersonId);
        super.onSaveInstanceState(savedInstanceState);
    }
}
