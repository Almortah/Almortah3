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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ALMAHRI on 02/12/2017.
 */

public class ApproveAdapter extends RecyclerView.Adapter<ApproveAdapter.MyViewHolder>  {

    private Context context;
    private Button control;
    private ArrayList<Reservation> reservations;
    private double sumOfCustomerRating ;
    private double totalRatingOfCustomer ;

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
        sumOfCustomerRating = 0.0; totalRatingOfCustomer = 0.0;
        holder.chaletName.setText(reservation.getChaletName());
        holder.date.setText(reservation.getDate());
        holder.inTime.setText(reservation.getCheckin());
        holder.id.setText(reservation.getReservationID());

        if(holder.rating.getText().toString().trim().matches("")) {
            FirebaseDatabase.getInstance().getReference().child("customerRatings").orderByChild("customerID")
                    .equalTo(reservation.getCustomerID())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int size = (int) dataSnapshot.getChildrenCount();
                                for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                    customerRatings customerRatings = singlValue.getValue(customerRatings.class);
                                    double avg = Double.parseDouble(customerRatings.getCleanRating()) + Double.parseDouble(customerRatings.getPaymentRating());
                                    double total = avg / 2;
                                    sumOfCustomerRating += total;
                                }

                                totalRatingOfCustomer = sumOfCustomerRating / size;
                                DecimalFormat df = new DecimalFormat("#.00");
                                String totalFormated = df.format(totalRatingOfCustomer);

                                holder.rating.setText(totalFormated + " / " + " 5.0");

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        if (holder.customerName.getText().toString().trim().matches("")) {
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child(reservation.getCustomerID()).child("Name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                holder.customerName.setText(dataSnapshot.getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        holder.accept.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                                 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                                 alertDialogBuilder.setMessage(context.getString(R.string.sure));
                                                 alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                                                         new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface arg0, int arg1) {
                                                                 reservations.remove(reservation);
                                                                 reservations.clear();
                                                                 notifyDataSetChanged();
                                                                 FirebaseDatabase.getInstance().getReference().child("reservation")
                                                                         .child(reservation.getReservationID()).child("confirm").setValue("1");
                                                                 //code to notify customer!


                                                             }
                                                         });

                                                 alertDialogBuilder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
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


                                        AlertDialog dialog;
                                        final CharSequence[] items = context.getResources().getStringArray(R.array.reasonsReject);
                                        //{" Easy "," Medium "," Hard "," Very Hard "};
                                        // arraylist to keep the selected items
                                        final ArrayList seletedItems=new ArrayList();

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle(context.getString(R.string.reson));
                                        builder.setMultiChoiceItems(items, null,
                                                new DialogInterface.OnMultiChoiceClickListener() {
                                                    // indexSelected contains the index of item (of which checkbox checked)
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int indexSelected,
                                                                        boolean isChecked) {
                                                        if (isChecked) {
                                                            // If the user checked the item, add it to the selected items
                                                            // write your code when user checked the checkbox
                                                            seletedItems.add(indexSelected);
                                                        } else if (seletedItems.contains(indexSelected)) {
                                                            // Else, if the item is already in the array, remove it
                                                            // write your code when user Uchecked the checkbox
                                                            seletedItems.remove(Integer.valueOf(indexSelected));
                                                        }
                                                    }
                                                })
                                                // Set the action buttons
                                                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        if (seletedItems.size() == 0)
                                                            return;

                                                        String reasons = "";
                                                        for (int i = 0; i < seletedItems.size(); i++) {
                                                            int tmp = (int) seletedItems.get(i);
                                                            reasons += tmp + "-";
                                                        }
                                                        FirebaseDatabase.getInstance().getReference().child("reasonsOfRejectedBooking")
                                                                .child(reservation.getReservationID())
                                                                .child("reasons").setValue(reasons).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FirebaseDatabase.getInstance().getReference().child("reservation")
                                                                        .child(reservation.getReservationID()).child("confirm").setValue("2");
                                                                reservations.remove(position);
                                                                notifyItemRemoved(position);
                                                                notifyItemRangeChanged(position,reservations.size());
                                                                holder.itemView.setVisibility(View.GONE);

                                                            }
                                                        });
                                                        //code to notify customer!

                                                    }
                                                })
                                                .setNegativeButton(context.getString(R.string.cancel1), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //  Your code when user clicked on Cancel
                                                        builder.create().cancel();
                                                    }
                                                });

                                        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
                                        dialog.show();

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
                notifyDataSetChanged();

            }
        });

//        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }
}

