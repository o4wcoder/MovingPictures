package com.android.fourthwardcoder.movingpictures.helpers;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Chris Hare on 6/15/2016.
 */
public class ErrorUtils {

    public static APIError parseError(Response<?> response) {

        Converter<ResponseBody,APIError> converter = ServiceGenerator.retrofit()
                .responseBodyConverter(APIError.class,
                        new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
