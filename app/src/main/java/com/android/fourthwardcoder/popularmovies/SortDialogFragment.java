package com.android.fourthwardcoder.popularmovies;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;


public class SortDialogFragment extends DialogFragment {

    private static final String TAG = SortDialogFragment.class.getSimpleName();
    public static final String EXTRA_SORT = "com.android.fourthwardcoder.popularmovies.extra_sort";


    String mSortOrder;

    public static SortDialogFragment newInstance(String sortOrder) {
        // Required empty public constructor
        Bundle args = new Bundle();
        args.putString(EXTRA_SORT,sortOrder);
        Log.e(TAG,"SorDialogFragment with sort " + sortOrder);

        SortDialogFragment fragment = new SortDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        mSortOrder = (String)getArguments().getString(EXTRA_SORT);

        /*
        //Create a Calendar to get the time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTime);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
*/
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_sort, null);

       /*
        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);
        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hour, int min) {

                //Log.d(TAG,"Got Hour " + hour);
                //retrieving the original date from the mTime value with a calendar
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mTime);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE,min);

                //Translating hourOfDay & minute into a Date object using a calendar, date keeps
                //the same
                mTime = calendar.getTime();
                //Log.d(TAG,"Hour in data object " + mTime.getHours());


                //Update arguments to preserve selected value on rotation
                getArguments().putSerializable(EXTRA_TIME, mTime);
            }

        });
        */

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.dialog_sort)
                .setSingleChoiceItems(R.array.sort_list, getSortPosition(), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "Got dialog sort item " + which);
                        Resources res = getResources();
                        String[] sortList = res.getStringArray(R.array.sort_url_list);

                        mSortOrder = sortList[which];

                        sendResult(Activity.RESULT_OK);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_CANCELED);

                    }
                })
                .create();

    }

    /***************************************************************/
	/*                   Private Methods                           */
    /***************************************************************/
    /**
     * Sends the result of the dialog back to calling fragment
     *
     * @param resultCode code of result of dialog. In this case "OK"
     */
    private void sendResult(int resultCode) {
        if(getTargetFragment() == null)
            return;

        //Create intent and put extra on it
        Intent i = new Intent();
        i.putExtra(EXTRA_SORT,mSortOrder );

        //Send result to PopularMoviesMain Fragment
        //Request code to tell the target who is returning hte result
        //result code to determine what action to take
        //An intent that can have extra data
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    private int getSortPosition() {

        Resources res = getResources();
        String[] sortList = res.getStringArray(R.array.sort_url_list);

        for(int i = 0; i < sortList.length; i++) {
            Log.e(TAG,"Sort list at " + i + " " + sortList[i]);
            if(sortList[i].equals(mSortOrder))
                return i;
        }

        //could not find the sort order, return 0 "popularity"
        return 0;
    }

}
