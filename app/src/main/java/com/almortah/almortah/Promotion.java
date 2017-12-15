package com.almortah.almortah;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Promotion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.inflateMenu(R.menu.owner_menu);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        final TextView tmpPrice = (TextView) findViewById(R.id.tmpPrice);
        tmpPrice.setText(tmpPrice.getText().toString()+" ("+getString(R.string.riyal)+")");

        setSupportActionBar(toolbar);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Bundle info = getIntent().getExtras();
        final Chalet chalet = (Chalet) info.getParcelable("chalet");
        final String chaletID = info.getString("id");
        Button submit = (Button) findViewById(R.id.submit);
        final RadioGroup offer = (RadioGroup) findViewById(R.id.offer);
        final RadioGroup payment = (RadioGroup) findViewById(R.id.payment);
        final RadioButton day,week,month;
        day = (RadioButton) findViewById(R.id.day);
        week = (RadioButton) findViewById(R.id.week);
        month = (RadioButton) findViewById(R.id.month);

       // Log.e("ID for chale?",chalet.getId());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offer.getCheckedRadioButtonId() != -1 && payment.getCheckedRadioButtonId() != -1) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Promotion.this);
                    alertDialogBuilder.setMessage(getString(R.string.sure));
                            alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Intent toPayment = new Intent(Promotion.this, PaymentPromotion.class);
                                            toPayment.putExtra("chalet",chalet);
                                            String duration = "";
                                            String price = "";
                                            if(day.isChecked()) {
                                                duration = "day";
                                                price = "10";
                                            }
                                            else if(week.isChecked()) {
                                                duration = "week";
                                                price = "50";
                                            }
                                            else {
                                                duration = "month";
                                                price = "180";
                                            }

                                            toPayment.putExtra("duration" , duration);
                                            toPayment.putExtra("price" , price);
                                            Date today = new Date(System.currentTimeMillis());
                                            Calendar calendar = new GregorianCalendar();
                                            calendar.setTime(today);
                                            int year = calendar.get(Calendar.YEAR);
                                            int month = calendar.get(Calendar.MONTH) + 1;
                                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                                            int toYear,toMonth,toDay;

                                            int noOfDays = 8; //i.e one week
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(today);
                                            c.add(Calendar.DAY_OF_YEAR, noOfDays);
                                            Date toDate = calendar.getTime();
                                            toYear = c.get(Calendar.YEAR);
                                            toMonth = c.get(Calendar.MONTH) + 1;
                                            toDay = c.get(Calendar.DAY_OF_MONTH);
                                            String finalDate = toDay + "-" + toMonth + "-" + toYear;
                                            String date = day +"-" + month +"-" + year;
                                            toPayment.putExtra("date" , date);
                                            toPayment.putExtra("toDate",finalDate);
                                            startActivity(toPayment);


//                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//                                            mDatabase.child("chalets").child(chaletID).child("promotion").setValue("1");
//                                            Toast.makeText(getApplicationContext(),R.string.donePromot,Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getBaseContext(),MyChalets.class));
                                        }
                                    });

                    alertDialogBuilder.setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
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
