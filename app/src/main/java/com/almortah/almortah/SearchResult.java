package com.almortah.almortah;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class SearchResult extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Chalet> chalets = new ArrayList<>();
    int i = 0;
    private ChaletListRV mAdapter;
    private RecyclerView rv;
    private DrawerLayout drawer;
    private ProgressDialog pDialog;


    int minPrice = -1;
    int maxPrice = -1;
    String name = null;
    String location = null;
    double maxLt,maxLg,minLt,minLg = -1;
    private String date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


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



        Bundle info = getIntent().getExtras();
        if(info.containsKey("min") && info.containsKey("max")) {
            minPrice = info.getInt("min");
            maxPrice = info.getInt("max");
        }
        if (info.containsKey("name"))
        name = info.getString("name");
        if (info.containsKey("location"))
            location = info.getString("location");
        if(info.containsKey("maxLt")) {
            maxLg = info.getDouble("maxLg");
            maxLt = info.getDouble("maxLt");
            minLg = info.getDouble("minLg");
            minLt = info.getDouble("minLt");
        }

        if(info.containsKey("date"))
            date = info.getString("date");

        mAdapter = new ChaletListRV(getBaseContext() ,chalets);
        new GetChalets().execute();
        mAdapter.notifyDataSetChanged();



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


    private class GetChalets extends AsyncTask<Void, Void, Void> {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chalets");
        private boolean doneSearch = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchResult.this);
            pDialog.setTitle(R.string.downChalet);
            pDialog.setMessage(getString(R.string.wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            rv = (RecyclerView) findViewById(R.id.recycler_view);
            rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));

            //set join adapter to your RecyclerView
            rv.setAdapter(mAdapter);
            // You don't need anything here

            if(minLg != -1 && date != null && maxPrice != -1 && minPrice != -1) {
                Log.e("FIRST IF","FIRST IF");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                Chalet chalet = singlValue.getValue(Chalet.class);
                                double currentLt = Double.parseDouble(chalet.getLatitude());
                                double currentLg = Double.parseDouble(chalet.getLongitude());
                                if( (currentLt <= maxLt && currentLt >= minLt) && (currentLg <= maxLg && currentLg >= minLg)
                                        && (Integer.parseInt(chalet.getNormalPrice()) >= minPrice &&
                                        Integer.parseInt(chalet.getNormalPrice()) <= maxPrice)
                                        )
                                    chalets.add(chalet);
                            }
                        }

                        for(int i = 0; i < chalets.size(); i++) {
                            final Chalet chalet = chalets.get(i);
                            FirebaseDatabase.getInstance().getReference().child("busyDates")
                                    .child(chalet.getId()).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        chalets.remove(chalet);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }



                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                doneSearch = true;
            }



            if(maxPrice != -1 && minPrice != -1 && minLg == -1 && !doneSearch) {
                Log.e("2nd IF","2nd IF");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                        while ((iterator.hasNext())) {
                            Chalet chalet = iterator.next().getValue(Chalet.class);
                            if (Integer.parseInt(chalet.getNormalPrice()) >= minPrice &&
                                    Integer.parseInt(chalet.getNormalPrice()) <= maxPrice)
                                chalets.add(chalet);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            if(name != null) {
                Log.e("3rd IF","3rd IF");
                reference.orderByChild("name").equalTo(name).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                            Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                            while ((iterator.hasNext())) {
                                Chalet chalet = iterator.next().getValue(Chalet.class);
                                chalets.add(chalet);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }



            if(minLg != -1 && date != null && !doneSearch && maxPrice == -1 && minPrice == -1) {
                Log.e("4th IF","4th IF");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                Chalet chalet = singlValue.getValue(Chalet.class);
                                double currentLt = Double.parseDouble(chalet.getLatitude());
                                double currentLg = Double.parseDouble(chalet.getLongitude());
                                if( (currentLt <= maxLt && currentLt >= minLt) && (currentLg <= maxLg && currentLg >= minLg) ) {
                                    chalets.add(chalet);
                                    Log.e("ADD: ",chalet.getName());
                                }
                            }

                            for(int i = 0; i < chalets.size(); i++) {
                                final Chalet chalet = chalets.get(i);
                                Log.e("GET: ",chalet.getName());

                                FirebaseDatabase.getInstance().getReference().child("busyDates")
                                        .child(chalet.getId()).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            Log.e("Remove: ",chalet.getName());
                                            chalets.remove(chalet);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        else {
                                            Log.e("WrongChild: ",chalet.getName()+" + "+date);
                                        }
                                    }



                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }


                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            if(date != null && minLg == -1 && !doneSearch && maxPrice == -1 && minPrice == -1) {
                Log.e("5th IF","5th IF");
                Log.e("MAX?", String.valueOf(maxPrice));
                Log.e("MIN?", String.valueOf(minPrice));


                Log.e("DATE::",date);
                final ArrayList<String> busyChalet = new ArrayList<>();

                FirebaseDatabase.getInstance().getReference().child("busyDates").orderByChild(date)
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                               if( singlValue.child(date).exists() ) {
                                   busyChalet.add(singlValue.getKey());
                                   Log.e("BUSY CHALet?", singlValue.getKey());
                               }
                            }

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                                            Chalet chalet = singlValue.getValue(Chalet.class);
                                            Log.e("!!CHALET ID:",chalet.getId());
                                                if(!busyChalet.contains(chalet.getId()))
                                                    chalets.add(chalet);
                                        }
                                        mAdapter.notifyDataSetChanged();
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

            HashSet<Chalet> hashSet = new HashSet<Chalet>();
            hashSet.addAll(chalets);
            chalets.clear();
            chalets.addAll(hashSet);
            mAdapter.notifyDataSetChanged();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            mAdapter.notifyDataSetChanged();
            HashSet<Chalet> hashSet = new HashSet<Chalet>();
            hashSet.addAll(chalets);
            chalets.clear();
            chalets.addAll(hashSet);
            mAdapter.notifyDataSetChanged();
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }

}
