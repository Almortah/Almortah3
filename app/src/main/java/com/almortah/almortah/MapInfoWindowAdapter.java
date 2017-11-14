package com.almortah.almortah;

import android.app.Activity;
import android.content.Context;
import android.database.Observable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

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
    private ImageView img;
    private String lat;
    private String log;
    private Executor executor;
    private OnCompleteListener<Chalet> listener;

    public Task<String> getChalet(final String lat,final String log) {
        final TaskCompletionSource<String> tcs = new TaskCompletionSource<>();
        mDatabase =  FirebaseDatabase.getInstance().getReference().child("chalets");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Log.i("MarkerPostion","112211");
                for (DataSnapshot snapm: dataSnapshot.getChildren()) {
                    Log.i("MarkerPostion",snapm.child("latitude").getValue().toString());
                    Log.i("Key",snapm.getKey());
                    if ( lat.equals(snapm.child("latitude").getValue().toString()) && log.equals(snapm.child("longitude").getValue().toString()) ){
                        chalet = snapm.getValue(Chalet.class);
                        Log.i("Owner ID","9999");
                    }

                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });

        return tcs.getTask();
    }

    public Task<String> insertImage(final Chalet chalet) {
        final TaskCompletionSource<String> tcs = new TaskCompletionSource<>();
        storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm()).child("1");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(img);
                //           textView.setText(chalet.getName());
                Log.i("working","11111");
            }
        });

        return tcs.getTask();
    }



    public MapInfoWindowAdapter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {



        mDatabase = FirebaseDatabase.getInstance().getReference().child("chalets");
        storageReference=FirebaseStorage.getInstance().getReference();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.marker_tag,null);

       final LatLng latlng = marker.getPosition();
       Double doubleLat = latlng.latitude;

         lat = String.valueOf(latlng.latitude);
         log = String.valueOf(latlng.longitude);
        Log.i("MarkerPostion",lat);

        img = (ImageView) v.findViewById(R.id.chaletImg);

       // final TextView textView = (TextView) v.findViewById(R.id.markerText);
        final Semaphore semaphore = new Semaphore(0);
       mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Log.i("MarkerPostion","112211");
                for (DataSnapshot snapm: dataSnapshot.getChildren()) {
                    Log.i("MarkerPostion",snapm.child("latitude").getValue().toString());
                    Log.i("Key",snapm.getKey());
                    if ( lat.equals(snapm.child("latitude").getValue().toString()) && log.equals(snapm.child("longitude").getValue().toString()) ){
                        chalet = snapm.getValue(Chalet.class);
                        Log.i("Owner ID","9999");
                        semaphore.release();
                    }

                }
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm()).child("1");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {


                        //Glide.with(context).load(uri).into(img);
                        //Picasso.with(context).load(uri).into(img);

                        Picasso.with(context).load(uri).into(img);

                        updateui(uri);

                        //           textView.setText(chalet.getName());
                        Log.i("MarkerPostion","11111");


                    }

                });




            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }




        });











/*
        Task<?>[] tasks = new Task[] {
                getChalet(lat,log),
                insertImage(chalet)
        };

        Tasks.whenAll(tasks)
                .continueWithTask(new RollbackIfFailure())
                .addOnCompleteListener()
                .addOnFailureListener(this);
*/
        Log.i("MarkerPostion","First");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

            }
        }, 500);

        return v;
    }


    /*class RollbackIfFailure implements Continuation<Void, Task<Void>> {
        @Override
        public Task<Void> then(@NonNull Task<Void> task) throws Exception {

            final TaskCompletionSource<Void> tcs = new TaskCompletionSource<>();

            if (task.isSuccessful()) {
                tcs.setResult(null);
            } else {
                // Rollback everything
            }

            return tcs.getTask();
        }
    }*/

    public void updateui(final Uri uri){

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.marker_tag,null);


        img = (ImageView) v.findViewById(R.id.chaletImg);

        Picasso.with(context).load(uri).into(img);

        img.invalidate();




        }








}



