package com.android.fourthwardcoder.movingpictures.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.fourthwardcoder.movingpictures.R;
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

        setTitle(getString(R.string.search_results));
        Log.e(TAG,"onCreate()");
        //Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String posterPath = intent.getStringExtra(SearchManager.SUGGEST_COLUMN_ICON_1);
            Log.e(TAG,"onCreate with query = " + query);
            Log.e(TAG,"onCreate with posterPath = " + posterPath);
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

    private static @EntertainmentType int convertStringMediaTypeToEnt(String mediaType) {

        if(mediaType.equals(MEDIA_TYPE_MOVIE))
            return ENT_TYPE_MOVIE;
        else if(mediaType.equals(MEDIA_TYPE_TV))
            return ENT_TYPE_TV;
        else
            return ENT_TYPE_PERSON;


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
            Util.startDetailActivity(mActivity,(int)mediaId,convertStringMediaTypeToEnt(mediaType),null);
            mActivity.finish();
        }
    }


}
