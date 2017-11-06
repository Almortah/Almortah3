package com.almortah.almortah;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ChaletInfoCustomer extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener , NavigationView.OnNavigationItemSelectedListener{
    private FirebaseAuth mAuth ;
    private FirebaseDatabase mDatabase ;
    private StorageReference storageReference ;
    private DrawerLayout drawer;
    private TabLayout tabLayout;

    private String images;
    private String eidPrice;
    private String name;
    private String normalPrice;
    private String ownerID;
    private String promotion;
    private String chaletNb;
    private String weekendPrice;
    private String location;
    private String ownerName;

    private TextView nameView;
    private TextView normalPriceView;
    private TextView weekendPriceView;
    private TextView eidPriceView;
    private TextView locationView;
    private TextView owner;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private SliderLayout mDemoSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalet_info_customer);
        Bundle chaletInfo = getIntent().getExtras();
        name = chaletInfo.getString("name");
        normalPrice = chaletInfo.getString("normalPrice");
        weekendPrice = chaletInfo.getString("weekendPrice");
        eidPrice = chaletInfo.getString("eidPrice");
        ownerID = chaletInfo.getString("ownerID");
        location = chaletInfo.getString("location");
        chaletNb = chaletInfo.getString("chaletNb");

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

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            navigationView.inflateMenu(R.menu.customer_menu);
        else
            navigationView.inflateMenu(R.menu.visitor_menu);



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(ownerID).child(chaletNb);
        DatabaseReference getOwnerName = FirebaseDatabase.getInstance().getReference().child("users").child(ownerID).child("Name");
        getOwnerName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ownerName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        //nameView = (TextView) findViewById(R.id.chaletName);
        normalPriceView = (TextView) findViewById(R.id.normalPrice);
        weekendPriceView = (TextView) findViewById(R.id.weekendPrice);
        eidPriceView = (TextView) findViewById(R.id.eidPrice);
        locationView = (TextView) findViewById(R.id.location);
       // owner = (TextView) findViewById(R.id.ownerName);

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        final ArrayList<String> listUrl = new ArrayList<String>();




        //nameView.setText(name);
        normalPriceView.setText(normalPrice);
        weekendPriceView.setText(weekendPrice);
        eidPriceView.setText(eidPrice);
        locationView.setText(location);
//        owner.setText(ownerName);

        for (int i = 1; i < 6; i++) {
            if (i == 1) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        TextSliderView textSliderView = new TextSliderView(ChaletInfoCustomer.this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(uri.toString())
                                .setBitmapTransformation(new CenterCrop())
                                .setOnSliderClickListener(ChaletInfoCustomer.this);
                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("extra","!!!!!!");
                        mDemoSlider.addSlider(textSliderView);                        /* Glide.with(ChaletInfoCustomer.this)
                                .load(uri)
                                .into(img1);
                        img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
                                intent.putExtra("img", uri );
                                startActivity(intent);
                            }
                        });*/
                    }
                });
            } else if (i == 2) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        TextSliderView textSliderView = new TextSliderView(ChaletInfoCustomer.this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(uri.toString())
                                .setBitmapTransformation(new CenterCrop())
                                .setOnSliderClickListener(ChaletInfoCustomer.this);
                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("extra","!!!!!!");
                        mDemoSlider.addSlider(textSliderView);
                        /*Glide.with(ChaletInfoCustomer.this)
                                .load(uri)
                                .into(img2);
                        img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
                                intent.putExtra("img", uri );
                                startActivity(intent);
                            }
                        });*/
                    }
                });
            }  else if (i == 3) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {

                        TextSliderView textSliderView = new TextSliderView(ChaletInfoCustomer.this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(uri.toString())
                                .setBitmapTransformation(new CenterCrop())
                                .setOnSliderClickListener(ChaletInfoCustomer.this);
                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("extra","!!!!!!");
                        mDemoSlider.addSlider(textSliderView);
                       /* Glide.with(ChaletInfoCustomer.this)
                                .load(uri)
                                .into(img3);
                        img3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
                                intent.putExtra("img", uri );
                                startActivity(intent);
                            }
                        });*/
                    }
                });
            }
            else if (i == 4) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {

                        TextSliderView textSliderView = new TextSliderView(ChaletInfoCustomer.this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(uri.toString())
                                .setBitmapTransformation(new CenterCrop())
                                .setOnSliderClickListener(ChaletInfoCustomer.this);
                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("extra","!!!!!!");
                        mDemoSlider.addSlider(textSliderView);                       /* Glide.with(ChaletInfoCustomer.this)
                                .load(uri)
                                .into(img4);
                        img4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
                                intent.putExtra("img", uri );
                                startActivity(intent);
                            }
                        });*/
                    }
                });
            }
            else {

                //StorageReference tmp = storageReference.child(String.valueOf(i));
                //tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               //     @Override
                 //   public void onSuccess(final Uri uri) {
                                /*Glide.with(ChaletInfoCustomer.this)
                                .load(uri)
                                .into(img5);
                        img5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
                                intent.putExtra("img", uri );
                                startActivity(intent);
                            }
                        });*/
              //      }
            //    });
            }
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(ChaletInfoCustomer.this);

        Button book = (Button) findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() == null) {
                    Toast.makeText(getApplicationContext(),R.string.bookVistor,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(),login.class));
                    return;
                }
                Intent toBooking = new Intent(ChaletInfoCustomer.this, BookingAChalet.class);
                toBooking.putExtra("ownerID",ownerID);
                toBooking.putExtra("chaletNb",chaletNb);
                toBooking.putExtra("normalPrice",normalPrice);
                toBooking.putExtra("weekendPrice",weekendPrice);
                toBooking.putExtra("name",name);
                startActivity(toBooking);


            }
        });
        /*
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    img1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    img1.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    img1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    img1.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });*/
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.visitor_menu, menu);
        } else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.customer_menu, menu);
        }


        return true;
    }

    /**
     * Event Handling for Individual visitor_menu item selected
     * Identify single visitor_menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchChaleh:
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,HomeActivity.class));
                return true;
            case R.id.login:
                startActivity(new Intent(this,login.class));
                return true;
            case R.id.register:
                startActivity(new Intent(this,Signup.class));
                return true;
            case R.id.history:
                startActivity(new Intent(this,MyReservation.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e("MyActivity", "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
        intent.putExtra("img", slider.getUrl() );
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
        switch(item.getItemId()) {
            case R.id.searchChaleh:
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,HomeActivity.class));
                break;
            case R.id.login:
                startActivity(new Intent(this,login.class));
                break;
            case R.id.register:
                startActivity(new Intent(this,Signup.class));
                break;
            case R.id.history:
                startActivity(new Intent(this,MyReservation.class));
                break;
            default:
                super.onOptionsItemSelected(item);
        }

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

