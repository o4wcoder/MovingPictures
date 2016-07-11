package com.fourthwardmobile.android.movingpictures.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.fourthwardmobile.android.movingpictures.fragments.ShowAllListFragment;
import com.fourthwardmobile.android.movingpictures.helpers.Util;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;

/**
 * Class ShowAllListActivity
 * Author: Chris Hare
 * Created: 9/25/15
 *
 * Activity to hold the Cast List of a MovieOld or TV show
 */
public class ShowAllListActivity extends ActionBarActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fourthwardmobile.android.movingpictures.R.layout.activity_movie_cast);

        //Set Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(com.fourthwardmobile.android.movingpictures.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        int id = getIntent().getIntExtra(EXTRA_ID,0);
        int entType = getIntent().getIntExtra(EXTRA_ENT_TYPE,0);
        int listType = getIntent().getIntExtra(EXTRA_LIST_TYPE,0);
        String title = getIntent().getStringExtra(EXTRA_TITLE);

        setTitle(title);
        ShowAllListFragment fragment = ShowAllListFragment.newInstance(id,entType,listType,null);

        getSupportFragmentManager().beginTransaction()
                .add(com.fourthwardmobile.android.movingpictures.R.id.detail_container, fragment)
                .commit();

        //Change status bar color
        Util.setStatusBarColor(this);
    }
}
