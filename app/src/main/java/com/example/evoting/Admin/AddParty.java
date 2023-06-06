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
import com.example.evoting.PartyModel;
import com.example.evoting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddParty extends AppCompatActivity {
    EditText t1,t2;
    DatabaseReference reference= AdminDatabaseConnection.createConnection().getFirebaseDatabase().getReference("Party");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_party);
        t1=findViewById(R.id.Email1);
        t2=findViewById(R.id.CNIC1);
    }

    public void goBack(View view) {
        Intent intent=new Intent(this,AdminPanel.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void addParty(View view) {
        String name=t1.getText().toString();
        String url=t2.getText().toString();
        if(name.length()==0 || url.length()==0)
        {
            Toast.makeText(this, "Name or URL missing", Toast.LENGTH_SHORT).show();
        }
        else
        {
            reference.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        Toast.makeText(AddParty.this, "Party against this name already exists", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        PartyModel model=new PartyModel(name,0,url);
                        reference.child(name).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddParty.this, "Party Added Successfully", Toast.LENGTH_SHORT).show();
                                goBack(view);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddParty.this, "Error:Party cannot be added at that moment try after some time", Toast.LENGTH_SHORT).show();
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