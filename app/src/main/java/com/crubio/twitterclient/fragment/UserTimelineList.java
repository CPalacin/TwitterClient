package com.crubio.twitterclient.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crubio.twitterclient.application.RestApplication;
import com.crubio.twitterclient.clients.TwitterClient;
import com.crubio.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserTimelineList extends GenericTweetsList{

    private static final String CLASS = UserTimelineList.class.getSimpleName();
    private static final String USER = "USER";
    private String user;

    public static UserTimelineList newInstance(String user) {
        UserTimelineList myFragment = new UserTimelineList();

        Bundle args = new Bundle();
        args.putString(USER, user);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = getArguments().getString(USER);
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    protected void fetchTweets(int page){
        Log.i(CLASS, ""+page);
        if(isNetworkAvailable()) {
            TwitterClient client = RestApplication.getRestClient();
            client.getUserTimeline(user, page, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                    Log.i(CLASS, "user timeline: " + Tweet.fromJson(jsonArray).get(0));
                    Log.i(CLASS, user);
                    tweets.addAll(Tweet.fromJson(jsonArray));
                    swipeContainer.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    onTweetDetailListener.onStopRefresh();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                    Log.e(CLASS, "timeline error: " + jsonObject);
                    swipeContainer.setRefreshing(false);
                    onTweetDetailListener.onStopRefresh();
                }
            });
        }
    }
}
