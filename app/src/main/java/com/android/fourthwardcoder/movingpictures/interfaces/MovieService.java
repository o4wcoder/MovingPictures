package com.android.fourthwardcoder.movingpictures.interfaces;

import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.models.Credits;
import com.android.fourthwardcoder.movingpictures.models.MediaList;
import com.android.fourthwardcoder.movingpictures.models.Movie;
import com.android.fourthwardcoder.movingpictures.models.Person;
import com.android.fourthwardcoder.movingpictures.models.PersonPhotoList;
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

//    @GET("/3/{ent_type}/{sort_type}")
  //  Call<MediaList> getMediaList(@Path("ent_type") String entType, @Path("sort_type") String sortType,
    //                             @Query(MovieDbAPI.PARAM_SORT) String sortOrder);

    @GET("/3/{ent_type}/{sort_type}")
    Call<MediaList> getMediaList(@Path("ent_type") String entType, @Path("sort_type") String sortType);

    @GET("/3/discover/movie")
    Call<MediaList> getNowPlayingMovies(@Query("primary_release_date.gte") String startDate,
                                        @Query("primary_release_date.lte") String endDate,
                                        @Query(MovieDbAPI.PARAM_SORT) String sortOrder);
//
//    @GET("3/movie/upcoming")
//    Call<MediaList> getUpcomingMovies();

    @GET("/3/movie/{id}?append_to_response=release_dates,credits,videos,reviews")
    Call<Movie> getMovie(@Path("id") int id);

    @GET("3/movie/{id}/videos")
    Call<VideoList> getVideoList(@Path("id") int id);

    @GET("3/{type}/{id}/credits")
    Call<Credits> getCredits(@Path("type") String type, @Path("id") int id);

    @GET("3/person/{id}")
    Call<Person> getPerson(@Path("id") int id);

    @GET("3/discover/movie?sort_by=popularity.desc")
    Call<MediaList> getPersonsTopMovies(@Query("with_cast") int id);

    @GET("3/person/{id}/{credit_type}")
    Call<Credits> getPersonsFilmography(@Path("id") int id,@Path("credit_type") String creditType);

    @GET("3/tv/{id}?append_to_response=credits,videos")
    Call<TvShow> getTvShow(@Path("id") int id);

    @GET("3/search/multi")
    Call<MediaList> getSearchResultList(@Query("query") String query);

    @GET("3/person/{id}/images")
    Call<PersonPhotoList> getPersonsPhotos(@Path("id") int id);




}
