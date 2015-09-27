package com.android.fourthwardcoder.popularmovies.models;

/**
 * Class Review
 * Author: Chris Hare
 * Created: 8/25/2015.
 *
 * Class to hold a movie review
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
