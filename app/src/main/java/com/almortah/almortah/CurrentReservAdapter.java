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

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 26/11/2017.
 */

public class CurrentReservAdapter extends RecyclerView.Adapter<CurrentReservAdapter.MyViewHolder>  {

    private Context context;
    private Button control;
    private ArrayList<Reservation> reservations;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date ;
        public TextView chaletName;
        public TextView inTime ;
        public TextView outTime;
        public TextView payment ;
        public TextView id;
        public Button cancel;


        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            inTime = (TextView) view.findViewById(R.id.inTime);
            outTime = (TextView) view.findViewById(R.id.outTime);
            id = (TextView) view.findViewById(R.id.id);
            payment = (TextView) view.findViewById(R.id.payment);
            cancel = (Button) view.findViewById(R.id.cancel);

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

    @Override
    public void onBindViewHolder(final CurrentReservAdapter.MyViewHolder holder, final int position) {
        final Reservation reservation = reservations.get(position);
        holder.chaletName.setText(reservation.getChaletName());
        holder.date.setText(reservation.getDate());
        holder.inTime.setText(reservation.getCheckin());
        holder.outTime.setText(reservation.getCheckout());
        holder.id.setText(reservation.getReservationID());
        holder.payment.setText(reservation.getPayment()+" / "+reservation.getPrice() + context.getString(R.string.riyal));


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
