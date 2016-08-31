package com.fourthwardmobile.android.movingpictures;

import android.app.Application;

import android.content.Context;

import com.fourthwardmobile.android.movingpictures.network.NetworkService;

import com.firebase.client.Firebase;
        import com.firebase.client.Logger;
/**
 * Created by Chris Hare on 8/28/2016.
 */
public class MovingPicturesApplication extends Application {

    private NetworkService mNetworkService;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetworkService = new NetworkService();

        //Initialize Firebase
        Firebase.setAndroidContext(this);

        //Set Firebase log messages
        Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
        /* Enable disk persistence  */
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

    }

    public NetworkService getNetworkService() {

        return mNetworkService;
    }

}
