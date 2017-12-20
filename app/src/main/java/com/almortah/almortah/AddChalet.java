package com.almortah.almortah;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.util.PickConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AddChalet extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private FirebaseApp app;
    private FirebaseStorage storage;
    private GoogleMap mGoogleMap;
    private Marker marker;
    private LocationManager locationManager;
    private String provider;
    private DrawerLayout drawer;

    private Button mUploadImage;
    private StorageReference firebaseStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText mChaletName;
    private EditText mChaletPrice;
    private Button submitChalet;
    private Chalet chalet;
    private int chaletCount;
    private int imgName;
    private String latitude;
    private String longitude;
    private String chaletLocation;
    private Location location;
    private int imgNb = 0;

    static final int PICK_CONTACT_REQUEST = 1;
    static final int MY_PERMISSIONS_REQUEST = 1;
    private String descr = "Empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chalet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference = mDatabase.child("users").child(user.getUid()).child("nbChalets");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chaletCount = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                chaletCount++;
                mDatabase.child("users").child(user.getUid()).child("nbChalets").setValue(String.valueOf(chaletCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//                    Users user = dataSnapshot.getValue(Users.class);
//                    chaletCount = Integer.parseInt(String.valueOf(user.getNbChalets()));
//                    chaletCount++;
//                    mDatabase.child("users").child(user.getUserID()).child("nbChalets").setValue(String.valueOf(chaletCount));
//                    isApproved = user.getIsApproved();
//                }
//
//                if(isApproved != null && isApproved.equals("0")) {
//                    Toast.makeText(getApplicationContext(),R.string.cantAdd,Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(), MyChalets.class));
//                    return;
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            navigationView.inflateMenu(R.menu.owner_menu);
        else
            navigationView.inflateMenu(R.menu.visitor_menu);


        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permissions Denied" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

            }
        };


        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle("Allow Permissions")
                .setRationaleMessage("Allow This app to access contacts and location")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("Allow")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();


        app = FirebaseApp.getInstance();
        storage = FirebaseStorage.getInstance(app);
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
                return;

            }
        }

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mChaletName = (EditText) findViewById(R.id.chaletName);
        mChaletPrice = (EditText) findViewById(R.id.normalPrice);
        final EditText mWeekend = (EditText) findViewById(R.id.weekendPrice);
        final EditText mEid = (EditText) findViewById(R.id.eidPrice);
        final EditText des = (EditText) findViewById(R.id.description);

        submitChalet = (Button) findViewById(R.id.submitChalet);

        submitChalet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaletName = mChaletName.getText().toString().trim(); // 2--20
                String chaletPrice = mChaletPrice.getText().toString().trim(); // 50-9999
                String weekendPrice = mWeekend.getText().toString().trim();
                String eidPrice = mEid.getText().toString().trim();
                descr = des.getText().toString().trim();
                String chaletOwnerId = user.getUid().toString();
                String id = chalet.getId();

                if (latitude == null || longitude == null || chaletName.matches("") // in Riyadh
                        || chaletPrice.matches("") || weekendPrice.matches("")
                        || eidPrice.matches("") ) {
                    Toast.makeText(getBaseContext(),R.string.erEmptyField,Toast.LENGTH_SHORT).show();
                    return;
                }

                if(chaletName.length() < 2 || chaletName.length() > 20) {
                    mChaletName.setError(getString(R.string.error));
                    return;
                }
                if(Integer.parseInt(chaletPrice) < 50 || Integer.parseInt(chaletPrice) > 9999) {
                    mChaletPrice.setError(getString(R.string.error));
                    return;
                }
                if(Integer.parseInt(weekendPrice) < 50 || Integer.parseInt(weekendPrice) > 9999) {
                    mWeekend.setError(getString(R.string.error));
                    return;
                }

                if(Integer.parseInt(eidPrice) < 50 || Integer.parseInt(eidPrice) > 9999) {
                    mEid.setError(getString(R.string.error));
                    return;
                }

                if(Double.parseDouble(longitude) < 24 || Double.parseDouble(longitude) > 47) {
                    Toast.makeText(getApplicationContext(),R.string.errorMap,Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("ownerID", chaletOwnerId);
                hashMap.put("name", chaletName);
                hashMap.put("normalPrice", chaletPrice);
                hashMap.put("weekendPrice", weekendPrice);
                hashMap.put("eidPrice", eidPrice);
                hashMap.put("description", descr);
                hashMap.put("ImageUrl", firebaseStorage.child(user.getUid()).child(String.valueOf(chaletCount)).getPath());
                hashMap.put("chaletNm", String.valueOf(chaletCount));
                hashMap.put("promotion", "0"); // 0 no promoted, 1 promoted
                hashMap.put("latitude", latitude);
                hashMap.put("longitude", longitude);
                hashMap.put("rating", "0.00");
                hashMap.put("nbImages", String.valueOf(imgNb));
                hashMap.put("id", id);
                hashMap.put("ownerToken", SharedPrefManager.getmInstance(getApplicationContext()).getToken() );
                mDatabase.child("chalets").child(id).setValue(hashMap);

                HashMap<String, String> dateHashMap = new HashMap<String, String>();
                dateHashMap.put("busyOn", "");
                //mDatabase.child("busyDates").child(id).child("busyOn").setValue("");

                startActivity(new Intent(AddChalet.this, MyChalets.class));
            }
        });


        mUploadImage = (Button) findViewById(R.id.upload);

        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickPhotoView.Builder(AddChalet.this)
                        .setPickPhotoSize(5)   //select max size
                        .setShowCamera(true)   //is show camera
                        .setSpanCount(4)       //SpanCount
                        .setLightStatusBar(true)  // custom theme
                        .setToolbarColor("#52bb6c")// custom toolbar icon
                        .setSelectIconColor("#52bb6c")  // custom select icon
                        .start();


            }
        });


        //MAP

        if (googleServiceAvail() == true) {
            initMap();
            // onMapReady(mGoogleMap);
        }


    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        permmision();
       /* mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){


            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                latitude =(""+marker.getPosition().latitude);
                longitude = (""+marker.getPosition().longitude);


            }
        }); */

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }

                MarkerOptions options = new MarkerOptions().position(latLng);
                marker = mGoogleMap.addMarker(options);
                latitude = ("" + marker.getPosition().latitude);
                longitude = ("" + marker.getPosition().longitude);

            }
        });


        permmision();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        location = locationManager.getLastKnownLocation(provider);
        LatLng userPostion;

        if(location != null) {
            userPostion = new LatLng(location.getLatitude(), location.getLongitude());
            latitude = String.valueOf(location.getLatitude()); longitude = String.valueOf(location.getLongitude());

        }
        else
            userPostion = new LatLng(24.788521290609893,46.790736615657806);


        MarkerOptions options = new MarkerOptions().position(userPostion).draggable(true);
        marker = mGoogleMap.addMarker(options);
        chaletLocation = marker.getPosition().toString();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(userPostion));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    public void permmision() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }


    public boolean googleServiceAvail() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();

        } else {
            Toast.makeText(this, "Cant connect to service", Toast.LENGTH_LONG);
        }
        return false;

    }

    //MAP

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        if (data == null) {
            return;
        }

        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            ArrayList<String> selectPaths = (ArrayList<String>) data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);
            imgName = 1;
            imgNb = selectPaths.size();
            for (int i = 0; i < selectPaths.size(); i++) {
                final Uri[] uri = new Uri[selectPaths.size()];
                uri[i] = Uri.parse("file://" + selectPaths.get(i));
                final StorageReference ref = firebaseStorage.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(chaletCount)).child(String.valueOf(imgName));
                ref.putFile(uri[i])
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String content = downloadUrl.toString();
                                if (content.length() > 0) {
                                    Random rand = new Random();
                                    int n = rand.nextInt(50) + 1;
                                    //++chaletCount;
                                    firebaseStorage.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(chaletCount)).child(String.valueOf(imgName));
                                }
                            }
                        });
                imgName++;
                // do something u want
            }
            Toast.makeText(getApplicationContext(), R.string.doneUpload, Toast.LENGTH_SHORT).show();
        }
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