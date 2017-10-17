package com.almortah.almortah;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ALMAHRI on 10/13/17.
 */

public class AlmortahDB extends Activity {
    private final DatabaseReference almortahDB = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Activity context;
    private boolean flag;

    public AlmortahDB(Activity context) {
        this.context = context;
    }

    public void signup(final String fullname, final String username, final String phone, final String email, String password, final int type) {
        Task<AuthResult> i = mAuth.createUserWithEmailAndPassword(email,password);
        i.isComplete();
        i.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Store user info in the database
                    FirebaseUser user = task.getResult().getUser();
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("Name", fullname);
                        hashMap.put("username", username);
                        hashMap.put("phone", phone);
                        hashMap.put("email", email);
                        hashMap.put("nbChalets","0");
                        hashMap.put("type", String.valueOf(type));
                        almortahDB.child("users").child(user.getUid()).setValue(hashMap);
                }
            }
        });
    }

    public ArrayList<Chalet> getAllChalets() {
        final ArrayList<Chalet> chalets = new ArrayList<>();
        DatabaseReference reference = almortahDB.child("chalets");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while ((iterator.hasNext())) {
                    Chalet chalet = iterator.next().getValue(Chalet.class);
                        chalets.add(chalet);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return chalets;
    }

    public ArrayList<Chalet> getMyChalets(final String ownerID){
        final ArrayList<Chalet> ownerChalets = new ArrayList<>();
        final String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference chalet = almortahDB.child("chalets");
        chalet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while ((iterator.hasNext())) {
                    Chalet chalet = iterator.next().getValue(Chalet.class);
                    if(chalet.getOwnerID().equals(ownerID)) {
                        ownerChalets.add(chalet);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return ownerChalets;
    }

    public ArrayList<Users> getAllUsers(int userType) { // 1: Customer, 2: Owner
        return null;
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            // after email is sent just logout the user and finish this activity
                            try {
                                mAuth.getInstance().signOut();
                                startActivity(new Intent(context.getApplicationContext(), login.class));
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(context.getApplicationContext(), R.string.erVerifyEmail, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(new Intent(context.getApplicationContext(), Signup.class));

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public DatabaseReference getAlmortahDB() {
        return almortahDB;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }


}

