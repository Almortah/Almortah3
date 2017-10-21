package com.almortah.almortah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MyChalets extends AppCompatActivity {

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

            });
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.owner_menu, menu);
        return true;
    }

    /**
     * Event Handling for Individual visitor_menu item selected
     * Identify single visitor_menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.newChalet:
                startActivity(new Intent(this, AddChalet.class));
                return true;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(this,HomeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e("MyActivity", "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

}