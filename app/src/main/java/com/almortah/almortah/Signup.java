package com.almortah.almortah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class Signup extends AppCompatActivity {

    private Spinner userType;
    boolean flag = false;
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private final static String TAG = "SignUP";

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText smsCode;
    private EditText mUsername;
    private EditText mFullname;
    private EditText mPhonenumber;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPassword2;
    private RadioGroup mRadio;
    private Button mSignup;
    private AlmortahDB almortahDB;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        almortahDB = new AlmortahDB(this);
        //FirebaseApp.initializeApp(this);


        mAuth = FirebaseAuth.getInstance();


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

                if (ownerType.isChecked())
                    type = 2;

                if (username.equals("") || fullname.equals("") || phone.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(Signup.this, R.string.erEmptyField, Toast.LENGTH_LONG).show();
                    return;
                } else if (!(password.equals(passowrd2))) {
                    Toast.makeText(Signup.this, R.string.erVerifyPassword, Toast.LENGTH_LONG).show();
                    return;
                } else if (phone.length() < 9 || !phone.startsWith("5")) {
                    Toast.makeText(Signup.this, R.string.badPhone, Toast.LENGTH_LONG).show();
                } else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), R.string.badPas, Toast.LENGTH_LONG).show();
                    return;
                } else {
                        almortahDB.signup(fullname, username, phone, email, password, type);
                }
            }
        });

    }

}