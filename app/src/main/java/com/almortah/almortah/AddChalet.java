package com.almortah.almortah;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AddChalet extends AppCompatActivity implements OnMapReadyCallback {

    private StorageReference storageRef;
    private FirebaseApp app;
    private FirebaseStorage storage;
    private GoogleMap mGoogleMap;
    private Marker marker;
    private LocationManager locationManager;
    private String provider;
    private String address;

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
    double lat1 =0, lng1=0;
    private Geocoder geo;
    private List<Address> addresses;


    static final int PICK_CONTACT_REQUEST = 1;
    static final int MY_PERMISSIONS_REQUEST=1;
    private Geocoder geoAr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chalet);
        geo = new Geocoder(AddChalet.this, Locale.ENGLISH);
        geoAr = new Geocoder(AddChalet.this, Locale.getDefault());


        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(),"Permissions Granted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(),"Permissions Denied"+deniedPermissions.toString(),Toast.LENGTH_SHORT).show();

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

        if(permissionCheck!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                return;
            }
            else{
              ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST);
                return ;

            }
        }

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mChaletName = (EditText) findViewById(R.id.chaletName);
        mChaletPrice = (EditText) findViewById(R.id.normalPrice);
        submitChalet = (Button) findViewById(R.id.submitChalet);
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
        submitChalet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaletName = mChaletName.getText().toString().trim();
                String chaletPrice = mChaletPrice.getText().toString().trim();
                String chaletOwnerId = user.getUid().toString();
                if(latitude == null || longitude == null) {
                    return;
                }
                // chaletCount++;
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("ownerID", chaletOwnerId);
                hashMap.put("name", chaletName);
                hashMap.put("normalPrice", chaletPrice);
                hashMap.put("weekendPrice", chaletPrice);
                hashMap.put("eidPrice", chaletPrice);
                hashMap.put("ImageUrl", firebaseStorage.child(user.getUid()).child(String.valueOf(chaletCount)).getPath());
                hashMap.put("chaletNm", String.valueOf(chaletCount));
                hashMap.put("promotion", "0"); // 0 no promoted, 1 promoted
                hashMap.put("latitude",latitude);
                hashMap.put("longitude",longitude);
                hashMap.put("nbImages", String.valueOf(imgNb));
                mDatabase.child("chalets").push().setValue(hashMap);

                HashMap<String,String> dateHashMap = new HashMap<String, String>();
                dateHashMap.put("busyOn","");
                mDatabase.child("busyDates").child(chaletOwnerId).child(String.valueOf(chaletCount)).
                        child("busyOn").setValue("");

                startActivity(new Intent(AddChalet.this, MyChalets.class));
            }
        });


        mUploadImage = (Button) findViewById(R.id.upload);

        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickPhotoView.Builder(AddChalet.this)
                        .setPickPhotoSize(9)   //select max size
                        .setShowCamera(true)   //is show camera
                        .setSpanCount(4)       //SpanCount
                        .setLightStatusBar(true)  // custom theme
                        .setStatusBarColor("#ffffff")   // custom statusBar
                        .setToolbarColor("#ffffff")   // custom toolbar
                        .setToolbarIconColor("#000000")   // custom toolbar icon
                        .setSelectIconColor("#00C07F")  // custom select icon
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
                latitude =(""+marker.getPosition().latitude);
                longitude = (""+marker.getPosition().longitude);

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
        LatLng userPostion = new LatLng(24.593976, 46.670161);



        MarkerOptions options = new MarkerOptions().position(userPostion).draggable(true);
        marker = mGoogleMap.addMarker(options);
        chaletLocation=marker.getPosition().toString();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(userPostion));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    public void permmision(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }



    public boolean googleServiceAvail(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        } else if(api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this,isAvailable,0);
            dialog.show();

        }else{
            Toast.makeText(this,"Cant connect to service",Toast.LENGTH_LONG);
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
                storageRef = storage.getReference();
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
            Toast.makeText(getApplicationContext(),R.string.doneUpload,Toast.LENGTH_SHORT).show();
        }
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