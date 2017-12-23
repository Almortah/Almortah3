package com.almortah.almortah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ALMAHRI on 27/11/2017.
 */

public class RateChaletAdapter extends RecyclerView.Adapter<RateChaletAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reservation> reservations;
    private int cleanProgress = 0;
    private int priceProgress = 0;
    private int reciptProgress = 0;
    private FirebaseUser user;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView chaletName;

        public TextView cleanRate;
        public SeekBar cleanBar;

        public TextView priceRate;
        public SeekBar priceBar;

        public TextView reciptRate;
        public SeekBar reciptBar;

        public TextView id;
        public Button submit;
        public EditText comment;


        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            id = (TextView) view.findViewById(R.id.id);
            comment = (EditText) view.findViewById(R.id.comment);
            submit = (Button) view.findViewById(R.id.submit);

            cleanRate = (TextView) view.findViewById(R.id.cleanRate);
            cleanBar = (SeekBar) view.findViewById(R.id.cleanBar);

            reciptRate = (TextView) view.findViewById(R.id.reciptRate);
            reciptBar = (SeekBar) view.findViewById(R.id.reciptBar);

            priceRate = (TextView) view.findViewById(R.id.priceRate);
            priceBar = (SeekBar) view.findViewById(R.id.priceBar);

        }
    }

    public RateChaletAdapter(@NonNull Context context, @NonNull ArrayList<Reservation> objects, FirebaseUser user1) {
        this.context = context;
        this.reservations = objects;
        this.user = user1;
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
        holder.cleanBar.setProgress(0);
        holder.priceBar.setProgress(0);
        holder.reciptBar.setProgress(0);


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

        holder.reciptRate.setText(holder.reciptBar.getProgress() + " / " + holder.reciptBar.getMax());
        holder.reciptBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                reciptProgress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                holder.reciptRate.setText(reciptProgress + " / " + seekBar.getMax());
            }
        });


        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = holder.comment.getText().toString().trim();
                Log.e("USER NAME?",user.getEmail());
                if (comment.matches(""))
                    comment = "-";
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("chaletID", reservation.getChaletID());
                hashMap.put("comment", comment);
                hashMap.put("cleanRating", String.valueOf( (Double) (cleanProgress/2.0) ));
                hashMap.put("reicptRating", String.valueOf( (Double) (reciptProgress/2.0) ));
                hashMap.put("priceRating", String.valueOf( (Double) (priceProgress/2.0) ));
                hashMap.put("customerName", user.getEmail());
                hashMap.put("customerID",reservation.getCustomerID());
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
                                                    double avgRating = 0.0;
                                                    for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                                        chaletRatings chaletRating = singlValue.getValue(chaletRatings.class);
                                                        avgRating =  (Double.parseDouble(chaletRating.getCleanRating()) +
                                                                Double.parseDouble(chaletRating.getPriceRating()) +
                                                                Double.parseDouble(chaletRating.getReicptRating()))  / 3 ;
                                                        Log.e("avgRating:", String.valueOf(avgRating));
                                                        sum += avgRating;
                                                    }
                                                    Log.e("sum::", String.valueOf(sum));

                                                    double total = sum / size;
                                                    DecimalFormat df = new DecimalFormat("#.00");
                                                    String totalFormated = df.format(total);
                                                    Log.e("TOTAL:", String.valueOf(total));
                                                    reference.child("chalets").child(reservation.getChaletID()).child("rating").setValue(totalFormated);

                                                    reservations.remove(reservation);
                                                    reservations.clear();
                                                    notifyDataSetChanged();
                                                    //holder.itemView.setVisibility(View.GONE);


                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }
                        });
                }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }


}

