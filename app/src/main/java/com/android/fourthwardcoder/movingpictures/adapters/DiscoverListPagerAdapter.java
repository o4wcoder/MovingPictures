package com.android.fourthwardcoder.movingpictures.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.fourthwardcoder.movingpictures.fragments.MainFragment;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;

/**
 * Created by Chris Hare on 6/24/2016.
 */
public class DiscoverListPagerAdapter extends FragmentStatePagerAdapter implements Constants {

    int mNumOfTabs;
    @EntertainmentType int mEntType;

    public DiscoverListPagerAdapter( FragmentManager fm, @EntertainmentType int entType, int numOfTabs) {
        super(fm);

        mEntType = entType;
        mNumOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                MainFragment fragment1 = MainFragment.newInstance(mEntType,SORT_POPULAR);
                return fragment1;
            case 1:
                MainFragment fragment2 = MainFragment.newInstance(mEntType,SORT_NOW_PLAYING);
                return fragment2;
            case 2:
                MainFragment fragment3 = MainFragment.newInstance(mEntType,SORT_UPCOMING);
                return fragment3;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
