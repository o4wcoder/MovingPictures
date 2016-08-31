package com.fourthwardmobile.android.movingpictures;

import android.app.Application;
import android.content.Context;

import com.fourthwardmobile.android.movingpictures.network.NetworkService;

/**
 * Created by Chris Hare on 8/28/2016.
 */
public class MovingPicturesApplication extends Application {

    private NetworkService mNetworkService;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        this.mContext = getContext();
        mNetworkService = new NetworkService();

    }

    public NetworkService getNetworkService() {

        return mNetworkService;
    }

    public static Context getContext(){
        return mContext;
    }
}
