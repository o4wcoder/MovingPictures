package com.fourthwardmobile.android.movingpictures.fragments;


import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.activities.PersonDetailActivity;
import com.fourthwardmobile.android.movingpictures.activities.SearchableActivity;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseDetailFragment extends Fragment implements Constants {

    /********************************************************************************************/
    /*                                    Constants                                             */
    /********************************************************************************************/
    private static final String TAG = BaseDetailFragment.class.getSimpleName();

    /********************************************************************************************/
    /*                                    Local Data                                            */
    /********************************************************************************************/
    protected Toolbar mToolbar;
    protected int mPrimaryColor;
    protected int mDarkPrimaryColor;

    protected ImageView mBackdropImageView;
    protected CollapsingToolbarLayout mCollapsingToolbarLayout;
    protected FloatingActionButton mFavoritesFAB;
    protected RelativeLayout mDetailLayout;
    protected CardView mDetailCardView;

    @EntertainmentType int mEntType;

    public BaseDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

       Log.e(TAG,"onCreateOptoinsMenu()");

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_search, menu);


        //Get the SearchView and set teh searchable configuration
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenu = (MenuItem) menu.findItem(R.id.action_search_db);
        final SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(getActivity(), SearchableActivity.class)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Close searchView after search button clicked
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {

                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
               // Log.e(TAG, "onSuggestionClick");
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.e(TAG, "onClick");
                mToolbar.setBackgroundColor(mPrimaryColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setStatusBarColor(mDarkPrimaryColor);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getActivity().getWindow();

                        // clear FLAG_TRANSLUCENT_STATUS flag:
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                        window.setStatusBarColor(mDarkPrimaryColor);
                    }
                }
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.e(TAG, "onClose()");
                mToolbar.getBackground().setAlpha(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                return false;
            }
        });

        //Finally inflate the share menu
        inflater.inflate(R.menu.menu_share,menu);

    }


    protected void setPaletteColors(final String title) {

        Bitmap bitmap = ((BitmapDrawable) mBackdropImageView.getDrawable()).getBitmap();

        if (bitmap != null && getActivity() != null) {

            Log.e(TAG, "Bitmap width =  " + bitmap.getWidth() + ", height = " + bitmap.getHeight() + ", density = " + bitmap.getDensity());
            Palette.PaletteAsyncListener paletteAsyncListener = new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette p) {
                    List<Palette.Swatch> swatches = p.getSwatches();
                    for (int i = 0; i < swatches.size(); i++) {
                        Log.e(TAG, "Swatch population = " + swatches.get(i).getPopulation());
                    }
                    //Set title and colors for collapsing toolbar
                    mCollapsingToolbarLayout.setTitle(title);
                    mCollapsingToolbarLayout.setExpandedTitleColor(

                            getResources().getColor(android.R.color.transparent)

                    );

                    //Set content descriptioni for toolbar/title
                    mCollapsingToolbarLayout.setContentDescription(title);

                    //Set pallet colors when toolbar is collapsed
                    int primaryColor = getResources().getColor(R.color.appPrimaryColor);
                    int primaryDarkColor = getResources().getColor(R.color.appDarkPrimaryColor);
                    int accentColor = getResources().getColor(R.color.appAccentColor);

                    mPrimaryColor = p.getMutedColor(primaryColor);
                    mDarkPrimaryColor = p.getDarkMutedColor(primaryDarkColor);
                    mCollapsingToolbarLayout.setContentScrimColor(p.getMutedColor(primaryColor));
                    mCollapsingToolbarLayout.setStatusBarScrimColor(p.getDarkMutedColor(primaryDarkColor));

                    if (!(

                            getActivity()

                                    instanceof PersonDetailActivity))

                    {
                        mDetailLayout.setBackgroundColor(p.getMutedColor(primaryColor));
                        mDetailCardView.setCardBackgroundColor(p.getMutedColor(primaryColor));
                    }

                    mFavoritesFAB.setBackgroundTintList(ColorStateList.valueOf(p.getVibrantColor(accentColor)));
                }
            };


            if (bitmap != null && !bitmap.isRecycled()) {
                Palette.from(bitmap).generate(paletteAsyncListener);
            }
        }
    }

    protected void startPostponedEnterTransition(final ImageView imageView) {
        //Log.e(TAG,"startPostponedEnterTransition() Inside");
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //  Log.e(TAG,"onPreDraw(): Start postponed enter transition!!!!");
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                //Must call this inside a PreDrawListener or the Enter Transition will not work
                //Need to make sure imageview is ready before starting transition.
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

}
