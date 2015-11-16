package com.crubio.twitterclient.models;

import android.util.Log;

import com.activeandroid.annotation.Column;

import org.json.JSONException;
import org.json.JSONObject;

public class UserStatus {
    private static final String CLASS = UserStatus.class.getSimpleName();

    @Column(name = "id")
    String id;

    @Column(name = "userProfileImage")
    String userProfileImage;

    public UserStatus() {
        super();
    }

    public UserStatus(JSONObject object) {
        super();
        Log.i(CLASS, ("" + object).substring(0, ("" + object).length()/2));
        Log.i(CLASS, ("" + object).substring(("" + object).length()/2));
        try {
            this.userProfileImage = object.getString("profile_image_url");
            this.id = object.getString("id_str");
        } catch (JSONException e) {
            Log.e(CLASS, "Error reading JSON", e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "id='" + id + '\'' +
                ", userProfileImage='" + userProfileImage + '\'' +
                '}';
    }
}
