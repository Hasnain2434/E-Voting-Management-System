package com.example.evoting.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evoting.CitizenDatabaseConnection;
import com.example.evoting.R;
import com.example.evoting.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserManager extends AppCompatActivity {
    Intent intent;
    DatabaseReference reference= CitizenDatabaseConnection.createConnection().getDatabaseReference();
    EditText t1,t2,t3,t4;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        t1=findViewById(R.id.Email1);
        t2=findViewById(R.id.CNIC1);
        t3=findViewById(R.id.Age1);
        t4=findViewById(R.id.Password1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        intent=getIntent();
        System.out.println(intent.getExtras().getString("cnic"));
        t1.setText(intent.getExtras().getString("email"));
        t2.setText(intent.getExtras().getString("cnic"));
        t3.setText(intent.getExtras().getString("age"));
        t4.setText(intent.getExtras().getString("password"));
    }

    public void goBackToManageUser(View view) {
        Intent intent1=new Intent(this,ManageUsers.class);
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    public void updateUser(View view) {
        String cnic=t2.getText().toString();
        String email=t1.getText().toString();
        String age=t3.getText().toString();
        String password=t4.getText().toString();
        String phoneNumberPattern = "^(\\+92|0)(3[0-9]{2}|5[0-9]{2}|6[0-9]{2}|7[0-9]{2})[0-9]{7}$";
        if (email.length()==0||cnic.length()==0||age.length()==0||password.length()==0)
        {
            Toast.makeText(this, "Fill the required fields", Toast.LENGTH_SHORT).show();
        }
        else if(!email.toString().matches(phoneNumberPattern))
        {
            Toast.makeText(this, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
        }
        else
        {
            reference.child(cnic).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String voteParty=snapshot.child("vote").getValue().toString();
                    String casted =snapshot.child("casted").getValue().toString();
                    UserModel userModel=new UserModel(email,password,Integer.parseInt(age),cnic,voteParty,casted);
                    reference.child(cnic).removeValue();
                    reference.child(cnic).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UserManager.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                            goBackToManageUser(view);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserManager.this, "Updation Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    public void deleteUser(View view) {
        String cnic=t2.getText().toString();
        reference.child(cnic).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UserManager.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                goBackToManageUser(view);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserManager.this, "Deletion Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}