package com.almortah.almortah;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.util.PickConfig;

import java.util.ArrayList;

public class ManageChalet extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private FirebaseApp app;
    private FirebaseStorage storage;
    private DrawerLayout drawer;

    private Button mUploadImage;
    private StorageReference firebaseStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText mChaletName;
    private EditText mChaletPrice;
    private Button submitChalet;
    private Chalet chalet;
    private int imgName;
    private int imgNb = 0;

    static final int PICK_CONTACT_REQUEST = 1;
    static final int MY_PERMISSIONS_REQUEST = 1;
    private String descr = "Empty";
    private SliderLayout mDemoSlider;

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
        Bundle chaletInfo = getIntent().getExtras();
        chalet = (Chalet) chaletInfo.getParcelable("chalet");
        Log.e("chalet",chalet.getName());
        Log.e("nbImages?", chalet.getNbImages());

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

        app = FirebaseApp.getInstance();
        storage = FirebaseStorage.getInstance(app);
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

        submitChalet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chaletName = mChaletName.getText().toString().trim(); // 2--20
                String chaletPrice = mChaletPrice.getText().toString().trim(); // 50-9999
                String weekendPrice = mWeekend.getText().toString().trim();
                String eidPrice = mEid.getText().toString().trim();
                descr = des.getText().toString().trim();

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

                if (!chaletName.equals(chalet.getName()))
                    mDatabase.child("chalets").child(chalet.getId()).child("name").setValue(chaletName);

                if (!eidPrice.equals(chalet.getEidPrice()))
                    mDatabase.child("chalets").child(chalet.getId()).child("eidPrice").setValue(eidPrice);

                if (!weekendPrice.equals(chalet.getWeekendPrice()))
                    mDatabase.child("chalets").child(chalet.getId()).child("weekendPrice").setValue(weekendPrice);

                if (!chaletPrice.equals(chalet.getNormalPrice()))
                    mDatabase.child("chalets").child(chalet.getId()).child("normalPrice").setValue(chaletPrice);

                if(!descr.equals(chalet.getDescription()))
                    mDatabase.child("chalets").child(chalet.getId()).child("description").setValue(descr);

                Toast.makeText(getApplicationContext(),R.string.updateChalet,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(),MyChalets.class));

            }
        });


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

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        if (Integer.parseInt(chalet.getNbImages()) > 0) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm());
            for (int i = 1; i <= Integer.parseInt(chalet.getNbImages()); i++) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        TextSliderView textSliderView = new TextSliderView(ManageChalet.this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(getString(R.string.clickToDelete))
                                .image(uri.toString())
                                .setBitmapTransformation(new CenterCrop())
                                .setOnSliderClickListener(ManageChalet.this);
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
            mDemoSlider.addOnPageChangeListener(ManageChalet.this);
        }
        else mDemoSlider.setVisibility(View.GONE);

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
            imgNb = Integer.parseInt(chalet.getNbImages()) + selectPaths.size();
            imgName = Integer.parseInt(chalet.getNbImages()) + 1;
            int newNbImgs = selectPaths.size() + imgNb;
            for (int i = 0; i < selectPaths.size(); i++) {
                final Uri[] uri = new Uri[selectPaths.size()];
                uri[i] = Uri.parse("file://" + selectPaths.get(i));
                final StorageReference ref = firebaseStorage.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(chalet.getChaletNm())).child(String.valueOf(imgName));
                ref.putFile(uri[i])
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String content = downloadUrl.toString();
                                if (content.length() > 0) {
                                    //++chaletCount;
                                    firebaseStorage.child(mAuth.getCurrentUser().getUid()).child(String.valueOf(chalet.getChaletNm())).child(String.valueOf(imgName));
                                }
                            }
                        });
                imgName++;
                // do something u want
            }
            FirebaseDatabase.getInstance().getReference().child("chalets").child(chalet.getId()).child("nbImages")
                    .setValue(imgNb);

            Toast.makeText(getApplicationContext(), R.string.doneUpload, Toast.LENGTH_SHORT).show();
            updatePhotosAfterUploads();
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

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(final BaseSliderView slider) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManageChalet.this);
        alertDialogBuilder.setMessage(getString(R.string.sureDelete));
        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        StorageReference mStorageRef;
                        String storageurl = slider.getUrl();
                        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(storageurl);
                        mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Toast.makeText(getApplicationContext(), "Photo deleted", Toast.LENGTH_SHORT).show();
                                int newNb = Integer.parseInt(chalet.getNbImages()) - 1;
                                FirebaseDatabase.getInstance().getReference().child("chalets").child(chalet.getId())
                                        .child("nbImages").setValue(String.valueOf(newNb));
                                if(newNb == 0)
                                    mDemoSlider.setVisibility(View.GONE);

                                recreate();
                                //Log.d(TAG, "onSuccess: deleted file");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                //Log.d(TAG, "onFailure: did not delete file");
                                Toast.makeText(getApplicationContext(), "Error deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        alertDialogBuilder.setNegativeButton(getString(R.string.no),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

    public void updatePhotosAfterUploads(){
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        if (Integer.parseInt(chalet.getNbImages()) > 0) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm());
            for (int i = 1; i <= Integer.parseInt(chalet.getNbImages()); i++) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        TextSliderView textSliderView = new TextSliderView(ManageChalet.this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(getString(R.string.clickToDelete))
                                .image(uri.toString())
                                .setBitmapTransformation(new CenterCrop())
                                .setOnSliderClickListener(ManageChalet.this);
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
            mDemoSlider.addOnPageChangeListener(ManageChalet.this);
        }
        else mDemoSlider.setVisibility(View.GONE);
    }
}