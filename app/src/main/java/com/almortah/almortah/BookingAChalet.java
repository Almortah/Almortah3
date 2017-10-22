package com.almortah.almortah;

import android.content.Intent;
import android.icu.util.Calendar;
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
    private String finalDates = "";
    private TextView t;
    private DatabaseReference reference;

    private boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_achalet);
        Bundle info = getIntent().getExtras();
        final String ownerID = info.getString("ownerID");
        final String chaletNb = info.getString("chaletNb");
        final String name = info.getString("name");

        final String normalPrice = info.getString("normalPrice");
        final String weekendPrice = info.getString("weekendPrice");



        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
       // calendarView.setMinDate(System.currentTimeMillis());


        t = (TextView) findViewById(R.id.dateChoose);
        checkBusy = (Button) findViewById(R.id.checkBusy);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth+ "-" + (month+1) + "-" + year;
            }
        });


        checkBusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  //              long milliseconds = calendarView.getDate();
      //          SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    //            Date today = new Date(milliseconds);
//                date = sdf.format(today);
                reference = FirebaseDatabase.getInstance().getReference().child("busyDates").child(ownerID).child(chaletNb).child("busyOn");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            finalDates = dataSnapshot.getValue().toString();
                            String[] busyDates = dataSnapshot.getValue().toString().split(",");
                            for (int i = 0; i < busyDates.length; i++) {
                                if (busyDates[i].equals(date))
                                    isBusy = true;
                            }
                        }
                        if(!isBusy) {
                            Toast.makeText(BookingAChalet.this, R.string.freeDay,Toast.LENGTH_SHORT).show();
                            Intent toConfirm = new Intent(BookingAChalet.this,ConfirmBooking.class);
                            toConfirm.putExtra("date",date);
                            toConfirm.putExtra("ownerID",ownerID);
                            toConfirm.putExtra("chaletNb",chaletNb);
                            if(finalDates == null)
                                finalDates = "";
                            toConfirm.putExtra("price",normalPrice);
                            toConfirm.putExtra("finalDate",finalDates);
                            toConfirm.putExtra("name",name);
                            startActivity(toConfirm);

                        }
                        else {
                            Toast.makeText(BookingAChalet.this,R.string.busyDay,Toast.LENGTH_SHORT).show();
                            isBusy = false;

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });



    }
}
