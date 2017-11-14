package com.almortah.almortah;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;

/**
 * Created by ALMAHRI on 10/24/17.
 */

public class ChaletcMapFragment extends Fragment implements OnMapReadyCallback {

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
    private Location location;
    private HashMap<String, Uri> images=new HashMap<String, Uri>();
    private Bitmap bitmap;

    public ChaletcMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.activity_map, container, false);

        chalets = new ArrayList<>();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
              //  Toast.makeText(view.getContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(view.getContext(), "Permissions Denied" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

            }
        };


        TedPermission.with(view.getContext())
                .setPermissionListener(permissionlistener)
                .setRationaleTitle("Allow Permissions")
                .setRationaleMessage("Allow This app to access location")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("Allow")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chalets");


        if (googleServiceAvail() == true) {
            initMap();

        }


        return view;
    }
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        location = locationManager.getLastKnownLocation(provider);
        LatLng userPostion = new LatLng(24,40);
        if(location != null)
            userPostion = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(userPostion));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(5));


        // mGoogleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(this,
        //       getLayoutInflater(),
        //     images));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String latitudeString;
                String longitudeString;
                for (DataSnapshot snapm: dataSnapshot.getChildren()) {
                    Chalet chalet = snapm.getValue(Chalet.class);
                    latitudeString = snapm.child("latitude").getValue(String.class);
                    Log.i("Latitude",latitudeString);
                    double latitude =Double.parseDouble(latitudeString);
                    longitudeString = snapm.child("longitude").getValue(String.class);
                    Log.i("Latitude",longitudeString);
                    double longitude=Double.parseDouble(longitudeString);
                    LatLng newLocation = new LatLng(latitude,longitude);
                    chalets.add(chalet);

                   //  mGoogleMap.addMarker(new MarkerOptions().position(newLocation));

                }

                for (int i=0;i<chalets.size();i++){
                    //addMarker(mGoogleMap);
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(chalets.get(i).getLatitude()),Double.parseDouble(chalets.get(i).getLongitude())))
                    .title(chalets.get(i).getName()))
                    ;
                    ;
                    Log.i("Name " ,chalets.get(i).getName());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        MapInfoWindowAdapter adapter = new MapInfoWindowAdapter(this.getContext(),getLayoutInflater());
        mGoogleMap.setInfoWindowAdapter(adapter);



    }


    public boolean googleServiceAvail(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(getContext());
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        } else if(api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(getActivity(),isAvailable,0);
            dialog.show();

        }else{
            Toast.makeText(getContext(),"Cant connect to service",Toast.LENGTH_LONG);
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
    } */

    private void addMarker(GoogleMap map, double lat, double lon,
                           int title, int snippet, String image) {
        Marker marker=
                map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                        .title(getString(title))
                );

        if (image != null) {
            images.put(marker.getId(),
                    Uri.parse("http://misc.commonsware.com/mapsv2/"
                            + image));
        }
    }


}
