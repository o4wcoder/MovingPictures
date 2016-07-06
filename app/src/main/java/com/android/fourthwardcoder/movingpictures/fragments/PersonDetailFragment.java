package com.android.fourthwardcoder.movingpictures.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.activities.PersonDetailActivity;
import com.android.fourthwardcoder.movingpictures.helpers.APIError;
import com.android.fourthwardcoder.movingpictures.helpers.ErrorUtils;
import com.android.fourthwardcoder.movingpictures.helpers.ImageTransitionListener;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.MediaBasic;
import com.android.fourthwardcoder.movingpictures.models.MovieList;
import com.android.fourthwardcoder.movingpictures.models.Person;
import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.activities.PersonFilmographyTabActivity;
import com.android.fourthwardcoder.movingpictures.activities.PersonPhotosActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;


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
    private static final int TEXT_FADE_DURATION = 500;

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
    int mPersonId;
    boolean mFetchData = false;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private NestedScrollView mNestedScrollView;
    FloatingActionButton mFavoritesFAB;

    //Known For Image and Text
    ImageView mKnownFor1ImageView;
    ImageView mKnownFor2ImageView;
    ImageView mKnownFor3ImageView;
    TextView mKnownFor1TextView;
    TextView mKnownFor2TextView;
    TextView mKnownFor3TextView;
    CardView mKnownForCardView;
    
    ArrayList<MediaBasic> mKnownForMovieList;

    private static final String ARG_ID = "id";

    public PersonDetailFragment() {
    }

    public static final PersonDetailFragment newInstance(int id) {

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ID,id);
        PersonDetailFragment fragment = new PersonDetailFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate() Inside");
        if(savedInstanceState != null) {
            mPerson = savedInstanceState.getParcelable(EXTRA_PERSON);
            mKnownForMovieList = savedInstanceState.getParcelableArrayList(EXTRA_MOVIE_LIST);
        }
        else {
            Bundle bundle = getArguments();
            mPersonId = bundle.getInt(ARG_ID);
            mFetchData = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;


        if(getActivity() instanceof PersonDetailActivity) {

            view = inflater.inflate(R.layout.fragment_person_detail, container, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
                toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white, null));
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "Back pressed");

                        //Kill this activity
                        getActivity().finish();
                    }
                });
            }
        } else {
            view = inflater.inflate(R.layout.fragment_person_detail_two_pane, container, false);
        }

        //Get CollapsingToolbarLayout
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
        //Get NestedScrollView;
        mNestedScrollView = (NestedScrollView)view.findViewById(R.id.scrollview);

        //Person Profile Image
        mProfileImageView = (ImageView) view.findViewById(R.id.profileImageView);
        //Person Name
        mNameTextView = (TextView) view.findViewById(R.id.detail_person_name_text_view);
        //Person Birth Date
        mBornDateTextView = (TextView) view.findViewById(R.id.bornDateTextView);
        //Person Birth Place
        mBornPlaceTextView = (TextView) view.findViewById(R.id.bornPlaceTextView);
        //Person Death Date
        mDeathDateTextView = (TextView) view.findViewById(R.id.deathDateTextView);

        //Person Biography Content
        mBiographyContentTextView = (ExpandableTextView) view.findViewById(R.id.person_biography_exp_text_view);

        mWebpageTextView = (TextView) view.findViewById(R.id.webPageTextView);
        //Person Filmography
        mFilmographyTextView = (TextView) view.findViewById(R.id.filmographyTextView);
        mFilmographyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), PersonFilmographyTabActivity.class);
                i.putExtra(EXTRA_ID, mPersonId);
                startActivity(i)
                ;
            }
        });

        mPhotosTextView = (TextView) view.findViewById(R.id.photosTextView);
        mPhotosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PersonPhotosActivity.class);
                i.putExtra(EXTRA_ID, mPersonId);
                i.putExtra(EXTRA_PERSON_NAME, mPerson.getName());
                startActivity(i);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getActivity().getWindow().getSharedElementEnterTransition().addListener(new ImageTransitionListener() {

                @Override
                public void onTransitionStart(Transition transition) {
                    mNameTextView.setAlpha(0f);
                    mFavoritesFAB.setVisibility(View.GONE);
                }
                @Override
                public void onTransitionEnd(Transition transition) {
                    Log.e(TAG,"Transition end. Scan in FAB");
                    mNameTextView.animate().setDuration(TEXT_FADE_DURATION).alpha(1f);
                    mFavoritesFAB.setVisibility(View.VISIBLE);

                    //Scale in FAB
                    if(getActivity() != null) {
                        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                                R.anim.scale_in_image);
                        mFavoritesFAB.startAnimation(scaleAnimation);
                    }
                }
            });
        }

        mFavoritesFAB = (FloatingActionButton) view.findViewById(R.id.favorites_fab);

        mFavoritesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPerson != null) {
                    String toastStr = "";
                    if (v.getTag().equals(false)) {
                        Log.e(TAG, "Set to favorite");
                        mFavoritesFAB.setTag(true);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.yellow));
                        toastStr = getString(R.string.added) + " " + mPerson.getName() + " "
                                + getString(R.string.to_favorites);
                        Util.addToFavoritesDb(getActivity(),mPerson.getContentValues());

                    } else {
                        Log.e(TAG, "remove from favorite");
                        mFavoritesFAB.setTag(false);
                        mFavoritesFAB.setColorFilter(getResources().getColor(R.color.white));
                        toastStr = getString(R.string.removed) + " " + mPerson.getName() + " "
                                + getString(R.string.from_favorites);
                        Util.removeFromFavoritesDb(getActivity(),mPerson.getId());
                    }
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            toastStr, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        View.OnClickListener knownForClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mKnownForMovieList != null) {
                    if (v.equals(mKnownFor1ImageView)) {
                        Util.startDetailActivity(getActivity(), mKnownForMovieList.get(0).getId(), ENT_TYPE_MOVIE, mKnownFor1ImageView);
                    } else if (v.equals(mKnownFor2ImageView)) {
                        Util.startDetailActivity(getActivity(), mKnownForMovieList.get(1).getId(), ENT_TYPE_MOVIE, mKnownFor2ImageView);
                    } else if (v.equals(mKnownFor3ImageView)) {
                        Util.startDetailActivity(getActivity(), mKnownForMovieList.get(2).getId(), ENT_TYPE_MOVIE, mKnownFor3ImageView);
                    }
                }
            }
        };
        //Get the 3 Top Billed Cast layouts and child views
        mKnownForCardView = (CardView) view.findViewById(R.id.knownForLayout);
        View cast1View = view.findViewById(R.id.person_movie_layout1);
        mKnownFor1ImageView = (ImageView) cast1View.findViewById(R.id.thumb_image_view);
        mKnownFor1ImageView.setOnClickListener(knownForClickListener);
        mKnownFor1TextView = (TextView) cast1View.findViewById(R.id.thumb_text_view);

        View cast2View = view.findViewById(R.id.person_movie_layout2);
        mKnownFor2ImageView = (ImageView) cast2View.findViewById(R.id.thumb_image_view);
        mKnownFor2ImageView.setOnClickListener(knownForClickListener);
        mKnownFor2TextView = (TextView) cast2View.findViewById(R.id.thumb_text_view);

        View cast3View = view.findViewById(R.id.person_movie_layout3);
        mKnownFor3ImageView = (ImageView) cast3View.findViewById(R.id.thumb_image_view);
        mKnownFor3ImageView.setOnClickListener(knownForClickListener);
        mKnownFor3TextView = (TextView) cast3View.findViewById(R.id.thumb_text_view);

        if(mFetchData) {
            Log.e(TAG,"Go fetch Person's data");
            getPerson(mPersonId);
        } else {
            //Got the entire Person object saved from instance state. Just set the layout.
            Log.e(TAG,"Already have Person's data, just set layouts");
            setLayout();
            setKnownForLayout();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putParcelable(EXTRA_PERSON, mPerson);
        savedInstanceState.putParcelableArrayList(EXTRA_MOVIE_LIST,mKnownForMovieList);
        super.onSaveInstanceState(savedInstanceState);
    }
    private void getPerson(int id) {

        Call<Person> call = MovieDbAPI.getMovieApiService().getPerson(id);

        call.enqueue(new retrofit2.Callback<Person>() {

            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {

                if(response.isSuccessful()) {

                    mPerson = response.body();
                    setLayout();
                } else {
                    Log.e(TAG,"Get Movie list call was not sucessful");
                    //parse the response to find the error. Display a message
//                    APIError error = ErrorUtils.parseError(response);
               //     Toast.makeText(getContext(),error.message(),Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Log.e(TAG, "onFailure() " + t.getMessage());
                Toast.makeText(getContext(),getContext().getString(R.string.toast_network_error),Toast.LENGTH_LONG);
            }
        });
    }

    private void getPersonsTopMovies() {

        Call<MovieList> call = MovieDbAPI.getMovieApiService().getPersonsTopMovies(mPersonId);

        call.enqueue(new retrofit2.Callback<MovieList>() {

            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse()");
                    mKnownForMovieList = ((ArrayList)response.body().getMovies());
                    
                    setKnownForLayout();
               
                } else {

                    //parse the response to find the error. Display a message
                  //  APIError error = ErrorUtils.parseError(response);
                   // Toast.makeText(getContext(),error.message(),Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });
    }

    private void setLayout() {


        if ((getActivity() != null) && (mPerson != null)) {

            Picasso.with(getActivity()).load(MovieDbAPI.getFullPosterPath(mPerson.getProfilePath())).into(mProfileImageView, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable)mProfileImageView.getDrawable()).getBitmap();

                    if(bitmap != null && getActivity() != null) {
                        Palette p = Palette.generate(bitmap, 12);
                        //   mMutedColor = p.getDarkMutedColor(0xFF333333);

                        //Set title and colors for collapsing toolbar
                        mCollapsingToolbarLayout.setTitle(mPerson.getName());
                        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

                        //Set content descriptioni for toolbar/title
                        mCollapsingToolbarLayout.setContentDescription(mPerson.getName());

                        //Set pallet colors when toolbar is collapsed
                        int primaryColor = getResources().getColor(R.color.appPrimaryColor);
                        int primaryDarkColor = getResources().getColor(R.color.appDarkPrimaryColor);
                        int accentColor = getResources().getColor(R.color.appAccentColor);
                        mCollapsingToolbarLayout.setContentScrimColor(p.getMutedColor(primaryColor));
                        mCollapsingToolbarLayout.setStatusBarScrimColor(p.getDarkMutedColor(primaryDarkColor));

                        startPostponedEnterTransition();

                    }
                }

                @Override
                public void onError() {
                       startPostponedEnterTransition();

                }
            });

            mNameTextView.setText(mPerson.getName());

            //Format birthday into form Jan 1, 2016
            if(mPerson.getBirthday() != null) {
                if (!(mPerson.getBirthday().equals(""))) {
                    String strBirthDay = Util.reverseDateString(mPerson.getBirthday());

                    Spanned bornDate = Html.fromHtml("<b>" + getString(R.string.born) + "</b>" + " " +
                            strBirthDay + " " + getString(R.string.age,getAge(mPerson.getBirthday())));


                    mBornDateTextView.setText(bornDate);
                }
            }

            mBornPlaceTextView.setText(mPerson.getPlaceOfBirth());

            if(mPerson.getDeathday() != null) {
                if (!(mPerson.getDeathday()).equals("")) {
                    Spanned deathDate = Html.fromHtml("<b>" + getString(R.string.death) + "</b>" + " " +
                            Util.reverseDateString(mPerson.getDeathday()));
                    mDeathDateTextView.setText(deathDate);
                } else {
                    mDeathDateTextView.setVisibility(View.GONE);
                }
            }

            Spanned biography = Html.fromHtml("<b>" + getString(R.string.biography) + "</b>" + " " +
                    mPerson.getBiography());
            mBiographyContentTextView.setText(biography);

            if(mPerson.getHomepage() != null){
                if (!(mPerson.getHomepage()).equals("")) {
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
                } else {
                    mWebpageTextView.setVisibility(View.GONE);
                }
            }

            //If we are fetching data on first tim through, go get the known for movies. Otherwise
            //we arleayd have them saved so just set the layout.
            if(mFetchData)
                getPersonsTopMovies();

            else
                setKnownForLayout();

            //See if this is a favorite movie and set the state of the star button
            Util.setFavoritesButton(mFavoritesFAB,getActivity(),mPerson.getId());
        }

    }

    private void startPostponedEnterTransition() {
        Log.e(TAG,"startPostponedEnterTransition() Inside");
        mProfileImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.e(TAG,"onPreDraw(): Start postponed enter transition!!!!");
                mProfileImageView.getViewTreeObserver().removeOnPreDrawListener(this);

                //Must call this inside a PreDrawListener or the Enter Transition will not work
                //Need to make sure imageview is ready before starting transition.
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
    }
    private void setKnownForLayout() {

        if (mKnownForMovieList != null) {
            Log.e(TAG,"set Known For List. Movie list not null with size = " + mKnownForMovieList.size());
            if (mKnownForMovieList.size() >= 3) {
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(0).getPosterPath(), mKnownFor1ImageView);
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(1).getPosterPath(), mKnownFor2ImageView);
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(2).getPosterPath(), mKnownFor3ImageView);
                mKnownFor1TextView.setText(mKnownForMovieList.get(0).getTitle());
                mKnownFor2TextView.setText(mKnownForMovieList.get(1).getTitle());
                mKnownFor3TextView.setText(mKnownForMovieList.get(2).getTitle());
            } else if (mKnownForMovieList.size() == 2) {
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(0).getPosterPath(), mKnownFor1ImageView);
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(1).getPosterPath(), mKnownFor2ImageView);
                mKnownFor1TextView.setText(mKnownForMovieList.get(0).getTitle());
                mKnownFor2TextView.setText(mKnownForMovieList.get(1).getTitle());
            } else if (mKnownForMovieList.size() == 3) {
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(0).getPosterPath(), mKnownFor1ImageView);
                mKnownFor1TextView.setText(mKnownForMovieList.get(0).getTitle());
            }
            else {
                //Cast size is 0. Don't show cast card.
                mKnownForCardView.setVisibility(View.GONE);
            }
        } else {
            //Did not return any cast. Don't show cast card.
            mKnownForCardView.setVisibility(View.GONE);
        }
    }


    private int getAge(String strBirthDate) {

        Log.e(TAG,"Got string birthdate = " + strBirthDate);

        String[] dobArray = strBirthDate.split("-");

        //Get data of birth calendar
        Calendar dob = Calendar.getInstance();
        dob.set(Integer.parseInt(dobArray[0]),Integer.parseInt(dobArray[1]),Integer.parseInt(dobArray[2]));

        //Get today's calendar
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;

    }


}
