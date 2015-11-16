package com.crubio.twitterclient.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.crubio.twitterclient.R;
import com.crubio.twitterclient.application.RestApplication;
import com.crubio.twitterclient.clients.TwitterClient;
import com.crubio.twitterclient.fragment.GenericTweetsList;
import com.crubio.twitterclient.fragment.TimelineList;
import com.crubio.twitterclient.fragment.UserTimelineList;
import com.crubio.twitterclient.models.Tweet;
import com.crubio.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class ProfileActivity extends BaseActivity {
    public static final String ID = "id";
    private static final String CLASS = ProfileActivity.class.getSimpleName();

    private User user;
    private ImageView profileImage;
    private ImageView imageBackground;
    private TextView username;
    private TextView userTextView;
    private TextView numFollowing;
    private TextView numFollowers;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = (ImageView) findViewById(R.id.iv_profile_image);
        imageBackground = (ImageView) findViewById(R.id.iv_background_image);
        username = (TextView) findViewById(R.id.tv_username);
        userTextView = (TextView) findViewById(R.id.tv_user);
        numFollowing = (TextView) findViewById(R.id.tv_num_following);
        numFollowers = (TextView) findViewById(R.id.tv_num_followers);
        Bundle b = getIntent().getExtras();
        id = b.getString(ID);
        getUserInformation(id);

        toolbarCreation();
        setHomeButtonVisibility(true);
        switchToTimelineFragment();
    }


    private void getUserInformation(String id){
        if(isNetworkAvailable()) {
            TwitterClient client = RestApplication.getRestClient();

            client.getUserInfo(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                    user = new User(jsonObject);
                    Log.i(CLASS, "user: " + user);
                    Picasso.with(ProfileActivity.this)
                            .load(user.getProfileImageUrl())
                            .into(profileImage);
                    final int color = Color.parseColor("#" + user.getBackgroundColor());
                    imageBackground.setBackgroundColor(color);
                    toolbar.setBackgroundColor(color);
                    username.setText(user.getUserName());
                    userTextView.setText(user.getUser());
                    numFollowers.setText(user.getFollowersCount());
                    numFollowing.setText(user.getFriendsCount());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
                    Log.e(CLASS, "profile error: " + jsonObject);
                }
            });
        }
    }

    @Override
    protected void openProfile() {
        //We are in the profile no need to start activity
    }

    @Override
    protected void refresh() {
        //TODO
    }

    private void switchToTimelineFragment(){
        GenericTweetsList f = UserTimelineList.newInstance(id);
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

}