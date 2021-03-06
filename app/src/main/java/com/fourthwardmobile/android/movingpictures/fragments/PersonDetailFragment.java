package com.fourthwardmobile.android.movingpictures.fragments;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourthwardmobile.android.movingpictures.MovingPicturesApplication;
import com.fourthwardmobile.android.movingpictures.R;
import com.fourthwardmobile.android.movingpictures.activities.PersonDetailActivity;
import com.fourthwardmobile.android.movingpictures.activities.SearchableActivity;
import com.fourthwardmobile.android.movingpictures.helpers.ImageTransitionListener;
import com.fourthwardmobile.android.movingpictures.helpers.MovieDbAPI;
import com.fourthwardmobile.android.movingpictures.helpers.Util;
import com.fourthwardmobile.android.movingpictures.interfaces.Constants;
import com.fourthwardmobile.android.movingpictures.models.MediaBasic;
import com.fourthwardmobile.android.movingpictures.models.MediaList;
import com.fourthwardmobile.android.movingpictures.models.Person;
import com.fourthwardmobile.android.movingpictures.activities.PersonFilmographyTabActivity;
import com.fourthwardmobile.android.movingpictures.activities.PersonPhotosActivity;
import com.fourthwardmobile.android.movingpictures.network.NetworkService;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Class PersonDetailFragment
 * Author: Chris Hare
 * Create: 8/25/15
 * <p/>
 * Fragment to hold the details of a person's info
 */
public class PersonDetailFragment extends BaseDetailFragment implements Constants,  Toolbar.OnMenuItemClickListener {

    /*********************************************************************/
    /*                          Constants                                */
    /*********************************************************************/
    private static final String TAG = PersonDetailFragment.class.getSimpleName();
    private static final int TEXT_FADE_DURATION = 500;

    /*********************************************************************/
    /*                          Local Data                               */
    /*********************************************************************/
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

    //Known For Image and Text
    FrameLayout mKnownFor1FrameView;
    FrameLayout mKnownFor2FrameView;
    FrameLayout mKnownFor3FrameView;
    ImageView mKnownFor1ImageView;
    ImageView mKnownFor2ImageView;
    ImageView mKnownFor3ImageView;
    TextView mKnownFor1TextView;
    TextView mKnownFor2TextView;
    TextView mKnownFor3TextView;
    CardView mKnownForCardView;


    ArrayList<MediaBasic> mKnownForMovieList;

    private static final String ARG_ID = "id";

    private NetworkService mNetworkService;
    private Subscription mPersonSubscription;
    private Subscription mPersonTopMoviesSubscription;
    private CompositeSubscription mCompositeSubscription;



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

        //Get reference to applications network service interface
        mNetworkService = ((MovingPicturesApplication)getActivity().getApplication()).getNetworkService();

        //Create Composition Subscription to store Rx Subscriptions that will be destoryed when the
        //fragment is destroyed
        mCompositeSubscription = new CompositeSubscription();

        if(savedInstanceState != null) {
            mPerson = savedInstanceState.getParcelable(EXTRA_PERSON);
            mKnownForMovieList = savedInstanceState.getParcelableArrayList(EXTRA_MOVIE_LIST);
        }
        else {
            Bundle bundle = getArguments();
            mPersonId = bundle.getInt(ARG_ID);
            mFetchData = true;
        }

        //This is a Person type
        mEntType = ENT_TYPE_PERSON;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;


