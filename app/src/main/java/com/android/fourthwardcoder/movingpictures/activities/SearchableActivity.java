package com.android.fourthwardcoder.movingpictures.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.fourthwardcoder.movingpictures.R;

public class SearchableActivity extends AppCompatActivity {

    /****************************************************************************************/
    /*                                  Constants                                           */
    /****************************************************************************************/
    private static final String TAG = SearchableActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchable);

        //Set toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.search_results));
        Log.e(TAG,"onCreate()");
        //Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.e(TAG,"onCreate with query = " + query);
        }
    }
}
