package com.almortah.almortah;

import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.util.PickConfig;

import java.util.ArrayList;
import java.util.Random;

public class ManageChalet extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_manage_chalet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        chalet = (Chalet) getIntent().getExtras().getParcelable("chalet");

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
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
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
        mChaletName.setText(chalet.getName());
        mChaletPrice.setText(chalet.getNormalPrice());
        mWeekend.setText(chalet.getWeekendPrice());
        mEid.setText(chalet.getEidPrice());
        des.setText(chalet.getDescription());


        submitChalet = (Button) findViewById(R.id.submitChalet);



        mUploadImage = (Button) findViewById(R.id.upload);

        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickPhotoView.Builder(ManageChalet.this)
                        .setPickPhotoSize(5)   //select max size
                        .setShowCamera(true)   //is show camera
                        .setSpanCount(4)       //SpanCount
                        .setLightStatusBar(true)  // custom theme
                        .setToolbarColor("#52bb6c")// custom toolbar icon
                        .setSelectIconColor("#52bb6c")  // custom select icon
                        .start();
            }
        });


    }

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
            imgNb = Integer.parseInt(chalet.getNbOfImages());
            imgName = imgNb + 1;
            int newNbImgs = selectPaths.size() + imgNb;
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