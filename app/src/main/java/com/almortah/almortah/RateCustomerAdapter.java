package com.almortah.almortah;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ALMAHRI on 02/12/2017.
 */

public class RateCustomerAdapter extends RecyclerView.Adapter<RateCustomerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reservation> reservations;
    private int cleanProgress = 0;
    private int priceProgress = 0;
    private FirebaseUser user;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView chaletName;

        public TextView cleanRate;
        public SeekBar cleanBar;

        public TextView priceRate;
        public SeekBar priceBar;


        public TextView id;
        public Button submit;

        public TextView customerName;


        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            id = (TextView) view.findViewById(R.id.id);
            submit = (Button) view.findViewById(R.id.submit);

            cleanRate = (TextView) view.findViewById(R.id.cleanRate);
            cleanBar = (SeekBar) view.findViewById(R.id.cleanBar);

            priceRate = (TextView) view.findViewById(R.id.priceRate);
            priceBar = (SeekBar) view.findViewById(R.id.priceBar);

            customerName = (TextView) view.findViewById(R.id.customerName);
        }
    }

    public RateCustomerAdapter(@NonNull Context context, @NonNull ArrayList<Reservation> objects, FirebaseUser user1) {
        this.context = context;
        this.reservations = objects;
        this.user = user1;
    }

    @Override
    public RateCustomerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_customer_item, parent, false);
        RateCustomerAdapter.MyViewHolder mViewHold = new RateCustomerAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final RateCustomerAdapter.MyViewHolder holder, final int position) {
        final Reservation reservation = reservations.get(position);
        holder.chaletName.setText(reservation.getChaletName());
        holder.date.setText(reservation.getDate());
        holder.id.setText(reservation.getReservationID());
        holder.cleanBar.setProgress(0);
        holder.priceBar.setProgress(0);

        reference.child("users").child(reservation.getCustomerID()).child("Name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            holder.customerName.setText(dataSnapshot.getValue().toString());
                        }
                        else
                            holder.customerName.setText(reservation.getCustomerID());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        holder.cleanRate.setText(holder.cleanBar.getProgress() + " / " + holder.cleanBar.getMax());
        holder.cleanBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                cleanProgress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                holder.cleanRate.setText(cleanProgress + " / " + seekBar.getMax());
            }
        });

        holder.priceRate.setText(holder.priceBar.getProgress() + " / " + holder.priceBar.getMax());
        holder.priceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                priceProgress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                holder.priceRate.setText(priceProgress + " / " + seekBar.getMax());
            }
        });


        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {
                    private ProgressDialog pDialog;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(context);
                        pDialog.setMessage(context.getString(R.string.wait));
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        Log.e("USER NAME?",user.getEmail());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("chaletID", reservation.getChaletID());
                        hashMap.put("cleanRating", String.valueOf( (Double) (cleanProgress/2.0) ));
                        hashMap.put("paymentRating", String.valueOf( (Double) (priceProgress/2.0) ));
                        hashMap.put("ownerID", reservation.getOwnerID());
                        hashMap.put("customerID", reservation.getCustomerID());
                        reference.child("customerRatings").child(reservation.getReservationID()).setValue(hashMap);
                        reference.child("reservation").child(reservation.getReservationID()).child("ratedCustomer").setValue("1");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        // Dismiss the cleanProgress dialog
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        reservations.remove(position);
                        notifyItemRemoved(position);
                    }
                };

                updateTask.execute();


            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }


}

