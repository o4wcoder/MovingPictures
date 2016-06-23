package com.android.fourthwardcoder.movingpictures.helpers;

/**
 * Created by Chris Hare on 6/21/2016.
 */
public class UnusedHttpMethods {


    /********************************************************************************************/
    /*                                  Old HTTP API Calls                                      */
    /********************************************************************************************/
    /**
     * Parses the JSON String returned from the query to the MovieOld DB. Pulls data out for
     * a each movie returned and puts that data into a MovieOld object. These movie objects are
     * returned in an ARRAYList
     * @param context       Context of the calling Activity
     * @param moviesJsonStr Full return JSON String of movie data
     * @return              ArrayList of Movies
     */
//    private static ArrayList<MovieOld> parseJsonMovieList(Context context, String moviesJsonStr) {
//
//        //List of Movies that get parsed MovieOld DB JSON return
//        ArrayList<MovieOld> movieList = null;
//
//        try {
//            JSONObject obj = new JSONObject(moviesJsonStr);
//
//            //Get JSONArray List of all movies
//            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_RESULTS);
//
//            //Log.e(TAG,"Results Array: " + resultsArray.get(0));
//
//            //Initialize movie array list by the number of movies returned in the query
//            movieList = new ArrayList<>(resultsArray.length());
//
//            //Loop through all of the movies returned in the query
//            for(int i=0;i <resultsArray.length(); i++) {
//
//                //Get MovieOld result. Create movie object and pull out particular data for
//                //that movie.
//                JSONObject result = resultsArray.getJSONObject(i);
//               // Log.e(TAG,"MOVIE Result: " + result.toString());
//
//                 int movieId = result.getInt(MovieDbAPI.TAG_ID);
//                 MovieOld movie = getMovie(movieId);
//
//                //Add movie to movie list array.
//                if(movie != null)
//                   movieList.add(movie);
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        return movieList;
//    }

    //    /**
//     * Get single movie
//     * @param id MovieOld id
//     * @return   MovieOld object
//     */
//    public static MovieOld getMovie(int id) {
//
//        Uri uri = buildMovieUri(id);
//        String jsonStr = MovieDbAPI.queryMovieDatabase(uri);
//
//        if(jsonStr == null)
//            return null;
//        else
//            return parseJsonMovie(jsonStr);
//    }

    /**
     * Get list of movies. Uri is required as the sort parameter
     * @param context   Context of calling Activity
     * @param movieUri  URI string use to poll data from movie database
     * @return          ArrayList of Simple Movies
     */
//    public static ArrayList<MovieOld> getMovieList(Context context, Uri movieUri) {
//
//        Log.e(TAG,"MovieURI: " + movieUri);
//        String movieJsonStr = queryMovieDatabase(movieUri);
//        Log.e(TAG,movieJsonStr);
//        //Error pulling movies, return null
//        if(movieJsonStr == null)
//            return null;
//
//        //Part data and return list of movies
//        return parseJsonMovieList(context,movieJsonStr);
//    }

    /**
     * Get list of videos from a MovieOld or TV Show
     * @param id MovieOld id
     * @return   ArrayList of VideoList
     */
//    public static ArrayList<ReviewOld> getReviewList(int id, int entType) {
//
//        Uri reviewUri = buildReviewsUri(id, entType);
//        String reviewJsonStr = queryMovieDatabase(reviewUri);
//
//        if(reviewJsonStr == null)
//            return null;
//        else
//            return parseJsonReviewList(reviewJsonStr);
//    }

