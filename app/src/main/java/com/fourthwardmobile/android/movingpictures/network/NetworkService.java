package com.fourthwardmobile.android.movingpictures.network;

import android.util.Log;

import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.fourthwardmobile.android.movingpictures.interfaces.MovieService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chris Hare on 8/28/2016.
 */
public class NetworkService {

    /*******************************************************************************************/
    /*                                    Constants                                            */
    /*******************************************************************************************/
    private static final String TAG = NetworkService.class.getSimpleName();

    private MovieService mMovieApiService;

    public NetworkService() {

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieDbAPI.MOVIE_DB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        mMovieApiService = retrofit.create(MovieService.class);

    }

    public MovieService getMovieApiService() {
        return mMovieApiService;

    }
}
