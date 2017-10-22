package com.almortah.almortah;

import android.app.Dialog;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseApp app;
    private FirebaseStorage storage;
    private GoogleMap mGoogleMap;
    private Marker marker;
    private LocationManager locationManager;
    private String provider;
    private DatabaseReference mDatabase;
    private ArrayList<Chalet> chalets;
    private ArrayList<String> img;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        chalets = new ArrayList<>();

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
                .setRationaleMessage("Allow This app to access location")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("Allow")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("chalets");


        if (googleServiceAvail() == true) {
            initMap();

        }


    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String latitudeString;
                String longitudeString;
                for (DataSnapshot snapm: dataSnapshot.getChildren()) {
                    Chalet chalet = snapm.getValue(Chalet.class);
                    latitudeString = snapm.child("latitude").getValue(String.class);
                 //   Log.i("Latitude",latitudeString);
                    double latitude = Double.parseDouble(latitudeString);
                    longitudeString = snapm.child("longitude").getValue(String.class);
                   // Log.i("Latitude",longitudeString);
                    double longitude=Double.parseDouble(longitudeString);
                    LatLng newLocation = new LatLng(latitude,longitude);
                    chalets.add(chalet);
                   // img.add(snapm.child("ImageUrl").toString());
                    //mGoogleMap.addMarker(new MarkerOptions().position(newLocation));

                }
                for (int i=0;i<chalets.size();i++){
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(chalets.get(i).getLatitude()),Double.parseDouble(chalets.get(i).getLongitude()))))

                            .setTitle(chalets.get(i).getName().toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
  /*  private Bitmap getMarkerBitmapFromView() {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_tag, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.chaletImg);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }*/

}
