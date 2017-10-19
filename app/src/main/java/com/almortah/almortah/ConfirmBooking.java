package com.almortah.almortah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ConfirmBooking extends AppCompatActivity {
    private String date;
    private String ownerID;
    private String chaletNb;
    private String finalDates;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        Bundle info = getIntent().getExtras();
        date = info.getString("date");
        ownerID = info.getString("ownerID");
        chaletNb = info.getString("chaletNb");
        finalDates = info.getString("finalDates");
        String upDate = finalDates+","+date;
        reference = FirebaseDatabase.getInstance().getReference().child("busyDates").child(ownerID).child(chaletNb);
        TextView dateView = (TextView) findViewById(R.id.date);
        dateView.setText(dateView.getText().toString()+date);
        Map<String, Object> update = new HashMap<String, Object>();
        update.put("busyOn", new String(upDate));
        reference.updateChildren(update);
        Toast.makeText(getApplicationContext(),"Now its busy",Toast.LENGTH_SHORT).show();

    }
}
