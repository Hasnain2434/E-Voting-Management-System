package com.example.evoting;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CitizenDatabaseConnection {
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;
    public static CitizenDatabaseConnection databaseConnection=null;
    private CitizenDatabaseConnection(){}

    public static CitizenDatabaseConnection createConnection()
    {
        if(databaseConnection==null)
        {
            firebaseDatabase = FirebaseDatabase.getInstance("https://evoting-d0a2f-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("Citizen");
            databaseConnection=new CitizenDatabaseConnection();
        }
        return databaseConnection;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public FirebaseDatabase getFirebaseDatabase()
    {
        return firebaseDatabase;
    }
}