    /**
     //     * Build URI to get the reviews of a MovieOld or TV show
     //     * @param id      Id of the movie or TV show
     //     * @param entType Type of entertainment; MovieOld or TV
     //     * @return        Uri to the reviews of the MovieOld or TV show
     //     */
//    private static Uri buildReviewsUri(int id, int entType) {
//
//        String entTypePath = "";
//        if(entType == ENT_TYPE_MOVIE)
//            entTypePath = PATH_MOVIE;
//        else
//            entTypePath = PATH_TV;
//
//        Uri reviewsUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
//                .appendPath(entTypePath)
//                .appendPath(String.valueOf(id))
//                .appendPath(PATH_REVIEWS)
//                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
//                .build();
//        Log.e(TAG,"ReviewOld URI: " + reviewsUri);
//        return reviewsUri;
//    }
    /**
     //     * Parse JSON String of Reviews
     //     * @param reviewsJsonStr JSON String of reviews
     //     * @return               ArrayList of reviews
     //     */
//    private static ArrayList<ReviewOld> parseJsonReviewList(String reviewsJsonStr) {
//
//        //List of Reviews that get parsed from MovieOld DB JSON return
//        ArrayList<ReviewOld> reviewList = null;
//        //Log.e(TAG,"REviewJson: " +reviewsJsonStr);
//        try {
//            JSONObject obj = new JSONObject(reviewsJsonStr);
//            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_RESULTS);
//
//            reviewList = new ArrayList<>(resultsArray.length());
//
//            for(int i = 0; i< resultsArray.length(); i++) {
//
//                JSONObject result = resultsArray.getJSONObject(i);
//                ReviewOld review = new ReviewOld();;
//                review.setAuthor(result.getString(MovieDbAPI.TAG_AUTHOR));
//                review.setContent(result.getString(MovieDbAPI.TAG_CONTENT));
//
//                Log.e(TAG, review.toString());
//                reviewList.add(review);
//            }
//        } catch (JSONException e) {
//            Log.e(TAG,"Caught JSON exception " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//
//        return reviewList;
//    }
    /**
     //     * Parse JSON String of MovieOld
     //     * @param movieJsonStr JSON String of MovieOld
     //     * @return             MovieOld object
     //     */
//    private static MovieOld parseJsonMovie(String movieJsonStr) {
//
//        MovieOld movie = null;
//
//        try {
//            if (movieJsonStr != null) {
//                //Log.e(TAG, "MovieOld: " + movieJsonStr);
//
//
//                JSONObject movieObj = new JSONObject(movieJsonStr);
//
//                movie = new MovieOld(movieObj.getInt(MovieDbAPI.TAG_ID));
//                movie.setTitle(movieObj.getString(MovieDbAPI.TAG_TITLE));
//                movie.setOverview(movieObj.getString(MovieDbAPI.TAG_OVERVIEW));
//                movie.setPosterPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE + movieObj.getString(MovieDbAPI.TAG_POSTER_PATH));
//                movie.setBackdropPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_500_SIZE + movieObj.getString(MovieDbAPI.TAG_BACKDROP_PATH));
//                movie.setReleaseDate(movieObj.getString(MovieDbAPI.TAG_RELEASE_DATE));
//                movie.setRating(movieObj.getDouble(MovieDbAPI.TAG_RATING));
//                movie.setRuntime(movieObj.getString(MovieDbAPI.TAG_RUNTIME));
//
//                movie.setGenres(parseList(movieObj, TAG_GENRES, TAG_NAME));
//
//                int iRevenue = movieObj.getInt(MovieDbAPI.TAG_REVENUE);
//                movie.setRevenue(NumberFormat.getIntegerInstance().format(iRevenue));
//
//                //Get Uri for credits of movie
//                //Build URI String to query the databaes for the list of credits
//                Uri creditUri = buildMovieCreditsUri(movie.getId());
//
//                String creditJsonStr = MovieDbAPI.queryMovieDatabase(creditUri);
//
//                if (creditJsonStr != null) {
//
//                    // Log.e(TAG,"Credits: " + creditJsonStr);
//                    JSONObject creditObj = new JSONObject(creditJsonStr);
//
//                    //Pull out Crew information
//                    JSONArray crewArray = creditObj.getJSONArray(MovieDbAPI.TAG_CREW);
//
//                    ArrayList<IdNamePair> directorList = new ArrayList<IdNamePair>();
//                    for (int j = 0; j < crewArray.length(); j++) {
//                        String job = crewArray.getJSONObject(j).getString(MovieDbAPI.TAG_JOB);
//
//                        //Find director
//                        if (job.equals(MovieDbAPI.TAG_JOB_DIRECTOR)) {
//                            IdNamePair director = new IdNamePair(crewArray.getJSONObject(j).getInt(MovieDbAPI.TAG_ID));
//                            director.setName(crewArray.getJSONObject(j).getString(MovieDbAPI.TAG_NAME));
//                            directorList.add(director);
//                        }
//                    }
//                    //Add director list to movie
//                    if (directorList.size() > 0)
//                        movie.setDirectors(directorList);
//
//                    //Pull out Cast Information
//                    JSONArray castArray = creditObj.getJSONArray(TAG_CAST);
//                    ArrayList<IdNamePair> actorNameList = new ArrayList<IdNamePair>();
//                    //Pull out actors of the movie
//                    for (int j = 0; j < castArray.length(); j++) {
//                        //Log.e(TAG, castArray.getJSONObject(j).toString());
//                        IdNamePair cast = new IdNamePair(castArray.getJSONObject(j).getInt(TAG_ID));
//                        cast.setName(castArray.getJSONObject(j).getString(TAG_NAME));
//
//                        actorNameList.add(cast);
//                    }
//
//                    //Add cast list to movie
//                    if (actorNameList.size() > 0) {
//                        //Log.e(TAG, "Setting actor lists to movie object");
//
//                        movie.setActors(actorNameList);
//                    }
//
//                    //Get VideoList
//                    movie.setVideos(getVideoList(movie.getId(), ENT_TYPE_MOVIE));
//
//                }
//            }
//        } catch (JSONException e) {
//            Log.e(TAG, e.getMessage());
//            return null;
//        }
//
//        return movie;
//
//    }
    /**
     //     * Get single person
     //     * @param id PersonOld id
     //     * @return   PersonOld object
     //     */
//    public static PersonOld getPerson(int id) {
//
//        Uri uri = buildPersonUri(id);
//        String jsonStr = MovieDbAPI.queryMovieDatabase(uri);
//        Log.e(TAG,"PersonOld = " + uri);
//        if(jsonStr == null)
//            return null;
//        else
//            return parseJsonPerson(jsonStr);
//    }

