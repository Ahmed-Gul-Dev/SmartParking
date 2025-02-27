package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParkingSlot5 extends AppCompatActivity {
    TextView slotview,feesview;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference node;
    DatabaseReference slotnode,feesnode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot5);

        slotview = (TextView) findViewById(R.id.pslotB);
        feesview = (TextView) findViewById(R.id.fees);
        node = db.getReference("ParkingSlots/UnAuthorize/2");
        slotnode = node.child("status");
        feesnode = node.child("fees");

        slotnode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class); // Patient Name
                slotview.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ParkingSlot5.this, "Connection Error", Toast.LENGTH_LONG).show();
            }
        });
        feesnode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class); // Patient Name
                feesview.setText("Rs "+value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ParkingSlot5.this, "Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void payfees(View v){
        node.child("fees").setValue("0");
    }

}