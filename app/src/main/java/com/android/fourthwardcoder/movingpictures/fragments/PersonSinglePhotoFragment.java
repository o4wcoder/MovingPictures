package com.android.fourthwardcoder.movingpictures.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Class PersonSinglePhotoFragment
 * Author: Chris Hare
 * Created: 8/25/15
 * <p/>
 * Fragent to hold a single person's picture
 */
public class PersonSinglePhotoFragment extends Fragment implements Constants {

    /*************************************************************************/
    /*                           Constants                                   */
    /*************************************************************************/
    private static final String TAG = PersonSinglePhotoFragment.class.getSimpleName();
    /*************************************************************************/
    /*                           Local Data                                  */
    /*************************************************************************/
    String mPhotoPath;

    public static PersonSinglePhotoFragment newInstance(String photoPath) {

        //Create argument bundle
        Bundle args = new Bundle();
        //Add person's picture path to bundle
        args.putSerializable(EXTRA_FULL_PHOTO_PATH, photoPath);

        //Create Fragment.
        PersonSinglePhotoFragment fragment = new PersonSinglePhotoFragment();
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

        View view = inflater.inflate(R.layout.person_image, container, false);



        final ImageView imageView = (ImageView) view.findViewById(R.id.movie_imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"onClick() ");

                ((OnPhotoClick)getActivity()).onSetBottomPanelVisibility();
            }
        });

        if ((imageView != null) && (mPhotoPath != null)) {

            Picasso.with(getActivity()).load(mPhotoPath).into(imageView);
        }

        return view;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            Log.e(TAG,"OnSuccess() with visible photo " + mPhotoPath);
//        }
//        else {
//        }
//    }
    public interface OnPhotoClick {

        void onSetBottomPanelVisibility();
    }
}
