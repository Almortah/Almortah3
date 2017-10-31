package com.almortah.almortah;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ALMAHRI on 10/31/17.
 */

public class MyChaletRV extends RecyclerView.Adapter<MyChaletRV.MyViewHolder> {

    private ArrayList<Chalet> chalets;
    private Context context;
    private Geocoder geo;
    private List<Address> addresses;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView chaletName;
        public ImageView img1;
        public Button manage;
        public Button stat;
        public Button promotion;

        public MyViewHolder(View view) {
            super(view);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            img1 = (ImageView) view.findViewById(R.id.chaletImg);
            manage = (Button) view.findViewById(R.id.manage);
            stat = (Button) view.findViewById(R.id.view);
            promotion = (Button) view.findViewById(R.id.promotion);
        }
    }


    public MyChaletRV(Context context, ArrayList<Chalet> chalets) {
        this.context = context;
        this.chalets = chalets;
        geo = new Geocoder(context, Locale.getDefault());
    }

    @Override
    public MyChaletRV.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_chalet_list, parent, false);
        MyChaletRV.MyViewHolder mViewHold = new MyChaletRV.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final MyChaletRV.MyViewHolder holder, int position) {
        final Chalet chalet = chalets.get(position);
        holder.chaletName.setText(chalet.getName());
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
    }

    @Override
    public int getItemCount() {
        return chalets.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}