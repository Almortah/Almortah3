package com.almortah.almortah;

import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ConfirmBooking extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String date;
    private String finalDates;
    private DatabaseReference reference;
    private String upDate ;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String checkin;
    private TimePicker inTime;
    private String price;
    private String payment;
    private int oneTimes = 0;
    private int TIME_PICKER_INTERVAL = 30;
    private String app_server_url = "http://almortah2017-001-site1.etempurl.com/insert_values.php";
    private String url_notify = "http://almortah2017-001-site1.etempurl.com/send_notofication.php";

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        Bundle info = getIntent().getExtras();
        date = info.getString("name");
        final Chalet chalet = (Chalet) info.getParcelable("chalet");
        finalDates = info.getString("finalDates");
        boolean finalPrice = info.getBoolean("price");
        if(finalPrice)
            price = chalet.getWeekendPrice();
        else
            price = chalet.getNormalPrice();


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

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            navigationView.inflateMenu(R.menu.customer_menu);
        else
            navigationView.inflateMenu(R.menu.visitor_menu);



        inTime = (TimePicker) findViewById(R.id.checkin);
        inTime.setCurrentHour(11);
        inTime.setCurrentMinute(0);
       // setTimePickerInterval(inTime);


        upDate = finalDates+","+date;
        reference = FirebaseDatabase.getInstance().getReference().child("busyDates").child(chalet.getOwnerID())
                .child(chalet.getChaletNm()).child("busyOn");
        TextView dateView = (TextView) findViewById(R.id.date);
        dateView.setText(dateView.getText().toString()+" "+date);

        TextView priceView = (TextView) findViewById(R.id.finalPrice);
        priceView.setText(getString(R.string.price) +": "+ price);


        //Map<String, Object> update = new HashMap<String, Object>();
        //update.put("busyOn", new String(upDate));
        //reference.updateChildren(update);
       // Toast.makeText(getApplicationContext(),"Now its busy",Toast.LENGTH_SHORT).show();

        Button confirm = (Button) findViewById(R.id.confirm);
        payment = "cash";
        final RadioButton visa = (RadioButton) findViewById(R.id.visa);


        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(oneTimes == 1 || date == null) {
                    Toast.makeText(getApplicationContext(),R.string.oneTimeOnly,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(),MyReservation.class));
                    return;
                }
                oneTimes++;
                if(visa.isChecked())
                    payment = "visa";
                checkin = String.valueOf(inTime.getHour()+":"+inTime.getMinute());
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("customerID",mAuth.getCurrentUser().getUid());
                map.put("ownerID",chalet.getOwnerID());
                map.put("chaletID",chalet.getId());



                map.put("date",date);
                map.put("checkin",checkin);
                map.put("checkout","2:00");
                map.put("payment",payment);
                map.put("price",price);
                map.put("confirm","0");
                map.put("ratedCustomer","0");
                map.put("chaletName",chalet.getName());
                map.put("rated","0");
              final  String id = mDatabase.child("reservation").push().getKey();
              final String token = SharedPrefManager.getmInstance(getApplicationContext()).getToken();
              final int resID = (int) System.currentTimeMillis();

                map.put("reservationID", String.valueOf(resID));
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(SharedPrefManager.getmInstance(getApplicationContext()).getToken()!=null){
                    Log.i("Token", token);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            //params.put("fcm_token",token);
                            params.put("reservation_id",id);
                            params.put("owner_id",chalet.getOwnerID());
                            params.put("chalet_id",chalet.getId());
                            params.put("owner_token",chalet.getOwnerToken());
                            params.put("customer_id",user.getUid());
                            params.put("customer_token",token);

                            return params;
                        }
                    };
                    MySingleton.getmInstance(ConfirmBooking.this).addToRequestque(stringRequest);
                }

                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url_notify,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("owner_token",chalet.getOwnerToken());
                        params.put("title","AlMortah");
                        params.put("body","You have new reservation on "+chalet.getName());
                        params.put("customer_token",token);

                        return params;
                    }
                };
                MySingleton.getmInstance(ConfirmBooking.this).addToRequestque(stringRequest2);

                mDatabase.child("reservation").child(String.valueOf(resID)).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseDatabase.getInstance().getReference().child("busyDates").
                                child(chalet.getId()).child(date).setValue(mAuth.getCurrentUser().getUid());
                                startActivity(new Intent(ConfirmBooking.this,CurrentReservations.class));
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

