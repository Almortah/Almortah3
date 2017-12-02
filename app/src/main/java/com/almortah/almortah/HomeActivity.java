package com.almortah.almortah;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button guestButton;
    private Button signinButton;
    private Button signupButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       /* PusherAndroid pusher = new PusherAndroid("06a4b24b55e1a8192374");
        PushNotificationRegistration nativePusher = pusher.nativePusher();
        try {
            nativePusher.registerFCM(this);
        } catch (ManifestValidator.InvalidManifestException e) {
            e.printStackTrace();
        }*/


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
                //test



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
//    public static void sendNotificationToUser(String user, final String message) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//        final DatabaseReference notifications = ref.child("notificationRequests");
//
//        Map notification = new HashMap<>();
//        notification.put("username", user);
//        notification.put("message", message);
//
//        notifications.push().setValue(notification);
//    }

}