    /**
     //     * Parse JSON string of PersonOld data
     //     * @param personJsonStr JSON string of person data
     //     * @return              PersonOld Object containing data of the person
     //     */
//    private static PersonOld parseJsonPerson(String personJsonStr) {
//
//        PersonOld person = null;
//
//        try {
//            JSONObject obj = new JSONObject(personJsonStr);
//
//            person = new PersonOld(obj.getInt(MovieDbAPI.TAG_ID));
//
//            person.setName(obj.getString(MovieDbAPI.TAG_NAME));
//            person.setBiography(obj.getString(MovieDbAPI.TAG_BIOGRAPHY));
//            person.setBirthday(obj.getString(MovieDbAPI.TAG_BIRTHDAY));
//            person.setDeathday(obj.getString(MovieDbAPI.TAG_DEATHDAY));
//            person.setBirthPlace(obj.getString(MovieDbAPI.TAG_PLACE_OF_BIRTH));
//            person.setProfileImagePath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE + obj.getString(MovieDbAPI.TAG_PROFILE_PATH));
//
//            String strPage = obj.getString(MovieDbAPI.TAG_HOMEPAGE);
//            strPage = strPage.replace("http://","");
//            person.setHomepage(strPage);
//
//        } catch (JSONException e) {
//            Log.e(TAG,"Caught JSON exception " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//
//        return person;
//    }

    /**
     //     * Build Uri to get a PersonOld
     //     * @param personId Id of person
     //     * @return         Uri to the PersonOld
     //     */
//    private static Uri buildPersonUri(int personId) {
//
//        Uri personUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
//                .appendPath(MovieDbAPI.PATH_PERSON)
//                .appendPath(String.valueOf(personId))
//                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
//                .build();
//
//        return personUri;
//    }

