package com.almortah.almortah;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;
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
    public AlmortahDB(){}

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
                        hashMap.put("userID",user.getUid());
                        hashMap.put("type", String.valueOf(type));
                        hashMap.put("isApproved","0");
                        almortahDB.child("users").child(user.getUid()).setValue(hashMap);
                        context.startActivity(new Intent(context,login.class));
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

    public void menu(MenuItem item){
        switch(item.getItemId()) {
            case R.id.searchChaleh:
                context.startActivity(new Intent(context, Search.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                context.startActivity(new Intent(context, HomeActivity.class));
                break;
            case R.id.login:
                context.startActivity(new Intent(context, login.class));
                break;
            case R.id.register:
                context.startActivity(new Intent(context, Signup.class));
                break;
            case R.id.history:
                context.startActivity(new Intent(context, MyReservation.class));
                break;
            case R.id.newChalet:
              almortahDB.child("users").child(mAuth.getCurrentUser().getUid()).child("isApproved")
                      .addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                              if(dataSnapshot.exists()) {
                                  if(dataSnapshot.getValue().toString().equals("1"))
                                      context.startActivity(new Intent(context, AddChalet.class));
                                    else
                                      Toast.makeText(context,R.string.cantAdd,Toast.LENGTH_LONG).show();
                              }
                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {

                          }
                      });
                break;
            case R.id.about:
                context.startActivity(new Intent(context, About.class));
                break;
            case R.id.homePage:
                context.startActivity(new Intent(context, HomePage.class));
                break;
            case R.id.myInfo:
                context.startActivity(new Intent(context, MyInformation.class));
                break;
            case R.id.myChalet:
                context.startActivity(new Intent(context, MyChalets.class));
                break;
            case R.id.rateChalet:
                context.startActivity(new Intent(context, RateAChalet.class));
                break;
            case R.id.current:
                context.startActivity(new Intent(context, CurrentReservations.class));
                break;
            case R.id.rateCustomer:
                context.startActivity(new Intent(context, RateCustomer.class));
                break;
            case R.id.approvBooking:
                context.startActivity(new Intent(context, ApproveBookingByOwner.class));
                break;
            case R.id.stat:
                context.startActivity(new Intent(context, Statics.class));
                break;
            default:
                super.onOptionsItemSelected(item);
        }
    }


    public DatabaseReference getAlmortahDB() {
        return almortahDB;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void adminMenu(MenuItem item){

        switch(item.getItemId()) {
            case R.id.searchChaleh:
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                context.startActivity(new Intent(context, HomeActivity.class));
                break;
            case R.id.approve:
                context.startActivity(new Intent(context, ApproveOwners.class));
                break;
            case R.id.chalets:
                context.startActivity(new Intent(context, HomePage.class));
                break;
            case R.id.users:
                context.startActivity(new Intent(context, AdminPage.class));
                break;
            case R.id.homePage:
                context.startActivity(new Intent(context, HomePage.class));
                break;
            case R.id.promotion:
                context.startActivity(new Intent(context, ApprovePromotions.class));
                break;
            case R.id.complain:
                context.startActivity(new Intent(context, CustomerComplaints.class)); // users
                break;
            default:
                super.onOptionsItemSelected(item);
        }
    }



}

