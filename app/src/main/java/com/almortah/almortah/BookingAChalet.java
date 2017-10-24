package com.almortah.almortah;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;

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
        calendarView.setMinDate(System.currentTimeMillis());


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.visitor_menu, menu);
        } else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.customer_menu, menu);
        }


        return true;
    }

    /**
     * Event Handling for Individual visitor_menu item selected
     * Identify single visitor_menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchChaleh:
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,HomeActivity.class));
                return true;
            case R.id.login:
                startActivity(new Intent(this,login.class));
                return true;
            case R.id.register:
                startActivity(new Intent(this,Signup.class));
                return true;
            case R.id.history:
                startActivity(new Intent(this,MyReservation.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e("MyActivity", "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

}
