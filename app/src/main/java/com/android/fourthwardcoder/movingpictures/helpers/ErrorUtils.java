package com.android.fourthwardcoder.movingpictures.helpers;

import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Chris Hare on 6/15/2016.
 */
public class ErrorUtils {

    private static final String TAG = ErrorUtils.class.getSimpleName();

    public static APIError parseError(Response<?> response) {


        Converter<ResponseBody,APIError> converter = ServiceGenerator.retrofit()
                .responseBodyConverter(APIError.class,
                        new Annotation[0]);

        APIError error;

        try {
            if(ServiceGenerator.retrofit() == null)
                Log.e(TAG,"APIError retrofit instance is null");
            else
                Log.e(TAG,"APIError retrofit instance is not null");

          //  Log.e(TAG,"APIError response message body = " + response.errorBody().string());

            error = converter.convert(response.errorBody());

        } catch (IOException e) {
            Log.e(TAG,"APIError convert failed " + e.getMessage());
            return new APIError();
        }
        Log.e(TAG,"APIError convert ok with error message = " + error.message());
        return error;
    }
}
