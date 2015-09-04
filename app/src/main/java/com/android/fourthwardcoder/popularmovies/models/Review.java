package com.android.fourthwardcoder.popularmovies.models;

/**
 * Created by chare on 8/25/2015.
 */
public class Review {

    String author;
    String content;

    public Review() {

    }

    @Override
    public String toString() {

        return "REVIEW:" + "\n" +
                author + "\n" +
                content + "\n";
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
