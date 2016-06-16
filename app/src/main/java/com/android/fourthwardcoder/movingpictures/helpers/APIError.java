package com.android.fourthwardcoder.movingpictures.helpers;

/**
 * Created by Chris Hare on 6/15/2016.
 */
public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
