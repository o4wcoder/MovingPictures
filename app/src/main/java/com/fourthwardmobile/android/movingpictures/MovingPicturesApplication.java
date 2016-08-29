package com.fourthwardmobile.android.movingpictures;

import android.app.Application;

import com.firebase.client.Firebase;
import com.firebase.client.Logger;

/**
 * Created by Chris Hare on 8/20/2016.
 */
public class MovingPicturesApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        //Initialize Firebase
        Firebase.setAndroidContext(this);

        //Set Firebase log messages
        Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
        /* Enable disk persistence  */
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
