package com.example.evoting.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evoting.AdminDatabaseConnection;
import com.example.evoting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PartyManage extends AppCompatActivity {
    String party;
    Intent intent;
    DatabaseReference reference;
    EditText t1,t2,t3;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_manage);
        reference= AdminDatabaseConnection.createConnection().getFirebaseDatabase().getReference("Party");
        t1=findViewById(R.id.Email1);
        t2=findViewById(R.id.CNIC1);
        t3=findViewById(R.id.Age1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        intent=getIntent();
        party=intent.getExtras().getString("Party");
        reference.child(party).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t1.setText(snapshot.child("name").getValue().toString());
                t2.setText(snapshot.child("url").getValue().toString());
                t3.setText(snapshot.child("votes").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goBack(View view) {
        Intent intent=new Intent(this,AdminPanel.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }



    public void deleteParty(View view) {
        Intent intent=new Intent(this,AdminPanel.class);
        reference.child(party).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PartyManage.this, "Party Deleted Successfully", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PartyManage.this, "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateParty(View view) {
        Intent intent=new Intent(this,AdminPanel.class);
        String url=t2.getText().toString();
       if(url.length()!=0)
       {
           reference.child(party).child("url").setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                   Toast.makeText(PartyManage.this, "Party Updated SuccessFully", Toast.LENGTH_SHORT).show();
                   startActivity(intent);
                   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                   finish();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(PartyManage.this, "Updation Failed", Toast.LENGTH_SHORT).show();
               }
           });
       }
       else
       {
           Toast.makeText(this, "Url cannot be empty", Toast.LENGTH_SHORT).show();
       }
    }
}