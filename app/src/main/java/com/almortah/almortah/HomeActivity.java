package com.almortah.almortah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button guestButton;
    private Button signinButton;
    private Button signupButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth.signOut();
        guestButton = (Button) findViewById(R.id.guestButton);
        signinButton = (Button) findViewById(R.id.signinButton);
        signupButton = (Button) findViewById(R.id.submitOwner);

        guestButton.setOnClickListener(this);
        signinButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId())
        {
            case R.id.guestButton :
                Intent intent = new Intent(this,ChaletListActivity.class);
                startActivity(intent);
                break;
            case R.id.signinButton :
                Intent signin = new Intent(this,login.class);
                startActivity(signin);
                break;
            case R.id.submitOwner:
                Intent signup = new Intent(this,Signup.class);
                startActivity(signup);
                break;


        }

    }

}
