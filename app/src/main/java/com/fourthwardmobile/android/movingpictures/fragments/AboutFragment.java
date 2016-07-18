package com.fourthwardmobile.android.movingpictures.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourthwardmobile.android.movingpictures.BuildConfig;
import com.fourthwardmobile.android.movingpictures.R;


public class AboutFragment extends DialogFragment {

    private static final String TAG = AboutFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView versionTextView = (TextView)view.findViewById(R.id.about_version_textview);

        versionTextView.setText(getString(R.string.version,BuildConfig.VERSION_NAME));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

}
