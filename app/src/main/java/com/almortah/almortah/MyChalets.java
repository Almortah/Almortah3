package com.almortah.almortah;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyChalets extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private AlmortahDB almortahDB;
    private String name;
    private String images;
    private String promotion;
    private String chaletNm;
    private String eidPrice;
    private String normalPrice;
    private String ownerID;
    private String weekendPrice;
    private ArrayList<Chalet> ownerChalets = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private String[] pageTitle;


    public ArrayList<Chalet> getOwnerChalets() {
        return ownerChalets;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chalets);
        almortahDB = new AlmortahDB(this);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
      //  ownerChalets = almortahDB.getMyChalets(mAuth.getCurrentUser().getUid());
        final String userID = user.getUid();
        final String userId = user.getProviderId();



        pageTitle = new String[]{getString(R.string.list), getString(R.string.map)};



        viewPager = (ViewPager)findViewById(R.id.view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //setting Tab layout (number of Tabs = number of ViewPager pages)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 2; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            navigationView.inflateMenu(R.menu.owner_menu);
        else
            navigationView.inflateMenu(R.menu.visitor_menu);

        //set viewpager adapter
        MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        refreshToken(SharedPrefManager.getmInstance(getApplicationContext()).getToken());



        // ArrayList<Chalet> ownerChalets = almortahDB.getMyChalets(user.getUid());
        //ownerChalets = almortahDB.getMyChalets(userID);
      /*  DatabaseReference chalet = almortahDB.getAlmortahDB().child("chalets");
        chalet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while ((iterator.hasNext())) {
                    Chalet chalet = iterator.next().getValue(Chalet.class);
                    if(chalet.getOwnerID().equals(userID) || chalet.getOwnerID().equals(userId)) {
                        ownerChalets.add(chalet);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/
       // if(!getOwnerChalets().isEmpty()) {
         //   ListView myChalest = (ListView) findViewById(R.id.myChalets);
           // MyChaletsAdapter myChaletsAdapter = new MyChaletsAdapter(this, getOwnerChalets());
            //yChalest.setAdapter(myChaletsAdapter);

/*
            final ListView listView = (ListView) findViewById(R.id.myChalets);
            final ArrayList<Chalet> myChalets = new ArrayList<>();
           DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chalets");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot singleChalet : dataSnapshot.getChildren()) {
                            Chalet chalet = singleChalet.getValue(Chalet.class);
                            if (chalet.getOwnerID().equals(userID)) {
                                myChalets.add(chalet);
                            }
                        }
                    }
                        MyChaletsAdapter myChaletsAdapter = new MyChaletsAdapter(MyChalets.this, myChalets);
                        listView.setAdapter(myChaletsAdapter);
                    }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });*/
        }

    public void refreshToken(final String token){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        user= FirebaseAuth.getInstance().getCurrentUser();

        mDatabase.child(user.getUid()).child("token").setValue(token);
        final DatabaseReference changeToken = FirebaseDatabase.getInstance().getReference().child("chalets");
        changeToken
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> updates = new HashMap<String,Object>();
                Chalet chalet;
                for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                    chalet = singlValue.getValue(Chalet.class);
                    if (chalet.getOwnerID().equals(user.getUid())) {
                        chalet.setOwnerToken(token);
                        changeToken.child(chalet.getId()).setValue(chalet);
                    }
                }
                //changeToken.updateChildren(updates);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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