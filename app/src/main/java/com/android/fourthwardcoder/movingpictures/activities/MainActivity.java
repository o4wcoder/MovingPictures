package com.android.fourthwardcoder.movingpictures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.adapters.DiscoverListPagerAdapter;
import com.android.fourthwardcoder.movingpictures.fragments.MainFragment;
import com.android.fourthwardcoder.movingpictures.fragments.MovieDetailFragment;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.MovieBasic;
import com.crashlytics.android.Crashlytics;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Class MainActivity
 * Author: Chris Hare
 * Created: 7/25/2015
 * <p/>
 * Main Activity of the PopularMovies App
 */
public class MainActivity extends AppCompatActivity implements MainFragment.Callback,
        Constants, NavigationView.OnNavigationItemSelectedListener {

    /*************************************************************/
    /*                       Constants                           */
    /*************************************************************/
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    /*************************************************************/
    /*                       Local Data                          */
    /*************************************************************/
    boolean mTwoPane;
    TabLayout mTabLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);


        Util.setStatusBarColor(this);

        //Set toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setPagerForSelection(ENT_TYPE_MOVIE);

        if ((findViewById(R.id.movie_detail_container) != null)) {
            //The detail container view will be present only in the large-screen layouts
            //(res/layout-sw600dp). If this view is present, then the activity should
            //be two-pane mode.
            mTwoPane = true;
            Log.e(TAG,"onCreate: mTwoPane = true");
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(),
                                DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            Log.e(TAG,"onCreate: onTwoPane = false");
            mTwoPane = false;
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        @EntertainmentType int entType = 0;

        if (id == R.id.nav_movies) {
            entType = ENT_TYPE_MOVIE;
            setTitle(R.string.title_movies);
        } else if (id == R.id.nav_tv_shows) {
            entType = ENT_TYPE_TV;
            setTitle(R.string.title_tv_shows);
        } else if (id == R.id.nav_people) {
            entType = ENT_TYPE_PERSON;
            setTitle(R.string.title_people);
        } else if (id == R.id.nav_favorites) {
            entType = ENT_TYPE_FAVORITE;
            setTitle(R.string.title_favorites);
        }

        setPagerForSelection(entType);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(@EntertainmentType int entType,int movieId, ImageView imageView) {

        if (mTwoPane) {
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment
            //transaction
            Log.e(TAG,"onItemSelected(): with twoPane");
            Bundle args = new Bundle();
            args.putInt(EXTRA_ID, movieId);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = null;
            if(entType == ENT_TYPE_MOVIE)
               intent = new Intent(this, MovieDetailActivity.class);
            else if(entType == ENT_TYPE_TV)
                intent = new Intent(this,TvDetailActivity.class);
            else if(entType == ENT_TYPE_PERSON)
                intent = new Intent(this,PersonDetailActivity.class);

            intent.putExtra(EXTRA_ID,movieId);


            if(intent != null) {
                //Start shared element transition for the movie poster
                ActivityOptionsCompat activityOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                new Pair<View, String>(imageView, getString(R.string.trans_poster)));

                startActivity(intent, activityOptions.toBundle());
            }

        }
    }

    @Override
    public void onLoadFinished(int id) {

        Log.e(TAG,"In onLoadFinsished()");
        if (mTwoPane) {
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment
            //transaction
            Log.e(TAG,"In two pane, setting details fragment");
            Bundle args = new Bundle();
            args.putInt(EXTRA_ID, id);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            //Set first movie in detail pane. Needed to use "commitAllowingStateLoss"
            //instead of just "commit" because calling this directly when the loader
            //was done causes an "illegal state exception"
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commitAllowingStateLoss();
        }
        else {
            Log.e(TAG,"onLoadFinished(): NOt in two pane!!!!!!!!");
        }
    }

    /***************************************************************************************/
    /*                                  Private Methods                                    */
    /***************************************************************************************/

    private void setPagerForSelection(@EntertainmentType int entType) {

        //Create Tab layout
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mTabLayout.removeAllTabs();
        if (entType == ENT_TYPE_MOVIE) {
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_popular)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_now_playing)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_upcoming)));

        } else if (entType == ENT_TYPE_TV) {
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_popular)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_airing_tonight)));
        } else if (entType == ENT_TYPE_PERSON) {
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_popular)));
        } else if(entType == ENT_TYPE_FAVORITE) {
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_movies)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_tv_shows)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_people)));
        }

        //Create view pager for tabs
        mViewPager = (ViewPager)findViewById(R.id.pager);
        PagerAdapter adapter = new DiscoverListPagerAdapter(getSupportFragmentManager(),entType,
                mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

//    private void updateFragment(Fragment fragment) {
//        if (fragment != null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.addToBackStack(null);
//            transaction.replace(R.id.fragment_container, fragment);
//            transaction.commit();
//        }
//
//    }
}
