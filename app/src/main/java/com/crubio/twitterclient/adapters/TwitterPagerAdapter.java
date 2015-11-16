package com.crubio.twitterclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.crubio.twitterclient.fragment.GenericTweetsList;
import com.crubio.twitterclient.fragment.MentionsList;
import com.crubio.twitterclient.fragment.TimelineList;

public class TwitterPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home", "Mentions" };
    private GenericTweetsList.OnTweetDetailListener onTweetDetailListener;

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
                Log.i("TwitterPagerAdapter","TimelineList");
                f = new TimelineList();
                break;
            case 1:
                Log.i("TwitterPagerAdapter","MentionsList");
                f = new MentionsList();
                break;
            default:
                throw new UnsupportedOperationException("The maximum number of tabs is two.");
        }
        f.setOnTweetDetailListener(onTweetDetailListener);
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}