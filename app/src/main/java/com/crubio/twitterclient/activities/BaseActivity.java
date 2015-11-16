package com.crubio.twitterclient.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.application.RestApplication;
import com.crubio.twitterclient.clients.TwitterClient;
import com.crubio.twitterclient.fragment.GenericTweetsList;
import com.crubio.twitterclient.fragment.WriteTweet;
import com.crubio.twitterclient.models.Tweet;
import com.crubio.twitterclient.models.UserStatus;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public abstract class BaseActivity extends AppCompatActivity implements GenericTweetsList.OnTweetDetailListener{
    private static final String CLASS = BaseActivity.class.getSimpleName();
    protected Toolbar toolbar;
    private String userId;
    private MenuItem miActionProgressItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserId();
    }

    private void getUserId() {
        if(isNetworkAvailable() && userId == null) {
            TwitterClient client = RestApplication.getRestClient();
            client.getUserStatus(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                    UserStatus userStatus = new UserStatus(jsonObject);
                    userId = userStatus.getId();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                    Log.e(CLASS, "user id error: " + jsonObject);
                }
            });
        }
    }


    protected void toolbarCreation() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        v.setVisibility(View.VISIBLE);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);
        onStartRefresh();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_twitter:
                showWriteTweetDialog();
                return true;
            case android.R.id.home:
                finish();
                setHomeButtonVisibility(false);
                return true;
            case R.id.action_profile:
                openProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void openProfile() {
        Intent i = new Intent(this , ProfileActivity.class);
        i.putExtra(ProfileActivity.ID, userId);
        startActivity(i);
    }

    protected void showWriteTweetDialog() {
        if(isNetworkAvailable()) {
            FragmentManager fm = getSupportFragmentManager();
            WriteTweet writeTweet = new WriteTweet() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    super.onDismiss(dialog);
                    refresh();
                }
            };
            writeTweet.show(fm, "fragment_edit_name");
        }else{
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
        }
    }

    protected abstract void refresh();


    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void setHomeButtonVisibility(boolean visibility) {
        getSupportActionBar().setHomeButtonEnabled(visibility);
        getSupportActionBar().setDisplayHomeAsUpEnabled(visibility);
    }

    public void showProgressBar() {
        if(miActionProgressItem != null)
            miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        if(miActionProgressItem != null)
            miActionProgressItem.setVisible(false);
    }

    protected void switchToDetail(Tweet tweet) {
        Intent i = new Intent(this , DetailActivity.class);
        i.putExtra(DetailActivity.TWEET, tweet);
        startActivity(i);
    }

    @Override
    public void onTweetDetail(Tweet tweet) {
        switchToDetail(tweet);
    }

    @Override
    public void onStartRefresh() {
        showProgressBar();
    }

    @Override
    public void onStopRefresh() {
        hideProgressBar();
    }
}
