package com.android.fourthwardcoder.movingpictures.interfaces;

import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.models.Movie;
import com.android.fourthwardcoder.movingpictures.models.MovieBasic;
import com.android.fourthwardcoder.movingpictures.models.MovieList;
import com.android.fourthwardcoder.movingpictures.models.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Chris Hare on 6/9/2016.
 */
public interface MovieService {

    @GET("/3/movie/{sort_type}/")
    Call<MovieList> getMovieList(@Path("sort_type") String sortType,
                                 @Query(MovieDbAPI.PARAM_SORT) String sortOrder);
    @GET("/3/movie/{id}")
    Call<Movie> getMovie(@Path("id") int id);

    @GET("3/movie/{id}/videos")
    Call<VideoList> getVideoList(@Path("id") int id);


}
