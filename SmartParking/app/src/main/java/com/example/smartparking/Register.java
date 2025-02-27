package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    EditText e1, e2, e3, e4;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference node;
    long node_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e1 = (EditText) findViewById(R.id.cnic); // CNIC
        e2 = (EditText) findViewById(R.id.contact); //Contact
        e3 = (EditText) findViewById(R.id.email); // Email
        e4 = (EditText) findViewById(R.id.regNo); //Registration No

        node = db.getReference("Users/UnAuthorize");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    node_id = (dataSnapshot.getChildrenCount());
                    Log.e("IDs: ", String.valueOf(node_id));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Register.this, "Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addstudent(View v) {
        if (node_id <= 3) {
            String cnic = e1.getText().toString().trim();
            String contact = e2.getText().toString().trim();
            String email = e3.getText().toString().trim();
            String regisNo = e4.getText().toString().trim();

            if (cnic.isEmpty()) {
                e1.setError("Please Enter your CNIC");
                e2.requestFocus();
                return;
            }
            if (contact.isEmpty()) {
                e2.setError("Please Enter your Conact No");
                e2.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                e3.setError("Please Enter your Email");
                e3.requestFocus();
                return;
            }
            if (regisNo.isEmpty()) {
                e4.setError("Please Enter your Registration No");
                e4.requestFocus();
                return;
            }

            // Writing data to Firebase
            node.child(String.valueOf(node_id + 1)).child("cnic").setValue(cnic);
            node.child(String.valueOf(node_id + 1)).child("contact").setValue(contact);
            node.child(String.valueOf(node_id + 1)).child("email").setValue(email);
            node.child(String.valueOf(node_id + 1)).child("regNo").setValue(regisNo);

            e1.setText("");
            e2.setText("");
            e3.setText("");
            e4.setText("");
            Toast.makeText(getApplicationContext(), "Saved Successfully.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Max. Limit Reached", Toast.LENGTH_LONG).show();
        }
    }
}