package com.almortah.almortah;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ALMAHRI on 27/11/2017.
 */

public class RateChaletAdapter extends RecyclerView.Adapter<RateChaletAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reservation> reservations;
    private int progress = 0;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView chaletName;
        public TextView rate;
        public SeekBar rateBar;
        public TextView id;
        public Button submit;
        public EditText comment;


        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            rate = (TextView) view.findViewById(R.id.rate);
            id = (TextView) view.findViewById(R.id.id);
            rateBar = (SeekBar) view.findViewById(R.id.seekBar);
            comment = (EditText) view.findViewById(R.id.comment);
            submit = (Button) view.findViewById(R.id.submit);

        }
    }

    public RateChaletAdapter(@NonNull Context context, @NonNull ArrayList<Reservation> objects) {
        this.context = context;
        this.reservations = objects;
    }

    @Override
    public RateChaletAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.need_rate_item, parent, false);
        RateChaletAdapter.MyViewHolder mViewHold = new RateChaletAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final RateChaletAdapter.MyViewHolder holder, final int position) {
        final Reservation reservation = reservations.get(position);
        holder.chaletName.setText(reservation.getChaletName());
        holder.date.setText(reservation.getDate());
        holder.id.setText(reservation.getReservationID());
        holder.rateBar.setProgress(0);

        holder.rate.setText(holder.rateBar.getProgress() + " / " + holder.rateBar.getMax());
        holder.rateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                holder.rate.setText(progress + " / " + seekBar.getMax());
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
                        String comment = holder.comment.getText().toString().trim();
                        if (comment.matches(""))
                            comment = "-";
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("chaletID", reservation.getChaletID());
                        hashMap.put("comment", comment);
                        hashMap.put("rating", String.valueOf(progress));
                        reference.child("chaletRatings").child(reservation.getReservationID()).setValue(hashMap);
                        reference.child("reservation").child(reservation.getReservationID()).child("rated").setValue("1")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reference.child("chaletRatings").orderByChild("chaletID").equalTo(reservation.getChaletID())
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            int size = (int) dataSnapshot.getChildrenCount();
                                                            double sum = 0.0;
                                                            for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                                                chaletRatings chaletRating = singlValue.getValue(chaletRatings.class);
                                                                sum += Double.parseDouble(chaletRating.getRating());
                                                            }

                                                            double total = sum / size;
                                                            reference.child("chalets").child(reservation.getChaletID()).child("rating").setValue(String.valueOf(total));
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                    }
                                });

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        // Dismiss the progress dialog
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

