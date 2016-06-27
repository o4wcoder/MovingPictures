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
                    MainFragment fragment1 = MainFragment.newInstance(mEntType, getSortType(position));
                    return fragment1;
                case 1:
                    MainFragment fragment2 = MainFragment.newInstance(mEntType, getSortType(position));
                    return fragment2;
                case 2:
                    MainFragment fragment3 = MainFragment.newInstance(mEntType, getSortType(position));
                    return fragment3;
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    private int getSortType(int position) {

        if(mEntType == ENT_TYPE_MOVIE) {

            switch (position) {

                case 0:
                    return SORT_POPULAR;
                case 1:
                    return SORT_NOW_PLAYING;
                case 2:
                    return SORT_UPCOMING;
                default:
                    return SORT_POPULAR;
            }
        } else if(mEntType == ENT_TYPE_TV) {

            switch (position) {

                case 0:
                    return SORT_POPULAR;
                case 1:
                    return SORT_AIRING_TONIGHT;
                default:
                    return SORT_POPULAR;
            }
        } else if (mEntType == ENT_TYPE_PERSON) {
            return SORT_POPULAR;

        } else if (mEntType == ENT_TYPE_FAVORITE) {

            switch (position) {

                case 0:
                    return SORT_MOVIES;
                case 1:
                    return SORT_TV;
                case 2:
                    return SORT_PERSON;
                default:
                    return SORT_MOVIES;

            }
        }

        //Got here something went wrong
        return SORT_POPULAR;
    }
}
