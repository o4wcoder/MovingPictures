package com.android.fourthwardcoder.movingpictures.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.adapters.DiscoverListPagerAdapter;
import com.android.fourthwardcoder.movingpictures.fragments.MainFragment;
import com.android.fourthwardcoder.movingpictures.fragments.MovieDetailFragment;
import com.android.fourthwardcoder.movingpictures.fragments.PersonDetailFragment;
import com.android.fourthwardcoder.movingpictures.fragments.TvDetailFragment;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

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

    //Saved Arguments
    private static final String ARG_ENT_TYPE = "ent_type";
    private static final String ARG_SORT_ORDER = "sort_order";
    /*************************************************************/
    /*                       Local Data                          */
    /*************************************************************/
    boolean mTwoPane;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    int mEntType;
    int mSortOrder;

    //First Movie Ids
    int mMoviePopularId;
    int mMovieNowPlayingId;
    int mMovieUpcomingId;

    //First TV Ids
    int mTvPopularId;
    int mTvAiringTonightId;

    //Firt Person Id
    int mPersonPopularId;

    //First Favorites Ids
    int mFavoriteMovieId;
    int mFavoriteTvId;
    int mFavoritPeopleId;



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

        if(savedInstanceState != null) {
            mEntType = savedInstanceState.getInt(ARG_ENT_TYPE);
            mSortOrder = savedInstanceState.getInt(ARG_SORT_ORDER);
            Log.e(TAG,"onCreate() Got saved ent type on rotation" + mEntType);
        } else {
            //First time through. Start with movies
           mEntType = ENT_TYPE_MOVIE;
            mSortOrder = SORT_POPULAR;
        }

        setPagerForSelection();

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            //    Log.e(TAG,"onTabSelected() with ent type = " + mEntType);
                int firstId = 0;
                if(mEntType == ENT_TYPE_MOVIE) {

                    switch (tab.getPosition()) {
                        case 0:
                            firstId = mMoviePopularId;
                            break;
                        case 1:
                            firstId = mMovieNowPlayingId;
                            break;
                        case 2:
                            firstId = mMovieUpcomingId;
                            break;
                    }
                } else if(mEntType == ENT_TYPE_TV) {
                    switch (tab.getPosition()) {
                        case 0:
                            firstId = mTvPopularId;
                            break;
                        case 1:
                            firstId = mTvAiringTonightId;
                            break;
                    }

                } else if(mEntType == ENT_TYPE_PERSON) {
                    switch (tab.getPosition()) {
                        case 0:
                            firstId = mPersonPopularId;
                            break;
                    }
                }
                else if(mEntType == ENT_TYPE_FAVORITE) {
                    switch(tab.getPosition()) {
                        case 0:
                            firstId = mFavoriteMovieId;
                            mSortOrder = SORT_MOVIES;
                            break;
                        case 1:
                            firstId = mFavoriteTvId;
                            mSortOrder = SORT_TV;
                            break;
                        case 2:
                            firstId = mFavoritPeopleId;
                            mSortOrder = SORT_PERSON;
                            break;
                    }
                }


