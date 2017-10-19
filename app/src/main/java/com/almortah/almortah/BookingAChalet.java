package com.almortah.almortah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingAChalet extends AppCompatActivity {

    private CalendarView calendarView;
    private Button checkBusy;
    private String date;
    private String finalDates;
    private TextView t;
    private DatabaseReference reference;

    private boolean isBusy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_achalet);
        Bundle info = getIntent().getExtras();
        final String ownerID = info.getString("ownerID");
        final String chaletNb = info.getString("chaletNb");
        isBusy = false;




        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(1);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth+ "-" + month + "-" + year;
            }
        });

        t = (TextView) findViewById(R.id.dateChoose);
        checkBusy = (Button) findViewById(R.id.checkBusy);
        checkBusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("busyDates").child(ownerID).child(chaletNb).child("busyOn");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        finalDates = dataSnapshot.getValue().toString();
                        String[] busyDates = dataSnapshot.getValue().toString().split(",");
                        for (int i = 0; i < busyDates.length; i++) {
                            if(busyDates[i].equals(date))
                                isBusy = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if(!isBusy) {
                    Toast.makeText(BookingAChalet.this,"EMPTY",Toast.LENGTH_SHORT).show();
                    Intent toConfirm = new Intent(BookingAChalet.this,ConfirmBooking.class);
                    toConfirm.putExtra("date",date);
                    toConfirm.putExtra("ownerID",ownerID);
                    toConfirm.putExtra("chaletNb",chaletNb);
                    toConfirm.putExtra("finalDate",finalDates);
                    startActivity(toConfirm);

                }
                else {
                    Toast.makeText(BookingAChalet.this,"Not Available",Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}
