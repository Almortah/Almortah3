package com.almortah.almortah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class ChaletListActivity extends AppCompatActivity {

    private AlmortahDB almortahDB = new AlmortahDB(this);
    private ArrayList<Chalet> chalets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalet_list);
        Bundle user = getIntent().getExtras();
        chalets = new ArrayList<>();
        DatabaseReference reference = almortahDB.getAlmortahDB().child("chalets");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while ((iterator.hasNext())) {
                    Chalet chalet = iterator.next().getValue(Chalet.class);
                    chalets.add(chalet);

                }
                ChaletAdapter adapter = new ChaletAdapter(ChaletListActivity.this,chalets);
                ListView listView= (ListView) findViewById(R.id.chalet_list);
                listView.setAdapter(adapter);
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
        menuInflater.inflate(R.menu.visitor_menu, menu);
        return true;
    }

    /**
     * Event Handling for Individual visitor_menu item selected
     * Identify single visitor_menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        return false;
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
