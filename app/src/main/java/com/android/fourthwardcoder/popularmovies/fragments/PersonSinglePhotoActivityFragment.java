package com.android.fourthwardcoder.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.R;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class PersonSinglePhotoActivityFragment extends Fragment implements Constants {

    String mPhotoPath;

    public static PersonSinglePhotoActivityFragment newInstance(String photoPath) {

        //Create argument bundle
        Bundle args = new Bundle();
        //Add crime ID to bundle. Can have any number of args
        args.putSerializable(EXTRA_FULL_PHOTO_PATH, photoPath);

        //Create Fragment. This method is used instead of constructor
        PersonSinglePhotoActivityFragment fragment = new PersonSinglePhotoActivityFragment();
        //Attach arguments to fragment
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        //Add the App Icon button as menu
        setHasOptionsMenu(true);

        //Get Fragment arguments and pull out ID of crime
        mPhotoPath = (String) getArguments().getSerializable(EXTRA_FULL_PHOTO_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //final String photoPath= getActivity().getIntent().getStringExtra(EXTRA_FULL_PHOTO_PATH);

        View view = inflater.inflate(R.layout.movie_image, container, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.movie_imageView);

        if((imageView != null) && (mPhotoPath != null)){

            Picasso.with(getActivity()).load(mPhotoPath).into(imageView);
        }


        return view;
    }
}
