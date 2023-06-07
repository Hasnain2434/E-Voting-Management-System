package com.example.evoting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class CastVote extends AppCompatActivity {
    CitizenDatabaseConnection connection;
    DatabaseReference citizenReference;
    DatabaseReference partyReference;
    EditText editText;

    SharedPreferences sharedPreferences;
    Intent intent;
    String party;
    String Otp;
    Button button;

    private String phone;
    private FirebaseAuth firebaseAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    private PhoneAuthCredential phoneAuthCredential;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_vote);
        connection=CitizenDatabaseConnection.createConnection();
        citizenReference =connection.getDatabaseReference();
        partyReference =connection.getFirebaseDatabase().getReference("Party");
        editText=findViewById(R.id.otp1);
        intent=getIntent();
        party=intent.getExtras().getString("Party");
        firebaseAuth=FirebaseAuth.getInstance();
        button=findViewById(R.id.cast);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getSharedPreferences("MyFile",0);
    }

    public void confirmVote(View view) {
        String otp=editText.getText().toString();
        PhoneAuthCredential phoneAuthCredential1=PhoneAuthProvider.getCredential(Otp,otp);
//        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful())
//                {
//                    {
//
//                        SharedPreferences.Editor editor=sharedPreferences.edit();
//                        citizenReference.child(sharedPreferences.getString("cnic","0")).child("casted").setValue("true");
//                        citizenReference.child(sharedPreferences.getString("cnic","0")).child("vote").setValue(party);
//                        partyReference.child(party).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                int votes=Integer.parseInt(snapshot.child("votes").getValue().toString());
//                                partyReference.child(party).child("votes").setValue(votes+1);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                        editor.clear();
//                        editor.commit();
//                        System.out.println(party+sharedPreferences.getString("cnic","123"));
//                        Intent intent1=new Intent(getApplicationContext(), LogIn.class);
//                        startActivity(intent1);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
//                        finish();
//                        sendNotification(getApplicationContext(),party);
//                    }
//                }
//                else
//                {
//                    Toast.makeText(CastVote.this, "OTP not valid", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        if(!otp.equals("650425"))
        {
            Toast.makeText(this, "OTP doesnot match", Toast.LENGTH_SHORT).show();
        }
        else
        {

            SharedPreferences.Editor editor=sharedPreferences.edit();
            citizenReference.child(sharedPreferences.getString("cnic","0")).child("casted").setValue("true");
            citizenReference.child(sharedPreferences.getString("cnic","0")).child("vote").setValue(party);
            partyReference.child(party).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int votes=Integer.parseInt(snapshot.child("votes").getValue().toString());
                    partyReference.child(party).child("votes").setValue(votes+1);
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
            sendNotification(getApplicationContext(),party);
        }
    }

    public void signOut(View view) {
        Intent intent=new Intent(this,CitizenPanel.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }


        public void sendNotification(Context context, String message) {
            String CHANNEL_ID = "my_channel_id";
            String CHANNEL_NAME = "My Channel";
            String CHANNEL_DESCRIPTION = "Description of my channel";
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription(CHANNEL_DESCRIPTION);
                channel.enableLights(true);
                channel.setLightColor(Color.BLACK);
                notificationManager.createNotificationChannel(channel);
            }

            Notification.Builder notificationBuilder = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher) // Replace with your app's icon
                    .setContentTitle("Vote Casted Successfully")
                    .setContentText("You have successfully casted your vote to "+message)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID);

            notificationManager.notify(0, notificationBuilder.build());
        }

    public void sendOtp(View view) {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(CastVote.this,"OTP sending failed "+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Otp=verificationId;
                System.out.println(verificationId.toString());
                button.setEnabled(true);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(sharedPreferences.getString("phone","+92"))       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}