package com.almortah.almortah;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class Search extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {

    private DrawerLayout drawer;
    private String[] address;
    private Spinner spinner;
    private int minPrice = -1;
    private int maxPrice = -1;
    String location;
    private DatePicker datePicker;
    private String date;

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



            String[] list = getResources().getStringArray(R.array.Riyadh);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Search.this,
                    android.R.layout.simple_spinner_item, list);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

        Button addMin = (Button) findViewById(R.id.addMin);
        Button desMin = (Button) findViewById(R.id.desMin);
        Button addMax = (Button) findViewById(R.id.addMax);
        Button desMax = (Button) findViewById(R.id.desMax);

        Button addAddMin = (Button) findViewById(R.id.addAddMin);
        Button desDesMin = (Button) findViewById(R.id.desDesMin);
        Button addAddMax = (Button) findViewById(R.id.addAddMax);
        Button desDesMax = (Button) findViewById(R.id.desDesMax);


            final EditText max = (EditText) findViewById(R.id.max);
            final EditText min = (EditText) findViewById(R.id.min);

            datePicker = (DatePicker) findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, +6);
        long result = c.getTimeInMillis();

            datePicker.setMinDate(System.currentTimeMillis() - 1000);
            datePicker.setMaxDate(result);


            desMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(min.getText().toString().matches("")) {
                        min.setText("0");
                        minPrice = 0;
                    }
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
                    if(max.getText().toString().matches("")) {
                        max.setText("0");
                        maxPrice = 0;
                    }
                    else {
                        maxPrice = Integer.parseInt(max.getText().toString().trim()) - 50;
                        if(maxPrice < 0)
                            maxPrice = 0;
                        else if(maxPrice < minPrice && minPrice != -1)
                            maxPrice = minPrice;
                        max.setText(String.valueOf(maxPrice));
                    }
                }
            });

            addMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(maxPrice == -1) {
                        Toast.makeText(getApplicationContext(),R.string.addMax,Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else if(minPrice > maxPrice)
                        return;

                    else if(min.getText().toString().matches("")) {
                        min.setText("0");
                        minPrice = 0;
                    }

                    else {
                        minPrice = Integer.parseInt(min.getText().toString().trim()) + 50;
                        if(minPrice > 5000)
                            minPrice = 5000;
                        else if (minPrice > maxPrice && maxPrice != -1)
                            minPrice = maxPrice;

                        min.setText(String.valueOf(minPrice));
                    }
                }
            });


            // **************************///


            addAddMax.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (max.getText().toString().matches("")) {
                        max.setText("0");
                        maxPrice = 0;
                }

                    else {
                        maxPrice = Integer.parseInt(max.getText().toString().trim()) + 200;
                        if(maxPrice > 9000)
                            maxPrice = 9000;
                        else if(maxPrice < minPrice)
                            maxPrice = minPrice;

                        max.setText(String.valueOf(maxPrice));
                    }
                }
            });



        desDesMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(min.getText().toString().matches("")) {
                    min.setText("0");
                    minPrice = 0;
                }
                else {
                    minPrice = Integer.parseInt(min.getText().toString().trim()) - 200;
                    if(minPrice < 0)
                        minPrice = 0;
                    else if(minPrice > maxPrice)
                        minPrice = maxPrice;

                    min.setText(String.valueOf(minPrice));
                }
            }
        });


        desDesMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(max.getText().toString().matches("")) {
                    max.setText("0");
                    maxPrice = 0;
                }
                else {
                    maxPrice = Integer.parseInt(max.getText().toString().trim()) - 200;
                    if(maxPrice < 0)
                        maxPrice = 0;
                    else if(maxPrice < minPrice && minPrice != -1)
                        maxPrice = minPrice;
                    max.setText(String.valueOf(maxPrice));
                }
            }
        });

        addAddMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maxPrice == -1) {
                    Toast.makeText(getApplicationContext(),R.string.addMax,Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(minPrice > maxPrice)
                    return;

                else if(min.getText().toString().matches("")) {
                    min.setText("0");
                    minPrice = 0;
                }

                else {
                    minPrice = Integer.parseInt(min.getText().toString().trim()) + 200;
                    if(minPrice > 5000)
                        minPrice = 5000;
                    else if (minPrice > maxPrice && maxPrice != -1)
                        minPrice = maxPrice;

                    min.setText(String.valueOf(minPrice));
                }
            }
        });

        addAddMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (max.getText().toString().matches("")) {
                    max.setText("0");
                    maxPrice = 0;
                }

                else {
                    maxPrice = Integer.parseInt(max.getText().toString().trim()) + 200;
                    if(maxPrice > 9000)
                        maxPrice = 9000;
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
                        if(minPrice == -1 && maxPrice != -1) {
                                minPrice = 0;
                                Log.e("BadMInPrice","-------");
                        }
                        toResult.putExtra("min",minPrice);
                        toResult.putExtra("max",maxPrice);
                     //   toResult.putExtra("location", location);
                        double maxLt = -1; double maxLg = -1;
                        double minLt = -1; double minLg = -1;
                        date = datePicker.getDayOfMonth() +"-"+ ( datePicker.getMonth()+1 )+"-"+datePicker.getYear();

                        if(spinner.getSelectedItemPosition() == 1) { // North
//                             maxLt = 24.79;  maxLg = 46.72;
//                             minLt = 24.75;  minLg = 46.60;
                            maxLt = 24.947828;  maxLg = 46.720927;
                            minLt = 24.743411;  minLg = 46.522921;

                        }

                        else if(spinner.getSelectedItemPosition() == 4) { // WEST
//                            maxLt = 24.75;  maxLg = 46.60;
//                            minLt = 24.60;  minLg = 46.50;
                            maxLt = 24.753846;  maxLg = 46.641426;
                            minLt = 24.604104;  minLg = 46.41655;
                        }

                        else if(spinner.getSelectedItemPosition() == 5) { // south
//                            maxLt = 24.60;  maxLg = 46.70;
//                            minLt = 24.50;  minLg = 46.50;
                            maxLt = 24.606289;  maxLg = 46.72039;
                            minLt = 24.494491;  minLg = 46.487274;

                        }

                        else if(spinner.getSelectedItemPosition() == 2) { // EAST
//                            maxLt = 24.80;  maxLg = 46.80;
//                            minLt = 24.63;  minLg = 46.72;
                            maxLt = 24.898894;  maxLg = 46.977539;
                            minLt = 24.570855;  minLg = 46.752319;
                        }

                        else if(spinner.getSelectedItemPosition() == 3) { // CENTER
                            maxLt = 24.7223;  maxLg = 46.765737;
                            minLt = 24.617425;  minLg = 46.660813;
                        }

                        toResult.putExtra("maxLt",maxLt);
                        toResult.putExtra("minLt",minLt);
                        toResult.putExtra("maxLg",maxLg);
                        toResult.putExtra("minLg",minLg);
                        toResult.putExtra("date",date);
                        startActivity(toResult);

                }
            });




            Button search = (Button) findViewById(R.id.search);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Search.this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.phone_dialog, null);
                    dialogBuilder.setView(dialogView);

                    final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                    dialogBuilder.setTitle(R.string.findSpecifcChalet);
                    dialogBuilder.setPositiveButton(getText(R.string.search), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //do something with edt.getText().toString();
                            String chaletName = edt.getText().toString().trim();
                            if(chaletName.matches(""))
                                return;
                            Intent toResult = new Intent(getBaseContext(), SearchResult.class);
                            toResult.putExtra("name",chaletName);
                            startActivity(toResult);
                            return;
                        }
                    });
                    dialogBuilder.setNegativeButton(getString(R.string.cancel1), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //pass
                            dialog.cancel();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
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
                double maxLt = 24.79; double maxLg = 46.72;
                double minLt = 24.75; double minLg = 46.60;
                intent.putExtra("maxLt",maxLt);
                intent.putExtra("minLt",minLt);
                intent.putExtra("maxLg",maxLg);
                intent.putExtra("minLg",minLg);
                //startActivity(intent);
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                intent.putExtra("location", parent.getItemAtPosition(position).toString());
                Log.e("LOCATION",parent.getItemAtPosition(position).toString());
                //startActivity(intent);
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
