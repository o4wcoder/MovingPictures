package com.android.fourthwardcoder.popularmovies.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.activities.MovieDetailActivity;
import com.android.fourthwardcoder.popularmovies.activities.TvDetailActivity;
import com.android.fourthwardcoder.popularmovies.adapters.CreditListAdapter;
import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Credit;

import java.util.ArrayList;


/**
 * Class PersonFilmographyFragment
 * Author: Chris Hare
 * Created: 8/15/15
 *
 * Display's a listview of Movies/TV of a person
 */
public class PersonFilmographyFragment extends Fragment implements Constants {

    /********************************************************************/
    /*                         Constants                                */
    /********************************************************************/
    private static final String TAG = PersonFilmographyFragment.class.getSimpleName();

    /********************************************************************/
    /*                         Local Data                               */
    /********************************************************************/
    ListView mListView;
    CreditListAdapter mAdapter;
    int mTabType;
    int mPersonId;

    public PersonFilmographyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);


//        if(savedInstance != null) {
//            mTabType = savedInstance.getInt(EXTRA_FILMOGRAPHY_TAB);
//            Log.e(TAG,"Got savedInstance tab position " + mTabType);
//            mPersonId = savedInstance.getInt(EXTRA_PERSON_ID);
//            Log.e(TAG,"Got savedInstance person id " + mPersonId);
//        }
//        else {
            Bundle bundle = getArguments();
            mTabType = bundle.getInt(EXTRA_FILMOGRAPHY_TAB);
            Log.e(TAG,"Got bundle tab position " + mTabType);


            mPersonId = getActivity().getIntent().getIntExtra(EXTRA_PERSON_ID, 0);

            Log.e(TAG,"Got intent person id " + mPersonId);
            if(mPersonId == 0) {
                mPersonId = bundle.getInt(EXTRA_PERSON_ID,0);
                Log.e(TAG,"Person id was 0 from intent. Trying from bundle " + mPersonId);
            }
       // }
        Log.e(TAG, "In filmography fragment with tab " + mTabType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_listview, container, false);



        mListView = (ListView) view.findViewById(R.id.listView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Credit credit = (Credit) mAdapter.getItem(position);
                Intent i;
                if (mTabType == TYPE_MOVIE) {
                    i = new Intent(getActivity(), MovieDetailActivity.class);
                    i.putExtra(EXTRA_MOVIE_ID, credit.getId());
                } else {
                    i = new Intent(getActivity(), TvDetailActivity.class);
                    i.putExtra(EXTRA_TV_ID, credit.getId());
                }
                startActivity(i);

            }
        });

        if (mListView != null) {

            new FetchFilmographyTask().execute(mPersonId);
        }
        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(EXTRA_FILMOGRAPHY_TAB, mTabType);
        savedInstanceState.putInt(EXTRA_PERSON_ID,mPersonId);
        super.onSaveInstanceState(savedInstanceState);
    }

    private class FetchFilmographyTask extends AsyncTask<Integer, Void, ArrayList<Credit>> {


        @Override
        protected ArrayList<Credit> doInBackground(Integer... params) {

            //Get ID of person
            int personId = params[0];

            Log.e(TAG,"Calling movieDB with person " + personId + " tab " + mTabType);
            return MovieDbAPI.getPersonCreditList(personId, mTabType);
        }

        @Override
        protected void onPostExecute(ArrayList<Credit> creditList) {

            if ((getActivity() != null) && (creditList != null)) {
                Log.e(TAG,"Credit list size " + creditList.size());
                mAdapter = new CreditListAdapter(getActivity(), creditList, true);
                mListView.setAdapter(mAdapter);
            }
        }

    }
}



