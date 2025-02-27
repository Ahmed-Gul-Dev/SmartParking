package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ReactiveGuide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        node = db.getReference("FromPi/scan");


        CardView pslot1 = findViewById(R.id.psA); //Parking Slot 4
        CardView pslot2 = findViewById(R.id.psB); //Parking Slot 5
        CardView pslot3 = findViewById(R.id.psC); //Parking Slot 6


        pslot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ParkingSlot4.class);
                startActivity(i);
            }
        });

        pslot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ParkingSlot5.class);
                startActivity(i);
            }
        });

        pslot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ParkingSlot6.class);
                startActivity(i);
            }
        });
    }
        public void scanf(View v){
            node.child("scan").setValue("YES");
        }


}