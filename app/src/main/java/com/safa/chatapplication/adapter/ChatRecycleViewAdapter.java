package com.safa.chatapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safa.chatapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRecycleViewAdapter extends RecyclerView.Adapter<ChatRecycleViewAdapter.RecyclerViewHolder> {

    private List<String> chatMessageList;

    public ChatRecycleViewAdapter(ArrayList<String> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_recycleview_row, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        System.out.println("test "+position);
        holder.chatMessageText.setText(chatMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView chatMessageText;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            chatMessageText = itemView.findViewById(R.id.chat_text_row);

        }
    }
}
