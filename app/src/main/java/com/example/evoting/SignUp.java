package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    CitizenDatabaseConnection connection;
    DatabaseReference reference;
    EditText email;
    EditText cnic;
    EditText password;
    EditText age;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email=findViewById(R.id.emailLogIn1);
        cnic =findViewById(R.id.cnicSignUp1);
        password=findViewById(R.id.passwordLogIn1);
        age=findViewById(R.id.ageSignUp1);
        connection=CitizenDatabaseConnection.createConnection();
        reference=connection.getDatabaseReference();
    }

    public void goToLogInPage(View view) {
        Intent intent=new Intent(this, LogIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }


    public void CitizenSignUp(View view) {
        Intent intent=new Intent(this, LogIn.class);
        String cnicPattern = "^\\d{5}-\\d{7}-\\d$";
        UserModel userModel=new UserModel(email.getText().toString(),password.getText().toString(),Integer.parseInt(age.getText().toString()),cnic.getText().toString(),"");
        if(Integer.parseInt(age.getText().toString())<18)
        {
            Toast.makeText(this, "You are not eligible for vote", Toast.LENGTH_SHORT).show();
        }
        else if(!cnic.getText().toString().matches(cnicPattern))
        {
            Toast.makeText(this, "Cnic not valid", Toast.LENGTH_SHORT).show();
        }
        else
        {
            reference.child(cnic.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        Toast.makeText(SignUp.this, "Citizen against this cnic already registered", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        reference.child(cnic.getText().toString()).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SignUp.this, "SuccessFully Registered", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUp.this, "Registration failed try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}