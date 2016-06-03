package com.android.fourthwardcoder.movingpictures.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.activities.PersonDetailActivity;
import com.android.fourthwardcoder.movingpictures.adapters.CreditListAdapter;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Credit;

import java.util.ArrayList;

/**
 * Class CastListFragment
 * Author: Chris Hare
 * Created: 9/25/15
 * <p/>
 * Fragment to hold the Cast List of a Movie or TV show
 */
public class CastListFragment extends Fragment implements Constants {

    /********************************************************************/
    /*                         Constants                                */
    /********************************************************************/
    private static final String TAG = PersonFilmographyFragment.class.getSimpleName();

    /********************************************************************/
    /*                         Local Data                               */
    /********************************************************************/
    ListView mListView;
    CreditListAdapter mAdapter;

    public CastListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_listview, container, false);

        Intent i = getActivity().getIntent();
        final int movieId = i.getIntExtra(EXTRA_MOVIE_ID, 0);
        final int entType = i.getIntExtra(EXTRA_ENT_TYPE, 0);

        getActivity().setTitle(getString(R.string.cast_toolbar) + " " + i.getStringExtra(EXTRA_TITLE));

        mListView = (ListView) view.findViewById(R.id.listView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Credit credit = (Credit) mAdapter.getItem(position);
                Intent i = new Intent(getActivity(), PersonDetailActivity.class);
                i.putExtra(EXTRA_PERSON_ID, credit.getId());
                startActivity(i);
            }
        });

        if (mListView != null) {

            if(Util.isNetworkAvailable(getActivity())) {
                new FetchCastTask().execute(movieId, entType);
            }
            else {
                Toast connectToast = Toast.makeText(getActivity().getApplicationContext(),
                        getString(R.string.toast_network_error), Toast.LENGTH_LONG);
                connectToast.show();
            }
        }
        return view;
    }

    private class FetchCastTask extends AsyncTask<Integer, Void, ArrayList<Credit>> {


        @Override
        protected ArrayList<Credit> doInBackground(Integer... params) {

            int id = params[0];
            int entType = params[1];

            if (entType == ENT_TYPE_MOVIE)
                return MovieDbAPI.getMovieCastList(id);
            else
                return MovieDbAPI.getTvCastList(id);
        }

        @Override
        protected void onPostExecute(ArrayList<Credit> creditList) {

            if ((getActivity() != null) && (creditList != null)) {
                mAdapter = new CreditListAdapter(getActivity(), creditList, false);
                mListView.setAdapter(mAdapter);
            }
        }
    }
}