    /**
     //     * Build URI to get a single movie from the database
     //     * @param movieId Id of the movie
     //     * @return        Uri to movie
     //     */
//    private static Uri buildMovieUri(int movieId) {
//        //Get Uri for basic movie info
//        //Build URI String to query the databaes for a specific movie
//        Uri movieUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
//                .appendPath(MovieDbAPI.PATH_MOVIE)
//                .appendPath(String.valueOf(movieId))
//                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
//                .build();
//
//        return movieUri;
//    }

//    /**
//     * Get list of credits of a person
//     * @param personId PersonOld id
//     * @return         ArrayList of credits of a person
//     */
//    public static ArrayList<CreditOld> getPersonCreditList(int personId, int creditType) {
//
//        Uri creditUri = buildPersonCreditsUri(personId, creditType);
//        String creditJsonStr = queryMovieDatabase(creditUri);
//
//        if(creditJsonStr == null)
//            return null;
//        else
//            return parseJsonPersonCreditList(creditJsonStr, creditType);
//    }
    /**
     //     * Build Uri to a PersonOld's credits
     //     * @param personId Id of PersonOld
     //     * @return         Uri to person's credits
     //     */
//    private static Uri buildPersonCreditsUri(int personId,int entType) {
//
//        String entTypePath = "";
//        if(entType == ENT_TYPE_MOVIE)
//            entTypePath = PATH_MOVIE_CREDIT;
//        else
//            entTypePath = PATH_TV_CREDIT;
//
//        Uri personCreditsUri = Uri.parse(BASE_MOVIE_DB_URL).buildUpon()
//                .appendPath(PATH_PERSON)
//                .appendPath(String.valueOf(personId))
//                .appendPath(entTypePath)
//                .appendQueryParameter(PARAM_API_KEY, API_KEY_MOVIE_DB)
//                .build();
//
//        Log.e(TAG,"Person's credits uri = " + personCreditsUri);
//        return personCreditsUri;
//    }

