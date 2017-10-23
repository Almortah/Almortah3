package com.almortah.almortah;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ChaletInfoCustomer extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private FirebaseDatabase mDatabase ;
    private StorageReference storageReference ;

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

    boolean isImageFitToScreen;



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

        nameView = (TextView) findViewById(R.id.chaletName);
        normalPriceView = (TextView) findViewById(R.id.normalPrice);
        weekendPriceView = (TextView) findViewById(R.id.weekendPrice);
        eidPriceView = (TextView) findViewById(R.id.eidPrice);
        locationView = (TextView) findViewById(R.id.location);
        owner = (TextView) findViewById(R.id.ownerName);
        img1 = (ImageView) findViewById(R.id.img1C);
        img2 = (ImageView) findViewById(R.id.img2C);
        img3 = (ImageView) findViewById(R.id.img3C);
        img4 = (ImageView) findViewById(R.id.img4C);
        img5 = (ImageView) findViewById(R.id.img5C);

        nameView.setText(name);
        normalPriceView.setText(normalPrice);
        weekendPriceView.setText(weekendPrice);
        eidPriceView.setText(eidPrice);
        locationView.setText(location);
        owner.setText(ownerName);

        for (int i = 1; i < 4; i++) {
            if (i == 1) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        Glide.with(ChaletInfoCustomer.this)
                                .load(uri)
                                .into(img1);
                        img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
                                intent.putExtra("img", uri );
                                startActivity(intent);
                            }
                        });
                    }
                });
            } else if (i == 2) {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        Glide.with(ChaletInfoCustomer.this)
                                .load(uri)
                                .into(img2);
                        img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
                                intent.putExtra("img", uri );
                                startActivity(intent);
                            }
                        });
                    }
                });
            } else {
                StorageReference tmp = storageReference.child(String.valueOf(i));
                tmp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                                Glide.with(ChaletInfoCustomer.this)
                                .load(uri)
                                .into(img3);
                        img3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChaletInfoCustomer.this, FullscreenActivity.class);
                                intent.putExtra("img", uri );
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }


        Button book = (Button) findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() == null) {
                    Toast.makeText(getApplicationContext(),R.string.bookVistor,Toast.LENGTH_SHORT).show();
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
}
