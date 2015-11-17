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
import com.crubio.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetsHolder>{

    private List<Tweet> tweets;
    private FragmentActivity context;

    public TweetsAdapter(List<Tweet> tweets, FragmentActivity context) {
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
//        Log.i("Tweet", tweet.toString());
        final String timestamp = tweet.getTimestamp();
        holder.timestamp.setText(replaceBySuffix(timestamp));

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyTweetDialog(tweet.getUser());
            }
        });



        if(!tweet.isRetweet()) {
            holder.retweetMsg.setVisibility(View.GONE);
            holder.retweetIcon.setVisibility(View.GONE);
            holder.tweet.setText(Html.fromHtml(tweet.getTweet()));
            holder.user.setText("@" + tweet.getUser());
            holder.userName.setText(tweet.getUserName());
            holder.userProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProfile(tweet.getUserId());
                }
            });
            Picasso.with(context).load(tweet.getUserProfileImage()).into(holder.userProfileImage);
        }else{
            holder.retweetMsg.setVisibility(View.VISIBLE);
            holder.retweetMsg.setText(tweet.getUserName() + " Retweeted");
            holder.retweetIcon.setVisibility(View.VISIBLE);
            String user = "@" + tweet.getRetweet().getUser();
            String tweetText = tweet.getRetweet().getTweet();
            tweetText = tweetText.replace("RT " + user + ": ", "");
            holder.tweet.setText(Html.fromHtml(tweetText));
            holder.user.setText(user);
            holder.userName.setText(tweet.getRetweet().getUserName());
            holder.userProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProfile(tweet.getRetweet().getUserId());
                }
            });
            Picasso.with(context).load(tweet.getRetweet().getUserProfileImage()).into(holder.userProfileImage);
        }
        if(tweet.getRetweets() != null && !tweet.getRetweets().equals("0")){
            holder.retweets.setText(tweet.getRetweets());
        }else{
            holder.retweets.setText(null);
        }
        if(tweet.getFavourites() != null && !tweet.getFavourites().equals("0")){
            holder.favourites.setText(tweet.getFavourites());
        }else{
            holder.favourites.setText(null);
        }
    }

    private void replyTweetDialog(String user) {
        if(isNetworkAvailable()) {
            FragmentManager fm = context.getSupportFragmentManager();
            WriteTweet writeTweet = WriteTweet.newInstance("@"+user);
            writeTweet.show(fm, "fragment_edit_name");
        }else{
            Toast.makeText(context, "Network is not available", Toast.LENGTH_LONG).show();
        }
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private String replaceBySuffix(String timestamp){
        String relativeDate = replace(timestamp, context.getString(R.string.day), context.getString(R.string.daySuffix));
        relativeDate = replace(relativeDate, context.getString(R.string.hour), context.getString(R.string.hourSuffix));
        relativeDate = replace(relativeDate, context.getString(R.string.minute), context.getString(R.string.minuteSuffix));
        return replace(relativeDate, context.getString(R.string.second), context.getString(R.string.secondSuffix));
    }

    private String replace(String timestamp, String target, String replacement){
        String relativeDate = timestamp;
        if( timestamp.contains(target)){
            relativeDate = relativeDate.replace(" "+target, replacement);
        } else if (timestamp.contains(target.substring(0, target.length()-1))){
            relativeDate = relativeDate.replace(" "+target.substring(0, target.length()-1), replacement);
        }
        return relativeDate;
    }

    protected void openProfile(String userId) {
        Intent i = new Intent(context , ProfileActivity.class);
        i.putExtra(ProfileActivity.ID, userId);
        context.startActivity(i);
    }

    public static class TweetsHolder extends RecyclerView.ViewHolder {
        public TextView tweet;
        public ImageView userProfileImage;
        public ImageView reply;
        public TextView user;
        public TextView userName;
        public TextView retweets;
        public TextView favourites;
        public ImageView retweetIcon;
        public TextView retweetMsg;


        public TextView timestamp;
        private TweetsHolder(View itemView) {
            super(itemView);
            tweet = (TextView) itemView.findViewById(R.id.tv_tweet);
            userProfileImage = (ImageView) itemView.findViewById(R.id.iv_user);
            user = (TextView) itemView.findViewById(R.id.tv_user);
            userName = (TextView) itemView.findViewById(R.id.tv_username);
            timestamp = (TextView) itemView.findViewById(R.id.tv_timestamp);
            retweets = (TextView) itemView.findViewById(R.id.tv_retweet);
            favourites = (TextView) itemView.findViewById(R.id.tv_like);
            retweetIcon = (ImageView) itemView.findViewById(R.id.iv_retweeticon);
            retweetMsg = (TextView) itemView.findViewById(R.id.tv_retweetmsg);
            reply = (ImageView) itemView.findViewById(R.id.iv_reply);
        }

    }
}