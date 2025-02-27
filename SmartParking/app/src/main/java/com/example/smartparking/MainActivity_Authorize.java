package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity_Authorize extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_authorize);

        CardView pslot1 = findViewById(R.id.psA); //Parking Slot 4
        CardView pslot2 = findViewById(R.id.psB); //Parking Slot 5
        CardView pslot3 = findViewById(R.id.psC); //Parking Slot 6



        pslot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_Authorize.this, ParkingSlot1.class);
                startActivity(i);
            }
        });

        pslot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_Authorize.this, ParkingSlot2.class);
                startActivity(i);
            }
        });

        pslot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_Authorize.this, ParkingSlot3.class);
                startActivity(i);
            }
        });
    }
}