package com.crubio.twitterclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.crubio.twitterclient.fragment.DirectMessages;
import com.crubio.twitterclient.fragment.GenericTweetsList;
import com.crubio.twitterclient.fragment.MentionsList;
import com.crubio.twitterclient.fragment.TimelineList;

public class TwitterPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Home", "Mentions", "Messages" };
    private GenericTweetsList.OnTweetDetailListener onTweetDetailListener;
    private DirectMessages directMessages;

    public TwitterPagerAdapter(FragmentManager fm, GenericTweetsList.OnTweetDetailListener onTweetDetailListener) {
        super(fm);
        this.onTweetDetailListener = onTweetDetailListener;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        GenericTweetsList f = null;
        switch (position){
            case 0:
                f = new TimelineList();
                f.setOnTweetDetailListener(onTweetDetailListener);
                return f;
            case 1:
                f = new MentionsList();
                f.setOnTweetDetailListener(onTweetDetailListener);
                return f;
            case 2:
                directMessages = new DirectMessages();
                directMessages.setOnTweetDetailListener(onTweetDetailListener);
                return directMessages;
            default:
                throw new UnsupportedOperationException("The maximum number of tabs is three.");
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}