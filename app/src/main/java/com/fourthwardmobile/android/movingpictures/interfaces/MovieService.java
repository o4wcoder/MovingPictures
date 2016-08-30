package com.fourthwardmobile.android.movingpictures.interfaces;

import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.fourthwardmobile.android.movingpictures.models.Credits;
import com.fourthwardmobile.android.movingpictures.models.MediaList;
import com.fourthwardmobile.android.movingpictures.models.Movie;
import com.fourthwardmobile.android.movingpictures.models.Person;
import com.fourthwardmobile.android.movingpictures.models.PersonPhotoList;
import com.fourthwardmobile.android.movingpictures.models.TvShow;
import com.fourthwardmobile.android.movingpictures.models.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Chris Hare on 6/9/2016.
 */
public interface MovieService {

    //RxJava Calls
    @GET("/3/{ent_type}/{sort_type}")
    Observable<MediaList> getMediaList(@Path("ent_type") String entType, @Path("sort_type") String sortType);

    @GET("/3/movie/{id}?append_to_response=release_dates,credits,videos,reviews")
    Observable<Movie> getMovie(@Path("id") int id);

    @GET("3/tv/{id}?append_to_response=credits,videos,content_ratings")
    Observable<TvShow> getTvShow(@Path("id") int id);

    @GET("3/person/{id}")
    Observable<Person> getPerson(@Path("id") int id);

    @GET("3/{type}/{id}/credits")
    Call<Credits> getCredits(@Path("type") String type, @Path("id") int id);



    @GET("3/discover/movie?sort_by=popularity.desc")
    Call<MediaList> getPersonsTopMovies(@Query("with_cast") int id);

    @GET("3/person/{id}/{credit_type}")
    Call<Credits> getPersonsFilmography(@Path("id") int id,@Path("credit_type") String creditType);



    @GET("3/search/multi")
    Call<MediaList> getSearchResultList(@Query("query") String query);

    @GET("3/person/{id}/images")
    Call<PersonPhotoList> getPersonsPhotos(@Path("id") int id);




    // Unused Api Calls. Keep them in case we want to come back to them
//    @GET("/3/discover/movie")
//    Call<MediaList> getNowPlayingMovies(@Query("primary_release_date.gte") String startDate,
//                                        @Query("primary_release_date.lte") String endDate,
//                                        @Query(MovieDbAPI.PARAM_SORT) String sortOrder);
//
//    @GET("3/movie/upcoming")
//    Call<MediaList> getUpcomingMovies();
//    @GET("3/movie/{id}/videos")
//    Call<VideoList> getVideoList(@Path("id") int id);

}
