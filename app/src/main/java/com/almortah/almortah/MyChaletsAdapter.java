package com.almortah.almortah;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 10/16/17.
 */

public class MyChaletsAdapter extends ArrayAdapter<Chalet> {
    private Context context;
    private int imgNb;

    private class Images extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }


    public MyChaletsAdapter(Activity context, ArrayList<Chalet> chalets) {
        super(context, 0, chalets);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
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
        final ImageView img1 = (ImageView) listItemView.findViewById(R.id.chaletImg1);
        final ImageView img2 = (ImageView) listItemView.findViewById(R.id.chaletImg2);
        final ImageView img3 = (ImageView) listItemView.findViewById(R.id.chaletImg3);

        for (int i = 1; i < 4; i++) {
            if (i == 1) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(img1);
                    }
                });
            } else if (i == 2) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(img2);
                    }
                });
            } else {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(img3);
                    }
                });
            }
        }

        img1.setMaxHeight(50);
        img2.setMaxHeight(50);
        img3.setMaxHeight(50);
        img1.setMaxWidth(50);
        img2.setMaxWidth(50);
        img3.setMaxWidth(50);


        return listItemView;

    }


}
