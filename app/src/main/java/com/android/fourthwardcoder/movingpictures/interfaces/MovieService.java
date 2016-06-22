package com.android.fourthwardcoder.movingpictures.interfaces;

import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.models.Credits;
import com.android.fourthwardcoder.movingpictures.models.Movie;
import com.android.fourthwardcoder.movingpictures.models.MovieList;
import com.android.fourthwardcoder.movingpictures.models.Person;
import com.android.fourthwardcoder.movingpictures.models.PersonOld;
import com.android.fourthwardcoder.movingpictures.models.TvShow;
import com.android.fourthwardcoder.movingpictures.models.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Chris Hare on 6/9/2016.
 */
public interface MovieService {

    @GET("/3/movie/{sort_type}")
    Call<MovieList> getMovieList(@Path("sort_type") String sortType,
                                 @Query(MovieDbAPI.PARAM_SORT) String sortOrder);
    @GET("/3/movie/{id}?append_to_response=release_dates,credits,videos,reviews")
    Call<Movie> getMovie(@Path("id") int id);

    @GET("3/movie/{id}/videos")
    Call<VideoList> getVideoList(@Path("id") int id);

    @GET("3/movie/{id}/credits")
    Call<Credits> getCredits(@Path("id") int id);

    @GET("3/person/{id}")
    Call<Person> getPerson(@Path("id") int id);

    @GET("3/discover/movie?sort_by=popularity.desc")
    Call<MovieList> getPersonsTopMovies(@Query("with_cast") int id);

    @GET("3/person/{id}/{credit_type}")
    Call<Credits> getPersonsFilmography(@Path("id") int id,@Path("credit_type") String creditType);

    @GET("3/tv/{id}?append_to_response=credits,videos")
    Call<TvShow> getTvShow(@Path("id") int id);


}
