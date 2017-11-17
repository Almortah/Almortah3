package com.almortah.almortah;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

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
        ImageView toArabic = (ImageView) findViewById(R.id.toArabic);
        ImageView toEnglish = (ImageView) findViewById(R.id.toEnglish);
        toArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ar","SA");
            }
        });

        toEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en","US");
            }
        });

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
                Intent intent = new Intent(this,HomePage.class);
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

    public void setLocale(String lang, String country) {
        //create a string for country
        //use constructor with country

      /*  Configuration mainConfig = new Configuration(getResources().getConfiguration());
        String languageToLoad = lang;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        mainConfig.setLocale(locale);
        getResources().updateConfiguration(mainConfig, null);*/

        Locale locale = new Locale(lang, country);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        //config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

}
