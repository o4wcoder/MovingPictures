package com.android.fourthwardcoder.popularmovies.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.fourthwardcoder.popularmovies.R;
import com.android.fourthwardcoder.popularmovies.activities.PersonDetailActivity;
import com.android.fourthwardcoder.popularmovies.adapters.CreditListAdapter;
import com.android.fourthwardcoder.popularmovies.helpers.MovieDbAPI;
import com.android.fourthwardcoder.popularmovies.interfaces.Constants;
import com.android.fourthwardcoder.popularmovies.models.Credit;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieCastActivityFragment extends Fragment implements Constants {

    /********************************************************************/
    /*                         Constants                                */
    /********************************************************************/
    private static final String TAG = PersonFilmographyActivityFragment.class.getSimpleName();

    /********************************************************************/
    /*                         Local Data                               */
    /********************************************************************/
    ListView mListView;
    CreditListAdapter mAdapter;

    public MovieCastActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_listview, container, false);

        final int movieId = getActivity().getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);

        mListView = (ListView)view.findViewById(R.id.listView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Credit credit = (Credit) mAdapter.getItem(position);
                Intent i = new Intent(getActivity(),PersonDetailActivity.class);
                i.putExtra(EXTRA_PERSON_ID, credit.getId());
                startActivity(i);
            }
        });

        if(mListView != null) {

            new FetchCastTask().execute(movieId);
        }
        return view;
    }

    private class FetchCastTask extends AsyncTask<Integer,Void,ArrayList<Credit>> {


        @Override
        protected ArrayList<Credit> doInBackground(Integer... params) {

            int movieId = params[0];

            return MovieDbAPI.getCastList(movieId);
        }

        @Override
        protected void onPostExecute(ArrayList<Credit> creditList) {

            if (creditList != null) {
                mAdapter = new CreditListAdapter(getActivity(), creditList, false);
                mListView.setAdapter(mAdapter);
            }
        }
    }


}
