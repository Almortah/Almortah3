package com.almortah.almortah;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText mEmailField;
    private Button mSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mEmailField = (EditText) findViewById(R.id.email);
        mSubmit = (Button) findViewById(R.id.submit);
        Log.d("Sent", "Email sent.");
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        mSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String emailAddress = mEmailField.getText().toString();
                Log.d("Sent", emailAddress);

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Reset password instructions has sent to your email",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });


    }
}
