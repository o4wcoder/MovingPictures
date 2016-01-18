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
import android.widget.Toast;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.activities.MovieDetailActivity;
import com.android.fourthwardcoder.popularmovies.activities.TvDetailActivity;
import com.android.fourthwardcoder.popularmovies.adapters.CreditListAdapter;
import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.helpers.Util;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Credit;

import java.util.ArrayList;


/**
 * Class PersonFilmographyFragment
 * Author: Chris Hare
 * Created: 8/15/15
 * <p/>
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
    int mEntType;
    int mPersonId;

    public static PersonFilmographyFragment newInstance(int entType, int personId) {

        //Store data in bungle for the fragment
        Bundle args = new Bundle();
        //Store entertainment type; movie or tv
        args.putInt(EXTRA_ENT_TYPE, entType);
        args.putInt(EXTRA_PERSON_ID, personId);
        //Create Fragment and store arguments to it.
        PersonFilmographyFragment fragment = new PersonFilmographyFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        //Check if we've rotated
        if (savedInstance != null) {
            mEntType = savedInstance.getInt(EXTRA_ENT_TYPE);
            mPersonId = savedInstance.getInt(EXTRA_PERSON_ID);
        } else {
            Bundle bundle = getArguments();
            mEntType = bundle.getInt(EXTRA_ENT_TYPE);
            mPersonId = bundle.getInt(EXTRA_PERSON_ID);
        }
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
                if (mEntType == ENT_TYPE_MOVIE) {
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

            if(Util.isNetworkAvailable(getActivity())) {
                new FetchFilmographyTask().execute(mPersonId, mEntType);
            }
            else {
                Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                connectToast.show();
            }
        }
        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(EXTRA_ENT_TYPE, mEntType);
        savedInstanceState.putInt(EXTRA_PERSON_ID, mPersonId);
        super.onSaveInstanceState(savedInstanceState);
    }

    /****************************************************************************/
    /*                              Inner Classes                               */

    /****************************************************************************/
    private class FetchFilmographyTask extends AsyncTask<Integer, Void, ArrayList<Credit>> {


        @Override
        protected ArrayList<Credit> doInBackground(Integer... params) {

            //Get ID of person
            int personId = params[0];
            //Get Entertainment type; Movie or TV
            int entType = params[1];

            //return list of a person credits from a movie or tv show
            return MovieDbAPI.getPersonCreditList(personId, entType);
        }

        @Override
        protected void onPostExecute(ArrayList<Credit> creditList) {

            if ((getActivity() != null) && (creditList != null)) {

                mAdapter = new CreditListAdapter(getActivity(), creditList, true);
                mListView.setAdapter(mAdapter);
            }
        }

    }
}



