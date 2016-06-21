package com.android.fourthwardcoder.movingpictures.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fourthwardcoder.movingpictures.R;
import com.android.fourthwardcoder.movingpictures.helpers.MovieDbAPI;
import com.android.fourthwardcoder.movingpictures.helpers.Util;
import com.android.fourthwardcoder.movingpictures.models.Cast;
import com.android.fourthwardcoder.movingpictures.models.Credits;
import com.android.fourthwardcoder.movingpictures.models.MovieBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Chris Hare on 6/21/2016.
 */
public class CreditListAdapter extends RecyclerView.Adapter<CreditListAdapter.CreditListAdapterViewHolder> {

    private static String TAG = CreditListAdapter.class.getSimpleName();

    /***********************************************************************************************/
    /*                                       Local Data                                            */
    /***********************************************************************************************/
    private Context mContext;
    private Credits mCredits;
    private CreditListAdapter.CreditListAdapterOnClickHandler mClickHandler;
    boolean mShowYear;

    public CreditListAdapter(Context context, Credits credits, boolean showYear,
                             CreditListAdapterOnClickHandler clickHandler ) {

        mContext = context;
        Collections.sort(credits.getCast());
        mCredits = credits;
        mClickHandler = clickHandler;
    }

    @Override
    public CreditListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.image_name_character_list_item,parent,false);
        final CreditListAdapterViewHolder vh = new CreditListAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CreditListAdapterViewHolder holder, int position) {


         Log.e(TAG,"onBindViewHolder() with credit = " + mCredits.getCast().get(position).getCharacter());
         Cast cast = mCredits.getCast().get(position);
         Log.e(TAG,"Movie Title = " + cast.getTitle());
         Log.e(TAG,"Pic path = " + cast.getPosterPath());
         Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(cast.getPosterPath())).into(holder.thumbImageView);

         holder.titleTextView.setText(cast.getTitle() + " " + Util.formatYearFromDate(cast.getReleaseDate()));
         holder.characterTextView.setText(cast.getCharacter());

    }

    @Override
    public int getItemCount() {

        return mCredits.getCast().size();
    }



    /***********************************************************************************************/
    /*                                    Inner Classes                                            */
    /***********************************************************************************************/
    public class CreditListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView thumbImageView;
        public TextView titleTextView;
        public TextView characterTextView;

        public CreditListAdapterViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            thumbImageView = (ImageView)view.findViewById(R.id.posterImageView);
            titleTextView = (TextView)view.findViewById(R.id.titleTextView);
            characterTextView = (TextView)view.findViewById(R.id.characterTextView);
        }

        @Override
        public void onClick(View v) {
            int id = mCredits.getCast().get(getAdapterPosition()).getId();
            mClickHandler.onCreditClick(id,this);
        }
    }

    public interface CreditListAdapterOnClickHandler {
        void onCreditClick(int id, CreditListAdapterViewHolder vh);
    }
}
