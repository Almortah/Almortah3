package com.almortah.almortah;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
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
        public Button delete;
        public Button promotion;

        public MyViewHolder(View view) {
            super(view);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            img1 = (ImageView) view.findViewById(R.id.chaletImg);
            manage = (Button) view.findViewById(R.id.manage);
            delete = (Button) view.findViewById(R.id.delete);
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
    public void onBindViewHolder(final MyChaletRV.MyViewHolder holder, final int position) {
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

        holder.promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPromot = new Intent(context, Promotion.class);
                toPromot.putExtra("chalet", chalet);
                toPromot.putExtra("id",chalet.getId());

                if(chalet.getPromotion().equals("0"))
                context.startActivity(toPromot);

                else Toast.makeText(context,R.string.promoted,Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(context.getString(R.string.sure));
                alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                        FirebaseDatabase.getInstance().getReference().child("chalets").child(chalet.getId()).removeValue();
                                        chalets.remove(chalet);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,getItemCount());

                            }
                        });

                alertDialogBuilder.setNegativeButton(context.getString(R.string.no),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();




            }
        });


    }

    @Override
    public int getItemCount() {
        return chalets.size();
    }


}