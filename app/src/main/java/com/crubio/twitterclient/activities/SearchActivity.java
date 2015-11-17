package com.crubio.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.fragment.GenericTweetsList;
import com.crubio.twitterclient.fragment.SearchFragment;

/**
 * Created by carlos on 11/16/2015.
 */
public class SearchActivity extends BaseActivity{

    public static final java.lang.String QUERY = "QUERY";
    private String query;
    private SearchFragment f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle b = getIntent().getExtras();
        query = b.getString(QUERY);
        switchToSearchFragment(query);
        toolbarCreation();
        setHomeButtonVisibility(true);
    }

    private void switchToSearchFragment(String id){
        f = SearchFragment.newInstance(id);
        f.setOnTweetDetailListener(this);
        switchFragment(f);
    }

    private void switchFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void refresh() {
        f.refresh();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
