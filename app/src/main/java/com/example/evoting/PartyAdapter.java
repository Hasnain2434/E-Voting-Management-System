package com.example.evoting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evoting.Admin.PartyManage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.ViewHolder>{

    ArrayList<PartyModel> model;
    Activity context;

    boolean adminFlag;

    public PartyAdapter(ArrayList<PartyModel> model,Activity context,boolean adminFlag) {
        this.model = model;
        this.context=context;
        this.adminFlag=adminFlag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.partylayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String party=model.get(position).getName();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminFlag==true)
                {
                    Intent intent=new Intent(context, PartyManage.class);
                    intent.putExtra("Party",party);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    context.finish();
                }
                else
                {
                    Intent intent=new Intent(context, CastVote.class);
                    intent.putExtra("Party",party);
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    context.finish();
                }
            }
        });
        holder.v1.setText(model.get(position).getName());
        if (adminFlag==true)
        {
            String votes=holder.v2.getText().toString()+" "+model.get(position).getVotes();
            holder.v2.setText(votes);
        }
        else
        {
            holder.v2.setText("");
        }
        DownloadImage downloadImage=new DownloadImage();
        try {
            Bitmap bitmap= downloadImage.execute(model.get(position).getUrl()).get();
            holder.img.setImageBitmap(bitmap);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView v1,v2;
        ImageView img;
        CardView cardView;
        public ViewHolder(@NonNull View view) {
            super(view);
            v1=view.findViewById(R.id.name);
            v2=view.findViewById(R.id.votenumber);
            img=view.findViewById(R.id.partyimage);
            cardView=view.findViewById(R.id.card);
        }
    }

    public class DownloadImage extends AsyncTask<String,Void, Bitmap>//1st parameter:input, 2nd parameter is progress bar show or not,3rd para is return type
    {

        @Override
        protected Bitmap doInBackground(String... strings) {//... shows there can be multiple strings(array of strings)
            try {
                URL url=new URL(strings[0]);
                HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();//because the file will come in the stream of bits/bytes
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);//it decodes the byte stream into bitmap

                Log.d("TAG","do in background in progress");

//                publishProgress(null);
                return bitmap;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
