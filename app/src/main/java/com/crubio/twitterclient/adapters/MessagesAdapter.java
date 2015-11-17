package com.crubio.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.activities.ProfileActivity;
import com.crubio.twitterclient.fragment.WriteTweet;
import com.crubio.twitterclient.models.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesHolder>{

    private List<Message> messages;
    private FragmentActivity context;

    public MessagesAdapter(List<Message> messages, FragmentActivity context) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public MessagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessagesHolder(v);
    }

    @Override
    public void onBindViewHolder(MessagesHolder holder, int position) {
        final Message message = messages.get(position);
        holder.message.setText(message.getMessage());
        holder.userName.setText(message.getUsername());
        Picasso.with(context).load(message.getUserImageUrl()).into(holder.userProfileImage);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MessagesHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public ImageView userProfileImage;
        public TextView userName;

        private MessagesHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.tv_message);
            userProfileImage = (ImageView) itemView.findViewById(R.id.iv_user);
            userName = (TextView) itemView.findViewById(R.id.tv_username);
        }

    }
}