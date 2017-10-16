package com.almortah.almortah;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 10/16/17.
 */

public class MyChaletsAdapter extends ArrayAdapter<Chalet> {
    Context context;

    public MyChaletsAdapter(Activity context, ArrayList<Chalet> chalets){
        super(context,0,chalets);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.my_chalet_list, parent, false);
        }
        Chalet chalet = getItem(position);
        String ownerID = chalet.getOwnerID();
        String chaletNb = chalet.getChaletNm();

        TextView chaletName = (TextView) listItemView.findViewById(R.id.chaletName);
        chaletName.setText(chalet.getName());

        // Reference to an image file in Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(ownerID).child(chaletNb);

// ImageView in your Activity
        ImageView img1 = (ImageView) listItemView.findViewById(R.id.chaletImg1);
        ImageView img2 = (ImageView) listItemView.findViewById(R.id.chaletImg2);
        ImageView img3 = (ImageView) listItemView.findViewById(R.id.chaletImg3);

        String imgPath = chalet.getImages();

// Load the image using Glide
        Glide.with(context)
                .load(storageReference.child("1").getDownloadUrl())
                .into(img1);
        Glide.with(context)
                .load(storageReference.child("2").getDownloadUrl())
                .into(img2);
        Glide.with(context)
                .load(storageReference.child("3").getDownloadUrl())
                .into(img3);


        return listItemView;

    }
}
