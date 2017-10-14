package com.almortah.almortah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyChalets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chalets);
        Intent intent = new Intent(MyChalets.this, AddChalet.class);
        startActivity(intent);
    }
}
