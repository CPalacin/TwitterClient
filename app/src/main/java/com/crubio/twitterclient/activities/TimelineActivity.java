package com.crubio.twitterclient.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.crubio.twitterclient.R;
import com.crubio.twitterclient.adapters.TwitterPagerAdapter;
import com.crubio.twitterclient.fragment.Detail;
import com.crubio.twitterclient.fragment.GenericTweetsList;
import com.crubio.twitterclient.fragment.WriteTweet;
import com.crubio.twitterclient.models.Tweet;

public class TimelineActivity extends BaseActivity implements Detail.OnReplyListener {
    private static final String CLASS = TimelineActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        toolbarCreation();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TwitterPagerAdapter(getSupportFragmentManager(), this));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
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

    @Override
    protected void refresh() {
        //TODO
    }
}
