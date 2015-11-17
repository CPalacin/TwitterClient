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
import com.crubio.twitterclient.adapters.MessagesAdapter;
import com.crubio.twitterclient.application.RestApplication;
import com.crubio.twitterclient.clients.TwitterClient;
import com.crubio.twitterclient.listeners.EndlessRecyclerOnScrollListener;
import com.crubio.twitterclient.models.Message;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DirectMessages extends Fragment {
    private static final String CLASS = DirectMessages.class.getSimpleName();
    protected SwipeRefreshLayout swipeContainer;
    protected List<Message> messages = new ArrayList<>();
    protected MessagesAdapter adapter;
    protected GenericTweetsList.OnTweetDetailListener onTweetDetailListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

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

        adapter = new MessagesAdapter(messages, getActivity());
        rv.setAdapter(adapter);
        rv.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                fetchMessages(current_page);
            }
        });

        return rootView;
    }

    private void refresh() {
        messages.clear();
        if(isNetworkAvailable()){
            onTweetDetailListener.onStartRefresh();
            fetchMessages(1);
        }else{
            Toast.makeText(getActivity(), "Unable to connect to internet.", Toast.LENGTH_LONG).show();
            swipeContainer.setRefreshing(false);
        }
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    protected void fetchMessages(int page) {
        if(isNetworkAvailable()) {
            TwitterClient client = RestApplication.getRestClient();
            client.getMessages(page, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                    Log.i(CLASS, "timeline: " + jsonArray.toString());
                    messages.addAll(Message.fromJson(jsonArray));
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

    public void setOnTweetDetailListener(GenericTweetsList.OnTweetDetailListener onTweetDetailListener) {
        this.onTweetDetailListener = onTweetDetailListener;
    }
}
