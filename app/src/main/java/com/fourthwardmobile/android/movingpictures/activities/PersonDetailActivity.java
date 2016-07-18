package com.fourthwardmobile.android.movingpictures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.fragments.AboutFragment;
import com.fourthwardmobile.android.movingpictures.fragments.PersonDetailFragment;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;

import java.util.Stack;

/**
 * Class PersonDetailActivity
 * Author: Chris Hare
 * Created: 8/26/2015
 * <p/>
 * Activity to show the details of a particular movie
 */
public class PersonDetailActivity extends AppCompatActivity implements Constants{

    /**********************************************************************/
    /*                         Local Data                                 */
    /**********************************************************************/
    //Stack to hold parent of activiy to aid in Up navigation to different parents
    public static Stack<Class<?>> parents = new Stack<Class<?>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change status bar color
       // Util.setStatusBarColor(this);

        //Start postpone element transition
        supportPostponeEnterTransition();

        //Add parent that called MovieOld Activity to stack
        parents.push(getClass());

        setContentView(R.layout.activity_single_fragment);
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //If we are being restored from a previous state, then don't recreate the fragment
        //or it will get built twice. Just return
        if(savedInstanceState != null)
            return;

        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        PersonDetailFragment fragment = PersonDetailFragment.newInstance(id);
        //fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.detail_container, fragment)
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

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                //When navigating back, get parent that called this activity.
                //Start parent activity.
                Intent parentActivityIntent = new Intent(this, parents.pop());
                parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(parentActivityIntent);
                //Kill this activity
                finish();
                return true;
            case R.id.action_about:
                FragmentManager fm = getSupportFragmentManager();
                AboutFragment aboutFragment = new AboutFragment();
                aboutFragment.show(fm,"About");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
