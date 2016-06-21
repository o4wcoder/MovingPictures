package com.android.fourthwardcoder.movingpictures.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.activities.CastListActivity;
import com.android.fourthwardcoder.movingpictures.activities.MovieDetailActivity;
import com.android.fourthwardcoder.movingpictures.activities.PersonDetailActivity;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Genre;
import com.android.fourthwardcoder.movingpictures.models.IdNamePair;
import com.android.fourthwardcoder.movingpictures.models.Movie;
import com.android.fourthwardcoder.movingpictures.models.MovieOld;
import com.android.fourthwardcoder.movingpictures.models.TvShow;
import com.android.fourthwardcoder.movingpictures.models.Video;
import com.android.fourthwardcoder.movingpictures.models.VideoOld;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
            window.setStatusBarColor(activity.getResources().getColor(R.color.appDarkPrimaryColor));

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
     * Start PersonOld Detail Activity of the cast memmber
     * @param context context of calling activity
     * @param id      id of person
     */
    public static void startActorDetailActivity(Context context, int id, ImageView imageView) {

        Intent i = new Intent(context,PersonDetailActivity.class);
        i.putExtra(EXTRA_PERSON_ID,id);
        Log.e(TAG,"startActorDetailActivity()");
        if(imageView != null) {
            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                            new Pair<View, String>(imageView, context.getString(R.string.trans_person_poster)));

            context.startActivity(i, activityOptions.toBundle());
        }
        else {
            context.startActivity(i);
        }
    }

    public static void startMovieDetailActivity(Context context, int id, ImageView imageView) {

        Intent i = new Intent(context,MovieDetailActivity.class);
        i.putExtra(EXTRA_MOVIE_ID,id);
        Log.e(TAG,"startMovieDetailActivity()");
        if(imageView != null) {
            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                            new Pair<View, String>(imageView, context.getString(R.string.trans_movie_poster)));

            context.startActivity(i, activityOptions.toBundle());
        }
        else {
            context.startActivity(i);
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

               startActorDetailActivity(context,id,null);
            }
        };

        return span;

    }

    public static void showCastListActivity(Context context, Movie movie, final int entType) {

        Intent i = new Intent(context, CastListActivity.class);
        i.putExtra(EXTRA_MOVIE_ID, movie.getId());
        i.putExtra(EXTRA_TITLE, movie.getTitle());
        if (entType == ENT_TYPE_MOVIE)
            i.putExtra(EXTRA_ENT_TYPE, ENT_TYPE_MOVIE);
        else
            i.putExtra(EXTRA_ENT_TYPE, ENT_TYPE_TV);
        context.startActivity(i);
    }

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
     * Create clickable links on the names of the cast. When clicked, it will go to that PersonOld's
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
                        getStringList(list) + ", " + context.getString(R.string.more));
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
                int span4End = spanStart + context.getString(R.string.more).length();
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



    public static void loadPosterThumbnail(Context context, String uri, final ImageView imageView) {

        Picasso.with(context).load(MovieDbAPI.getFullPosterPath(uri)).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.e(TAG,"getCastThumbnail() onSuccess()");
            }

            @Override
            public void onError() {
                Log.e(TAG,"getCastThumbnail() onError()");
                imageView.setImageResource(R.drawable.person_no_pic_thumnail);
            }
        });
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
//        if(movie instanceof TvShow)
//            strSubject = activity.getString(R.string.share_tvshow_subject);
//
//        String subject = video.getType() + " " + strSubject +
//                " " + movie.getTitle();
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, (MovieDbAPI.buildYoutubeUri(video)).toString());
//
//        return shareIntent;
//    }
}
