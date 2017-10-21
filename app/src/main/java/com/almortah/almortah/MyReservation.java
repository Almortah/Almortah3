package com.almortah.almortah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyReservation extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        listView = (ListView) findViewById(R.id.myHistory);
        final ArrayList<Reservation> myHistory = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("reservation");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                        Reservation reservation = singlValue.getValue(Reservation.class);
                        if (reservation.getCustomerID().equals(user.getUid())) {
                            myHistory.add(reservation);
                        }
                    }
                }
                MyReservationAdapter adapter = new MyReservationAdapter(MyReservation.this, myHistory);
                listView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


}

