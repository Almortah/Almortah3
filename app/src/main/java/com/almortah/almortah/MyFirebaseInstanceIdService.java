package com.almortah.almortah;

import android.content.Intent;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TOKEN_BROADCAST = "New Token Generated";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d("Token", "Refreshed token: " + refreshedToken);
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(refreshedToken);

    }
    /*private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }*/
    private void storeToken(String token ){
        SharedPrefManager.getmInstance(getApplicationContext()).storeToken(token);
    }

}
