package com.android.fourthwardcoder.movingpictures.data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.MediaBasic;
import com.android.fourthwardcoder.movingpictures.models.MediaList;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Chris Hare on 7/7/2016.
 */
public class MediaSuggestionProvider extends ContentProvider implements Constants {

    /******************************************************************************************/
    /*                                    Constants                                           */
    /******************************************************************************************/
    private static final String TAG = MediaSuggestionProvider.class.getSimpleName();

    static final int ALL_SUGGESTION = 101;
    static final int SINGLE_SUGGESTION = 102;

    //Content provider authority for Search
    private static final String CONTENT_AUTHORITY = "com.android.fourthwardcoder.movingpictures.mediasuggestion";

    private static UriMatcher sUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CONTENT_AUTHORITY,"search_suggest_query/*",ALL_SUGGESTION);
        uriMatcher.addURI(CONTENT_AUTHORITY,"/#",SINGLE_SUGGESTION);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String query = uri.getLastPathSegment().toUpperCase();
        final int queryLimit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));
        Log.e(TAG,"query param = " + query);
        Call<MediaList> call = null;

        if(!(query.equals(SearchManager.SUGGEST_URI_PATH_QUERY))) {

            call = MovieDbAPI.getMovieApiService().getSearchResultList(query);

                      try {
                          MediaList queryResults = call.execute().body();
                          Log.e(TAG,"response sucessful");

                                                  MatrixCursor cursor = new MatrixCursor(
                                new String[] {
                                        BaseColumns._ID,
                                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                                }
                        );

                          ArrayList<MediaBasic> resultList = (ArrayList)queryResults.getMediaResults();
                        Log.e(TAG,"Number of media results = " + resultList.size());
                        for (int i = 0; i < queryLimit &&  i < resultList.size(); i++) {

                            String text = "";
                            if(resultList.get(i).getMediaType().equals(MEDIA_TYPE_MOVIE))
                                text = resultList.get(i).getTitle();
                            else
                                text = resultList.get(i).getName();


                            cursor.addRow(new Object[]{i, text, i});
                        }

                          Log.e(TAG,"Return with cursor size = " + cursor.getCount());
                          return cursor;
                      } catch (IOException e) {
                          Log.e(TAG,"Got retrofit io exception " + e.getMessage());
                      }
        }

        Log.e(TAG,"Return null from query");
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
