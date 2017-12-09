package com.almortah.almortah;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 26/11/2017.
 */

public class CurrentReservAdapter extends RecyclerView.Adapter<CurrentReservAdapter.MyViewHolder>  {

    private Context context;
    private Button control;
    private ArrayList<Reservation> reservations;
    String reason1; String reason2;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date ;
        public TextView chaletName;
        public TextView inTime ;
        public TextView outTime;
        public TextView payment ;
        public TextView id;
        public Button cancel;
        public ImageView icon;
        public TextView confirmed;
        public ImageView why;


        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            inTime = (TextView) view.findViewById(R.id.inTime);
            outTime = (TextView) view.findViewById(R.id.outTime);
            id = (TextView) view.findViewById(R.id.id);
            payment = (TextView) view.findViewById(R.id.payment);
            cancel = (Button) view.findViewById(R.id.cancel);

            icon = (ImageView) view.findViewById(R.id.icon);
            confirmed = (TextView) view.findViewById(R.id.confirmed);
            why = (ImageView) view.findViewById(R.id.why);

        }
    }

    public CurrentReservAdapter(@NonNull Context context, @NonNull ArrayList<Reservation> objects) {
        this.context = context;
        this.reservations = objects;
    }

    @Override
    public CurrentReservAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_reserv_item, parent, false);
        CurrentReservAdapter.MyViewHolder mViewHold = new CurrentReservAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final CurrentReservAdapter.MyViewHolder holder, final int position) {
        final Reservation reservation = reservations.get(position);
        holder.chaletName.setText(reservation.getChaletName());
        holder.date.setText(reservation.getDate());
        holder.inTime.setText(reservation.getCheckin());
        holder.outTime.setText(reservation.getCheckout());
        holder.id.setText(reservation.getReservationID());
        String payment;
        if(reservation.getPayment().equals("cash"))
            payment = context.getString(R.string.cash);
        else
            payment = context.getString(R.string.visa);

        holder.payment.setText(payment+" / "+reservation.getPrice()+ " " + context.getString(R.string.riyal));

        if(reservation.getConfirm().equals("0")) {
            holder.icon.setImageResource(R.drawable.ic_pend);
            holder.confirmed.setText(R.string.pending);
            holder.confirmed.setTextColor(context.getColor(R.color.colorDarkGrey));
        }
        else if(reservation.getConfirm().equals("1")) {
            holder.icon.setImageResource(R.drawable.ic_curent);
            holder.confirmed.setText(R.string.confirmed);
            holder.confirmed.setTextColor(context.getColor(R.color.colorGreen));
        }

        else {
            holder.icon.setImageResource(R.drawable.ic_close_black_24dp);
            holder.confirmed.setText(R.string.rejected);
            holder.confirmed.setTextColor(Color.parseColor("#b50009"));
            holder.why.setVisibility(View.VISIBLE);
        }


        holder.why.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.reasons));
                final String[] reasons = context.getResources().getStringArray(R.array.reasonsReject);
                reason1 = null; reason2 = null;
                final ArrayList<String> rejected = new ArrayList<>();

// add a list
                final String[][] stringArray1 = new String[1][1];
                FirebaseDatabase.getInstance().getReference().child("reasonsOfRejectedBooking")
                        .child(reservation.getReservationID())
                        .child("reasons").addListenerForSingleValueEvent(new ValueEventListener() {
                    String[] stringArray1;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            String[] tmp = dataSnapshot.getValue().toString().split("-");
                            ArrayList<Integer> userReasons = new ArrayList<>();
                            for (int i = 0; i < (tmp.length); i++) {
                                userReasons.add(Integer.parseInt(tmp[i]));
                                rejected.add(reasons[Integer.parseInt(tmp[i])]);
                                Log.e("FirstReason",reasons[Integer.parseInt(tmp[i])]);
                            }
                            stringArray1 = rejected.toArray(new String[rejected.size()]);
                            Log.e("InStringArray", stringArray1[0]);
                        }

                        builder.setItems(stringArray1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.create().cancel();
//                        switch (which) {
//                            case 0: // horse
//                            case 1: // cow
//                            case 2: // camel
//                            case 3: // sheep
//                            case 4: // goat
//                        }
                            }
                        });


                        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.create().cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

// create and show the alert dialog


            }
        });


        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(context.getString(R.string.sure));
                alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                FirebaseDatabase.getInstance().getReference().child("reservation")
                                        .child(reservation.getReservationID()).removeValue();

                                FirebaseDatabase.getInstance().getReference().child("busyDates").child(reservation.getChaletID())
                                        .child(reservation.getDate()).removeValue();
                                reservations.remove(reservation);
                                notifyDataSetChanged();

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
