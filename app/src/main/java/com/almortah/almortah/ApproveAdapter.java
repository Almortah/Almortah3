package com.almortah.almortah;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 02/12/2017.
 */

public class ApproveAdapter extends RecyclerView.Adapter<ApproveAdapter.MyViewHolder>  {

    private Context context;
    private Button control;
    private ArrayList<Reservation> reservations;
    private double sumOfCustomerRating = 0.0;
    private double totalRatingOfCustomer = 0.0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date ;
        public TextView chaletName;
        public TextView inTime ;
        public TextView rating ;
        public TextView id;
        public Button accept;
        public Button reject;
        public TextView customerName;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            inTime = (TextView) view.findViewById(R.id.inTime);
            id = (TextView) view.findViewById(R.id.id);
            rating = (TextView) view.findViewById(R.id.rateCustomer);
            accept = (Button) view.findViewById(R.id.accept);
            reject = (Button) view.findViewById(R.id.reject);
            customerName = (TextView) view.findViewById(R.id.customerName);
        }
    }

    public ApproveAdapter(@NonNull Context context, @NonNull ArrayList<Reservation> objects) {
        this.context = context;
        this.reservations = objects;
    }

    @Override
    public ApproveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.approve_booking_item, parent, false);
        ApproveAdapter.MyViewHolder mViewHold = new ApproveAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final ApproveAdapter.MyViewHolder holder, final int position) {
        final Reservation reservation = reservations.get(position);
        holder.chaletName.setText(reservation.getChaletName());
        holder.date.setText(reservation.getDate());
        Log.e("CHECKINN",reservation.getCheckin());
        holder.inTime.setText(reservation.getCheckin());
        holder.id.setText(reservation.getReservationID());
        FirebaseDatabase.getInstance().getReference().child("customerRatings").orderByChild("customerID")
                .equalTo(reservation.getCustomerID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            int size = (int) dataSnapshot.getChildrenCount();
                            for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                customerRatings customerRatings = singlValue.getValue(customerRatings.class);
                                double avg = Double.parseDouble(customerRatings.getCleanRating()) + Double.parseDouble(customerRatings.getPaymentRating());
                                double total = avg/2;
                                sumOfCustomerRating += total;
                            }

                            totalRatingOfCustomer = sumOfCustomerRating/size;

                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(reservation.getCustomerID()).child("Name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            holder.customerName.setText(dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        holder.rating.setText(totalRatingOfCustomer+" / "+" 5.0");


        holder.accept.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {


                                                 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                                 alertDialogBuilder.setMessage(context.getString(R.string.sure));
                                                 alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                                                         new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface arg0, int arg1) {
                                                                 FirebaseDatabase.getInstance().getReference().child("reservation")
                                                                         .child(reservation.getReservationID()).child("confirm").setValue("1");
                                                                 //code to notify customer!

                                                                 reservations.remove(reservation);
                                                                 notifyItemRemoved(position);
                                                                 notifyItemRangeChanged(position, getItemCount());

                                                             }
                                                         });

                                                 alertDialogBuilder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int which) {
                                                         dialog.cancel();
                                                     }
                                                 });
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
                                        FirebaseDatabase.getInstance().getReference().child("reservation")
                                                .child(reservation.getReservationID()).child("confirm").setValue("2");
                                        //code to notify customer!
                                        reservations.remove(reservation);
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
        return reservations.size();
    }
}

