package com.crubio.twitterclient.fragment;

import android.util.Log;

import com.crubio.twitterclient.application.RestApplication;
import com.crubio.twitterclient.clients.TwitterClient;
import com.crubio.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class TimelineList extends GenericTweetsList{

    private static final String CLASS = TimelineList.class.getSimpleName();

    protected void fetchTweets(int page){
        if(isNetworkAvailable()) {
            TwitterClient client = RestApplication.getRestClient();
            client.getHomeTimeline(page, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
//                    Log.i(CLASS, "timeline: " + jsonArray.toString());
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
