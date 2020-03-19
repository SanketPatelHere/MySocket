package com.example.mysocket;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<ChatMessagePojo> lst;
    public Context context;
    public MyAdapter()
    {
    }
    public MyAdapter(ArrayList<ChatMessagePojo> mylstSender, Context context)
    {
        lst = mylstSender;
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return lst.size();
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_message, parent, false);
        return new MyViewHolder(listItem);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message_user, message_time, message_text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message_user = (TextView)itemView.findViewById(R.id.message_user);
            message_time = (TextView)itemView.findViewById(R.id.message_time);
            message_text = (TextView)itemView.findViewById(R.id.message_text);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        ChatMessagePojo dp = lst.get(position);
        holder.message_user.setText(dp.getSender().getName());
        holder.message_time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                dp.getMessageTime()));
        holder.message_text.setText(dp.getMessage());


        if(lst.get(position).getSender().getId().equalsIgnoreCase("11111111111111111111"))
        {
            //me-sender = right side = not change
            Log.i("My inside = ","My Sender side - Right");
            //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.message_text.getLayoutParams();
            //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        else
        {
            //receiver = left side
            //message_text
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.message_text.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            Log.i("My inside = ","Recevier side - Left");
        }

    }




}
