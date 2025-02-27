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

public class Login_Autorize extends AppCompatActivity {

    EditText etun,e2,e3;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference node,facultynode;
    long node_id1 = 0;
    String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_autorize);
        etun = (EditText) findViewById(R.id.regNo);
        e2 = (EditText) findViewById(R.id.depart);
        e3 = (EditText) findViewById(R.id.designation);

        node = db.getReference("Users/Authorize");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    node_id1 = (dataSnapshot.getChildrenCount());
                    Log.e("Authorized IDs: ", String.valueOf(node_id1));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Login_Autorize.this, "Connection Error", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void StudentCheck() {
        Log.e("Authorized IDs: ", String.valueOf(node_id1));
        list = new String[(int) node_id1];
        if (node_id1 == 1) {
            facultynode = node.child("1").child("regNo");
            facultynode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[0] = String.valueOf(value);
                    Log.e("Reg No. 1: ", list[0]);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login_Autorize.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (node_id1 == 2) {
            facultynode = node.child("1").child("regNo");
            facultynode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[0] = String.valueOf(value);
                    Log.e("Reg No. 1: ", list[0]);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login_Autorize.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
            facultynode = node.child("2").child("regNo");
            facultynode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[1] = String.valueOf(value);
                    Log.e("Reg No. 2: ", list[1]);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login_Autorize.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (node_id1 == 3) {
            facultynode = node.child("1").child("regNo");
            facultynode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[0] = String.valueOf(value);
                    Log.e("Reg No. 1: ", list[0]);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login_Autorize.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
            facultynode = node.child("2").child("regNo");
            facultynode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[1] = String.valueOf(value);
                    Log.e("Reg No. 2: ", list[1]);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login_Autorize.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
            facultynode = node.child("3").child("regNo");
            facultynode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class); // Patient Name
                    list[2] = String.valueOf(value);
                    Log.e("Reg No. 3: ", list[2]);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Login_Autorize.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void loginFaculty(View v){
        String registrationNo = etun.getText().toString();
        String depart = e2.getText().toString();
        String design = e3.getText().toString();
        if (registrationNo.isEmpty()) {
            etun.setError("Registration No. can't be Empty");
            Toast.makeText(Login_Autorize.this, "Enter Registration No.", Toast.LENGTH_LONG).show();
            etun.requestFocus();
            return;
        }
        if (depart.isEmpty()) {
            e2.setError("Department can't be Empty");
            Toast.makeText(Login_Autorize.this, "Enter Department", Toast.LENGTH_LONG).show();
            e2.requestFocus();
            return;
        }
        if (design.isEmpty()) {
            e3.setError("Designation can't be Empty");
            Toast.makeText(Login_Autorize.this, "Enter Designation", Toast.LENGTH_LONG).show();
            e3.requestFocus();
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
                        Log.e("Matched Faculty: ", registrationNo);
                        Log.e("ID: ", String.valueOf(i));
                        Toast.makeText(Login_Autorize.this, "Success !!", Toast.LENGTH_LONG).show();
                        Intent myintent = new Intent(Login_Autorize.this, MainActivity_Authorize.class);
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

    public void registerAuthorize(View v) {
        Intent i = new Intent(Login_Autorize.this, Register_Authorize.class);
        startActivity(i);
    }
}