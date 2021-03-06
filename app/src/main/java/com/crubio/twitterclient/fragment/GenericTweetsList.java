package com.crubio.twitterclient.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.adapters.TweetsAdapter;
import com.crubio.twitterclient.application.RestApplication;
import com.crubio.twitterclient.clients.TwitterClient;
import com.crubio.twitterclient.listeners.EndlessRecyclerOnScrollListener;
import com.crubio.twitterclient.models.Tweet;
import com.crubio.twitterclient.util.ItemClickSupport;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericTweetsList extends Fragment{
    private static final String CLASS = GenericTweetsList.class.getSimpleName();
    protected SwipeRefreshLayout swipeContainer;
    protected List<Tweet> tweets = new ArrayList<>();
    protected TweetsAdapter adapter;
    protected OnTweetDetailListener onTweetDetailListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.primaryColor);

        Log.i("onCreateView", "onCreateView");
        // Retrieving the RecyclerView from the fragment layout
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);

        // Setting a LinearLayoutManager as LayoutManager (Make it look like a ListView)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(linearLayoutManager);
        final HorizontalDividerItemDecoration divider =
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.LTGRAY)
                        .sizeResId(R.dimen.divider)
                        .build();
        rv.addItemDecoration(divider);

        // Fetching new tweets
        refresh();

        adapter = new TweetsAdapter(tweets, getActivity());
        rv.setAdapter(adapter);
        rv.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                fetchTweets(current_page);
            }
        });


        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                onTweetDetailListener.onTweetDetail(tweets.get(position));
                Log.i("onCreateView", "setOnItemClickListener");
            }
        });

        return rootView;
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    public void refresh() {
        tweets.clear();
        if(isNetworkAvailable()){
            onTweetDetailListener.onStartRefresh();
            fetchTweets(1);
        }else{
            tweets.addAll(Tweet.getAll());
            Toast.makeText(getActivity(), "Unable to connect to internet. Loading lastest tweets.", Toast.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
        }
    }

    protected abstract void fetchTweets(int pages);

    public void setOnTweetDetailListener(OnTweetDetailListener onTweetDetailListener) {
        this.onTweetDetailListener = onTweetDetailListener;
    }

    public interface OnTweetDetailListener{
        void onTweetDetail(Tweet tweet);
        void onStartRefresh();
        void onStopRefresh();
    }


}
