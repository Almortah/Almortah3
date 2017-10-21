package com.almortah.almortah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by ziyadalkhonein on 10/9/17.
 */

public class ChaletAdapter extends ArrayAdapter<Chalet> {
    private Context context;
    private RatingBar chaletRating;
    private ImageView img1;
    private boolean zoomOut =  false;


    public ChaletAdapter(Activity context, ArrayList<Chalet> chalets){
        super(context,0,chalets);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chalet_list_item, parent, false);
        }
        final Chalet chalet = getItem(position);



        final TextView chaletName = (TextView) listItemView.findViewById(R.id.chaletName);
        chaletName.setText(chalet.getName());
        img1 = (ImageView) listItemView.findViewById(R.id.chaletImg);
        chaletRating = (RatingBar) listItemView.findViewById(R.id.chaletRating);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm());
        StorageReference tmp = storageReference.child(String.valueOf(1));
        tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(img1);
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(zoomOut) {
                                            img1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));
                                             img1.setScaleType(ImageView.ScaleType.FIT_XY);
                                            zoomOut =false;
                                        }else{
                                            img1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                            img1.setScaleType(ImageView.ScaleType.FIT_XY);
                                            zoomOut = true;
                                        }
                                    }
                                });

        TextView price = (TextView) listItemView.findViewById(R.id.normalPrice);
        price.setText(chalet.getNormalPrice());
        //chaletRating.setText(""+chalet.getChaletRating());
        TextView chaletLocation = (TextView) listItemView.findViewById(R.id.chaletLocation);
        chaletLocation.setText(chalet.getLocation());

       final View finalListItemView = listItemView;
        finalListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChaletInfo = new Intent(context,ChaletInfoCustomer.class);
                toChaletInfo.putExtra("name",chalet.getName());
                toChaletInfo.putExtra("normalPrice",chalet.getNormalPrice());
                toChaletInfo.putExtra("weekendPrice",chalet.getWeekendPrice());
                toChaletInfo.putExtra("eidPrice",chalet.getEidPrice());
                toChaletInfo.putExtra("images",chalet.getChaletNm());
                toChaletInfo.putExtra("ownerID",chalet.getOwnerID());
                toChaletInfo.putExtra("location",chalet.getLocation());
                toChaletInfo.putExtra("chaletNb",chalet.getChaletNm());
                context.startActivity(toChaletInfo);
            }
        }
        );

        return listItemView;

    }
}
