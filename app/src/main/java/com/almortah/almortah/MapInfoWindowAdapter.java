package com.almortah.almortah;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ziyadalkhonein on 10/23/17.
 */

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    LayoutInflater inflater;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private Chalet chalet;
    private boolean query;


    public MapInfoWindowAdapter(Activity context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chalets");
        storageReference=FirebaseStorage.getInstance().getReference();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.marker_tag,null);

       final LatLng latlng = marker.getPosition();
       Double doubleLat = latlng.latitude;

        final String lat = String.valueOf(latlng.latitude);
        final String log = String.valueOf(latlng.longitude);
        Log.i("MarkerPostion",lat);

        final ImageView img = (ImageView) v.findViewById(R.id.chaletImg);
       // final TextView textView = (TextView) v.findViewById(R.id.markerText);


        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("MarkerPostion","11111");
                for (DataSnapshot snapm: dataSnapshot.getChildren()) {
                    Log.i("MarkerPostion",snapm.child("latitude").getValue().toString());
                    Log.i("Key",snapm.getKey());
                 if ( lat.equals(snapm.child("latitude").getValue().toString()) && log.equals(snapm.child("longitude").getValue().toString()) ){
                     chalet = snapm.getValue(Chalet.class);
                     Log.i("Owner ID","9999");

                 }

                }
                storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm()).child("1");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(img);
            //            textView.setText(chalet.getName());
                        Log.i("working","11111");

                    }
                });

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;
    }


}



