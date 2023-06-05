package com.example.evoting.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.evoting.AdminDatabaseConnection;
import com.example.evoting.AdminLogIn;
import com.example.evoting.PartyAdapter;
import com.example.evoting.PartyModel;
import com.example.evoting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {
    AdminDatabaseConnection connection;
    DatabaseReference reference;

    RecyclerView recyclerView;

    PartyAdapter partyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        connection=AdminDatabaseConnection.createConnection();
        reference=connection.getFirebaseDatabase().getReference("Party");
        recyclerView=findViewById(R.id.recview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<PartyModel> arrayList=new ArrayList<>();
//        reference= FirebaseDatabase.getInstance("https://evoting-d0a2f-default-rtdb.firebaseio.com/").getReference("Party");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    PartyModel partyModel=new PartyModel(dataSnapshot.child("name").getValue().toString(),Integer.parseInt(dataSnapshot.child("votes").getValue().toString()),dataSnapshot.child("url").getValue().toString());
                    arrayList.add(partyModel);
                }
                setDataIntoRecyclerView(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setDataIntoRecyclerView(ArrayList<PartyModel> arrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        partyAdapter = new PartyAdapter(arrayList,this,true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(partyAdapter);
        partyAdapter.notifyDataSetChanged();
    }

    public void logOut(View view) {
        Intent intent=new Intent(this, AdminLogIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void goToAddParty(View view) {
        Intent intent=new Intent(this,AddParty.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }
}