package com.almortah.almortah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 20/12/2017.
 */

public class StaticsAdapter extends RecyclerView.Adapter<StaticsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<StaticsItem> items;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView totalReservations;
        public TextView chaletName;

        public TextView customerName;
        public TextView priceRate;

        public TextView rating;
        public TextView totalRevenue;

        public TextView reciptRate;
        public TextView cleanRate;



        public MyViewHolder(View view) {
            super(view);
            totalReservations = (TextView) view.findViewById(R.id.nbReservation);
            chaletName = (TextView) view.findViewById(R.id.chaletName);

            cleanRate = (TextView) view.findViewById(R.id.cleanRate);
            customerName = (TextView) view.findViewById(R.id.customerName);
            totalRevenue = (TextView) view.findViewById(R.id.totalPayments);

            reciptRate = (TextView) view.findViewById(R.id.reciptRate);
            priceRate = (TextView) view.findViewById(R.id.priceRate);
            rating = (TextView) view.findViewById(R.id.rating);

        }
    }

    public StaticsAdapter(@NonNull Context context, @NonNull ArrayList<StaticsItem> objects) {
        this.context = context;
        this.items = objects;
    }

    @Override
    public StaticsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_item, parent, false);
        StaticsAdapter.MyViewHolder mViewHold = new StaticsAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final StaticsAdapter.MyViewHolder holder, final int position) {
        final StaticsItem item = items.get(position);
        holder.cleanRate.setText(item.getCleanRating() +" / 5");
        holder.priceRate.setText(item.getPriceRating() +" / 5");
        holder.reciptRate.setText(item.getReicptRating() +" / 5");
        holder.rating.setText(item.getAvgRating() + " / 5");
        holder.customerName.setText(item.getBestCustomer());
        holder.totalRevenue.setText( String.valueOf(item.getTotalRevenue()) + " " +context.getString(R.string.riyal));
       holder.totalReservations.setText( String.valueOf(item.getTotalReservation()) );
        holder.chaletName.setText(item.getChaletName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

