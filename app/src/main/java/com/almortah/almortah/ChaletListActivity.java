package com.almortah.almortah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ChaletListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalet_list);

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
}
