package com.almortah.almortah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import br.com.moip.validators.CreditCard;

public class PaymentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private EditText cardNm;
    private EditText cardName;
    //private EditText mmYY;
    private EditText cvv;
    private CheckBox saveVisa;
    private Switch loadVisa;
    private Button pay;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Spinner mm ;
    private Spinner yy ;

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        Bundle info = getIntent().getExtras();
        final Reservation reservation = (Reservation) info.getParcelable("res");


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
        navigationView.inflateMenu(R.menu.customer_menu);

        cardNm = (EditText) findViewById(R.id.cardNum);
        cardName = (EditText) findViewById(R.id.cardName);
        cvv = (EditText) findViewById(R.id.cvv);
        //mmYY = (EditText) findViewById(R.id.mmYY);

        saveVisa = (CheckBox) findViewById(R.id.saveVisa);
        loadVisa = (Switch) findViewById(R.id.loadVisa);
        pay = (Button) findViewById(R.id.pay);
        logo = (ImageView) findViewById(R.id.logo);

        mm = (Spinner) findViewById(R.id.spinner1);
        yy = (Spinner) findViewById(R.id.spinner2);


        //ArrayList<String> listMM = new ArrayList<>();

        List<String> listMM = new ArrayList<String>();
        listMM.add("01");
        listMM.add("02");
        listMM.add("03");
        listMM.add("04");
        listMM.add("05");
        listMM.add("06");
        listMM.add("07");
        listMM.add("08");
        listMM.add("09");
        listMM.add("10");
        listMM.add("11");
        listMM.add("12");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMM);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mm.setAdapter(dataAdapter);



        List<String> listYY = new ArrayList<String>();
        listYY.add("18");
        listYY.add("19");
        listYY.add("20");
        listYY.add("21");
        listYY.add("22");
        listYY.add("23");
        listYY.add("24");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, listYY);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        yy.setAdapter(dataAdapter2);


        cardNm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 16) {
                    Log.e("Hi","CARD NUMBER");
                    CreditCard card = new CreditCard(s.toString().trim());
                    if(card.isValid()) {
                        Log.e("Hi","Good CARD NUMBER");
                        Log.e("Hi",card.getBrand().name());
                        if (card.getBrand().name().equals("VISA"))
                            logo.setImageResource(R.mipmap.visa);
                        else if (card.getBrand().name().equals("MASTERCARD"))
                            logo.setImageResource(R.mipmap.mastercard);
                        else if (card.getBrand().name().equals("AMEX"))
                            logo.setImageResource(R.mipmap.amex);
                    }
                }
            }
        });

//        mmYY.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length() == 4 && !s.toString().contains("/")) {
//                    char[] md = s.toString().toCharArray().clone();
//                    String tmp = md[0] + md[1] + "/" + md[2] + md[3];
//                    Log.e("FirstELM", String.valueOf(md[0]));
//                    Log.e("TMP", String.valueOf(tmp));
//
//                    mmYY.setText(tmp);
////                }
////            }
//
//            String lastInput ="";
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String input = s.toString();
//                if(input.length() == 5) {
//                    SimpleDateFormat formatter = new SimpleDateFormat("MM/yy");
//                    char[] md = s.toString().toCharArray();
//                    //mmYY.setText(md[0]+md[1]+"/"+md[2]+md[3]);
//                    Calendar expiryDateDate = Calendar.getInstance();
//                    String[] tmp = input.split("/");
//                    if (Integer.parseInt(tmp[0]) > 12 || Integer.parseInt(tmp[1]) > 25
//                            || Integer.parseInt(tmp[1]) < 18 || Integer.parseInt(tmp[0]) < 1)
//                        Toast.makeText(getBaseContext(), "mmYY????", Toast.LENGTH_SHORT).show();
//                    // expiryDateDate has a valid date from the user
//                    // Do something with expiryDateDate here
//                }
//            }
//        });

        loadVisa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    FirebaseDatabase.getInstance().getReference().child("savedCC")
                            .child(auth.getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        savedCC savedCC = dataSnapshot.getValue(savedCC.class);
                                        int finalMM = Integer.parseInt(savedCC.getMM()) -1;
                                        int finalYY = Integer.parseInt(savedCC.getYY());
                                        switch (finalYY) {
                                            case 18:
                                                finalYY = 0;
                                                break;
                                            case 19:
                                                finalYY = 1;
                                                break;
                                            case 20:
                                                finalYY = 2;
                                                break;
                                            case 21:
                                                finalYY = 3;
                                                break;
                                            case 22:
                                                finalYY = 4;
                                                break;
                                            case 23:
                                                finalYY = 5;
                                                break;
                                            case 24:
                                                finalYY = 6;
                                                break;
                                        }
                                        cardNm.setText(savedCC.getCardNum());
                                        cardName.setText(savedCC.getCardName());
                                        cvv.setText(savedCC.getCvv());
                                        mm.setSelection(finalMM);
                                        yy.setSelection(finalYY);
                                        saveVisa.setVisibility(View.INVISIBLE);



                                    }
                                    else {
                                        loadVisa.setChecked(false);
                                        Toast.makeText(getBaseContext(),R.string.noLoad,Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }

                else {
                    cardNm.setText("");
                    cardName.setText("");
                    logo.setVisibility(View.INVISIBLE);
                    cvv.setText("");
                    mm.setSelection(0);
                    yy.setSelection(0);
                    saveVisa.setVisibility(View.VISIBLE);

                }
            }
        });

        final Calendar today = Calendar.getInstance();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String[] tmp = mmYY.getText().toString().split("/");
                CreditCard card = new CreditCard(cardNm.getText().toString());
                if(!card.isValid()) {
                    return;
                }

                if(cvv.getText().length() != 3) {
                    return;
                }


                if(saveVisa.isChecked()) {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("cardNum",cardNm.getText().toString().trim());
                    map.put("cardName",cardName.getText().toString().trim());
                    map.put("MM",mm.getSelectedItem().toString());
                    map.put("YY",yy.getSelectedItem().toString());
                    map.put("cvv",cvv.getText().toString().trim());
                    FirebaseDatabase.getInstance().getReference().child("savedCC").child(auth.getCurrentUser().getUid()).setValue(map);
                }

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("customerID",auth.getCurrentUser().getUid());
                map.put("ownerID",reservation.getOwnerID());
                map.put("chaletID",reservation.getChaletID());
                map.put("date",reservation.getDate());
                map.put("checkin",reservation.getCheckin());
                map.put("checkout","2:00");
                map.put("payment","visa");
                map.put("price",reservation.getPrice());
                map.put("confirm","0");
                map.put("ratedCustomer","0");
                map.put("chaletName",reservation.getChaletName());
                map.put("rated","0");
                map.put("reservationID", reservation.getReservationID());
                FirebaseDatabase.getInstance().getReference()
                        .child("reservation").child(reservation.getReservationID())
                        .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference().child("busyDates").
                                child(reservation.getChaletID()).child(reservation.getDate()).setValue(auth.getCurrentUser().getUid());
                        startActivity(new Intent(PaymentPage.this,CurrentReservations.class));
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
