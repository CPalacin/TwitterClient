package com.crubio.twitterclient.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.crubio.twitterclient.adapters.TweetsAdapter;
import com.crubio.twitterclient.application.RestApplication;
import com.crubio.twitterclient.clients.TwitterClient;
import com.crubio.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineList extends Fragment {
    private static final String CLASS = TimelineList.class.getSimpleName();
    private SwipeRefreshLayout swipeContainer;
    private List<Tweet> tweets = new ArrayList<>();
    private TweetsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTweets();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.primaryColor);


        // Retrieving the RecyclerView from the fragment layout
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);

        // Setting a LinearLayoutManager as LayoutManager (Make it look like a ListView)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(linearLayoutManager);

        // Fetching new photos
        if(tweets == null || tweets.isEmpty()) {
            fetchTweets();
        }

        adapter = new TweetsAdapter(tweets, getActivity());
        rv.setAdapter(adapter);

        return rootView;
    }

    private void fetchTweets(){
        TwitterClient client = RestApplication.getRestClient();
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.i(CLASS, "timeline: " + jsonArray.toString());
                // Load json array into model classes
                tweets.addAll(Tweet.fromJson(jsonArray));
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                Log.e(CLASS, "timeline error: " + jsonObject.toString());
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
