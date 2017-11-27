package com.almortah.almortah;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class Search extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {

    private DrawerLayout drawer;
    private String[] address;
    private Spinner spinner;
    private int minPrice = 0;
    private int maxPrice = 0;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        spinner = (Spinner) findViewById(R.id.spinner1);

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



            String[] list = getResources().getStringArray(R.array.RiyadhWest);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Search.this,
                    android.R.layout.simple_spinner_item, list);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

            Button addMin = (Button) findViewById(R.id.addMin);
            Button desMin = (Button) findViewById(R.id.desMin);
            Button addMax = (Button) findViewById(R.id.addMax);
            Button desMax = (Button) findViewById(R.id.desMax);

            final EditText max = (EditText) findViewById(R.id.max);
            final EditText min = (EditText) findViewById(R.id.min);


            desMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(min.getText().toString().matches(""))
                        min.setText("0");
                    else {
                        minPrice = Integer.parseInt(min.getText().toString().trim()) - 50;
                        if(minPrice < 0)
                            minPrice = 0;
                        else if(minPrice > maxPrice)
                            minPrice = maxPrice;

                        min.setText(String.valueOf(minPrice));
                    }
                }
            });


            desMax.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(max.getText().toString().matches(""))
                        max.setText("0");
                    else {
                        maxPrice = Integer.parseInt(max.getText().toString().trim()) - 50;
                        if(maxPrice < 0)
                            maxPrice = 0;
                        else if(maxPrice < minPrice)
                            maxPrice = minPrice;
                        max.setText(String.valueOf(maxPrice));
                    }
                }
            });

            addMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(min.getText().toString().matches(""))
                        min.setText("0");
                    else {
                        minPrice = Integer.parseInt(min.getText().toString().trim()) + 50;
                        if(minPrice > 1000)
                            minPrice = 1000;
                        else if (minPrice > maxPrice)
                            minPrice = maxPrice;

                        min.setText(String.valueOf(minPrice));
                    }
                }
            });

            addMax.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(max.getText().toString().matches(""))
                        max.setText("0");
                    else {
                        maxPrice = Integer.parseInt(max.getText().toString().trim()) + 50;
                        if(maxPrice > 3000)
                            maxPrice = 3000;
                        else if(maxPrice < minPrice)
                            maxPrice = minPrice;
                        max.setText(String.valueOf(maxPrice));
                    }
                }
            });

            Button filter = (Button) findViewById(R.id.filter);
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // String minPrice = min.getText().toString().trim();
                    //String maxPrice = max.getText().toString().trim();
                        Intent toResult = new Intent(getBaseContext() ,SearchResult.class);
                        toResult.putExtra("min",minPrice);
                        toResult.putExtra("max",maxPrice);
                        toResult.putExtra("location", location);
                        startActivity(toResult);

                }
            });




            Button search = (Button) findViewById(R.id.search);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = (EditText) findViewById(R.id.chaletName);
                    String chaletName = editText.getText().toString().trim();
                    if(chaletName.matches(""))
                        return;
                    Intent toResult = new Intent(getBaseContext(), SearchResult.class);
                    toResult.putExtra("name",chaletName);
                    startActivity(toResult);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(getBaseContext(),SearchResult.class);
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                intent.putExtra("location", parent.getItemAtPosition(position).toString());
                Log.e("LOCATION",parent.getItemAtPosition(position).toString());
                startActivity(intent);
                break;
            case 2:
                // Whatever you want to happen when the third item gets selected
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
