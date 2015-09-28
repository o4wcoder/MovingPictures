package com.android.fourthwardcoder.popularmovies.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.helpers.Util;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Person;
import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.activities.PersonFilmographyTabActivity;
import com.android.fourthwardcoder.popularmovies.activities.PersonPhotosActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Class PersonDetailFragment
 * Author: Chris Hare
 * Create: 8/25/15
 *
 * Fragment to hold the details of a person's info
 */
public class PersonDetailFragment extends Fragment implements Constants {

    /*********************************************************************/
    /*                          Constants                                */
    /*********************************************************************/
    private static final String TAG = PersonDetailFragment.class.getSimpleName();

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
    TextView mWebpageTextView;
    TextView mFilmographyTextView;
    TextView mPhotosTextView;

    Person mPerson;

    public PersonDetailFragment() {
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

        //Person Biography Content
        mBiographyContentTextView = (ExpandableTextView)view.findViewById(R.id.biographyContentExpandableTextView);

        mWebpageTextView = (TextView)view.findViewById(R.id.webPageTextView);
        //Person Filmography
        mFilmographyTextView = (TextView)view.findViewById(R.id.filmographyTextView);
        mFilmographyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(),PersonFilmographyTabActivity.class);
                i.putExtra(MovieDetailFragment.EXTRA_PERSON_ID,personId);
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

            return MovieDbAPI.getPerson(personId);
        }

        @Override
        protected void onPostExecute(Person person) {

            if((getActivity()!= null) && (person != null)) {

                //Store local copy of Person object
                mPerson = person;
                Picasso.with(getActivity()).load(person.getProfileImagePath()).into(mProfileImageView);

                mNameTextView.setText(person.getName());

                //Format birthday into form Jan 1, 2016
                String strBirthDay = Util.reverseDateString(person.getBirthday());

                Spanned bornDate = Html.fromHtml("<b>" + getString(R.string.born) + "</b>" + " " +
                        strBirthDay);

                mBornDateTextView.setText(bornDate);
                mBornPlaceTextView.setText(mPerson.getBirthPlace());

                if (!(mPerson.getDeathday()).equals("")) {
                    Spanned deathDate = Html.fromHtml("<b>" + getString(R.string.death) + "</b>" + " " +
                            mPerson.getDeathday());
                    mDeathDateTextView.setText(deathDate);
                } else {
                    mDeathDateTextView.setVisibility(View.INVISIBLE);
                }


                mBiographyContentTextView.setText(mPerson.getBiography());

                SpannableString pageSS = new SpannableString(mPerson.getHomepage());

                ClickableSpan span = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + mPerson.getHomepage()));
                        // Verify that the intent will resolve to an activity
                        if (i.resolveActivity(getActivity().getPackageManager()) != null)
                           startActivity(i);
                    }
                };

                pageSS.setSpan(span,0,mPerson.getHomepage().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mWebpageTextView.setText(pageSS);
                mWebpageTextView.setMovementMethod(LinkMovementMethod.getInstance());

            }

        }

    }
}
