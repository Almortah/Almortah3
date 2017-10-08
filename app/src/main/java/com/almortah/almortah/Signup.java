package com.almortah.almortah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    private EditText mUsername;
    private EditText mFullname;
    private EditText mPhonenumber;
    private EditText mEmail;
    private EditText mPassword;
    private FirebaseAuth mAuth;
    private Button mSignup;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //EditText
        mUsername = (EditText) findViewById(R.id.username);
        mFullname = (EditText) findViewById(R.id.fullname);
        mPhonenumber = (EditText) findViewById(R.id.phone);
        mEmail= (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mSignup= (Button) findViewById(R.id.signupButton);
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(mEmail.getText().toString(),mPassword.getText().toString());


            }
        });

    }
    private void createAccount(final String email, String password) {

        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String username= mUsername.getText().toString().trim();
                            String fullname = mFullname.getText().toString().trim();
                            String phone = mPhonenumber.getText().toString().trim();

                            //Store user info in the database
                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<String,String> hashMap = new HashMap<String, String>();
                            hashMap.put("Name",fullname );
                            hashMap.put("username",username);
                            hashMap.put("phone", phone);
                            hashMap.put("email", email);
                            mDatabase.child("users").child(user.getUid()).setValue(hashMap);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
           Intent intent = new Intent(this,login.class);
            startActivity(intent);

        } else {
            Toast.makeText(this,"Wrong Email or Password",Toast.LENGTH_LONG);
        }
    }

    //validate Input form
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Required.");
            valid = false;
        } else {
            mPassword.setError(null);
        }
        String username = mUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            mUsername.setError("Required.");
            valid = false;
        } else {
            mUsername.setError(null);
        }
        String phonenumber = mPhonenumber.getText().toString();
        if (TextUtils.isEmpty(phonenumber)) {
            mPhonenumber.setError("Required.");
            valid = false;
        } else {
            mPhonenumber.setError(null);
        }
        String fullname = mFullname.getText().toString();
        if (TextUtils.isEmpty(fullname)) {
            mFullname.setError("Required.");
            valid = false;
        } else {
            mFullname.setError(null);
        }

        return valid;
    }


}
