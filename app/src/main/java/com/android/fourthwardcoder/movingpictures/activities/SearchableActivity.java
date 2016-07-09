package com.android.fourthwardcoder.movingpictures.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.fragments.ShowAllListFragment;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;

public class SearchableActivity extends AppCompatActivity implements Constants {

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white, null));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "Back pressed");

                    //Kill this activity
                    finish();
                }
            });
        }

        setTitle(getString(R.string.search_results));
        Log.e(TAG,"onCreate()");
        //Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Log.e(TAG,"onCreate with query = " + query);

            if(savedInstanceState == null) {

                ShowAllListFragment fragment = ShowAllListFragment.newInstance(0,ENT_TYPE_SEARCH,LIST_TYPE_SEARCH,query);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_container, fragment)
                        .commit();
            }

        } else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
          String uri = intent.getDataString();
           // String title = intent.getStringExtra(SearchManager.SUGGEST_COLUMN_TEXT_1);
           Log.e(TAG,"onCreate with action = " + uri);
            //Log.e(TAG,"onCreate with title = " + title);
            Uri data = intent.getData();

            ResultQueryHandler handler = new ResultQueryHandler(this);
            handler.startQuery(0,null,data,null,null,null,null);

        }
    }


    static class ResultQueryHandler extends AsyncQueryHandler {

         SearchableActivity mActivity;
        public ResultQueryHandler(SearchableActivity activity) {
            super(activity.getContentResolver());

            mActivity = activity;
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token,cookie,cursor);

            if(cursor == null || cursor.getCount() == 0) return;

            cursor.moveToFirst();

            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
            long mediaId = cursor.getLong(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID));
            String mediaType = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA));

            cursor.close();

            Log.e(TAG,"onQueryComplete() id = " + id + ", title = " + title + ", mediaId =" + mediaId + ", mediaType = " + mediaType);
            Util.startDetailActivity(mActivity,(int)mediaId,Util.convertStringMediaTypeToEnt(mediaType),null);
            mActivity.finish();
        }
    }


}
