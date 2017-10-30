package com.almortah.almortah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    private Spinner userType;

    private EditText mUsername;
    private EditText mFullname;
    private EditText mPhonenumber;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPassword2;
    private RadioGroup mRadio;
    private Button mSignup;
    private AlmortahDB almortahDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        almortahDB = new AlmortahDB(this);
        //EditText
        mUsername = (EditText) findViewById(R.id.username);
        mFullname = (EditText) findViewById(R.id.fullname);
        mPhonenumber = (EditText) findViewById(R.id.phone);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword2 = (EditText) findViewById(R.id.password2);
        mRadio = (RadioGroup) findViewById(R.id.type);

        mSignup = (Button) findViewById(R.id.submitCustomer);
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsername.getText().toString().trim();
                final String fullname = mFullname.getText().toString().trim();
                final String phone = mPhonenumber.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String passowrd2 = mPassword2.getText().toString().trim();
                int type = 1;
                RadioButton ownerType = (RadioButton) findViewById(R.id.ownerType);

                if(ownerType.isChecked())
                    type = 2;

                if (username.equals("") || fullname.equals("") || phone.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(Signup.this, R.string.erEmptyField, Toast.LENGTH_LONG).show();
                    return;
                } else if (!(password.equals(passowrd2))) {
                    Toast.makeText(Signup.this, R.string.erVerifyPassword, Toast.LENGTH_LONG).show();
                    return;
                } else if (phone.length() < 10 || !phone.startsWith("05")) {
                    Toast.makeText(Signup.this, "Bad phone", Toast.LENGTH_LONG).show();
                }

                else {
                        almortahDB.signup(fullname, username, phone, email, password, type);
                        Toast.makeText(getApplicationContext(),"YREEE",Toast.LENGTH_LONG).show();
                         startActivity(new Intent(Signup.this, login.class));

                }
            }
        });

    }

}
