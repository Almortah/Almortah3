package com.almortah.almortah;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Promotion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        final Chalet chalet = (Chalet) getIntent().getSerializableExtra("chalet");
        Button submit = (Button) findViewById(R.id.submit);
        final RadioGroup offer = (RadioGroup) findViewById(R.id.offer);
        final RadioGroup payment = (RadioGroup) findViewById(R.id.payment);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offer.getCheckedRadioButtonId() != -1 && payment.getCheckedRadioButtonId() != -1) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Promotion.this);
                    alertDialogBuilder.setMessage("Are you sure You wanted to make decision");
                            alertDialogBuilder.setPositiveButton("yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                            Map<String, Chalet> chaletMap = new HashMap<>();
                                            chaletMap.put(chalet.getName(), chalet);
                                            mDatabase.child("promotion").push().setValue(chaletMap);
                                        }
                                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                }
        });


    }
}
