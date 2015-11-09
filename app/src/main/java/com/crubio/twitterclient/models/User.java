package com.crubio.twitterclient.models;

import android.util.Log;

import com.activeandroid.annotation.Column;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private static final String CLASS = User.class.getSimpleName();

    @Column(name = "userProfileImage")
    String userProfileImage;

    public User() {
        super();
    }

    public User(JSONObject object) {
        super();
        try {
            this.userProfileImage = object.getString("profile_image_url");
        } catch (JSONException e) {
            Log.e(CLASS, "Error reading JSON", e);
        }
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "userProfileImage='" + userProfileImage + '\'' +
                '}';
    }
}
