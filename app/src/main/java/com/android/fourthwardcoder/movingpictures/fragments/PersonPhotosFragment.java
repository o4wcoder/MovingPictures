package com.android.fourthwardcoder.movingpictures.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.adapters.ProfileListAdapter;
import com.android.fourthwardcoder.movingpictures.helpers.APIError;
import com.android.fourthwardcoder.movingpictures.helpers.ErrorUtils;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.activities.PersonPhotoPagerActivity;
import com.android.fourthwardcoder.movingpictures.models.PersonPhotoList;
import com.android.fourthwardcoder.movingpictures.models.Profile;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Class PersonPhotosFragment
 * Author: Chris Hare
 * Created: 9/26/2015
 * <p/>
 * Fragment to hold the GridView of a person's photos
 */
public class PersonPhotosFragment extends Fragment implements Constants, ProfileListAdapter.ProfileListAdapterOnClickHandler {

    /************************************************************************/
    /*                              Constanta                               */
    /************************************************************************/
    private static final String TAG = PersonPhotosFragment.class.getSimpleName();

    private static final String ARG_PERSON_ID = "person_id";
    private static final String ARG_PERSON_NAME = "person_name";
    private static final String ARG_PHOTO_LIST = "photo_list";

    /************************************************************************/
    /*                             Local Data                               */
    /************************************************************************/
    //GridView mGridView;
  //  ArrayList<PersonPhoto> mPhotoList;
    String mPersonName;
    private int mPersonId;
    private RecyclerView mRecyclerView;
    ArrayList<Profile> mProfileList;
    ProfileListAdapter mAdapter;
    boolean mFetchData = false;

    public static PersonPhotosFragment newInstance(int personId, String mPersonName) {

        Log.e(TAG,"newInstance()");
        Bundle args = new Bundle();
        args.putInt(ARG_PERSON_ID,personId);
        args.putString(ARG_PERSON_NAME,mPersonName);
        PersonPhotosFragment fragment = new PersonPhotosFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate()");
        if (savedInstanceState != null) {

            mPersonId = savedInstanceState.getInt(ARG_PERSON_ID);
            mPersonName = savedInstanceState.getString(ARG_PERSON_NAME);
            mProfileList = savedInstanceState.getParcelableArrayList(ARG_PHOTO_LIST);

        } else {
            Bundle bundle = getArguments();
            mPersonId = bundle.getInt(ARG_PERSON_ID);
            mPersonName = bundle.getString(ARG_PERSON_NAME);
            //Fetch data
            mFetchData = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_recycler_view, container, false);

        Log.e(TAG,"onCreateView()");
        //Set Up RecyclerView in Grid Layout
        mRecyclerView = (RecyclerView) view.findViewById(R.id.grid_recycler_view);
        //Set Layout Manager for RecyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        if(mRecyclerView != null) {

            //If we don't have a list, then go fetch it. Otherwise just set the adapter
            if(mFetchData) {
                Log.e(TAG,"Go fetch data");
                getPersonsPhotos();
            }
            else {
                Log.e(TAG,"Already have data, setAdapter");
                setAdapter();
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(ARG_PERSON_ID,mPersonId);
        savedInstanceState.putString(ARG_PERSON_NAME,mPersonName);
        savedInstanceState.putParcelableArrayList(ARG_PHOTO_LIST,mProfileList);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void getPersonsPhotos() {

        Log.e(TAG,"getPersonPhotos()");
        Call<PersonPhotoList> call = MovieDbAPI.getMovieApiService().getPersonsPhotos(mPersonId);

        call.enqueue(new retrofit2.Callback<PersonPhotoList>() {

            @Override
            public void onResponse(Call<PersonPhotoList> call, Response<PersonPhotoList> response) {

                if(response.isSuccessful()) {

                    mProfileList = (ArrayList)response.body().getProfiles();
                    setAdapter();

                } else {

                    APIError error = ErrorUtils.parseError(response);
                    Log.e(TAG, "Error message = " + error.message());
                    Toast.makeText(getContext(), error.message(), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<PersonPhotoList> call, Throwable t) {

                Toast.makeText(getContext(), getContext().getString(R.string.toast_network_error), Toast.LENGTH_LONG);
            }
        });
    }

    private void setAdapter() {

        mAdapter = new ProfileListAdapter(getActivity(),mProfileList,this);
        mRecyclerView.setAdapter(mAdapter);

        //Only show animation if we've fetched data
        if(mFetchData)
            mRecyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onProfileClick(int position, ProfileListAdapter.ProfileListAdapterViewHolder vh) {

        Log.e(TAG,"onProfileClick()");
        Intent i = new Intent(getActivity(), PersonPhotoPagerActivity.class);
                i.putParcelableArrayListExtra(EXTRA_PERSON_PHOTO, mProfileList);
                i.putExtra(EXTRA_PERSON_NAME, mPersonName);
                i.putExtra(EXTRA_PERSON_PHOTO_POSITION, position);
                startActivity(i);

    }

}
