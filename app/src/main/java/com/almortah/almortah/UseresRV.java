package com.almortah.almortah;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 11/10/17.
 */

public class UseresRV extends RecyclerView.Adapter<UseresRV.MyViewHolder> {
    private ArrayList<Users> users;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView email;
        public TextView phone;
        public Button manage;
        public Button delete;

        public MyViewHolder(View view) {
            super(view);
            email = (TextView) view.findViewById(R.id.email);
            phone = (TextView) view.findViewById(R.id.phone);
            manage = (Button) view.findViewById(R.id.manage);
            delete = (Button) view.findViewById(R.id.delete);
        }
    }

    public UseresRV(Context context, ArrayList<Users> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public UseresRV.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        UseresRV.MyViewHolder mViewHold = new UseresRV.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final UseresRV.MyViewHolder holder, int position) {
        final Users user = users.get(position);
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhone());

    }


    @Override
    public int getItemCount() {
        return users.size();
    }

}
