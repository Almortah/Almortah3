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
 * Created by ALMAHRI on 30/11/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>  {

    private Context context;
    private Button control;
    private ArrayList<Rating> ratings;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView comment;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            comment = (TextView) view.findViewById(R.id.comment);
        }
    }

    public CommentsAdapter(@NonNull Context context, @NonNull ArrayList<Rating> objects) {
        this.context = context;
        this.ratings = objects;
    }

    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        CommentsAdapter.MyViewHolder mViewHold = new CommentsAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.MyViewHolder holder, final int position) {
        final Rating rating = ratings.get(position);
        holder.comment.setText(rating.getComment());
        holder.name.setText("#" + " " + (position+1));
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }




}
