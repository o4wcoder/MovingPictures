package com.fourthwardmobile.android.movingpictures.helpers;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.activities.SearchableActivity;
import com.fourthwardmobile.android.movingpictures.activities.ShowAllListActivity;
import com.fourthwardmobile.android.movingpictures.activities.MovieDetailActivity;
import com.fourthwardmobile.android.movingpictures.activities.PersonDetailActivity;
import com.fourthwardmobile.android.movingpictures.activities.TvDetailActivity;
import com.fourthwardmobile.android.movingpictures.data.FavoritesContract;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.Genre;
import com.fourthwardmobile.android.movingpictures.models.IdNamePair;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import static android.view.WindowManager.*;

/**
 * Clsss Util
 * Author: Chris Hare
 * Created: 9/25/2015.
 *
 * Class to keep common methods used between Activities/Fragments
 */
public class Util implements Constants {

    /************************************************************************/
    /*                           Constants                                  */
    /************************************************************************/
    private static final String TAG = Util.class.getSimpleName();

    /**
     * Set status/notification bar color on Lollipop devices
     * @param activity Activity that is setting the status bar
     */
    public static void setStatusBarColor(Activity activity) {

       if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(com.fourthwardmobile.android.movingpictures.R.color.appDarkPrimaryColor));

        }
    }

    /**
     * Change Date string from "yyyy-MM-dd", to "MMM d, yyyy"
     * @param strDate String date to be changed
     * @return        String date of changed string
     */
    public static String reverseDateString(String strDate) {

        String strBirthDay;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
            strBirthDay = new SimpleDateFormat("MMM d, yyyy").format(date);
        }
        catch(ParseException pe) {
            strBirthDay = strDate;
        }

        return strBirthDay;
    }


    /**
     * Start Person Detail Activity of the cast memmber
     * @param context context of calling activity
     * @param id      id of person
     */
//    public static void startActorDetailActivity(Context context, int id, ImageView imageView) {
//
//        Intent i = new Intent(context,PersonDetailActivity.class);
//        i.putExtra(EXTRA_PERSON_ID,id);
//        Log.e(TAG,"startActorDetailActivity()");
//        if(imageView != null) {
//            ActivityOptionsCompat activityOptions =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
//                            new Pair<View, String>(imageView, context.getString(R.string.trans_poster)));
//
//            context.startActivity(i, activityOptions.toBundle());
//        }
//        else {
//            context.startActivity(i);
//        }
//    }

    public static void startDetailActivity(Context context, int id, @EntertainmentType int type,View imageView) {

        Intent intent = null;

        //Find out which type of detail activity we want to start
        if(type == ENT_TYPE_MOVIE)
            intent = new Intent(context,MovieDetailActivity.class);
        else if(type == ENT_TYPE_PERSON)
            intent = new Intent(context,PersonDetailActivity.class);
        else if(type == ENT_TYPE_TV)
            intent = new Intent(context, TvDetailActivity.class);

        if(intent != null) {
            intent.putExtra(EXTRA_ID, id);
            Log.e(TAG, "startDetailActivity()");
            if (imageView != null) {
                ActivityOptionsCompat activityOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                                new Pair<View, String>(imageView, context.getString(com.fourthwardmobile.android.movingpictures.R.string.trans_poster)));

                context.startActivity(intent, activityOptions.toBundle());
            } else {
                context.startActivity(intent);
            }
        } else {
            Log.e(TAG,"startDetailActivity() Intentn to start activity was null with ent type = " + type);
        }

    }
    /**
     * Create a clickable span over and actors name
     * @param passContext context of calling activity
     * @param passId      id of movie/tv show
     * @return            Clickable span for the cast name
     */
    private static ClickableSpan createClickablSpan(Context passContext, int passId) {

        final Context context = passContext;
        final int id = passId;
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

               startDetailActivity(context,id,ENT_TYPE_PERSON,null);
            }
        };

        return span;

    }

    public static void showListActivity(Context context, int id, String title, final int entType, final int listType) {

        Intent i = new Intent(context, ShowAllListActivity.class);
        i.putExtra(EXTRA_ID, id);
        i.putExtra(EXTRA_TITLE, title);
        i.putExtra(EXTRA_ENT_TYPE, entType);
        i.putExtra(EXTRA_LIST_TYPE,listType);
        context.startActivity(i);
    }

