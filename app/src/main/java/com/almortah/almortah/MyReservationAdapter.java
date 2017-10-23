package com.almortah.almortah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ALMAHRI on 10/21/17.
 */

public class MyReservationAdapter extends ArrayAdapter<Reservation> {

    private Context context;
    private Button control;

    public MyReservationAdapter(@NonNull Context context, @NonNull List<Reservation> objects) {
        super(context, 0, objects);
        this.context = context;
    }

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

        String check[] = isFuture.split("-");
        String reservDate[] = reservation.getDate().split("-");
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
    }
}
