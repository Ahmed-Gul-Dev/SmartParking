package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParkingSlot1 extends AppCompatActivity {
    TextView slotview;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference node;
    DatabaseReference slotnode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot1);

        slotview = (TextView) findViewById(R.id.pslotA);
        node = db.getReference("ParkingSlots/Authorize/1");
        slotnode = node.child("status");

        slotnode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class); // Patient Name
                slotview.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ParkingSlot1.this, "Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}