package com.android.fourthwardcoder.movingpictures.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.fragments.TvDetailFragment;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;

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


        setContentView(R.layout.activity_detail);

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
                .add(R.id.detail_container, fragment)
                .commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
