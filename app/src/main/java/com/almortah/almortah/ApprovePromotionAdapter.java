package com.almortah.almortah;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 15/12/2017.
 */

public class ApprovePromotionAdapter extends RecyclerView.Adapter<ApprovePromotionAdapter.MyViewHolder>  {

    private Context context;
    private ArrayList<Promotions> arrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView offer ;
        public TextView chaletName;
        public TextView date;

        public Button accept;
        public Button reject;


        public MyViewHolder(View view) {
            super(view);
            offer = (TextView) view.findViewById(R.id.duration);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            date = (TextView) view.findViewById(R.id.fromDate);

            accept = (Button) view.findViewById(R.id.accept);
            reject = (Button) view.findViewById(R.id.reject);

        }
    }

    public ApprovePromotionAdapter(@NonNull Context context, @NonNull ArrayList<Promotions> objects) {
        this.context = context;
        this.arrayList = objects;
    }

    @Override
    public ApprovePromotionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.approve_promotion_item, parent, false);
        ApprovePromotionAdapter.MyViewHolder mViewHold = new ApprovePromotionAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final ApprovePromotionAdapter.MyViewHolder holder, final int position) {
        final Promotions promotions = arrayList.get(position);
        holder.chaletName.setText(promotions.getChaletName());
        holder.offer.setText(promotions.getDuration());
        holder.date.setText(promotions.getFromDate());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(context.getString(R.string.sure));
                alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arrayList.remove(promotions);
                                arrayList.clear();
                                notifyDataSetChanged();
                                FirebaseDatabase.getInstance().getReference().child("chalets").child(promotions.getChaletID()).child("promotion").setValue("1");
                                FirebaseDatabase.getInstance().getReference().child("Promotions").child(promotions.getChaletID()).removeValue();
                                Toast.makeText(context,R.string.promotedAccepted,Toast.LENGTH_SHORT).show();

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

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(context.getString(R.string.sure));
                alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arrayList.remove(promotions);
                                arrayList.clear();
                                notifyDataSetChanged();

                                FirebaseDatabase.getInstance().getReference().child("chalets").child(promotions.getChaletID()).child("promotion").setValue("0");
                                FirebaseDatabase.getInstance().getReference().child("Promotions").child(promotions.getChaletID()).removeValue();
                                Toast.makeText(context,R.string.rejectedPromotion,Toast.LENGTH_SHORT).show();
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
        return arrayList.size();
    }
}
