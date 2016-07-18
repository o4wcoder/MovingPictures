package com.fourthwardmobile.android.movingpictures.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.fragments.AboutFragment;
import com.fourthwardmobile.android.movingpictures.fragments.TvDetailFragment;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;

/**
 * Class: TvDetailActivity
 * Author: Chris Hare
 * Created: 9/25/2015
 * <p/>
 * Activity for the TV Show Details.
 */
public class TvDetailActivity extends AppCompatActivity implements Constants {

    /**********************************************************************/
    /*                          Constants                                 */
    /**********************************************************************/
    private final static String TAG = TvDetailActivity.class.getSimpleName();

   // int mPersonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(com.fourthwardmobile.android.movingpictures.R.layout.activity_single_fragment);

        //Start postpone element transition
        supportPostponeEnterTransition();

        //If we are being restored from a previous state, then don't recreate the fragment
        //or it will get built twice. Just return
        if(savedInstanceState != null)
            return;

        int id = getIntent().getIntExtra(EXTRA_ID,0);
        TvDetailFragment fragment = TvDetailFragment.newInstance(id);
        //fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(com.fourthwardmobile.android.movingpictures.R.id.detail_container, fragment)
                .commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflate menu for main activity toolbar
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {

            case R.id.action_about:
                FragmentManager fm = getSupportFragmentManager();
                AboutFragment aboutFragment = new AboutFragment();
                aboutFragment.show(fm,"About");
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
