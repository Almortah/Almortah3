package com.almortah.almortah;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Statics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private RecyclerView recyclerView;

    private int nbOfReservations;
    private int totalRevenue;

    private ProgressDialog progressDialog;

    private ArrayList<StaticsItem> arrayList;
    private StaticsAdapter adapter;
    private ArrayList<Chalet> chalets;
    //private Chalet chalet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statics);

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
        navigationView.inflateMenu(R.menu.owner_menu);
        //Chalet chalet;



        progressDialog = new ProgressDialog(Statics.this);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(null);
        arrayList = new ArrayList<>();
        adapter = new StaticsAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        chalets = new ArrayList<>();
        new GetChalets().execute();

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AlmortahDB almortahDB = new AlmortahDB(this);
        almortahDB.menu(item);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getRepeatedString(ArrayList football_club) {
        List<String> List1 = football_club;
        HashMap<String, Integer> data = new HashMap<String, Integer>();
        int max = 0;
        for (String string : List1) {
            int count = 1;
            if (data.containsKey(string)) {
                count = data.get(string) + 1;
                data.put(string, count);
            } else {
                data.put(string, 1);
            }
            if (max < count)
                max =count;
        }
        System.out.println(data);
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if (max == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }


    private class GetChalets extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase.getInstance().getReference().child("chalets").orderByChild("ownerID")
                    .equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("Exists?", String.valueOf(dataSnapshot.exists()));
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                            final StaticsItem item = new StaticsItem();
                            final Chalet chalet = singlValue.getValue(Chalet.class);
                            chalets.add(chalet);
                            item.setChaletName(chalet.getName());
                            item.setAvgRating(chalet.getRating());
                            Log.e("name?", item.getChaletName());
                            Log.e("rating?", item.getAvgRating());


                            FirebaseDatabase.getInstance().getReference().child("reservation").orderByChild("chaletID")
                                    .equalTo(chalet.getId())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()) {
                                                nbOfReservations = (int) dataSnapshot.getChildrenCount();
                                                item.setTotalReservation(String.valueOf(nbOfReservations));
                                                totalRevenue = 0;
                                                ArrayList<String> array = new ArrayList<>();

                                                for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                                    Reservation reservation = singlValue.getValue(Reservation.class);
                                                    totalRevenue += Integer.parseInt(reservation.getPrice());
                                                    array.add(reservation.getCustomerID());
                                                }

                                                String popular = getRepeatedString(array);
                                                Log.e("popular?", String.valueOf(popular) );
                                                item.setTotalRevenue(totalRevenue);

                                                FirebaseDatabase.getInstance().getReference().child("users")
                                                        .child(popular).child("Name")
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Log.e("findName?", String.valueOf(dataSnapshot.exists()));
                                                                if (dataSnapshot.exists()) {
                                                                    item.setBestCustomer(dataSnapshot.getValue().toString());
                                                                    Log.e("Name?", dataSnapshot.getValue().toString());
                                                                    FirebaseDatabase.getInstance().getReference().child("chaletRatings").orderByChild("chaletID")
                                                                            .equalTo(chalet.getId()).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if(dataSnapshot.exists()) {
                                                                                double totalCleaning = 0.0;
                                                                                double totalRecipt = 0.0;
                                                                                double totalPrice = 0.0;
                                                                                int totalItems = (int) dataSnapshot.getChildrenCount();

                                                                                for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                                                                    chaletRatings chaletRatings = singlValue.getValue(com.almortah.almortah.chaletRatings.class);
                                                                                    totalCleaning += Double.parseDouble(chaletRatings.getCleanRating());
                                                                                    totalRecipt += Double.parseDouble(chaletRatings.getReicptRating());
                                                                                    totalPrice += Double.parseDouble(chaletRatings.getPriceRating());
                                                                                }

                                                                                DecimalFormat df = new DecimalFormat("#.##");
                                                                                double finalCleanRate = totalCleaning/totalItems;
                                                                                double finalReciptRate = totalRecipt/totalItems;
                                                                                double finalPriceRate = totalPrice/totalItems;
                                                                                item.setCleanRating(df.format(finalCleanRate));
                                                                                item.setReicptRating(df.format(finalReciptRate));
                                                                                item.setPriceRating(df.format(finalPriceRate));
                                                                                //item.setBestCustomer("--");

                                                                                StaticsItem tmp = new StaticsItem(chalet.getName(), String.valueOf(nbOfReservations),
                                                                                        chalet.getRating(),"--",totalRevenue, df.format(finalPriceRate)
                                                                                        , df.format(finalReciptRate), df.format(finalCleanRate) );
                                                                                //tmp.setTotalRevenue(totalRevenue);
                                                                                if (item.isOK()) {
                                                                                    arrayList.add(item);
                                                                                    Log.e("اضافه", item.getChaletName());
                                                                                }
                                                                                adapter.notifyDataSetChanged();
                                                                                Log.e("SizeOfArray", String.valueOf(arrayList.size()));

                                                                                //String chaletName, String totalReservation, String avgRating, String bestCustomer, int totalRevenue, String priceRating, String reicptRating, String cleanRating) {
                                                                            }

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        }

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                        Log.e("NotifyNOW", "---***---");
                        adapter.notifyDataSetChanged();
                        Log.e("SizeOfArray2", String.valueOf(arrayList.size()));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            adapter.notifyDataSetChanged();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            adapter.notifyDataSetChanged();
            HashSet<Chalet> hashSet = new HashSet<Chalet>();
            hashSet.addAll(chalets);
            chalets.clear();
            chalets.addAll(hashSet);
            adapter.notifyDataSetChanged();
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }

    }

}
