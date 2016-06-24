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
import com.android.fourthwardcoder.movingpictures.interfaces.Constants;
import com.android.fourthwardcoder.movingpictures.models.Cast;
import com.android.fourthwardcoder.movingpictures.models.Credits;
import com.android.fourthwardcoder.movingpictures.models.Crew;
import com.android.fourthwardcoder.movingpictures.models.MovieBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Chris Hare on 6/21/2016.
 */
public class CreditListAdapter extends RecyclerView.Adapter<CreditListAdapter.CreditListAdapterViewHolder>
    implements Constants{

    private static String TAG = CreditListAdapter.class.getSimpleName();

    /***********************************************************************************************/
    /*                                       Local Data                                            */
    /***********************************************************************************************/
    private Context mContext;
    private Credits mCredits;
    private int mEntType;
    private int mListType;
    private CreditListAdapter.CreditListAdapterOnClickHandler mClickHandler;

    public CreditListAdapter(Context context, Credits credits, int entType,int listType,
                             CreditListAdapterOnClickHandler clickHandler ) {

        mContext = context;
        Collections.sort(credits.getCast());
        mCredits = credits;
        mEntType = entType;
        mListType = listType;
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
        Log.e(TAG,"Name = " + cast.getName());



         Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(cast.getPosterPath())).into(holder.thumbImageView);
         Log.e(TAG,"Ent type = " + mEntType);
        if (mEntType == ENT_TYPE_MOVIE) {
            Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(cast.getPosterPath())).into(holder.thumbImageView);
            holder.titleTextView.setText(cast.getTitle() + " " + Util.formatYearFromDate(cast.getReleaseDate()));
            holder.characterTextView.setText(cast.getCharacter());
        }
        else if(mEntType == ENT_TYPE_TV) {
            Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(cast.getPosterPath())).into(holder.thumbImageView);
            holder.titleTextView.setText(cast.getName() + " " + Util.formatYearFromDate(cast.getFirstAirDate()));
            holder.characterTextView.setText(cast.getCharacter());

        }
        else if(mEntType == ENT_TYPE_PERSON) {
            if(mListType == LIST_TYPE_MOVIE_CAST || mListType == LIST_TYPE_TV_CAST) {
                Log.e(TAG,"------------- Load CAST -------------");
                Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(cast.getProfilePath())).into(holder.thumbImageView);
                holder.titleTextView.setText(cast.getName());
                holder.characterTextView.setText(cast.getCharacter());
            }
            else if(mListType == LIST_TYPE_MOVIE_CREW || mListType == LIST_TYPE_TV_CREW) {
                Log.e(TAG,"------------- Load CREW -------------");
                Crew crew = mCredits.getCrew().get(position);
                Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath((String)crew.getProfilePath())).into(holder.thumbImageView);
                holder.titleTextView.setText(crew.getName());
                holder.characterTextView.setText(crew.getJob());
            }
        }



    }

    @Override
    public int getItemCount() {

        if (mListType == LIST_TYPE_MOVIE_CREW || mListType == LIST_TYPE_TV_CREW)
            return mCredits.getCrew().size();
        else
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
            int id = 0;
            if (mListType == LIST_TYPE_MOVIE_CREW || mListType == LIST_TYPE_TV_CREW)
                id = mCredits.getCrew().get(getAdapterPosition()).getId();
            else
                id = mCredits.getCast().get(getAdapterPosition()).getId();

            mClickHandler.onCreditClick(id, this);
        }
    }

    public interface CreditListAdapterOnClickHandler {
        void onCreditClick(int id, CreditListAdapterViewHolder vh);
    }
}
