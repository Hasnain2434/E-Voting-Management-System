package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class CastVote extends AppCompatActivity {
    CitizenDatabaseConnection connection;
    DatabaseReference reference;
    DatabaseReference reference1;
    EditText editText;

    SharedPreferences sharedPreferences;
    Intent intent;
    String party;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_vote);
        connection=CitizenDatabaseConnection.createConnection();
        reference=connection.getDatabaseReference();
        reference1=connection.getFirebaseDatabase().getReference("Party");
        editText=findViewById(R.id.otp1);
        intent=getIntent();
        party=intent.getExtras().getString("Party");
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getSharedPreferences("MyFile",0);
    }

    public void confirmVote(View view) {
        String otp=editText.getText().toString();
        if(otp.length()==0)
        {
            Toast.makeText(this, "Enter the OTP", Toast.LENGTH_SHORT).show();
        }
        else
        {

            SharedPreferences.Editor editor=sharedPreferences.edit();
            reference.child(sharedPreferences.getString("cnic","0")).child("casted").setValue("true");
            reference.child(sharedPreferences.getString("cnic","0")).child("vote").setValue(party);
            reference1.child(party).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int votes=Integer.parseInt(snapshot.child("votes").getValue().toString());
                    reference1.child(party).child("votes").setValue(votes+1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            editor.clear();
            editor.commit();
            System.out.println(party+sharedPreferences.getString("cnic","123"));
            Intent intent1=new Intent(this, LogIn.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            finish();
        }
    }

    public void signOut(View view) {
        Intent intent=new Intent(this,CitizenPanel.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }
}