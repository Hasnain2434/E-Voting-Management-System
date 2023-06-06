package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {
    CitizenDatabaseConnection connection;
    SharedPreferences sharedPreferences;
    DatabaseReference reference;
    EditText cnic;
    EditText password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        connection=CitizenDatabaseConnection.createConnection();
        reference=connection.getDatabaseReference();
        cnic=findViewById(R.id.emailLogIn1);
        password=findViewById(R.id.passwordLogIn1);
    }

    @SuppressLint("WorldWriteableFiles")
    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getSharedPreferences("MyFile", 0);
    }

    public void goTosignUpPage(View view) {
        Intent intent=new Intent(this, SignUp.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void goToAdminPage(View view) {
        Intent intent=new Intent(this, AdminLogIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void CitizenLogIn(View view) {
        String cnicPattern = "^\\d{5}-\\d{7}-\\d$";
        Intent intent=new Intent(this,CitizenPanel.class);
        if(!cnic.getText().toString().matches(cnicPattern))
        {
            Toast.makeText(LogIn.this, "Cnic not valid", Toast.LENGTH_SHORT).show();
        }
        else {
            reference.child(cnic.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists())
                    {
                        Toast.makeText(LogIn.this, "You are not registered", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(snapshot.child("password").getValue().toString().equals(password.getText().toString())&&snapshot.child("cnic").getValue().toString().equals(cnic.getText().toString()))
                        {
                            if(snapshot.child("casted").getValue().toString().equals("false"))
                            {
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("cnic",snapshot.child("cnic").getValue().toString());
                                editor.putString("phone",snapshot.child("phone").getValue().toString());
                                editor.commit();

                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LogIn.this, "You have already casted you vote", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(LogIn.this, "Incorrect Password or CNIC", Toast.LENGTH_SHORT).show();
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