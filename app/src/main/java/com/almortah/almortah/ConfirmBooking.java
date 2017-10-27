package com.almortah.almortah;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ConfirmBooking extends AppCompatActivity {
    private String date;
    private String ownerID;
    private String chaletNb;
    private String finalDates;
    private DatabaseReference reference;
    private String upDate ;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String checkin;
    private TimePicker inTime;
    private String price;
    private String payment;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        Bundle info = getIntent().getExtras();
        date = info.getString("date");
        ownerID = info.getString("ownerID");
        chaletNb = info.getString("chaletNb");
        finalDates = info.getString("finalDates");
        price = info.getString("price");
        name = info.getString("name");

        inTime = (TimePicker) findViewById(R.id.checkin);
        upDate = finalDates+","+date;
        reference = FirebaseDatabase.getInstance().getReference().child("busyDates").child(ownerID).child(chaletNb).child("busyOn");
        TextView dateView = (TextView) findViewById(R.id.date);
        dateView.setText(dateView.getText().toString()+" "+date);

        TextView priceView = (TextView) findViewById(R.id.finalPrice);
        priceView.setText(R.string.Price +": "+ price);


        //Map<String, Object> update = new HashMap<String, Object>();
        //update.put("busyOn", new String(upDate));
        //reference.updateChildren(update);
        Toast.makeText(getApplicationContext(),"Now its busy",Toast.LENGTH_SHORT).show();

        Button confirm = (Button) findViewById(R.id.confirm);
        payment = "cash";
        final RadioButton visa = (RadioButton) findViewById(R.id.visa);

        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(visa.isChecked())
                    payment = "visa";
                checkin = String.valueOf(inTime.getHour()+":"+inTime.getMinute());
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("customerID",mAuth.getCurrentUser().getUid());
                map.put("ownerID",ownerID);
                map.put("chaletNb",chaletNb);
                map.put("date",date);
                map.put("check-in",checkin);
                map.put("check-out","2:00");
                map.put("payment",payment);
                map.put("price",price);
                map.put("chaletName",name);
                mDatabase.child("reservation").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                upDate = String.valueOf(dataSnapshot.getValue());
                                upDate = upDate + "," + date;
                                mDatabase.child("busyDates").child(ownerID).child(chaletNb).child("busyOn").setValue(String.valueOf(upDate));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        startActivity(new Intent(ConfirmBooking.this,MyReservation.class));
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.visitor_menu, menu);
        } else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.customer_menu, menu);
        }


        return true;
    }

    /**
     * Event Handling for Individual visitor_menu item selected
     * Identify single visitor_menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchChaleh:
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,HomeActivity.class));
                return true;
            case R.id.login:
                startActivity(new Intent(this,login.class));
                return true;
            case R.id.register:
                startActivity(new Intent(this,Signup.class));
                return true;
            case R.id.history:
                startActivity(new Intent(this,MyReservation.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e("MyActivity", "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onBackPressed() {
        // your code.
        startActivity(new Intent(this,HomePage.class));
    }


}
