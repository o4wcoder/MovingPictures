package com.fourthwardmobile.android.movingpictures.helpers;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.activities.SearchableActivity;

/**
 * Created by Chris Hare on 7/13/2016.
 */
public class SearchBar {

    private static final String TAG = SearchBar.class.getSimpleName();

    FragmentActivity mActivity;
    Toolbar mToolbar;
    int mPrimaryColor;
    int mDarkPrimaryColor;
    SearchView searchView;

    public SearchBar(Menu menu, MenuInflater inflater, FragmentActivity activity,
                     Toolbar toolbar, int primaryColor, int darkPrimaryColor) {

        mActivity = activity;
        mToolbar = toolbar;
        mPrimaryColor = primaryColor;
        mDarkPrimaryColor = darkPrimaryColor;

        inflater.inflate(R.menu.menu_search, menu);

        //Get the SearchView and set teh searchable configuration
        SearchManager searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenu = (MenuItem) menu.findItem(R.id.action_search_db);
        searchView = (SearchView) searchMenu.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(mActivity, SearchableActivity.class)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Close searchView after search button clicked
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

                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Log.e(TAG, "onSuggestionClick");
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"onClick");
                mToolbar.setBackgroundColor(mPrimaryColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mActivity.getWindow().setStatusBarColor(mDarkPrimaryColor);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = mActivity.getWindow();

                        // clear FLAG_TRANSLUCENT_STATUS flag:
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                        window.setStatusBarColor(mDarkPrimaryColor);
                    }
                }
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.e(TAG,"onClose()");
                mToolbar.getBackground().setAlpha(0);
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                return false;
            }
        });
    }
}
