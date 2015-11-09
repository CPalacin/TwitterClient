package com.crubio.twitterclient.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.fragment.Detail;
import com.crubio.twitterclient.fragment.TimelineList;
import com.crubio.twitterclient.fragment.WriteTweet;
import com.crubio.twitterclient.models.Tweet;

public class TimelineActivity extends AppCompatActivity implements TimelineList.OnTweetDetailListener, Detail.OnReplyListener {
    private static final String CLASS = TimelineActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TimelineList timelineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        toolbarCreation();
        switchToTimelineList();
    }


    private void switchToTimelineList() {
        timelineList = new TimelineList();
        timelineList.setOnTweetDetailListener(this);
        switchFragment(timelineList);
    }

    private void switchToDetail(Tweet tweet) {
        setHomeButtonVisibility(true);
        Detail detail = Detail.newInstance(tweet);
        detail.setOnReplyListener(this);
        switchFragment(detail);
    }

    private void switchFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.contentFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void toolbarCreation() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    private void showWriteTweetDialog() {
        if(isNetworkAvailable()) {
            FragmentManager fm = getSupportFragmentManager();
            WriteTweet writeTweet = new WriteTweet() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    super.onDismiss(dialog);
                    timelineList.refresh();
                }
            };
            writeTweet.show(fm, "fragment_edit_name");
        }else{
            Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_twitter:
                showWriteTweetDialog();
                return true;
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                setHomeButtonVisibility(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setHomeButtonVisibility(boolean visibility) {
        getSupportActionBar().setHomeButtonEnabled(visibility);
        getSupportActionBar().setDisplayHomeAsUpEnabled(visibility);
    }

    @Override
    public void onTweetDetail(Tweet tweet) {
        switchToDetail(tweet);
    }

    @Override
    public void onReply(String user) {
        replyTweetDialog(user);
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
}
