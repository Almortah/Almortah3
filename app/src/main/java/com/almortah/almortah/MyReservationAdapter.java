package com.almortah.almortah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 10/21/17.
 */

public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.MyViewHolder>  {

    private Context context;
    private Button control;
    private ArrayList<Reservation> reservations;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date ;
        public TextView chaletName;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
        }
    }

    public MyReservationAdapter(@NonNull Context context, @NonNull ArrayList<Reservation> objects) {
        this.context = context;
        this.reservations = objects;
    }

    @Override
    public MyReservationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_reservation_item, parent, false);
        MyReservationAdapter.MyViewHolder mViewHold = new MyReservationAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final MyReservationAdapter.MyViewHolder holder, final int position) {
        final Reservation reservation = reservations.get(position);
        holder.chaletName.setText(reservation.getChaletName());
        holder.date.setText(reservation.getDate());

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }


    /*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.my_reservation_item, parent, false);
        }
        Reservation reservation = getItem(position);
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        TextView chaletName = (TextView) listItemView.findViewById(R.id.chaletName);
        control = (Button) listItemView.findViewById(R.id.control);

        date.setText(reservation.getDate());
        chaletName.setText(reservation.getChaletName());

        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date(yourmilliseconds);

        String isFuture = sdf.format(today);

        final String check[] = isFuture.split("-");
        final String reservDate[] = reservation.getDate().split("-");
        if(Integer.parseInt(check[1]) < Integer.parseInt(reservDate[1]) || Integer.parseInt(check[2]) < Integer.parseInt(reservDate[2]) ) {
            control.setText(R.string.cancel);
            control.setBackgroundColor(listItemView.getResources().getColor(R.color.colorDarkGrey));
        }
        else if (Integer.parseInt(check[1]) == Integer.parseInt(reservDate[1]) && Integer.parseInt(check[2]) == Integer.parseInt(reservDate[2])  ) {
            if(Integer.parseInt(check[0]) < Integer.parseInt(reservDate[0]) ) {
                control.setText(R.string.cancel);
                control.setBackgroundColor(listItemView.getResources().getColor(R.color.colorDarkGrey));
            }
        }




        return listItemView;
    }*/
}
