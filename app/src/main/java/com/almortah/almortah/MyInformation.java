package com.almortah.almortah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyInformation extends AppCompatActivity {
    private String name;
    private String email;
    private String nbChalets;
    private String phone;
    private String type;
    private String username;

    private TextView mUsername;
    private TextView mFullname;
    private TextView mPhonenumber;
    private TextView mEmail;
    private TextView mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_informaion);

        mUsername = (TextView) findViewById(R.id.username);
        mFullname = (TextView) findViewById(R.id.fullname);
        mPhonenumber = (TextView) findViewById(R.id.phone);
        mEmail = (TextView) findViewById(R.id.email);
        mType = (TextView) findViewById(R.id.type);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        mUsername.setText(user.getDisplayName());
        mEmail.setText(user.getEmail());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users info = dataSnapshot.getValue(Users.class);
                mPhonenumber.setText(info.getPhone());
                mType.setText(info.getType());
                mUsername.setText(info.getUsername());
                mFullname.setText(info.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
