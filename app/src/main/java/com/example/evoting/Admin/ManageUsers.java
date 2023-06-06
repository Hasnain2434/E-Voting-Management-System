package com.example.evoting.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.evoting.CitizenDatabaseConnection;
import com.example.evoting.PartyAdapter;
import com.example.evoting.PartyModel;
import com.example.evoting.R;
import com.example.evoting.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageUsers extends AppCompatActivity {
    ArrayList<UserModel> users;
    UserAdapter userAdapter;
    RecyclerView recyclerView;
    DatabaseReference reference=CitizenDatabaseConnection.createConnection().getDatabaseReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        recyclerView=findViewById(R.id.userView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        users=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    String email=dataSnapshot.child("phone").getValue().toString();
                    String age=dataSnapshot.child("age").getValue().toString();
                    String cnic=dataSnapshot.child("cnic").getValue().toString();
                    String password=dataSnapshot.child("password").getValue().toString();
                    UserModel userModel=new UserModel(email,password,Integer.parseInt(age),cnic,"");
                    users.add(userModel);
                }
                setDataIntoRecyclerView(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setDataIntoRecyclerView(ArrayList<UserModel> arrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userAdapter = new UserAdapter(arrayList,this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    public void goBackToAdminPanel(View view) {
        Intent intent=new Intent(this,AdminPanel.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void deleteAllUser(View view) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        reference.child(snapshot1.child("cnic").getValue().toString()).removeValue();
                    }
                    Toast.makeText(ManageUsers.this, "Citizens deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ManageUsers.this, "No Citizens found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageUsers.this, "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        });
        goBackToAdminPanel(view);
    }
}