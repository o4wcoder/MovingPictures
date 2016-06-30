package com.android.fourthwardcoder.movingpictures.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.fragments.PersonSinglePhotoFragment;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Profile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Class PersonPhotoPageActivityActivity
 * Author: Chris Hare
 * Created: 8/26/2015
 *
 * Activity to show the GridView of photos of a person.
 */
public class PersonPhotoPagerActivity extends AppCompatActivity implements Constants, PersonSinglePhotoFragment.OnPhotoClick {

    /**********************************************************************/
    /*                             Constants                              */
    /**********************************************************************/
    private static final String TAG = PersonPhotoPagerActivity.class.getSimpleName();

    /**********************************************************************/
    /*                             Local Data                             */
    /**********************************************************************/
    ViewPager mPager;
   // ArrayList<PersonPhoto> mPhotoList;
    ArrayList<Profile> mProfileList;
    FrameLayout mBottomPanel;
    Toolbar mToolbar;
    boolean mIsToolbarVisible = true;
    Button mShareButton;
    int mStartingPosition;
    String mPersonName;

    //Argument for saving state of toolbars
    private final String ARG_TOOLBAR_VISIBLE = "arg_toolbar_visible";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_pager);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPager = (ViewPager)findViewById(R.id.pager);
        mBottomPanel = (FrameLayout)findViewById(R.id.person_photo_bottom_panel);


        if(savedInstanceState != null) {

            mIsToolbarVisible = savedInstanceState.getBoolean(ARG_TOOLBAR_VISIBLE);
            setToolbarVisibility();
        }
        //Change status bar color
      //  Util.setStatusBarColor(this);

        mProfileList = getIntent().getParcelableArrayListExtra(EXTRA_PERSON_PHOTO);
        mPersonName = getIntent().getStringExtra(EXTRA_PERSON_NAME);
        mStartingPosition = getIntent().getIntExtra(EXTRA_PERSON_PHOTO_POSITION,0);

        setTitle(mPersonName);

        //Set up Share button
        mShareButton = (Button)findViewById(R.id.person_photo_share_button);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"onClick() Share Button with current item = " +  mPager.getCurrentItem());

                final Profile profile = mProfileList.get(mPager.getCurrentItem());
                Picasso.with(getApplicationContext()).load(MovieDbAPI.getFullPosterPath(profile.getFilePath())).into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        if(bitmap != null) {
                            Log.e(TAG, "Got bitmap! with byte size = " + bitmap.getHeight() + " x " + bitmap.getWidth());
                            shareBitmap(bitmap);
                        }
                        else
                            Log.e(TAG,"Bitmap was null");
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


            }
        });

        //Get Activities instance of the Fragment Manager
        FragmentManager fm = getSupportFragmentManager();

        mPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Profile profile = mProfileList.get(position);

                return PersonSinglePhotoFragment.newInstance(MovieDbAPI.getFullPosterPath(profile.getFilePath()));
            }

            @Override
            public int getCount() {
                return mProfileList.size();
            }
        });

        //Start pager on the position of the photo selected so we just don't start on the first one
        mPager.setCurrentItem(mStartingPosition);




    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean(ARG_TOOLBAR_VISIBLE,mIsToolbarVisible);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setToolbarVisibility() {

        if(mIsToolbarVisible) {
            mToolbar.setVisibility(View.VISIBLE);
            mBottomPanel.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.INVISIBLE);
            mBottomPanel.setVisibility(View.INVISIBLE);
        }

    }
    @Override
    public void onSetBottomPanelVisibility() {

        if(mIsToolbarVisible) {
            Animation topAnimHide = AnimationUtils.loadAnimation(this,R.anim.slide_down_from_top);
            Animation bottomAnimHide = AnimationUtils.loadAnimation(this, R.anim.slide_down_from_bottom);
            mToolbar.startAnimation(topAnimHide);
            mBottomPanel.startAnimation(bottomAnimHide);

            //Set the toolbars visibility and switch flag.
            mIsToolbarVisible = false;
            setToolbarVisibility();
        }
        else {
            Animation topAnimHide = AnimationUtils.loadAnimation(this,R.anim.slide_up_from_top);
            Animation animShow = AnimationUtils.loadAnimation(this, R.anim.slide_up_from_bottom);

            mToolbar.startAnimation(topAnimHide);
            mBottomPanel.startAnimation(animShow);

            //Set the toolbars visibility and switch flag.
            mIsToolbarVisible = true;
            setToolbarVisibility();
        }

    }

    private void shareBitmap(Bitmap bitmap) {

        File dir = new File(getExternalCacheDir(),getString(R.string.share_storage_directory));

        if(!dir.exists()) {

            if(dir.mkdirs())
                Log.e(TAG,"Success creating new directory");
            else
                Log.e(TAG,"Failed creating new directory");
        } else {
            Log.e(TAG,"Directory already exists");
        }


        String filename = mPersonName.replace(" ","_");
        File file = new File(dir,filename + ".png");

        FileOutputStream fOut;

        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,85,fOut);
            fOut.flush();
            fOut.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(intent,getString(R.string.share_title)));
    }

}
