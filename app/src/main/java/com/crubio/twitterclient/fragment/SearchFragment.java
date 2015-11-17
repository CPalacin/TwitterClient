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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carlos on 11/16/2015.
 */
public class SearchFragment extends GenericTweetsList{

    private static final String QUERY = "QUERY";
    private static final String CLASS = SearchFragment.class.getSimpleName();
    private String query;

    public static SearchFragment newInstance(String query) {
        SearchFragment myFragment = new SearchFragment();

        Bundle args = new Bundle();
        args.putString(QUERY, query);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        query = getArguments().getString(QUERY);
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    @Override
    protected void fetchTweets(int page) {
        if(isNetworkAvailable()) {
            TwitterClient client = RestApplication.getRestClient();
            client.getSearch(query, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
//                    Log.i(CLASS, "timeline: " + jsonArray.toString());
                    try {
                        tweets.addAll(Tweet.fromJson(jsonObject.getJSONArray("statuses")));
                    } catch (JSONException e) {
                        Log.e(CLASS, "search error: " + jsonObject);
                    }
                    swipeContainer.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    onTweetDetailListener.onStopRefresh();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                    Log.e(CLASS, "search error: " + jsonObject);
                    swipeContainer.setRefreshing(false);
                    onTweetDetailListener.onStopRefresh();
                }
            });
        }
    }
}
