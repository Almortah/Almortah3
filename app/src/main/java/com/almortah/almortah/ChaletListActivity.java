package com.almortah.almortah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ChaletListActivity extends AppCompatActivity {

    private AlmortahDB almortahDB = new AlmortahDB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalet_list);
        Bundle user = getIntent().getExtras();
        ArrayList<Chalet> chalets= new ArrayList<>();
        chalets.add(new Chalet("1","Golden","Type","Alflah",7));
        chalets.add(new Chalet("1","Silver","Type","alwadi",10));
        chalets.add(new Chalet("1","bronz","Type","albdy3h",5));
        chalets.add(new Chalet("1","alnseem chalet","Type","alnseem",2));
        chalets.add(new Chalet("1","rose chalet","Type","rose district",10));
        ChaletAdapter adapter = new ChaletAdapter(this,chalets);
        ListView listView= (ListView) findViewById(R.id.chalet_list);
        listView.setAdapter(adapter);



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
