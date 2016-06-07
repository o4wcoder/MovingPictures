package com.android.fourthwardcoder.movingpictures.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.helpers.Util;

import java.util.Stack;

/**
 * Class PersonDetailActivity
 * Author: Chris Hare
 * Created: 8/26/2015
 * <p/>
 * Activity to show the details of a particular movie
 */
public class PersonDetailActivity extends AppCompatActivity {

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

        //Add parent that called Movie Activity to stack
        parents.push(getClass());

        setContentView(R.layout.activity_actor_detail);
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        }

        return super.onOptionsItemSelected(item);
    }
}