        if(getActivity() instanceof PersonDetailActivity) {

            //Set Options menu if we are not in two pane mode
            setHasOptionsMenu(true);

            //Don't show a title if we don't have an image to display for the person
            getActivity().setTitle("");

            view = inflater.inflate(com.fourthwardmobile.android.movingpictures.R.layout.fragment_person_detail, container, false);
            mToolbar = (Toolbar) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mToolbar = (Toolbar) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.toolbar);
                mToolbar.setNavigationIcon(getResources().getDrawable(com.fourthwardmobile.android.movingpictures.R.drawable.ic_arrow_back_white, null));
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "Back pressed");

                        //Kill this activity
                        getActivity().finish();
                    }
                });
            }
            //Set Toolbar so we can see menu options
            ((PersonDetailActivity) getActivity()).setSupportActionBar(mToolbar);
        } else {
            view = inflater.inflate(com.fourthwardmobile.android.movingpictures.R.layout.fragment_person_detail_two_pane, container, false);
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

            mToolbar.inflateMenu(R.menu.menu_share);
            mToolbar.setOnMenuItemClickListener(this);
        }

        //Get CollapsingToolbarLayout
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.collapsing_toolbar);

        //Person Profile Image
        mBackdropImageView = (ImageView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.profileImageView);
        //Person Name
        mNameTextView = (TextView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.detail_person_name_text_view);
        //Person Birth Date
        mBornDateTextView = (TextView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.bornDateTextView);
        //Person Birth Place
        mBornPlaceTextView = (TextView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.bornPlaceTextView);
        //Person Death Date
        mDeathDateTextView = (TextView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.deathDateTextView);

        //Person Biography Content
        mBiographyContentTextView = (ExpandableTextView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.person_biography_exp_text_view);

        mWebpageTextView = (TextView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.webPageTextView);
        //Person Filmography
        mFilmographyTextView = (TextView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.filmographyTextView);
        mFilmographyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), PersonFilmographyTabActivity.class);
                i.putExtra(EXTRA_ID, mPersonId);
                startActivity(i)
                ;
            }
        });

        mPhotosTextView = (TextView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.photosTextView);
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
                                com.fourthwardmobile.android.movingpictures.R.anim.scale_in_image);
                        mFavoritesFAB.startAnimation(scaleAnimation);
                    }
                }
            });
        }

        mFavoritesFAB = (FloatingActionButton) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.favorites_fab);

        mFavoritesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPerson != null) {
                    String toastStr = "";
                    if (v.getTag().equals(false)) {
                        Log.e(TAG, "Set to favorite");
                        mFavoritesFAB.setTag(true);
                        mFavoritesFAB.setColorFilter(getResources().getColor(com.fourthwardmobile.android.movingpictures.R.color.yellow));
                        toastStr = getString(com.fourthwardmobile.android.movingpictures.R.string.added) + " " + mPerson.getName() + " "
                                + getString(com.fourthwardmobile.android.movingpictures.R.string.to_favorites);
                        Util.addToFavoritesDb(getActivity(),mPerson.getContentValues());

                    } else {
                        Log.e(TAG, "remove from favorite");
                        mFavoritesFAB.setTag(false);
                        mFavoritesFAB.setColorFilter(getResources().getColor(com.fourthwardmobile.android.movingpictures.R.color.white));
                        toastStr = getString(com.fourthwardmobile.android.movingpictures.R.string.removed) + " " + mPerson.getName() + " "
                                + getString(com.fourthwardmobile.android.movingpictures.R.string.from_favorites);
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
                    if (v.equals(mKnownFor1FrameView)) {
                        Util.startDetailActivity(getActivity(), mKnownForMovieList.get(0).getId(), ENT_TYPE_MOVIE, mKnownFor1FrameView);
                    } else if (v.equals(mKnownFor2FrameView)) {
                        Util.startDetailActivity(getActivity(), mKnownForMovieList.get(1).getId(), ENT_TYPE_MOVIE, mKnownFor2FrameView);
                    } else if (v.equals(mKnownFor3FrameView)) {
                        Util.startDetailActivity(getActivity(), mKnownForMovieList.get(2).getId(), ENT_TYPE_MOVIE, mKnownFor3FrameView);
                    }
                }
            }
        };
        //Get the 3 Top Billed Cast layouts and child views
        mKnownForCardView = (CardView) view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.knownForLayout);
        View cast1View = view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.person_movie_layout1);
        mKnownFor1ImageView = (ImageView) cast1View.findViewById(com.fourthwardmobile.android.movingpictures.R.id.thumb_image_view);
        mKnownFor1FrameView = (FrameLayout)cast1View.findViewById(R.id.thumb_frame);
        mKnownFor1FrameView.setOnClickListener(knownForClickListener);
        mKnownFor1TextView = (TextView) cast1View.findViewById(com.fourthwardmobile.android.movingpictures.R.id.thumb_text_view);

        View cast2View = view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.person_movie_layout2);
        mKnownFor2ImageView = (ImageView) cast2View.findViewById(com.fourthwardmobile.android.movingpictures.R.id.thumb_image_view);
        mKnownFor2FrameView = (FrameLayout)cast2View.findViewById(R.id.thumb_frame);
        mKnownFor2FrameView.setOnClickListener(knownForClickListener);
        mKnownFor2TextView = (TextView) cast2View.findViewById(com.fourthwardmobile.android.movingpictures.R.id.thumb_text_view);

        View cast3View = view.findViewById(com.fourthwardmobile.android.movingpictures.R.id.person_movie_layout3);
        mKnownFor3ImageView = (ImageView) cast3View.findViewById(com.fourthwardmobile.android.movingpictures.R.id.thumb_image_view);
        mKnownFor3FrameView = (FrameLayout)cast3View.findViewById(R.id.thumb_frame);
        mKnownFor3FrameView.setOnClickListener(knownForClickListener);
        mKnownFor3TextView = (TextView) cast3View.findViewById(com.fourthwardmobile.android.movingpictures.R.id.thumb_text_view);

        if(mFetchData) {
            Log.e(TAG,"Go fetch Person's data");
            getPerson();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Log.e(TAG,"onOptionsItemSelected() Fragment");
        switch (item.getItemId()) {
            case R.id.action_share:
                Log.e(TAG,"Menu share click");
                sharePerson();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Used when calling menu items from Fragment when in two pane/tablet mode. Otherwise
     * it goes through the normal onOptionsItemSelected method
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.e(TAG,"onMenuItemClick()");

        switch(item.getItemId()) {
            case R.id.action_share:
                Log.e(TAG,"Share click in fragment");
                sharePerson();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Clean up Rx Subscriptions
        mCompositeSubscription.clear();
    }

    private void getPerson() {

        Observable<Person> personObservable = mNetworkService.getMovieApiService().getPerson(mPersonId);

        mPersonSubscription = personObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Person>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        //Want to make sure activity is valid before showing any toasts
                        if(getActivity() != null) {
                            mNetworkService.processNetworkError(getContext(),e);
                        }
                    }

                    @Override
                    public void onNext(Person person) {
                        mPerson = person;
                        setLayout();
                    }
                });

        mCompositeSubscription.add(mPersonSubscription);

    }
