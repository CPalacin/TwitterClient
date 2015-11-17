package com.crubio.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.fragment.WriteTweet;
import com.crubio.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

public class DetailActivity extends BaseActivity{
    private static final String CLASS = DetailActivity.class.getSimpleName();
    public static final String TWEET = "Tweet";

    private Tweet tweet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbarCreation();
        Bundle b = getIntent().getExtras();
        tweet = b.getParcelable(TWEET);

        TextView tweetText = (TextView) findViewById(R.id.tv_tweet);
        ImageView userProfileImage = (ImageView) findViewById(R.id.iv_user);
        TextView user = (TextView) findViewById(R.id.tv_user);
        TextView userName = (TextView) findViewById(R.id.tv_username);
        TextView timestamp = (TextView) findViewById(R.id.tv_timestamp);
        TextView retweets = (TextView) findViewById(R.id.tv_retweet);
        TextView favourites = (TextView) findViewById(R.id.tv_like);
        ImageView media = (ImageView) findViewById(R.id.iv_media);

        Picasso.with(this).load(tweet.getMediaUrl()).into(media);
        ImageView reply = (ImageView) findViewById(R.id.iv_reply);
        Log.i(CLASS, "" + reply);

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyTweetDialog(tweet.getUser());
            }
        });

        TextView retweetMsg = (TextView) findViewById(R.id.tv_retweetmsg);
        ImageView retweetIcon = (ImageView) findViewById(R.id.iv_retweeticon);
        if(!tweet.isRetweet()) {
            retweetMsg.setVisibility(View.GONE);
            retweetIcon.setVisibility(View.GONE);
            tweetText.setText(Html.fromHtml(tweet.getTweet()));
            user.setText("@" + tweet.getUser());
            userName.setText(tweet.getUserName());
            Picasso.with(this).load(tweet.getUserProfileImage()).into(userProfileImage);
        }else{
            retweetMsg.setVisibility(View.VISIBLE);
            retweetMsg.setText(tweet.getUserName() + " Retweeted");
            retweetIcon.setVisibility(View.VISIBLE);
            String userTxt = "@" + tweet.getRetweet().getUser();
            String retweetText = tweet.getRetweet().getTweet();
            retweetText = retweetText.replace("RT " + userTxt + ": ", "");
            tweetText.setText(Html.fromHtml(retweetText));
            user.setText("@" + tweet.getRetweet().getUser());
            userName.setText(tweet.getRetweet().getUserName());
            Picasso.with(this).load(tweet.getRetweet().getUserProfileImage()).into(userProfileImage);
        }

        final String time = tweet.getTimestamp();
        timestamp.setText(replaceBySuffix(time));


        if (tweet.getRetweets() != null && !tweet.getRetweets().equals("0")){
            retweets.setText(tweet.getRetweets());
        }else{
            retweets.setText(null);
        }
        if(tweet.getFavourites() != null && !tweet.getFavourites().equals("0")){
            favourites.setText(tweet.getFavourites());
        }else{
            favourites.setText(null);
        }

        setHomeButtonVisibility(true);
    }

    private void replyTweetDialog(String user) {
        if(isNetworkAvailable()) {
            FragmentManager fm = getSupportFragmentManager();
            WriteTweet writeTweet = WriteTweet.newInstance("@"+user);
            writeTweet.show(fm, "fragment_edit_name");
        }else{
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
        }
    }

    private String replaceBySuffix(String timestamp){
        String relativeDate = replace(timestamp, getString(R.string.day), getString(R.string.daySuffix));
        relativeDate = replace(relativeDate, getString(R.string.hour), getString(R.string.hourSuffix));
        relativeDate = replace(relativeDate, getString(R.string.minute), getString(R.string.minuteSuffix));
        return replace(relativeDate, getString(R.string.second), getString(R.string.secondSuffix));
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

    @Override
    protected void refresh() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
