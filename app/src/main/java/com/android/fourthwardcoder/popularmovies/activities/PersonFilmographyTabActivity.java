package com.android.fourthwardcoder.popularmovies.activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.fourthwardcoder.popularmovies.R;
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

    int mTabPosition;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmography);

        //Change status bar color
        Util.setStatusBarColor(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {


                mTabPosition = tab.getPosition();

                //Already created the fragment, just grab it
                if(fragList.size() > tab.getPosition())
                    fragList.get(tab.getPosition());

                if(fragment == null) {

                    Bundle bundle = new Bundle();
                    tabFragment = new PersonFilmographyFragment();
                    bundle.putInt(EXTRA_FILMOGRAPHY_TAB,tab.getPosition());
                    tabFragment.setArguments(bundle);
                    fragList.add(tabFragment);
                }
                else {
                        tabFragment = fragment;
                }

                ft.replace(R.id.fragment_container, tabFragment);

                Log.e(TAG, "TAb " + tab.getPosition());


            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        actionBar.addTab(actionBar.newTab().setText(R.string.filmography_tab_movies).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(R.string.filmography_tab_tv).setTabListener(tabListener));

        if(savedInstanceState != null) {
            mTabPosition = savedInstanceState.getInt(EXTRA_FILMOGRAPHY_TAB);
            Log.e(TAG,"OnCreate setting tab " + mTabPosition);
            actionBar.setSelectedNavigationItem(mTabPosition);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        Log.e(TAG,"onSaveInstanceState with tab pos " + mTabPosition);
        savedInstanceState.putInt(EXTRA_FILMOGRAPHY_TAB, mTabPosition);
        super.onSaveInstanceState(savedInstanceState);
    }
}
