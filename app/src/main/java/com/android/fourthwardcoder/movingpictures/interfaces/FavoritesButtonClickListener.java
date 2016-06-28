//package com.android.fourthwardcoder.movingpictures.interfaces;
//
//import android.content.Context;
//import android.support.design.widget.FloatingActionButton;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.android.fourthwardcoder.movingpictures.R;
//import com.android.fourthwardcoder.movingpictures.helpers.Util;
//import com.android.fourthwardcoder.movingpictures.models.Movie;
//
///**
// * Created by Chris Hare on 6/27/2016.
// */
//public class FavoritesButtonClickListener implements View.OnClickListener {
//
//    private Context mContext;
//    private FloatingActionButton mFab;
//    private int mId;
//    private String mTitle;
//
//    private Object mPassObject;
//
//    public FavoritesButtonClickListener(Context context, Object passObject, FloatingActionButton floatingActionButton, int id, String title) {
//
//        this.mContext = context;
//        this.mPassObject = passObject;
//        this.mFab = floatingActionButton;
//        this.mId = id;
//        this.mTitle = title;
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        String toastStr = "";
//        if (v.getTag().equals(false)) {
//           // Log.e(TAG, "Set to favorite");
//            mFab.setTag(true);
//            mFab.setColorFilter(mContext.getResources().getColor(R.color.yellow));
//            toastStr = mContext.getString(R.string.added) + " " + mTitle + " "
//                    + mContext.getString(R.string.to_favorites);
//          //  Util.addToFavoritesDb(mContext,);
//
//        } else {
//            //Log.e(TAG, "remove from favorite");
//            mFab.setTag(false);
//            mFab.setColorFilter(mContext.getResources().getColor(R.color.white));
//            toastStr = mContext.getString(R.string.removed) + " " + mMovie.getTitle() + " "
//                    + mContext.getString(R.string.from_favorites);
//            //removeMovieFromDb();
//        }
//        Toast toast = Toast.makeText(mContext.getApplicationContext(),
//                toastStr, Toast.LENGTH_SHORT);
//        toast.show();
//    }
//}
