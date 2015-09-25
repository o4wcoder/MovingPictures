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
import com.android.fourthwardcoder.popularmovies.adapters.CreditListAdapter;
import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Credit;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
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
    FilmographyTabName mTabType;

    public PersonFilmographyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Bundle bundle = getArguments();
        int i = bundle.getInt(EXTRA_FILMOGRAPHY_TAB);
        mTabType = FilmographyTabName.values()[i];

        Log.e(TAG, "In filmography fragment with tab " + mTabType);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_listview, container, false);

        final int personId = getActivity().getIntent().getIntExtra(EXTRA_PERSON_ID, 0);

        mListView = (ListView)view.findViewById(R.id.listView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Credit credit = (Credit) mAdapter.getItem(position);
                Intent i = new Intent(getActivity(),MovieDetailActivity.class);
                i.putExtra(EXTRA_MOVIE_ID, credit.getId());
                startActivity(i);

            }
        });

        if(mListView != null) {

            new FetchFilmographyTask().execute(personId);
        }
        return view;

    }

    private class FetchFilmographyTask extends AsyncTask<Integer,Void,ArrayList<Credit>> {


        @Override
        protected ArrayList<Credit> doInBackground(Integer... params) {

            //Get ID of person
            int personId = params[0];


            return MovieDbAPI.getPersonCreditList(personId,mTabType);
        }

        @Override
        protected void onPostExecute(ArrayList<Credit> creditList) {

            if(creditList != null) {
                mAdapter = new CreditListAdapter(getActivity(), creditList,true);
                mListView.setAdapter(mAdapter);
            }
        }

    }
}
