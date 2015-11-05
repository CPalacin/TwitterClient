package com.crubio.twitterclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.crubio.twitterclient.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetsHolder>{

    private List<Tweet> tweets;
    private Context context;

    public TweetsAdapter(List<Tweet> tweets, Context context) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public TweetsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        return new TweetsHolder(v);
    }

    @Override
    public void onBindViewHolder(TweetsHolder holder, int position) {
        final Tweet tweet = tweets.get(position);
        holder.tweet.setText(Html.fromHtml(tweet.getBody()));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class TweetsHolder extends RecyclerView.ViewHolder {
        public TextView tweet;

        private TweetsHolder(View itemView) {
            super(itemView);
            tweet = (TextView)itemView.findViewById(R.id.tv_tweet);
        }
    }

}