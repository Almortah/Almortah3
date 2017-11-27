package com.almortah.almortah;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ALMAHRI on 10/23/17.
 */
public class ChaletListRV extends RecyclerView.Adapter<ChaletListRV.MyViewHolder> {

    private ArrayList<Chalet> chalets;
    private Context context;
    private Geocoder geo;
    private List<Address> addresses;

    public class MyViewHolder extends RecyclerView.ViewHolder {
       public TextView chaletName;
        public ImageView img1;
        public RatingBar chaletRating;
       public TextView chaletLocation;
       public ImageView promot;

        public MyViewHolder(View view) {
            super(view);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            img1 = (ImageView) view.findViewById(R.id.chaletImg);
            chaletLocation = (TextView) view.findViewById(R.id.chaletLocation);
            chaletRating = (RatingBar) view.findViewById(R.id.chaletRating);
            promot = (ImageView) view.findViewById(R.id.promotion);
        }
    }


    public ChaletListRV(Context context, ArrayList<Chalet> chalets) {
        this.context = context;
        this.chalets = chalets;
         geo = new Geocoder(context, Locale.getDefault());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chalet_list_item, parent, false);
        MyViewHolder mViewHold = new MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Chalet chalet = chalets.get(position);
        holder.chaletName.setText(chalet.getName());
        if(chalet.getPromotion().equals("1"))
            holder.promot.setVisibility(View.VISIBLE);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm());
        StorageReference tmp = storageReference.child(String.valueOf(1));

        //final View finalListItemView1 = listItemView;
        tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.img1);

                holder.img1.invalidate();
            }
        });

        holder.chaletLocation.setText(R.string.defLocation);

        new Thread() {
            public void run() {
                try {
                    List<Address> addresses = geo.getFromLocation(Double.parseDouble(chalet.getLatitude()), Double.parseDouble(chalet.getLongitude()), 1);
                    if (addresses.isEmpty()) {
                        ;

                    }
                    else {
                        if (addresses.size() > 0) {
                            holder.chaletLocation.setText(addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality());
                            //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace(); // getFromLocation() may sometimes fail
                }
            }
        }.start();

       // holder.chaletLocation.setText(chalet.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent toChaletInfo = new Intent(context, ChaletInfoCustomer.class);
                                                     toChaletInfo.putExtra("chalet",chalet);
                                                     toChaletInfo.putExtra("location",holder.chaletLocation.getText().toString());
                                                     context.startActivity(toChaletInfo);
                                                 }
                                             }
        );

    }


    @Override
    public int getItemCount() {
        return chalets.size();
    }
/*
    @Override
    public int getItemViewType(int position) {
        return position;
    }
*/



}