    /**
     //     * Parse JSON String of a PersonOld's credits
     //     * @param personCreditsJsonStr JSON String of PersonOld's credits
     //     * @param entType              Type of entertainment; MovieOld or TV
     //     * @return                     ArrayList of PersonOld's credits
     //     */
//    private static ArrayList<CreditOld> parseJsonPersonCreditList(String personCreditsJsonStr, int entType) {
//
//        //List of Reviews that get parsed from MovieOld DB JSON return
//        ArrayList<CreditOld> creditList = null;
//
//        try {
//            JSONObject obj = new JSONObject(personCreditsJsonStr);
//            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_CAST);
//
//            creditList = new ArrayList<>(resultsArray.length());
//
//            Log.e(TAG, "CreditOld json: " + personCreditsJsonStr);
//            for(int i = 0; i< resultsArray.length(); i++) {
//
//                JSONObject result = resultsArray.getJSONObject(i);
//                CreditOld credit = new CreditOld(result.getInt(MovieDbAPI.TAG_ID));;
//                credit.setCharacter(result.getString(MovieDbAPI.TAG_CHARACTER));
//                credit.setPosterPath(MovieDbAPI.BASE_MOVIE_IMAGE_URL + MovieDbAPI.IMAGE_185_SIZE
//                        + result.getString(MovieDbAPI.TAG_POSTER_PATH));
//
//                //Different release and title tags between movies and tv filmography
//                String releaseTag = TAG_RELEASE_DATE;
//                String titleTag = TAG_TITLE;
//                if(entType == ENT_TYPE_TV) {
//                    releaseTag = TAG_FIRST_AIR_DATE;
//                    titleTag = TAG_NAME;
//                }
//
//                String releaseDate = result.getString(releaseTag);
//                credit.setTitle(result.getString(titleTag));
//
//                if((releaseDate != null) && (releaseDate != "") && (releaseDate != "null")) {
//                    String dateArray[] = releaseDate.split("-");
//
//                    credit.setReleaseYear(Integer.parseInt(dateArray[0]));
//                }
//                else
//                    Log.e(TAG,"Got null release date");
//
//
//                creditList.add(credit);
//            }
//
//            //Sort array from newest to oldest film
//            Collections.sort(creditList);
//        } catch (JSONException e) {
//            Log.e(TAG,"Caught JSON exception " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//
//        return creditList;
//    }
    /**
     //     * Get a single tv show
     //     * @param id TV id
     //     * @return   TV object
     //     */
//    public static TvShowOld getTvShow(int id) {
//
//        Uri uri = buildTvUri(id);
//        Log.e(TAG,"TvShowOld Uri = " + uri);
//        String jsonStr = MovieDbAPI.queryMovieDatabase(uri);
//
//        if(jsonStr == null)
//            return null;
//        else
//            return parseJsonTvShow(jsonStr);
//    }
    /**
     //     * Build URI to get a single TV show from the database
     //     * @param tvId Id of the TV show
     //     * @return     Uri to TV show
     //     */
//    private static Uri buildTvUri(int tvId) {
//        //Get Uri for basic tv info
//        //Build URI String to query the databaes for a specific movie
//        Uri movieUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
//                .appendPath(MovieDbAPI.PATH_TV)
//                .appendPath(String.valueOf(tvId))
//                .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
//                .build();
//
//        return movieUri;
//    }
    /**
     //     * Parse JSON String of TV Show
     //     * @param tvJsonStr Json String of TV Show
     //     * @return          TVShow object
     //     */
//    public static TvShowOld parseJsonTvShow(String tvJsonStr) {
//
//        TvShowOld tvShow = null;
//
//        try {
//            if (tvJsonStr != null) {
//                //Log.e(TAG, "TV: " + tvJsonStr);
//
//
//                JSONObject tvObj = new JSONObject(tvJsonStr);
//
//                tvShow = new TvShowOld(tvObj.getInt(MovieDbAPI.TAG_ID));
//                tvShow.setTitle(tvObj.getString(MovieDbAPI.TAG_NAME));
//                tvShow.setOverview(tvObj.getString(MovieDbAPI.TAG_OVERVIEW));
//                tvShow.setPosterPath(BASE_MOVIE_IMAGE_URL + IMAGE_185_SIZE + tvObj.getString(TAG_POSTER_PATH));
//                tvShow.setBackdropPath(BASE_MOVIE_IMAGE_URL + IMAGE_500_SIZE + tvObj.getString(TAG_BACKDROP_PATH));
//                tvShow.setReleaseDate(tvObj.getString(TAG_FIRST_AIR_DATE));
//                tvShow.setLastAirDate(tvObj.getString(TAG_LAST_AIR_DATE));
//                tvShow.setRating(tvObj.getDouble(MovieDbAPI.TAG_RATING));
//
//
//                tvShow.setCreatedBy(parseList(tvObj, TAG_CREATED_BY, TAG_NAME));
//
//                JSONArray runtimeArray = tvObj.getJSONArray(TAG_EPISODE_RUN_TIME);
//                tvShow.setRuntime(String.valueOf(runtimeArray.getInt(0)));
//
//                tvShow.setGenres(parseList(tvObj, TAG_GENRES, TAG_NAME));
//
//                tvShow.setNetworks(parseList(tvObj, TAG_NETWORKS, TAG_NAME));
//                //Get status of show
//                tvShow.setStatus(tvObj.getString(TAG_STATUS));
//                //Get Uri for credits of tv show
//                //Build URI String to query the databaes for the list of credits
//                Uri creditUri = buildTvCreditsUri(tvShow.getId());
//
//                String creditJsonStr = queryMovieDatabase(creditUri);
//
//                if (creditJsonStr != null) {
//
//                    // Log.e(TAG,"Credits: " + creditJsonStr);
//                    JSONObject creditObj = new JSONObject(creditJsonStr);
//
//                    //Pull out Cast Information
//                    JSONArray castArray = creditObj.getJSONArray(TAG_CAST);
//                    ArrayList<IdNamePair> actorNameList = new ArrayList<>(castArray.length());
//                    //Pull out actors of the movie
//                    for (int j = 0; j < castArray.length(); j++) {
//                        //Log.e(TAG, castArray.getJSONObject(j).toString());
//                        IdNamePair cast = new IdNamePair(castArray.getJSONObject(j).getInt(TAG_ID));
//                        cast.setName(castArray.getJSONObject(j).getString(TAG_NAME));
//
//                        actorNameList.add(cast);
//                    }
//
//                    //Add cast list to movie
//                    if (actorNameList.size() > 0) {
//                        //Log.e(TAG, "Setting actor lists to movie object");
//                        tvShow.setActors(actorNameList);
//                    }
//                }
//
//                //Get VideoList
//                tvShow.setVideos(getVideoList(tvShow.getId(), ENT_TYPE_TV));
//            }
//        }
//        catch (JSONException e) {
//            Log.e(TAG,"Caught JSON exception " + e.getMessage());
//        }
//
//        return tvShow;
//    }

