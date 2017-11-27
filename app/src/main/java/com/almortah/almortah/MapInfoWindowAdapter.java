package com.almortah.almortah;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by ziyadalkhonein on 10/23/17.
 */



public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    LayoutInflater inflater;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private Chalet chalet;
    private boolean query=false;
    public ImageView img;
    public TextView chaletName;
    private String lat;
    private String log;
    private Marker markerShowingInfoWindow;
    private Marker mLastSelectedMarker;
    private Uri imgUrl;
    private String nameHolder;
    private Button direction;
    private Button info;






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

        final  View v = inflater.inflate(R.layout.marker_tag,null);



            img = (ImageView) v.findViewById(R.id.chaletImg);
            chaletName =(TextView) v.findViewById(R.id.chaletName);
            info = (Button) v.findViewById(R.id.info);
            direction = (Button) v.findViewById(R.id.dirc);




        if (query==true){
            Picasso.with(v.getContext()).load(imgUrl).into(img);
            chaletName.setText(nameHolder);

        }
        else {
            final LatLng latlng = marker.getPosition();
            Double doubleLat = latlng.latitude;

            lat = String.valueOf(latlng.latitude);
            log = String.valueOf(latlng.longitude);
            Log.i("MarkerPostion", lat);

            if (img.getDrawable() != null) {
                img.setVisibility(View.GONE);

            } else {
                Log.i("MarkerPostion", "Hello");

                mDatabase.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Log.i("MarkerPostion", "112211");
                        for (DataSnapshot snapm : dataSnapshot.getChildren()) {
                            Log.i("MarkerPostion", snapm.child("latitude").getValue().toString());
                            Log.i("Key", snapm.getKey());
                            if (lat.equals(snapm.child("latitude").getValue().toString()) && log.equals(snapm.child("longitude").getValue().toString())) {
                                chalet = snapm.getValue(Chalet.class);
                                Log.i("MarkerPostion", "9999");
                                break;
                            }

                        }


                        storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm()).child("1");

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {


                                //   Glide.with(v.getContext()).load(uri).into(img);
                                //Picasso.with(context).load(uri).into(img);

                                //  Picasso.with(v.getContext()).load(uri).into(img);

                                query=true;
                                imgUrl=uri;

                                nameHolder = chalet.getName();
                                Picasso.with(v.getContext()).load(uri).into(img, new InfoWindowRefresher(marker));
                                info.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent toInfo = new Intent(context,ChaletInfoCustomer.class);
                                        toInfo.putExtra("chalet",chalet);
                                        context.startActivity(toInfo);
                                    }
                                });

                                direction.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                Uri.parse("geo:0,0?q="+chalet.getLatitude()+","+chalet.getLongitude()+" (" + chalet.getName() + ")"));
                                        context.startActivity(intent);
                                    }
                                });



                                //    updateui(uri);

                                //           textView.setText(chalet.getName());
                                Log.i("MarkerPostion", "2222");


                            }

                        });


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });
            }



//            final LatLng latlng = marker.getPosition();
//       Double doubleLat = latlng.latitude;
//
//         lat = String.valueOf(latlng.latitude);
//         log = String.valueOf(latlng.longitude);
//        Log.i("MarkerPostion",lat);
//
//        img = (ImageView) v.findViewById(R.id.chaletImg);
//
//       // final TextView textView = (TextView) v.findViewById(R.id.markerText);
//        final Semaphore semaphore = new Semaphore(0);
//       mDatabase.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                Log.i("MarkerPostion","112211");
//                for (DataSnapshot snapm: dataSnapshot.getChildren()) {
//                    Log.i("MarkerPostion",snapm.child("latitude").getValue().toString());
//                    Log.i("Key",snapm.getKey());
//                    if ( lat.equals(snapm.child("latitude").getValue().toString()) && log.equals(snapm.child("longitude").getValue().toString()) ){
//                        chalet = snapm.getValue(Chalet.class);
//                        Log.i("MarkerPostion","9999");
//                        semaphore.release();
//                        break;
//                    }
//
//                }
//                try {
//                    semaphore.acquire();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm()).child("1");
//                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//
//                     //   Glide.with(v.getContext()).load(uri).into(img);
//                        //Picasso.with(context).load(uri).into(img);
//
//                      //  Picasso.with(v.getContext()).load(uri).into(img);
//                        if(img.getDrawable()==null){
//                            Picasso.with(v.getContext()).load(uri).resize(50,50).centerCrop().into(img, new InfoWindowRefresher(marker));
//
//                        }
//                        else{
//                            Picasso.with(v.getContext()).load(uri).resize(50,50).centerCrop().into(img);
//
//                        }
//
//                    //    updateui(uri);
//
//                        //           textView.setText(chalet.getName());
//                        Log.i("MarkerPostion","2222");
//
//
//
//                    }
//
//                });
//
//
//
//
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//
//
//
//        });











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
        // Log.i("MarkerPostion","First");
      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

            }
        }, 500);
*/}
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




    private class InfoWindowRefresher implements Callback {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            if (markerToRefresh != null && markerToRefresh.isInfoWindowShown()) {

                markerToRefresh.hideInfoWindow();

                Log.i("MarkerPostion","Thrid");


                markerToRefresh.showInfoWindow();
            }

        }

        @Override
        public void onError() {}
    }





}



