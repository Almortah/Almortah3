package com.almortah.almortah;

import android.app.Activity;
import android.content.Intent;
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
        final Chalet chalet = getItem(position);

        final TextView chaletName = (TextView) listItemView.findViewById(R.id.chaletName);
        chaletName.setText(chalet.getName());

        TextView price = (TextView) listItemView.findViewById(R.id.normalPrice);
        price.setText(chalet.getNormalPrice());
        //chaletRating.setText(""+chalet.getChaletRating());
        TextView chaletLocation = (TextView) listItemView.findViewById(R.id.chaletLocation);
        chaletLocation.setText(chalet.getPromotion());

        final View finalListItemView = listItemView;
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChaletInfo = new Intent(finalListItemView.getContext(),ChaletInfoCustomer.class);
                toChaletInfo.putExtra("name",chalet.getName());
                toChaletInfo.putExtra("normalPrice",chalet.getNormalPrice());
                toChaletInfo.putExtra("weekendPrice",chalet.getWeekendPrice());
                toChaletInfo.putExtra("eidPrice",chalet.getEidPrice());
                toChaletInfo.putExtra("images",chalet.getChaletNm());

            }
        }
        );

        return listItemView;

    }
}
