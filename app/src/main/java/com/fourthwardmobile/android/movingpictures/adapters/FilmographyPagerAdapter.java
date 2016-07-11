package com.fourthwardmobile.android.movingpictures.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fourthwardmobile.android.movingpictures.fragments.ShowAllListFragment;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;

/**
 * Class FilmographyPagerAdapter
 * Author: Chris Hare
 * Created: 9/30/2015.
 * <p/>
 * Adapter for the Filmography Pager
 */
public class FilmographyPagerAdapter extends FragmentStatePagerAdapter implements Constants {

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
                ShowAllListFragment tab1 = ShowAllListFragment.newInstance(mPersonId,position, LIST_TYPE_MOVIE,null);
                return tab1;
            case 1:
                ShowAllListFragment tab2 = ShowAllListFragment.newInstance(mPersonId,position, LIST_TYPE_TV,null);
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
