package com.almortah.almortah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ALMAHRI on 14/12/2017.
 */

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.MyViewHolder>  {

    private Context context;
    private Button control;
    private ArrayList<complaints> arrayList;
    String reason1; String reason2;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName ;
        public TextView chaletName;
        public TextView reasons;
        public Button chaletPage;
        public Button banCustomer;
        public Button dismiss;


        public MyViewHolder(View view) {
            super(view);
            customerName = (TextView) view.findViewById(R.id.customerName);
            chaletName = (TextView) view.findViewById(R.id.chaletName);
            reasons = (TextView) view.findViewById(R.id.reasons);

            chaletPage = (Button) view.findViewById(R.id.toChalet);
            banCustomer = (Button) view.findViewById(R.id.toCustomer);
            dismiss = (Button) view.findViewById(R.id.dismiss);

        }
    }

    public ComplaintsAdapter(@NonNull Context context, @NonNull ArrayList<complaints> objects) {
        this.context = context;
        this.arrayList = objects;
    }

    @Override
    public ComplaintsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complain_item, parent, false);
        ComplaintsAdapter.MyViewHolder mViewHold = new ComplaintsAdapter.MyViewHolder(mView);
        return mViewHold;
    }

    @Override
    public void onBindViewHolder(final ComplaintsAdapter.MyViewHolder holder, final int position) {
        complaints complain = arrayList.get(position);
        holder.chaletName.setText(complain.getChaletName());
        FirebaseDatabase.getInstance().getReference().child("users").child(complain.getCustomerID()).child("Name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.customerName.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        String[] reasons = complain.getReasons().split("-");
        String[] complaints = context.getResources().getStringArray(R.array.complaints);
        String finalText ="";
        for (int i=0; i<reasons.length; i++) {
            if(i != (reasons.length-1) )
            finalText += complaints[Integer.parseInt(reasons[i])] + System.getProperty ("line.separator");
            else
                finalText += complaints[Integer.parseInt(reasons[i])];
        }
        holder.reasons.setText(finalText);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
