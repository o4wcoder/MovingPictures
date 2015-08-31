package com.android.fourthwardcoder.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A placeholder fragment containing a simple view.
 */
public class PersonDetailActivityFragment extends Fragment {

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
    TextView mBiographyContentTextView;

    public PersonDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_detail, container, false);

        int personId = getActivity().getIntent().getIntExtra(MovieDetailActivityFragment.EXTRA_PERSON_ID,0);

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

        mBiographyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBiographyContentTextView.isShown()) {

                }
            }
        });
        //Person Biography Content
        mBiographyContentTextView = (TextView)view.findViewById(R.id.biographyContentTextView);
        mBiographyContentTextView.setVisibility(View.GONE);

        Log.e(TAG,"In Person detail with id " + personId);
        new FetchPersonTask().execute(personId);

        return view;
    }

    public static void slideDown(Context context, View v){
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private class FetchPersonTask extends AsyncTask<Integer,Void,Person> {


        @Override
        protected Person doInBackground(Integer... params) {

            //Get ID of movie
            int personId = params[0];

            Uri personUri = Uri.parse(DBUtil.BASE_MOVIE_DB_URL).buildUpon()
                    .appendPath(DBUtil.PATH_PERSON)
                    .appendPath(String.valueOf(personId))
                    .appendQueryParameter(DBUtil.PARM_API_KEY, DBUtil.API_KEY_MOVIE_DB)
                    .build();

            Log.e(TAG, personUri.toString());
            String personJsonStr = DBUtil.queryMovieDatabase(personUri);

            if(personJsonStr == null)
                return null;

            Log.e(TAG, "Person: " + personJsonStr);

            //List of Reviews that get parsed from Movie DB JSON return
            Person person;


            try {
                JSONObject obj = new JSONObject(personJsonStr);

                person = new Person(personId);

                person.setName(obj.getString(DBUtil.TAG_NAME));
                person.setBiography(obj.getString(DBUtil.TAG_BIOGRAPHY));
                person.setBirthday(obj.getString(DBUtil.TAG_BIRTHDAY));
                person.setDeathday(obj.getString(DBUtil.TAG_DEATHDAY));
                person.setBirthPlace(obj.getString(DBUtil.TAG_PLACE_OF_BIRTH));
                person.setProfileImagePath(DBUtil.BASE_MOVIE_IMAGE_URL + DBUtil.IMAGE_185_SIZE + obj.getString(DBUtil.TAG_PROFILE_PATH));
                person.setHomepage(obj.getString(DBUtil.TAG_HOMEPAGE));



            } catch (JSONException e) {
                Log.e(TAG,"Caught JSON exception " + e.getMessage());
                e.printStackTrace();
                return null;
            }

            return person;
        }

        @Override
        protected void onPostExecute(Person person) {

            if(person != null) {
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