//    public static void showListActivity(Context context, TvShow tvShow, final int entType) {
//
//        Intent i = new Intent(context, ShowAllListActivity.class);
//        i.putExtra(EXTRA_ID, tvShow.getId());
//        i.putExtra(EXTRA_TITLE, tvShow.getName());
//            i.putExtra(EXTRA_ENT_TYPE, ENT_TYPE_TV);
//        context.startActivity(i);
//    }

    private static String getStringList(ArrayList<IdNamePair> list) {

        String strList = "";

        //We want to show at least 3 actors, but some movies have less
        int numList = NUM_CAST_DISPLAY;
        if(list.size() < NUM_CAST_DISPLAY)
            numList = list.size();

        //Set up display string for actors. Just display the first 3 top billed
        for(int i = 0; i < numList; i++) {
            strList += list.get(i).getName() + ", ";
        }

        if(list.size() > 0)
            strList = strList.substring(0,strList.length() - 2);

        return strList;
    }
    /**
     * Create clickable links on the names of the cast. When clicked, it will go to that Person's
     * detail page activity.
     * @param passContext context of calling activity
     * @param passMovie   movie/tv object
     * @param textView    TextView to set the list of links
     * @param entType     Entertainment type; MovieOld or TV Show.
     */
    public static void setCrewLinks(Context passContext, ArrayList<IdNamePair> passList, TextView textView, String header) {

        //Since this is a static method, need to create local versions of variables.
       // final Movie movie = passMovie;
        final ArrayList<IdNamePair> list = passList;
        final Context context = passContext;

        Log.e(TAG,"setCrewLinks with list size = " + list.size());
        //If there are no cast members, don't show anything in the textview
        if(list != null) {
            //If there are more than 3 actors, add the "More" link to get the rest of the cast
            Spanned cast;
            if (list.size() > 3) {
                cast = Html.fromHtml("<b>" + header + "</b>" + " " +
                        getStringList(list) + ", " + context.getString(com.fourthwardmobile.android.movingpictures.R.string.more));
            } else {
                cast = Html.fromHtml("<b>" + header + "</b>" + " " +
                        getStringList(list));
            }

            //Get spannable string that links will be set to.
            SpannableString castSS = new SpannableString(cast);

            //Only going to display 3 persons. If there are less than 3 persons in the list
            //get the number in the list.
            int numOfPersons = 3;
            if (list.size() < 3)
                numOfPersons = list.size();

            int spanStart = header.length() +1 ;
            for (int i = 0; i < numOfPersons; i++) {
                ClickableSpan span = createClickablSpan(context, list.get(i).getId());

                int spanEnd = spanStart + list.get(i).getName().length();

                castSS.setSpan(span, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanStart = spanEnd + 2;
            }

            //More link to the full list of cast members
            ClickableSpan spanMore = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //showCastListActivity(context,list,entType);
                }
            };

            if (list.size() > 3) {
                int span4End = spanStart + context.getString(com.fourthwardmobile.android.movingpictures.R.string.more).length();
                castSS.setSpan(spanMore, spanStart, span4End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            Log.e(TAG,"setCrewList() Setting text view to "+ castSS);
            textView.setText(castSS);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /**
     * Take and ArrayList of stings and combine them separated by a comma
     * @param list ArrayList of strings
     * @return     comma separated list of strings
     */
    public static String buildListString(ArrayList<String> list) {

        String str = "";
        //Set up display string of list
        for(int i = 0; i< list.size(); i++) {
            str += list.get(i) + ", ";
        }

        if(list.size() > 0)
            str = str.substring(0,str.length() - 2);

        return str;
    }

    public static String buildGenreString(ArrayList<Genre> list) {

        String str = "";
        //Set up display string of list
        for(int i = 0; i< list.size(); i++) {
            str += list.get(i).getName() + ", ";
        }

        if(list.size() > 0)
            str = str.substring(0,str.length() - 2);

        return str;
    }

    /**
     * Take and ArrayList of IdNamePair and combine them separated by a comma
     * @param list ArrayList of strings
     * @return     comma separated list of strings
     */
    public static String buildPersonListString(ArrayList<IdNamePair> list) {

        String str = "";
        //Set up display string of list
        for(int i = 0; i< list.size(); i++) {
            str += list.get(i).getName() + ", ";
        }

        if(list.size() > 0)
            str = str.substring(0,str.length() - 2);

        return str;
    }

    /**
     * Detect is there is a network connection
     * @param context Context testing connectivity
     * @return        Return status of network connectivity
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static void loadPosterThumbnail(Context context, String uri, final ImageView imageView, @EntertainmentType int entType) {

        int thumbnail = R.drawable.movie_poster_thumbnail;

        if(entType == ENT_TYPE_MOVIE)
            thumbnail = R.drawable.movie_poster_thumbnail;
        else if(entType == ENT_TYPE_TV)
            thumbnail = R.drawable.movie_poster_thumbnail;
        else if(entType == ENT_TYPE_PERSON)
            thumbnail = R.drawable.person_thumbnail;

        Picasso.with(context).load(MovieDbAPI.getFullPosterPath(uri))
                .placeholder(thumbnail)
                .into(imageView);
    }

    public static String formatYearFromDate(String releaseDate) {

        if((releaseDate != null) && (releaseDate != "") && (releaseDate != "null")) {
            String dateArray[] = releaseDate.split("-");
            return "(" + dateArray[0] + ")";
        }
        else
            return "(????)";
    }
    /**
     * Create SharedIntent to share a video from movie
     * @return
     */
//    public static Intent createShareVideoIntent(Activity activity, Movie movie) {
//
//        Video video = movie.getVideos().get(0);
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        shareIntent.setType("text/plain");
//
//        String strSubject = activity.getString(R.string.share_movie_subject);
//        if(movie instanceof TvShowOld)
//            strSubject = activity.getString(R.string.share_tvshow_subject);
//
//        String subject = video.getType() + " " + strSubject +
//                " " + movie.getTitle();
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, (MovieDbAPI.buildYoutubeUri(video)).toString());
//
//        return shareIntent;
//    }

    public static void addToFavoritesDb(Context context, ContentValues contentValues) {
        Uri inserted = context.getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, contentValues);
    }

    public static void removeFromFavoritesDb(Context context, int id) {

        //Put togeter SQL selection
        String selection = FavoritesContract.FavoritesEntry.COLUMN_ID + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        //Remove movie data from the content provider
        int deletedRow = context.getContentResolver().delete(FavoritesContract.FavoritesEntry.CONTENT_URI, selection, selectionArgs);
    }

    private static boolean checkIfInFavoritesDb(Context context, int id)  {

        //Get projection with MovieOld ID
        String[] projection =
                {
                        FavoritesContract.FavoritesEntry.COLUMN_ID
                };

        //Put together SQL selection
        String selection = FavoritesContract.FavoritesEntry.COLUMN_ID + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(id);

        //Return cursor to the row that contains the movie
        Cursor cursor = context.getContentResolver().query(
                FavoritesContract.FavoritesEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        if (cursor != null) {

            //If the cursor is empty, than the movie is not in the DB; else it is in the DB
            if (cursor.getCount() < 1)
                return false;
            else
                return true;
        }

        //Something went wrong. Just return false.
        return false;
    }
    public static void setFavoritesButton(FloatingActionButton fab, Context context, int id) {

        if (Util.checkIfInFavoritesDb(context,id)) {
            Log.e(TAG, "Already favorite, set tag to true");
            fab.setTag(true);
            fab.setColorFilter(context.getResources().getColor(com.fourthwardmobile.android.movingpictures.R.color.yellow));
        } else {

            Log.e(TAG, "Not favorite, set tag to false");
            fab.setTag(false);
            fab.setColorFilter(context.getResources().getColor(com.fourthwardmobile.android.movingpictures.R.color.white));
        }
    }

    public static @EntertainmentType int convertStringMediaTypeToEnt(String mediaType) {

        if(mediaType.equals(MEDIA_TYPE_MOVIE))
            return ENT_TYPE_MOVIE;
        else if(mediaType.equals(MEDIA_TYPE_TV))
            return ENT_TYPE_TV;
        else
            return ENT_TYPE_PERSON;


    }

    public static void shareMedia(Context context, @EntertainmentType int entType, int id, String subject) {

        String path = MovieDbAPI.MOVIE_DB_HTTP_URL;
        if(entType == ENT_TYPE_MOVIE)
            path += MovieDbAPI.PATH_MOVIE + "/";
        else if(entType == ENT_TYPE_TV)
            path += MovieDbAPI.PATH_TV + "/";
        else if(entType == ENT_TYPE_PERSON)
            path += MovieDbAPI.PATH_PERSON + "/";

            path +=  String.valueOf(id);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            shareIntent.putExtra(Intent.EXTRA_TEXT, path);
            context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.share_link)));

    }

    public static String encodeEmail(String email) {

        return email.replace(".",",");
    }

    public static String decodeEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

