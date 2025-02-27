package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText etun;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference node,studentnode;
    long node_id1 = 0;
    String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etun = (EditText) findViewById(R.id.regNo);

        node = db.getReference("Users/UnAuthorize");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    node_id1 = (dataSnapshot.getChildrenCount());
                    Log.e("UnAuthorized IDs: ", String.valueOf(node_id1));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void StudentCheck() {
        Log.e("UnAuthorized IDs: ", String.valueOf(node_id1));
        list = new String[(int) node_id1];
        if (node_id1 == 1) {
            studentnode = node.child("1").child("regNo");
            studentnode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[0] = String.valueOf(value);
                    Log.e("Reg No. 1: ", list[0]);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (node_id1 == 2) {
            studentnode = node.child("1").child("regNo");
            studentnode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[0] = String.valueOf(value);
                    Log.e("Reg No. 1: ", list[0]);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
            studentnode = node.child("2").child("regNo");
            studentnode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[1] = String.valueOf(value);
                    Log.e("Reg No. 2: ", list[1]);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (node_id1 == 3) {
            studentnode = node.child("1").child("regNo");
            studentnode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[0] = String.valueOf(value);
                    Log.e("Reg No. 1: ", list[0]);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
            studentnode = node.child("2").child("regNo");
            studentnode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[1] = String.valueOf(value);
                    Log.e("Reg No. 2: ", list[1]);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
            studentnode = node.child("3").child("regNo");
            studentnode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[2] = String.valueOf(value);
                    Log.e("Reg No. 3: ", list[2]);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void loginStudent(View v){
        String registrationNo = etun.getText().toString();
        if (registrationNo.isEmpty()) {
            etun.setError("Registration No. can't be Empty");
            Toast.makeText(Login.this, "Enter Registration No.", Toast.LENGTH_LONG).show();
            etun.requestFocus();
            return;
        }
        StudentCheck();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                boolean matched = false;
                    for (int i = 1; i <= node_id1; i++) {
                        if (registrationNo.equals(list[count])) {
                            matched = true;
                            Log.e("Matched Student: ", registrationNo);
                            Log.e("ID: ", String.valueOf(i));
                            Toast.makeText(Login.this, "Success !!", Toast.LENGTH_LONG).show();
                            Intent myintent = new Intent(Login.this, MainActivity.class);
                            startActivity(myintent);
                        }
                        count = count + 1;
                    }

                if (matched == false) {
                    Log.e("Error: ", "Invalid Registration No");
                    Toast.makeText(getApplicationContext(), "Invalid Registration No", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }

    public void register(View v) {
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
    }
}