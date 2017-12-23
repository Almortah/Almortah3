package com.almortah.almortah;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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
        final complaints complain = arrayList.get(position);
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

        holder.chaletPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FirebaseDatabase.getInstance().getReference().child("chalet").child(complain.getChaletID())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    Chalet chalet = dataSnapshot.getValue(Chalet.class);
                                    Intent intent = new Intent(v.getContext(),ChaletInfoCustomer.class);
                                    intent.putExtra("chalet",chalet);
                                    v.getContext().startActivity(intent);
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });

        holder.banCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage(context.getString(R.string.sure));
                alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                final HashMap<String,String> map = new HashMap<>();
                                FirebaseDatabase.getInstance().getReference().child("users").child(complain.getCustomerID())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()) {
                                                    final Users user = dataSnapshot.getValue(Users.class);
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
                                                                    DatabaseReference reference =  FirebaseDatabase.getInstance().getReference();
                                                                    reference.child("blacklistReasons").child(complain.getCustomerID()).child("reasons").setValue(reasons); //Reasons First!
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
                                                                                    arrayList.remove(complain);
                                                                                    arrayList.clear();
                                                                                    notifyDataSetChanged();
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .setNegativeButton(context.getString(R.string.cancel1), new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    //  Your code when user clicked on Cancel
                                                                    dialog.cancel();
                                                                }
                                                            });

                                                    dialog = builder.create();//AlertDialog dialog; create like this outside onClick
                                                    dialog.show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
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


        holder.dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(complain);
                arrayList.clear();
                notifyDataSetChanged();
                FirebaseDatabase.getInstance().getReference().child("complaints").child("isDismiss").setValue("1");
            }
        });






    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
