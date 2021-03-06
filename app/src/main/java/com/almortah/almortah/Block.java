package com.almortah.almortah;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Block extends AppCompatActivity {
    private ArrayList<Integer> userReasons = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView r1 = (TextView) findViewById(R.id.r1);
        final TextView r2 = (TextView) findViewById(R.id.r2);
        final TextView r3 = (TextView) findViewById(R.id.r3);
        final TextView r4 = (TextView) findViewById(R.id.r4);

       // String lang = Locale.getDefault().getLanguage();//return
        final String[] reasons = getResources().getStringArray(R.array.reasons);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference().child("blacklistReasons").child(user.getUid()).child("reasons")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] tmp = dataSnapshot.getValue().toString().split("-");
                for (int i = 0; i< (tmp.length); i++) {
                    userReasons.add(Integer.parseInt(tmp[i]));
                }
                for (int i = 0; i<userReasons.size(); i++) {
                    if(i == 0)
                        r1.setText(reasons[userReasons.get(i)]);
                    else if (i == 1)
                        r2.setText(reasons[userReasons.get(i)]);
                    else if (i == 2)
                        r3.setText(reasons[userReasons.get(i)]);
                    else if (i == 3)
                        r4.setText(reasons[userReasons.get(i)]);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        Button contact = (Button) findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Send email", "");

                String[] TO = {"aalmortah@gmail.com"};
                //String[] CC = {"xyz@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");


                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "About block my account");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("Finished", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Block.this,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
