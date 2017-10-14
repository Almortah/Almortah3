package com.almortah.almortah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText mEmailField;
    private EditText mPassField;
    private Button mLogin;
    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Error");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailField = (EditText) findViewById(R.id.email);
        mPassField = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in
        onStart();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin(mEmailField.getText().toString(),mPassField.getText().toString());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    public void signin(String email,String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    updateUI(null);
                }

            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG,"Error");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference type=mDatabase.child("users").child(user.getUid()).child("type");
            type.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String typeValue = dataSnapshot.getValue().toString();
                    Log.i("TYPE",typeValue);
                    if(typeValue=="1"){
                        Intent intent = new Intent(login.this,ChaletListActivity.class);
                        startActivity(intent);
                    }
                    else{
                        startActivity(new Intent(login.this,MyChalets.class));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("Error","Error fetch type");

                }
            });



        } else {
            Toast.makeText(this,"Wrong Email or Password",Toast.LENGTH_LONG).show();
        }
    }






}
