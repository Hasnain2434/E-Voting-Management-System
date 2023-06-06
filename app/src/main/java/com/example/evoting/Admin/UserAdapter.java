package com.example.evoting.Admin;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evoting.R;
import com.example.evoting.UserModel;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    ArrayList<UserModel> users;
    Activity activity;

    public UserAdapter(ArrayList<UserModel> users, Activity activity) {
        this.users = users;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String cnic=users.get(position).getCnic();
        System.out.println(cnic);
        String email=users.get(position).getPhone();
        int age=users.get(position).getAge();
        String password=users.get(position).getPassword();
        holder.v1.setText(email);
        holder.v2.setText(cnic);
        holder.v3.setText(String.valueOf(age));
        holder.v4.setText(password);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,UserManager.class);
                intent.putExtra("cnic",cnic);
                intent.putExtra("email",email);
                intent.putExtra("age",String.valueOf(age));
                intent.putExtra("password",password);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView v1,v2,v3,v4;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View view) {
            super(view);
            v1=view.findViewById(R.id.email);
            v2=view.findViewById(R.id.cnic);
            v3=view.findViewById(R.id.age);
            v4=view.findViewById(R.id.password);
            linearLayout=view.findViewById(R.id.externalview);
        }
    }
}
