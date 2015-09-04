package com.android.fourthwardcoder.popularmovies.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.helpers.DBUtil;
import com.android.fourthwardcoder.popularmovies.models.Person;
import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.activities.PersonFilmographyActivity;
import com.android.fourthwardcoder.popularmovies.activities.PersonPhotosActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class PersonDetailActivityFragment extends Fragment implements Constants {

    /*********************************************************************/
    /*                          Constants                                */
    /*********************************************************************/
    private static final String TAG = PersonDetailActivityFragment.class.getSimpleName();

    /*********************************************************************/
    /*                          Local Data                               */
    /*********************************************************************/
    ImageView mProfileImageView;
    TextView mNameTextView;
    TextView mBornDateTextView;
    TextView mBornPlaceTextView;
    TextView mDeathDateTextView;
    TextView mBiographyTextView;
    ExpandableTextView mBiographyContentTextView;
    TextView mFilmographyTextView;
    TextView mPhotosTextView;

    Person mPerson;

    public PersonDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retain the instance on rotation
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_detail, container, false);

        final int personId = getActivity().getIntent().getIntExtra(EXTRA_PERSON_ID,0);

        //Person Profile Image
        mProfileImageView = (ImageView)view.findViewById(R.id.profileImageView);
        //Person Name
        mNameTextView = (TextView)view.findViewById(R.id.nameTextView);
        //Person Birth Date
        mBornDateTextView = (TextView)view.findViewById(R.id.bornDateTextView);
        //Person Birth Place
        mBornPlaceTextView = (TextView)view.findViewById(R.id.bornPlaceTextView);
        //Person Death Date
        mDeathDateTextView = (TextView)view.findViewById(R.id.deathDateTextView);
        //Person Biography Link
        mBiographyTextView = (TextView)view.findViewById(R.id.biographyTextView);

//        mBiographyTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mBiographyContentTextView.isShown()) {
//                    AnimationUtil.slideUp(getActivity(), mBiographyContentTextView);
//                    mBiographyContentTextView.setVisibility(View.GONE);
//                } else {
//                    mBiographyContentTextView.setVisibility(View.VISIBLE);
//                    AnimationUtil.slideDown(getActivity(), mBiographyContentTextView);
//                }
//            }
//        });
        //Person Biography Content
        mBiographyContentTextView = (ExpandableTextView)view.findViewById(R.id.biographyContentExpandableTextView);

        //Person Filmography
        mFilmographyTextView = (TextView)view.findViewById(R.id.filmographyTextView);
        mFilmographyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(),PersonFilmographyActivity.class);
                i.putExtra(MovieDetailActivityFragment.EXTRA_PERSON_ID,personId);
                startActivity(i)
;            }
        });

        mPhotosTextView = (TextView)view.findViewById(R.id.photosTextView);
        mPhotosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),PersonPhotosActivity.class);
                i.putExtra(EXTRA_PERSON_ID,personId);
                i.putExtra(EXTRA_PERSON_NAME,mPerson.getName());
                startActivity(i);
            }
        });
        Log.e(TAG,"In Person detail with id " + personId);
        new FetchPersonTask().execute(personId);

        return view;
    }


    private class FetchPersonTask extends AsyncTask<Integer,Void,Person> {


        @Override
        protected Person doInBackground(Integer... params) {

            //Get ID of movie
            int personId = params[0];

            return DBUtil.getPerson(personId);
        }

        @Override
        protected void onPostExecute(Person person) {

            if(person != null) {

                //Store local copy of Person object
                mPerson = person;
                Picasso.with(getActivity()).load(person.getProfileImagePath()).into(mProfileImageView);

                mNameTextView.setText(person.getName());
                Spanned bornDate = Html.fromHtml("<b>" + getString(R.string.born) + "</b>" + " " +
                        person.getBirthday());
                mBornDateTextView.setText(bornDate);
                mBornPlaceTextView.setText(person.getBirthPlace());

                if (!(person.getDeathday()).equals("")) {
                    Spanned deathDate = Html.fromHtml("<b>" + getString(R.string.death) + "</b>" + " " +
                            person.getDeathday());
                    mDeathDateTextView.setText(deathDate);
                } else {
                    mDeathDateTextView.setVisibility(View.INVISIBLE);
                }


                mBiographyContentTextView.setText(person.getBiography());

            }

        }

    }
}
