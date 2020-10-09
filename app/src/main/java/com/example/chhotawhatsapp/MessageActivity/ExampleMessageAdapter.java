package com.example.chhotawhatsapp.MessageActivity;

import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chhotawhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ExampleMessageAdapter extends RecyclerView.Adapter<ExampleMessageAdapter.ExampleViewHolder> {
    private static final int SENDER = 0;
    private static final int RECEIVER = 1;

    private  ArrayList<ExampleMessageItem> mMessageExampleList;
    private OnItemClickListener mListener;

    private FirebaseAuth mAuth;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView tvMessage;
        public TextView tvTime;
        public LinearLayout layoutMessage;
        public LinearLayout outerLayoutMessage;

        public ExampleViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.msg_item_message);
            tvTime = itemView.findViewById(R.id.msg_item_time);
            layoutMessage = itemView.findViewById(R.id.msg_item_layout);
            //Log.d("TAG","adp:tag:"+tvMessage.getTag()+","+layoutMessage.getTag());   Tag to know layout

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ExampleMessageAdapter(ArrayList<ExampleMessageItem> exampleMessageList){
        mAuth = FirebaseAuth.getInstance();
        mMessageExampleList = exampleMessageList;
    }

    @Override
    public int getItemViewType(int position) {
        ExampleMessageItem currentItem = mMessageExampleList.get(position);
        String currentUserId = mAuth.getCurrentUser().getUid();

        if(!currentUserId.equals(currentItem.getSender())){ return RECEIVER;  }
        return SENDER;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==SENDER){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_message_item, parent,false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_message_item_receive, parent,false);
        }
        ExampleViewHolder evh = new ExampleViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleMessageItem currentItem = mMessageExampleList.get(position);

        holder.tvMessage.setText(currentItem.getMessage());
        holder.tvTime.setText(currentItem.getTime());
    }

    @Override
    public int getItemCount() {
        return mMessageExampleList.size();
    }
}