//                Log.e(TAG,"onTabSelected() with popId = " + mMoviePopularId + ", npId = "
//                        + mMovieNowPlayingId + ", upUd = " + mMovieUpcomingId);
                setTwoFrameDetailFragment(mEntType,firstId);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(ARG_ENT_TYPE,mEntType);
        savedInstanceState.putInt(ARG_SORT_ORDER,mSortOrder);
        super.onSaveInstanceState(savedInstanceState);
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

        //Get the SearchView and set teh searchable configuration
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenu = (MenuItem)menu.findItem(R.id.action_search_db);
        final SearchView searchView = (SearchView)searchMenu.getActionView();
        //Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchableActivity.class)));
       // searchView.setIconifiedByDefault(false);
      //  searchView.setIconified(true);
       // searchView.onActionViewCollapsed();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Closet searchView after search button clicked
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {

                Log.e(TAG,"onSuggestionListenter");
               // searchView.setIconified(true);
                searchMenu.collapseActionView();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Log.e(TAG,"onSuggestionClick");
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }
        });


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

        if (id == R.id.nav_movies) {
            mEntType = ENT_TYPE_MOVIE;
        } else if (id == R.id.nav_tv_shows) {
            mEntType = ENT_TYPE_TV;
        } else if (id == R.id.nav_people) {
            mEntType = ENT_TYPE_PERSON;
        } else if (id == R.id.nav_favorites) {
            mEntType = ENT_TYPE_FAVORITE;
            mSortOrder = SORT_MOVIES;
        }

        setPagerForSelection();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(@EntertainmentType int entType,int id, ImageView imageView) {

        if (mTwoPane) {
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment
            //transaction
            Log.e(TAG,"onItemSelected(): with twoPane with entType = " + entType);
            Bundle args = new Bundle();
            args.putInt(EXTRA_ID, id);

            Fragment fragment = null;

            if(entType == ENT_TYPE_MOVIE) {
                fragment = MovieDetailFragment.newInstance(id);
            }
            else if(entType == ENT_TYPE_TV) {
                fragment = TvDetailFragment.newInstance(id);
            }
            else if(entType == ENT_TYPE_PERSON)
                fragment = PersonDetailFragment.newInstance(id);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = null;
            Log.e(TAG,"onItemSelected with entType = " + entType + " id = " + id);
            if(entType == ENT_TYPE_MOVIE)
               intent = new Intent(this, MovieDetailActivity.class);
            else if(entType == ENT_TYPE_TV)
                intent = new Intent(this,TvDetailActivity.class);
            else if(entType == ENT_TYPE_PERSON)
                intent = new Intent(this,PersonDetailActivity.class);

            Log.e(TAG,"onItemSelected() put extra id = " + id);
            intent.putExtra(EXTRA_ID,id);


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
    public void onLoadFinished(@EntertainmentType int entType, int sortOrder, int id) {

      //  Log.e(TAG,"In onLoadFiished()");
        if (mTwoPane) {
            //In two-pane mode, show the detail view in this activity by
            //adding or replacing the detail fragment using a fragment
            //transaction
//          Log.e(TAG,"onLoadFinished() In two pane, setting details fragment withe ent type = " + entType +
//             ", sortOrder = " + sortOrder + ", id = " + id);

            //Store sort order. Need this for Favorites.
            mSortOrder = sortOrder;
            //Store off ids from the loading fragments in each tab
            setFirstDetailId(entType,sortOrder,id);

            //Set the first detail fragment from the first tab. This works because Movies, TV
            // and People all have the same first sort (Popular)
            if(sortOrder == mTabLayout.getSelectedTabPosition()) {
                setTwoFrameDetailFragment(entType, id);
            }
            //For the favorites we need to check the sort order to see if it's the first one (movies)
            else if(entType == ENT_TYPE_FAVORITE && sortOrder == SORT_MOVIES && mTabLayout.getSelectedTabPosition() == 0) {
                setTwoFrameDetailFragment(entType,id);
            }
        }
//        else {
//            Log.e(TAG,"onLoadFinished(): NOt in two pane!!!!!!!!");
//        }
    }

    /***************************************************************************************/
    /*                                  Private Methods                                    */
    /***************************************************************************************/

    private void setTwoFrameDetailFragment(@EntertainmentType int entType, int id) {

        if(mTwoPane) {
//            Log.e(TAG, "setTwoFramDetailFragment() with entType = " + entType + ", id = " + id +
//            ", sort order = " + mSortOrder);
            //If we don't have an ID, don't bother trying to set up a detail fragment
            if (id > 0) {
                Fragment fragment = null;

                //Select the fragment type based on ENT type (Movie, TV or Person). Or by
                //the sort type if it's a Favorites type.
                if (entType == ENT_TYPE_MOVIE || mSortOrder == SORT_MOVIES) {
                    fragment = MovieDetailFragment.newInstance(id);
                } else if (entType == ENT_TYPE_TV || mSortOrder == SORT_TV) {
                    fragment = TvDetailFragment.newInstance(id);
                } else if (entType == ENT_TYPE_PERSON || mSortOrder == SORT_PERSON)
                    fragment = PersonDetailFragment.newInstance(id);
                else {
                    return;
                }

                //Set first movie in detail pane. Needed to use "commitAllowingStateLoss"
                //instead of just "commit" because calling this directly when the loader
                //was done causes an "illegal state exception"
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                        .commitAllowingStateLoss();
            }
        }

    }

    private void setFirstDetailId(@EntertainmentType int entType, int sortOrder, int id) {

//        Log.e(TAG,"setFirstDetailId() with sort order = " + sortOrder + ", id = " + id);
        if (entType == ENT_TYPE_MOVIE) {
            switch (sortOrder) {

                case SORT_POPULAR:
                    mMoviePopularId = id;
                    break;
                case SORT_NOW_PLAYING:
                    mMovieNowPlayingId = id;
                    break;
                case SORT_UPCOMING:
                    mMovieUpcomingId = id;
                    break;
                case SORT_MOVIES:
                    mFavoriteMovieId = id;
                    break;
            }
        } else if (entType == ENT_TYPE_TV) {
            switch (sortOrder) {
                case SORT_POPULAR:
                    mTvPopularId = id;
                    break;
                case SORT_AIRING_TONIGHT:
                    mTvAiringTonightId = id;
                    break;
                case SORT_TV:
                    mFavoriteTvId = id;
                    break;
            }
        } else if (entType == ENT_TYPE_PERSON) {
            switch (sortOrder) {
                case SORT_POPULAR:
                    mPersonPopularId = id;
                    break;
                case SORT_PERSON:
                    mFavoritPeopleId = id;
                    break;
            }
        } else if (entType == ENT_TYPE_FAVORITE) {
            switch (sortOrder) {
                case SORT_MOVIES:
                    mFavoriteMovieId = id;
                    break;
                case SORT_TV:
                    mFavoriteTvId = id;
                    break;
                case SORT_PERSON:
                    mFavoritPeopleId = id;
                    break;
            }
        }

//        Log.e(TAG,"setFirstDetailId() for Favorites with movieId = " + mFavoriteMovieId + ", tvId = "
//             + mFavoriteTvId + ", personId = " + mFavoritPeopleId);
    }

    private void setPagerForSelection() {

        //Create Tab layout
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mTabLayout.removeAllTabs();
        if (mEntType == ENT_TYPE_MOVIE) {
            //Set title of activity
            setTitle(R.string.title_movies);

            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_popular)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_now_playing)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_upcoming)));

        } else if (mEntType == ENT_TYPE_TV) {
            //Set title of activity
            setTitle(R.string.title_tv_shows);

            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_popular)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_airing_tonight)));
        } else if (mEntType == ENT_TYPE_PERSON) {
            //Set title of activity
            setTitle(R.string.title_people);

            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_popular)));
        } else if(mEntType == ENT_TYPE_FAVORITE) {
            //Set title of activity
            setTitle(R.string.title_favorites);

            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_movies)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_tv_shows)));
            mTabLayout.addTab(mTabLayout.newTab()
                    .setText(getString(R.string.tab_people)));
        }

        //Create view pager for tabs
        mViewPager = (ViewPager)findViewById(R.id.pager);
        PagerAdapter adapter = new DiscoverListPagerAdapter(getSupportFragmentManager(),mEntType,
                mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            Toast.makeText(this, "Suggestion: "+ uri, Toast.LENGTH_SHORT).show();
        }
    }
}