//    public static void setSearchMenu(Menu menu, MenuInflater inflater, final FragmentActivity activity,
//                                     final Toolbar toolbar, final int primaryColor, final int darkPrimaryColor) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.menu_search, menu);
//
//        //Get the SearchView and set teh searchable configuration
//        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
//        final MenuItem searchMenu = (MenuItem) menu.findItem(R.id.action_search_db);
//        final SearchView searchView = (SearchView) searchMenu.getActionView();
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(
//                new ComponentName(activity, SearchableActivity.class)));
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//

//                //Close searchView after search button clicked
//                searchView.setQuery("", false);
//                searchView.setIconified(true);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//            @Override
//            public boolean onSuggestionSelect(int position) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onSuggestionClick(int position) {
//                Log.e(TAG, "onSuggestionClick");
//                searchView.setQuery("", false);
//                searchView.setIconified(true);
//                return false;
//            }
//        });
//
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e(TAG, "onClick");
//                toolbar.setBackgroundColor(primaryColor);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    activity.getWindow().setStatusBarColor(darkPrimaryColor);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Window window = activity.getWindow();
//
//                        // clear FLAG_TRANSLUCENT_STATUS flag:
//                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//                        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//                        window.setStatusBarColor(darkPrimaryColor);
//                    }
//                }
//            }
//        });
//
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Log.e(TAG, "onClose()");
//                toolbar.getBackground().setAlpha(0);
//                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                return false;
//            }
//        });
//
//    }

//    public void setStatusBarColor(int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getActivity().getWindow();
//
//// clear FLAG_TRANSLUCENT_STATUS flag:
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            window.setStatusBarColor(color);
//        }
//    }


}
