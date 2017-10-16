package com.almortah.almortah;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ziyadalkhonein on 10/9/17.
 */

public class ChaletAdapter extends ArrayAdapter<Chalet> {

    public ChaletAdapter(Activity context, ArrayList<Chalet> chalets){
        super(context,0,chalets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chalet_list_item, parent, false);
        }
        Chalet chalet = getItem(position);

        TextView chaletName = (TextView) listItemView.findViewById(R.id.chaletName);
        chaletName.setText(chalet.getName());
      //  TextView chaletRating = (TextView) listItemView.findViewById(R.id.chaletRating);
        //chaletRating.setText(""+chalet.getChaletRating());
        TextView chaletLocation = (TextView) listItemView.findViewById(R.id.chaletLocation);
        chaletLocation.setText(chalet.getPromotion());

        return listItemView;

    }
}
