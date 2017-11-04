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
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

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
                                            HashMap<String, String> hashMap = new HashMap<String, String>();
                                            hashMap.put("ownerID", chalet.getOwnerID());
                                            hashMap.put("name", chalet.getName());
                                            hashMap.put("normalPrice", chalet.getNormalPrice());
                                            hashMap.put("weekendPrice", chalet.getWeekendPrice());
                                            hashMap.put("eidPrice", chalet.getEidPrice());
                                            hashMap.put("ImageUrl", FirebaseStorage.getInstance().getReference().child(chalet.getOwnerID()).child(chalet.getChaletNm()).getPath());
                                            hashMap.put("chaletNm", chalet.getChaletNm());
                                            hashMap.put("promotion", "1"); // 0 no promoted, 1 promoted
                                            hashMap.put("latitude",chalet.getLatitude());
                                            hashMap.put("longitude",chalet.getLongitude());
                                            hashMap.put("nbImages", chalet.getNbOfImages());
                                            mDatabase.child("promotion").push().setValue(hashMap);
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
