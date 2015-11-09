package com.crubio.twitterclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

public class Detail extends Fragment {
    private static final String CLASS = Detail.class.getSimpleName();
    public static final String TWEET = "Tweet";

    private Tweet tweet;
    private OnReplyListener onReplyListener;


    public static Detail newInstance(Tweet tweet) {
        Detail myFragment = new Detail();

        Bundle args = new Bundle();
        args.putParcelable(TWEET, tweet);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        tweet = getArguments().getParcelable(TWEET);

        TextView tweetText = (TextView) rootView.findViewById(R.id.tv_tweet);
        ImageView userProfileImage = (ImageView) rootView.findViewById(R.id.iv_user);
        TextView user = (TextView) rootView.findViewById(R.id.tv_user);
        TextView userName = (TextView) rootView.findViewById(R.id.tv_username);
        TextView timestamp = (TextView) rootView.findViewById(R.id.tv_timestamp);
        TextView retweets = (TextView) rootView.findViewById(R.id.tv_retweet);
        TextView favourites = (TextView) rootView.findViewById(R.id.tv_like);

        ImageView reply = (ImageView) rootView.findViewById(R.id.iv_reply);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReplyListener.onReply(tweet.getUser());
            }
        });

        tweetText.setText(Html.fromHtml(tweet.getTweet()));
        final String time = tweet.getTimestamp();
        timestamp.setText(replaceBySuffix(time));
        Picasso.with(getActivity()).load(tweet.getUserProfileImage()).into(userProfileImage);
        user.setText("@" + tweet.getUser());
        userName.setText(tweet.getUserName());
        if(tweet.getRetweets() != null && !tweet.getRetweets().equals("0")){
            retweets.setText(tweet.getRetweets());
        }else{
            retweets.setText(null);
        }
        if(tweet.getFavourites() != null && !tweet.getFavourites().equals("0")){
            favourites.setText(tweet.getFavourites());
        }else{
            favourites.setText(null);
        }

        return rootView;
    }

    private String replaceBySuffix(String timestamp){

        String relativeDate = replace(timestamp, getActivity().getString(R.string.day), getActivity().getString(R.string.daySuffix));
        relativeDate = replace(relativeDate, getActivity().getString(R.string.hour), getActivity().getString(R.string.hourSuffix));
        relativeDate = replace(relativeDate, getActivity().getString(R.string.minute), getActivity().getString(R.string.minuteSuffix));
        return replace(relativeDate, getActivity().getString(R.string.second), getActivity().getString(R.string.secondSuffix));
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

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    public interface OnReplyListener{
        void onReply(String user);
    }
}
