package com.android.fourthwardcoder.movingpictures.helpers;

import retrofit2.Retrofit;

/**
 * Created by Chris Hare on 6/15/2016.
 */
public class ServiceGenerator {

    private static Retrofit retrofit;

    public static Retrofit retrofit() {
        return retrofit;
    }
}
