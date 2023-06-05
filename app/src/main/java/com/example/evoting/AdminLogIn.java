package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evoting.Admin.AdminPanel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AdminLogIn extends AppCompatActivity {
    AdminDatabaseConnection connection;
    DatabaseReference reference;
    EditText cnic;
    EditText password;
    Intent intent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);
        connection=AdminDatabaseConnection.createConnection();
        reference=connection.getDatabaseReference();
        cnic=findViewById(R.id.adminemailLogIn1);
        password=findViewById(R.id.adminpasswordLogIn1);
    }

    public void goToCitizenLogIn(View view) {
        Intent intent=new Intent(this, LogIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void logInAdmin(View view) {
        String cnicPattern = "^\\d{5}-\\d{7}-\\d$";
        intent=new Intent(this, AdminPanel.class);
        if(!cnic.getText().toString().matches(cnicPattern))
        {
            Toast.makeText(AdminLogIn.this, "Cnic not valid", Toast.LENGTH_SHORT).show();
        }
        else {
            reference.child(cnic.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists())
                    {
                        Toast.makeText(AdminLogIn.this, "You are not registered as Admin", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(snapshot.child("password").getValue().toString().equals(password.getText().toString())&&snapshot.child("cnic").getValue().toString().equals(cnic.getText().toString()))
                        {
                             startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(AdminLogIn.this, "Incorrect Password or CNIC", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}