    /**
     //     * Parse a JSON Array and pull out date from one element in the array
     //     * @param obj      JSON Object containing Array
     //     * @param arrayTag Name of JSON Array in obj
     //     * @param innerTag Name of element in the array to pull out
     //     * @return         ArrayList of String elements pulled from the array
     //     */
//    private static ArrayList<String> parseList(JSONObject obj,String arrayTag, String innerTag) {
//
//        ArrayList<String> list = null;
//        try {
//            JSONArray jsonArray = obj.getJSONArray(arrayTag);
//
//            //Get genres of the movie
//            list = new ArrayList<>(jsonArray.length());
//            for (int j = 0; j < jsonArray.length(); j++) {
//                list.add(jsonArray.getJSONObject(j).getString(innerTag));
//            }
//        }
//        catch (JSONException e) {
//            Log.e(TAG,"Caught JSON exception " + e.getMessage());
//        }
//
//        return list;
//    }

    /**
     //     * Get list of videos from a MovieOld
     //     * @param movieId MovieOld id
     //     * @return        ArrayList of VideoList
     //     */
//    public static ArrayList<VideoOld> getVideoList(int movieId, int entType) {
//
//        Uri videoUri = buildVideoUri(movieId, entType);
//        Log.e(TAG,"video Uri = " + videoUri);
//        String videoJsonStr = queryMovieDatabase(videoUri);
//
//        if(videoJsonStr == null)
//            return null;
//        else
//            return parseJsonVideoList(videoJsonStr);
//    }
    /**
     //     * Build URI to get the videos of a MovieOld or TV show
     //     * @param id      Id of the movie or TV show
     //     * @param entType Type of entertainment; MovieOld or TV
     //     * @return        Uri to the videos of the MovieOld or TV show
     //     */
//    private static Uri buildVideoUri(int id, int entType) {
//
//        String entTypePath = "";
//        if(entType == ENT_TYPE_MOVIE)
//            entTypePath = PATH_MOVIE;
//        else
//            entTypePath = PATH_TV;
//
//        Uri videosUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
//            .appendPath(entTypePath)
//            .appendPath(String.valueOf(id))
//            .appendPath(PATH_VIDEOS)
//            .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
//            .build();
//        //Log.e(TAG,"video URI: " + videosUri);
//        return videosUri;
//    }
    /**
     //     * Parses the JSON String returned from the query to the MovieOld DB. Pulls data out for
     //     * a each video/trailers returned and puts that data into a VideoOld object. These video objects are
     //     * returned in an ARRAYList
     //     *
     //     * @param videosJsonStr
     //     * @return Full return JSON String of video data
     //     */
//    private static ArrayList<VideoOld> parseJsonVideoList(String videosJsonStr) {
//
//        ArrayList<VideoOld> videoList = null;
//       // Log.e(TAG,"videoJson: " + videosJsonStr);
//
//        try {
//            JSONObject obj = new JSONObject(videosJsonStr);
//            JSONArray resultsArray = obj.getJSONArray(MovieDbAPI.TAG_RESULTS);
//
//            videoList = new ArrayList<>(resultsArray.length());
//
//            for(int i = 0; i< resultsArray.length(); i++) {
//
//                JSONObject result = resultsArray.getJSONObject(i);
//                VideoOld video = new VideoOld(result.getString(MovieDbAPI.TAG_KEY));
//                video.setName(result.getString(MovieDbAPI.TAG_NAME));
//                video.setType(result.getString(MovieDbAPI.TAG_TYPE));
//                video.setSize(result.getInt(MovieDbAPI.TAG_SIZE));
//
//                Log.e(TAG, video.getName());
//                videoList.add(video);
//            }
//        } catch (JSONException e) {
//            Log.e(TAG,"Caught JSON exception " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//
//        return videoList;
//    }
}
