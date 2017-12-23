package com.almortah.almortah;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChaletInfoCustomer extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference storageReference;
    private DrawerLayout drawer;
    private String location;
    private TextView normalPriceView;
    private TextView weekendPriceView;
    private TextView eidPriceView;
    private TextView locationView;
    private TextView description;
    private SliderLayout mDemoSlider;
    private Chalet chalet;
    private RecyclerView recyclerView;
    private boolean isComplain = false;
    private Button book;
    private Button complain;
    private String date = null;
    private boolean isWeekend = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalet_info_customer);
        Bundle chaletInfo = getIntent().getExtras();
        location = chaletInfo.getString("location");
        chalet = (Chalet) chaletInfo.getParcelable("chalet");
        book = (Button) findViewById(R.id.book);
        if (chaletInfo.containsKey("date")) {
            date = chaletInfo.getString("date");
            String[] tmp = date.split("-");
            book.setText(book.getText().toString() + " " + getString(R.string.on) + " " + tmp[0] + "/" + tmp[1]);
        }

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
        complain = (Button) findViewById(R.id.complain);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("abod@admin.com")) {
                navigationView.inflateMenu(R.menu.admin_menu);
                book.setText(R.string.delete);
                book.setBackgroundColor(R.color.colorDarkGrey);
                complain.setVisibility(View.GONE);
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChaletInfoCustomer.this);
                        alertDialogBuilder.setMessage(getString(R.string.sure));
                        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        FirebaseDatabase.getInstance().getReference().child("chalets").child(chalet.getId()).removeValue();
                                        Toast.makeText(getApplicationContext(), R.string.deleteChalet, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ChaletInfoCustomer.this, HomePage.class));
                                    }
                                });

                        alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        ///////////////////////////////////////


                    }
                });

            } else {
                navigationView.inflateMenu(R.menu.customer_menu);
//                if(date != null) {
//                    String[] tmp = date.split("-");
//                    book.setText(book.getText().toString() +" "+getString(R.string.on) + " " + tmp[0] +"/"+tmp[1]);
//
//                }

                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (date != null) {
                            Intent toConfirm = new Intent(getBaseContext(), ConfirmBooking.class);
                            toConfirm.putExtra("name", date);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                            String[] tmp = date.split("-");
                            calendar.set(Integer.parseInt(tmp[2]), (Integer.parseInt(tmp[1]) - 1), Integer.parseInt(tmp[0]));
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            Log.e("DayOfWeek", String.valueOf(dayOfWeek));
                            if (dayOfWeek > 4) {
                                isWeekend = true;
                            } else {
                                isWeekend = false;
                            }

                            toConfirm.putExtra("price", isWeekend);
                            Log.e("Weekend?", String.valueOf(isWeekend));
                            //    Log.e("finalDates?",finalDates);
                            toConfirm.putExtra("chalet", chalet);
                            startActivity(toConfirm);
                            return;
                        }

                        Intent toBooking = new Intent(ChaletInfoCustomer.this, BookingAChalet.class);
                        toBooking.putExtra("chalet", chalet);

                        startActivity(toBooking);


                    }
                });

                complain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isComplain)
                            return;

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChaletInfoCustomer.this);
                        alertDialogBuilder.setMessage(getString(R.string.sure));
                        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        final HashMap<String, String> map = new HashMap<>();

                                        AlertDialog dialog;
                                        final CharSequence[] items = getResources().getStringArray(R.array.complaints);
                                        //{" Easy "," Medium "," Hard "," Very Hard "};
                                        // arraylist to keep the selected items
                                        final ArrayList seletedItems = new ArrayList();

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ChaletInfoCustomer.this);
                                        builder.setTitle(getString(R.string.complainReson));
                                        builder.setMultiChoiceItems(items, null,
                                                new DialogInterface.OnMultiChoiceClickListener() {
                                                    // indexSelected contains the index of item (of which checkbox checked)
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int indexSelected,
                                                                        boolean isChecked) {
                                                        if (isChecked) {
                                                            // If the user checked the item, add it to the selected items
                                                            // write your code when user checked the checkbox
                                                            seletedItems.add(indexSelected);
                                                        } else if (seletedItems.contains(indexSelected)) {
                                                            // Else, if the item is already in the array, remove it
                                                            // write your code when user Uchecked the checkbox
                                                            seletedItems.remove(Integer.valueOf(indexSelected));
                                                        }
                                                    }
                                                })
                                                // Set the action buttons
                                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        String reasons = "";
                                                        for (int i = 0; i < seletedItems.size(); i++) {
                                                            int tmp = (int) seletedItems.get(i);
                                                            reasons += tmp + "-";
                                                        }
                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                                        final int complainTime = (int) System.currentTimeMillis();
                                                        final String complainID = String.valueOf(complainTime);
                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("reasons", reasons);
                                                        hashMap.put("customerID", mAuth.getCurrentUser().getUid());
                                                        hashMap.put("chaletID", chalet.getId());
                                                        hashMap.put("isDismiss", "0");
                                                        hashMap.put("chaletName", chalet.getName());
                                                        hashMap.put("complainID", complainID);
                                                        reference.child("complaints").child(complainID).setValue(hashMap);
                                                        isComplain = true;
                                                        Toast.makeText(getApplicationContext(), R.string.doneComplain, Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .setNegativeButton(getString(R.string.cancel1), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //  Your code when user clicked on Cancel
                                                        builder.create().cancel();
                                                    }
                                                });

                                        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
                                        dialog.show();
                                    }
                                });

                        alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });

            }
        } else {
            navigationView.inflateMenu(R.menu.visitor_menu);
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), R.string.bookVistor, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), login.class));
                    return;
                }
            });

            complain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), R.string.needLogin, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), login.class));
                    return;
                }
            });
        }


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm());

        normalPriceView = (TextView) findViewById(R.id.normalPrice);
        weekendPriceView = (TextView) findViewById(R.id.weekendPrice);
        eidPriceView = (TextView) findViewById(R.id.eidPrice);
        locationView = (TextView) findViewById(R.id.location);
        description = (TextView) findViewById(R.id.description);

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        normalPriceView.setText(chalet.getNormalPrice() + " " + getString(R.string.riyal));
        weekendPriceView.setText(chalet.getWeekendPrice() + " " + getString(R.string.riyal));
        eidPriceView.setText(chalet.getEidPrice() + " " + getString(R.string.riyal));

        try {
            Geocoder geo = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(Double.parseDouble(chalet.getLatitude()), Double.parseDouble(chalet.getLongitude()), 1);
            if (addresses.isEmpty()) {
                ;
            } else {
                if (addresses.size() > 0) {
                    location = addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality();
                    locationView.setText(location);
                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }


        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + chalet.getLatitude() + "," + chalet.getLongitude() + " (" + chalet.getName() + ")"));
                startActivity(intent);
            }
        });

        ImageView locationLogo = (ImageView) findViewById(R.id.chaletLocation);
        locationLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + chalet.getLatitude() + "," + chalet.getLongitude() + " (" + chalet.getName() + ")"));
                startActivity(intent);
            }
        });

        description.setText(chalet.getDescription());
        if (Integer.parseInt(chalet.getNbImages()) > 0) {
            for (int i = 1; i <= Integer.parseInt(chalet.getNbImages()); i++) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        TextSliderView textSliderView = new TextSliderView(ChaletInfoCustomer.this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(chalet.getName())
                                .image(uri.toString())
                                .setBitmapTransformation(new CenterCrop())
                                .setOnSliderClickListener(ChaletInfoCustomer.this);
                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("extra", "!!!!!!");
                        mDemoSlider.addSlider(textSliderView);
                    }
                });
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(ChaletInfoCustomer.this);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            final ArrayList<Rating> ratings = new ArrayList<>();
            final CommentsAdapter commentsAdapter = new CommentsAdapter(ChaletInfoCustomer.this, ratings);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chaletRatings");
            recyclerView.setAdapter(commentsAdapter);
            reference.orderByChild("chaletID").equalTo(chalet.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot singlValue : dataSnapshot.getChildren()) {
                            Rating rating = singlValue.getValue(Rating.class);
                            if (!rating.getComment().equals("-"))
                                ratings.add(rating);
                        }
                        Collections.reverse(ratings);
                        commentsAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    /**
     * Event Handling for Individual visitor_menu item selected
     * Identify single visitor_menu item by it's id
     */

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
        intent.putExtra("img", slider.getUrl());
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        AlmortahDB almortahDB = new AlmortahDB(this);

        if(FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("abod@admin.com"))
            almortahDB.adminMenu(item);

        else
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

