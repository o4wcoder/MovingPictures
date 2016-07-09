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
import com.android.fourthwardcoder.movingpictures.models.MediaBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Chris Hare on 6/21/2016.
 */
public class ShowAllListAdapter extends RecyclerView.Adapter<ShowAllListAdapter.CreditListAdapterViewHolder>
    implements Constants{

    private static String TAG = ShowAllListAdapter.class.getSimpleName();

    /***********************************************************************************************/
    /*                                       Local Data                                            */
    /***********************************************************************************************/
    private Context mContext;
    private Credits mCredits;
    private ArrayList<MediaBasic> mQueryResults;
    private int mEntType;
    private int mListType;
    private ShowAllListAdapter.ShowAllListAdapterOnClickHandler mClickHandler;

    public ShowAllListAdapter(Context context, Credits credits, int entType, int listType,
                              ShowAllListAdapterOnClickHandler clickHandler ) {

        mContext = context;
        Collections.sort(credits.getCast());
        mCredits = credits;
        mEntType = entType;
        mListType = listType;
        mClickHandler = clickHandler;
    }

    public ShowAllListAdapter(Context context, ArrayList<MediaBasic> queryResults, int entType, int listType,
                              ShowAllListAdapterOnClickHandler clickHandler ) {

        mContext = context;
        mQueryResults = queryResults;
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

        if(mEntType == ENT_TYPE_SEARCH) {

            MediaBasic media = mQueryResults.get(position);

            if(media.getMediaType().equals(MEDIA_TYPE_MOVIE)) {
                Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(media.getPosterPath())).into(holder.thumbImageView);
                holder.titleTextView.setText(media.getTitle());
                holder.characterTextView.setText(media.getReleaseYear());

            } else if(media.getMediaType().equals(MEDIA_TYPE_TV)) {
                Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(media.getPosterPath())).into(holder.thumbImageView);
                holder.titleTextView.setText(media.getName());
                holder.characterTextView.setText(media.getFirstAirYear());
            } else if(media.getMediaType().equals(MEDIA_TYPE_PERSON)) {
                Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(media.getProfilePath())).into(holder.thumbImageView);
                holder.titleTextView.setText(media.getName());
            }
        }
        else {

            Cast cast = mCredits.getCast().get(position);

            if (mEntType == ENT_TYPE_MOVIE) {
                Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(cast.getPosterPath())).into(holder.thumbImageView);
                holder.titleTextView.setText(cast.getTitle() + " " + Util.formatYearFromDate(cast.getReleaseDate()));
                holder.characterTextView.setText(cast.getCharacter());
            } else if (mEntType == ENT_TYPE_TV) {
                Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(cast.getPosterPath())).into(holder.thumbImageView);
                holder.titleTextView.setText(cast.getName() + " " + Util.formatYearFromDate(cast.getFirstAirDate()));
                holder.characterTextView.setText(cast.getCharacter());

            } else if (mEntType == ENT_TYPE_PERSON) {
                if (mListType == LIST_TYPE_MOVIE_CAST || mListType == LIST_TYPE_TV_CAST) {
                    Log.e(TAG, "------------- Load CAST -------------");
                    Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath(cast.getProfilePath())).into(holder.thumbImageView);
                    holder.titleTextView.setText(cast.getName());
                    holder.characterTextView.setText(cast.getCharacter());
                } else if (mListType == LIST_TYPE_MOVIE_CREW || mListType == LIST_TYPE_TV_CREW) {
                    Log.e(TAG, "------------- Load CREW -------------");
                    Crew crew = mCredits.getCrew().get(position);
                    Picasso.with(mContext).load(MovieDbAPI.getFullPosterPath((String) crew.getProfilePath())).into(holder.thumbImageView);
                    holder.titleTextView.setText(crew.getName());
                    holder.characterTextView.setText(crew.getJob());
                }
            }
        }
    }

    @Override
    public int getItemCount() {

        if(mListType == LIST_TYPE_SEARCH)
            return mQueryResults.size();
        else if (mListType == LIST_TYPE_MOVIE_CREW || mListType == LIST_TYPE_TV_CREW)
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

            if(mListType == LIST_TYPE_SEARCH)
                id = mQueryResults.get(getAdapterPosition()).getId();
            else if (mListType == LIST_TYPE_MOVIE_CREW || mListType == LIST_TYPE_TV_CREW)
                id = mCredits.getCrew().get(getAdapterPosition()).getId();
            else
                id = mCredits.getCast().get(getAdapterPosition()).getId();

            mClickHandler.onClick(id, getAdapterPosition(), this);
        }
    }

    public interface ShowAllListAdapterOnClickHandler {
        void onClick(int id, int position, CreditListAdapterViewHolder vh);
    }
}
