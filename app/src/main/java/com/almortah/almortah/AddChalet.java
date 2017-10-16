package com.almortah.almortah;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.util.PickConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AddChalet extends AppCompatActivity {

    private StorageReference storageRef;
    private FirebaseApp app;
    private FirebaseStorage storage;

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


    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chalet);
        app = FirebaseApp.getInstance();
        storage = FirebaseStorage.getInstance(app);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mChaletName = (EditText) findViewById(R.id.chaletName);
        mChaletPrice = (EditText) findViewById(R.id.chaletPrice);
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
                mDatabase.child("chalets").push().setValue(hashMap);
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
            imgName = 1;
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
        }
    }
}