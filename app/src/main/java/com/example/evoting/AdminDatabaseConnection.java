package com.example.evoting;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminDatabaseConnection {
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;
    public static AdminDatabaseConnection databaseConnection=null;
    private AdminDatabaseConnection(){}

    public static AdminDatabaseConnection createConnection()
    {
        if(databaseConnection==null)
        {
            firebaseDatabase = FirebaseDatabase.getInstance("https://evoting-4f0ab-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("Citizen");
            databaseConnection=new AdminDatabaseConnection();
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
