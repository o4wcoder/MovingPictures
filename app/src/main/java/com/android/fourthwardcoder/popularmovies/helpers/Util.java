package com.android.fourthwardcoder.popularmovies.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.activities.CastListActivity;
import com.android.fourthwardcoder.popularmovies.activities.PersonDetailActivity;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Movie;
import com.android.fourthwardcoder.popularmovies.models.TvShow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.WindowManager.*;

/**
 * Created by Chris Hare on 9/25/2015.
 */
public class Util implements Constants {

    /************************************************************************/
    /*                           Constants                                  */
    /************************************************************************/
    private static final String TAG = Util.class.getSimpleName();

    public static void setStatusBarColor(Activity activity) {

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.appDarkPrimaryColor));

        }
    }

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

    public static String getDateHistory(TvShow tvShow){

        String[] yearStart = tvShow.getReleaseDate().split("-");
        String startYear = yearStart[0];

        String[] yearEnd = tvShow.getLastAirDate().split("-");
        String endYear = yearEnd[0];

        Log.e(TAG,"Tv status is " + tvShow.getStatus());
        if(tvShow.getStatus().equals(MovieDbAPI.STATUS_ENDED))
            return "(" + startYear + " - " + endYear + ")";
        else
            return "(" + startYear + " - ";
    }

    private static void startActorDetailActivity(Context context, int id) {

        Intent i = new Intent(context,PersonDetailActivity.class);
        i.putExtra(EXTRA_PERSON_ID,id);
        context.startActivity(i);
    }

    public static void setCastLinks(Context passContext, Movie passMovie, TextView textView, final int entType) {

        final Movie movie = passMovie;
        final Context context = passContext;
        Spanned cast = Html.fromHtml("<b>" + context.getString(R.string.cast) + "</b>" + " " +
                movie.getActorsString() + ", " + context.getString(R.string.more));

        SpannableString castSS = new SpannableString(cast);

        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                startActorDetailActivity(context,movie.getActorIds().get(0));
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                startActorDetailActivity(context,movie.getActorIds().get(1));
            }
        };

        ClickableSpan span3 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                startActorDetailActivity(context,movie.getActorIds().get(2));
            }
        };

        //More test to the full list of cast members
        ClickableSpan span4 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent i = new Intent(context, CastListActivity.class);
                i.putExtra(EXTRA_MOVIE_ID, movie.getId());
                if(entType == TYPE_MOVIE)
                   i.putExtra(EXTRA_ENT_TYPE,TYPE_MOVIE);
                else
                   i.putExtra(EXTRA_ENT_TYPE,TYPE_TV);
                context.startActivity(i);
            }
        };

        int span1Start = 6;

        int span1End = span1Start + movie.getActorNames().get(0).length();
        int span2Start = span1End + 2;
        int span2End = span2Start + movie.getActorNames().get(1).length();
        int span3Start = span2End + 2;
        int span3End = span3Start + movie.getActorNames().get(2).length();
        int span4Start = span3End +2;
        int span4End = span4Start + context.getString(R.string.more).length();

        castSS.setSpan(span1, span1Start, span1End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        castSS.setSpan(span2, span2Start,span2End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        castSS.setSpan(span3, span3Start,span3End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        castSS.setSpan(span4, span4Start,span4End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(castSS);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

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
}
