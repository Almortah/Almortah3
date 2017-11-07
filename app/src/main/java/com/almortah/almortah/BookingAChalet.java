package com.almortah.almortah;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

public class BookingAChalet extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CalendarView calendarView;
    private Button checkBusy;
    private String date;
    private String finalDates = "";
    private TextView t;
    private DatabaseReference reference;

    private DrawerLayout drawer;

    private boolean isBusy = false;
    private boolean isWeekend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_achalet);
        Bundle info = getIntent().getExtras();
        final String ownerID = info.getString("ownerID");
        final String chaletNb = info.getString("chaletNb");
        final String name = info.getString("name");
        final Chalet chalet = (Chalet) getIntent().getSerializableExtra("chalet");

        final String normalPrice = info.getString("normalPrice");
        final String weekendPrice = info.getString("weekendPrice");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            navigationView.inflateMenu(R.menu.customer_menu);
        else
            navigationView.inflateMenu(R.menu.visitor_menu);


        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        Long l = System.currentTimeMillis();
        calendarView.setMinDate(l);


        t = (TextView) findViewById(R.id.dateChoose);
        checkBusy = (Button) findViewById(R.id.checkBusy);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (view.getDate() == System.currentTimeMillis())
                    return;
                date = dayOfMonth + "-" + (month + 1) + "-" + year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == 5 || dayOfWeek == 6 || dayOfWeek == 7)
                   isWeekend = true;
            }
        });


        checkBusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date == null)
                    return;
                //              long milliseconds = calendarView.getDate();
                //          SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                //            Date today = new Date(milliseconds);
//                date = sdf.format(today);
                reference = FirebaseDatabase.getInstance().getReference().child("busyDates").child(ownerID).child(chaletNb).child("busyOn");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            finalDates = dataSnapshot.getValue().toString();
                            String[] busyDates = dataSnapshot.getValue().toString().split(",");
                            for (int i = 0; i < busyDates.length; i++) {
                                if (busyDates[i].equals(date))
                                    isBusy = true;
                            }
                        }
                        if (!isBusy) {
                            Toast.makeText(BookingAChalet.this, R.string.freeDay, Toast.LENGTH_SHORT).show();
                            Intent toConfirm = new Intent(BookingAChalet.this, ConfirmBooking.class);
                            toConfirm.putExtra("date", date);
                            toConfirm.putExtra("ownerID", ownerID);
                            toConfirm.putExtra("chaletNb", chaletNb);
                            if (finalDates == null)
                                finalDates = "";
                                toConfirm.putExtra("price", isWeekend);
                            toConfirm.putExtra("finalDate", finalDates);
                            toConfirm.putExtra("name", name);
                            toConfirm.putExtra("chalet",chalet);
                            startActivity(toConfirm);

                        } else {
                            Toast.makeText(BookingAChalet.this, R.string.busyDay, Toast.LENGTH_SHORT).show();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        AlmortahDB almortahDB = new AlmortahDB(this);
        almortahDB.menu(item);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}


