package com.almortah.almortah;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddChalet extends AppCompatActivity {

    private Button mUploadImage;
    private StorageReference firebaseStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText mChaletName;
    private EditText mChaletPrice;
    private Button submitChalet;
    private Chalet chalet;
    static final int PICK_CONTACT_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chalet);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mChaletName = (EditText) findViewById(R.id.chaletName);
        mChaletPrice= (EditText) findViewById(R.id.chaletPrice);
        submitChalet = (Button) findViewById(R.id.submitChalet);

        submitChalet.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String chaletName= mChaletName.getText().toString().trim();
                String chaletPrice = mChaletPrice.getText().toString().trim();
                String chaletOwnerId = user.getUid().toString();
                HashMap<String,String> hashMap = new HashMap<String, String>();
                String url = firebaseStorage.child(user.getUid().toString()).getDownloadUrl().getResult().toString();
                hashMap.put("user id",chaletOwnerId);
                hashMap.put("name",chaletName);
                hashMap.put("price", chaletPrice);
                hashMap.put("ImageUrl",url);
                mDatabase.child("chalet").push().setValue(hashMap);
            }
        });



        mUploadImage= (Button) findViewById(R.id.upload);

        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent,PICK_CONTACT_REQUEST);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK){
            Log.v("CHECK","CHECKING ");
            Uri uri = data.getData();
            StorageReference filepath = firebaseStorage.child(user.getUid()).child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddChalet.this,"Upload Done",Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
