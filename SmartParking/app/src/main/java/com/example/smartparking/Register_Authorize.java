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

public class Register_Authorize extends AppCompatActivity {

    EditText e1, e2, e3;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference node;
    long node_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_authorize);

        e1 = (EditText) findViewById(R.id.depart); // CNIC
        e2 = (EditText) findViewById(R.id.design); //Contact
        e3 = (EditText) findViewById(R.id.regNo); // Registration No

        node = db.getReference("Users/Authorize");
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
                Toast.makeText(Register_Authorize.this, "Connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addfaculty(View v) {
        if (node_id <= 3) {
            String depart = e1.getText().toString().trim();
            String desig = e2.getText().toString().trim();
            String regNo = e3.getText().toString().trim();


            if (depart.isEmpty()) {
                e1.setError("Please Enter your Department");
                e2.requestFocus();
                return;
            }
            if (desig.isEmpty()) {
                e2.setError("Please Enter your Designation");
                e2.requestFocus();
                return;
            }
            if (regNo.isEmpty()) {
                e3.setError("Please Enter your Registration No");
                e3.requestFocus();
                return;
            }

            // Writing data to Firebase
            node.child(String.valueOf(node_id + 1)).child("depart").setValue(depart);
            node.child(String.valueOf(node_id + 1)).child("desig").setValue(desig);
            node.child(String.valueOf(node_id + 1)).child("regNo").setValue(regNo);

            e1.setText("");
            e2.setText("");
            e3.setText("");

            Toast.makeText(getApplicationContext(), "Saved Successfully.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Register_Authorize.this, Login_Autorize.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Max. Limit Reached", Toast.LENGTH_LONG).show();
        }
    }
}