package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CitizenPanel extends AppCompatActivity {
    CitizenDatabaseConnection connection;
    DatabaseReference reference;
    RecyclerView recyclerView;
    PartyAdapter partyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_panel);
        recyclerView=findViewById(R.id.recview);
        connection=CitizenDatabaseConnection.createConnection();
        reference=connection.getFirebaseDatabase().getReference("Party");
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
        partyAdapter = new PartyAdapter(arrayList,this,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(partyAdapter);
        partyAdapter.notifyDataSetChanged();
    }

}