//    private void getPersonsTopMovies() {
//
//        Call<MediaList> call = MovieDbAPI.getMovieApiService().getPersonsTopMovies(mPersonId);
//
//        call.enqueue(new retrofit2.Callback<MediaList>() {
//
//            @Override
//            public void onResponse(Call<MediaList> call, Response<MediaList> response) {
//
//                if (response.isSuccessful()) {
//                    Log.e(TAG, "onResponse()");
//                    mKnownForMovieList = ((ArrayList)response.body().getMediaResults());
//
//                    setKnownForLayout();
//
//                } else {
//
//                    //parse the response to find the error. Display a message
//                  //  APIError error = ErrorUtils.parseError(response);
//                   // Toast.makeText(getContext(),error.message(),Toast.LENGTH_LONG);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<MediaList> call, Throwable t) {
//
//            }
//        });
//    }

    private void getPersonsTopMovies() {

        Observable<MediaList> topMoviesObservable = mNetworkService.getMovieApiService().getPersonsTopMovies(mPersonId);

        mPersonTopMoviesSubscription = topMoviesObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MediaList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        if(getActivity() != null) {
                            mNetworkService.processNetworkError(getContext(),e);
                        }
                    }

                    @Override
                    public void onNext(MediaList mediaList) {
                        mKnownForMovieList = (ArrayList)mediaList.getMediaResults();
                        setKnownForLayout();
                    }
                });

        mCompositeSubscription.add(mPersonTopMoviesSubscription);

    }



    private void setLayout() {


        if ((getActivity() != null) && (mPerson != null)) {

            Picasso.with(getActivity()).load(MovieDbAPI.getFullPosterPath(mPerson.getProfilePath()))
                    .placeholder(R.drawable.person_thumbnail)
                    .into(mBackdropImageView, new Callback() {
                @Override
                public void onSuccess() {

                    setPaletteColors(mPerson.getName());
                    startPostponedEnterTransition(mBackdropImageView);
                }

                @Override
                public void onError() {
                       setPaletteColors(mPerson.getName());
                       startPostponedEnterTransition(mBackdropImageView);

                }
            });

            mNameTextView.setText(mPerson.getName());

            //Format birthday into form Jan 1, 2016
            if (mPerson.getBirthday() != null) {
                if (!(mPerson.getBirthday().equals(""))) {
                    String strBirthDay = Util.reverseDateString(mPerson.getBirthday());

                    Spanned bornDate;
                    if (mPerson.getDeathday() == null || mPerson.getDeathday().equals("")) {

                        int age = getAge(mPerson.getBirthday());
                        //Only show age if we were able to calculate it.
                        if (age > 0) {
                            bornDate = Html.fromHtml("<b>" + getString(com.fourthwardmobile.android.movingpictures.R.string.born) + "</b>" + " " +
                                    strBirthDay + " " + getString(com.fourthwardmobile.android.movingpictures.R.string.age, age));
                        } else {
                            bornDate = Html.fromHtml("<b>" + getString(com.fourthwardmobile.android.movingpictures.R.string.born) + "</b>" + " " +
                                    strBirthDay);
                        }
                    } else {
                        bornDate = Html.fromHtml("<b>" + getString(com.fourthwardmobile.android.movingpictures.R.string.born) + "</b>" + " " +
                                strBirthDay);
                    }

                    mBornDateTextView.setText(bornDate);
                }
            }

            mBornPlaceTextView.setText(mPerson.getPlaceOfBirth());

            if(mPerson.getDeathday() != null) {
                if (!(mPerson.getDeathday()).equals("")) {

                    String strDeathDay = Util.reverseDateString(mPerson.getDeathday());
                    Spanned deathDate = Html.fromHtml("<b>" + getString(com.fourthwardmobile.android.movingpictures.R.string.death) + "</b>" + " " +
                            strDeathDay + " " + getString(com.fourthwardmobile.android.movingpictures.R.string.age, getDeathAge(mPerson.getBirthday(),mPerson.getDeathday())));
                    mDeathDateTextView.setText(deathDate);
                } else {
                    mDeathDateTextView.setVisibility(View.GONE);
                }
            }

            if(mPerson.getBiography() != null) {
                Spanned biography = Html.fromHtml("<b>" + getString(com.fourthwardmobile.android.movingpictures.R.string.biography) + "</b>" + " " +
                        mPerson.getBiography());
                mBiographyContentTextView.setText(biography);
            } else {
                mBiographyContentTextView.setVisibility(View.GONE);
            }


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

    private void setKnownForLayout() {

        if (mKnownForMovieList != null) {
            Log.e(TAG,"set Known For List. Movie list not null with size = " + mKnownForMovieList.size());
            if (mKnownForMovieList.size() >= 3) {
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(0).getPosterPath(), mKnownFor1ImageView,ENT_TYPE_MOVIE);
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(1).getPosterPath(), mKnownFor2ImageView,ENT_TYPE_MOVIE);
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(2).getPosterPath(), mKnownFor3ImageView,ENT_TYPE_MOVIE);
                mKnownFor1TextView.setText(mKnownForMovieList.get(0).getTitle());
                mKnownFor2TextView.setText(mKnownForMovieList.get(1).getTitle());
                mKnownFor3TextView.setText(mKnownForMovieList.get(2).getTitle());
            } else if (mKnownForMovieList.size() == 2) {
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(0).getPosterPath(), mKnownFor1ImageView,ENT_TYPE_MOVIE);
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(1).getPosterPath(), mKnownFor2ImageView,ENT_TYPE_MOVIE);
                mKnownFor1TextView.setText(mKnownForMovieList.get(0).getTitle());
                mKnownFor2TextView.setText(mKnownForMovieList.get(1).getTitle());
            } else if (mKnownForMovieList.size() == 3) {
                Util.loadPosterThumbnail(getContext(),mKnownForMovieList.get(0).getPosterPath(), mKnownFor1ImageView,ENT_TYPE_MOVIE);
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

        if(dobArray.length == 3) {
            //Get data of birth calendar
            Calendar dob = Calendar.getInstance();
            dob.set(Integer.parseInt(dobArray[0]), Integer.parseInt(dobArray[1]), Integer.parseInt(dobArray[2]));

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
        else {
            return 0;
        }
    }

    private int getDeathAge(String strBirthDate,String strDeathDate) {

        Log.e(TAG,"Got string birthdate = " + strBirthDate);

        String[] dobArray = strBirthDate.split("-");
        String[] deathArray = strDeathDate.split("-");

        //Get data of birth calendar
        Calendar dob = Calendar.getInstance();
        dob.set(Integer.parseInt(dobArray[0]),Integer.parseInt(dobArray[1]),Integer.parseInt(dobArray[2]));

        int deathYear = Integer.parseInt(deathArray[0]);
        int deathMonth = Integer.parseInt(deathArray[1]);

        //Get today's calendar
        Calendar today = Calendar.getInstance();
        int age = deathYear - dob.get(Calendar.YEAR);
        if (deathMonth < dob.get(Calendar.MONTH)) {
            age--;
        } else if (deathMonth == dob.get(Calendar.MONTH)
                && deathMonth < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;

    }

    private void sharePerson() {

        Util.shareMedia(getActivity(),ENT_TYPE_PERSON, mPerson.getId(),getString(R.string.share_person_subject,mPerson.getName()));
    }
}
