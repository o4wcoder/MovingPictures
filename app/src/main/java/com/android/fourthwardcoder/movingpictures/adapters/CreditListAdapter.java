package com.android.fourthwardcoder.movingpictures.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fourthwardcoder.movingpictures.models.Credit;
import com.android.fourthwardcoder.movingpictures.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Class CreditListAdapter
 * Author: Chris Hare
 * Created: 9/2/2015.
 * <p/>
 * Adapter to display a MovieOld or TV shows Credit info in a listview of the Filmography
 */
public class CreditListAdapter extends ArrayAdapter<Credit> {

    /**********************************************************************/
    /*                           Constants                                */
    /**********************************************************************/

    /**********************************************************************/
    /*                           Local Data                               */
    /**********************************************************************/
    private Context mContext;
    private boolean mShowYear;

    public CreditListAdapter(Context context, ArrayList<Credit> creditList, boolean showYear) {
        super(context, 0, creditList);

        mContext = context;
        mShowYear = showYear;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.image_name_character_list_item, parent, false);
            //Get imageView
            holder.imageView = (ImageView) convertView.findViewById(R.id.posterImageView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.characterTextView = (TextView) convertView.findViewById(R.id.characterTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Get each MovieOld using the position in the ArrayAdapter
        Credit credit = getItem(position);

        //Call Picasso to load it into the imageView
        Picasso.with(mContext).load(credit.getPosterPath()).into(holder.imageView);

        //Set (????) if Credit does not have a release year
        String releaseYear = "";
        if (mShowYear) {
            if (credit.getReleaseYear() == 0)
                releaseYear = "(????)";
            else
                releaseYear = "(" + String.valueOf(credit.getReleaseYear()) + ")";
        }

        holder.nameTextView.setText(credit.getTitle() + " "
                + releaseYear);
        holder.characterTextView.setText(credit.getCharacter());

        return convertView;
    }

    /**********************************************************************/
    /*                          Inner Classes                             */

    /**********************************************************************/
    private static class ViewHolder {

        ImageView imageView;
        TextView nameTextView;
        TextView characterTextView;
    }
}
