package com.android.fourthwardcoder.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesMainFragment extends Fragment {


    /**************************************************/
	/*                 Constants                      */
    /**************************************************/
    private static final String TAG = PopularMoviesMainFragment.class.getSimpleName();
    public static final String EXTRA_MOVIE = "com.android.fourthwardcoder.popularmovies.extra_movie";

    private static final String PICASSO_API_KEY = APIKeys.PICASSO_API_KEY;
    private static final String BASE_MOVE_URL = "http://api.themoviedb.org/3/discover/movie";
    private static final String BASE_GENRE_URL = "http://api.themoviedb.org/3/genre/movie/list";
    private static final String BASE_CREDITS_URL = "http://api.themoviedb.org/3/movie/";
    private static final String SORT_PARM = "sort_by";
    private static final String API_KEY_PARAM = "api_key";

    private static final String PREF_SORT = "sort";
    //Tag for the time Sort Dialog
    private static final String DIALOG_SORT = "dialogSort";

    //Constant for request code to Sort Dialog
    public static final int REQUEST_SORT = 0;
    /**************************************************/
	/*                Local Data                      */
    /**************************************************/
    GridView mGridView;
    ArrayList<Movie> mMovieList;
    ArrayList<Pair<Integer,String>> mGenreList;
    String mSortOrder;
    SharedPreferences.Editor prefsEditor;

    public PopularMoviesMainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retain fragment across Activity re-creation
        setRetainInstance(true);

        //Set Option Menu
        setHasOptionsMenu(true);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        prefsEditor = sharedPrefs.edit();

        Resources res = getResources();
        String[] sortList = res.getStringArray(R.array.sort_url_list);
        mSortOrder = sharedPrefs.getString(PREF_SORT,sortList[0]);
        Log.e(TAG,"onCreate with sort " + mSortOrder);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView)view.findViewById(R.id.gridView);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get selected moview from the GridView
                Movie movie = mMovieList.get(position);
                //Start intent to bring up Details Activity
                Intent i = new Intent(getActivity(),MovieDetailActivity.class);
                i.putExtra(EXTRA_MOVIE, movie);
                startActivity(i);
            }
        });



        if(mGridView != null)
            new FetchPhotosTask().execute(mSortOrder);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);

        //Pass the resource ID of the menu and populate the Menu
        //instance with the items defined in the xml file
        inflater.inflate(R.menu.menu_sort, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_item_sort:
                Log.e(TAG,"Got menu sort");
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                SortDialogFragment dialog = SortDialogFragment.newInstance(mSortOrder);
                //Make PopularMoviesMainFragment the target fragment of the SortDialogFragment instance
                dialog.setTargetFragment(PopularMoviesMainFragment.this, REQUEST_SORT);
                dialog.show(fm, DIALOG_SORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Get results from Dialog boxes and other Activities
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e(TAG, "In onActivityResult with requestCode " + String.valueOf(requestCode));
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_SORT) {
            mSortOrder = data.getStringExtra(SortDialogFragment.EXTRA_SORT);
            prefsEditor.putString(PREF_SORT,mSortOrder);
            prefsEditor.commit();
            if(mGridView != null)
                new FetchPhotosTask().execute(mSortOrder);

        }
    }
    /******************************************************************************/
    /*                             Inner Classes                                  */
    /******************************************************************************/
    private class FetchPhotosTask extends AsyncTask<String,Void,ArrayList<Movie>> {

        private String moviesJsonStr;


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {


            String sortOrder = params[0];
            if(mGenreList == null) {
                //Build URI String to query the databaes for the list of genres
                Uri genreUri = Uri.parse(BASE_GENRE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, PICASSO_API_KEY)
                        .build();
                String genreJsonStr = queryMovieDatabase(genreUri);
                parseJsonGenres(genreJsonStr);
            }

            //Build URI String to query the database for a list of movies
            Uri movieUri = Uri.parse(BASE_MOVE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARM, sortOrder)
                    .appendQueryParameter(API_KEY_PARAM, PICASSO_API_KEY)
                    .build();

            return parseJsonMovies(queryMovieDatabase(movieUri));

        }

        private String queryMovieDatabase(Uri builtUri) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                Log.e(TAG, builtUri.toString());

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

                Log.e(TAG, moviesJsonStr);

            }
            catch(IOException e) {
                Log.e(TAG,e.getMessage());
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }

                //Return list of Movies
                return moviesJsonStr;
            }


        }
        private void parseJsonGenres(String genreJsonStr) {
            final String TAG_GENRES = "genres";
            final String TAG_ID = "id";
            final String TAG_NAME = "name";
            ArrayList<Pair> genreList;

            //Get genres http://api.themoviedb.org/3/genre/movie/list?api_key=e80f27e43348054952d67e7d0353ac38

            try {
                JSONObject obj = new JSONObject(genreJsonStr);
                JSONArray genreArray = obj.getJSONArray(TAG_GENRES);

                //Initialize genre List to the size of the number of returned genres
                if (genreArray != null) {
                    mGenreList = new ArrayList<Pair<Integer, String>>(genreArray.length());
                    for (int i = 0; i < genreArray.length(); i++) {
                        int id = genreArray.getJSONObject(i).getInt(TAG_ID);
                        String name = genreArray.getJSONObject(i).getString(TAG_NAME);

                        //Store genre ID and name into a Pair
                        Pair<Integer, String> genre = new Pair(id, name);

                        //Put pair into genre list
                        mGenreList.add(genre);
                    }
                }

            }
            catch(JSONException e) {
                e.printStackTrace();

            }


        }
        private ArrayList<Movie> parseJsonMovies(String moviesJsonStr) {

            final String TAG_RESULTS = "results";
            final String TAG_ID = "id";
            final String TAG_TITLE = "title";
            final String TAG_OVERVIEW = "overview";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_BACKDROP_PATH = "backdrop_path";
            final String TAG_RELEASE_DATE = "release_date";
            final String TAG_RATING = "vote_average";
            final String TAG_GENRE_IDS = "genre_ids";
            final String TAG_CREDITS = "credits";
            final String TAG_CAST = "cast";
            final String TAG_CREW = "crew";
            final String TAG_JOB = "job";
            final String TAG_JOB_DIRECTOR = "Director";
            final String TAG_NAME = "name";

            final String BASE_URL = "http://image.tmdb.org/t/p/";
            final String IMAGE_185_SIZE = "w185/";
            final String IMAGE_342_SIZE = "w342/";
            final String IMAGE_500_SIZE = "w500/";

            ArrayList<Movie> movieList = null;

            try {
                JSONObject obj = new JSONObject(moviesJsonStr);

                //Get Array List o
                // f all movies
                JSONArray resultsArray = obj.getJSONArray(TAG_RESULTS);

                //Log.e(TAG,"Results Array: " + resultsArray.get(0));

                movieList = new ArrayList<>(resultsArray.length());

                for(int i=0;i <resultsArray.length(); i++) {

                    JSONObject result = resultsArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setId(result.getInt(TAG_ID));
                    movie.setTitle(result.getString(TAG_TITLE));
                    movie.setOverview(result.getString(TAG_OVERVIEW));
                    movie.setPosterPath(BASE_URL + IMAGE_185_SIZE + result.getString(TAG_POSTER_PATH));
                    movie.setBackdropPath(BASE_URL + IMAGE_500_SIZE + result.getString(TAG_BACKDROP_PATH));
                    movie.setReleaseDate(result.getString(TAG_RELEASE_DATE));
                    movie.setRating(result.getDouble(TAG_RATING));

                    //Get genre ids
                    JSONArray genreArray = result.getJSONArray(TAG_GENRE_IDS);

                    //Move genre ids from JSON Array into ArrayList
                    ArrayList<Integer> genreList = new ArrayList<Integer>(genreArray.length());
                    for(int j=0; j < genreArray.length(); j++) {
                        //Log.e(TAG,"ID: " + genreArray.getInt(j));
                        genreList.add(j,genreArray.getInt(j));
                    }
                    //Store genre list in movie object
                    movie.setGenreList(genreList);

                    //Log.e(TAG, getGenreString(genreList));
                    movie.setGenreString(getGenreString(genreList));

                    //Get Uri for credits of movie
                    //Build URI String to query the databaes for the list of genres
                    Uri creditUri = Uri.parse(BASE_CREDITS_URL).buildUpon()
                            .appendPath(String.valueOf(movie.getId()))
                            .appendPath(TAG_CREDITS)
                            .appendQueryParameter(API_KEY_PARAM, PICASSO_API_KEY)
                            .build();

                    String creditJsonStr = queryMovieDatabase(creditUri);
                   // Log.e(TAG,"Credits: " + creditJsonStr);
                    JSONObject creditObj = new JSONObject(creditJsonStr);

                    //Pull out Crew information
                    JSONArray crewArray = creditObj.getJSONArray(TAG_CREW);

                    ArrayList<String> directorList = new ArrayList<String>();
                    for(int j = 0; j < crewArray.length(); j++) {
                        String job = crewArray.getJSONObject(j).getString(TAG_JOB);

                        //Find director
                        if(job.equals(TAG_JOB_DIRECTOR))
                            directorList.add(crewArray.getJSONObject(j).getString(TAG_NAME));

                    }
                    //Add director list to movie
                    movie.setDirectors(directorList);

                    //Pull out Cast Information
                    JSONArray castArray = creditObj.getJSONArray(TAG_CAST);
                    ArrayList<String> actorList = new ArrayList<String>();

                    for(int j = 0; j < castArray.length(); j++) {
                        String name = castArray.getJSONObject(j).getString(TAG_NAME);

                        actorList.add(name);

                    }

                    //Add cast list to movie
                    movie.setActors(actorList);

                    //Add movie to movie list array.
                    movieList.add(movie);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return movieList;
        }

        private String getGenreString(ArrayList<Integer> idList) {

            String genreStr = "";

            for(int i=0; i < idList.size();i++) {
                int id = idList.get(i);
                for(int j=0; j < mGenreList.size(); j++) {
                    Pair<Integer,String> genre = mGenreList.get(j);
                    if(id == genre.first)
                        genreStr += genre.second + ", ";
                }
            }

            if(idList.size() > 0)
                genreStr = genreStr.substring(0,genreStr.length() - 2);

            return genreStr;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> movieList) {

            String strTitle = movieList.get(0).getTitle();
            Log.e(TAG, "OnPostExecute with first moview " + strTitle);

            if(getActivity() != null && mGridView != null) {
                MovieImageAdapter adapter = new MovieImageAdapter(movieList);
                mGridView.setAdapter(adapter);

                Log.e(TAG,"Size of genre list is " + mGenreList.size());
            }
        }
    }

    private class MovieImageAdapter extends ArrayAdapter<Movie> {

        public MovieImageAdapter(ArrayList<Movie> movies) {
            super(getActivity(),0,movies);

            //Store Global List of movies
            mMovieList = movies;
        }

        //Override the getView to return an ImageView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.movie_image, parent, false);
            }

            //Get imageView
            ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_imageView);


            //Get each Movie using the position in the ArrayAdapter
            Movie movie = getItem(position);

            //Call Picasso to load it into the imageView
            Picasso.with(getActivity()).load(movie.getPosterPath()).into(imageView);

            return convertView;
        }
    }

}
