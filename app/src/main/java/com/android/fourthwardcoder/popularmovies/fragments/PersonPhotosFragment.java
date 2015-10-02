package com.android.fourthwardcoder.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.helpers.Util;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.adapters.PersonImageAdapter;
import com.android.fourthwardcoder.popularmovies.models.PersonPhoto;
import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.activities.PersonPhotoPagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Class PersonPhotosFragment
 * Author: Chris Hare
 * Created: 9/26/2015
 * <p/>
 * Fragment to hold the GridView of a person's photos
 */
public class PersonPhotosFragment extends Fragment implements Constants {

    /************************************************************************/
    /*                              Constanta                               */
    /************************************************************************/
    private static final String TAG = PersonPhotosFragment.class.getSimpleName();

    /************************************************************************/
    /*                             Local Data                               */
    /************************************************************************/
    GridView mGridView;
    ArrayList<PersonPhoto> mPhotoList;
    String mPersonName;

    public PersonPhotosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Retain fragment across Activity re-creation
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_photos, container, false);

        final int personId = getActivity().getIntent().getIntExtra(EXTRA_PERSON_ID, 0);
        mPersonName = getActivity().getIntent().getStringExtra(EXTRA_PERSON_NAME);

        getActivity().setTitle(getString(R.string.photos_toolbar) + " " + mPersonName);

        mGridView = (GridView) view.findViewById(R.id.personPhotoGridView);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //PersonPhoto personPhoto = mPhotoList.get(position);
                Intent i = new Intent(getActivity(), PersonPhotoPagerActivity.class);
                i.putParcelableArrayListExtra(EXTRA_PERSON_PHOTO, mPhotoList);
                i.putExtra(EXTRA_PERSON_NAME, mPersonName);
                i.putExtra(EXTRA_PERSON_PHOTO_ID, position);
                startActivity(i);

            }
        });

        if (mGridView != null) {

            if(Util.isNetworkAvailable(getActivity())) {
                new FetchPersonPhotosTask().execute(personId);
            }
            else {
                Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                connectToast.show();
            }
        } else {
            //Hit this when we retained our instance of the fragment on a rotation.
            //Just apply the current list of photos
            PersonImageAdapter adapter = new PersonImageAdapter(getActivity().getApplicationContext(), mPhotoList);
            mGridView.setAdapter(adapter);
        }
        return view;
    }

    /****************************************************************************/
    /*                            Inner Classes                                 */

    /****************************************************************************/
    private class FetchPersonPhotosTask extends AsyncTask<Integer, Void, ArrayList<PersonPhoto>> {

        //ProgressDialog to be displayed while the data is being fetched and parsed
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            //Start ProgressDialog on Main Thread UI before precessing begins
            progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.progress_downloading_photos), true);
        }

        @Override
        protected ArrayList<PersonPhoto> doInBackground(Integer... params) {

            //Get ID of movie
            int personId = params[0];

            Uri personPhotosUri = Uri.parse(MovieDbAPI.BASE_MOVIE_DB_URL).buildUpon()
                    .appendPath(MovieDbAPI.PATH_PERSON)
                    .appendPath(String.valueOf(personId))
                    .appendPath(MovieDbAPI.PATH_IMAGES)
                    .appendQueryParameter(MovieDbAPI.PARAM_API_KEY, MovieDbAPI.API_KEY_MOVIE_DB)
                    .build();

            //Log.e(TAG, "Phot URI: " + personPhotosUri);

            String personPhotosJSONStr = MovieDbAPI.queryMovieDatabase(personPhotosUri);
            Log.e(TAG, personPhotosJSONStr);

            ArrayList<PersonPhoto> photoList = null;

            try {
                JSONObject obj = new JSONObject(personPhotosJSONStr);

                JSONArray profilesArray = obj.getJSONArray(MovieDbAPI.TAG_PROFILES);

                photoList = new ArrayList<>(profilesArray.length());

                for (int i = 0; i < profilesArray.length(); i++) {

                    JSONObject photoProfile = profilesArray.getJSONObject(i);

                    PersonPhoto personPhoto = new PersonPhoto(personId);

                    personPhoto.setThumbnailImagePath(MovieDbAPI.BASE_MOVIE_IMAGE_URL +
                            MovieDbAPI.getPosterSize(getActivity()) + photoProfile.getString(MovieDbAPI.TAG_FILE_PATH));

                    personPhoto.setFullImagePath(MovieDbAPI.BASE_MOVIE_IMAGE_URL +
                            MovieDbAPI.IMAGE_500_SIZE + photoProfile.getString(MovieDbAPI.TAG_FILE_PATH));

                    Log.e(TAG, personPhoto.getThumbnailImagePath());
                    photoList.add(personPhoto);

                }
            } catch (JSONException e) {
                Log.e(TAG, "Caught JSON Exception " + e.getMessage());
            }

            return photoList;
        }

        @Override
        protected void onPostExecute(ArrayList<PersonPhoto> photoList) {

            //Done processing the movie query, kill Progress Dialog on main UI
            progressDialog.dismiss();

            if (getActivity() != null && mGridView != null) {

                //If we've got movies in the list, then send them to the adapter fro the
                //GridView
                if (photoList != null) {

                    //Store global copy
                    mPhotoList = photoList;
                    PersonImageAdapter adapter = new PersonImageAdapter(getActivity().getApplicationContext(), photoList);
                    mGridView.setAdapter(adapter);
                } else {

                    //If we get here, then the movieList was empty and something went wrong.
                    //Most likely a network connection problem
                    Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                    connectToast.show();
                }

            }
        }
    }
}
