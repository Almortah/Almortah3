package com.almortah.almortah;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ALMAHRI on 11/10/17.
 */

public class UseresRV extends RecyclerView.Adapter<UseresRV.MyViewHolder> {
    private ArrayList<Users> users;
    private Context context;
    private FirebaseAuth auth;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private int banCount ;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView email;
        public TextView phone;
        public TextView fullname;
        public Button manage;
        public Button delete;

        public MyViewHolder(View view) {
            super(view);
            email = (TextView) view.findViewById(R.id.email);
            phone = (TextView) view.findViewById(R.id.phone);
            manage = (Button) view.findViewById(R.id.manage);
            delete = (Button) view.findViewById(R.id.delete);
            fullname = (TextView) view.findViewById(R.id.fullname);
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
    public void onBindViewHolder(final UseresRV.MyViewHolder holder, final int position) {
        final Users user = users.get(position);
        holder.email.setText(user.getEmail());
        holder.phone.setText(user.getPhone());
        holder.fullname.setText(user.getName());
        final String type;
        if(user.getType().equals("1"))
            type = context.getString(R.string.customer);
        else
            type = context.getString(R.string.owner);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(context.getString(R.string.sure));
                alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                final HashMap<String,String> map = new HashMap<>();

                                AlertDialog dialog;
                                final CharSequence[] items = context.getResources().getStringArray(R.array.reasons);
                                        //{" Easy "," Medium "," Hard "," Very Hard "};
                                // arraylist to keep the selected items
                                final ArrayList seletedItems=new ArrayList();

                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(context.getString(R.string.reson));
                                builder.setMultiChoiceItems(items, null,
                                        new DialogInterface.OnMultiChoiceClickListener() {
                                            // indexSelected contains the index of item (of which checkbox checked)
                                            @Override
                                            public void onClick(DialogInterface dialog, int indexSelected,
                                                                boolean isChecked) {
                                                if (isChecked) {
                                                    // If the user checked the item, add it to the selected items
                                                    // write your code when user checked the checkbox
                                                    seletedItems.add(indexSelected);
                                                } else if (seletedItems.contains(indexSelected)) {
                                                    // Else, if the item is already in the array, remove it
                                                    // write your code when user Uchecked the checkbox
                                                    seletedItems.remove(Integer.valueOf(indexSelected));
                                                }
                                            }
                                        })
                                        // Set the action buttons
                                        .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                String reasons = "";
                                                if (seletedItems.isEmpty())
                                                    return;

                                                for (int i = 0; i < seletedItems.size(); i++) {
                                                    int tmp = (int) seletedItems.get(i);
                                                    reasons += tmp + "-";
                                                }
                                                reference.child("blacklistReasons").child(user.getUserID()).child("reasons").setValue(reasons); //Reasons First!
                                                HashMap<String, String> hashMap = new HashMap<String, String>(); // Move to blacklist
                                                hashMap.put("Name", user.getName());
                                                hashMap.put("username", user.getUsername());
                                                hashMap.put("phone", user.getPhone());
                                                hashMap.put("email", user.getEmail());
                                                hashMap.put("nbChalets","0");
                                                hashMap.put("userID",user.getUserID());
                                                hashMap.put("type",user.getType());
                                                reference.child("blacklist")
                                                        .child(user.getUserID()).setValue(hashMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseDatabase.getInstance().getReference()
                                                              .child("users").child(user.getUserID()).removeValue();
                                                        users.remove(user);
                                                        users.clear();
                                                        notifyDataSetChanged();
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton(context.getString(R.string.cancel1), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Your code when user clicked on Cancel
                                                builder.create().cancel();
                                            }
                                        });

                                dialog = builder.create();//AlertDialog dialog; create like this outside onClick
                                dialog.show();
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


        holder.manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog d = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.info))
                        .setNegativeButton(context.getString(R.string.ok), null)
                        .setItems(new String[]{
                                        context.getString(R.string.fullname)+": "+user.getName(),
                                        context.getString(R.string.emailAddress)+": "+user.getEmail(),
                                        context.getString(R.string.phoneNum)+": "+user.getPhone(),
                                        context.getString(R.string.username)+": "+user.getUsername(),
                                        context.getString(R.string.type)+": "+type,
                                        context.getString(R.string.nbChalets)+": "+user.getNbChalets(),
                                },

                                new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dlg, int position)
                            {
                            }
                        })
                        .create();
                d.show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return users.size();
    }

}
