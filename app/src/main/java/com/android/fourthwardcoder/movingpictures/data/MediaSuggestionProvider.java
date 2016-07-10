package com.android.fourthwardcoder.movingpictures.data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.MediaBasic;
import com.android.fourthwardcoder.movingpictures.models.MediaList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    ArrayList<MediaBasic> mResultList;

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

    private static Drawable getDrawableFromUrl(String url) throws IOException {

        Bitmap bitmap;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");
        connection.connect();
        InputStream inputStream = connection.getInputStream();

        bitmap = BitmapFactory.decodeStream(inputStream);
        return new BitmapDrawable(bitmap);
    }

    private void addCursorRow(int position, MatrixCursor cursor, MediaBasic media) {

        String text = "";
        String subText = "";
        String mediaType = media.getMediaType();
        if (mediaType.equals(MEDIA_TYPE_MOVIE)) {
            text = media.getTitle();
            subText = getContext().getString(R.string.search_subtext_movie) +
                    " (" + media.getReleaseYear() + ")";
        }
        else if(mediaType.equals(MEDIA_TYPE_TV)) {
            text = media.getName();
            subText = getContext().getString(R.string.search_subtext_tv_series) +
                    " (" + media.getFirstAirYear() + ")";
        } else {
            text = media.getName();
        }


        int mediaId = media.getId();
        cursor.addRow(new Object[]{position, text,subText,mediaId,mediaType});

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String query = uri.getLastPathSegment().toUpperCase();
        Log.e(TAG,"query param = " + query);
        Call<MediaList> call = null;


        MatrixCursor cursor = new MatrixCursor(
                new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_TEXT_2,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                        SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
                });

        if(sUriMatcher.match(uri) == ALL_SUGGESTION) {
            if (!(query.equals(SearchManager.SUGGEST_URI_PATH_QUERY))) {

                call = MovieDbAPI.getMovieApiService().getSearchResultList(query);

                try {
                    MediaList queryResults = call.execute().body();

                    final int queryLimit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));
                    mResultList = (ArrayList) queryResults.getMediaResults();

                    //Add all query results into a row of the cursor
                    for (int i = 0; i < queryLimit && i < mResultList.size(); i++) {

                        addCursorRow(i,cursor,mResultList.get(i));

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Got retrofit io exception " + e.getMessage());
                }

                return cursor;
            }
        } else if(sUriMatcher.match(uri) == SINGLE_SUGGESTION) {

            int mediaId = Integer.parseInt(uri.getLastPathSegment());

            MediaBasic media = null;
            //Search for media id return in uri.
            for(int i = 0; i < mResultList.size(); i ++) {

                //if found media id, create cursor and return
                if(mResultList.get(i).getId() == mediaId) {
                    media = mResultList.get(i);

                    addCursorRow(i,cursor,media);
                    return cursor;
                }
            }

        }

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
