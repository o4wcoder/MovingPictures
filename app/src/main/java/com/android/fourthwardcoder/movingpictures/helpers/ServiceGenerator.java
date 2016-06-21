package com.android.fourthwardcoder.movingpictures.helpers;

import android.util.Log;

import com.android.fourthwardcoder.movingpictures.interfaces.MovieService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chris Hare on 6/15/2016.
 */
public class ServiceGenerator {

    private static final String TAG = ServiceGenerator.class.getSimpleName();

    private static Retrofit mRetrofit;

    public static Retrofit retrofit() {
        return mRetrofit;
    }

    public static MovieService getMovieApiService() {

        //Intercept the OkHttp Call from Retrofit and append the API Key to ever request
        OkHttpClient httpClient =  new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(MovieDbAPI.PARAM_API_KEY,MovieDbAPI.API_KEY_MOVIE_DB)
                        .build();

                Log.e(TAG,"URL with API_KEY = " + url);
                //Append the API key to the original URL
                Request.Builder requestBuilder = original.newBuilder().url(url).method(original.method(),
                        original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(MovieDbAPI.MOVIE_DB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        return mRetrofit.create(MovieService.class);
    }
}
