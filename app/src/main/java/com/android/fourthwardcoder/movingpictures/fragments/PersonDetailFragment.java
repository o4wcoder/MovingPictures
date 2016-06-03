package com.android.fourthwardcoder.movingpictures.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Person;
import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.activities.PersonFilmographyTabActivity;
import com.android.fourthwardcoder.movingpictures.activities.PersonPhotosActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;


/**
 * Class PersonDetailFragment
 * Author: Chris Hare
 * Create: 8/25/15
 * <p/>
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

        final int personId = getActivity().getIntent().getIntExtra(EXTRA_PERSON_ID, 0);

        //Person Profile Image
        mProfileImageView = (ImageView) view.findViewById(R.id.profileImageView);
        //Person Name
        mNameTextView = (TextView) view.findViewById(R.id.nameTextView);
        //Person Birth Date
        mBornDateTextView = (TextView) view.findViewById(R.id.bornDateTextView);
        //Person Birth Place
        mBornPlaceTextView = (TextView) view.findViewById(R.id.bornPlaceTextView);
        //Person Death Date
        mDeathDateTextView = (TextView) view.findViewById(R.id.deathDateTextView);

        //Person Biography Content
        mBiographyContentTextView = (ExpandableTextView) view.findViewById(R.id.biographyContentExpandableTextView);

        mWebpageTextView = (TextView) view.findViewById(R.id.webPageTextView);
        //Person Filmography
        mFilmographyTextView = (TextView) view.findViewById(R.id.filmographyTextView);
        mFilmographyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), PersonFilmographyTabActivity.class);
                i.putExtra(MovieDetailFragment.EXTRA_PERSON_ID, personId);
                startActivity(i)
                ;
            }
        });

        mPhotosTextView = (TextView) view.findViewById(R.id.photosTextView);
        mPhotosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PersonPhotosActivity.class);
                i.putExtra(EXTRA_PERSON_ID, personId);
                i.putExtra(EXTRA_PERSON_NAME, mPerson.getName());
                startActivity(i);
            }
        });

        if(Util.isNetworkAvailable(getActivity())) {
            new FetchPersonTask().execute(personId);
        }
        else {
            Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.toast_network_error), Toast.LENGTH_LONG);
            connectToast.show();
        }

        return view;
    }

    /*************************************************************************/
    /*                           Inner Classes                               */

    /*************************************************************************/
    private class FetchPersonTask extends AsyncTask<Integer, Void, Person> {

        @Override
        protected Person doInBackground(Integer... params) {

            //Get ID of movie
            int personId = params[0];

            return MovieDbAPI.getPerson(personId);
        }

        @Override
        protected void onPostExecute(Person person) {

            if ((getActivity() != null) && (person != null)) {

                //Store local copy of Person object
                mPerson = person;
                Picasso.with(getActivity()).load(person.getProfileImagePath()).into(mProfileImageView);

                //Set title of Movie on Action Bar
                getActivity().setTitle(person.getName());
                mNameTextView.setText(person.getName());

                //Format birthday into form Jan 1, 2016
                String strBirthDay = Util.reverseDateString(person.getBirthday());

                Spanned bornDate = Html.fromHtml("<b>" + getString(R.string.born) + "</b>" + " " +
                        strBirthDay);

                mBornDateTextView.setText(bornDate);
                mBornPlaceTextView.setText(mPerson.getBirthPlace());

                if (!(mPerson.getDeathday()).equals("")) {
                    Spanned deathDate = Html.fromHtml("<b>" + getString(R.string.death) + "</b>" + " " +
                            Util.reverseDateString(mPerson.getDeathday()));
                    mDeathDateTextView.setText(deathDate);
                } else {
                    mDeathDateTextView.setVisibility(View.INVISIBLE);
                }

                Spanned biography = Html.fromHtml("<b>" + getString(R.string.biography) + "</b>" + " " +
                        mPerson.getBiography());
                mBiographyContentTextView.setText(biography);

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

                pageSS.setSpan(span, 0, mPerson.getHomepage().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mWebpageTextView.setText(pageSS);
                mWebpageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }
}
