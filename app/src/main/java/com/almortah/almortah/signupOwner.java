package com.almortah.almortah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signupOwner extends AppCompatActivity {
    private EditText mUsername;
    private EditText mFullname;
    private EditText mPhonenumber;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPassword2;
    private AlmortahDB almortahDB = new AlmortahDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner);
        //EditText
        mUsername = (EditText) findViewById(R.id.ownerUsername);
        mFullname = (EditText) findViewById(R.id.ownerFullname);
        mPhonenumber = (EditText) findViewById(R.id.ownerPhone);
        mEmail = (EditText) findViewById(R.id.ownerEmail);
        mPassword = (EditText) findViewById(R.id.ownerPassword);
        mPassword2 = (EditText) findViewById(R.id.ownerPassword2);

        Button submit = (Button) findViewById(R.id.submitOwner);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsername.getText().toString().trim();
                final String fullname = mFullname.getText().toString().trim();
                final String phone = mPhonenumber.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String passowrd2 = mPassword2.getText().toString().trim();

                if (username.equals("") || fullname.equals("") || phone.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(signupOwner.this, R.string.erEmptyField, Toast.LENGTH_LONG).show();
                    return;
                } else if (!(password.equals(passowrd2))) {
                    Toast.makeText(signupOwner.this, R.string.erVerifyPassword, Toast.LENGTH_LONG).show();
                    return;
                }
                    almortahDB.signupAnOwner(fullname, username, phone, email, password);
                    startActivity(new Intent(signupOwner.this , login.class));

            }
        });
    }
}
