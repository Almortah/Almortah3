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
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 15/12/2017.
 */

public class ApproveOwnersAdapter extends RecyclerView.Adapter<ApproveOwnersAdapter.MyViewHolder>  {

    private Context context;
    private ArrayList<Users> arrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name ;
        public TextView phone;
        public TextView email;

        public Button accept;
        public Button reject;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.ownerName);
            phone = (TextView) view.findViewById(R.id.phone);
            email = (TextView) view.findViewById(R.id.email);

            accept = (Button) view.findViewById(R.id.accept);
            reject = (Button) view.findViewById(R.id.reject);

        }
    }

    public ApproveOwnersAdapter(@NonNull Context context, @NonNull ArrayList<Users> objects) {
        this.context = context;
        this.arrayList = objects;
    }

    @Override
    public ApproveOwnersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.approve_owners_item, parent, false);
        ApproveOwnersAdapter.MyViewHolder mViewHold = new ApproveOwnersAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final ApproveOwnersAdapter.MyViewHolder holder, final int position) {
        final Users user = arrayList.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhone());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(context.getString(R.string.sure));
                alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUserID()).child("isApproved").setValue("1");
                                arrayList.remove(position);
                                Toast.makeText(context,R.string.promoted,Toast.LENGTH_SHORT).show();
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

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(context.getString(R.string.sure));
                alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arrayList.remove(position);
                                Toast.makeText(context,R.string.rejected,Toast.LENGTH_SHORT).show();
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
        return arrayList.size();
    }
}
