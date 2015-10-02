package com.android.fourthwardcoder.popularmovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.android.fourthwardcoder.popularmovies.fragments.PersonFilmographyFragment;

/**
 * Class FilmographyPagerAdapter
 * Author: Chris Hare
 * Created: 9/30/2015.
 * <p/>
 * Adapter for the Filmography Pager
 */
public class FilmographyPagerAdapter extends FragmentStatePagerAdapter {

    /*************************************************************************/
    /*                             Constants                                 */
    /*************************************************************************/
    final static String TAG = FragmentStatePagerAdapter.class.getSimpleName();

    /*************************************************************************/
    /*                             Local Data                                */
    /*************************************************************************/
    int mNumOfTabs;
    int mPersonId;

    public FilmographyPagerAdapter(FragmentManager fm, int numOfTabs, int personId) {
        super(fm);

        this.mNumOfTabs = numOfTabs;
        this.mPersonId = personId;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                PersonFilmographyFragment tab1 = PersonFilmographyFragment.newInstance(position, mPersonId);
                return tab1;
            case 1:
                PersonFilmographyFragment tab2 = PersonFilmographyFragment.newInstance(position, mPersonId);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
