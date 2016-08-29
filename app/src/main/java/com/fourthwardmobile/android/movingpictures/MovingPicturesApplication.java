package com.fourthwardmobile.android.movingpictures;

import android.app.Application;

import com.fourthwardmobile.android.movingpictures.network.NetworkService;

/**
 * Created by Chris Hare on 8/28/2016.
 */
public class MovingPicturesApplication extends Application {

    private NetworkService mNetworkService;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetworkService = new NetworkService();

    }

    public NetworkService getNetworkService() {

        return mNetworkService;
    }
}
