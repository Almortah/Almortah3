package com.almortah.almortah;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyInformation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private TextView mUsername;
    private TextView mFullname;
    private TextView mPhonenumber;
    private TextView mEmail;
    private TextView mType;

    boolean isChanged = false;
    private EditText newEmail;
    private EditText newFullname;
    private EditText newPhone;
    private Button submit;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_informaion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);






    mUsername = (TextView) findViewById(R.id.username);
        mFullname = (TextView) findViewById(R.id.fullname);
        mPhonenumber = (TextView) findViewById(R.id.phone);
        mEmail = (TextView) findViewById(R.id.email);
        mType = (TextView) findViewById(R.id.type);

        mUsername.setText(user.getDisplayName());
        mEmail.setText(user.getEmail());
        final Users[] info = new Users[1];
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info[0] = dataSnapshot.getValue(Users.class);
                mPhonenumber.setText(info[0].getPhone());
                mUsername.setText(info[0].getUsername());
                mFullname.setText(info[0].getName());
                if(info[0].getType().equals("1")) {
                    mType.setText(getString(R.string.customer));
                    navigationView.inflateMenu(R.menu.customer_menu);
                }

                else {
                    mType.setText(getString(R.string.owner));
                    navigationView.inflateMenu(R.menu.owner_menu);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Button edit = (Button) findViewById(R.id.edit);
        submit = (Button) findViewById(R.id.submit);
        newEmail = (EditText) findViewById(R.id.emailE);
        newFullname = (EditText) findViewById(R.id.fullnameE);
        newPhone = (EditText) findViewById(R.id.phoneE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.VISIBLE);
                newFullname.setVisibility(View.VISIBLE);
                newPhone.setVisibility(View.VISIBLE);
                newEmail.setText(user.getEmail());
                newFullname.setText(info[0].getName());
                newPhone.setText(info[0].getPhone());
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mFullname.getText().toString().trim().equals(newFullname.getText().toString().trim())) {
                    reference.child("Name").setValue(newFullname.getText().toString().trim());
                    isChanged = true;
                }
                if(!newEmail.getText().toString().trim().equals(user.getEmail())) {
                    user.updateEmail(newEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            reference.child("email").setValue(newEmail.getText().toString().trim());
                            isChanged = true;
                        }
                    });
                }

                if(!newPhone.getText().toString().trim().equals(mPhonenumber.getText().toString())) {
                    reference.child("phone").setValue(newPhone.getText().toString().trim());
                    isChanged = true;
                }

                if(isChanged) {
                    Toast.makeText(getBaseContext(), R.string.updateProfile, Toast.LENGTH_LONG).show();
                    recreate();
                }
            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        AlmortahDB almortahDB = new AlmortahDB(this);
        almortahDB.menu(item);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
